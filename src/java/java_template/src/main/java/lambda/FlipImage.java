package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import saaf.Inspector;
import saaf.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;

/**
 * uwt.lambda_test::handleRequest
 *
 * @author Wes Lloyd
 * @author Robert Cordingly
 */
public class FlipImage implements RequestHandler<Request, HashMap<String, Object>> {

    /**
     * Lambda Function Handler
     * 
     * @param request Request POJO with defined variables from Request.java
     * @param context 
     * @return HashMap that Lambda will automatically convert into JSON.
     */
    public HashMap<String, Object> handleRequest(Request request, Context context) {
        //Collect inital data.
        Inspector inspector = new Inspector();
        inspector.inspectAll();
        
        //****************START FUNCTION IMPLEMENTATION*************************

        var bucketname = request.getBucketname();
        var originalFilename = request.getOriginalfilename();
        var newFilename = request.getNewfilename();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        var s3Object = s3Client.getObject(bucketname, originalFilename);
        var objectData = s3Object.getObjectContent();
        BufferedImage image;
        try {
            image = ImageIO.read(objectData);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        var newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g = newImage.createGraphics();
        g.drawImage(image, image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);
        g.dispose();
        var os = new ByteArrayOutputStream();
        try {
            ImageIO.write(newImage, "png", os);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        var meta = new ObjectMetadata();
        meta.setContentType("image/png");
        meta.setContentLength(os.size());

        s3Client.putObject(bucketname, newFilename, new ByteArrayInputStream(os.toByteArray()), meta);

        Response response = new Response();
        response.setValue("Bucket: %s, filename: %s, processed.".formatted(bucketname, originalFilename));
        
        inspector.consumeResponse(response);
        
        //****************END FUNCTION IMPLEMENTATION***************************
        
        //Collect final information such as total runtime and cpu deltas.
        inspector.inspectAllDeltas();
        return inspector.finish();
    }
}

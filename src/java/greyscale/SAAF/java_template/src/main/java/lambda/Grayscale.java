package lambda;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import saaf.Inspector;

/**
 * uwt.lambda_test::handleRequest
 *
 * @author Wes Lloyd
 * @author Robert Cordingly
 */
public class Grayscale implements RequestHandler<Request, HashMap<String, Object>> {

    /**
     * Lambda Function Handler
     * 
     * @param request Request POJO with defined variables from Request.java
     * @param context
     * @return HashMap that Lambda will automatically convert into JSON.
     */
    public HashMap<String, Object> handleRequest(Request request, Context context) {

        // Collect inital data.
        Inspector inspector = new Inspector();
        inspector.inspectAll();

        // ****************START FUNCTION IMPLEMENTATION*************************
        // Add custom key/value attribute to SAAF's output. (OPTIONAL)
        // inspector.addAttribute("message", "Hello " + request.getName()
        // + "! This is an attributed added to the Inspector!");

        // //Create and populate a separate response object for function output.
        // (OPTIONAL)
        // Response response = new Response();
        // response.setValue("Hello " + request.getNameALLCAPS()
        // + "! This is from a response object!");

        // inspector.consumeResponse(response);

        // adapted from
        // https://github.com/AleksanderGrzybowski/aws-lambda-grayscale-converter

        String inputBase64 = Request.getImage();

        try {
            String output = process(inputBase64);
            Response response = new Response(output);
            inspector.consumeResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // return picture in response

        // ****************END FUNCTION IMPLEMENTATION***************************

        // Collect final information such as total runtime and cpu deltas.
        inspector.inspectAllDeltas();
        return inspector.finish();
    }

    private String process(String inputBase64) throws Exception {
        BufferedImage input = decodeImageBase64(inputBase64);
        BufferedImage result = convertToGrayscale(input);

        return encodeImageBase64(result);
    }

    private BufferedImage decodeImageBase64(String input64) throws Exception {
        byte[] sourceData = Base64.getDecoder().decode(input64);

        return ImageIO.read(new ByteArrayInputStream(sourceData));
    }

    private String encodeImageBase64(BufferedImage image) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        ImageIO.write(image, IMAGE_TYPE, stream);
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

    private BufferedImage convertToGrayscale(BufferedImage image) {
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(colorSpace, null);
        return op.filter(image, null);
    }
}

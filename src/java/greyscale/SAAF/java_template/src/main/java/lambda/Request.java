package lambda;

/**
 *
 * @author Wes Lloyd
 */
public class Request {

    String image;

    public Request(String image) {
        this.image = image;
    }

    public Request() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

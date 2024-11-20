package lambda;

/**
 *
 * @author Wes Lloyd
 */
public class Request {

    private String bucketname;
    private String originalfilename;
    private String newfilename;

    String name;

    public String getName() {
        return name;
    }
    
    public String getNameALLCAPS() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request(String name) {
        this.name = name;
    }

    public Request() {

    }

    public java.lang.String getBucketname() {
        return bucketname;
    }

    public void setBucketname(java.lang.String bucketname) {
        this.bucketname = bucketname;
    }

    public java.lang.String getOriginalfilename() {
        return originalfilename;
    }

    public void setOriginalfilename(java.lang.String originalFilename) {
        this.originalfilename = originalFilename;
    }

    public String getNewfilename() {
        return newfilename;
    }

    public void setNewfilename(String newFilename) {
        this.newfilename = newFilename;
    }
}

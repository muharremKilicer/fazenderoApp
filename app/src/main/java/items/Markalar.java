package items;

/**
 * Created by mkatr on 3.7.2016.
 */
public class Markalar {

    String productId,productName,images;

    public Markalar(String productId, String productName, String images) {
        this.productId = productId;
        this.productName = productName;
        this.images = images;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}

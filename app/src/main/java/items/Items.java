package items;

/**
 * Created by mkatr on 4.7.2016.
 */
public class Items {

    String productId,productName,price,images;

    public Items(String productId, String productName, String price, String images) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}

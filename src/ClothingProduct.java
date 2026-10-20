public class ClothingProduct extends Product {
    private static final long serialVersionUID = 1L;

    private String size;

    public ClothingProduct(String id, String name, double price, String size) {
        super(id, name, price);
        this.size = size;
    }

    @Override
    public String getDetails() {
        return "[Clothing] " + id + ", " + name + ", Rs." + price + ", Size: " + size;
    }
}

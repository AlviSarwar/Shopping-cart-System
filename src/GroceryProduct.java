public class GroceryProduct extends Product {
    private static final long serialVersionUID = 1L;

    private double weight;

    public GroceryProduct(String id, String name, double price, double weight) {
        super(id, name, price);
        this.weight = weight;
    }

    @Override
    public String getDetails() {
        return "[Grocery] " + id + ", " + name + ", Rs." + price + ", " + weight + "kg";
    }
}

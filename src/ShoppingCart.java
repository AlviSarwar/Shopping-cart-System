import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements CartOperations {
    private List<Product> cart;

    public ShoppingCart() {
        cart = new ArrayList<>();
    }

    public void addItem(Product product) {
        cart.add(product);
        System.out.println(product.getId() + " added to cart.");
    }

    public void removeItem(String productId) {
        boolean removed = cart.removeIf(p -> p.getId().equals(productId));
        System.out.println(productId + (removed ? " removed from cart." : " not found in cart."));
    }

    public void displayCart() {
        System.out.println("\nItems in Cart:");
        for (Product p : cart) {
            System.out.println(p.getDetails());
        }
    }

    public double calculateTotal() {
        return cart.stream().mapToDouble(Product::getPrice).sum();
    }

    public List<Product> getCartItems() {
        return cart;
    }
}

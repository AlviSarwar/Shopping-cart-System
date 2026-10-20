public interface CartOperations {
    void addItem(Product product);
    void removeItem(String productId);
    void displayCart();
    double calculateTotal();
}

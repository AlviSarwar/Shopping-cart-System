import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExportCartThread extends Thread {
    private ShoppingCart cart;
    private String filename;

    public ExportCartThread(ShoppingCart cart, String filename) {
        this.cart = cart;
        this.filename = filename;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : cart.getCartItems()) {
                writer.write(product.getDetails());
                writer.newLine();
            }
            writer.write("Total: Rs." + cart.calculateTotal());
            System.out.println("\nCart exported to " + filename);
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }
}

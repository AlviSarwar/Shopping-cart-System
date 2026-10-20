import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/** Dependency-free tests. Run: java -cp out ShoppingCartTest (exits 1 on any failure). */
public class ShoppingCartTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US); // Scanner parses numbers using the default locale

        test("empty cart totals zero", () -> {
            check(new ShoppingCart().calculateTotal() == 0.0, "expected 0.0");
        });
        test("total is the sum of item prices", () -> {
            ShoppingCart cart = new ShoppingCart();
            cart.addItem(new GroceryProduct("G1", "rice", 50, 2));
            cart.addItem(new ElectronicProduct("E1", "phone", 1200, 12));
            cart.addItem(new ClothingProduct("C1", "shirt", 120, "XL"));
            check(cart.calculateTotal() == 1370.0, "expected 1370.0 but was " + cart.calculateTotal());
        });
        test("removeItem removes only the matching ID", () -> {
            ShoppingCart cart = new ShoppingCart();
            cart.addItem(new GroceryProduct("G1", "rice", 50, 2));
            cart.addItem(new GroceryProduct("G2", "oil", 90, 1));
            cart.removeItem("G1");
            check(cart.getCartItems().size() == 1, "expected 1 item left");
            check(cart.getCartItems().get(0).getId().equals("G2"), "wrong item left");
        });
        test("getDetails formats each product type", () -> {
            check(new GroceryProduct("G1", "rice", 50, 2).getDetails().equals("[Grocery] G1, rice, Rs.50.0, 2.0kg"), "grocery");
            check(new ElectronicProduct("E1", "phone", 1200, 12).getDetails().equals("[Electronic] E1, phone, Rs.1200.0, Warranty: 12 months"), "electronic");
            check(new ClothingProduct("C1", "shirt", 120, "XL").getDetails().equals("[Clothing] C1, shirt, Rs.120.0, Size: XL"), "clothing");
        });
        test("ExportCartThread writes each item and the total", () -> {
            ShoppingCart cart = new ShoppingCart();
            cart.addItem(new GroceryProduct("G1", "rice", 50, 2));
            Path file = Files.createTempFile("cart", ".txt");
            try {
                ExportCartThread t = new ExportCartThread(cart, file.toString());
                t.start();
                t.join();
                List<String> lines = Files.readAllLines(file);
                check(lines.equals(Arrays.asList("[Grocery] G1, rice, Rs.50.0, 2.0kg", "Total: Rs.50.0")), "unexpected file: " + lines);
            } finally {
                Files.deleteIfExists(file);
            }
        });

        // Menu behaviour with scripted input
        test("menu: add then view shows item and total", () -> {
            String out = runMenu("1\nG1 rice 50 2\n5\n7\n");
            check(out.contains("G1 added to cart."), "no add message");
            check(out.contains("Total: Rs.50.0"), "wrong total");
        });
        test("menu: non-numeric choice does not crash", () -> {
            String out = runMenu("abc\n7\n");
            check(out.contains("Invalid option. Try again."), "no invalid-option message");
            check(out.contains("Exiting Shopping Cart System."), "did not exit cleanly");
        });
        test("menu: bad price is rejected and the menu keeps working", () -> {
            String out = runMenu("1\nG1 rice abc 2\n5\n7\n");
            check(out.contains("Error:"), "no error message");
            check(out.contains("Total: Rs.0.0"), "item should not have been added");
            check(out.contains("Exiting Shopping Cart System."), "did not exit cleanly");
        });
        test("menu: negative price is rejected", () -> {
            String out = runMenu("1\nG1 rice -5 2\n5\n7\n");
            check(out.contains("Price must be a non-negative number."), "no validation message");
            check(out.contains("Total: Rs.0.0"), "negative item was added");
        });
        test("menu: input ending without Exit leaves cleanly", () -> {
            check(runMenu("5\n").contains("Exiting Shopping Cart System."), "did not exit cleanly");
        });
        test("menu: removing a missing ID says so", () -> {
            check(runMenu("4\nZZZ\n7\n").contains("ZZZ not found in cart."), "wrong message");
        });

        System.out.println("\n" + passed + " passed, " + failed + " failed");
        System.exit(failed == 0 ? 0 : 1);
    }

    private interface Body { void run() throws Exception; }

    private static void test(String name, Body body) {
        try {
            body.run();
            passed++;
            System.out.println("PASS  " + name);
        } catch (Throwable t) {
            failed++;
            System.out.println("FAIL  " + name + " -> " + t);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /** Runs the real menu against scripted input and returns everything it printed. */
    private static String runMenu(String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            PrintStream capture = new PrintStream(buffer, true);
            System.setOut(capture);
            System.setErr(capture);
            new UserMenu().start();
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
            System.setErr(oldErr);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }
}

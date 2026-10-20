import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserMenu {
    private ShoppingCart cart;
    private Scanner scanner;

    public UserMenu() {
        cart = new ShoppingCart();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Shopping Cart Menu ---");
            System.out.println("1. Add Grocery Product");
            System.out.println("2. Add Electronic Product");
            System.out.println("3. Add Clothing Product");
            System.out.println("4. Remove Product by ID");
            System.out.println("5. View Cart");
            System.out.println("6. Export Cart to File");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNext()) {
                break; // input ended (for example, piped input) - leave cleanly
            }
            if (!scanner.hasNextInt()) {
                skipRestOfLine();
                System.out.println("Invalid option. Try again.");
                continue;
            }
            int choice = scanner.nextInt();
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // consume newline
            }

            try {
                switch (choice) {
                    case 1: {
                        System.out.print("Enter ID, Name, Price, Weight (kg): ");
                        String id = scanner.next();
                        String name = scanner.next();
                        double price = scanner.nextDouble();
                        double weight = scanner.nextDouble();
                        requireNonNegative("Price", price);
                        requireNonNegative("Weight", weight);
                        Product g = new GroceryProduct(id, name, price, weight);
                        cart.addItem(g);
                        break;
                    }
                    case 2: {
                        System.out.print("Enter ID, Name, Price, Warranty (months): ");
                        String id = scanner.next();
                        String name = scanner.next();
                        double price = scanner.nextDouble();
                        int warranty = scanner.nextInt();
                        requireNonNegative("Price", price);
                        requireNonNegative("Warranty", warranty);
                        Product e = new ElectronicProduct(id, name, price, warranty);
                        cart.addItem(e);
                        break;
                    }
                    case 3: {
                        System.out.print("Enter ID, Name, Price, Size: ");
                        String id = scanner.next();
                        String name = scanner.next();
                        double price = scanner.nextDouble();
                        String size = scanner.next();
                        requireNonNegative("Price", price);
                        Product c = new ClothingProduct(id, name, price, size);
                        cart.addItem(c);
                        break;
                    }
                    case 4:
                        System.out.print("Enter Product ID to remove: ");
                        cart.removeItem(scanner.next());
                        break;
                    case 5:
                        cart.displayCart();
                        System.out.println("Total: Rs." + cart.calculateTotal());
                        break;
                    case 6:
                        ExportCartThread thread = new ExportCartThread(cart, "cart_summary.txt");
                        thread.start();
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (InputMismatchException ex) {
                // Must be caught before NoSuchElementException (its superclass).
                System.err.println("Error: expected a number but got something else.");
                skipRestOfLine();
            } catch (NoSuchElementException ex) {
                System.err.println("Error: input ended before the product details were complete.");
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
        scanner.close();
        System.out.println("Exiting Shopping Cart System.");
    }

    /** Throws InvalidProductException for negative or non-finite values. */
    private static void requireNonNegative(String field, double value) throws InvalidProductException {
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
            throw new InvalidProductException(field + " must be a non-negative number.");
        }
    }

    /** Discards the bad token and whatever else is left on the current line. */
    private void skipRestOfLine() {
        if (scanner.hasNext()) {
            scanner.next();
        }
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}

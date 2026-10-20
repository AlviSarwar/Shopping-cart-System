# Shopping Cart System

A menu-driven Java console application that models a shopping cart with grocery, electronic and clothing products. It was written to practise core object-oriented design: abstraction, inheritance, polymorphism, interfaces, custom exceptions and threads.

## Features

- Add grocery, electronic and clothing products, each with its own details.
- Remove a product by ID and view the cart with a running total.
- Export the cart to a text file on a background thread.
- Input validation: bad numbers, negative values and unexpected end of input are reported instead of crashing the program.

## Object-oriented design

| Concept | Where |
|---|---|
| Abstraction | `Product` is an abstract base class with an abstract `getDetails()` |
| Inheritance and polymorphism | `GroceryProduct`, `ElectronicProduct` and `ClothingProduct` extend `Product` and each format their own details |
| Interface | `CartOperations` defines the cart contract; `ShoppingCart` implements it |
| Custom exception | `InvalidProductException` is thrown for negative or non-finite values |
| Threads | `ExportCartThread` writes the cart to a file in the background |

## Getting started

Requires a JDK, Java 8 or newer. The code compiles with `javac --release 8` and was tested on JDK 21.

macOS, Linux or Git Bash:

```bash
javac -d out src/*.java
java -cp out ShoppingCartSystem
```

Windows PowerShell:

```powershell
javac -d out (Get-ChildItem src\*.java)
java -cp out ShoppingCartSystem
```

## Usage

Enter the details for each product on one line, separated by spaces. Names must be a single word.

| Option | Input after choosing it |
|---|---|
| 1 Add Grocery Product | `ID Name Price Weight(kg)` |
| 2 Add Electronic Product | `ID Name Price Warranty(months)` |
| 3 Add Clothing Product | `ID Name Price Size` |
| 4 Remove Product by ID | `ID` |
| 5 View Cart | none |
| 6 Export Cart to File | none (writes `cart_summary.txt` in the current folder) |
| 7 Exit | none |

Example session (abridged: menu lines are omitted and typed input follows each prompt):

```text
Choose an option: 1
Enter ID, Name, Price, Weight (kg): G123 alu 20 1.5
G123 added to cart.
Choose an option: 2
Enter ID, Name, Price, Warranty (months): T131 telephone 1200 12
T131 added to cart.
Choose an option: 3
Enter ID, Name, Price, Size: S151 shirt 120 XL
S151 added to cart.
Choose an option: 5

Items in Cart:
[Grocery] G123, alu, Rs.20.0, 1.5kg
[Electronic] T131, telephone, Rs.1200.0, Warranty: 12 months
[Clothing] S151, shirt, Rs.120.0, Size: XL
Total: Rs.1340.0
Choose an option: 4
Enter Product ID to remove: T131
T131 removed from cart.
```

## Tests

The tests have no external dependencies. They cover the cart logic, the export thread and the menu's handling of bad input.

macOS, Linux or Git Bash:

```bash
javac -d out src/*.java test/*.java
java -cp out ShoppingCartTest
```

Windows PowerShell:

```powershell
javac -d out (Get-ChildItem src\*.java) (Get-ChildItem test\*.java)
java -cp out ShoppingCartTest
```

## Project structure

```text
src/    application source (default package)
test/   ShoppingCartTest.java, a dependency-free test runner
SHopping Cart SYS.pdf          project report: class diagram and source listing
Shopping Cart System.zip       the original Eclipse project archive
.github/workflows/ci.yml       compiles with warnings as errors, then runs the tests
```

## Known limitations

- Product names must be a single word, because input is read token by token.
- `ExportCartThread` reads the live cart without synchronisation, so changing the cart while an export is running is not safe.
- "Cart exported" may print after the next menu prompt because the export runs on its own thread.
- Prices are `double`, which is fine for a coursework demo but not for real money.

## Changes since the original submission

The PDF report shows the version that was first submitted. Since then:

- Source files were extracted from the zip into `src/`.
- Invalid input no longer crashes the menu, and `InvalidProductException` is now used to reject negative prices, weights and warranty periods.
- Removing an ID that is not in the cart now says so instead of reporting a removal.
- Added `serialVersionUID` to the serializable classes, tests, CI and this README.

## Credits

Team project for Object-Oriented Programming 2 (course code 202) at the University of Asia Pacific, built by a team of three students.

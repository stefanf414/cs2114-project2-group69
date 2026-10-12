import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import student.TestCase;

/**
 * Provides the console user interface for the ice-cream builder application.
 *
 * <p>This class handles user interaction and delegates order operations to
 * @link IceCreamOrder, @link Topping, and @link OrderHistory. It does
 * not implement storage itself.</p>
 */

public class UserInterface {
    /** Represents a menu choice and its price. */
    private record MenuItem(String name, double price) {}

    /** Available ice-cream flavors. */
    private static final List<MenuItem> FLAVORS = List.of(
            new MenuItem("Vanilla", 3.00),
            new MenuItem("Chocolate", 3.25),
            new MenuItem("Strawberry", 3.50));

    /** Available toppings. */
    private static final List<MenuItem> TOPPINGS = List.of(
            new MenuItem("Sprinkles", 0.50),
            new MenuItem("Oreo", 0.75),
            new MenuItem("Brownie", 1.00),
            new MenuItem("Caramel", 0.65));

    /** Input source used by the interface. */
    private final Scanner scanner;

    /** Storage for submitted orders. */
    private final OrderHistory history;

    /**
     * Creates a user interface with the provided input source and order history.
     *
     * @param scanner scanner used to read user input
     * @param history history used to store completed orders
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public UserInterface(Scanner scanner, OrderHistory history) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        if (history == null) {
            throw new IllegalArgumentException("Order history cannot be null.");
        }
        this.scanner = scanner;
        this.history = history;
    }

    /** Runs the main menu until the user chooses to exit. */
    public void run() {
        System.out.println("Ice Cream Builder");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Build an order\n2. View completed orders\n3. Exit");
            int choice = readInt("Choose an option: ", 1, 3);
            switch (choice) {
                case 1 -> buildOrder();
                case 2 -> System.out.println(history.display());
                case 3 -> running = false;
                default -> throw new AssertionError("Validated menu choice");
            }
        }
        System.out.println("Goodbye!");
    }

    /** Guides the user through selecting a flavor and editing an order. */
    private void buildOrder() {
        System.out.println("\nChoose a flavor:");
        printMenu(FLAVORS);
        MenuItem flavor = FLAVORS.get(readInt("Flavor: ", 1, FLAVORS.size()) - 1);
        IceCreamOrder order = new IceCreamOrder(flavor.name(), flavor.price());

        boolean editing = true;
        while (editing) {
            System.out.println("\n1. Add a topping\n2. Remove a topping\n0. Finish order");
            int action = readInt("Choose an action: ", 0, 2);
            if (action == 1) {
                addTopping(order);
            } else if (action == 2) {
                removeTopping(order);
            } else {
                editing = false;
            }
        }

        System.out.println("\n" + order.display());
        if (readYesNo("Submit this order? (y/n): ")) {
            history.submit(order);
            System.out.println("Order submitted.");
        } else {
            System.out.println("Order discarded.");
        }
    }

    /** Adds one selected topping to an order. */
    private void addTopping(IceCreamOrder order) {
        System.out.println("\nChoose a topping:");
        printMenu(TOPPINGS);
        MenuItem topping = TOPPINGS.get(readInt("Topping: ", 1, TOPPINGS.size()) - 1);
        order.addTopping(new Topping(topping.name(), topping.price()));
        System.out.println(topping.name() + " added.");
    }

    /** Removes a user-selected topping, if the order contains one. */
    private void removeTopping(IceCreamOrder order) {
        if (order.toppingCount() == 0) {
            System.out.println("No toppings are currently in this order.");
            return;
        }

        System.out.println("\nChoose a topping to remove:");
        for (int i = 0; i < order.toppingCount(); i++) {
            System.out.printf("%d. %s%n", i + 1, order.getTopping(i));
        }

        int index = readInt("Topping: ", 1, order.toppingCount()) - 1;
        Topping topping = order.getTopping(index);
        if (order.removeTopping(topping)) {
            System.out.println(topping.getName() + " removed.");
        } else {
            System.out.println("Topping was not found; the order was unchanged.");
        }
    }

    /** Reads and validates an integer in the specified inclusive range. */
    private int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return maximum;
            }

            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // The message below handles nonnumeric input.
            }
            System.out.println("Invalid selection. Please enter a number from "
                    + minimum + " to " + maximum + ".");
        }
    }

    /** Reads a yes/no response and repeats until the response is valid. */
    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return false;
            }

            String input = scanner.nextLine().trim().toLowerCase(Locale.US);
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }

    /** Prints numbered menu items with prices. */
    private static void printMenu(List<MenuItem> items) {
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf(Locale.US, "%d. %s ($%.2f)%n",
                    i + 1, item.name(), item.price());
        }
    }

    /** Starts the application using standard input and a new order history. */
    public static void main(String[] args) {
        new UserInterface(new Scanner(System.in), new OrderHistory()).run();
    }
}
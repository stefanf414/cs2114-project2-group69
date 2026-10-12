import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Provides the console interface for the ice-cream builder application. */
public class UserInterface {
    /** One selectable menu item with a name and price. */
    private static class MenuItem {
        private final String name;
        private final double price;

        MenuItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        String name() {
            return name;
        }

        double price() {
            return price;
        }
    }

    private static final List<MenuItem> FLAVORS = Arrays.asList(
            new MenuItem("Vanilla", 3.00),
            new MenuItem("Chocolate", 3.25),
            new MenuItem("Strawberry", 3.50));

    private static final List<MenuItem> TOPPINGS = Arrays.asList(
            new MenuItem("Sprinkles", 0.50),
            new MenuItem("Oreo", 0.75),
            new MenuItem("Brownie", 1.00),
            new MenuItem("Caramel", 0.65));

    private final Scanner scanner;
    private final OrderHistory history;

    /** Creates an interface with an input scanner and shared order history. */
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

    /** Runs the main menu until the user exits. */
    public void run() {
        System.out.println("Ice Cream Builder");
        boolean running = true;
        while (running) {
            System.out.println(
                    "\n1. Build an order\n2. View completed orders\n3. Exit");
            int choice = readInt("Choose an option: ", 1, 3);
            switch (choice) {
                case 1:
                    buildOrder();
                    break;
                case 2:
                    System.out.println(history.display());
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    throw new AssertionError("Validated menu choice");
            }
        }
        System.out.println("Goodbye!");
    }

    /** Builds an order by selecting a flavor and editing toppings. */
    private void buildOrder() {
        System.out.println("\nChoose a flavor:");
        printMenu(FLAVORS);
        MenuItem flavor = FLAVORS.get(
                readInt("Flavor: ", 1, FLAVORS.size()) - 1);
        IceCreamOrder order = new IceCreamOrder(
                flavor.name(), flavor.price());

        boolean editing = true;
        while (editing) {
            System.out.println(
                    "\n1. Add a topping\n2. Remove a topping\n0. Finish order");
            int action = readInt("Choose an action: ", 0, 2);
            if (action == 1) {
                addTopping(order);
            }
            else if (action == 2) {
                removeTopping(order);
            }
            else {
                editing = false;
            }
        }

        System.out.println("\n" + order.display());
        if (readYesNo("Submit this order? (y/n): ")) {
            history.submit(order);
            System.out.println("Order submitted.");
        }
        else {
            System.out.println("Order discarded.");
        }
    }

    /** Adds one selected topping. */
    private void addTopping(IceCreamOrder order) {
        System.out.println("\nChoose a topping:");
        printMenu(TOPPINGS);
        MenuItem item = TOPPINGS.get(
                readInt("Topping: ", 1, TOPPINGS.size()) - 1);
        order.addTopping(new Topping(item.name(), item.price()));
        System.out.println(item.name() + " added.");
    }

    /** Removes one selected topping. */
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
        }
        else {
            System.out.println(
                    "Topping was not found; the order was unchanged.");
        }
    }

    /** Reads an integer within an inclusive range. */
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
            }
            catch (NumberFormatException ignored) {
                // The common error message below handles invalid text.
            }
            System.out.println("Invalid selection. Please enter a number from "
                    + minimum + " to " + maximum + ".");
        }
    }

    /** Reads a yes-or-no response. */
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

    /** Prints a numbered menu. */
    private static void printMenu(List<MenuItem> items) {
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf(Locale.US, "%d. %s ($%.2f)%n",
                    i + 1, item.name(), item.price());
        }
    }

    /** Starts the application. */
    public static void main(String[] args) {
        new UserInterface(new Scanner(System.in), new OrderHistory()).run();
    }
}

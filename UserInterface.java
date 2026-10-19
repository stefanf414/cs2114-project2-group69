import java.util.Locale;
import java.util.Scanner;

/** Handles all console input and output for the Ice Cream Builder. */
public class UserInterface {
    private final Scanner input;
    private final Menu menu;
    private final OrderListAssembler assembler;
    private final OrderStats stats;

    /** Creates a specification-based user interface. */
    public UserInterface(Menu menu, OrderListAssembler assembler,
            OrderStats stats, Scanner input) {
        if (menu == null || assembler == null || stats == null
                || input == null) {
            throw new IllegalArgumentException("UI collaborators cannot be null.");
        }
        this.menu = menu;
        this.assembler = assembler;
        this.stats = stats;
        this.input = input;
    }

    /** Creates a compatibility UI using an OrderHistory wrapper. */
    public UserInterface(Scanner input, OrderHistory history) {
        this(new Menu(),
                new OrderListAssembler(requireHistory(history).getChain()),
                new OrderStats(history.getChain()), input);
    }

    /** Runs the main menu until the user chooses quit. */
    public void run() {
        boolean running = true;
        System.out.println("Ice Cream Builder");
        while (running) {
            System.out.println("\n1. New order");
            System.out.println("2. View history");
            System.out.println("3. View statistics");
            System.out.println("4. Quit");
            int choice = readMenuChoice("Choose an option: ", 1, 4);
            switch (choice) {
                case 1:
                    buildOrder();
                    break;
                case 2:
                    showHistory();
                    break;
                case 3:
                    showStats();
                    break;
                default:
                    running = false;
                    break;
            }
        }
        System.out.println("Goodbye!");
    }

    /** Reads an integer within an inclusive range, retrying invalid input. */
    public int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextLine()) {
                return max;
            }
            String text = input.nextLine().trim();
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Print the common validation message below.
            }
            System.out.println("Invalid selection. Please enter a number from "
                    + min + " to " + max + ".");
        }
    }

    /** Displays the current order or an empty message. */
    public void showCurrentOrder() {
        if (!assembler.hasCurrentOrder()) {
            System.out.println("Your order is empty.");
        } else {
            System.out.println(assembler.getCurrentOrder());
        }
    }

    /** Displays completed orders, newest first. */
    public void showHistory() {
        IceCreamOrder[] orders = assembler.getHistoryArray();
        if (orders.length == 0) {
            System.out.println("No orders yet.");
            return;
        }
        System.out.println("Completed orders:");
        for (int i = 0; i < orders.length; i++) {
            System.out.println((i + 1) + ".");
            System.out.println(orders[i]);
        }
    }

    /** Displays the statistics report. */
    public void showStats() {
        System.out.println(stats.getSummary());
    }

    private void buildOrder() {
        System.out.println("\nChoose a cone or container:");
        printCones();
        Cone cone = menu.getCone(readMenuChoice("Cone: ", 1,
                menu.getCones().length) - 1);

        System.out.println("\nChoose a flavor:");
        printFlavors();
        Flavor flavor = menu.getFlavor(readMenuChoice("Flavor: ", 1,
                menu.getFlavors().length) - 1);

        assembler.startOrder(cone, flavor);
        System.out.println("Recommended topping: "
                + menu.getRecommendedTopping(flavor));

        boolean editing = true;
        while (editing) {
            System.out.println("\n1. Add a topping");
            System.out.println("2. Remove a topping");
            System.out.println("3. View current order");
            System.out.println("4. Submit order");
            System.out.println("5. Cancel order");
            int action = readMenuChoice("Choose an action: ", 1, 5);
            switch (action) {
                case 1:
                    addTopping();
                    break;
                case 2:
                    removeTopping();
                    break;
                case 3:
                    showCurrentOrder();
                    break;
                case 4:
                    showCurrentOrder();
                    assembler.submitOrder();
                    System.out.println("Order submitted.");
                    editing = false;
                    break;
                default:
                    assembler.cancelOrder();
                    System.out.println("Order canceled.");
                    editing = false;
                    break;
            }
        }
    }

    private void addTopping() {
        printToppings();
        Topping topping = menu.getTopping(readMenuChoice("Topping: ", 1,
                menu.getToppings().length) - 1);
        assembler.addTopping(topping);
        System.out.println(topping.getName() + " added.");
    }

    private void removeTopping() {
        if (!assembler.hasCurrentOrder()
                || assembler.getCurrentOrder().getToppingCount() == 0) {
            System.out.println("No toppings are currently in this order.");
            return;
        }
        Topping[] current = assembler.getCurrentOrder().getToppingArray();
        for (int i = 0; i < current.length; i++) {
            System.out.println((i + 1) + ". " + current[i]);
        }
        Topping topping = current[readMenuChoice("Topping: ", 1,
                current.length) - 1];
        if (assembler.removeTopping(topping)) {
            System.out.println(topping.getName() + " removed.");
        } else {
            System.out.println("Topping not found; order unchanged.");
        }
    }

    private void printCones() {
        Cone[] choices = menu.getCones();
        for (int i = 0; i < choices.length; i++) {
            System.out.printf(Locale.US, "%d. %s%n", i + 1, choices[i]);
        }
    }

    private void printFlavors() {
        Flavor[] choices = menu.getFlavors();
        for (int i = 0; i < choices.length; i++) {
            System.out.printf(Locale.US, "%d. %s%n", i + 1, choices[i]);
        }
    }

    private void printToppings() {
        Topping[] choices = menu.getToppings();
        for (int i = 0; i < choices.length; i++) {
            System.out.printf(Locale.US, "%d. %s%n", i + 1, choices[i]);
        }
    }

    private static OrderHistory requireHistory(OrderHistory history) {
        if (history == null) {
            throw new IllegalArgumentException("Order history cannot be null.");
        }
        return history;
    }
}

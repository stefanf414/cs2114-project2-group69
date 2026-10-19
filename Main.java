import java.util.Scanner;

/** Starts the Ice Cream Builder console application. */
public class Main {
    /** Creates collaborators and starts the UI. */
    public static void main(String[] args) {
        Menu menu = new Menu();
        LinkedChain<IceCreamOrder> history =
                new LinkedChain<IceCreamOrder>();
        OrderListAssembler assembler = new OrderListAssembler(history);
        OrderStats stats = new OrderStats(history);
        UserInterface ui = new UserInterface(menu, assembler, stats,
                new Scanner(System.in));
        ui.run();
    }
}

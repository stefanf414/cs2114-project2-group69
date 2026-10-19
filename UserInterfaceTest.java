import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import student.TestCase;

/** Tests the latest console flow. */
public class UserInterfaceTest extends TestCase {
    public void testConeToppingsSubmissionHistoryAndStats() {
        String commands = String.join("\n",
                "1", "3", "2", "1", "1", "1", "2", "4",
                "2", "3", "4") + "\n";
        LinkedChain<IceCreamOrder> history =
                new LinkedChain<IceCreamOrder>();
        Menu menu = new Menu();
        UserInterface ui = new UserInterface(menu,
                new OrderListAssembler(menu, history),
                new OrderStats(history),
                new Scanner(new ByteArrayInputStream(
                        commands.getBytes(StandardCharsets.UTF_8))));
        String output = capture(ui);

        assertEquals(1, history.size());
        assertEquals("Waffle Cone",
                history.get(0).getCone().getType());
        assertEquals(2, history.get(0).getToppingCount());
        assertTrue(output.contains("Order submitted."));
        assertTrue(output.contains("Total orders: 1"));
    }

    public void testInvalidInputDoesNotCrash() {
        String commands = "abc\n\n9\n4\n";
        UserInterface ui = new UserInterface(new Menu(),
                new OrderListAssembler(new LinkedChain<IceCreamOrder>()),
                new OrderStats(new LinkedChain<IceCreamOrder>()),
                new Scanner(new ByteArrayInputStream(
                        commands.getBytes(StandardCharsets.UTF_8))));
        assertTrue(capture(ui).contains("Invalid selection."));
    }

    private static String capture(UserInterface ui) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream old = System.out;
        try {
            System.setOut(new PrintStream(bytes, true));
            ui.run();
            return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            System.setOut(old);
        }
    }
}

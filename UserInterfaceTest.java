import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import student.TestCase;

/** Tests the console behavior of UserInterface. */
public class UserInterfaceTest extends TestCase {
    /** Tests building, submitting, and displaying an order. */
    public void testNormalFlowBuildsAndSubmitsOrder() {
        String input = String.join("\n",
                "1", "2", "1", "2", "1", "1", "0", "y", "2", "3")
                + "\n";
        OrderHistory history = new OrderHistory();
        String output = runApp(input, history);
        assertEquals(1, history.size());
        assertEquals("Chocolate", history.getOrder(0).getFlavor());
        assertEquals(2, history.getOrder(0).toppingCount());
        assertEquals(4.50, history.getOrder(0).total(), 0.001);
        assertTrue(output.contains("Order submitted."));
        assertTrue(output.contains("Completed orders:"));
    }

    /** Tests rejection of text and out-of-range menu input. */
    public void testInvalidMenuInputIsRejected() {
        String output = runApp("text\n9\n3\n", new OrderHistory());
        assertTrue(output.contains("Invalid selection."));
        assertTrue(output.contains("Goodbye!"));
    }

    /** Tests selecting and removing a topping before submission. */
    public void testRemoveToppingFlowRemovesSelectedTopping() {
        String input = String.join("\n",
                "1", "1", "1", "1", "2", "1", "0", "n", "3") + "\n";
        String output = runApp(input, new OrderHistory());
        assertTrue(output.contains("Sprinkles removed."));
        assertTrue(output.contains("Total: $3.00"));
        assertTrue(output.contains("Order discarded."));
    }

    /** Tests the empty-toppings removal message. */
    public void testRemoveWhenNoToppingsExist() {
        String input = String.join("\n",
                "1", "1", "2", "0", "n", "3") + "\n";
        String output = runApp(input, new OrderHistory());
        assertTrue(output.contains(
                "No toppings are currently in this order."));
    }

    /** Tests constructor validation for both dependencies. */
    public void testConstructorRejectsNullDependencies() {
        try {
            new UserInterface(null, new OrderHistory());
            fail("A null scanner should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }

        Scanner scanner = new Scanner("");
        try {
            new UserInterface(scanner, null);
            fail("A null history should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
        finally {
            scanner.close();
        }
    }

    /** Runs the application with simulated input and captures its output. */
    private static String runApp(String input, OrderHistory history) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(captured, true));
            new UserInterface(scanner, history).run();
            return new String(captured.toByteArray(), StandardCharsets.UTF_8);
        }
        finally {
            System.setOut(original);
            scanner.close();
        }
    }
}

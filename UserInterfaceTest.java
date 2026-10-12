import student.TestCase;
/** Tests the console behavior of {@link UserInterface}. */
class UserInterfaceTest {
    /** Verifies that a user can build and submit an order with two toppings. */
    @Test
    void normalFlowBuildsAndSubmitsOrder() {
        String input = String.join("\n",
                "1", "2", "1", "2", "1", "1", "0", "y", "2", "3") + "\n";
        OrderHistory history = new OrderHistory();
        String output = runApp(input, history);

        assertEquals(1, history.size());
        assertEquals("Chocolate", history.getOrder(0).getFlavor());
        assertEquals(2, history.getOrder(0).toppingCount());
        assertEquals(4.50, history.getOrder(0).total(), 0.001);
        assertTrue(output.contains("Order submitted."));
        assertTrue(output.contains("Completed orders:"));
    }

    /** Verifies that invalid menu input is rejected without crashing. */
    @Test
    void invalidMenuInputIsRejectedAndApplicationContinues() {
        String input = String.join("\n", "text", "9", "3") + "\n";
        OrderHistory history = new OrderHistory();
        String output = runApp(input, history);

        assertEquals(0, history.size());
        assertTrue(output.contains("Invalid selection."));
        assertTrue(output.contains("Goodbye!"));
    }

    /** Verifies that a selected topping can be removed before submission. */
    @Test
    void removeToppingFlowRemovesSelectedTopping() {
        String input = String.join("\n",
                "1", "1", "1", "1", "2", "1", "0", "n", "3") + "\n";
        OrderHistory history = new OrderHistory();
        String output = runApp(input, history);

        assertEquals(0, history.size());
        assertTrue(output.contains("Sprinkles removed."));
        assertTrue(output.contains("Total: $3.00"));
    }

    /** Verifies that null dependencies are rejected by the constructor. */
    @Test
    void constructorRejectsNullDependencies() {
        assertThrows(IllegalArgumentException.class,
                () -> new UserInterface(null, new OrderHistory()));
        assertThrows(IllegalArgumentException.class,
                () -> new UserInterface(new Scanner(""), null));
    }

    /** Runs the interface with scripted input and captures its console output. */
    private static String runApp(String input, OrderHistory history) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
            new UserInterface(scanner, history).run();
            return captured.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(original);
            scanner.close();
        }
    }
}
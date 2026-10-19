import student.TestCase;

/** Tests fixed menus and recommendation stretch functionality. */
public class MenuTest extends TestCase {
    public void testConeChoices() {
        Menu menu = new Menu();
        assertEquals(4, menu.getCones().length);
        assertEquals("Cup", menu.getCone(0).getType());
        assertEquals("Cone", menu.getCone(1).getType());
        assertEquals("Waffle Cone", menu.getCone(2).getType());
        assertEquals("Waffle Cup", menu.getCone(3).getType());
    }

    public void testDefensiveCopiesAndRecommendation() {
        Menu menu = new Menu();
        Cone[] choices = menu.getCones();
        choices[0] = new Cone("Changed", 99.0);
        assertEquals("Cup", menu.getCone(0).getType());
        assertEquals("Brownie",
                menu.getRecommendedToppings(menu.getFlavor(1))[0].getName());
    }
}

Ice Cream Builder — Project 2, Group 69

CS 2114 · Deliverable 3 (Prototype + Tests)

A console application where a customer builds an ice cream order: pick a cone, pick one flavor, add any number of toppings (duplicates allowed), remove toppings, view an itemized total, submit, and later view history statistics. Storage is the provided generic LinkedChain<T> (unchanged): one LinkedChain<Topping> inside every IceCreamOrder, and one shared LinkedChain<IceCreamOrder> holding all completed orders.

How to compile and run

# compile the application
javac -d out src/*.java

# run the program
java -cp out Main

# compile and run the tests (use ; instead of : on Windows)
javac -cp out:junit-platform-console-standalone.jar -d out test/*.java
java -jar junit-platform-console-standalone.jar -cp out --scan-classpath

The tests can also be run from an IDE with JUnit 5.

Sample session: 1 (new order) → 3 (Waffle) → 1 (Vanilla) → 1 (add topping) → 1 (Sprinkles) → 4 (submit) → 3 (statistics) → 4 (quit).

Reflection
What changed from our specification?
Class names and files. The assembler was written as RyanOrderListAssembler; it was renamed to OrderListAssembler to match the spec and the diagram. Ryan's separate RyanLinkedChain copy was removed, because the project must reuse the single provided LinkedChain rather than keep a second copy.
UserInterface.readMenuChoice still re-prompts on blank, non-numeric, and out-of-range input as specified. We added one case the spec did not mention: if the input runs out (scripted tests, Ctrl-D), it throws IllegalStateException and run() exits cleanly, instead of re-prompting forever.
OrderStats keeps every spec signature. Each statistic copies the chain once with toArray (O(n)) instead of looping with get(i) (which would make each statistic O(n²)). Name lookups ignore case and surrounding spaces. getSummary() also prints the average price, and "none" when no topping was ordered.
UserInterface. Stefan's first version used a hard-coded flavor/topping list, no cone, its own OrderHistory class, and constructed IceCreamOrder(name, price). That did not match the spec's class design (Menu, OrderListAssembler, OrderStats, Cone, and one shared LinkedChain<IceCreamOrder>), so the integrated version follows the spec's constructor UserInterface(Menu, OrderListAssembler, OrderStats, Scanner). [Team: confirm this decision.] Two ideas from Stefan's version are worth keeping: a y/n confirmation before submitting, and removing a topping by choosing from the toppings actually in the order. See Next Steps.
Menu contents were not in the spec; we chose 3 cones, 5 flavors, and 6 toppings.
Topping/Flavor/Cone use primitive double for price (the spec's field list said Double; the constructor signatures used double). They also reject NaN prices.
Main has no logic; it creates the one shared completed-order chain and passes the same object to OrderListAssembler and OrderStats.
Tests. We followed the spec's test plan: every public method has a normal case and a bad-input case. Added: IntegrationTest (assembler → shared chain → stats) and LinkedChainTest (the provided class's behavior, which our app depends on). One example value differs from the plan: the average-price test uses totals 3.00 and 6.00 (avg 4.50).
Validation locations are as specified in section 7 of the spec.

What changed in the LinkedChain?

Nothing. We use the provided LinkedChain.java unchanged and only through its public API (add, remove(), remove(T), count, size, isEmpty, toArray, get). All application logic (pricing, statistics, validation, menus) lives in our own classes. The same generic class stores both Topping and IceCreamOrder. Note that its get throws IndexOutOfBoundsException for a bad index, and our tests assert that.

What happened to our unknowns?
Duplicate toppings being ignored in pricing — figured out. Each add creates its own node; count and the price calculation include every duplicate (tested in IceCreamOrderTest, OrderListAssemblerTest, OrderStatsTest).
[Team: add any other unknowns from the original scope and mark each solved or open.]
What doesn't work yet?
Stretch goals (recommendations, preset orders, nutrition info, discounts) are not started.
Main itself has no automated test (it only wires objects; IntegrationTest covers the same wiring and the UI tests cover run()).
The UI cannot yet cancel in the middle of choosing a cone or flavor; only after the order has started.


Next Steps
What is left to finish the MVP (in order)
Run the full test suite with the real JUnit jar in lab and fix anything that fails.
Manually play through the program, including junk input, and fix usability issues (e.g. allow backing out while choosing a cone or flavor).
Port Stefan's y/n submit confirmation and "remove by choosing from the order's toppings" into the spec-compliant UserInterface, with tests.
Final read-through of Javadoc and style on every file.

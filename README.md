# Ice Cream Builder — Project 2, Group 69
CS 2114 · Deliverable 3 (Prototype + Tests)

A console application where a customer builds an ice cream order: pick a cone, pick one flavor,
add any number of toppings (duplicates allowed), remove toppings, view an itemized total,
submit, and later view history statistics. Storage is the provided generic `LinkedChain<T>`
(unchanged): one `LinkedChain<Topping>` inside every `IceCreamOrder`, and one shared
`LinkedChain<IceCreamOrder>` holding all completed orders.

## How to compile and run

Requires Java 17+. Tests use JUnit 5 (jar: `junit-platform-console-standalone`).

```
# compile the application
javac -d out src/*.java

# run the program
java -cp out Main

# compile and run the tests (use ; instead of : on Windows)
javac -cp out:junit-platform-console-standalone.jar -d out test/*.java
java -jar junit-platform-console-standalone.jar -cp out --scan-classpath
```

The tests can also be run from an IDE with JUnit 5.

Sample session: `1` (new order) → `3` (Waffle) → `1` (Vanilla) → `1` (add topping) → `1`
(Sprinkles) → `4` (submit) → `3` (statistics) → `4` (quit).

## Demonstration: it works

### Program run (real output from `java -cp out Main`)
The full scripted session is in `demo/demo-input.txt` (input) and `demo/demo-output.txt`
(output). To reproduce it:

```
java -cp out Main < demo/demo-input.txt
```

Bad input does not crash. The start of the run types `abc`, a blank line, and `9` at the main
menu, and each is caught and re-prompted:

```
=== Ice Cream Builder ===
1. New order
2. View order history
3. View statistics
4. Quit
Choose an option: Please enter a number.
Choose an option: Input cannot be blank.
Choose an option: Invalid selection. Enter a number from 1 to 4.
Choose an option: 
```

Later in the same run, removing a topping that is not in the order prints `Topping not found.`
and leaves the order unchanged. After two orders are submitted, history (newest first) and
statistics come from the shared `LinkedChain<IceCreamOrder>`:

```
2. View order history
3. View statistics
4. Quit
Choose an option: --- Order 1 ---
Cone: Cake ($0.50)
Flavor: Chocolate ($2.00)
Topping: Sprinkles ($0.50)
Total: $3.00
--- Order 2 ---
Cone: Waffle ($1.00)
Flavor: Vanilla ($2.00)
Topping: Sprinkles ($0.50)
Topping: Sprinkles ($0.50)
Topping: Sprinkles ($0.50)
Total: $4.50

=== Ice Cream Builder ===
1. New order
2. View order history
3. View statistics
4. Quit
Choose an option: Total orders: 2
Most popular flavor: Chocolate
Most common topping: Sprinkles
Average order price: $3.75

```

(`Most popular flavor` is a tie, so the flavor seen first, the newest order, wins, as the
specification says.)

### Tests

| Test file | Tests | What it covers |
|---|---|---|
| `ToppingTest`, `FlavorTest`, `ConeTest` | 6 each | constructor, accessors, `equals`/`hashCode`, `toString`, bad input |
| `MenuTest` | 5 | lists, copies, index getters, out-of-range |
| `IceCreamOrderTest` | 13 | add/remove/count, duplicates, pricing, `toString` |
| `OrderListAssemblerTest` | 18 | start/add/remove/submit/cancel, history order |
| `OrderStatsTest` | 23 | every statistic, ties, empty chain, blank input |
| `LinkedChainTest` | 19 | the provided chain's behavior our app depends on |
| `UserInterfaceTest` | 17 | scripted input, junk input, every display method |
| `IntegrationTest` | 2 | assembler to shared chain to `OrderStats` |

That is 115 tests across 10 files, each public method with at least a normal and a
bad-input case.

Result of running the tests with the JUnit console launcher:

```
[Team: paste the output of the JUnit run here, e.g. the "tests successful" summary.]
```

## Reflection

### What changed from our specification?
- **Class names and files.** The assembler was written as `RyanOrderListAssembler`; it was
  renamed to `OrderListAssembler` to match the spec and the diagram. Ryan's separate
  `RyanLinkedChain` copy was removed, because the project must reuse the single provided
  `LinkedChain` rather than keep a second copy.
- **`UserInterface.readMenuChoice`** still re-prompts on blank, non-numeric, and out-of-range
  input as specified. We added one case the spec did not mention: if the input runs out
  (scripted tests, Ctrl-D), it throws `IllegalStateException` and `run()` exits cleanly,
  instead of re-prompting forever.
- **`OrderStats`** keeps every spec signature. Each statistic copies the chain once with
  `toArray` (O(n)) instead of looping with `get(i)` (which would make each statistic O(n²)).
  Name lookups ignore case and surrounding spaces. `getSummary()` also prints the average
  price, and "none" when no topping was ordered.
- **`UserInterface`.** Stefan's first version used a hard-coded flavor/topping list, no cone,
  its own `OrderHistory` class, and constructed `IceCreamOrder(name, price)`. That did not match
  the spec's class design (`Menu`, `OrderListAssembler`, `OrderStats`, `Cone`, and one shared
  `LinkedChain<IceCreamOrder>`), so the integrated version follows the spec's constructor
  `UserInterface(Menu, OrderListAssembler, OrderStats, Scanner)`. *[Team: confirm this decision.]*
  Two ideas from Stefan's version are worth keeping: a y/n confirmation before submitting, and
  removing a topping by choosing from the toppings actually in the order. See Next Steps.
- **`Menu`** contents were not in the spec; we chose 3 cones, 5 flavors, and 6 toppings.
- **`Topping`/`Flavor`/`Cone`** use primitive `double` for price (the spec's field list said
  `Double`; the constructor signatures used `double`). They also reject NaN prices.
- **`Main`** has no logic; it creates the one shared completed-order chain and passes the same
  object to `OrderListAssembler` and `OrderStats`.
- **Tests.** We followed the spec's test plan: every public method has a normal case and a
  bad-input case. Added: `IntegrationTest` (assembler → shared chain → stats) and
  `LinkedChainTest` (the provided class's behavior, which our app depends on). One example
  value differs from the plan: the average-price test uses totals 3.00 and 6.00 (avg 4.50).
- **Validation locations** are as specified in section 7 of the spec.

### What changed in the LinkedChain?
Nothing. We use the provided `LinkedChain.java` unchanged and only through its public API
(`add`, `remove()`, `remove(T)`, `count`, `size`, `isEmpty`, `toArray`, `get`). All application
logic (pricing, statistics, validation, menus) lives in our own classes. The same generic class
stores both `Topping` and `IceCreamOrder`. Note that its `get` throws
`IndexOutOfBoundsException` for a bad index, and our tests assert that.

### What happened to our unknowns?
- **Duplicate toppings being ignored in pricing — figured out.** Each add creates its own
  node; `count` and the price calculation include every duplicate (tested in
  `IceCreamOrderTest`, `OrderListAssemblerTest`, `OrderStatsTest`).
- *[Team: add any other unknowns from the original scope and mark each solved or open.]*

### What doesn't work yet?
- Stretch goals (recommendations, preset orders, nutrition info, discounts) are not started.
- `Main` itself has no automated test (it only wires objects; `IntegrationTest` covers the
  same wiring and the UI tests cover `run()`).
- The UI cannot yet cancel in the middle of choosing a cone or flavor; only after the order
  has started.
- *[Team: add anything that fails when you run the tests in lab.]*

## Next Steps

### What is left to finish the MVP (in order)
1. Run the full test suite with the real JUnit jar in lab and fix anything that fails.
2. Manually play through the program, including junk input, and fix usability issues
   (e.g. allow backing out while choosing a cone or flavor).
3. Port Stefan's y/n submit confirmation and "remove by choosing from the order's toppings"
   into the spec-compliant `UserInterface`, with tests.
4. Final read-through of Javadoc and style on every file.

### Stretch goals
*[Confirm as a group.]* Proposed: **commit to** topping recommendations (built from
`OrderStats.getToppingCount` data) and **discounts** (a small pricing change in
`IceCreamOrder`); **drop** preset specialty orders and nutritional information to protect the
MVP.

### New tests needed
- Recommendation method: normal case plus empty-history and null-input cases.
- Discount: price with and without discount, and invalid-discount input.
- UI tests for any new menu options or "back" navigation.

### Who does what
| Item | Owner |
|---|---|
| Stats, recommendations (if kept), stats tests | Ashwin |
| Assembler/history chain, discount hook, Main wiring | Ryan |
| UserInterface changes (back navigation, new menu options), UI tests | Stefan |
| Topping, IceCreamOrder changes and tests | Cassandra |
| Menu, Flavor, Cone *(unassigned in spec; proposed: Cassandra)* | *[confirm]* |

## Attribution

| File | Who |
|---|---|
| `LinkedChain.java` | Course staff |
| `OrderStats.java`, `OrderStatsTest.java` | Ashwin |
| `OrderListAssembler.java`, `OrderListAssemblerTest.java` | Ryan |
| `LinkedChainTest.java` | Ryan |
| `Topping.java`, `ToppingTest.java`, `IceCreamOrder.java`, `IceCreamOrderTest.java` | Cassandra |
| `Flavor.java`, `Cone.java`, `Menu.java` and their tests | *[owner]* |
| `UserInterface.java`, `UserInterfaceTest.java` | Stefan |
| `Main.java`, `IntegrationTest.java` | *[owner]* |

### GenAI note
We used GenAI to plan the specification (test cases, signatures, Big-O estimates). For this
deliverable, GenAI drafted `OrderStats`, `Topping`, `Flavor`, `Cone`, `IceCreamOrder`, `Menu`,
`UserInterface`, `Main`, and most test files from our specification, and helped rename and
convert Ryan's assembler and chain tests. We reviewed the code against the spec and the
provided `LinkedChain.java`, checked that all classes use only its public API, and ran the
tests ourselves. *[Each member: edit this so it accurately describes what you personally
reviewed or changed.]*

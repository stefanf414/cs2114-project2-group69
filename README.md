# Ice Cream Builder — Project 2, Group 69
CS 2114 · Deliverable 3 (Prototype + Tests)

A console application where a customer builds an ice cream order: pick a flavor, add any number
of toppings (duplicates allowed), remove toppings, see an itemized total, confirm and submit, and
view the completed orders. Storage is a generic `LinkedChain<T>`: one `LinkedChain<Topping>` inside
every `IceCreamOrder`, and one `LinkedChain<IceCreamOrder>` holding completed orders.

## How to compile and run

Run the `UserInterface` file, then interact with the program in the console.

To run the tests, run each test class (`ToppingTest`, `OrderStatsTest`, `OrderListAssemblerTest`,
`UserInterfaceTest`, `LinkedChainTest`) as a JUnit test in your IDE.

Sample session: `1` (build an order) → `2` (Chocolate) → `1` (add topping) → `2` (Oreo) → `0`
(finish) → `y` (submit) → `2` (view completed orders) → `3` (exit).

## Demonstration: it works

Real output from `java -cp out UserInterface < demo/demo-input.txt` (input and full output are in
the `demo/` folder). The script types junk at the main menu (`text`, a blank line, `9`), builds a
Chocolate order with Oreo and two Sprinkles, removes one Sprinkles, answers `maybe` to the submit
question, then submits it and a second order.

Bad input is rejected and the program re-prompts instead of crashing:

```
Ice Cream Builder

1. Build an order
2. View completed orders
3. Exit
Choose an option: Invalid selection. Please enter a number from 1 to 3.
Choose an option: Invalid selection. Please enter a number from 1 to 3.
Choose an option: Invalid selection. Please enter a number from 1 to 3.
Choose an option: 
```

Order summary, a bad yes/no answer, and submission:

```
Flavor: Chocolate
Cone: Cup
Toppings: Oreo, Sprinkles
Total: $4.50
Submit this order? (y/n): Please enter y or n.
Submit this order? (y/n): Order submitted.
```

Completed orders, newest first:

```
Choose an option: Completed orders:
1. Vanilla ($3.00)
2. Chocolate ($4.50)

1. Build an order
2. View completed orders
3. Exit
Choose an option: Goodbye!

```

### Tests

| Test file | Tests | What it covers |
|---|---|---|
| `ToppingTest` | 7 | construction, trimming, zero price, bad names and prices, `equals`/`hashCode`, `toString` |
| `OrderStatsTest` | 8 | every statistic, empty history, blank names, summary |
| `OrderListAssemblerTest` | 12 | start/add/remove/submit/cancel, duplicates, newest-first history |
| `UserInterfaceTest` | 5 | full scripted order, invalid input, removing toppings, null constructor arguments |
| `LinkedChainTest` | 8 | add/remove/count/contains/get/toArray/clear, null and bad-index handling |

That is 40 tests in 5 files.

Result of running the tests:

```
[Team: paste your IDE or console test results here.]
```

## Reflection

### What changed from our specification?
- **No `Menu` class.** The menu lists (3 flavors, 4 toppings) live inside `UserInterface`.
- **`UserInterface` and `OrderHistory`.** The spec's `UserInterface` took a `Menu`,
  `OrderListAssembler` and `OrderStats`. Ours takes a `Scanner` and a new `OrderHistory` class
  that wraps the completed-order `LinkedChain<IceCreamOrder>`. We also added a yes/no
  confirmation before submitting.
- **No cone choice in the console app.** Orders use a free "Cup" cone, even though `Cone` and
  `IceCreamOrder` support cones.
- **Tests.** We wrote tests for `Topping`, `OrderStats`, `OrderListAssembler`, `UserInterface` and
  `LinkedChain`. Tests for `Flavor`, `Cone` and `IceCreamOrder` are not written yet.

### What changed in the LinkedChain?
The `LinkedChain.java` in our project is not identical to the provided file. It has the same
public methods and the same behavior for nulls and duplicates, but:
- `add` and `addToEnd` return `void` instead of `boolean`.
- `get` throws `IllegalArgumentException` instead of `IndexOutOfBoundsException`.
- The `toString()` method is gone.
- `remove(T)` reuses `remove()` when the match is the first node, and `Node.data` is `final`.

Our application does not need any of these changes, and `LinkedChainTest` asserts the
`IllegalArgumentException` behavior. *[Team: say why the provided file was replaced, or restore
the provided file and update `LinkedChainTest` to expect `IndexOutOfBoundsException`.]*

All application classes use the chain only through its public methods (`add`, `addToEnd`,
`remove`, `count`, `get`, `size`, `isEmpty`, `toArray`), and the pricing, statistics and
validation logic lives in our own classes. The same generic class stores `Topping` and
`IceCreamOrder`.

### What happened to our unknowns?
- **Duplicate toppings being ignored in pricing: figured out.** Each add creates its own node,
  and the total price adds up every topping in the chain. Tested in `OrderListAssemblerTest`
  and `UserInterfaceTest`.
- *[Team: add the other unknowns from the original scope and mark each solved or open.]*

### What doesn't work yet?
- **The statistics are not reachable from the program.** `OrderStats` works in its tests, but
  the console app has no statistics option, and `OrderListAssembler` is not used by the app.
- **The user cannot choose a cone.** Every order uses the free "Cup".
- **Missing tests.** `Flavor`, `Cone`, `IceCreamOrder` and `OrderHistory` have no test files of
  their own, so not every public method has a normal case and a bad-input case yet.
- **No `Menu` class**, and `Flavor` and `Cone` do not override `equals`/`hashCode`.
- Neither stretch goal has been started.
- *[Team: add anything that fails when you run the tests.]*

## Next Steps

### What is left to finish the MVP (in order)
1. Connect the pieces: make `UserInterface` use `OrderListAssembler` and the shared completed-order
   chain, add a "View statistics" option that prints `OrderStats.getSummary()`, and remove or
   fold in `OrderHistory`.
2. Add cone selection to the order-building flow.
3. Add a `Menu` class (or document that the menu stays in `UserInterface`).
4. Add `equals`/`hashCode` to `Flavor` and `Cone`, and decide whether name comparison ignores case.
5. Write the missing tests: `FlavorTest`, `ConeTest`, `IceCreamOrderTest`, `OrderHistoryTest`
   (or its replacement), plus tests for the new statistics option.
6. Decide whether to restore the provided `LinkedChain.java` or keep ours and explain why.

### Stretch goals
We will implement **topping recommendations** and **specialty (preset) orders**. We are dropping
discounts and nutritional information.

### New tests needed
- Statistics menu option: empty history and populated history.
- Cone selection: valid choice, invalid choice, non-numeric input.
- Topping recommendations and specialty orders: a normal case and a bad-input case each.
- The classes listed under "Missing tests" above.

### Who does what
*[Proposed from the spec's division of work; confirm.]*

| Item | Owner |
|---|---|
| Statistics menu option, `OrderStats` tests, topping recommendations | Ashwin |
| `LinkedChain` decision, `OrderListAssembler` wiring, `OrderHistory` | Ryan |
| Cone selection, menu, `UserInterface` tests | Stefan |
| `Flavor`, `Cone`, `IceCreamOrder` tests, specialty orders | Cassandra |

## Attribution

| File | Who |
|---|---|
| `LinkedChain.java`, `LinkedChainTest.java` | Ryan |
| `OrderListAssembler.java`, `OrderListAssemblerTest.java` | Ryan |
| `OrderStats.java`, `OrderStatsTest.java` | Ashwin |
| `UserInterface.java`, `UserInterfaceTest.java` | Stefan |
| `OrderHistory.java` | *[confirm]* |
| `Topping.java`, `ToppingTest.java`, `IceCreamOrder.java` | Cassandra |
| `Flavor.java`, `Cone.java` | *[confirm]* |

### GenAI note
Our group used GenAI to plan the specification (test cases, method signatures, and Big-O
estimates). *[Each member: describe whether and how you used GenAI on your own classes and
tests, and what you checked yourself.]*


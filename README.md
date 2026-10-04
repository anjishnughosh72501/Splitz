# Splitz - Expense Splitter & Debt Simplifier

> **"Given a group of people and their shared expenses, calculate each person's net balance and reduce the resulting debts to as few payments as possible."**

**Splitz** is a native Android application built in Kotlin and Jetpack Compose. It serves as both a clean, modern offline expense-sharing tool (similar to Splitwise) and a rich engineering showcase for foundational data structures and algorithms (Graph theory, Two-Heap greedy reduction, DFS cycle detection, NP-hard Exact Partitioning via Backtracking/Bitmask DP, and LIFO Stacks).

---

## 🌟 Key Features

1. **Group & Expense Management**:
   - Create multi-member expense groups.
   - Record shared expenses with custom categories (Food, Travel, Shopping, etc.).
   - Support for **Equal**, **Exact Amount**, **Percentage Basis Points (bp)**, and **Shares** splitting.
   - Strict integer math: **Zero floating-point rounding errors** (all values tracked in `Long` paise, e.g. ₹1 = 100 paise).
2. **Dynamic Ledger Recomputation**:
   - Balances are never cached permanently; net balances are calculated dynamically from active expenses and settlements.
3. **Debt Simplification (The Flagship Feature)**:
   - Visualizes transformation from a dense, complex web of debts (e.g. 7 pairwise debts) into minimal transactions (e.g. 3 payments).
   - **Greedy 2-PriorityQueue Solver**: $O(N \log N)$ reduction.
   - **Exact Minimum Solver (Branch-and-Bound / Bitmask DP)**: Guarantees the absolute mathematical minimum number of transactions for $\le 8$ members.
4. **Interactive Graph Visualization**:
   - Custom Canvas-based directed debt graph with circular node positioning, curved Bézier edges, arrowheads, and currency labels.
   - **DFS Cycle Detection & Cancellation**: Detects cyclic debt paths ($A \to B \to C \to A$) and eliminates redundant money flows.
5. **Settlement Flow**:
   - One-tap "Mark as Settled" recording atomic `SettlementEntity` transactions in Room SQLite.
   - Complete settlement history with search and filtering.
6. **Group-Level Undo / Redo**:
   - Dual-Stack mechanism (`Stack<ExpenseAction>`) allowing instant undo and redo of expense additions without deleting historical database records.
7. **Financial Analytics**:
   - Total spending, average expense, spending by category with visual progress bars, and per-person consumption breakdown.
8. **Live Data-Structure Debug / Viva Panel**:
   - Real-time inspector displaying the internal state of:
     - `HashMap<UserId, Long>` (Net balances & zero-sum invariant check)
     - `PriorityQueue<Balance>` (Creditors Max-Heap)
     - `PriorityQueue<Balance>` (Debtors Max-Debt Heap)
     - `DebtGraph` Adjacency Map
     - DFS Cycle detector status & cycle path
     - Greedy vs Exact transaction counts
     - `Stack<ExpenseAction>` (Undo & Redo stack sizes)
9. **One-Tap Demo Mode**:
   - Preloads the realistic "Goa Trip" sample dataset (Ali, Maya, Rahul, Sara with hotel, dinner, taxi, and ferry expenses) to instantly test and demonstrate all algorithms.
10. **100% Offline & Private**:
    - No backend, no login, no analytics tracking, no internet connection required. Room SQLite is the local source of truth.

---

## 🏛️ Architecture & Clean Separation

Splitz follows the **Repository Pattern** and **Unidirectional Data Flow (UDF)**:

```text
┌────────────────────────────────────────────────────────┐
│                   Jetpack Compose UI                   │
│   (HomeScreen, Dashboard, AddExpense, Simplify, etc.)  │
└───────────────────────────▲────────────────────────────┘
                            │ State / Events
┌───────────────────────────┴────────────────────────────┐
│                    Android ViewModels                  │
│       (HomeViewModel, SimplifyViewModel, etc.)         │
└───────────────────────────▲────────────────────────────┘
                            │ Domain Calls
┌───────────────────────────┴────────────────────────────┐
│                  Pure Algorithm Layer                  │
│  • ExpenseSplitter        • DebtSimplifier (2 Heaps)   │
│  • BalanceCalculator      • ExactSettlementSolver      │
│  • DebtGraph (DFS)        • UndoRedoManager (Stacks)   │
│  • AnalyticsCalculator                                 │
└───────────────────────────▲────────────────────────────┘
                            │ Repositories
┌───────────────────────────┴────────────────────────────┐
│                     Database Layer                     │
│               Room SQLite (6 Entities & DAOs)          │
└────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Schema

The persistent source of truth is Room SQLite (`paisede_database`):

1. **`GroupEntity`**: `id` (PK), `name`, `createdAt`, `currencyCode` ("INR")
2. **`MemberEntity`**: `id` (PK), `groupId` (FK), `name`, `createdAt`
3. **`ExpenseEntity`**: `id` (PK), `groupId` (FK), `payerId` (Index), `amountPaise`, `description`, `category`, `splitType`, `createdAt`
4. **`ExpenseSplitEntity`**: `id` (Auto PK), `expenseId` (FK), `userId`, `amountPaise`, `percentageBasisPoints`, `shares`
5. **`SettlementEntity`**: `id` (PK), `groupId` (FK), `fromUserId`, `toUserId`, `amountPaise`, `createdAt`
6. **`ExpenseActionEntity`**: `id` (PK), `groupId` (FK), `expenseId`, `actionType` (`ADD`, `UNDO`, `REDO`), `createdAt`

---

## 🧩 Data Structures & Algorithm Breakdown

| Data Structure / Algorithm | Class Name | Real Role in Splitz | Time Complexity | Space Complexity |
|---|---|---|---|---|
| **HashMap** | `BalanceCalculator` | Aggregates credits and debits per user to establish net balances. | $O(E)$ | $O(V)$ |
| **PriorityQueue (Two Heaps)** | `DebtSimplifier` | Greedy matching of greatest creditor with greatest debtor. | $O(N \log N)$ | $O(N)$ |
| **Adjacency Map (Graph)** | `DebtGraph` | Represents raw debt directed edges: `Map<UserId, MutableMap<UserId, Long>>`. | $O(V + E)$ | $O(V + E)$ |
| **DFS (Depth-First Search)** | `DebtGraph` | Cycle detection using 3 states (`UNVISITED`, `VISITING`, `VISITED`). | $O(V + E)$ | $O(V)$ |
| **Stack (LIFO)** | `UndoRedoManager` | Dual-stack (`undoStack`, `redoStack`) for non-destructive group expense undo/redo. | $O(1)$ ops | $O(A)$ |
| **Backtracking / Bitmask DP** | `ExactSettlementSolver` | Exact minimum transaction solver via maximum disjoint zero-sum partition ($N \le 8$). | $O(3^N)$ | $O(2^N)$ |
| **Ledger (Append-only)** | `ExpenseRepositoryImpl` | Database transaction recording `ExpenseActionEntity` events. | $O(1)$ append | $O(E)$ |

---

## 💰 Strict Integer Money Model

**Rule**: Never use `Float`, `Double`, or `BigDecimal` for money math. Floating-point numbers introduce IEEE-754 precision issues (e.g. $0.1 + 0.2 = 0.30000000000000004$).

- In Splitz, **all internal values are stored in `Long` paise**:
  - ₹1 = 100 paise
  - ₹100 = 10,000 paise
  - ₹1,200 = 120,000 paise
- Percentages are represented in **basis points (bp)**, where $100\% = 10,000\text{ bp}$.
  - $50\% = 5,000\text{ bp}$
  - $33.33\% = 3,333\text{ bp}$
- When integer division has a remainder ($R = \text{amount} \pmod N$), remainder paise are distributed deterministically (+1 paise) to the first $R$ participants. **The sum of splits is guaranteed to equal total expense amount.**

---

## 🎓 VIVA PREPARATION & TECHNICAL INTERVIEW GUIDE

This section contains direct answers to questions typically asked in engineering project vivas and algorithmic defense presentations:

### 1. Why use a `HashMap` for balance calculation?
- **Answer**: In a group of $V$ people and $E$ expense splits, calculating net balance requires frequent read-modify-write operations (`balanceMap[userId] += amount`). A `HashMap` provides amortized $O(1)$ time complexity for key lookups and insertions, allowing the entire ledger to be aggregated in linear $O(E)$ time.

### 2. Why use a `PriorityQueue`? Why two heaps instead of one?
- **Answer**: The greedy debt simplification algorithm repeatedly pairs the person who is owed the most money (largest creditor) with the person who owes the most money (largest debtor).
  - A single heap would require searching or filtering positive vs negative numbers.
  - By using **two dedicated Max-Heaps** (`creditorHeap` prioritized by largest positive balance, and `debtorHeap` prioritized by largest absolute negative debt), we can extract the maximum creditor and debtor in $O(1)$ and restore heap invariants in $O(\log N)$ time.

### 3. How does the Greedy Debt Simplification algorithm work?
- **Answer**:
  1. Extract largest creditor $C$ and largest debtor $D$.
  2. Compute $\text{payment} = \min(C.\text{balance}, |D.\text{balance}|)$.
  3. Create transaction $D \to C$ for that payment amount.
  4. Deduct payment from both balances.
  5. If $C$ still has remaining credit, push $C$ back into `creditorHeap`.
  6. If $D$ still owes remaining debt, push $D$ back into `debtorHeap`.
  7. Repeat until both heaps are empty.
  This produces at most $N - 1$ transactions, where $N$ is the number of members with non-zero balances.

### 4. Why isn't the greedy algorithm always mathematically optimal?
- **Answer**: Greedy makes the locally optimal choice at each step (largest creditor + largest debtor). However, the global minimum transaction problem is equivalent to the **Subset Sum / Minimum Disjoint Zero-Sum Partition Problem**, which is NP-hard.
  - *Counterexample*: Suppose net balances are:
    - $A = +10, B = -10$ (Sum = 0)
    - $C = +20, D = -20$ (Sum = 0)
  - The exact minimum is **2 transactions** ($B \to A: 10$ and $D \to C: 20$).
  - A naive greedy order might match $D (-20)$ with $A (+10)$, creating $D \to A: 10$, leaving $D (-10)$ to settle with $C (+20)$, taking **3 transactions**.

### 5. Why is the Exact Minimum Solver restricted to $\le 8$ participants?
- **Answer**: Because finding the maximum number of disjoint subsets that each sum to zero is an NP-complete partition problem. The search space over all subsets is $2^N$, and testing disjoint combinations scales exponentially ($O(3^N)$ with submask iteration). For $N = 8$, $2^8 = 256$, which computes in sub-milliseconds on a mobile processor without freezing the UI. For large groups ($N > 8$), the greedy $O(N \log N)$ algorithm is used instead.

### 6. Why use DFS for cycle detection? What do the 3 states mean?
- **Answer**: Directed cycles in a debt graph (e.g. $A \text{ owes } B \text{ owes } C \text{ owes } A$) can be detected using Depth-First Search with 3 vertex colors/states:
  - `UNVISITED` (White): Node not yet encountered.
  - `VISITING` (Gray): Node currently on the active recursion stack. If an outgoing edge points to a `VISITING` node, a **back-edge** exists, proving a directed cycle.
  - `VISITED` (Black): Node and all its descendants have been fully explored.

### 7. Why use a `Stack` for Undo/Redo?
- **Answer**: Undo and redo are intrinsically Last-In, First-Out (LIFO) operations.
  - When an expense is added, its action is pushed onto `undoStack`, and `redoStack` is cleared.
  - Undoing pops from `undoStack` and pushes onto `redoStack`.
  - Redoing pops from `redoStack` and pushes onto `undoStack`.
  Both push and pop run in strict $O(1)$ time.

---

## 🧪 Testing

Splitz contains comprehensive JUnit unit tests covering:
- `ExpenseSplitterTest`: Equal split with remainder paise distribution, exact sum validation, percentage basis points, shares, zero values.
- `BalanceCalculatorTest`: Single/multiple expenses, multiple payers, settlement integration, zero-sum invariant verification.
- `DebtGraphTest`: Edge additions, accumulations, DFS cycle detection, and cycle cancellation.
- `DebtSimplifierTest`: Two-PriorityQueue polling order, greedy transaction generation, zero-transaction prevention.
- `ExactSettlementSolverTest`: Zero-sum subset partitioning, ensuring exact solver is never worse than greedy.
- `UndoRedoManagerTest`: Full lifecycle of Add $\to$ Undo $\to$ Redo $\to$ New Action clearing redo stack.
- `AnalyticsCalculatorTest`: Pure `Long` paise financial metrics.
- `CurrencyUtilsTest`: Indian numeral grouping (`1,00,000`) and currency parsing.

Run all unit tests:
```bash
./gradlew test
```

Build the debug APK:
```bash
./gradlew assembleDebug
```

---

## 🚀 How to Run

1. Open project in **Android Studio** (Koala / Ladybug or newer).
2. Ensure JDK 21 is configured (`Settings > Build, Execution, Deployment > Build Tools > Gradle`).
3. Select the `app` configuration and hit **Run** on any Android device or emulator (Android 8.0+ / API 26+).
4. On the Home screen, tap **Demo Data** to immediately load the sample "Goa Trip" and explore the algorithms!

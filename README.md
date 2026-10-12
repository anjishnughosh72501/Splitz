# Splitz - Offline Group Expense Manager & Algorithmic Debt Simplifier

<div align="center">
  <img src="app/src/main/res/drawable-nodpi/splitz_logo.png" alt="Splitz Logo" width="140" height="140" style="border-radius: 28px;" />
  <h3>Split expenses. Settle debts. Stress less.</h3>
  <p><strong>Version 1.1.0</strong> • Native Android • Jetpack Compose • Room SQLite • Offline-First</p>

  [![Android CI](https://github.com/anjishnughosh72501/Splitz/actions/workflows/android-ci.yml/badge.svg)](https://github.com/anjishnughosh72501/Splitz/actions/workflows/android-ci.yml)
  [![Release](https://img.shields.io/badge/Release-v1.1.0-blue.svg)](https://github.com/anjishnughosh72501/Splitz/releases)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
  [![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-orange.svg)](https://developer.android.com)
</div>

---

## 📖 About Splitz

### What is Splitz?
**Splitz** is a modern, privacy-focused, offline-first Android expense-sharing and debt-settlement application built entirely with Kotlin and Jetpack Compose. 

Whether you are splitting rent with roommates, traveling on vacation with friends, dining out in a group, or organizing shared household costs, Splitz makes tracking shared expenditures effortless—while eliminating awkward math and messy webs of mutual IOUs.

### Why Splitz?
Traditional bill-splitting apps often suffer from three major shortcomings:
1. **Intrusive Account Requirements**: They demand email registrations, phone numbers, cloud accounts, and constant internet connectivity just to split a restaurant bill.
2. **Privacy Concerns**: Financial transaction histories and personal spending habits are harvested and stored on third-party cloud servers.
3. **Inefficient Settlements & Rounding Errors**: Standard apps often leave users with circular debts (where A owes B, B owes C, and C owes A) and floating-point fractional cent discrepancies ($0.1 + 0.2 \neq 0.3$).

**Splitz solves all three:**
- 🔒 **100% Offline & Private**: Zero cloud backend, zero telemetry, zero trackers, and zero required permissions. All ledger data resides strictly on your local device in encrypted Room SQLite storage.
- ⚡ **First-Run Onboarding**: Seamless display-name onboarding and currency configuration without logins or account creation.
- 🧮 **Exact Integer Math**: All currency subunits are stored and calculated as 64-bit integer subunits (e.g. Paise, Cents, Pence, Sen) with deterministic remainder distribution. Floating-point rounding discrepancies are mathematically impossible.
- 🚀 **Mathematical Debt Simplification**: Built-in Graph theory and Two-PriorityQueue greedy reduction algorithms minimize the total number of cash transfers required to settle an entire group ledger to equilibrium.
- 🎨 **Tactile High-Contrast UI**: Featuring custom atmospheric artwork themes (Graphite, Electric Blue, Ultraviolet, Matcha), interactive debt graphs, and in-app typography scaling.

---

## 📥 Download & Install

You can immediately install and run the production build on any Android device running Android 8.0 (Oreo) or higher:

- **Direct Repository Binary**: Download [`Splitz.apk`](Splitz.apk) directly from the repository root.
- **GitHub Releases**: Download the verified release assets from [GitHub Releases v1.1.0](https://github.com/anjishnughosh72501/Splitz/releases).

---

## 🌟 Key Features

1. **Group & Expense Management**:
   - Create multi-member expense groups with customized group contexts.
   - Record shared expenses with categories (Food, Travel, Entertainment, Accommodation, Utilities, etc.).
   - Support for **Equal**, **Exact Amount**, **Percentage Basis Points (bp)**, and **Shares / Weight** splits.
   - Deterministic remainder distribution: When an expense doesn't divide evenly, subunits are apportioned deterministically so the sum of individual shares always matches the total expense amount to the penny/paise.

2. **Automated Debt Simplification (The Flagship Feature)**:
   - Visualizes transformation from a dense, complex web of debts (e.g. 7 pairwise debts) into minimal transactions (e.g. 3 payments).
   - **Greedy 2-PriorityQueue Solver**: $O(N \log N)$ reduction pairing greatest creditors with greatest debtors.
   - **Exact Minimum Solver (Branch-and-Bound / Bitmask DP)**: Guarantees the absolute mathematical minimum number of transactions for $\le 8$ members by solving maximum disjoint zero-sum partitions.

3. **Interactive Directed Debt Graph**:
   - Custom Canvas-based directed debt graph with circular node positioning, curved Bézier edges, arrowheads, and currency labels.
   - **DFS Cycle Detection & Cancellation**: Detects cyclic debt paths ($A \to B \to C \to A$) and eliminates redundant circular money flows before settlement.

4. **Group-Level Undo / Redo**:
   - Dual-Stack mechanism (`Stack<ExpenseAction>`) allowing instant undo and redo of expense additions and modifications without corrupting historical database states.

5. **Multi-Currency Support**:
   - Native support for international currencies: **INR (₹)**, **USD ($)**, **EUR (€)**, **GBP (£)**, **JPY (¥)**, **CAD (CA$)**, **AUD (A$)**, **SGD (S$)**, and **AED (د.إ)** with localized number grouping (including Indian Lakh/Crore systems).

6. **Custom Themes & High Contrast**:
   - Atmospheric visual themes: **Graphite**, **Electric Blue**, **Ultraviolet**, and **Matcha**.
   - High-contrast card surfaces and glassmorphic typography optimized for legibility in all lighting conditions.
   - In-app font scaling (Small, Medium, Large) with real-time UI previews.

7. **Financial Analytics**:
   - Total group spending, average expense per person, spending breakdowns by category with visual progress meters, and net debtor/creditor balances.

8. **Live Data Structure Viva & Inspector Panel**:
   - Real-time inspector displaying the internal state of live heaps, maps, and stacks:
     - `HashMap<UserId, Long>` (Net balances & zero-sum invariant check)
     - `PriorityQueue<Balance>` (Creditors Max-Heap)
     - `PriorityQueue<Balance>` (Debtors Max-Debt Heap)
     - `DebtGraph` Adjacency Map
     - DFS Cycle detector status & cycle path
     - Greedy vs Exact transaction counts
     - `Stack<ExpenseAction>` (Undo & Redo stack depths)

---

## 🏛️ Architecture & Clean Separation

Splitz follows modern Android development practices with the **Repository Pattern** and **Unidirectional Data Flow (UDF)**:

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
                            │ Domain Invocations
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

The persistent source of truth is local Room SQLite (`paisede_database`):

1. **`GroupEntity`**: `id` (PK), `name`, `createdAt`, `currencyCode`
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

**Rule**: Never use `Float`, `Double`, or standard floating-point representation for money. IEEE-754 precision issues inevitably corrupt ledger balances.

- In Splitz, **all internal transactions are computed in 64-bit `Long` integer subunits**:
  - $1.00 = 100 \text{ subunits}$
  - $100.00 = 10,000 \text{ subunits}$
- Percentages are represented in **basis points (bp)**, where $100\% = 10,000\text{ bp}$.
  - $50\% = 5,000\text{ bp}$
  - $33.33\% = 3,333\text{ bp}$
- When division leaves a remainder ($R = \text{amount} \pmod N$), remainder subunits are distributed deterministically (+1 subunit) to the first $R$ participants. **The sum of splits always equals the total expense amount.**

---

## 🎓 Viva & Technical Interview Defense Guide

### 1. Why use a `HashMap` for balance calculation?
- **Answer**: In a group of $V$ people and $E$ expense splits, calculating net balance requires frequent read-modify-write operations (`balanceMap[userId] += amount`). A `HashMap` provides amortized $O(1)$ time complexity for key lookups and insertions, allowing the entire ledger to be aggregated in linear $O(E)$ time.

### 2. Why use a `PriorityQueue`? Why two heaps instead of one?
- **Answer**: The greedy debt simplification algorithm repeatedly pairs the person who is owed the most money (largest creditor) with the person who owes the most money (largest debtor).
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
- **Answer**: Greedy makes the locally optimal choice at each step (largest creditor + largest debtor). However, the global minimum transaction problem is equivalent to the **Minimum Disjoint Zero-Sum Partition Problem**, which is NP-hard.
  - *Counterexample*: Suppose net balances are:
    - $A = +10, B = -10$ (Sum = 0)
    - $C = +20, D = -20$ (Sum = 0)
  - The exact minimum is **2 transactions** ($B \to A: 10$ and $D \to C: 20$).
  - A naive greedy order matching $D (-20)$ with $A (+10)$ would create $D \to A: 10$, leaving $D (-10)$ to settle with $C (+20)$, taking **3 transactions**.

### 5. Why is the Exact Minimum Solver restricted to $\le 8$ participants?
- **Answer**: Finding the maximum number of disjoint subsets that each sum to zero is an NP-complete partition problem. The search space over all subsets is $2^N$, and testing disjoint combinations scales exponentially ($O(3^N)$ with submask iteration). For $N = 8$, $2^8 = 256$, which computes in sub-milliseconds on mobile processors without UI thread latency. For larger groups ($N > 8$), the greedy $O(N \log N)$ algorithm runs instead.

### 6. Why use DFS for cycle detection? What do the 3 states mean?
- **Answer**: Directed cycles in a debt graph (e.g. $A \text{ owes } B \text{ owes } C \text{ owes } A$) are detected using Depth-First Search with 3 vertex colors/states:
  - `UNVISITED` (White): Node not yet encountered.
  - `VISITING` (Gray): Node currently on the active recursion stack. If an outgoing edge points to a `VISITING` node, a **back-edge** exists, proving a directed cycle.
  - `VISITED` (Black): Node and all its descendants have been fully explored.

---

## 🧪 Automated Testing

Splitz maintains an automated test suite covering:
- `ExpenseSplitterTest`: Equal splits with remainder subunit distribution, exact sum validation, percentage basis points, shares, zero values.
- `BalanceCalculatorTest`: Single/multiple expenses, multiple payers, settlement integration, zero-sum invariant verification.
- `DebtGraphTest`: Edge additions, accumulations, DFS cycle detection, and cycle cancellation.
- `DebtSimplifierTest`: Two-PriorityQueue polling order, greedy transaction generation, zero-transaction prevention.
- `ExactSettlementSolverTest`: Zero-sum subset partitioning, ensuring exact solver is never worse than greedy.
- `UndoRedoManagerTest`: Full lifecycle of Add $\to$ Undo $\to$ Redo $\to$ New Action clearing redo stack.
- `AnalyticsCalculatorTest`: 64-bit integer financial metrics.
- `ThemeSystemTest`: Verification of visual theme palettes, contrast ratios, and persistent state flows.
- `UserIdentityResolutionTest`: Verification of "Me (User)" automatic assignment in group rosters.

Run all unit tests:
```bash
./gradlew testDebugUnitTest
```

---

## 🚀 Building & Running Locally

1. Open project in **Android Studio** (Ladybug / Koala or newer).
2. Ensure **JDK 21** is selected under `Settings > Build, Execution, Deployment > Build Tools > Gradle`.
3. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
4. Build the release APK (with ProGuard/R8 minification):
   ```bash
   ./gradlew assembleRelease
   ```
5. Deploy to a connected physical device or emulator running Android 8.0+ (API 26+).

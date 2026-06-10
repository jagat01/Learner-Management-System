# Design Notes — LearnTrack

## 1. Why ArrayList Instead of Array?

In LearnTrack we store all Students, Courses, and Enrollments in `ArrayList<T>` (found inside each repository class). Here is why we chose `ArrayList` over a plain Java array:

| Feature | `Array` | `ArrayList` |
|---------|---------|-------------|
| Fixed size? | Yes — must declare size upfront | No — grows/shrinks automatically |
| Add element | Must create a new, larger array and copy | `list.add(element)` — one line |
| Remove element | Shift elements manually | `list.remove(index)` — built-in |
| Iterate | `for` loop with index | Enhanced `for-each` loop or Iterator |
| Part of Collections framework? | No | Yes |

Since we do not know how many students or courses will be added at runtime, using a fixed-size array would either waste memory (if too large) or crash (if too small). `ArrayList` handles this automatically by resizing itself as needed. This is why `ArrayList` is the idiomatic Java choice for dynamic collections in an in-memory data store.

---

## 2. Where and Why Static Members Were Used

### `IdGenerator` (utility class)

```java
private static final AtomicInteger studentCounter = new AtomicInteger(1);

public static String generateStudentId() {
    return "STUDENT-" + studentCounter.getAndIncrement();
}
```

The counters are `static` because they belong to the *class itself*, not to any particular instance. There should only ever be one student counter shared across the entire application. If the counter were an instance field, every time you created a new `IdGenerator` object the counter would reset to 1, causing duplicate IDs.

Static utility methods (`generateStudentId()`, `generateCourseId()`, etc.) are also a natural fit here: you do not need to carry around an `IdGenerator` object to call them. Callers simply write `IdGenerator.generateStudentId()`.

### `AppConstants` and `MenuOptions` (constants classes)

Both classes contain only `static final` fields. This is the standard Java pattern for shared, immutable constants. No instances of these classes are needed — the values are accessed directly via the class name (e.g. `AppConstants.SEPARATOR`).

### `InputValidator` (utility class)

All validation methods are `static` because they are pure functions — they take input, return a result, and require no state. Marking them `static` communicates intent: "this is a stateless helper", and saves the caller from creating a throwaway object.

---

## 3. Where and What We Gained from Inheritance

### `Student extends Person`

We introduced a `Person` base class with the common fields shared by any kind of person in the system: `id`, `firstName`, `lastName`, and `email`.

```
Person
  └── Student   (adds: batch, active)
  └── Trainer   (future: could add specialisation, rating, etc.)
```

**What we gained:**

- **No duplication.** Fields like `id`, `firstName`, `lastName`, and `email` are defined once in `Person` and automatically available in `Student`. Without inheritance, every person-like class would need to repeat those four fields plus their getters/setters.

- **Polymorphism via `getDisplayName()`.** `Person` defines a base implementation (`"firstName lastName"`). `Student` overrides it to append the batch: `"firstName lastName (Batch: Batch-2024)"`. Any code that holds a `Person` reference can call `getDisplayName()` and get the correct, specialized output without knowing whether it is a `Student` or a `Trainer`.

- **Extensibility.** If the project later adds a `Trainer` role, it simply extends `Person` and reuses all the base fields and methods. Only trainer-specific attributes need to be added.

- **`super` usage.** `Student` constructors explicitly call `super(id, firstName, lastName, email)` to delegate initialization of the inherited fields to the parent. This keeps code clean and avoids redundancy.

---

## 4. Separation of Concerns (Layered Architecture)

| Layer | Classes | Responsibility |
|-------|---------|---------------|
| **Entity** | `Person`, `Student`, `Course`, `Enrollment` | Data model only — no logic |
| **Repository** | `*Repository` | Raw storage/retrieval using `ArrayList` — no business rules |
| **Service** | `*Service` | Business logic, validation, orchestration |
| **UI** | `Main.java` | Display menus, read input, call services — no business logic |
| **Util/Constants** | `IdGenerator`, `InputValidator`, `AppConstants`, `MenuOptions` | Stateless helpers and shared constants |

This separation ensures that each class has one clear job, making the code easier to read, test, and extend.

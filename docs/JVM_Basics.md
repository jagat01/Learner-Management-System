# JVM Basics — LearnTrack

## 1. What is JVM (Java Virtual Machine)?

The **JVM** is a software program that runs Java bytecode. Think of it as a universal engine: no matter what operating system (Windows, macOS, Linux) you are on, the JVM provides the same runtime environment for your Java program.

When you run `java MyProgram`, the JVM:
1. Loads the compiled `.class` file (bytecode) from disk.
2. Verifies the bytecode is safe to execute.
3. Interprets or **JIT-compiles** (Just-In-Time) the bytecode into native machine instructions.
4. Manages memory automatically via **Garbage Collection** — it frees memory you are no longer using.

> **In short:** the JVM is what lets the same compiled Java program run on any device without recompilation.

---

## 2. What is JRE (Java Runtime Environment)?

The **JRE** is a package that bundles:
- The **JVM** (to run programs), and
- The **Java standard library** (core classes like `java.util.ArrayList`, `java.lang.String`, etc.)

If you only want to *run* a Java application (not develop one), the JRE is all you need.

---

## 3. What is JDK (Java Development Kit)?

The **JDK** is the full developer toolkit. It includes everything in the JRE, plus:
- **`javac`** — the Java compiler that turns `.java` source files into `.class` bytecode.
- **`javadoc`** — generates HTML documentation from code comments.
- **`jar`** — packages compiled classes into a distributable `.jar` archive.
- Debugging and profiling tools.

> **Rule of thumb:** Developers install the JDK. End-users who just run the app install the JRE.

---

## 4. What is Bytecode?

When you compile a Java source file with `javac`, the output is **bytecode** — a set of instructions stored in `.class` files. Bytecode is *not* native machine code (zeros and ones specific to a CPU); instead it is an intermediate format that the JVM can understand and execute on any platform.

```
Source (.java)  -->  javac  -->  Bytecode (.class)  -->  JVM  -->  Program runs
```

---

## 5. "Write Once, Run Anywhere" (WORA)

This is Java's most famous promise, and it is made possible by bytecode + the JVM.

Before Java, a program compiled for Windows (native x86 code) could not run on macOS or Linux without being rewritten and recompiled for each platform. Java solves this by adding an extra step: instead of compiling to machine code, `javac` compiles to **platform-neutral bytecode**. The bytecode is the same no matter where it was compiled.

Each operating system has its own JVM implementation that knows how to translate that shared bytecode into the correct native instructions for *that* OS. So:

- You write and compile your `.java` file once.
- The resulting `.class` bytecode file can be dropped onto a Windows machine, a Mac, or a Linux server, and as long as a JVM is installed, it runs correctly.

This is what "Write Once, Run Anywhere" means.

# Setup Instructions — LearnTrack

## 1. JDK Version Used

This project targets **Java 17** (LTS). The `pom.xml` is configured with:

```xml
<maven.compiler.release>17</maven.compiler.release>
```

To verify your installation:

```bash
java -version
# Expected output: openjdk 17.x.x or similar
```

---

## 2. Installing the JDK

| Platform | Steps |
|----------|-------|
| **Windows** | Download the JDK 17 installer from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/). Run the installer and ensure `JAVA_HOME` is set. |
| **macOS**   | `brew install openjdk@17` or download from Adoptium. |
| **Linux**   | `sudo apt install openjdk-17-jdk` (Debian/Ubuntu) |

After installing, add `JAVA_HOME` to your environment:

```bash
export JAVA_HOME=/path/to/jdk-17
export PATH=$JAVA_HOME/bin:$PATH
```

---

## 3. Running the Classic "Hello, World!" Program

**Step 1 – Create the file** `HelloWorld.java`:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

**Step 2 – Compile** using the Java compiler:

```bash
javac HelloWorld.java
# This produces: HelloWorld.class (bytecode)
```

**Step 3 – Run** using the Java runtime:

```bash
java HelloWorld
# Output: Hello, World!
```

> **What just happened?**
> 1. `javac` translated your `.java` source into platform-neutral **bytecode** (`.class` file).
> 2. `java` launched the **JVM** which interpreted/JIT-compiled that bytecode and ran your program.

---

## 4. Running LearnTrack

```bash
# From the project root directory:
mvn compile
mvn exec:java -Dexec.mainClass="com.airtribe.learntrack.Main"
```

Or with plain Java (after compiling):

```bash
javac -d out -sourcepath src/main/java $(find src/main/java -name "*.java")
java -cp out com.airtribe.learntrack.Main
```

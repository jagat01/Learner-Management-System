# LearnTrack — Student & Course Management System

> A console-based management system built in **Core Java** to practice fundamental OOP, collections, and clean code principles.

---

## 📖 Project Description

LearnTrack lets an administrator manage **Students**, **Courses**, and **Enrollments** through an interactive menu-driven CLI. All data is stored in-memory (no database), making it easy to compile and run without any external setup.

---

## 🗂️ Directory Structure

```
LearnTrack/
├── pom.xml                          # Maven build file
├── README.md                        # This file
├── docs/
│   ├── Setup_Instructions.md
│   ├── JVM_Basics.md
│   └── Design_Notes.md
└── src/
    └── main/java/com/airtribe/learntrack/
        ├── Main.java                # Entry point & console UI
        ├── entity/
        │   ├── Person.java          # Base class
        │   ├── Student.java         # Extends Person
        │   ├── Course.java
        │   └── Enrollment.java
        ├── repository/              # In-memory ArrayList storage
        │   ├── StudentRepository.java
        │   ├── CourseRepository.java
        │   └── EnrollmentRepository.java
        ├── service/                 # Business logic
        │   ├── StudentService.java
        │   ├── CourseService.java
        │   └── EnrollmentService.java
        ├── exception/
        │   ├── EntityNotFoundException.java
        │   └── InvalidInputException.java
        ├── util/
        │   ├── IdGenerator.java
        │   └── InputValidator.java
        ├── constants/
        │   ├── AppConstants.java
        │   └── MenuOptions.java
        └── enums/
            ├── EnrollmentStatus.java
            └── CourseStatus.java
```

---

## 🏗️ Class Diagram

```mermaid
classDiagram
    direction TB

    class Person {
        -String id
        -String firstName
        -String lastName
        -String email
        +getDisplayName() String
        +getters/setters()
    }

    class Student {
        -String batch
        -boolean active
        +Student(id, firstName, lastName, email, batch, active)
        +Student(id, firstName, lastName, batch, active)
        +getDisplayName() String
        +getters/setters()
    }

    class Course {
        -String id
        -String courseName
        -String description
        -int durationInWeeks
        -boolean active
        +getters/setters()
    }

    class Enrollment {
        -String id
        -String studentId
        -String courseId
        -LocalDate enrollmentDate
        -EnrollmentStatus status
        +getters/setters()
    }

    class EnrollmentStatus {
        <<enumeration>>
        ENROLLED
        COMPLETED
        CANCELLED
    }

    class CourseStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
        ARCHIVED
    }

    class StudentRepository {
        -List~Student~ students
        +save(Student)
        +findById(String) Student
        +findAll() List~Student~
    }

    class CourseRepository {
        -List~Course~ courses
        +save(Course)
        +findById(String) Course
        +findAll() List~Course~
    }

    class EnrollmentRepository {
        -List~Enrollment~ enrollments
        +save(Enrollment)
        +findById(String) Enrollment
        +findAll() List~Enrollment~
        +findByStudentId(String) List~Enrollment~
    }

    class StudentService {
        -StudentRepository studentRepository
        +addStudent(firstName, lastName, email, batch) Student
        +addStudent(firstName, lastName, batch) Student
        +getAllStudents() List~Student~
        +getStudentById(String) Student
        +deactivateStudent(String)
    }

    class CourseService {
        -CourseRepository courseRepository
        +addCourse(name, desc, weeks) Course
        +getAllCourses() List~Course~
        +getCourseById(String) Course
        +activateCourse(String)
        +deactivateCourse(String)
    }

    class EnrollmentService {
        -EnrollmentRepository enrollmentRepository
        -StudentService studentService
        -CourseService courseService
        +enrollStudent(studentId, courseId) Enrollment
        +getEnrollmentsByStudent(String) List~Enrollment~
        +markCompleted(String)
        +markCancelled(String)
    }

    class IdGenerator {
        <<utility>>
        -static int studentCounter
        -static int courseCounter
        -static int enrollmentCounter
        +static generateStudentId() String
        +static generateCourseId() String
        +static generateEnrollmentId() String
    }

    class InputValidator {
        <<utility>>
        +static isValidEmail(String) boolean
        +static isNotNullOrEmpty(String) boolean
        +static isNonNegative(int) boolean
        +static isPositive(int) boolean
    }

    class EntityNotFoundException {
        +EntityNotFoundException(String)
    }

    class InvalidInputException {
        +InvalidInputException(String)
    }

    class Main {
        +static main(String[])
    }

    Person <|-- Student : extends
    Enrollment --> EnrollmentStatus : uses
    Course --> CourseStatus : uses

    StudentRepository --> Student : stores
    CourseRepository --> Course : stores
    EnrollmentRepository --> Enrollment : stores

    StudentService --> StudentRepository : uses
    CourseService --> CourseRepository : uses
    EnrollmentService --> EnrollmentRepository : uses
    EnrollmentService --> StudentService : depends on
    EnrollmentService --> CourseService : depends on

    Main --> StudentService : calls
    Main --> CourseService : calls
    Main --> EnrollmentService : calls

    StudentService ..> EntityNotFoundException : throws
    CourseService ..> EntityNotFoundException : throws
    EnrollmentService ..> EntityNotFoundException : throws
    StudentService ..> InvalidInputException : throws
    CourseService ..> InvalidInputException : throws
    EnrollmentService ..> InvalidInputException : throws
```

---

## 🚀 How to Compile & Run

### Prerequisites
- **Java 17+** (JDK)
- **Maven 3.8+** (or use the included Maven wrapper)

### Using Maven (recommended)

```bash
# 1. Compile the project
mvn compile

# 2. Run the application
mvn exec:java -Dexec.mainClass="com.airtribe.learntrack.Main"
```

### Using plain javac / java

```bash
# 1. From the project root, compile all sources
javac -d out -sourcepath src/main/java $(find src/main/java -name "*.java")

# 2. Run
java -cp out com.airtribe.learntrack.Main
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 💡 Key Design Decisions

See [`docs/Design_Notes.md`](docs/Design_Notes.md) for a detailed explanation of why ArrayList was chosen, how static members are used, and the inheritance hierarchy.

---

## 👥 Team Roles

| Member | Responsibility |
|--------|---------------|
| 1 | Entities, constructors, inheritance, encapsulation (`entity/`) |
| 2 | Services & business logic, console UI (`service/`, `Main.java`) |
| 3 | Utilities, exceptions, constants, documentation (`util/`, `exception/`, `constants/`, `docs/`) |

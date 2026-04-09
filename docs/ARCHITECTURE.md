# Architecture Documentation

## System Architecture Overview

The University Management System follows a **3-Tier Layered Architecture** designed for scalability, maintainability, and separation of concerns.

```
┌──────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                         │
│                  (Console Menu Interface)                      │
│  - Main Menu                                                   │
│  - Student Management Menu                                     │
│  - Course Management Menu                                      │
│  - Enrollment Management Menu                                  │
│  - Fee Calculation & Reports Menu                              │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ↓
┌──────────────────────────────────────────────────────────────┐
│                   BUSINESS LOGIC LAYER                        │
│              (Core Application Logic)                          │
│  - Student Service Logic                                       │
│  - Course Service Logic                                        │
│  - Enrollment Service Logic                                    │
│  - Fee Calculation Engine                                      │
│  - Waiver Calculation & GPA-based Logic                        │
│  - Report Generation                                           │
│  - Data Validation                                             │
│  - Business Rule Enforcement                                   │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ↓
┌──────────────────────────────────────────────────────────────┐
│                   DATA ACCESS LAYER                           │
│              (JDBC Database Operations)                        │
│  - PreparedStatement Execution                                 │
│  - SQL Query Execution                                         │
│  - Connection Management                                       │
│  - Transaction Handling                                        │
│  - Result Set Processing                                       │
└──────────────────────┬───────────────────────────────────────┘
                       │
                       ↓
┌──────────────────────────────────────────────────────────────┐
│                  PERSISTENCE LAYER                            │
│              (MySQL Database Server)                           │
│  - Students Table          - Enrollments Table                 │
│  - Courses Table           - Fees Table                        │
│  - Indexes & Constraints                                       │
│  - Foreign Key Relationships                                   │
└──────────────────────────────────────────────────────────────┘
```

---

## Component Diagram

```
UniversityManagementSystem.java (Main Class)
│
├── Database Connection Module
│   ├── connectToDatabase()
│   └── initializeDatabase()
│
├── Student Management Module
│   ├── addStudent()
│   ├── viewAllStudents()
│   ├── editStudent()
│   └── deleteStudent()
│
├── Course Management Module
│   ├── addCourse()
│   └── viewAllCourses()
│
├── Enrollment Management Module
│   ├── enrollStudent()
│   └── viewStudentEnrollments()
│
├── Fee Management Module
│   ├── calculateStudentFees()
│   ├── getWaiverPercentage()
│   └── viewFeeReports()
│
└── Menu System Module
    ├── displayMainMenu()
    ├── studentManagementMenu()
    ├── courseManagementMenu()
    ├── enrollmentManagementMenu()
    ├── feeManagementMenu()
    └── run()
```

---

## Data Flow Diagram

### Student Registration Flow

```
User Input: Student Details
         ↓
Input Validation (Name, Email, Email uniqueness)
         ↓
Build SQL INSERT Query with PreparedStatement
         ↓
Execute Query on Database
         ↓
Student Inserted with AUTO_INCREMENT ID
         ↓
Success Message with ID
         ↓
Database stores: id, name, email, phone, gpa, enrollment_date, status
```

### Fee Calculation Flow

```
User selects Student ID
         ↓
Retrieve Student Details (ID, Previous GPA)
         ↓
Query Student Enrollments (All courses)
         ↓
Calculate Total Credits (SUM of course credits)
         ↓
Get Waiver Percentage (Based on GPA)
         ↓
BASE FEE = Total Credits × 2800
         ↓
WAIVER AMOUNT = Base Fee × (Waiver % / 100)
         ↓
FINAL FEE = Base Fee - Waiver Amount
         ↓
INSERT into Fees Table
         ↓
Display Calculated Details
         ↓
Store in Database for Record-keeping
```

---

## Class Structure

### UniversityManagementSystem Class

**Attributes:**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db"
private static final String DB_USER = "root"
private static final String DB_PASSWORD = ""
private static final double FEE_PER_CREDIT = 2800.0
private Connection connection
private Scanner scanner
private boolean running
```

**Methods by Category:**

#### Database Methods
- `connectToDatabase()` - Establish MySQL connection via JDBC
- `initializeDatabase()` - Create tables if they don't exist
- `closeConnection()` - Close database connection

#### Student Management
- `addStudent()` - INSERT new student record
- `viewAllStudents()` - SELECT and display all students
- `editStudent()` - UPDATE student information (GPA, etc.)
- `deleteStudent()` - DELETE student record

#### Course Management
- `addCourse()` - INSERT new course
- `viewAllCourses()` - SELECT and display all courses

#### Enrollment Management
- `enrollStudent()` - INSERT enrollment relationship
- `viewStudentEnrollments()` - SELECT student's courses

#### Fee Management
- `calculateStudentFees()` - Complex fee calculation with waiver
- `getWaiverPercentage(double gpa)` - Determine waiver tier
- `viewFeeReports()` - Display fee information

#### UI Methods
- `displayMainMenu()` - Main menu
- `studentManagementMenu()` - Student CRUD menu
- `courseManagementMenu()` - Course management menu
- `enrollmentManagementMenu()` - Enrollment menu
- `feeManagementMenu()` - Fee calculation & reports menu
- `run()` - Main application loop

---

## Database Architecture

### Entity-Relationship Model

```
┌─────────────────┐              ┌──────────────────┐
│    STUDENTS     │              │     COURSES      │
├─────────────────┤              ├──────────────────┤
│ PK: student_id  │─────┐    ┌──│ PK: course_id    │
│    name         │     │    │   │    course_name   │
│    email        │     │ M:N│   │    course_code   │
│    phone        │     │    │   │    credits       │
│    previous_gpa │     │    │   │    department    │
│    status       │     │    │   │    description   │
└─────────────────┘     └────┼───┴──────────────────┘
        ▲                     │
        │                     │
        │              ┌──────▼──────────┐
        │              │  ENROLLMENTS    │
        │              ├─────────────────┤
        │              │ PK: enrollment_ │
        │              │     id          │
        │              │ FK: student_id  │
        │              │ FK: course_id   │
        │              │ enrollment_date │
        │              │ status          │
        │              └──────┬──────────┘
        │                     │
        │                     │
        │            ┌────────▼─────────┐
        └────────────┤      FEES        │
                     ├──────────────────┤
                     │ PK: fee_id       │
                     │ FK: student_id   │
                     │ total_credits    │
                     │ base_fee         │
                     │ waiver_percentage│
                     │ waiver_amount    │
                     │ final_fee        │
                     │ calculation_date │
                     └──────────────────┘
```

### Key Relationships

| Relationship | Type | Constraint |
|---|---|---|
| Students ↔ Enrollments | 1:M | FK with CASCADE DELETE |
| Courses ↔ Enrollments | 1:M | FK with CASCADE DELETE |
| Students ↔ Fees | 1:M | FK with CASCADE DELETE |

---

## Technology Stack Details

### Java (Presentation & Business Logic)
- **Version**: JDK 8 or higher
- **Pattern**: MVC (Model-View-Controller)
- **Features Used**:
  - Collections API for data management
  - Scanner for console input
  - Exception handling with try-catch
  - String formatting for output

### JDBC (Data Access Layer)
- **Driver**: MySQL Connector/J 8.0+
- **Connection Type**: Direct Connection (no pooling)
- **Statement Type**: PreparedStatement (prevents SQL injection)
- **Features**:
  - Connection management
  - Result set processing
  - Exception handling

### MySQL (Database)
- **Version**: 5.7 or higher
- **Storage Engine**: InnoDB (transactional)
- **Features**:
  - Foreign Key Constraints
  - Indexes on all FK columns
  - Auto-increment primary keys
  - UTF-8 charset support

---

## Design Patterns Used

### 1. **Singleton Pattern**
- Single database connection maintained throughout application lifecycle
- Prevents multiple connection leaks

### 2. **Repository Pattern**
- Data access methods encapsulated in main class
- Clean separation between business logic and data access

### 3. **Menu Pattern**
- Recursive menu methods for navigation
- State management through application run loop

### 4. **Validation Pattern**
- Input validation before database operations
- Constraint enforcement at both application and database levels

---

## Processing Architecture

### Request-Response Cycle

```
User Input
    ↓
Input Validation
    ↓
Menu Route Decision
    ↓
Get Input Parameters
    ↓
Validate Parameters
    ↓
Build SQL Query (with PreparedStatement)
    ↓
Execute Query
    ↓
Process Results
    ↓
Display Output
    ↓
Return to Menu
```

---

## Error Handling Architecture

```
Database Operation (SQL/JDBC)
    ↓
    ├─→ SQLException (Database Error)
    │   └─→ Log Error & Display User-Friendly Message
    │
    ├─→ InputMismatchException (Invalid User Input)
    │   └─→ Clear Scanner Buffer & Prompt Again
    │
    └─→ Other Exceptions (Unexpected)
        └─→ Log & Display Generic Error Message
```

---

## Performance Considerations

### Indexing Strategy

```sql
-- Indexes created for faster queries
CREATE INDEX idx_student_id ON enrollments(student_id);
CREATE INDEX idx_course_id ON enrollments(course_id);
CREATE INDEX idx_fee_student_id ON fees(student_id);
```

### Query Optimization

| Operation | Optimization |
|-----------|---------------|
| Find student enrollments | Indexed FK on enrollments |
| Calculate total credits | SUM with indexed query |
| Retrieve student fees | Direct indexed lookup |
| List all courses | Simple SELECT (typically < 50 rows) |

---

## Scalability Roadmap

**Current**: Single-threaded console application

**Future Improvements**:
1. **Connection Pooling**: HikariCP or Apache DBCP
2. **REST API Layer**: Spring Boot framework
3. **Caching Layer**: Redis for frequently accessed data
4. **Load Balancing**: Multiple database replicas
5. **Microservices**: Separate services for fees, enrollment, reports
6. **Async Processing**: Message queues for fee calculations
7. **API Gateway**: For future web/mobile clients

---

## Security Architecture

### Current Implementation
- ✅ PreparedStatements (SQL Injection Prevention)
- ✅ Input Validation
- ✅ Foreign Key Constraints
- ✅ Transaction Management

### Recommended Future Enhancements
- 🔒 Authentication (JWT tokens)
- 🔒 Authorization (Role-based access: admin, accountant, student)
- 🔒 Encryption (database credentials, sensitive data)
- 🔒 Audit Logging (track all modifications)
- 🔒 Rate Limiting (prevent abuse)

---

## Deployment Architecture

```
Development Environment
    ├── Local MySQL Server (3306)
    └── Java IDE (Eclipse/IntelliJ)
         ↓
       Build
         ↓
Staging Environment
    ├── Staging MySQL Database
    └── Testing
         ↓
       Test Pass
         ↓
Production Environment
    ├── Production MySQL Database
    ├── Database Backups
    ├── Monitoring
    └── Alerting
```

---

**Document Version**: 1.0  
**Last Updated**: April 2026  
**Maintained By**: Himel

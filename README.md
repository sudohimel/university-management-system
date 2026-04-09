# University Management System - Fees Management Backend

![Java](https://img.shields.io/badge/Language-Java-red?logo=java)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)
![Backend](https://img.shields.io/badge/Type-Backend%20System-green)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen)

A comprehensive **University Management System** backend application for handling student fees management. Built with Java and MySQL, it automates fee calculation with GPA-based scholarship waivers, student enrollment, and course management. Perfect for academic institutions needing a flexible fees management solution.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Fee Calculation Logic](#fee-calculation-logic)
- [Installation & Setup](#installation--setup)
- [Usage Guide](#usage-guide)
- [Demo & Examples](#demo--examples)
- [Fee Structure](#fee-structure)
- [Learning Outcomes](#learning-outcomes)
- [API Operations](#api-operations)
- [Future Enhancements](#future-enhancements)
- [License](#license)

---

## 🎓 Project Overview

**University Management System** is a professional-grade backend application designed for university administration to manage student fees with intelligent scholarship waiver systems. The application handles:

- **Student Management** – Register, update, and manage student records
- **Course Management** – Create and organize courses with credit systems
- **Enrollment System** – Enroll students in courses and track enrollments
- **Automated Fee Calculation** – Calculate fees based on credits and GPA
- **Scholarship Waivers** – Apply automatic waivers based on previous academic performance
- **Fee Reporting** – Generate detailed fee reports for accounting

**Tech Stack:** Java 8+ | MySQL 5.7+ | JDBC API  
**Course:** University Management & Database Systems  
**Institution:** Academic Project  
**Year:** 2026  

---

## ✨ Features

### 🎯 Core Functionality

✅ **Student Management**
   - Add new students with GPA
   - View all students with details
   - Edit student information (GPA updates)
   - Delete student records
   - Track enrollment dates and status

✅ **Course Management**
   - Add courses with credit values (1.0, 1.5, 2.0, 3.0)
   - Unique course codes for identification
   - Department categorization
   - Course descriptions
   - View all available courses

✅ **Enrollment System**
   - Enroll students in courses
   - Track enrollment status
   - View student enrollments with course details
   - Multiple course enrollment per student
   - Enrollment date tracking

✅ **Fee Calculation & Waivers**
   - Fixed fee: **2800 TK per credit**
   - GPA-based automatic waivers:
     - **3.50-4.00 GPA:** 50% waiver
     - **3.00-3.49 GPA:** 40% waiver
     - **2.50-2.99 GPA:** 30% waiver
     - **Below 2.50:** No waiver
   - Automatic waiver amount calculation
   - Final fee calculation after waiver

✅ **Reporting & Analytics**
   - Comprehensive fee reports
   - Student-wise fee breakdown
   - Waiver analysis
   - Total fees and collections data
   - Historical fee records

✅ **Database Operations**
   - CRUD operations for all entities
   - Referential integrity with foreign keys
   - Cascade delete handling
   - Indexed queries for performance
   - Transaction support

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | JDK 8+ |
| **Database** | MySQL | 5.7+ |
| **JDBC** | MySQL Connector | 8.0+ |
| **UI** | Console-based CLI | N/A |
| **Architecture** | MVC Pattern | N/A |
| **Connection Pool** | Direct JDBC | N/A |

---

## 🏗️ System Architecture

### Layered Architecture:

```
┌─────────────────────────────────────────────────┐
│       Console Menu Interface (UI Layer)         │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│    Business Logic Layer                         │
│  - Fee Calculation                              │
│  - Waiver Logic                                 │
│  - Validation                                   │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│    Data Access Layer (JDBC)                     │
│  - CRUD Operations                              │
│  - Database Queries                             │
│  - Connection Management                        │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│         MySQL Database (Persistence)            │
│  - Students Table                               │
│  - Courses Table                                │
│  - Enrollments Table                            │
│  - Fees Table                                   │
└─────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
university-management-system/
│
├── src/
│   └── UniversityManagementSystem.java      # Main application with all logic
│       ├── Database connection
│       ├── Student management
│       ├── Course management
│       ├── Enrollment management
│       ├── Fee calculation
│       └── Menu system
│
├── database_setup.sql                       # MySQL database initialization script
├── README.md                                # Project documentation
├── LICENSE                                  # MIT License
├── .gitignore                              # Git ignore rules
└── lib/
    └── mysql-connector-java-8.0.x.jar      # MySQL JDBC Driver (download required)
```

---

## 📊 Database Schema

### Students Table
```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    previous_gpa DOUBLE DEFAULT 0.0,
    enrollment_date DATE,
    status VARCHAR(20)
);
```

### Courses Table
```sql
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    course_code VARCHAR(20) UNIQUE,
    credits DOUBLE NOT NULL,
    department VARCHAR(50),
    description TEXT
);
```

### Enrollments Table
```sql
CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date DATE,
    status VARCHAR(20),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
```

### Fees Table
```sql
CREATE TABLE fees (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    total_credits DOUBLE,
    base_fee DOUBLE,
    waiver_percentage DOUBLE,
    waiver_amount DOUBLE,
    final_fee DOUBLE,
    calculation_date DATE,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);
```

---

## 💰 Fee Calculation Logic

### Formula:

```
Base Fee = Total Credits × 2800 TK/credit

Waiver Percentage = Based on GPA:
    - GPA ≥ 3.50: 50%
    - GPA ≥ 3.00: 40%
    - GPA ≥ 2.50: 30%
    - else: 0%

Waiver Amount = Base Fee × (Waiver Percentage / 100)

Final Fee = Base Fee - Waiver Amount
```

### Example Calculation:

```
Student: Ahmed (GPA: 3.75, Credits: 8)

Step 1: Base Fee = 8 credits × 2800 = 22,400 TK
Step 2: GPA 3.75 → Waiver = 50%
Step 3: Waiver Amount = 22,400 × 0.50 = 11,200 TK
Step 4: Final Fee = 22,400 - 11,200 = 11,200 TK

Result: Ahmed pays 11,200 TK (saved 11,200 TK)
```

### Another Example:

```
Student: Fatima (GPA: 2.70, Credits: 6)

Step 1: Base Fee = 6 credits × 2800 = 16,800 TK
Step 2: GPA 2.70 → Waiver = 30%
Step 3: Waiver Amount = 16,800 × 0.30 = 5,040 TK
Step 4: Final Fee = 16,800 - 5,040 = 11,760 TK

Result: Fatima pays 11,760 TK (saved 5,040 TK)
```

---

## 🚀 Installation & Setup

### Prerequisites

1. **Java Development Kit (JDK)** – Version 8 or higher
   ```bash
   java -version
   ```

2. **MySQL Server** – Version 5.7 or higher
   ```bash
   mysql --version
   ```

3. **MySQL JDBC Driver** – Download from [MySQL Official Site](https://dev.mysql.com/downloads/connector/j/)

### Step 1: Clone Repository

```bash
git clone https://github.com/sudohimel/university-management-system.git
cd university-management-system
```

### Step 2: Download MySQL JDBC Driver

1. Download `mysql-connector-java-8.0.x.jar`
2. Create `lib` folder in project root
3. Place the JAR file in `lib/` folder

### Step 3: Create Database

```bash
mysql -u root -p < database_setup.sql
```

Or manually:
```bash
mysql -u root -p
CREATE DATABASE university_db;
USE university_db;
-- Run all SQL commands from database_setup.sql
```

### Step 4: Configure Database Connection

Edit `src/UniversityManagementSystem.java` and update:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
private static final String DB_USER = "root";     // Your MySQL username
private static final String DB_PASSWORD = "";      // Your MySQL password
```

### Step 5: Compile & Run

```bash
# Compile with JDBC driver in classpath
javac -cp lib/mysql-connector-java-8.0.x.jar src/UniversityManagementSystem.java

# Run with JDBC driver
java -cp lib/mysql-connector-java-8.0.x.jar:src UniversityManagementSystem
```

---

## 📖 Usage Guide

### Main Menu Navigation

```
MAIN MENU
1. Student Management
2. Course Management
3. Enrollment Management
4. Fee Calculation & Waiver
5. Reports & View Data
6. Exit
```

### Student Management Workflow

```
1. Add New Student
   - Name: Ahmed Hassan
   - Email: ahmed@university.edu
   - Phone: 0171234567
   - Previous GPA: 3.75

2. View All Students (Lists all registered students)

3. Edit Student (Update GPA based on recent grades)

4. Delete Student (Remove from system)
```

### Fee Calculation Workflow

```
1. Student enrolls in courses (e.g., 3 courses of 3, 2, 1.5 credits)
2. Total Credits = 6.5
3. Base Fee = 6.5 × 2800 = 18,200 TK
4. GPA = 3.45 → 40% waiver
5. Waiver Amount = 18,200 × 0.40 = 7,280 TK
6. Final Fee = 18,200 - 7,280 = 10,920 TK
```

---

## 🎮 Demo & Examples

### Complete Demo Run

**Terminal Session:**

```
====================================================================
UNIVERSITY MANAGEMENT SYSTEM - STARTUP
====================================================================
Initializing...

✓ Connected to MySQL Database
✓ Database tables initialized

====================================================================
UNIVERSITY MANAGEMENT SYSTEM - FEES MANAGEMENT
====================================================================
1. Student Management
2. Course Management
3. Enrollment Management
4. Fee Calculation & Waiver
5. Reports & View Data
6. Exit

Enter your choice (1-6): 1

====================================================================
STUDENT MANAGEMENT
====================================================================
1. Add New Student
2. View All Students
3. Edit Student (Update GPA)
4. Delete Student
5. Back to Main Menu

Enter your choice (1-5): 1

====================================================================
ADD NEW STUDENT
====================================================================
Student Name: Mehrab Hassan
Email: mehrab@university.edu
Phone: 01912345678
Previous Semester GPA (0.0-4.0): 3.75

✓ Student added successfully!

[Return to menu and continue...]

Enter your choice (1-5): 2

====================================================================
ALL STUDENTS
====================================================================
ID     Name                  Email                          Phone         GPA    Status
------
1      Mehrab Hassan         mehrab@university.edu          01912345678   3.75   Active
2      Fatima Khan           fatima@university.edu          01912345679   2.70   Active
3      Ahmed Ali             ahmed@university.edu           01912345680   3.45   Active

Enter your choice (1-5): 5

Successfully returned to main menu...

Enter your choice (1-6): 4

====================================================================
FEE CALCULATION & WAIVER
====================================================================
Waiver Structure:
  GPA >= 3.50: 50% waiver
  GPA >= 3.00: 40% waiver
  GPA >= 2.50: 30% waiver
  GPA < 2.50: No waiver

1. Calculate & Store Student Fees
2. View Fee Reports
3. Back to Main Menu

Enter your choice (1-3): 1

====================================================================
CALCULATE & STORE STUDENT FEES
====================================================================
Enter Student ID: 1

============================================================
FEE CALCULATION RESULTS
============================================================
Student ID: 1
Previous GPA: 3.75
Total Credits: 8.0
Base Fee (2800 TK/credit): 22400.00 TK
Waiver Percentage: 50.0%
Waiver Amount: 11200.00 TK
FINAL FEE: 11200.00 TK
============================================================
✓ Fees calculated and stored successfully!
```

---

## 💰 Fee Structure

### Credit System

| Credits | Type | Example Courses |
|---------|------|-----------------|
| 1.0 CR | Minor | English Composition |
| 1.5 CR | Standard | Physical Education |
| 2.0 CR | Standard | Web Development |
| 3.0 CR | Major | Data Structures, Database |

### Fee per Credit: **2800 TK**

### GPA-based Waiver Table

| GPA Range | Waiver | Example |
|-----------|--------|---------|
| 3.50-4.00 | 50% | Base 20,000 → Pay 10,000 |
| 3.00-3.49 | 40% | Base 20,000 → Pay 12,000 |
| 2.50-2.99 | 30% | Base 20,000 → Pay 14,000 |
| Below 2.50 | 0% | Base 20,000 → Pay 20,000 |

---

## 📚 Learning Outcomes

Through this project, you learn:

✔️ **Database Design** – Designing relational schemas with proper normalization  
✔️ **JDBC Programming** – Connecting and querying MySQL from Java  
✔️ **SQL Operations** – INSERT, SELECT, UPDATE, DELETE with PreparedStatements  
✔️ **Transaction Management** – ACID properties and data consistency  
✔️ **Business Logic** – Implementing complex fee calculation algorithms  
✔️ **Application Architecture** – Layered design with separation of concerns  
✔️ **Menu-Driven Systems** – Building interactive CLI applications  
✔️ **Data Validation** – Ensuring data integrity and constraint handling  
✔️ **Reporting** – Generating formatted reports from database  
✔️ **Error Handling** – Managing exceptions in database operations  

---

## 🔌 API Operations

### Student Operations

| Operation | Input | Output |
|-----------|-------|--------|
| Add Student | Name, Email, Phone, GPA | Student added, ID assigned |
| View Students | None | All students list |
| Edit Student | Student ID, New GPA | Student updated |
| Delete Student | Student ID | Student removed |

### Course Operations

| Operation | Input | Output |
|-----------|-------|--------|
| Add Course | Name, Code, Credits, Dept | Course added, ID assigned |
| View Courses | None | All courses list |

### Enrollment Operations

| Operation | Input | Output |
|-----------|-------|--------|
| Enroll Student | Student ID, Course ID | Enrollment created |
| View Enrollments | Student ID | Student's courses list |

### Fee Operations

| Operation | Input | Output |
|-----------|-------|--------|
| Calculate Fees | Student ID | Fee record stored |
| View Fee Report | Student ID (optional) | Detailed fee report |

---

## 🔮 Future Enhancements

- [ ] **Web Interface** – Spring Boot REST API
- [ ] **Authentication** – User login with roles (Admin, Accountant, Student)
- [ ] **Advanced Reports** – PDF generation, chart analysis
- [ ] **Payment Integration** – Online payment gateway
- [ ] **Email Notifications** – Send fee notices to students
- [ ] **Semester Management** – Multiple semesters support
- [ ] **Progress Tracking** – Monitor student grades over time
- [ ] **Archive System** – Historical data retention and access
- [ ] **Auditing** – Track all modifications with timestamps
- [ ] **Mobile App** – Android/iOS companion application
- [ ] **Batch Operations** – Import students/courses from CSV
- [ ] **API Documentation** – Swagger/OpenAPI specs

---

## 🏆 Performance Considerations

| Factor | Impact | Optimization |
|--------|--------|--------------|
| Database Indexing | Query speed | Indexed student_id, course_id |
| Referential Integrity | Data consistency | Foreign keys with CASCADE |
| Prepared Statements | SQL injection prevention | Used throughout |
| Connection Management | Resource usage | Connection closed after use |

---

## 📄 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Himel** – Backend Developer | CSE Graduate  
GitHub: [@sudohimel](https://github.com/sudohimel)

**Academic Course:** University Management Systems  
**Database Course:** Advanced Database Design  
**Year:** 2026

---

## � Full Documentation

This project includes comprehensive documentation in the `docs/` folder:

- **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** – System architecture, design patterns, layered architecture
- **[API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)** – All database operations, current JDBC implementation, future REST API spec
- **[DATABASE_SCHEMA.md](docs/DATABASE_SCHEMA.md)** – Complete ER diagram, table definitions, indexes, query optimization
- **[FLOW.md](docs/FLOW.md)** – Business process flows, fee calculation workflow, student lifecycle

Visual evidence and screenshots: [screenshots/](screenshots/)

---

## �📚 References

- [MySQL Official Documentation](https://dev.mysql.com/doc/)
- [Java JDBC Programming Guide](https://docs.oracle.com/en/java/)
- [Database Design Principles](https://en.wikipedia.org/wiki/Database_design)
- [Relational Database Theory](https://en.wikipedia.org/wiki/Relational_database)

---

**Made with ❤️ for academic excellence - 2026**

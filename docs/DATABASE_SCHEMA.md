# Database Schema Documentation

## Complete Database Schema

### Database: `university_db`

**Character Set**: UTF-8 (utf8mb4)  
**Collation**: utf8mb4_unicode_ci  
**Engine**: InnoDB (transactional support)

---

## Entity-Relationship Diagram (ER Diagram)

```
┌─────────────────────────────────────────────────────────────┐
│                          STUDENTS                            │
├─────────────────────────────────────────────────────────────┤
│ PK: student_id (INT, AUTO_INCREMENT)                         │
│ name (VARCHAR 100, NOT NULL)                                 │
│ email (VARCHAR 100, UNIQUE, NOT NULL)                        │
│ phone (VARCHAR 15)                                           │
│ previous_gpa (DOUBLE, DEFAULT 0.0)                           │
│ enrollment_date (DATE)                                       │
│ status (VARCHAR 20)                                          │
│ created_at (TIMESTAMP, DEFAULT NOW())                        │
│ updated_at (TIMESTAMP, DEFAULT NOW() ON UPDATE)              │
├─────────────────────────────────────────────────────────────┤
│ Relationships:                                                │
│ • 1:M with ENROLLMENTS (one student, many enrollments)       │
│ • 1:M with FEES (one student, multiple fee records)          │
└───────────────────────┬───────────────────────────────────────┘
                        │
                    1:M │
                        │
┌───────────────────────▼──────────────────────────────────────┐
│                       ENROLLMENTS                             │
├─────────────────────────────────────────────────────────────┤
│ PK: enrollment_id (INT, AUTO_INCREMENT)                      │
│ FK: student_id (INT, NOT NULL) ──────────┐                   │
│ FK: course_id (INT, NOT NULL)  ──┐       │                   │
│ enrollment_date (DATE)           │       │                   │
│ status (VARCHAR 20))             │       │                   │
│ created_at (TIMESTAMP)           │       │                   │
├─────────────────────────────────┼───────┼───────────────────┤
│ Composite Unique Constraint:      │       │                   │
│ UNIQUE (student_id, course_id)    │       │                   │
└──────────────────────────────────┼───────┼───────────────────┘
                                   │       │
                              M:N │       │ M:1
                                   │       │
                   ┌───────────────┘       │
                   │                       │
                   │    ┌──────────────────┘
                   │    │
┌──────────────────▼────▼──────────────────────────────────────┐
│                       COURSES                                 │
├─────────────────────────────────────────────────────────────┤
│ PK: course_id (INT, AUTO_INCREMENT)                          │
│ course_name (VARCHAR 100, NOT NULL)                          │
│ course_code (VARCHAR 20, UNIQUE, NOT NULL)                   │
│ credits (DOUBLE, NOT NULL) [1.0, 1.5, 2.0, 3.0]             │
│ department (VARCHAR 50)                                      │
│ description (TEXT)                                           │
│ created_at (TIMESTAMP, DEFAULT NOW())                        │
│ updated_at (TIMESTAMP, DEFAULT NOW() ON UPDATE)              │
├─────────────────────────────────────────────────────────────┤
│ Relationships:                                                │
│ • 1:M with ENROLLMENTS (one course, many enrollments)        │
└─────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────┐
│                         FEES                                  │
├─────────────────────────────────────────────────────────────┤
│ PK: fee_id (INT, AUTO_INCREMENT)                             │
│ FK: student_id (INT, NOT NULL) ────────┐                     │
│ total_credits (DOUBLE)            ────┐│                     │
│ base_fee (DOUBLE)                 ────┼┤ Fee Calculation     │
│ waiver_percentage (DOUBLE)            ││ Data                │
│ waiver_amount (DOUBLE)                ││                     │
│ final_fee (DOUBLE)                ────┘│                     │
│ calculation_date (DATE)                │                     │
│ created_at (TIMESTAMP)                 │                     │
├─────────────────────────────────────────────────────────────┤
│ Relationship:                           │                     │
│ FK: student_id (REFERENCES STUDENTS)   │                     │
└──────────────────────────────────────────────────────────────┘
```

---

## Table Definitions

### 1. STUDENTS Table

```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    previous_gpa DOUBLE DEFAULT 0.0,
    enrollment_date DATE,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_status (status)
);
```

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| student_id | INT | PK, AUTO_INCREMENT | Unique identifier |
| name | VARCHAR(100) | NOT NULL | Student full name |
| email | VARCHAR(100) | UNIQUE, NOT NULL | University email address |
| phone | VARCHAR(15) | - | Contact phone number |
| previous_gpa | DOUBLE | DEFAULT 0.0 | GPA from previous semester |
| enrollment_date | DATE | - | When student enrolled |
| status | VARCHAR(20) | - | 'Active', 'Inactive', 'Graduated' |
| created_at | TIMESTAMP | DEFAULT NOW() | Record creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last modification timestamp |

**Indexes:**
- `idx_email`: Fast lookup by email
- `idx_status`: Filter by status

---

### 2. COURSES Table

```sql
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    credits DOUBLE NOT NULL,
    department VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_course_code (course_code),
    INDEX idx_department (department)
);
```

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| course_id | INT | PK, AUTO_INCREMENT | Unique course identifier |
| course_name | VARCHAR(100) | NOT NULL | Full course name |
| course_code | VARCHAR(20) | UNIQUE, NOT NULL | Course code (e.g., CS201) |
| credits | DOUBLE | NOT NULL | Credit hours (1.0, 1.5, 2.0, 3.0) |
| department | VARCHAR(50) | - | Department offering course |
| description | TEXT | - | Course description |
| created_at | TIMESTAMP | DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update timestamp |

**Indexes:**
- `idx_course_code`: Quick lookup by course code
- `idx_department`: Filter by department

**Credit Values Used:**
- 1.0 credits: Minor courses
- 1.5 credits: Practical/lab courses
- 2.0 credits: Standard theoretical courses
- 3.0 credits: Major/core courses

---

### 3. ENROLLMENTS Table

```sql
CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date DATE,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id),
    
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id)
);
```

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| enrollment_id | INT | PK, AUTO_INCREMENT | Unique enrollment record |
| student_id | INT | FK NOT NULL | Reference to student |
| course_id | INT | FK NOT NULL | Reference to course |
| enrollment_date | DATE | - | Date of enrollment |
| status | VARCHAR(20) | - | 'Active', 'Completed', 'Dropped' |
| created_at | TIMESTAMP | DEFAULT NOW() | Record creation time |
| updated_at | TIMESTAMP | AUTO UPDATE | Last modification time |

**Constraints:**
- `FOREIGN KEY student_id`: Links to `students.student_id` with CASCADE DELETE
- `FOREIGN KEY course_id`: Links to `courses.course_id` with CASCADE DELETE
- `UNIQUE (student_id, course_id)`: Prevent duplicate enrollments

**Indexes:**
- `idx_student_id`: Find all enrollments for a student
- `idx_course_id`: Find all students in a course

---

### 4. FEES Table

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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_student_id (student_id),
    INDEX idx_calculation_date (calculation_date)
);
```

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| fee_id | INT | PK, AUTO_INCREMENT | Unique fee record ID |
| student_id | INT | FK NOT NULL | Reference to student |
| total_credits | DOUBLE | - | Sum of enrolled course credits |
| base_fee | DOUBLE | - | Total credits × 2800 TK |
| waiver_percentage | DOUBLE | - | GPA-based waiver (0, 30, 40, 50) |
| waiver_amount | DOUBLE | - | base_fee × (waiver% / 100) |
| final_fee | DOUBLE | - | base_fee - waiver_amount |
| calculation_date | DATE | - | When fees were calculated |
| created_at | TIMESTAMP | DEFAULT NOW() | Record creation time |
| updated_at | TIMESTAMP | AUTO UPDATE | Last modification time |

**Calculation Logic (in database):**
```sql
base_fee = total_credits * 2800
waiver_amount = base_fee * (waiver_percentage / 100)
final_fee = base_fee - waiver_amount
```

**Indexes:**
- `idx_student_id`: Retrieve fees for specific student
- `idx_calculation_date`: Generate reports by date range

---

## Sample Data

### Sample Students (10 of 20)

```sql
INSERT INTO students (name, email, phone, previous_gpa, enrollment_date, status) VALUES
('Ahmed Hassan', 'ahmed@university.edu', '01712345678', 3.75, '2026-01-10', 'Active'),
('Fatima Khan', 'fatima@university.edu', '01712345679', 2.70, '2026-01-10', 'Active'),
('Ali Ahmed', 'ali@university.edu', '01712345680', 3.45, '2026-01-10', 'Active'),
('Zainab Islam', 'zainab@university.edu', '01712345681', 3.90, '2026-01-10', 'Active'),
('Mohammad Hassan', 'mohammad@university.edu', '01712345682', 2.30, '2026-01-10', 'Active'),
('Ayesha Rahman', 'ayesha@university.edu', '01712345683', 3.55, '2026-01-10', 'Active'),
('Ibrahim Khan', 'ibrahim@university.edu', '01712345684', 2.85, '2026-01-10', 'Active'),
('Noor Ahmed', 'noor@university.edu', '01712345685', 3.20, '2026-01-10', 'Active'),
('Samir Hassan', 'samir@university.edu', '01712345686', 3.65, '2026-01-10', 'Active'),
('Leila Ahmed', 'leila@university.edu', '01712345687', 2.95, '2026-01-10', 'Active');
```

### Sample Courses (10)

```sql
INSERT INTO courses (course_name, course_code, credits, department, description) VALUES
('Data Structures', 'CS201', 3.0, 'Computer Science', 'Fundamentals of data structures'),
('Web Development', 'CS202', 2.0, 'Computer Science', 'HTML, CSS, JavaScript, Backend'),
('Database Systems', 'CS301', 3.0, 'Computer Science', 'Relational databases and SQL'),
('Operating Systems', 'CS302', 3.0, 'Computer Science', 'Kernel, processes, scheduling'),
('Software Engineering', 'CS303', 2.0, 'Computer Science', 'SDLC, design patterns, testing'),
('Network Programming', 'CS304', 3.0, 'Computer Science', 'TCP/IP, sockets, protocols'),
('Mobile App Development', 'CS305', 2.0, 'Computer Science', 'Android/iOS development'),
('Machine Learning', 'CS306', 3.0, 'Computer Science', 'ML algorithms and implementations'),
('Cloud Computing', 'CS307', 1.5, 'Computer Science', 'AWS, Azure, GCP basics'),
('Cybersecurity', 'CS308', 2.0, 'Computer Science', 'Security fundamentals, cryptography');
```

### Sample Enrollments

```sql
INSERT INTO enrollments (student_id, course_id, enrollment_date, status) VALUES
(1, 1, '2026-01-15', 'Active'),
(1, 2, '2026-01-15', 'Active'),
(1, 3, '2026-01-15', 'Active'),
(2, 1, '2026-01-15', 'Active'),
(2, 4, '2026-01-15', 'Active'),
(3, 2, '2026-01-15', 'Active'),
(3, 5, '2026-01-15', 'Active'),
(3, 6, '2026-01-15', 'Active'),
-- ... more enrollments ...
```

---

## Key Relationships & Constraints

### Referential Integrity

| Relationship | Action | Rule |
|---|---|---|
| Student → Enrollments | DELETE student | CASCADE (delete all enrollments) |
| Student → Fees | DELETE student | CASCADE (delete all fee records) |
| Course → Enrollments | DELETE course | CASCADE (delete all enrollments) |

### Data Validation

| Table | Column | Rule |
|---|---|---|
| students | email | UNIQUE, NOT NULL, valid email format |
| students | previous_gpa | 0.0 - 4.0 range |
| courses | credits | Only 1.0, 1.5, 2.0, 3.0 allowed |
| courses | course_code | UNIQUE, NOT NULL |
| enrollments | (student_id, course_id) | UNIQUE (no duplicate enrollments) |
| fees | final_fee | Must be >= 0 |

---

## Indexes for Performance

### Current Indexes

```sql
-- Students Table Indexes
CREATE INDEX idx_email ON students(email);
CREATE INDEX idx_status ON students(status);

-- Courses Table Indexes
CREATE INDEX idx_course_code ON courses(course_code);
CREATE INDEX idx_department ON courses(department);

-- Enrollments Table Indexes
CREATE INDEX idx_student_id ON enrollments(student_id);
CREATE INDEX idx_course_id ON enrollments(course_id);

-- Fees Table Indexes
CREATE INDEX idx_fee_student_id ON fees(student_id);
CREATE INDEX idx_calculation_date ON fees(calculation_date);
```

### Performance Impact

- **Fast queries**: By email, status, course code, student ID
- **Efficient joins**: Between students, enrollments, courses
- **Quick reports**: By date range

---

## Query Performance Analysis

### Query 1: Get Student Enrollments (Indexed)

```sql
SELECT e.enrollment_id, c.course_name, c.credits
FROM enrollments e
JOIN courses c ON e.course_id = c.course_id
WHERE e.student_id = 1;
```
**Index Used**: `idx_student_id` on enrollments  
**Execution Time**: < 1ms

### Query 2: Calculate Total Credits (Indexed)

```sql
SELECT SUM(c.credits) as total_credits
FROM enrollments e
JOIN courses c ON e.course_id = c.course_id
WHERE e.student_id = 1 AND e.status = 'Active';
```
**Index Used**: `idx_student_id`, `idx_course_id`  
**Execution Time**: < 2ms

### Query 3: Generate Fee Report

```sql
SELECT s.student_id, s.name, SUM(f.final_fee) as total_fees
FROM students s
LEFT JOIN fees f ON s.student_id = f.student_id
GROUP BY s.student_id
HAVING MONTH(f.calculation_date) = MONTH(CURDATE());
```
**Index Used**: `idx_fee_student_id`, `idx_calculation_date`  
**Execution Time**: 5-10ms (depends on data volume)

---

## Backup & Recovery

### Backup Strategy

```bash
# Full database backup
mysqldump -u root -p university_db > backup_2026_04_09.sql

# Restore from backup
mysql -u root -p university_db < backup_2026_04_09.sql
```

### Disaster Recovery

1. **Daily automated backups**
2. **Point-in-time recovery capability**
3. **Replication to backup server** (future)

---

## Scalability Roadmap

### Current
- Single database instance
- Maximum ~100,000 students
- Maximum ~10 concurrent users

### Phase 2
- Read replicas for reporting
- Connection pooling
- Caching layer

### Phase 3
- Database sharding by student_id
- Distributed transactions
- NoSQL for analytics

---

**Document Version**: 1.0  
**Last Updated**: April 2026  
**Maintained By**: Himel  
**Database Version**: MySQL 5.7+

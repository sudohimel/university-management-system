# API Documentation

## REST API Endpoints (Future Implementation)

Currently, the system uses a Console-based CLI. Below is the API structure that will be implemented in the future using **Spring Boot + Swagger/OpenAPI 3.0**.

---

## Base URL (Future)

```
http://localhost:8080/api/v1
```

---

## Authentication (Future)

```
Header: Authorization: Bearer <JWT_TOKEN>
```

---

## Current Implementation - Console-Based Operations

Since this is a backend system with console interface, operations are performed through the menu system. Below are the equivalent API operations:

---

## Student Endpoints

### Operation 1: Create Student

**Menu Path**: Main Menu → 1 → 1

**Current**: Console Input  
**Future REST**:
```http
POST /api/v1/students
Content-Type: application/json

{
  "name": "Ahmed Hassan",
  "email": "ahmed@university.edu",
  "phone": "01712345678",
  "previousGpa": 3.75
}
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "Ahmed Hassan",
  "email": "ahmed@university.edu",
  "phone": "01712345678",
  "previousGpa": 3.75,
  "enrollmentDate": "2026-04-09",
  "status": "Active",
  "createdAt": "2026-04-09T10:30:00Z"
}
```

**Error Response (400)**:
```json
{
  "error": "Email already exists",
  "status": 400,
  "timestamp": "2026-04-09T10:30:00Z"
}
```

---

### Operation 2: Get All Students

**Menu Path**: Main Menu → 1 → 2

**Current**: Console Display  
**Future REST**:
```http
GET /api/v1/students
```

**Response (200 OK)**:
```json
{
  "data": [
    {
      "id": 1,
      "name": "Ahmed Hassan",
      "email": "ahmed@university.edu",
      "phone": "01712345678",
      "previousGpa": 3.75,
      "enrollmentDate": "2026-04-09",
      "status": "Active"
    },
    {
      "id": 2,
      "name": "Fatima Khan",
      "email": "fatima@university.edu",
      "phone": "01712345679",
      "previousGpa": 2.70,
      "enrollmentDate": "2026-04-09",
      "status": "Active"
    }
  ],
  "total": 2,
  "timestamp": "2026-04-09T10:30:00Z"
}
```

---

### Operation 3: Get Student by ID

**Future REST**:
```http
GET /api/v1/students/{studentId}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Ahmed Hassan",
  "email": "ahmed@university.edu",
  "phone": "01712345678",
  "previousGpa": 3.75,
  "enrollmentDate": "2026-04-09",
  "status": "Active",
  "enrollments": [
    {
      "enrollmentId": 1,
      "courseId": 1,
      "courseName": "Data Structures",
      "credits": 3.0,
      "enrollmentDate": "2026-04-09"
    }
  ]
}
```

---

### Operation 4: Update Student (GPA)

**Menu Path**: Main Menu → 1 → 3

**Current**: Console Input  
**Future REST**:
```http
PUT /api/v1/students/{studentId}
Content-Type: application/json

{
  "previousGpa": 3.85
}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Ahmed Hassan",
  "email": "ahmed@university.edu",
  "previousGpa": 3.85,
  "updated": true,
  "updatedAt": "2026-04-09T11:00:00Z"
}
```

---

### Operation 5: Delete Student

**Menu Path**: Main Menu → 1 → 4

**Current**: Console Input  
**Future REST**:
```http
DELETE /api/v1/students/{studentId}
```

**Response (204 No Content)**:
```
Status: 204
```

**Error Response (404)**:
```json
{
  "error": "Student not found",
  "status": 404
}
```

---

## Course Endpoints

### Operation 1: Create Course

**Menu Path**: Main Menu → 2 → 1

**Current**: Console Input  
**Future REST**:
```http
POST /api/v1/courses
Content-Type: application/json

{
  "courseName": "Advanced Database Design",
  "courseCode": "CS405",
  "credits": 3.0,
  "department": "Computer Science",
  "description": "Advanced concepts in relational database design"
}
```

**Response (201 Created)**:
```json
{
  "id": 11,
  "courseName": "Advanced Database Design",
  "courseCode": "CS405",
  "credits": 3.0,
  "department": "Computer Science",
  "description": "Advanced concepts in relational database design",
  "createdAt": "2026-04-09T10:30:00Z"
}
```

---

### Operation 2: Get All Courses

**Menu Path**: Main Menu → 2 → 2

**Current**: Console Display  
**Future REST**:
```http
GET /api/v1/courses
```

**Response (200 OK)**:
```json
{
  "data": [
    {
      "id": 1,
      "courseName": "Data Structures",
      "courseCode": "CS201",
      "credits": 3.0,
      "department": "Computer Science",
      "description": "Fundamental data structures"
    },
    {
      "id": 2,
      "courseName": "Web Development",
      "courseCode": "CS202",
      "credits": 2.0,
      "department": "Computer Science",
      "description": "HTML, CSS, JavaScript, and backend basics"
    }
  ],
  "total": 10,
  "timestamp": "2026-04-09T10:30:00Z"
}
```

---

## Enrollment Endpoints

### Operation 1: Enroll Student in Course

**Menu Path**: Main Menu → 3 → 1

**Current**: Console Input  
**Future REST**:
```http
POST /api/v1/enrollments
Content-Type: application/json

{
  "studentId": 1,
  "courseId": 3,
  "status": "Active"
}
```

**Response (201 Created)**:
```json
{
  "enrollmentId": 5,
  "studentId": 1,
  "courseId": 3,
  "enrollmentDate": "2026-04-09",
  "status": "Active",
  "createdAt": "2026-04-09T10:30:00Z"
}
```

**Error Response (409)**:
```json
{
  "error": "Student already enrolled in this course",
  "status": 409
}
```

---

### Operation 2: Get Student Enrollments

**Menu Path**: Main Menu → 3 → 2

**Current**: Console Display  
**Future REST**:
```http
GET /api/v1/students/{studentId}/enrollments
```

**Response (200 OK)**:
```json
{
  "studentId": 1,
  "studentName": "Ahmed Hassan",
  "enrollments": [
    {
      "enrollmentId": 1,
      "courseId": 1,
      "courseName": "Data Structures",
      "courseCode": "CS201",
      "credits": 3.0,
      "department": "Computer Science",
      "enrollmentDate": "2026-04-09",
      "status": "Active"
    },
    {
      "enrollmentId": 2,
      "courseId": 2,
      "courseName": "Web Development",
      "courseCode": "CS202",
      "credits": 2.0,
      "department": "Computer Science",
      "enrollmentDate": "2026-04-09",
      "status": "Active"
    }
  ],
  "totalCredits": 5.0,
  "enrollmentCount": 2
}
```

---

## Fee Endpoints

### Operation 1: Calculate Student Fees

**Menu Path**: Main Menu → 4 → 1

**Current**: Console Input  
**Future REST**:
```http
POST /api/v1/fees/calculate
Content-Type: application/json

{
  "studentId": 1
}
```

**Response (201 Created)**:
```json
{
  "feeId": 1,
  "studentId": 1,
  "studentName": "Ahmed Hassan",
  "previousGpa": 3.75,
  "totalCredits": 5.0,
  "baseFee": 14000.0,
  "waiverPercentage": 50.0,
  "waiverAmount": 7000.0,
  "finalFee": 7000.0,
  "calculationDate": "2026-04-09",
  "breakdown": {
    "course1": {
      "name": "Data Structures",
      "credits": 3.0,
      "baseCost": 8400.0
    },
    "course2": {
      "name": "Web Development",
      "credits": 2.0,
      "baseCost": 5600.0
    }
  },
  "message": "50% waiver applied (GPA 3.75)",
  "createdAt": "2026-04-09T10:30:00Z"
}
```

---

### Operation 2: Get Student Fees

**Menu Path**: Main Menu → 4 → 2

**Future REST**:
```http
GET /api/v1/students/{studentId}/fees
```

**Response (200 OK)**:
```json
{
  "studentId": 1,
  "studentName": "Ahmed Hassan",
  "fees": [
    {
      "feeId": 1,
      "totalCredits": 5.0,
      "baseFee": 14000.0,
      "waiverPercentage": 50.0,
      "waiverAmount": 7000.0,
      "finalFee": 7000.0,
      "calculationDate": "2026-04-09",
      "semester": "Spring 2026"
    }
  ],
  "totalFeesDue": 7000.0,
  "totalFeesWaived": 7000.0
}
```

---

### Operation 3: Get All Fees Report

**Future REST**:
```http
GET /api/v1/fees/report
```

**Response (200 OK)**:
```json
{
  "reportDate": "2026-04-09",
  "totalStudents": 20,
  "totalFeesCalculated": 250000.0,
  "totalWaivers": 85000.0,
  "totalFeesCollected": 165000.0,
  "averageWaiverPercentage": 34.0,
  "breakdown": [
    {
      "studentId": 1,
      "studentName": "Ahmed Hassan",
      "finalFee": 7000.0,
      "status": "Pending"
    }
  ]
}
```

---

## Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK | Data retrieved successfully |
| 201 | Created | New resource created |
| 204 | No Content | Successful deletion |
| 400 | Bad Request | Invalid input (email exists, invalid GPA) |
| 404 | Not Found | Student/Course not found |
| 409 | Conflict | Duplicate enrollment |
| 500 | Server Error | Database connection error |

---

## Database Operations Mapping

### Current Implementation (JDBC)

```java
// Student Operations
addStudent()              → INSERT INTO students
viewAllStudents()         → SELECT * FROM students
editStudent()             → UPDATE students SET ... WHERE student_id
deleteStudent()           → DELETE FROM students WHERE student_id

// Course Operations
addCourse()              → INSERT INTO courses
viewAllCourses()         → SELECT * FROM courses

// Enrollment Operations
enrollStudent()          → INSERT INTO enrollments
viewStudentEnrollments() → SELECT e.*, c.* FROM enrollments e JOIN courses c

// Fee Operations
calculateStudentFees()   → SELECT SUM(credits), then INSERT INTO fees
viewFeeReports()         → SELECT * FROM fees with analysis
```

---

## Query Examples

### Calculate Total Credits for Student

```sql
SELECT SUM(c.credits) as total_credits
FROM enrollments e
JOIN courses c ON e.course_id = c.course_id
WHERE e.student_id = ? 
AND e.status = 'Active';
```

### Get Student with All Details

```sql
SELECT 
  s.student_id,
  s.name,
  s.email,
  s.previous_gpa,
  COUNT(e.enrollment_id) as enrolled_courses,
  SUM(c.credits) as total_credits
FROM students s
LEFT JOIN enrollments e ON s.student_id = e.student_id
LEFT JOIN courses c ON e.course_id = c.course_id
WHERE s.student_id = ?
GROUP BY s.student_id;
```

### Get Fee Summary Report

```sql
SELECT 
  SUM(base_fee) as total_fees,
  SUM(waiver_amount) as total_waivers,
  SUM(final_fee) as total_collected,
  AVG(waiver_percentage) as avg_waiver
FROM fees
WHERE MONTH(calculation_date) = MONTH(CURDATE())
AND YEAR(calculation_date) = YEAR(CURDATE());
```

---

## Future Swagger/OpenAPI Implementation

```yaml
openapi: 3.0.0
info:
  title: University Management System API
  version: 1.0.0
  description: RESTful API for university fees management
servers:
  - url: http://localhost:8080/api/v1
paths:
  /students:
    post:
      summary: Create a new student
      tags: [Students]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/StudentInput'
      responses:
        '201':
          description: Student created successfully
    get:
      summary: Get all students
      tags: [Students]
      responses:
        '200':
          description: List of students
```

---

**Document Version**: 1.0  
**API Version**: 1.0 (Future)  
**Last Updated**: April 2026  
**Maintained By**: Himel

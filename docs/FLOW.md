# System Flow & Working Process

## Application Startup Flow

```
┌─────────────────────────────┐
│   Program Starts            │
│   java UniversityManagement │
│   System                    │
└────────────┬────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│ Initialize Database Connection      │
│ - Load MySQL Connector Driver       │
│ - Connect to localhost:3306         │
│ - Connect to database: university_db│
│ - User: root, Password: (blank)     │
└────────────┬────────────────────────┘
             │
        ✓ Success?
        ├─ YES: Continue
        └─ NO: Exit with error message
             │
             ▼
┌─────────────────────────────────────┐
│ Initialize Database Tables          │
│ - Check if tables exist             │
│ - Create if missing:                │
│   • students                        │
│   • courses                         │
│   • enrollments                     │
│   • fees                            │
│ - Pre-load sample courses           │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│ Display Welcome Message             │
│ System Ready for Operations         │
│                                      │
│ Press 1-6 for Menu Selection        │
└─────────────────────────────────────┘
```

---

## Main Menu Flow

```
┌──────────────────────────────────────────┐
│       MAIN MENU                          │
│                                           │
│ 1 → Student Management                   │
│ 2 → Course Management                    │
│ 3 → Enrollment Management                │
│ 4 → Fee Calculation & Waiver             │
│ 5 → Reports & View Data                  │
│ 6 → Exit                                 │
└──────────┬───────────────────────────────┘
           │
           ├─ Input: 1 ──────────────────────────────► Student Menu
           │
           ├─ Input: 2 ──────────────────────────────► Course Menu
           │
           ├─ Input: 3 ──────────────────────────────► Enrollment Menu
           │
           ├─ Input: 4 ──────────────────────────────► Fee Menu
           │
           ├─ Input: 5 ──────────────────────────────► Reports Menu
           │
           └─ Input: 6 ──────────────────────────────► Exit Program
```

---

## Student Management Workflow

### Complete Student Lifecycle

```
┌───────────────────────────────────┐
│  STUDENT MANAGEMENT MENU          │
│                                    │
│ 1 → Add New Student               │
│ 2 → View All Students             │
│ 3 → Edit Student (GPA Update)     │
│ 4 → Delete Student                │
│ 5 → Back to Main Menu             │
└───────────┬───────────────────────┘
            │
            ├─ Choice 1: ADD STUDENT
            │  ├─ Get: Name
            │  ├─ Get: Email
            │  ├─ Get: Phone
            │  ├─ Get: Previous GPA
            │  ├─ Validation: Email uniqueness check
            │  ├─ INSERT into students table
            │  ├─ Auto-generate: student_id
            │  ├─ Auto-set: enrollment_date = TODAY
            │  ├─ Auto-set: status = 'Active'
            │  └─ Success message with ID
            │
            │
            ├─ Choice 2: VIEW ALL STUDENTS
            │  ├─ SELECT * FROM students
            │  ├─ Display: ID, Name, Email, Phone, GPA, Status
            │  └─ List all 20 sample students
            │
            │
            ├─ Choice 3: EDIT STUDENT (GPA)
            │  ├─ Get: Student ID
            │  ├─ Get: New GPA value
            │  ├─ Validation: GPA in range [0.0 - 4.0]
            │  ├─ UPDATE previous_gpa WHERE student_id
            │  └─ Success confirmation
            │
            │
            ├─ Choice 4: DELETE STUDENT
            │  ├─ Get: Student ID
            │  ├─ Confirm deletion
            │  ├─ DELETE FROM students
            │  │  → Cascade: enrollments deleted
            │  │  → Cascade: fees deleted
            │  └─ Confirmation message
            │
            │
            └─ Choice 5: Back to Main Menu
```

### Add Student Details Flow

```
╔═════════════════════════════════════╗
║     ADD NEW STUDENT                 ║
╚══════════════┬══════════════════════╝
               │
               ▼
         Enter Name
         (e.g., "Ahmed Hassan")
               │
               ▼
         Enter Email
         (e.g., "ahmed@university.edu")
               │
         Validation: Email UNIQUE?
         ├─ NO → Show error, retry
         └─ YES → Continue
               │
               ▼
         Enter Phone
         (e.g., "01712345678")
               │
               ▼
         Enter Previous GPA
         (0.0 - 4.0)
               │
         Validation: Valid range?
         ├─ NO → Show error, retry
         └─ YES → Continue
               │
               ▼
    ┌──────────────────────────┐
    │ SQL EXECUTION:           │
    │ INSERT INTO students (   │
    │   name, email, phone,    │
    │   previous_gpa,          │
    │   enrollment_date,       │
    │   status)                │
    │ VALUES (?, ?, ?, ?, ?,?) │
    └──────────────────────────┘
               │
               ▼
    📊 Database Update
    ✓ New Row Created
       ID: (auto-generated)
       Status: "Active"
               │
               ▼
    ✅ Success Message
    "Student added with ID: 1"
```

---

## Enrollment & Fee Calculation Flow

### Complete Enrollment to Fee Calculation

```
┌─────────────────────────────────────────┐
│ STUDENT ENROLLMENT PROCESS              │
└──────────┬──────────────────────────────┘
           │
           ▼  Step 1: Select Student
    ┌──────────────────────────┐
    │ Which student to enroll? │
    │ Enter Student ID: 1      │
    │ [Fetch: Ahmed Hassan]    │
    └──────────┬───────────────┘
               │
               ▼  Step 2: Select Course
        ┌──────────────────────────┐
        │ Which course to enroll?  │
        │ (Display available       │
        │  courses)                │
        │ Enter Course ID: 3       │
        │ [Fetch: Database Sys]    │
        └──────────┬───────────────┘
                   │
                   ▼  Step 3: Validate Enrollment
            ┌────────────────────────────┐
            │ Check: Not already         │
            │ enrolled in this course    │
            │ Status: VALID → Continue   │
            └──────────┬─────────────────┘
                       │
                       ▼
          ┌──────────────────────────────┐
          │ INSERT INTO enrollments      │
          │ (student_id, course_id,      │
          │  enrollment_date, status)    │
          │ VALUES (1, 3, 2026-04-09,    │
          │         'Active')            │
          └──────────┬───────────────────┘
                     │
                     ▼
            ✅ Enrollment Created
```

### Fee Calculation Complete Flow

```
┌──────────────────────────────────────────────────────┐
│ FEE CALCULATION PROCESS                              │
│ (Triggered after student completes enrollments)      │
└───────────────┬──────────────────────────────────────┘
                │
                ▼  Input: Student ID to calculate fees
         ┌────────────────────────┐
         │ Calculate fees for     │
         │ Student ID: 1          │
         │ (Ahmed Hassan)         │
         └────────┬───────────────┘
                  │
    ┌─────────────┴──────────────┐
    │                            │
    ▼  Step 1: Fetch Student     │
  SELECT previous_gpa            │
  FROM students WHERE id = 1     │
  RESULT: 3.75 GPA              │
    │                            │
    ├─────────────┬──────────────┘
    │             
    ▼  Step 2: Calculate Total Credits
  SELECT SUM(credits)
  FROM courses c
  JOIN enrollments e
  WHERE e.student_id = 1
  ENROLLMENTS: Data Structures (3.0 cr),
              Web Dev (2.0 cr)
  TOTAL: 5.0 credits
    │
    ▼  Step 3: Determine Waiver Tier
  GPA = 3.75
  Range Check:
  • 3.75 >= 3.50 AND 3.75 < 4.00?
  • YES → 50% waiver
  WAIVER_PCT = 50.0%
    │
    ▼  Step 4: Calculate Base Fee
  BASE_FEE = 5.0 × 2800
  BASE_FEE = 14,000 TK
    │
    ▼  Step 5: Calculate Waiver Amount
  WAIVER_AMOUNT = 14,000 × (50/100)
  WAIVER_AMOUNT = 7,000 TK
    │
    ▼  Step 6: Calculate Final Fee
  FINAL_FEE = 14,000 - 7,000
  FINAL_FEE = 7,000 TK
    │
    ▼  Step 7: Store in Database
  INSERT INTO fees (
    student_id,
    total_credits,
    base_fee,
    waiver_percentage,
    waiver_amount,
    final_fee,
    calculation_date)
  VALUES (1, 5.0, 14000, 50.0,
          7000, 7000, 2026-04-09)
    │
    ▼
  ╔═══════════════════════════════════╗
  ║  FEE CALCULATION COMPLETE         ║
  ║───────────────────────────────────║
  ║  Student ID: 1                    ║
  ║  Name: Ahmed Hassan               ║
  ║  GPA: 3.75                        ║
  ║  Total Credits: 5.0               ║
  ║  Base Fee: 14,000 TK              ║
  ║  Waiver (%): 50.0%                ║
  ║  Waiver Amount: 7,000 TK          ║
  ║  FINAL FEE: 7,000 TK              ║
  ║                                   ║
  ║  Status: ✅ SAVED TO DATABASE     ║
  ╚═══════════════════════════════════╝
```

---

## GPA-Based Waiver Tier Decision Tree

```
                    ┌─────────────────┐
                    │ Student GPA     │
                    └────────┬────────┘
                             │
             ┌───────────────┼───────────────┐
             │               │               │
             ▼               ▼               ▼
      GPA >= 3.50    3.00 <= GPA   2.50 <= GPA
      AND < 4.00     < 3.50        < 3.00
             │               │               │
             ▼               ▼               ▼
        WAIVER       WAIVER              WAIVER
         50%           40%                30%
             │               │               │
             ├──────────┬────┴───┬──────────┤
                        │        │
                        ▼        ▼
                    Example 1: GPA 3.75
                    Tier: 50% waiver
                    Base: 14,000 TK
                    Waiver: 7,000 TK
                    Pay: 7,000 TK

                    Example 2: GPA 3.25
                    Tier: 40% waiver
                    Base: 14,000 TK
                    Waiver: 5,600 TK
                    Pay: 8,400 TK

                    Example 3: GPA 2.65
                    Tier: 30% waiver
                    Base: 14,000 TK
                    Waiver: 4,200 TK
                    Pay: 9,800 TK
```

---

## Database Operation Sequence

### Complete Transaction Sequence

```
User Opens Application
       │
       ▼
connect_to_database()
├─ Load MySQL Driver
├─ Create connection to localhost:3306
└─ Status: CONNECTED ✓
       │
       ▼
initialize_database()
├─ IF tables don't exist:
│  ├─ CREATE TABLE students
│  ├─ CREATE TABLE courses
│  ├─ CREATE TABLE enrollments
│  └─ CREATE TABLE fees
├─ IF courses table empty:
│  └─ INSERT 10 sample courses
└─ Status: INITIALIZED ✓
       │
       ▼
main_application_loop()
├─ Display Main Menu
├─ Wait for user input (1-6)
├─ Route to selected operation
└─ Repeat until exit
       │
       ▼
User selects operation:

ADD STUDENT:
       │
       ├─ Get input (name, email, phone, gpa)
       ├─ Validate email uniqueness
       │  └─ Query: SELECT COUNT(*) FROM students WHERE email = ?
       ├─ Prepare statement
       │  └─ INSERT INTO students VALUES (...)
       ├─ Execute
       ├─ Get generated ID
       └─ Display success

VIEW STUDENTS:
       │
       ├─ Query: SELECT * FROM students
       ├─ Fetch all rows
       ├─ Format and display
       └─ Return to menu

CALCULATE FEES:
       │
       ├─ Get Student ID
       ├─ Query: SELECT previous_gpa FROM students WHERE id = ?
       ├─ Query: SELECT SUM(credits) FROM ... WHERE student_id = ?
       ├─ Calculate waiver using getWaiverPercentage(gpa)
       ├─ Calculate: base = credits × 2800
       ├─ Calculate: waiver_amt = base × (waiver% / 100)
       ├─ Calculate: final = base - waiver_amt
       ├─ Prepare statement
       │  └─ INSERT INTO fees VALUES (...)
       ├─ Execute
       └─ Display results

User presses Exit (6):
       │
       └─ close_connection()
          ├─ Close database connection
          ├─ Release resources
          └─ Program terminates
```

---

## Error Handling Flow

```
User Input / Database Operation
         │
         ▼
   Try Block
         │
    ├─ Parse input
    ├─ Validate input
    ├─ Execute SQL
    ├─ Process results
    │
    └─ Success?
       │
       ├─ YES → Display results & return
       │
       └─ NO → Exception caught
              │
              ▼
           Catch Block
           │
           ├─ SQLException (Database error)
           │  ├─ Log error message
           │  ├─ Display: "Database error occurred"
           │  └─ Prompt: "Try again?"
           │
           ├─ InputMismatchException (Invalid input)
           │  ├─ Log error message
           │  ├─ Clear scanner buffer
           │  ├─ Display: "Invalid input"
           │  └─ Prompt: "Try again?"
           │
           └─ General Exception
              ├─ Log error
              ├─ Display: "Unexpected error"
              └─ Prompt: "Try again or return to menu?"
           │
           ▼
      Return to Menu
```

---

## Report Generation Flow

```
┌────────────────────────────────────┐
│ FEE REPORT GENERATION              │
└───────────┬────────────────────────┘
            │
            ▼
    SELECT f.*, s.name
    FROM fees f
    JOIN students s
    WHERE f.calculation_date = TODAY
    OR f.calculation_date >= ? (range)
            │
            ▼
    ┌─────────────────────────────┐
    │ Results Fetched             │
    │ Loop through each fee record│
    └────────┬────────────────────┘
             │
             ├─ Student ID, Name
             ├─ Total Credits
             ├─ Base Fee
             ├─ Waiver %
             ├─ Waiver Amount
             ├─ Final Fee
             │
             ▼
    ╔═════════════════════════════════╗
    ║ FEE REPORT - April 9, 2026      ║
    ╠═════════════════════════════════╣
    ║ ID │ Name │ Credits │ Final Fee ║
    ║ ── │ ──── │ ─────── │ ───────── ║
    ║  1 │Ahmed │   5.0   │  7,000 TK ║
    ║  2 │Fatima│   6.0   │ 11,760 TK ║
    ║  3 │Ali   │   7.0   │ 12,320 TK ║
    ║                                 ║
    ║ Total Fees: 31,080 TK           ║
    ║ Total Waivers: 23,820 TK        ║
    ║ Average Waiver: 39.4%           ║
    ╚═════════════════════════════════╝
```

---

## Multi-Step Student Enrollment Journey

```
DAY 1: REGISTRATION
    │
    ├─→ Menu: 1 (Student Management)
    ├─→ Choice: 1 (Add Student)
    ├─→ Input: Name, Email, Phone, GPA
    └─→ Result: Student ID 15 created, Status: Active
       Database: students table +1 row

DAY 2: COURSE SELECTION
    │
    ├─→ Menu: 3 (Enrollment Management)
    ├─→ Choice: 1 (Enroll Student)
    ├─→ Student ID: 15
    ├─→ Select Course 1: Data Structures (3 CR)
    └─→ Database: enrollments table +1 row
           │
           ├─→ Same student, Course 2: Web Dev (2 CR)
           └─→ Database: enrollments table +1 row
                  │
                  └─→ Same student, Course 3: Database (3 CR)
                      Database: enrollments table +1 row

DAY 3: VIEW ENROLLMENTS
    │
    ├─→ Menu: 3 (Enrollment Management)
    ├─→ Choice: 2 (View Student Enrollments)
    ├─→ Student ID: 15
    └─→ Display:
        Course 1: Data Structures - 3 CR
        Course 2: Web Development - 2 CR
        Course 3: Database Systems - 3 CR
        TOTAL: 8 CR

DAY 4: FEE CALCULATION
    │
    ├─→ Menu: 4 (Fee Calculation)
    ├─→ Choice: 1 (Calculate Fees)
    ├─→ Student ID: 15 (GPA: 3.45)
    ├─→ Calculation:
    │   • Total Credits: 8.0
    │   • Base Fee: 8.0 × 2800 = 22,400 TK
    │   • GPA 3.45 → 40% waiver
    │   • Waiver Amount: 22,400 × 0.40 = 8,960 TK
    │   • Final Fee: 22,400 - 8,960 = 13,440 TK
    └─→ Database: fees table +1 row

DAY 5: REVIEW & REPORTING
    │
    ├─→ Menu: 4 (Fee Management)
    ├─→ Choice: 2 (View Fee Reports)
    ├─→ Display Student 15's fee details
    └─→ Report: Shows calculation breakdown
        Status: Ready for payment collection

THROUGHOUT: AUDIT TRAIL
    │
    └─→ Database timestamps:
        • students.created_at: Registration date
        • students.updated_at: Last GPA update
        • enrollments.created_at: When enrolled
        • fees.created_at: Calculation date
```

---

## System State Transitions

```
      ┌─────────────────────┐
      │  SYSTEM START       │
      └──────────┬──────────┘
                 │
                 ▼
      ┌─────────────────────┐
      │  INITIALIZING       │
      │  - Load drivers     │
      │  - Connect DB       │
      │  - Create tables    │
      └──────────┬──────────┘
                 │
                 ▼
      ┌─────────────────────┐
      │  READY              │
      │  - Display menu     │
      │  - Wait for input   │
      └──────────┬──────────┘
         ┌───────┼───────┐
         │       │       │
         ▼       ▼       ▼
      STUDENT  COURSE  ....
      MGMT     MGMT
         │       │
      ┌──┴───────┴───┐
      │               │
      ▼               ▼
   PROCESSING    DISPLAYING
      │               │
      └───────┬───────┘
              │
              ▼
        ┌─────────────┐
        │ ERROR?      │
        ├─────┬───────┤
        │YES  │ NO    │
        ├─────┼───────┤
        │ Log │Return │
        │Show │to Menu│
        │msg  │       │
        └─────┴───────┘
```

---

**Document Version**: 1.0  
**Last Updated**: April 2026  
**Maintained By**: Himel

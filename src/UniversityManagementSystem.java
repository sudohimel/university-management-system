import java.sql.*;
import java.util.*;

public class UniversityManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private Connection connection;
    private Scanner scanner;
    private static final double FEE_PER_CREDIT = 2800.0; // TK per credit

    public UniversityManagementSystem() {
        this.scanner = new Scanner(System.in);
    }

    public boolean connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Connected to MySQL Database");
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL Driver not found: " + e.getMessage());
            System.err.println("Make sure MySQL JDBC driver is added to classpath");
            return false;
        } catch (SQLException e) {
            System.err.println("✗ Could not connect to database: " + e.getMessage());
            System.err.println("Make sure MySQL is running and database 'university_db' exists");
            return false;
        }
    }

    public void initializeDatabase() {
        try {
            Statement stmt = connection.createStatement();
            
            // Create Students table
            String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                    "student_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE," +
                    "phone VARCHAR(15)," +
                    "previous_gpa DOUBLE DEFAULT 0.0," +
                    "enrollment_date DATE DEFAULT CURDATE()," +
                    "status VARCHAR(20) DEFAULT 'Active'" +
                    ")";
            
            // Create Courses table
            String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses (" +
                    "course_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "course_name VARCHAR(100) NOT NULL," +
                    "course_code VARCHAR(20) UNIQUE NOT NULL," +
                    "credits DOUBLE NOT NULL," +
                    "department VARCHAR(50)," +
                    "description TEXT" +
                    ")";
            
            // Create Enrollments table
            String createEnrollmentsTable = "CREATE TABLE IF NOT EXISTS enrollments (" +
                    "enrollment_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "student_id INT NOT NULL," +
                    "course_id INT NOT NULL," +
                    "enrollment_date DATE DEFAULT CURDATE()," +
                    "status VARCHAR(20) DEFAULT 'Active'," +
                    "FOREIGN KEY (student_id) REFERENCES students(student_id)," +
                    "FOREIGN KEY (course_id) REFERENCES courses(course_id)" +
                    ")";
            
            // Create Fees table
            String createFeesTable = "CREATE TABLE IF NOT EXISTS fees (" +
                    "fee_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "student_id INT NOT NULL," +
                    "total_credits DOUBLE," +
                    "base_fee DOUBLE," +
                    "waiver_percentage DOUBLE," +
                    "waiver_amount DOUBLE," +
                    "final_fee DOUBLE," +
                    "calculation_date DATE DEFAULT CURDATE()," +
                    "FOREIGN KEY (student_id) REFERENCES students(student_id)" +
                    ")";
            
            stmt.execute(createStudentsTable);
            stmt.execute(createCoursesTable);
            stmt.execute(createEnrollmentsTable);
            stmt.execute(createFeesTable);
            
            System.out.println("✓ Database tables initialized");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    // ============ STUDENT MANAGEMENT ============

    public void addStudent() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ADD NEW STUDENT");
        System.out.println("=".repeat(80));
        
        System.out.print("Student Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Previous Semester GPA (0.0-4.0): ");
        double gpa = Double.parseDouble(scanner.nextLine());
        
        String sql = "INSERT INTO students (name, email, phone, previous_gpa) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setDouble(4, gpa);
            pstmt.executeUpdate();
            System.out.println("✓ Student added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
        }
    }

    public void viewAllStudents() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("ALL STUDENTS");
        System.out.println("=".repeat(100));
        
        String sql = "SELECT * FROM students ORDER BY student_id";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.printf("%-6s %-25s %-30s %-15s %-8s %s\n", 
                    "ID", "Name", "Email", "Phone", "GPA", "Status");
            System.out.println("-".repeat(100));
            
            while (rs.next()) {
                System.out.printf("%-6d %-25s %-30s %-15s %-8.2f %s\n",
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDouble("previous_gpa"),
                        rs.getString("status"));
            }
            System.out.println("=".repeat(100));
        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
        }
    }

    public void editStudent() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EDIT STUDENT");
        System.out.println("=".repeat(80));
        
        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        
        System.out.print("New GPA: ");
        double newGpa = Double.parseDouble(scanner.nextLine());
        
        String sql = "UPDATE students SET previous_gpa = ? WHERE student_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, newGpa);
            pstmt.setInt(2, studentId);
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✓ Student updated successfully!");
            } else {
                System.out.println("✗ Student not found!");
            }
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
    }

    public void deleteStudent() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DELETE STUDENT");
        System.out.println("=".repeat(80));
        
        System.out.print("Enter Student ID to delete: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        
        String sql = "DELETE FROM students WHERE student_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✓ Student deleted successfully!");
            } else {
                System.out.println("✗ Student not found!");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }

    // ============ COURSE MANAGEMENT ============

    public void addCourse() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ADD NEW COURSE");
        System.out.println("=".repeat(80));
        
        System.out.print("Course Name: ");
        String courseName = scanner.nextLine().trim();
        
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine().trim();
        
        System.out.println("Credits (1.0, 1.5, 2.0, or 3.0): ");
        double credits = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        
        String sql = "INSERT INTO courses (course_name, course_code, credits, department) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, courseName);
            pstmt.setString(2, courseCode);
            pstmt.setDouble(3, credits);
            pstmt.setString(4, department);
            pstmt.executeUpdate();
            System.out.println("✓ Course added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
    }

    public void viewAllCourses() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("ALL COURSES");
        System.out.println("=".repeat(100));
        
        String sql = "SELECT * FROM courses ORDER BY course_id";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.printf("%-6s %-25s %-15s %-8s %s\n", 
                    "ID", "Course Name", "Code", "Credits", "Department");
            System.out.println("-".repeat(100));
            
            while (rs.next()) {
                System.out.printf("%-6d %-25s %-15s %-8.1f %s\n",
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("course_code"),
                        rs.getDouble("credits"),
                        rs.getString("department"));
            }
            System.out.println("=".repeat(100));
        } catch (SQLException e) {
            System.err.println("Error retrieving courses: " + e.getMessage());
        }
    }

    // ============ ENROLLMENT MANAGEMENT ============

    public void enrollStudent() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ENROLL STUDENT IN COURSE");
        System.out.println("=".repeat(80));
        
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(scanner.nextLine());
        
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();
            System.out.println("✓ Student enrolled successfully!");
        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
        }
    }

    public void viewStudentEnrollments() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("STUDENT ENROLLMENTS");
        System.out.println("=".repeat(100));
        
        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        
        String sql = "SELECT s.name, c.course_name, c.credits, e.enrollment_date FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.student_id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.printf("%-25s %-25s %-8s %s\n", 
                    "Student Name", "Course Name", "Credits", "Date");
            System.out.println("-".repeat(100));
            
            while (rs.next()) {
                System.out.printf("%-25s %-25s %-8.1f %s\n",
                        rs.getString("name"),
                        rs.getString("course_name"),
                        rs.getDouble("credits"),
                        rs.getDate("enrollment_date"));
            }
            System.out.println("=".repeat(100));
        } catch (SQLException e) {
            System.err.println("Error retrieving enrollments: " + e.getMessage());
        }
    }

    // ============ FEE CALCULATION ============

    public double getWaiverPercentage(double gpa) {
        if (gpa >= 3.50 && gpa < 4.00) {
            return 50.0;
        } else if (gpa >= 3.00 && gpa < 3.50) {
            return 40.0;
        } else if (gpa >= 2.50 && gpa < 3.00) {
            return 30.0;
        }
        return 0.0;
    }

    public void calculateStudentFees() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("CALCULATE & STORE STUDENT FEES");
        System.out.println("=".repeat(80));
        
        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        
        try {
            // Get student GPA
            String studentSql = "SELECT previous_gpa FROM students WHERE student_id = ?";
            PreparedStatement studentStmt = connection.prepareStatement(studentSql);
            studentStmt.setInt(1, studentId);
            ResultSet studentRs = studentStmt.executeQuery();
            
            if (!studentRs.next()) {
                System.out.println("✗ Student not found!");
                return;
            }
            
            double gpa = studentRs.getDouble("previous_gpa");
            
            // Calculate total credits
            String creditsSql = "SELECT SUM(credits) as total_credits FROM enrollments e " +
                               "JOIN courses c ON e.course_id = c.course_id " +
                               "WHERE e.student_id = ? AND e.status = 'Active'";
            PreparedStatement creditsStmt = connection.prepareStatement(creditsSql);
            creditsStmt.setInt(1, studentId);
            ResultSet creditsRs = creditsStmt.executeQuery();
            
            double totalCredits = 0;
            if (creditsRs.next()) {
                totalCredits = creditsRs.getDouble("total_credits");
            }
            
            if (totalCredits == 0) {
                System.out.println("✗ Student has no active enrollments!");
                return;
            }
            
            // Calculate fees
            double baseFee = totalCredits * FEE_PER_CREDIT;
            double waiverPercentage = getWaiverPercentage(gpa);
            double waiverAmount = baseFee * (waiverPercentage / 100.0);
            double finalFee = baseFee - waiverAmount;
            
            // Insert into fees table
            String insertFeeSql = "INSERT INTO fees (student_id, total_credits, base_fee, waiver_percentage, waiver_amount, final_fee) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertFeeSql);
            insertStmt.setInt(1, studentId);
            insertStmt.setDouble(2, totalCredits);
            insertStmt.setDouble(3, baseFee);
            insertStmt.setDouble(4, waiverPercentage);
            insertStmt.setDouble(5, waiverAmount);
            insertStmt.setDouble(6, finalFee);
            insertStmt.executeUpdate();
            
            // Display results
            System.out.println("\n" + "=".repeat(60));
            System.out.println("FEE CALCULATION RESULTS");
            System.out.println("=".repeat(60));
            System.out.printf("Student ID: %d\n", studentId);
            System.out.printf("Previous GPA: %.2f\n", gpa);
            System.out.printf("Total Credits: %.2f\n", totalCredits);
            System.out.printf("Base Fee (2800 TK/credit): %.2f TK\n", baseFee);
            System.out.printf("Waiver Percentage: %.1f%%\n", waiverPercentage);
            System.out.printf("Waiver Amount: %.2f TK\n", waiverAmount);
            System.out.printf("FINAL FEE: %.2f TK\n", finalFee);
            System.out.println("=".repeat(60));
            System.out.println("✓ Fees calculated and stored successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error calculating fees: " + e.getMessage());
        }
    }

    public void viewStudentFees() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("STUDENT FEE REPORT");
        System.out.println("=".repeat(100));
        
        System.out.print("Enter Student ID (or 0 to view all): ");
        int studentId = Integer.parseInt(scanner.nextLine());
        
        String sql;
        PreparedStatement pstmt;
        
        try {
            if (studentId == 0) {
                sql = "SELECT f.fee_id, s.student_id, s.name, s.previous_gpa, f.total_credits, " +
                      "f.base_fee, f.waiver_percentage, f.waiver_amount, f.final_fee FROM fees f " +
                      "JOIN students s ON f.student_id = s.student_id ORDER BY f.fee_id DESC LIMIT 10";
                pstmt = connection.prepareStatement(sql);
            } else {
                sql = "SELECT f.fee_id, s.student_id, s.name, s.previous_gpa, f.total_credits, " +
                      "f.base_fee, f.waiver_percentage, f.waiver_amount, f.final_fee FROM fees f " +
                      "JOIN students s ON f.student_id = s.student_id WHERE s.student_id = ? ORDER BY f.fee_id DESC";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, studentId);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            System.out.printf("%-10s %-8s %-20s %-8s %-10s %-12s %-10s %-12s %-12s\n",
                    "Fee ID", "Stu ID", "Student Name", "GPA", "Credits", "Base Fee", "Waiver%", "Waiver Amt", "Final Fee");
            System.out.println("-".repeat(100));
            
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-10d %-8d %-20s %-8.2f %-10.1f %-12.2f %-10.1f %-12.2f %-12.2f\n",
                        rs.getInt("fee_id"),
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getDouble("previous_gpa"),
                        rs.getDouble("total_credits"),
                        rs.getDouble("base_fee"),
                        rs.getDouble("waiver_percentage"),
                        rs.getDouble("waiver_amount"),
                        rs.getDouble("final_fee"));
            }
            
            if (!found) {
                System.out.println("No fee records found!");
            }
            System.out.println("=".repeat(100));
            
        } catch (SQLException e) {
            System.err.println("Error retrieving fees: " + e.getMessage());
        }
    }

    // ============ MENU SYSTEM ============

    public void displayMainMenu() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UNIVERSITY MANAGEMENT SYSTEM - FEES MANAGEMENT");
        System.out.println("=".repeat(80));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Fee Calculation & Waiver");
        System.out.println("5. Reports & View Data");
        System.out.println("6. Exit");
        System.out.print("\nEnter your choice (1-6): ");
    }

    public void studentManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("STUDENT MANAGEMENT");
            System.out.println("=".repeat(80));
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Edit Student (Update GPA)");
            System.out.println("4. Delete Student");
            System.out.println("5. Back to Main Menu");
            System.out.print("\nEnter your choice (1-5): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> editStudent();
                case 4 -> deleteStudent();
                case 5 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public void courseManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("COURSE MANAGEMENT");
            System.out.println("=".repeat(80));
            System.out.println("1. Add New Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Back to Main Menu");
            System.out.print("\nEnter your choice (1-3): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addCourse();
                case 2 -> viewAllCourses();
                case 3 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public void enrollmentManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("ENROLLMENT MANAGEMENT");
            System.out.println("=".repeat(80));
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. View Student Enrollments");
            System.out.println("3. Back to Main Menu");
            System.out.print("\nEnter your choice (1-3): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> enrollStudent();
                case 2 -> viewStudentEnrollments();
                case 3 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public void feeManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("FEE CALCULATION & WAIVER");
            System.out.println("=".repeat(80));
            System.out.println("Waiver Structure:");
            System.out.println("  GPA >= 3.50: 50% waiver");
            System.out.println("  GPA >= 3.00: 40% waiver");
            System.out.println("  GPA >= 2.50: 30% waiver");
            System.out.println("  GPA < 2.50: No waiver");
            System.out.println("\n1. Calculate & Store Student Fees");
            System.out.println("2. View Fee Reports");
            System.out.println("3. Back to Main Menu");
            System.out.print("\nEnter your choice (1-3): ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> calculateStudentFees();
                case 2 -> viewStudentFees();
                case 3 -> running = false;
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1 -> studentManagementMenu();
                case 2 -> courseManagementMenu();
                case 3 -> enrollmentManagementMenu();
                case 4 -> feeManagementMenu();
                case 5 -> viewReports();
                case 6 -> {
                    System.out.println("\nThank you for using University Management System!");
                    running = false;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
        
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        scanner.close();
    }

    public void viewReports() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("REPORTS");
        System.out.println("=".repeat(80));
        System.out.println("1. Fee Reports");
        System.out.println("2. Back to Main Menu");
        System.out.print("\nEnter your choice: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            viewStudentFees();
        }
    }

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UNIVERSITY MANAGEMENT SYSTEM - STARTUP");
        System.out.println("=".repeat(80));
        System.out.println("Initializing...\n");
        
        UniversityManagementSystem system = new UniversityManagementSystem();
        
        if (system.connectToDatabase()) {
            system.initializeDatabase();
            system.run();
        } else {
            System.out.println("\n✗ Failed to start application - Database connection failed");
        }
    }
}

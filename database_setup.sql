-- University Management System - Database Setup Script
-- Run this script in MySQL to create the database and tables

-- Create Database
CREATE DATABASE IF NOT EXISTS university_db;
USE university_db;

-- Students Table
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    previous_gpa DOUBLE DEFAULT 0.0,
    enrollment_date DATE DEFAULT CURDATE(),
    status VARCHAR(20) DEFAULT 'Active',
    INDEX idx_student_id (student_id),
    INDEX idx_email (email)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Courses Table
CREATE TABLE IF NOT EXISTS courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    credits DOUBLE NOT NULL,
    department VARCHAR(50),
    description TEXT,
    INDEX idx_course_id (course_id),
    INDEX idx_course_code (course_code)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Enrollments Table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date DATE DEFAULT CURDATE(),
    status VARCHAR(20) DEFAULT 'Active',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    INDEX idx_enrollment_id (enrollment_id),
    INDEX idx_student_id (student_id),
    INDEX idx_course_id (course_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Fees Table
CREATE TABLE IF NOT EXISTS fees (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    total_credits DOUBLE,
    base_fee DOUBLE,
    waiver_percentage DOUBLE,
    waiver_amount DOUBLE,
    final_fee DOUBLE,
    calculation_date DATE DEFAULT CURDATE(),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    INDEX idx_fee_id (fee_id),
    INDEX idx_student_id (student_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Sample Data (Optional)
INSERT INTO courses (course_name, course_code, credits, department) VALUES
('Data Structures', 'CSC101', 3, 'Computer Science'),
('Database Management', 'CSC201', 3, 'Computer Science'),
('Web Development', 'CSC202', 2, 'Computer Science'),
('Software Engineering', 'CSC301', 3, 'Computer Science'),
('Calculus I', 'MATH101', 3, 'Mathematics'),
('Linear Algebra', 'MATH102', 3, 'Mathematics'),
('Physics I', 'PHY101', 3, 'Physics'),
('Chemistry I', 'CHM101', 2, 'Chemistry'),
('English Composition', 'ENG101', 2, 'English'),
('Bengali Literature', 'BNG101', 1, 'Bengali');

-- Display database structure
SELECT 'Database setup completed successfully!' as status;

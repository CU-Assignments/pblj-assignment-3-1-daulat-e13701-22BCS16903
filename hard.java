create the database structure:
-- Create students table
CREATE TABLE students (
 student_id INT PRIMARY KEY,
 name VARCHAR(100) NOT NULL,
 class VARCHAR(20),
 section CHAR(1)
);
-- Create attendance table
CREATE TABLE attendance (
 id INT PRIMARY KEY AUTO_INCREMENT,
 student_id INT,
 date DATE NOT NULL,
 status ENUM('Present', 'Absent', 'Late') NOT NULL,
 remarks VARCHAR(255),
 FOREIGN KEY (student_id) REFERENCES students(student_id)
)
-- Insert sample student data
INSERT INTO students VALUES (1001, 'Alex Johnson', '10', 'A');
INSERT INTO students VALUES (1002, 'Sophia Davis', '10', 'A');
INSERT INTO students VALUES (1003, 'Ethan Wilson', '10', 'B');
INSERT INTO students VALUES (1004, 'Olivia Martin', '10', 'B');
INSERT INTO students VALUES (1005, 'Noah Thompson', '10', 'A');
create a DBUtil class:
// com.studentportal.util.DBUtil.java
package com.studentportal.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBUtil {
 private static final String JDBC_URL = "jdbc:mysql://localhost:3306/studentportal";
 private static final String JDBC_USER = "root";
 private static final String JDBC_PASSWORD = "password";
 static {
 try {
 Class.forName("com.mysql.cj.jdbc.Driver");
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 }
 }
 public static Connection getConnection() throws SQLException {
 return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
 }
 public static void closeConnection(Connection conn) {
 if (conn != null) {
 try {
 conn.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
   }
 }
}
create model classes:
// com.studentportal.model.Student.java
package com.studentportal.model;
public class Student {
 private int studentId;
 private String name;
 private String className;
 private char section;
 // Constructors
 public Student() {}
 public Student(int studentId, String name, String className, char section) {
 this.studentId = studentId;
 this.name = name;
 this.className = className;
 this.section = section;
 }
 // Getters and Setters
 public int getStudentId() {
 return studentId;
 }
 public void setStudentId(int studentId) {
 this.studentId = studentId;
 }

 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
  public String getClassName() {
 return className;
 }
 public void setClassName(String className) {
 this.className = className;
 }
 public char getSection() {
 return section;
 }
 public void setSection(char section) {
 this.section = section;
 }
}
// com.studentportal.model.Attendance.java
package com.studentportal.model;
import java.util.Date;
public class Attendance {
 private int id;
 private int studentId;
 private Date date;
 private String status;
 private String remarks;
 // Constructors
 public Attendance() {}

 public Attendance(int id, int studentId, Date date, String status, String remarks) {
 this.id = id;
 this.studentId = studentId;
 this.date = date;
 this.status = status;
 this.remarks = remarks;
 }
 // Getters and Setters
 public int getId() {
 return id;
 }
 public void setId(int id) {
 this.id = id;
 }
 public int getStudentId() {
 return studentId;
 }
 public void setStudentId(int studentId) {
 this.studentId = studentId;
 }
 public Date getDate() {
 return date;
 }
 public void setDate(Date date) {
 this.date = date;
 }
 public String getStatus() {
 return status;
 }
 public void setStatus(String status) {
 this.status = status;
 }
 public String getRemarks() {
 return remarks;
 }
 public void setRemarks(String remarks) {
 this.remarks = remarks;
 }
}
create DAO (Data Access Object) classes:
// com.studentportal.dao.StudentDAO.java
package com.studentportal.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.studentportal.model.Student;
import com.studentportal.util.DBUtil;
public class StudentDAO {
 public List<Student> getAllStudents() throws SQLException {
 List<Student> students = new ArrayList<>();
 Connection conn = null;
 try {
 conn = DBUtil.getConnection();
 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students ORDER BY name");
 ResultSet rs = pstmt.executeQuery();
 while (rs.next()) {
 Student student = new Student();
 student.setStudentId(rs.getInt("student_id"));
 student.setName(rs.getString("name"));
 student.setClassName(rs.getString("class"));
 student.setSection(rs.getString("section").charAt(0));
 students.add(student);
 }
 rs.close();
 pstmt.close();
 } finally {
 DBUtil.closeConnection(conn);
 }
 return students;
 }
 public Student getStudentById(int studentId) throws SQLException {
   Student student = null;
 Connection conn = null;
 try {
 conn = DBUtil.getConnection();
 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students WHERE student_id = ?");
 pstmt.setInt(1, studentId);
 ResultSet rs = pstmt.executeQuery();
 if (rs.next()) {
 student = new Student();
 student.setStudentId(rs.getInt("student_id"));
 student.setName(rs.getString("name"));
 student.setClassName(rs.getString("class"));
 student.setSection(rs.getString("section").charAt(0));
 }
 rs.close();
 pstmt.close();
 } finally {
 DBUtil.closeConnection(conn);
 }

 return student;
 }
 public List<Student> getStudentsByClassAndSection(String className, char section) throws SQLException {
 List<Student> students = new ArrayList<>();
 Connection conn = null;
 try {
 conn = DBUtil.getConnection();
 PreparedStatement pstmt = conn.prepareStatement(
 "SELECT * FROM students WHERE class = ? AND section = ? ORDER BY name");
 pstmt.setString(1, className);
 pstmt.setString(2, String.valueOf(section));
 ResultSet rs = pstmt.executeQuery();
 while (rs.next()) {
   Student student = new Student();
 student.setStudentId(rs.getInt("student_id"));
 student.setName(rs.getString("name"));
 student.setClassName(rs.getString("class"));
 student.setSection(rs.getString("section").charAt(0));
 students.add(student);
 }
 rs.close();
 pstmt.close();
 } finally {
 DBUtil.closeConnection(conn);
 }
 return students;
 }
}
// com.studentportal.dao.AttendanceDAO.java
package com.studentportal.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.studentportal.model.Attendance;
import com.studentportal.util.DBUtil;
public class AttendanceDAO {
 public boolean saveAttendance(Attendance attendance) throws SQLException {
 Connection conn = null;
 boolean success = false;
 try {
 conn = DBUtil.getConnection(); 
   // Check if an entry already exists for this student on this date
 PreparedStatement checkStmt = conn.prepareStatement(
 "SELECT id FROM attendance WHERE student_id = ? AND date = ?");
 checkStmt.setInt(1, attendance.getStudentId());
 checkStmt.setDate(2, new java.sql.Date(attendance.getDate().getTime()));
 ResultSet rs = checkStmt.executeQuery();
 if (rs.next()) {
 // Update existing record
 int id = rs.getInt("id");
 PreparedStatement updateStmt = conn.prepareStatement(
 "UPDATE attendance SET status = ?, remarks = ? WHERE id = ?");
 updateStmt.setString(1, attendance.getStatus());
 updateStmt.setString(2, attendance.getRemarks());
 updateStmt.setInt(3, id);

 success = updateStmt.executeUpdate() > 0;
 updateStmt.close();
 } else {
 // Insert new record
 PreparedStatement insertStmt = conn.prepareStatement(
 "INSERT INTO attendance (student_id, date, status, remarks) VALUES (?, ?, ?, ?)");
 insertStmt.setInt(1, attendance.getStudentId());
 insertStmt.setDate(2, new java.sql.Date(attendance.getDate().getTime()));
 insertStmt.setString(3, attendance.getStatus());
 insertStmt.setString(4, attendance.getRemarks());
 success = insertStmt.executeUpdate() > 0;
 insertStmt.close();}
 rs.close();
 checkStmt.close();
 } finally {
 DBUtil.closeConnection(conn);
 }
 return success;
    }
 public List<Attendance> getAttendanceByDate(Date date) throws SQLException {
 List<Attendance> attendanceList = new ArrayList<>();
 Connection conn = null;
 try {
 conn = DBUtil.getConnection();
 PreparedStatement pstmt = conn.prepareStatement(
 "SELECT * FROM attendance WHERE date = ?");
 pstmt.setDate(1, new java.sql.Date(date.getTime()));
 ResultSet rs = pstmt.executeQuery();
 while (rs.next()) {
 Attendance attendance = new Attendance();
 attendance.setId(rs.getInt("id"));
 attendance.setStudentId(rs.getInt("student_id"));
 attendance.setDate(rs.getDate("date"));
 attendance.setStatus(rs.getString("status"));
 attendance.setRemarks(rs.getString("remarks"));
 attendanceList.add(attendance);
 }
 rs.close();
 pstmt.close();
 } finally {
 DBUtil.closeConnection(conn);
 }
 return attendanceList;
 }
 public List<Attendance> getAttendanceByStudent(int studentId) throws SQLException {
 List<Attendance> attendanceList = new ArrayList<>();
 Connection conn = null;
 try {
 conn = DBUtil.getConnection();
 PreparedStatement pstmt = conn.prepareStatement(
 "SELECT * FROM attendance WHERE student_id = ? ORDER BY date DESC");
   pstmt.setInt(1, studentId);
 ResultSet rs = pstmt.executeQuery();
 while (rs.next()) {
 Attendance attendance = new Attendance();
 attendance.setId(rs.getInt("id"));
 attendance.setStudentId(rs.getInt("student_id"));
 attendance.setDate(rs.getDate("date"));
 attendance.setStatus(rs.getString("status"));
 attendance.setRemarks(rs.getString("remarks"));
 attendanceList.add(attendance);
 }

 rs.close();
 pstmt.close();
 } finally {
 DBUtil.closeConnection(conn);
 }
 return attendanceList;
 }
}
create the servlet to handle attendance submission:
// com.studentportal.servlet.AttendanceServlet.java
package com.studentportal.servlet;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.studentportal.dao.AttendanceDAO;
import com.studentportal.model.Attendance;
@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
 String dateStr = request.getParameter("date");
 String className = request.getParameter("class");
 String section = request.getParameter("section");
 try {
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Date date = dateFormat.parse(dateStr);
 String[] studentIds = request.getParameterValues("studentId");
 String[] statuses = request.getParameterValues("status");
 String[] remarks = request.getParameterValues("remarks");
 AttendanceDAO attendanceDAO = new Attendance

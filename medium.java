create the database structure:
-- Create employee table
CREATE TABLE employees (
 id INT PRIMARY KEY,
 name VARCHAR(100) NOT NULL,
 position VARCHAR(100),
 salary DECIMAL(10,2),
 hire_date DATE
);
-- Insert some sample data
INSERT INTO employees VALUES (101, 'John Doe', 'Software Engineer', 75000.00, '2020-01-15');
INSERT INTO employees VALUES (102, 'Jane Smith', 'Project Manager', 85000.00, '2019-05-20');
INSERT INTO employees VALUES (103, 'Bob Johnson', 'UI/UX Designer', 70000.00, '2021-03-10');
INSERT INTO employees VALUES (104, 'Alice Williams', 'Database Administrator', 80000.00, '2018-11-05');
INSERT INTO employees VALUES (105, 'Charlie Brown', 'System Analyst', 72000.00, '2020-09-25');
create the HTML form for searching employees:
<!-- employeeSearch.html -->
<!DOCTYPE html>
  <html>
<head>
 <title>Employee Search</title>
 <style>
 body {
 font-family: Arial, sans-serif;
 margin: 40px;
 }
 .container {
 width: 80%;
 max-width: 800px;
 margin: 0 auto;
 }
 .search-box {
 padding: 20px;
 background-color: #f5f5f5;
 border-radius: 5px;
 margin-bottom: 20px;
 }
 input[type="text"] {
 padding: 8px;
 width: 200px;
 }
 button {
 padding: 8px 15px;
 background-color: #4CAF50;
 color: white;
 border: none;
 cursor: pointer;
 }
 a.button {
   padding: 8px 15px;
 background-color: #2196F3;
 color: white;
 text-decoration: none;
 border-radius: 3px;
 margin-left: 10px;
 }
 </style>
</head>
<body>
 <div class="container">
 <h1>Employee Directory</h1>
 <div class="search-box">
 <h3>Search Employee by ID</h3>
 <form action="EmployeeServlet" method="get">
 <input type="text" name="empId" placeholder="Enter Employee ID">
 <button type="submit">Search</button>
 <a href="EmployeeServlet" class="button">View All Employees</a>
 </form>
 </div>
 </div>
</body>
</html>
create an Employee model class:
// Employee.java
import java.util.Date;
public class Employee {
 private int id;
 private String name;
 private String position;
 private double salary;
   private Date hireDate;
 // Constructors
 public Employee() {}
 public Employee(int id, String name, String position, double salary, Date hireDate) {
 this.id = id;
 this.name = name;
 this.position = position;
 this.salary = salary;
 this.hireDate = hireDate;
 }
 // Getters and Setters
 public int getId() {
 return id;
 }
 public void setId(int id) {
 this.id = id;
 }
 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
 }
 public String getPosition() {
 return position;
 }
 public void setPosition(String position) {
 this.position = position;
 }
 public double getSalary() {
 return salary
   }
 public void setSalary(double salary) {
 this.salary = salary;
 }
 public Date getHireDate() {
 return hireDate;
 }
 public void setHireDate(Date hireDate) {
 this.hireDate = hireDate;
 }
}
create the EmployeeServlet:
// EmployeeServlet.java
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 protected void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
 response.setContentType("text/html");
 PrintWriter out = response.getWriter();
 String empIdParam = request.getParameter("empId");
 Connection conn = null;
 try {
 conn = DBUtil.getConnection();
 List<Employee> employees = new ArrayList<>();
 if (empIdParam != null && !empIdParam.trim().isEmpty()) {
 // Search for specific employee
 int empId = Integer.parseInt(empIdParam);
 PreparedStatement pstmt = conn.prepareStatement(
 "SELECT * FROM employees WHERE id = ?");
 pstmt.setInt(1, empId);
 ResultSet rs = pstmt.executeQuery();
 while (rs.next()) {
 Employee emp = new Employee();
 emp.setId(rs.getInt("id"));
 emp.setName(rs.getString("name"));
 emp.setPosition(rs.getString("position"));
 emp.setSalary(rs.getDouble("salary"));
 emp.setHireDate(rs.getDate("hire_date"));
 employees.add(emp);
 }
 rs.close();
 pstmt.close();
 } else {
 // Fetch all employees
 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employees");
 ResultSet rs = pstmt.executeQuery();
 while (rs.next()) {
   Employee emp = new Employee();
 emp.setId(rs.getInt("id"));
 emp.setName(rs.getString("name"));
 emp.setPosition(rs.getString("position"));
 emp.setSalary(rs.getDouble("salary"));
 emp.setHireDate(rs.getDate("hire_date"));
 employees.add(emp);
 }
 rs.close();
 pstmt.close();
 }
 // Generate HTML output
 out.println("<!DOCTYPE html>");
 out.println("<html>");
 out.println("<head>");
 out.println("<title>Employee Directory</title>");
 out.println("<style>");
 out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
 out.println(".container { width: 80%; max-width: 800px; margin: 0 auto; }");
 out.println("table { width: 100%; border-collapse: collapse; }");
 out.println("th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }");
 out.println("th { background-color: #f2f2f2; }");
 out.println(".search-box { padding: 20px; background-color: #f5f5f5; border-radius: 5px; margin-bottom:
20px; }");
 out.println("input[type=\"text\"] { padding: 8px; width: 200px; }");
 out.println("button { padding: 8px 15px; background-color: #4CAF50; color: white; border: none; cursor:
pointer; }");
 out.println("a.button { padding: 8px 15px; background-color: #2196F3; color: white; text-decoration: none;
border-radius: 3px; margin-left: 10px; display: inline-block; }");
 out.println(".no-results { background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px;
}");
 out.println("</style>");
out.println("</head>");
 out.println("<body>");
 out.println("<div class='container'>");
 out.println("<h1>Employee Directory</h1>");

 out.println("<div class='search-box'>");
 out.println("<h3>Search Employee by ID</h3>");
 out.println("<form action='EmployeeServlet' method='get'>");
 out.println("<input type='text' name='empId' placeholder='Enter Employee ID' value='" +
 (empIdParam != null ? empIdParam : "") + "'>");
 out.println("<button type='submit'>Search</button>");
 out.println("<a href='EmployeeServlet' class='button'>View All Employees</a>");
 out.println("</form>");
 out.println("</div>");
 if (employees.isEmpty()) {
 out.println("<div class='no-results'>");
 out.println("<h3>No employees found</h3>");
 out.println("</div>");
 } else {
 out.println("<table>");
 out.println("<tr>");
 out.println("<th>ID</th>");
 out.println("<th>Name</th>");
 out.println("<th>Position</th>");
 out.println("<th>Salary</th>");
 out.println("<th>Hire Date</th>");
 out.println("</tr>");
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 for (Employee emp : employees) {
 out.println("<tr>");
 out.println("<td>" + emp.getId() + "</td>");
   out.println("<td>" + emp.getName() + "</td>");
 out.println("<td>" + emp.getPosition() + "</td>");
 out.println("<td>$" + String.format("%.2f", emp.getSalary()) + "</td>");
 out.println("<td>" + dateFormat.format(emp.getHireDate()) + "</td>");
 out.println("</tr>");
 }
 out.println("</table>");
 }
 out.println("</div>");
 out.println("</body>");
 out.println("</html>");
 } catch (SQLException e) {
 out.println("<h3>Database Error: " + e.getMessage() + "</h3>");
 e.printStackTrace();
 } catch (NumberFormatException e) {
 out.println("<h3>Invalid Employee ID format</h3>");
 } finally {
 DBUtil.closeConnection(conn);
 }
 }
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
 doGet(request, response);
 }
}
create a DBUtil class to manage database connections:
// DBUtil.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBUtil {
   private static final String JDBC_URL = "jdbc:mysql://localhost:3306/employeedb";
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

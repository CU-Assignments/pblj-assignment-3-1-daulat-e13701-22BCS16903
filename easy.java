create the HTML login form:
<!-- login.html -->
<!DOCTYPE html>
<html>
<head>
 <title>Login Form</title>
 <style>
 body {
 font-family: Arial, sans-serif;
 margin: 40px;
 }
 .login-container {
 width: 300px;
 padding: 20px;
 border: 1px solid #ddd;
 border-radius: 5px;
 }
input[type="text"], input[type="password"] {
 width: 100%;
 padding: 10px;
 margin: 8px 0;
 box-sizing: border-box;
 }
 input[type="submit"] {
 background-color: #4CAF50;
 color: white;
 padding: 10px 15px;
 border: none;
 cursor: pointer;
 width: 100%;
 }
 </style>
</head>
<body>
 <div class="login-container">
 <h2>User Login</h2>
 <form action="LoginServlet" method="post">
 <label for="username">Username:</label>
 <input type="text" id="username" name="username" required>
 <label for="password">Password:</label>
 <input type="password" id="password" name="password" required>
 <input type="submit" value="Login">
 </form>
 </div>
</body>
</html>
create the servlet to handle the login:
// LoginServlet.java
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 // Hard-coded credentials for demonstration
 private static final String VALID_USERNAME = "admin";
 private static final String VALID_PASSWORD = "password";
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {
 // Get the form parameters
 String username = request.getParameter("username");
 String password = request.getParameter("password");
 response.setContentType("text/html");
 PrintWriter out = response.getWriter();
 out.println("<!DOCTYPE html>");
 out.println("<html>");
 out.println("<head>");
 out.println("<title>Login Result</title>");
 out.println("<style>");
 out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
 out.println(".message { padding: 20px; border-radius: 5px; margin-top: 20px; }");
 out.println(".success { background-color: #dff0d8; color: #3c763d; }");
 out.println(".error { background-color: #f2dede; color: #a94442; }");
 out.println("</style>");
 out.println("</head>");
 out.println("<body>");
 // Validate credentials
 if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
 out.println("<div class='message
   out.println("<h2>Welcome, " + username + "!</h2>");
 out.println("<p>You have successfully logged in.</p>");
 out.println("</div>");
 } else {
 out.println("<div class='message error'>");
 out.println("<h2>Login Failed</h2>");
 out.println("<p>Invalid username or password. Please try again.</p>");
 out.println("<a href='login.html'>Back to Login</a>");
 out.println("</div>"); }
 out.println("</body>");
 out.println("</html>");
 }
}

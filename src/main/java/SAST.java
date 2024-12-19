import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class VulnerableCodeExample extends HttpServlet {

    // Vulnerability 1: SQL Injection
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId"); // User input
        String query = "SELECT * FROM users WHERE user_id = '" + userId + "'"; // SQL Injection vulnerability
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "password");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                response.getWriter().println("User: " + rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vulnerability 2: Insecure Deserialization
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String objectData = request.getParameter("objectData"); // User input
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(objectData)));
            Object obj = in.readObject(); // Insecure deserialization
            response.getWriter().println("Deserialized object: " + obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Vulnerability 3: Cross-Site Scripting (XSS)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("search"); // User input
        response.setContentType("text/html");
        response.getWriter().println("<html><body><h1>Search Results for: " + searchTerm + "</h1>");
        response.getWriter().println("<p>" + searchTerm + "</p>"); // XSS vulnerability
        response.getWriter().println("</body></html>");
    }

    // Vulnerability 4: Hardcoded Password
    public void connectToDatabase() {
        String username = "admin";
        String password = "password123"; // Hardcoded password
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", username, password)) {
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vulnerability 5: Insecure Direct Object Reference (IDOR)
    public void viewProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String profileId = request.getParameter("profileId"); // User input
        String query = "SELECT * FROM profiles WHERE profile_id = '" + profileId + "'"; // IDOR vulnerability
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "password");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                response.getWriter().println("Profile: " + rs.getString("profile_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

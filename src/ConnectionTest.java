import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple standalone class to test database connection.
 * 
 * @author Eric Stine
 */
public class ConnectionTest {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://db.engr.ship.edu:3306/cmsc471_18?useTimezone=true&serverTimezone=UTC";
    private static final String DB_USER = "cmsc471_18";
    private static final String DB_PASSWORD = "Password_18";
    
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Open a connection
            System.out.println("Testing connection to database...");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            if (connection != null) {
                System.out.println("Connection successful! Database is accessible.");
                System.out.println("Database URL: " + DB_URL);
                System.out.println("Database User: " + DB_USER);
            } else {
                System.out.println("Failed to establish connection.");
            }
            
        } catch (SQLException se) {
            // Handle JDBC errors
            System.out.println("SQL Exception occurred: " + se.getMessage());
            se.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Handle Class.forName errors
            System.out.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other errors
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
} 
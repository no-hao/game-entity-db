import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
<<<<<<< HEAD
 * Standalone MySQL connection test for cmsc471_18.
 * Prints detailed diagnostics on success or failure.
 * 
 * @author Eric Stine
 */
public class ConnectionTest {
<<<<<<< HEAD
    private static final String DB_URL = "jdbc:mysql://db.engr.ship.edu:3306/cmsc471_18?useTimezone=true&serverTimezone=UTC";
    private static final String DB_USER = "cmsc471_18";
    private static final String DB_PASSWORD = "Password_18";

    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===");
        System.out.println("Target: " + DB_URL);
        try {
            System.out.print("Loading MySQL JDBC driver... ");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("OK");

            System.out.print("Connecting... ");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                System.out.println("SUCCESS!");
                System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("\nERROR: MySQL JDBC driver not found in classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("\nERROR: SQL Exception during connection.");
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            if (e.getErrorCode() == 1045) {
                System.err.println("Access denied: Check username/password.");
            } else if (e.getErrorCode() == 2003) {
                System.err.println("Can't connect: Check network/firewall or server status.");
            }
        }
    }
}
=======
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
>>>>>>> 881746ae643b667cd0a082b37df4e4f61325944b

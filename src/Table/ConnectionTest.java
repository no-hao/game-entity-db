import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Standalone MySQL connection test for cmsc471_18.
 * Prints detailed diagnostics on success or failure.
 * 
 * @author Eric Stine
 */
public class ConnectionTest {

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
package Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

/**
 * This class handles the initial database schema creation by creating all required tables and constraints.
 * It connects to the database, executes SQL statements to create the schema, and then closes the connection.
 * It does NOT add any data to the tables - that's handled separately in another class.
 * 
 * @author Eric Stine
 */
public class SchemaCreator {
    // Database connection parameters for local testing
    // private static final String DB_URL = "jdbc:mysql://localhost:3306/gamedb?useTimezone=true&serverTimezone=UTC";
    // private static final String DB_USER = "root";
    // private static final String DB_PASSWORD = "Password_27";
    
    // Set to true for school deployment:
    private static final String DB_URL = "jdbc:mysql://db.engr.ship.edu:3306/cmsc471_18?useTimezone=true&serverTimezone=UTC";
    private static final String DB_USER = "cmsc471_18";
    private static final String DB_PASSWORD = "Password_18";
    
    // Flag to control whether to drop existing tables before creating them
    private static final boolean DROP_EXISTING_TABLES = true;
    
    // Flag to control debug output
    private static final boolean DEBUG_OUTPUT = true;
    
    /**
     * Constructor that initiates the schema creation process
     */
    public SchemaCreator() {
        createSchema();
    }
    
    /**
     * Sets up the database by creating all required tables and constraints
     */
    private void createSchema() {
        Connection connection = null;
        Statement statement = null;
        
        try {
            // Register JDBC driver
            debugLog("Loading JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            debugLog("JDBC driver loaded successfully");
            
            // Open a connection
            debugLog("Connecting to database at URL: " + DB_URL);
            debugLog("Using username: " + DB_USER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Verify connection
            if (DEBUG_OUTPUT) {
                DatabaseMetaData metaData = connection.getMetaData();
                debugLog("Connected successfully to: " + metaData.getURL());
                debugLog("Database product: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
                debugLog("JDBC driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
            } else {
                System.out.println("Connected to database successfully.");
            }
            
            // Execute SQL statements to create tables
            System.out.println("Creating database schema...");
            statement = connection.createStatement();
            
            // Optionally drop existing tables based on flag
            if (DROP_EXISTING_TABLES) {
                System.out.println("Dropping any existing tables first...");
                dropTablesIfExist(statement);
            }
            
            // Create PERSON table
            debugLog("Creating PERSON table...");
            statement.executeUpdate("CREATE TABLE PERSON (" +
                "loginId VARCHAR(12) NOT NULL, " +
                "email VARCHAR(50) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "dateCreated DATE NOT NULL, " +
                "PRIMARY KEY (loginId), " +
                "UNIQUE(email))");
            
            // Create MANAGER table
            debugLog("Creating MANAGER table...");
            statement.executeUpdate("CREATE TABLE MANAGER (" +
                "loginId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (loginId), " +
                "FOREIGN KEY (loginId) REFERENCES PERSON (loginId))");
            
            // Create MODERATOR table
            debugLog("Creating MODERATOR table...");
            statement.executeUpdate("CREATE TABLE MODERATOR (" +
                "loginId VARCHAR(12) NOT NULL, " +
                "isSilenced BOOLEAN NOT NULL, " +
                "isBlocked BOOLEAN NOT NULL, " +
                "worksWith VARCHAR(12), " +
                "PRIMARY KEY (loginId), " +
                "FOREIGN KEY (loginId) REFERENCES PERSON (loginId), " +
                "FOREIGN KEY (worksWith) REFERENCES MANAGER (loginId))");
            
            // Create PLAYER table
            debugLog("Creating PLAYER table...");
            statement.executeUpdate("CREATE TABLE PLAYER (" +
                "loginId VARCHAR(12) NOT NULL, " +
                "isSilenced BOOLEAN NOT NULL, " +
                "isBlocked BOOLEAN NOT NULL, " +
                "watchedBy VARCHAR(12), " +
                "PRIMARY KEY (loginId), " +
                "FOREIGN KEY (loginId) REFERENCES PERSON (loginId), " +
                "FOREIGN KEY (watchedBy) REFERENCES PERSON (loginId))");
            
            // Create LOCATION table
            debugLog("Creating LOCATION table...");
            statement.executeUpdate("CREATE TABLE LOCATION (" +
                "lId VARCHAR(12) NOT NULL, " +
                "size INT NOT NULL, " +
                "type VARCHAR(8) NOT NULL, " +
                "PRIMARY KEY (lId))");
            
            // Create EXITSTO table
            debugLog("Creating EXITSTO table...");
            statement.executeUpdate("CREATE TABLE EXITSTO (" +
                "enterId VARCHAR(12) NOT NULL, " +
                "exitId VARCHAR(12) NOT NULL, " +
                "FOREIGN KEY (enterId) REFERENCES LOCATION (lId), " +
                "FOREIGN KEY (exitId) REFERENCES LOCATION (lId))");
            
            // Create GAMECHARACTER table
            debugLog("Creating GAMECHARACTER table...");
            statement.executeUpdate("CREATE TABLE GAMECHARACTER (" +
                "name VARCHAR(20) NOT NULL, " +
                "playerId VARCHAR(12) NOT NULL, " +
                "maxPoints INT NOT NULL, " +
                "currentPoints INT NOT NULL, " +
                "stamina INT NOT NULL, " +
                "strength INT NOT NULL, " +
                "profilePicture MEDIUMBLOB, " +
                "locationId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (name), " +
                "FOREIGN KEY (locationId) REFERENCES LOCATION (lId))");
            
            // Create ITEM table
            debugLog("Creating ITEM table...");
            statement.executeUpdate("CREATE TABLE ITEM (" +
                "IdItem VARCHAR(12) NOT NULL, " +
                "description TEXT, " +
                "volume INT NOT NULL, " +
                "weight DECIMAL(4, 2) NOT NULL, " +
                "isTwoHanded BOOLEAN NOT NULL, " +
                "ownedBy VARCHAR(20), " +
                "PRIMARY KEY (IdItem), " +
                "FOREIGN KEY (ownedBy) REFERENCES GAMECHARACTER (name))");
            
            // Create ARMOR table
            debugLog("Creating ARMOR table...");
            statement.executeUpdate("CREATE TABLE ARMOR (" +
                "aId VARCHAR(12) NOT NULL, " +
                "protection INT NOT NULL, " +
                "equipLocation VARCHAR(12) NOT NULL, " +
                "isEquiped BOOLEAN NOT NULL, " +
                "PRIMARY KEY (aId), " +
                "FOREIGN KEY (aId) REFERENCES ITEM (IdItem))");
            
            // Create CONTAINER table
            debugLog("Creating CONTAINER table...");
            statement.executeUpdate("CREATE TABLE CONTAINER (" +
                "cId VARCHAR(12) NOT NULL, " +
                "volume INT NOT NULL, " +
                "weight DECIMAL(4,2) NOT NULL, " +
                "PRIMARY KEY (cId), " +
                "FOREIGN KEY (cId) REFERENCES ITEM (IdItem))");
            
            // Create CONTAINEDIN table
            debugLog("Creating CONTAINEDIN table...");
            statement.executeUpdate("CREATE TABLE CONTAINEDIN (" +
                "cId VARCHAR(12) NOT NULL, " +
                "IdItem VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (cId), " +
                "FOREIGN KEY (cId) REFERENCES CONTAINER (cId), " +
                "FOREIGN KEY (IdItem) REFERENCES ITEM (IdItem))");
            
            // Create ABILITY table
            debugLog("Creating ABILITY table...");
            statement.executeUpdate("CREATE TABLE ABILITY (" +
                "name VARCHAR(12) NOT NULL, " +
                "targetStat VARCHAR(20) NOT NULL, " +
                "amount INT NOT NULL, " +
                "durationToExecute INT NOT NULL, " +
                "cooldown DECIMAL(3,1) NOT NULL, " +
                "uses INT NOT NULL, " +
                "PRIMARY KEY (name))");
            
            // Create WEAPON table
            debugLog("Creating WEAPON table...");
            statement.executeUpdate("CREATE TABLE WEAPON (" +
                "wId VARCHAR(12) NOT NULL, " +
                "ability VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (wId), " +
                "FOREIGN KEY (wId) REFERENCES ITEM (IdItem))");
            
            // Create CREATURE table
            debugLog("Creating CREATURE table...");
            statement.executeUpdate("CREATE TABLE CREATURE (" +
                "name VARCHAR(12) NOT NULL, " +
                "damageProtection INT NOT NULL, " +
                "currentPoints INT NOT NULL, " +
                "stamina INT NOT NULL, " +
                "maxPoints INT NOT NULL, " +
                "locationId VARCHAR(12) NOT NULL, " +
                "ability VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (name), " +
                "FOREIGN KEY (locationId) REFERENCES LOCATION (lId), " +
                "FOREIGN KEY (ability) REFERENCES ABILITY (name))");
            
            // Create CREATURELIKES table
            debugLog("Creating CREATURELIKES table...");
            statement.executeUpdate("CREATE TABLE CREATURELIKES (" +
                "cId VARCHAR(12) NOT NULL, " +
                "rId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (cId), " +
                "FOREIGN KEY (cId) REFERENCES CREATURE (name), " +
                "FOREIGN KEY (rId) REFERENCES CREATURE (name))");
            
            // Create CREATUREHATES table
            debugLog("Creating CREATUREHATES table...");
            statement.executeUpdate("CREATE TABLE CREATUREHATES (" +
                "cId VARCHAR(12) NOT NULL, " +
                "rId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (cId), " +
                "FOREIGN KEY (cId) REFERENCES CREATURE (name), " +
                "FOREIGN KEY (rId) REFERENCES CREATURE (name))");
            
            // Create PLAYERLIKES table
            debugLog("Creating PLAYERLIKES table...");
            statement.executeUpdate("CREATE TABLE PLAYERLIKES (" +
                "pId VARCHAR(12) NOT NULL, " +
                "rId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (pId), " +
                "FOREIGN KEY (pId) REFERENCES PLAYER (loginId), " +
                "FOREIGN KEY (rId) REFERENCES CREATURE (name))");
            
            // Create PLAYERHATES table
            debugLog("Creating PLAYERHATES table...");
            statement.executeUpdate("CREATE TABLE PLAYERHATES (" +
                "pId VARCHAR(12) NOT NULL, " +
                "rId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (pId), " +
                "FOREIGN KEY (pId) REFERENCES PLAYER (loginId), " +
                "FOREIGN KEY (rId) REFERENCES CREATURE (name))");
            
            // Create ALLOWEDTOGO table
            debugLog("Creating ALLOWEDTOGO table...");
            statement.executeUpdate("CREATE TABLE ALLOWEDTOGO (" +
                "cId VARCHAR(12) NOT NULL, " +
                "lId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (cId), " +
                "FOREIGN KEY (cId) REFERENCES CREATURE (name), " +
                "FOREIGN KEY (lId) REFERENCES LOCATION (lId))");
            
            // Create ITEMSPRESENT table
            debugLog("Creating ITEMSPRESENT table...");
            statement.executeUpdate("CREATE TABLE ITEMSPRESENT (" +
                "lId VARCHAR(12) NOT NULL, " +
                "ItemId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (lId), " +
                "FOREIGN KEY (lId) REFERENCES LOCATION (lId), " +
                "FOREIGN KEY (ItemId) REFERENCES ITEM (IdItem))");
            
            // Create CHARACTERSPRESENT table
            debugLog("Creating CHARACTERSPRESENT table...");
            statement.executeUpdate("CREATE TABLE CHARACTERSPRESENT (" +
                "lId VARCHAR(12) NOT NULL, " +
                "cId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (lId), " +
                "FOREIGN KEY (lId) REFERENCES LOCATION (lId), " +
                "FOREIGN KEY (cId) REFERENCES GAMECHARACTER (name))");
            
            // Create CREATURESPRESENT table
            debugLog("Creating CREATURESPRESENT table...");
            statement.executeUpdate("CREATE TABLE CREATURESPRESENT (" +
                "lId VARCHAR(12) NOT NULL, " +
                "creatureId VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (lId), " +
                "FOREIGN KEY (lId) REFERENCES LOCATION (lId), " +
                "FOREIGN KEY (creatureId) REFERENCES CREATURE (name))");
            
            System.out.println("Database schema created successfully!");
            
        } catch (SQLException se) {
            // Handle JDBC errors
            System.out.println("SQL Exception occurred: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("Error Code: " + se.getErrorCode());
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
                if (statement != null) {
                    statement.close();
                    debugLog("Statement closed.");
                }
            } catch (SQLException se) {
                System.out.println("Error closing statement: " + se.getMessage());
                se.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                    debugLog("Connection closed.");
                }
            } catch (SQLException se) {
                System.out.println("Error closing connection: " + se.getMessage());
                se.printStackTrace();
            }
            System.out.println("Schema creation process complete.");
        }
    }
    
    /**
     * Drops all tables if they exist to avoid foreign key constraint issues
     * Tables are dropped in reverse order of their dependencies
     */
    private void dropTablesIfExist(Statement statement) throws SQLException {
        // Drop all tables in reverse order of dependencies
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CREATURESPRESENT");
            debugLog("Dropped CREATURESPRESENT table");
        } catch (SQLException e) {
            System.out.println("Error dropping CREATURESPRESENT: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CHARACTERSPRESENT");
            debugLog("Dropped CHARACTERSPRESENT table");
        } catch (SQLException e) {
            System.out.println("Error dropping CHARACTERSPRESENT: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS ITEMSPRESENT");
            debugLog("Dropped ITEMSPRESENT table");
        } catch (SQLException e) {
            System.out.println("Error dropping ITEMSPRESENT: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS ALLOWEDTOGO");
            debugLog("Dropped ALLOWEDTOGO table");
        } catch (SQLException e) {
            System.out.println("Error dropping ALLOWEDTOGO: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS PLAYERHATES");
            debugLog("Dropped PLAYERHATES table");
        } catch (SQLException e) {
            System.out.println("Error dropping PLAYERHATES: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS PLAYERLIKES");
            debugLog("Dropped PLAYERLIKES table");
        } catch (SQLException e) {
            System.out.println("Error dropping PLAYERLIKES: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CREATUREHATES");
            debugLog("Dropped CREATUREHATES table");
        } catch (SQLException e) {
            System.out.println("Error dropping CREATUREHATES: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CREATURELIKES");
            debugLog("Dropped CREATURELIKES table");
        } catch (SQLException e) {
            System.out.println("Error dropping CREATURELIKES: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CREATURE");
            debugLog("Dropped CREATURE table");
        } catch (SQLException e) {
            System.out.println("Error dropping CREATURE: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS WEAPON");
            debugLog("Dropped WEAPON table");
        } catch (SQLException e) {
            System.out.println("Error dropping WEAPON: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS ABILITY");
            debugLog("Dropped ABILITY table");
        } catch (SQLException e) {
            System.out.println("Error dropping ABILITY: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CONTAINEDIN");
            debugLog("Dropped CONTAINEDIN table");
        } catch (SQLException e) {
            System.out.println("Error dropping CONTAINEDIN: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS CONTAINER");
            debugLog("Dropped CONTAINER table");
        } catch (SQLException e) {
            System.out.println("Error dropping CONTAINER: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS ARMOR");
            debugLog("Dropped ARMOR table");
        } catch (SQLException e) {
            System.out.println("Error dropping ARMOR: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS ITEM");
            debugLog("Dropped ITEM table");
        } catch (SQLException e) {
            System.out.println("Error dropping ITEM: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS GAMECHARACTER");
            debugLog("Dropped GAMECHARACTER table");
        } catch (SQLException e) {
            System.out.println("Error dropping GAMECHARACTER: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS EXITSTO");
            debugLog("Dropped EXITSTO table");
        } catch (SQLException e) {
            System.out.println("Error dropping EXITSTO: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS LOCATION");
            debugLog("Dropped LOCATION table");
        } catch (SQLException e) {
            System.out.println("Error dropping LOCATION: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS PLAYER");
            debugLog("Dropped PLAYER table");
        } catch (SQLException e) {
            System.out.println("Error dropping PLAYER: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS MODERATOR");
            debugLog("Dropped MODERATOR table");
        } catch (SQLException e) {
            System.out.println("Error dropping MODERATOR: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS MANAGER");
            debugLog("Dropped MANAGER table");
        } catch (SQLException e) {
            System.out.println("Error dropping MANAGER: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS PERSON");
            debugLog("Dropped PERSON table");
        } catch (SQLException e) {
            System.out.println("Error dropping PERSON: " + e.getMessage());
        }
    }
    
    /**
     * Utility method for debug logging - only prints if DEBUG_OUTPUT is true
     * 
     * @param message The debug message to print
     */
    private void debugLog(String message) {
        if (DEBUG_OUTPUT) {
            System.out.println(message);
        }
    }
}
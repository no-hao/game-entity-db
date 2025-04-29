package ServerCommunications;

import java.sql.*;
import java.util.Arrays;

/**
 * Creates Communication from Server to DB
 *
 * @author Adolfo
 */
public class Database {

  //URL of DB
  public static final String DB_LOCATION = "jdbc:mysql://db.engr.ship.edu:3306/cmsc471_18?useTimezone=true&serverTimezone=UTC";

  //Login Name of DB
  public static final String LOGIN_NAME = "cmsc471_18";

  //Password of DB
  public static final String PASSWORD = "Password_18";
  protected Connection m_dbConn = null;

  private boolean isConnected = false;

  /**
   * Creates a connection to the database that you can then send commands to.
   * Needs to be in a method.
   */
  public boolean createConnection() {
    try {
      m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
      isConnected = true;

      try (Statement s = m_dbConn.createStatement()) {
        s.execute("DROP PROCEDURE IF EXISTS get_characters_in_location");
      }

      // -- > PROCEDURE IS HERE <--
      String createProc =
              "CREATE PROCEDURE get_characters_in_location(IN location_id VARCHAR(12))\n" +
                      "BEGIN\n" +
                      "  DECLARE cnt INT DEFAULT 0;\n" +
                      "  SELECT COUNT(*) INTO cnt\n" +
                      "    FROM CHARACTERSPRESENT\n" +
                      "   WHERE lId = location_id;\n" +
                      "  IF cnt = 0 THEN\n" +
                      "    SELECT CONCAT('No characters in location ', location_id) AS message;\n" +
                      "  ELSE\n" +
                      "    SELECT gc.*\n" +
                      "      FROM GAMECHARACTER gc\n" +
                      "      JOIN CHARACTERSPRESENT cp ON gc.name = cp.cId\n" +
                      "     WHERE cp.lId = location_id;\n" +
                      "  END IF;\n" +
                      "END";
      try (Statement s = m_dbConn.createStatement()) {
        s.execute(createProc);
      }
      // -- > PROCEDURE IS HERE <--

      return true;
    } catch (SQLException e) {
      System.err.println("Database connection failed: " + e.getMessage());
      isConnected = false;
      return false;
    }
  }

  /**
   * Validates the database connection.
   * Throws an exception if the connection is not active.
   *
   * @throws SQLException if the connection is not valid.
   */
  private void validateConnection() throws SQLException {
    if (m_dbConn == null || m_dbConn.isClosed()) {
      throw new SQLException("Database connection is not active");
    }
  }

  /**
   * Handles incoming messages from the client.
   * Parses the message and executes the corresponding SQL command.
   *
   * @param message The incoming message from the client.
   * @return The result of the SQL command or an error message.
   */
  public synchronized String handleMessage(String message) {
    try {
      validateConnection();
    } catch (SQLException e) {
      return "DATABASE ERROR: Connection issue - " + e.getMessage();
    }
    if (!isConnected) {
      return "ERROR: Not connected to the database.";
    }

    try {
      String[] parts = message.split(":");
      String command = parts[0].toUpperCase();

      return switch (command) {
        case "INSERT" -> handleInsert(parts);
        case "SELECT" -> handleSelect(parts);
        case "UPDATE" -> handleUpdate(parts);
        case "DELETE" -> handleDelete(parts);
        case "PROC" -> callStoredProcedure(parts);
        default -> "ERROR: Unsupported command." + command;
      };
    } catch (SQLException e) {
      return "SQL ERROR: " + e.getMessage();
    } catch (Exception e) {
      return "ERROR: " + e.getMessage();
    }
  }


  /**
   * Handles a procedure
   * parts[1] = procedure name
   * parts>1 = in parameters
   *
   * @author Zach Kline
   */
  private String handleProcedure(String[] parts) throws SQLException {
    if (parts.length < 2) {
      return "ERROR: Malformed PROC call.";
    }

    StringBuilder placeholders = new StringBuilder();
    for (int i = 2; i < parts.length; i++) {
      placeholders.append("?,");
    }
    if (placeholders.length() > 0) {
      placeholders.setLength(placeholders.length() - 1);
    }

    String sql = "{CALL " + parts[1] + "(" + placeholders + ")}";
    try (CallableStatement stmt = m_dbConn.prepareCall(sql)) {
      // bind each in parameter
      for (int i = 2; i < parts.length; i++) {
        stmt.setString(i - 1, parts[i]);
      }

      boolean hasResult = stmt.execute();
      if (hasResult) {
        try (ResultSet rs = stmt.getResultSet()) {
          return resultSetToCSV(rs);
        }
      }

      int count = stmt.getUpdateCount();
      return (count >= 0) ? (count + " row(s) affected.") : "Procedure executed.";
    }
  }

  /**
   * Handles the INSERT command.
   * Parses the command and executes the corresponding SQL INSERT statement.
   *
   * @param parts The parsed command parts.
   * @return The result of the INSERT operation.
   */
  private String handleInsert(String[] parts) throws SQLException {
    int mid = (parts.length - 2) / 2 + 2;
    String table = parts[1];
    String[] columns = new String[(parts.length - 2) / 2];
    String[] values = new String[columns.length];

    for (int i = 2; i < mid; i++) columns[i - 2] = parts[i];
    for (int i = mid; i < parts.length; i++) values[i - mid] = parts[i];

    String sql = "INSERT INTO " + table + " (" + String.join(",", columns) + ") VALUES (" +
            "?,".repeat(values.length).replaceAll(",$", "") + ")";
    PreparedStatement stmt = m_dbConn.prepareStatement(sql);

    for (int i = 0; i < values.length; i++) {
      stmt.setString(i + 1, values[i]);
    }

    int rows = stmt.executeUpdate();
    return rows + " row(s) inserted.";
  }

  /**
   * Handles the SELECT command.
   * Parses the command and executes the corresponding SQL SELECT statement.
   *
   * @param parts The parsed command parts.
   * @return The result of the SELECT operation.
   */
  private String handleSelect(String[] parts) throws SQLException {
    if (parts.length == 3 && "*".equals(parts[2])) {
      String sql = "SELECT * FROM " + parts[1];
      try (Statement stmt = m_dbConn.createStatement();
           ResultSet rs = stmt.executeQuery(sql)) {
        return resultSetToCSV(rs);
      }
    }

    if (parts.length == 5) {
      String table = parts[1];
      String sql;
      PreparedStatement stmt;

      if (parts[3].matches("=|<>|<|>|<=|>=")) {
        sql = "SELECT * FROM " + table +
                " WHERE " + parts[2] + " " + parts[3] + " ?";
        stmt = m_dbConn.prepareStatement(sql);
        stmt.setString(1, parts[4]);
      } else {
        sql = "SELECT " + parts[2] +
                " FROM " + table +
                " WHERE " + parts[3] + " = ?";
        stmt = m_dbConn.prepareStatement(sql);
        stmt.setString(1, parts[4]);
      }

      try (ResultSet rs = stmt.executeQuery()) {
        return resultSetToCSV(rs);
      }
    }

    if (parts.length >= 4) {
      String table = parts[1];
      String[] cols = Arrays.copyOfRange(parts, 2, parts.length);
      String sql = "SELECT " + String.join(",", cols) +
              " FROM " + table;
      try (Statement stmt = m_dbConn.createStatement();
           ResultSet rs = stmt.executeQuery(sql)) {
        return resultSetToCSV(rs);
      }
    }

    return "ERROR: Malformed SELECT.";
  }

  /**
   * Helper Function that turns
   * Multiple values into a single
   * string seperated by commas.
   * @param rs
   * @return
   * @throws SQLException
   */
  private String resultSetToCSV(ResultSet rs) throws SQLException {
    StringBuilder sb = new StringBuilder();
    ResultSetMetaData meta = rs.getMetaData();
    int colCount = meta.getColumnCount();
    while (rs.next()) {
      for (int i = 1; i <= colCount; i++) {
        sb.append(rs.getString(i));
        if (i < colCount) sb.append(",");
      }
      sb.append("\n");
    }
    return sb.length() == 0 ? "No results found." : sb.toString().trim();
  }

  /**
   * Handles the UPDATE command.
   * Parses the command and executes the corresponding SQL UPDATE statement.
   *
   * @param parts The parsed command parts.
   * @return The result of the UPDATE operation.
   */
  private String handleUpdate(String[] parts) throws SQLException {
    if (parts.length != 6) return "ERROR: Malformed UPDATE.";

    String sql = "UPDATE " + parts[1] + " SET " + parts[2] + " = ? WHERE " + parts[4] + " = ?";
    PreparedStatement stmt = m_dbConn.prepareStatement(sql);
    stmt.setString(1, parts[3]);
    stmt.setString(2, parts[5]);

    int rows = stmt.executeUpdate();
    return rows + " row(s) updated.";
  }

  /**
   * Handles the DELETE command.
   * Parses the command and executes the corresponding SQL DELETE statement.
   *
   * @param parts The parsed command parts.
   * @return The result of the DELETE operation.
   */
  private String handleDelete(String[] parts) throws SQLException {
    if (parts.length != 4) return "ERROR: Malformed DELETE.";

    String sql = "DELETE FROM " + parts[1] + " WHERE " + parts[2] + " = ?";
    PreparedStatement stmt = m_dbConn.prepareStatement(sql);
    stmt.setString(1, parts[3]);

    int rows = stmt.executeUpdate();
    return rows + " row(s) deleted.";
  }

  /**
   * Calls a stored procedure with the given parameters.
   * Parses the command and executes the corresponding SQL stored procedure.
   *
   * @param parts The parsed command parts.
   * @return The result of the stored procedure call.
   */
  private String callStoredProcedure(String[] parts) throws SQLException {
    if (parts.length < 2) return "ERROR: Malformed PROC call.";

    StringBuilder placeholders = new StringBuilder();
    for (int i = 2; i < parts.length; i++) placeholders.append("?,");
    if (placeholders.length() > 0) placeholders.deleteCharAt(placeholders.length() - 1);

    String sql = "{CALL " + parts[1] + "(" + placeholders + ")}";
    CallableStatement stmt = m_dbConn.prepareCall(sql);
    for (int i = 2; i < parts.length; i++) {
      stmt.setString(i - 1, parts[i]);
    }

    boolean hasResult = stmt.execute();
    if (hasResult) {
      ResultSet rs = stmt.getResultSet();
      ResultSetMetaData meta = rs.getMetaData();
      StringBuilder result = new StringBuilder();

      while (rs.next()) {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          result.append(meta.getColumnName(i)).append(": ").append(rs.getString(i)).append(" | ");
        }
        result.append("\n");
      }
      return result.toString();
    } else {
      return "Stored procedure executed.";
    }
  }

  /**
   * Closes the database connection.
   * Ensures that the connection is properly closed when no longer needed.
   */
  public void closeConnection() {
    try {
      if (m_dbConn != null && !m_dbConn.isClosed()) {
        m_dbConn.close();
        isConnected = false;
      }
    } catch (SQLException e) {
      System.err.println("Error closing database connection: " + e.getMessage());
    }
  }
}


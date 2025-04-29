package ServerCommunications;

import Exceptions.DBException;
import java.io.IOException;

/**
 * Connection between Client and @class DataProvider.
 * This allows for data changes shown in the window to translate to client.
 * And for DataProvider to handle responses from Client.
 *
 * @author Zach Kline
 */
public class DBDataProvider {

  /**
   * Send a sql command to Server though Client
   * @param cmd Cmd wanted to be sent
   * @throws DBException
   */
  public static void sql(String cmd) throws DBException {
    try {
      String raw = Client.sendCommand(cmd);

      if (raw.indexOf("SQL ERROR:") == 0) {
        throw new DBException(raw);
      }

    } catch (IOException e) {
      throw new DBException("I/O error: " + e.getMessage());
    }
  }

  /**
   * Making a select sql request to Client
   * @param selectCmd Desired Cmd
   * @return Values from Client Received
   * @throws DBException
   */
  public static Object[][] query(String selectCmd) throws DBException {
    try {
      String raw = Client.sendCommand("SELECT:" + selectCmd);
      if (raw.isEmpty()) return new Object[0][0];
      String[] lines = raw.split("\n");
      Object[][] result = new Object[lines.length][];
      for (int i = 0; i < lines.length; i++) {
        result[i] = lines[i].split(",");
      }
      return result;
    } catch (IOException e) {
      throw new DBException("I/O error: " + e.getMessage());
    }
  }
}

package Table;

import Exceptions.DBException;
import Exceptions.InvalidDeleteException;
import Exceptions.InvalidInsertException;
import Exceptions.InvalidUpdateException;
import ServerCommunications.DBDataProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles DisplayFour of assignment
 * Give/Update/Insert data related to LOCATION table in the db
 *
 * @author Zach Kline
 */
public class DisplayFourDataProvider implements DataProvider {

  /**
   * Current Data from the db
   */
  static private Object[][] data;

  /**
   * Get the columns shown for LOCATION Table
   * @return
   */
  public String[] getColumnNames() {
    return new String[]{"Location Id", "Size", "Type", "Exits"};
  }

  /**
   * Get the data for LOCATION/EXITSTO Table
   * @return
   */
  @Override
  public Object[][] getRowData() {
    try {
      Object[][] locs = DBDataProvider.query("LOCATION:*");

      Object[][] result = new Object[locs.length][4];
      for (int i = 0; i < locs.length; i++) {
        String lId   = (String) locs[i][0];
        Integer size = Integer.valueOf((String) locs[i][1]);
        String type  = (String) locs[i][2];

        Object[][] exits = DBDataProvider.query("EXITSTO:enterId:=:" + lId);

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < exits.length; j++) {
          if (exits[j].length > 1) {
            sb.append((String) exits[j][1]);
            if (j < exits.length - 1) sb.append(", ");
          }
        }

        result[i][0] = lId;
        result[i][1] = size;
        result[i][2] = type;
        result[i][3] = sb.toString();
      }

      data = result;
      return result;
    } catch (DBException ex) {
      ex.printStackTrace();
      return new Object[0][0];
    }
  }


  /**
   * Update data for LOCATION Table
   *
   * @param row row of data affected
   * @param col col of data affected
   * @param value The new value for the data
   * @throws InvalidUpdateException
   */
  @Override
  public void updateData(int row, int col, Object value) throws InvalidUpdateException {
    if (row < 0 || row >= data.length || col < 1 || col > 3) {
      throw new InvalidUpdateException("Can only update Size, Type or Exits");
    }
    String lId   = (String) data[row][0];
    String dbCol = (col == 1 ? "size" : "type");

    if (col == 3) {
      String oldExitsStr = (String) data[row][3];
      String newExitsStr = value.toString();

      //build sets of old and new exits
      Set<String> oldExits = new HashSet<>();
      if (oldExitsStr != null && !oldExitsStr.isEmpty()) {
        for (String s : oldExitsStr.split("\\s*,\\s*")) oldExits.add(s);
      }
      Set<String> newExits = new HashSet<>();
      if (newExitsStr != null && !newExitsStr.isEmpty()) {
        for (String s : newExitsStr.split("\\s*,\\s*")) newExits.add(s);
      }

      try {
        //add new ones to db
        for (String exit : newExits) {
          if (!oldExits.contains(exit)) {
            String insertCmd = String.format("INSERT:EXITSTO:enterId:exitId:%s:%s", lId, exit);
            DBDataProvider.sql(insertCmd);
          }
        }
        //delete ones removed
        for (String exit : oldExits) {
          if (!newExits.contains(exit)) {
            String deleteCmd = String.format("DELETE:EXITSTO:enterId:exitId:%s:%s", lId, exit);
            DBDataProvider.sql(deleteCmd);
          }
        }
      } catch (DBException ex) {
        throw new InvalidUpdateException("Error updating Exits: " + ex.getMessage());
      }

      data[row][col] = newExitsStr;
      return;
    }

    String cmd = String.format("UPDATE:LOCATION:%s:%s:lId:%s", dbCol, value, lId);

    try {
      DBDataProvider.sql(cmd);
      data[row][col] = value;
    } catch (DBException ex) {
      throw new InvalidUpdateException(ex.getMessage());
    }
  }

  /**
   * Delete for LOCATION table
   *
   * @param row Row that was deleted
   * @throws InvalidDeleteException
   */
  @Override
  public void deleteRow(int row) throws InvalidDeleteException {
    if (row < 0 || row >= data.length) return;
    String lId = (String) data[row][0];
    try {
      // cascade removal of exits is handled by DB; just delete location
      DBDataProvider.sql("DELETE:LOCATION:lId:" + lId);
    } catch (DBException ex) {
      throw new InvalidDeleteException(ex.getMessage());
    }
    // update cache
    Object[][] newData = new Object[data.length - 1][data[0].length];
    for (int i = 0, j = 0; i < data.length; i++) {
      if (i == row) continue;
      newData[j++] = data[i];
    }
    data = newData;
  }


  /**
   * Get columns needed for insert in LOCATION table
   * @return
   */
  @Override
  public String[] getInsertColumnNames() {
    return new String[]{"Name", "Size", "Type"};
  }


  /**
   * Handle Insert for LOCATION table
   * @param values
   * @throws InvalidInsertException
   */
  @Override
  public void insertRow(Object[] values) throws InvalidInsertException {
    if (values == null || values.length != 3) {
      throw new InvalidInsertException("Expected 3 values: Location Id, Size, Type");
    }
    String lId  = (String) values[0];
    String size = values[1].toString();
    String type = (String) values[2];

    String cmd = String.format("INSERT:LOCATION:lId:size:type:%s:%s:%s", lId, size, type);

    try {
      DBDataProvider.sql(cmd);
    } catch (DBException ex) {
      throw new InvalidInsertException(ex.getMessage());
    }
    data = getRowData();
  }

}
package Table;

import Exceptions.DBException;
import Exceptions.InvalidDeleteException;
import Exceptions.InvalidInsertException;
import Exceptions.InvalidUpdateException;
import ServerCommunications.DBDataProvider;

/**
 * Handles DisplayThree of assignment
 * Give/Update/Insert data related to ABILITY table in the db
 *
 * @author Zach Kline
 */
public class DisplayThreeDataProvider implements DataProvider {
  private static final String TABLE = "ABILITY";
  private static final String[] DB_COLS = {
          "name",
          "targetStat",
          "amount",
          "durationToExecute",
          "cooldown",
          "uses"
  };
  static private Object[][] data;


  /**
   * Get the columns shown for ABILITY Table
   * @return
   */
  public String[] getColumnNames() {
    return new String[]{"Name", "Target Stat", "Amount", "Dur To Execute", "Cool down", "Uses"};
  }

  /**
   * Get the data for ABILITY Table
   * @return
   */
  @Override
  public Object[][] getRowData() {
    try {
      data = DBDataProvider.query(TABLE + ":" + String.join(":", DB_COLS));
      return data;
    } catch (DBException ex) {
      ex.printStackTrace();
      return new Object[0][0];
    }
  }


  /**
   * Update data for ABILITY Table
   *
   * @param row row of data affected
   * @param col col of data affected
   * @param value The new value for the data
   * @throws InvalidUpdateException
   */
  @Override
  public void updateData(int row, int col, Object value) throws InvalidUpdateException {
    if (row < 0 || row >= data.length || col < 1 || col >= DB_COLS.length) {
      throw new InvalidUpdateException("Can only edit targetStat, amount, durationToExecute, cooldown, or uses");
    }
    String dbCol = DB_COLS[col];  // col=1→targetStat, 2→amount, …, 5→uses
    String pkVal = (String) data[row][0];
    String cmd = String.format(
            "UPDATE:ABILITY:%s:%s:name:%s",
            dbCol, value, pkVal
    );
    try {
      DBDataProvider.sql(cmd);
      data[row][col] = value;
    } catch (DBException ex) {
      throw new InvalidUpdateException(ex.getMessage());
    }
  }

  /**
   *  Delete row from ABILITY table
   * @param row Row that was deleted
   * @throws InvalidDeleteException
   */
  @Override
  public void deleteRow(int row) throws InvalidDeleteException {
    if (row < 0 || row >= data.length) return;
    String name = (String) data[row][0];
    try {
      DBDataProvider.sql(String.format("DELETE:ABILITY:name:%s", name));
    } catch (DBException ex) {
      throw new InvalidDeleteException(ex.getMessage());
    }
    // remove from local cache
    Object[][] newData = new Object[data.length - 1][data[0].length];
    for (int i = 0, j = 0; i < data.length; i++) {
      if (i == row) continue;
      newData[j++] = data[i];
    }
    data = newData;
  }


  /**
   * Get columns needed for insert in ABILITY table
   * @return
   */
  @Override
  public String[] getInsertColumnNames() {
    return new String[]{"Name", "Target Stat", "Amount", "Dur To Execute", "Cool down", "Uses"};
  }

  /**
   * Handle Insert for ABILITY table
   * @param values
   * @throws InvalidInsertException
   */
  @Override
  public void insertRow(Object[] values) throws InvalidInsertException {
    if (values == null || values.length != DB_COLS.length) {
      throw new InvalidInsertException("Expected " + DB_COLS.length + " values for insert");
    }

    StringBuilder cmd = new StringBuilder("INSERT:ABILITY:");
    for (String col : DB_COLS) {
      cmd.append(col).append(':');
    }
    for (Object v : values) {
      cmd.append(v).append(':');
    }
    //remove ;
    cmd.setLength(cmd.length() - 1);

    try {
      DBDataProvider.sql(cmd.toString());
    } catch (DBException ex) {
      throw new InvalidInsertException(ex.getMessage());
    }

    data = getRowData();
  }

}
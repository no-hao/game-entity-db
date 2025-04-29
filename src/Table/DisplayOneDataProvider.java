package Table;

import Exceptions.DBException;
import Exceptions.InvalidDeleteException;
import Exceptions.InvalidInsertException;
import Exceptions.InvalidUpdateException;
import ServerCommunications.DBDataProvider;

/**
 * Handles DisplayOne of assignment
 * Give/Update/Insert data related to GameCharacter table in the db
 *
 * @author Zach Kline
 */
public class DisplayOneDataProvider implements DataProvider {
  private static final String[] DB_COLS = {
          "name",
          "playerId",
          "maxPoints",
          "currentPoints",
          "strength",
          "stamina"
  };

  /**
   * Current Data from the db
   */
  static private Object[][] data;


  /**
   * Get the columns shown for GameCharacter Table
   * @return
   */
  @Override
  public String[] getColumnNames() {
    return new String[]{"Name", "PlayerId", "Max Points", "Current Points", "Strength", "Stamina"};
  }

  /**
   * Get the data for GameCharacter Table
   * @return
   */
  @Override
  public Object[][] getRowData() {
    try {
      data = DBDataProvider.query(
              "GAMECHARACTER:name:playerId:maxPoints:currentPoints:strength:stamina"
      );
      return data;
    } catch (DBException ex) {
      ex.printStackTrace();
      return new Object[0][0];
    }
  }


  /**
   * Update data for GameCharacter Table
   *
   * @param row row of data affected
   * @param col col of data affected
   * @param value The new value for the data
   * @throws InvalidUpdateException
   */
  @Override
  public void updateData(int row, int col, Object value) throws InvalidUpdateException {
    // bounds check against the data in the table model
    if (row < 0 || row >= data.length || col < 0 || col >= data[0].length) {
      throw new InvalidUpdateException("Out of bounds row or column update");
    }

    // MaxPts, CurPts, Strength, Stamina only editable
    if (col < 2 || col > 5) {
      throw new InvalidUpdateException("Can only edit max / current hit points, strength, or stamina");
    }

    //update to table schema
    String[] dbCols = { "maxPoints", "currentPoints", "strength", "stamina" };
    String dbColName = dbCols[col - 2];

    String cmd = String.format(
            "UPDATE:GAMECHARACTER:%s:%s:name:%s",
            dbColName, value, data[row][0]
    );

    try {
      DBDataProvider.sql(cmd);
      data[row][col] = value;
    } catch (DBException ex) {
      throw new InvalidUpdateException(ex.getMessage());
    }
  }


  /**
   * Delete for GameCharacter table
   * @param row Row that was deleted
   * @throws InvalidDeleteException
   */
  @Override
  public void deleteRow(int row) throws InvalidDeleteException {
    if (row < 0 || row >= data.length) {
      return;
    }

    String name = (String) data[row][0];
    try {
      DBDataProvider.sql(String.format("DELETE:GAMECHARACTER:name:%s", name));
    } catch (DBException ex) {
      throw new InvalidDeleteException(ex.getMessage());
    }

    Object[][] newData = new Object[data.length - 1][data[0].length];
    for (int i = 0, j = 0; i < data.length; i++) {
      if (i == row) continue;
      newData[j++] = data[i];
    }
    data = newData;
  }

  /**
   * Get columns needed for insert in GameCharacter table
   * @return
   */
  @Override
  public String[] getInsertColumnNames() {
    return new String[]{"PlayerId", "Name", "Max HP", "Current HP", "Strength", "Stamina"};
  }

  /**
   * Handle Insert for GameCharacter table
   * @param values
   * @throws InvalidInsertException
   */
  @Override
  public void insertRow(Object[] values) throws InvalidInsertException {
    if (values == null || values.length != DB_COLS.length) {
      throw new InvalidInsertException("Expected " + DB_COLS.length + " values for insert");
    }

    StringBuilder cmd = new StringBuilder("INSERT:GAMECHARACTER:name:playerId:maxPoints:currentPoints:strength:stamina:locationId:");

    for (int i = 0; i < values.length; i++) {
      cmd.append(values[i]);
      cmd.append(':');
    }

    cmd.append("Loc1");
    try {
      DBDataProvider.sql(cmd.toString());
    } catch (DBException ex) {
      throw new InvalidInsertException(ex.getMessage());
    }
  }

}
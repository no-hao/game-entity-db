package Table;

import Exceptions.DBException;
import Exceptions.InvalidDeleteException;
import Exceptions.InvalidInsertException;
import Exceptions.InvalidUpdateException;
import ServerCommunications.DBDataProvider;

import java.util.Arrays;

/**
 * Handles DisplayTwo of assignment
 * Give/Update/Insert data related to PLAYER table in the db
 * "When a player is selected it displays the email, login id and list of characters controlled by that player."
 * "They should also be able to edit/add/remove players from the list."
 * "For edit all that can be changed is the email"
 *
 * @author Zach Kline
 */
public class DisplayTwoDataProvider implements DataProvider {
  /**
   * Current Data from the db
   */
  static private Object[][] data;


  /**
   * Get the columns shown for Player Table
   * @return
   */
  public String[] getColumnNames() {
    return new String[]{"Login Id", "Email", "Characters Controlled"};
  }


  /**
   * Get the data for PLAYER / PERSON Table
   * @return
   */
  @Override
  public Object[][] getRowData() {
    try {
      Object[][] people = DBDataProvider.query("PERSON:loginId:email");

      //error if no people without this
      if (people.length == 0) {
        data = new Object[0][0];
        return data;
      }

      Object[][] result = new Object[people.length][3];

      for (int i = 0; i < people.length; i++) {
        String loginId = (String) people[i][0];
        String email   = (String) people[i][1];

        //now get characters for that player
        Object[][] chars = DBDataProvider.query("GAMECHARACTER:name:playerId:" + loginId);

        //join characters
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < chars.length; j++) {
          sb.append((String) chars[j][0]);
          if (j < chars.length - 1) sb.append(", ");
        }

        //add together
        result[i][0] = loginId;
        result[i][1] = email;
        result[i][2] = sb.toString();
      }

      data = result;

      return result;
    } catch (DBException ex) {
      ex.printStackTrace();
      return new Object[0][0];
    }
  }

  /**
   * Handles update and pushes to DB
   * @param row row of data affected
   * @param col col of data affected
   * @param value The new value for the data
   * @throws InvalidUpdateException
   */
  @Override
  public void updateData(int row, int col, Object value) throws InvalidUpdateException {
    if (row >= data.length || row < 0 || col < 0 || col >= data[0].length) {
      throw new InvalidUpdateException("Out of bounds row or column Update");
    }

    //EDIT EMAIL ONLY (hardcoded)
    if (col != 1) {
      throw new InvalidUpdateException("Can only update Players Email");
    }

    //add check for db and if error from db return false
    if (false) {
      throw new InvalidUpdateException("DB update error invalid type");
    }

    data[row][col] = value;
  }


  /**
   * Row has been deleted
   * forward deleted row to DB
   * @param row Row that was deleted
   * @throws InvalidDeleteException
   */
  @Override
  public void deleteRow(int row) throws InvalidDeleteException {
    if (row < 0 || row >= data.length) {
      return;
    }

    try {
      DBDataProvider.sql(String.format("DELETE:PERSON:loginId:%s", data[row][0]));
    } catch (DBException ex) {
      throw new InvalidDeleteException(ex.getMessage());
    }

    if (data.length == 1) {
      getRowData();
      return;
    }

    Object[][] newData = new Object[data.length - 1][data[0].length];
    for (int i = 0, j = 0; i < data.length; i++) {
      if (i == row) continue;
      newData[j++] = data[i];
    }
    data = newData;
  }


  /**
   * All the values required
   * to complete an insert.
   * @return String[] of required insert values.
   */
  @Override
  public String[] getInsertColumnNames() {
    return new String[]{"loginId", "Email", "Password"};
  }

  /**
   * Data Inserted update DB.
   * @param values
   * @throws InvalidInsertException
   */
  @Override
  public void insertRow(Object[] values) throws InvalidInsertException {
    if (values == null || values.length != 3) {
      throw new InvalidInsertException("Expected 3 values: loginId, email, password");
    }
    String loginId = (String) values[0];
    String email   = (String) values[1];
    String password= (String) values[2];
    String dateCreated = java.time.LocalDate.now().toString();

    try {
      //create in person
      DBDataProvider.sql(String.format(
              "INSERT:PERSON:loginId:email:password:dateCreated:%s:%s:%s:%s",
              loginId, email, password, dateCreated));

      //insert in player
      DBDataProvider.sql(String.format(
              "INSERT:PLAYER:loginId:isSilenced:isBlocked:watchedBy:%s:0:0:%s",
              loginId,loginId));

    } catch (DBException ex) {
      throw new InvalidInsertException("DB error on insert: " + ex.getMessage());
    }

    data = getRowData();
  }


}
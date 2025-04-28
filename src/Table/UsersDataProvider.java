package Table;

import Exceptions.InvalidUpdateException;

public class UsersDataProvider implements DataProvider {


  static private Object[][] data = {
    {"Apples",        50, "desc1"},
    {"Bananas",       80, "desc2"},
    {"Oranges",       30, "desc3"},
    {"Grapes",        45, "desc4"},
    {"Pears",         55, "desc5"},
    {"Peaches",       65, "desc6"},
    {"Plums",         25, "desc7"},
    {"Strawberries",  75, "desc8"},
    {"Blueberries",   40, "desc9"},
    {"Mangoes",       90, "desc10"},
    {"Pineapples",    35, "desc11"},
    {"Kiwis",         20, "desc12"},
    {"Watermelons",   70, "desc13"},
    {"Cherries",      15, "desc14"},
    {"Papayas",       95, "desc15"}
  };


  public String[] getColumnNames() {
    return new String[]{"Item", "Quantity", "Joe"};
  }
  public Object[][] getRowData() {
    return data;
  }


  /**
   * NOT COMPLETE TALK TO DB
   * @param row
   * @param col
   */
  @Override
  public void updateData(int row, int col, Object value) throws InvalidUpdateException {
    if (row >= data.length || row < 0 || col < 0 || col >= data[0].length) {
      throw new InvalidUpdateException("Out of bounds row or column Update");
    }

    //add check for db and if error from db return false
    if (false) {
      throw new InvalidUpdateException("DB update error invalid type");
    }

    data[row][col] = value;
  }

  /**
   * NOT COMPLETE TALK TO DB
   * @param row
   */
  @Override
  public void deleteRow(int row) {
    if (row < 0 || row >= data.length) {
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
   * NOT COMPLETE TALK TO DB
   * @param values
   * @throws InvalidUpdateException
   */
  @Override
  public void insertRow(Object[] values) throws InvalidUpdateException {
    if (values == null || values.length != data[0].length) {
      throw new InvalidUpdateException("Wrong number of columns for insert");
    }

    Object[][] newData = new Object[data.length + 1][data[0].length];
    for (int i = 0; i < data.length; i++) {
      newData[i] = data[i];
    }
    newData[data.length] = values;
    data = newData;
  }



}
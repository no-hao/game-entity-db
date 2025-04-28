package Table;

import Exceptions.InvalidUpdateException;

public interface DataProvider {
  String[] getColumnNames();
  Object[][] getRowData();


  //make sure talks to db !important
  void updateData(int row, int col, Object value) throws InvalidUpdateException;

  void deleteRow(int row);
  void insertRow(Object[] values) throws InvalidUpdateException;
}


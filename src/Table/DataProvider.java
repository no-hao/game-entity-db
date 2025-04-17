package Table;

public interface DataProvider {
  String[] getColumnNames();
  Object[][] getRowData();
}


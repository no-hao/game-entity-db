package Table;

public class UsersDataProvider implements DataProvider {
  public String[] getColumnNames() {
    return new String[]{"Item", "Quantity"};
  }
  public Object[][] getRowData() {
    return new Object[][] {
            {"Apples", 50},
            {"Bananas", 80},
            {"Oranges", 30}
    };
  }
}
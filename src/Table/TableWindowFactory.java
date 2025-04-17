package Table;

import javax.swing.JFrame;

public class TableWindowFactory {
  public static TableWindow createWindow(TableType type) {
    DataProvider provider = DataProviderFactory.createProvider(type);
    TableWindow window = new TableWindow(provider);
    window.setTitle(type.name());
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    return window;
  }
}

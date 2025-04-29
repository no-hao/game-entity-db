package Table;

import javax.swing.JFrame;

/**
 * Create a @class TableWindow based on
 * Type of display
 *
 * @author Zach Kline
 */
public class TableWindowFactory {

  /**
   * Create new instance of a TableWindow based on display type.
   * @param type
   * @return
   */
  public static TableWindow createWindow(TableType type) {
    DataProvider provider = DataProviderFactory.createProvider(type);

    TableWindow window = new TableWindow(provider, type.name());
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    return window;
  }
}

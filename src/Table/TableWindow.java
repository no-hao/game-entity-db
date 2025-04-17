package Table;

import javax.swing.*;
import java.awt.BorderLayout;

public class TableWindow extends JFrame {
  public TableWindow(DataProvider provider) {
    JTable table = new JTable(provider.getRowData(), provider.getColumnNames());
    getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(null);
  }
}
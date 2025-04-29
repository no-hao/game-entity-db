package Table;

import javax.swing.*;
import java.awt.FlowLayout;

/**
 * Main Menu to access different table in DB
 *
 * @author Zach Kline
 */
public class DisplayRenderer extends JFrame {

  /**
   * Create new Main Menu with buttons for any
   * type of @class TableType.
   */
  public DisplayRenderer() {
    setTitle("Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new FlowLayout());

    for (TableType type : TableType.values()) {
      JButton button = new JButton(type.name());
      button.addActionListener(e -> {
        TableWindow win = TableWindowFactory.createWindow(type);
        win.setVisible(true);
      });
      add(button);
    }

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

}

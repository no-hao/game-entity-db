package Display;

import Table.TableType;
import Table.TableWindow;
import Table.TableWindowFactory;

import javax.swing.*;
import java.awt.FlowLayout;

public class DisplayRenderer extends JFrame {

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

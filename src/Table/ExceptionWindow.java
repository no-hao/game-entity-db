package Table;

import javax.swing.*;
import java.awt.*;

public class ExceptionWindow extends JFrame {

  public ExceptionWindow(String errorMessage) {
    super("Error");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setAlwaysOnTop(true);
    setLayout(new BorderLayout(10, 10));

    //error message
    JLabel lblError = new JLabel(errorMessage, SwingConstants.CENTER);
    lblError.setFont(lblError.getFont().deriveFont(Font.PLAIN, 14f));
    add(lblError, BorderLayout.CENTER);

    //close window
    JButton btnOk = new JButton("OK");
    btnOk.addActionListener(e -> dispose());
    JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pnlButton.add(btnOk);
    add(pnlButton, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(null);
  }
}
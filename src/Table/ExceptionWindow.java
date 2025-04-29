package Table;

import javax.swing.*;
import java.awt.*;

/**
 * A window that shows an error that has occurred.
 * If an error occurred during any operation this window will give
 * a message explaining what happened for the user.
 *
 * @author Zach Kline
 */
public class ExceptionWindow extends JFrame {

  /**
   * Create new exceptionWindow, to show user an error message popup.
   * @param errorMessage What message should be shown
   */
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
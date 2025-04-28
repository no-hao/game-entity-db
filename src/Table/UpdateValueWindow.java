package Table;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class UpdateValueWindow extends JFrame {
  public UpdateValueWindow(Object oldValue, Consumer<Object> onPut) {
    super("Updating: " + oldValue);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setAlwaysOnTop(true); //fixes issue on triple click it going behind
    setLayout(new BorderLayout(10, 10));

    //title text
    JLabel lblUpdate = new JLabel("Update " + oldValue + " here:", SwingConstants.CENTER);
    lblUpdate.setFont(lblUpdate.getFont().deriveFont(Font.PLAIN, 14f));
    add(lblUpdate, BorderLayout.NORTH);

    //new value input
    JTextField txtNewValue = new JTextField(20);
    JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pnlInput.add(txtNewValue);
    add(pnlInput, BorderLayout.CENTER);

    //put button
    JButton btnPut = new JButton("Put");
    btnPut.addActionListener(e -> {
      String input = txtNewValue.getText();
      Object newValue = input;
      onPut.accept(newValue);
      dispose();
    });
    JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pnlButton.add(btnPut);
    add(pnlButton, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(null);
  }
}

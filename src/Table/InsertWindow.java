package Table;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class InsertWindow extends JFrame {
  public InsertWindow(DataProvider provider, Consumer<Object[]> onInsert) {
    super("Insert Row");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    //table
    String[] cols = provider.getColumnNames();
    DefaultTableModel model = new DefaultTableModel(cols, 1) {
      @Override public boolean isCellEditable(int row, int col) { return true; }
    };
    JTable table = new JTable(model);
    table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

    table.setPreferredScrollableViewportSize(
            new Dimension(
                    table.getPreferredSize().width,
                    table.getRowHeight() * model.getRowCount()
                            + table.getTableHeader().getPreferredSize().height
            )
    );

    add(new JScrollPane(table), BorderLayout.CENTER);

    //insert button (disabled until all values filled)
    JButton btnInsert = new JButton("Insert");
    btnInsert.setEnabled(false);
    btnInsert.addActionListener(e -> {
      Object[] values = new Object[model.getColumnCount()];
      for (int i = 0; i < values.length; i++) {
        values[i] = model.getValueAt(0, i);
      }
      onInsert.accept(values);
      dispose();
    });

    //table listener
    model.addTableModelListener(new TableModelListener() {
      @Override public void tableChanged(TableModelEvent e) {
        boolean allFilled = true;
        for (int c = 0; c < model.getColumnCount(); c++) {
          Object v = model.getValueAt(0, c);
          if (v == null || v.toString().trim().isEmpty()) {
            allFilled = false;
            break;
          }
        }
        btnInsert.setEnabled(allFilled);
      }
    });

    //button panel
    JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pnlBtn.add(btnInsert);
    add(pnlBtn, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(null);
  }
}

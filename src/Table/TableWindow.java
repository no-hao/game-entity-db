package Table;

import Exceptions.InvalidUpdateException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class TableWindow extends JFrame {
  private JTable table;
  private DefaultTableModel tableModel;

  private DataProvider provider;

  private static int rowsPerPage = 10;
  private int currentPage = 1;
  private int totalPages;
  private JButton btnPrev;
  private JButton btnNext;
  private JButton btnInsert;
  private JLabel lblPageInfo;

  public TableWindow(DataProvider provider, String title) {
    super(title);
    this.provider = provider;
    this.totalPages = (int) Math.ceil((double) provider.getRowData().length / rowsPerPage);

    setLayout(new BorderLayout());

    //top title text
    JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
    lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
    add(lblTitle, BorderLayout.NORTH);

    //actual data table
    tableModel = new DefaultTableModel(getPageData(), provider.getColumnNames()) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    table = new JTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    //update value (double click)
    table.addMouseListener(new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        int col = table.columnAtPoint(e.getPoint());
        if (row >= 0 && col >= 0 && e.getClickCount() == 2) {
          handleValueUpdate(row, col);
        }
      }
    });

    // Delete key to remove selected row
    InputMap im = table.getInputMap(JComponent.WHEN_FOCUSED);
    ActionMap am = table.getActionMap();
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
    am.put("deleteRow", new AbstractAction() {
      @Override public void actionPerformed(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
          int globalRow = (currentPage - 1) * rowsPerPage + row;
          provider.deleteRow(globalRow);
          totalPages = (int) Math.ceil((double) provider.getRowData().length / rowsPerPage);
          if (currentPage > totalPages) currentPage = totalPages;
          updateTable();
        }
      }
    });

    JScrollPane scrollPane = new JScrollPane(table);

    int headerH = table.getTableHeader().getPreferredSize().height;
    int rowH    = table.getRowHeight();
    int rows    = table.getRowCount();
    int h       = headerH + rowH * rows;
    int w       = table.getPreferredSize().width > 600 ? table.getPreferredSize().width : 600;

    table.setPreferredScrollableViewportSize(new Dimension(w, h));

    //insert row button
    btnInsert = new JButton("Insert");
    btnInsert.addActionListener(e -> {
      new InsertWindow(provider, values -> {
        try {
          provider.insertRow(values);

          //to go to inserted data place
          //may need to remove depending on db sorting
          currentPage = totalPages;
          updateTable();
        } catch (InvalidUpdateException ex) {
          SwingUtilities.invokeLater(() -> {
            ExceptionWindow ew = new ExceptionWindow(ex.getMessage());
            ew.setVisible(true);
          });
        }
        totalPages = (int)Math.ceil((double)provider.getRowData().length / rowsPerPage);
        if (currentPage > totalPages) currentPage = totalPages;
        updateTable();
      }).setVisible(true);
    });


    //combine table and insert
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(scrollPane,    BorderLayout.CENTER);
    add(centerPanel, BorderLayout.CENTER);

    //instructions how to use
    JLabel instructions = new JLabel(
            "<html>"
                    + "Update Data: Double-click value<br>"
                    + "Delete Row: Select row & press Del<br>"
                    + "Insert: Press Insert"
                    + "</html>"
    );
    instructions.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

    //combine table/insert button/instructions
    JPanel instrHolder = new JPanel(new FlowLayout(FlowLayout.LEFT));
    instrHolder.add(instructions);

    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

    JPanel btnHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnHolder.add(btnInsert);
    actionPanel.add(btnHolder);

    actionPanel.add(instrHolder);

    centerPanel.add(actionPanel, BorderLayout.SOUTH);



    //page system
    JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    btnPrev = new JButton("<");
    btnNext = new JButton(">");
    lblPageInfo = new JLabel();

    btnPrev.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentPage > 1) {
          currentPage--;
          updateTable();
        }
      }
    });
    btnNext.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentPage < totalPages) {
          currentPage++;
          updateTable();
        }
      }
    });

    navPanel.add(btnPrev);
    navPanel.add(lblPageInfo);
    navPanel.add(btnNext);

    JPanel bottom = new JPanel(new BorderLayout());
    bottom.add(navPanel,   BorderLayout.SOUTH);
    add(bottom, BorderLayout.SOUTH);

    updateNavButtons();

    pack();
    setLocationRelativeTo(null);
  }

  private Object[][] getPageData() {
    int start = (currentPage - 1) * rowsPerPage;
    int end = Math.min(start + rowsPerPage, provider.getRowData().length);
    int length = end - start;
    Object[][] pageData = new Object[length][provider.getColumnNames().length];
    for (int i = 0; i < length; i++) {
      pageData[i] = provider.getRowData()[start + i];
    }
    return pageData;
  }

  private void updateTable() {
    tableModel.setDataVector(getPageData(), provider.getColumnNames());
    updateNavButtons();
  }

  private void updateNavButtons() {
    lblPageInfo.setText("Page " + currentPage + " of " + totalPages);
    btnPrev.setEnabled(currentPage > 1);
    btnNext.setEnabled(currentPage < totalPages);
  }


  private void handleValueUpdate(int row, int col) {
    if (row < 0 || col < 0) {
      return;
    }

    System.out.println("Selected: " + table.getValueAt(row, col));

    Object oldValue = table.getValueAt(row, col);
    int globalRow = (currentPage - 1) * rowsPerPage + row;

    SwingUtilities.invokeLater(() -> {
      UpdateValueWindow uwv = new UpdateValueWindow(oldValue, newValue -> {
        // update data
        try {
          provider.updateData(globalRow, col, newValue);

          // refresh the visible cell
          tableModel.setValueAt(newValue, row, col);
        } catch (InvalidUpdateException ex) {
          System.out.println(ex);

          SwingUtilities.invokeLater(() -> {
            ExceptionWindow ew = new ExceptionWindow(ex.getMessage());
            ew.setVisible(true);
          });
        }
      });
      uwv.setVisible(true);
    });
  }

}

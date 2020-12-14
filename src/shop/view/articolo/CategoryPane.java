package shop.view.articolo;

import shop.db.ConnectionManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static shop.view.ArticoloPane.*;

public class CategoryPane extends JFrame implements ActionListener {

    private static final int WIDTH = 480;
    private static final int HEIGHT = 420;

    private final JButton btn_add, btn_remove, btn_update;

    JScrollPane scrollPane;
    JPanel interPanel;

    // Creazione della tabella che contiene le categorie
    DefaultTableModel tableModel;
    private final JTable table;
    JTableHeader tableHeader;

    public CategoryPane(String attribute, List<String> elements) {

        setTitle("Gestione ".concat(attribute));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(new Dimension(WIDTH, HEIGHT));
        setSize(size);
        setPreferredSize(size);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/ico.png"))).getImage());

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // Toolbar
        // I pulsanti della Toolbar
        JToolBar toolbar = new JToolBar();

        JButton btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(e -> dispose());

        Font font = new Font("HelveticaNeue", Font.BOLD, 16);

        // pulsante di aggiornamento
        btn_update = new JButton("Aggiorna");
        btn_update.setFont(font);
        btn_update.setForeground(Color.WHITE);
        btn_update.setBorder(new LineBorder(Color.BLACK));
        btn_update.setBackground(new Color(0, 128, 128));
        btn_update.setFocusPainted(false);
        btn_update.addActionListener(this);
        btn_update.setPreferredSize(new Dimension(118, 40));


        // pulsante di aggiornamento
        btn_remove = new JButton("Elimina");
        btn_remove.setFont(font);
        btn_remove.setForeground(Color.WHITE);
        btn_remove.setBorder(new LineBorder(Color.BLACK));
        btn_remove.setBackground(new Color(0, 128, 128));
        btn_remove.setFocusPainted(false);
        btn_remove.addActionListener(this);
        btn_remove.setPreferredSize(new Dimension(118, 40));

        // pulsante di eliminazione
        btn_add = new JButton("+ Nuovo");
        btn_add.setFont(font);
        btn_add.setForeground(Color.WHITE);
        btn_add.setBorder(new LineBorder(Color.BLACK));
        btn_add.setBackground(new Color(0, 128, 128));
        btn_add.setFocusPainted(false);
        btn_add.setPreferredSize(new Dimension(118, 40));
        btn_add.addActionListener(this);
        // Pannello interno
        interPanel = new JPanel();
        interPanel.setBackground(new Color(236, 240, 241 ));
        interPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        String[] header = {"Categorie"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                return returnComp;
            }

        };

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("HelveticaNeue", Font.BOLD, 16));
        table.setFont(new Font("HelveticaNeue", Font.BOLD, 16));
        table.setRowHeight(25);

        IntStream.range(0, elements.size()).forEach(i -> tableModel.addRow(new String[]{elements.get(i)}));

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(WIDTH - 115, HEIGHT - 180));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        JPanel wrapperPane = new JPanel();
        wrapperPane.setBackground(interPanel.getBackground());

        wrapperPane.add(btn_add);
        wrapperPane.add(btn_remove);
        wrapperPane.add(btn_update);

        interPanel.setLayout(new FlowLayout());
        interPanel.add(scrollPane);
        interPanel.add(wrapperPane);

        toolbar.setFloatable(false);
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(interPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_add) {
            tableModel.addRow(new Object[]{});
            validate();
            repaint();
        } else if (e.getSource() == btn_update) {
            ArrayList<String> categories = new ArrayList<>();
            try{
                if (table.isEditing())
                    table.getCellEditor().stopCellEditing();
                IntStream.range(0, tableModel.getRowCount()).filter(i -> tableModel.getValueAt(i, 0) == null).forEach(i -> tableModel.removeRow(i));
                if(tableModel.getRowCount()> 0){
                    categories = IntStream.range(0, tableModel.getRowCount()).filter(i -> !tableModel.getValueAt(i, 0).toString().isEmpty()).mapToObj(i -> tableModel.getValueAt(i, 0).toString()).collect(Collectors.toCollection(ArrayList::new));
                }

            }catch (ArrayIndexOutOfBoundsException ignored)  {}

            try {
                Connection con = (new ConnectionManager()).getConnection();
                PreparedStatement ps = con.prepareStatement("DELETE FROM CATEGORIA_PRODOTTO");
                ps.execute();
                Statement stmt = con.createStatement();
                // CATEGORIA DI PRODOTTI
                for (String s : categories) {
                    stmt.executeUpdate(String.format("INSERT INTO CATEGORIA_PRODOTTO VALUES ('%s')", s));
                }
                stmt.close();
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(loadProductAttribute(Attribute.CATEGORIA.getAttribute()).toArray(new String[0]));
            jcbCategoria.setModel(model);
            jcbCategoria.validate();
            jcbCategoria.repaint();
        } else if (e.getSource() == btn_remove) {

            while (table.getSelectedRow() >= 0) {
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(String.format("DELETE FROM CATEGORIA_PRODOTTO WHERE CATEGORIA='%s'", table.getValueAt( table.getSelectedRow(), 0)));
                    stmt.close();
                    con.close();

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                tableModel.removeRow(table.getSelectedRow());
            }

        }
    }
}

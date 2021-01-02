package shop.view;

import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.model.Carico;
import shop.model.Fornitore;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import shop.view.carico.InfoCaricoPane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import static shop.utils.DesktopRender.FONT_FAMILY;
import static shop.view.carico.controller.CaricoDbOperation.deleteCaricoFromDB;
import static shop.view.carico.controller.CaricoDbOperation.loadCaricoFromDB;
import static shop.view.fornitore.controller.FornitoreDbOperation.*;

public class CaricoPane extends AContainer implements ActionListener {

    public JButton btn_prima;

    // pannello interno
    private JPanel internPane, wrapperPane, clientPane;
    private RoundedPanel searchPane;
    protected JTextField filterField;
    private static final Color JTF_COLOR = new Color(46, 134, 193);

    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    protected JButton  btn_add, btn_update, btn_remove;

    // Pulsante di carica articolo
    private Font font;

    public CaricoPane() {
        initPanel();
    }

    public void initPanel() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // I pulsanti della Toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));

        btn_prima = new JButton();
        btn_prima.setIcon(new ImageIcon(this.getClass().getResource("/images/back.png")));
        toolbar.add(btn_prima);
        btn_prima.setFocusPainted(false);
        btn_prima.addActionListener(this);
        btn_prima.setToolTipText("Prima");
        toolbar.addSeparator();
        btn_prima.addActionListener(this);

        // I pulsanti delle funzionalita'
        internPane = new JPanel();
        wrapperPane = new JPanel();
        clientPane = new JPanel();
        searchPane = new RoundedPanel();
        build();
        buildClientArea();
        toolbar.setFloatable(false);
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }


    public void build() {
        internPane.setBounds(90, 110, 1200, 675);
        wrapperPane.setPreferredSize(new Dimension(1200, 675));
        internPane.setBackground(container.getBackground());
        internPane.setLayout(new BorderLayout());
        internPane.add(wrapperPane, BorderLayout.CENTER);
        container.add(internPane);
    }

    private void buildClientArea() {

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);
        wrapperPane.setLayout(new FlowLayout());

        clientPane.setBackground(wrapperPane.getBackground());
        clientPane.setPreferredSize(new Dimension(1150, 450));
        buildArticleDetails();
        searchPane.setPreferredSize(new Dimension(1150, 80));
        searchPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 10, 3, 10);
        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        filterField.setBackground(JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));


        btn_add = new JButton(DesktopRender.formatButton("+ New"));
        btn_update = new JButton(DesktopRender.formatButton("Update"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove"));

        formatButton(btn_add);
        formatButton(btn_update);
        formatButton(btn_remove);

        searchPane.add(lbl, c);
        searchPane.add(filterField, c);
        searchPane.add(btn_add, c);
        searchPane.add(btn_update, c);
        searchPane.add(btn_remove, c);

        wrapperPane.add(searchPane, BorderLayout.NORTH);
        wrapperPane.add(clientPane, BorderLayout.CENTER);

        btn_remove.addActionListener(e -> deleteCaricoFromDB());
    }

    void buildArticleDetails() {
        String[] header = {"ID", "Codice Prodotto","Descrizione", "Data Carico", "Quantita'", "Fornitore", "Note"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int i=0;
        for (Carico carico : loadCaricoFromDB()) {
            tableModel.addRow(new String[]{String.valueOf(++i),carico.getCodice(), carico.getDescrizione(), (new SimpleDateFormat("dd/MM/yyyy")).format(carico.getDatacarico()),String.valueOf(carico.getQuantita()), carico.getFornitore(), carico.getNote()});
        }

        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                int rendererWidth = returnComp.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    returnComp.setBackground((row % 2 == 0 ? new Color(88, 214, 141) : Color.WHITE));
                }
                return returnComp;
            }
        };

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        resizeColumnWidth(table);

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(20);

        RendererHighlighted renderer = new RendererHighlighted(filterField);
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(1150, 420));
        table.setPreferredSize(new Dimension(1130, 420));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        clientPane.add(scrollPane, BorderLayout.CENTER);
    }


    public void resizeColumnWidth(JTable table) {


        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth() + 120;
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                //  We've exceeded the maximum width, no need to check other rows

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setResizable(false);



/*
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 40; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
            table.getColumnModel().getColumn(column).setResizable(false);
        }
        */
    }


    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.BLACK));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
    }


    /*
    public Fornitore getSelectedFornitore() {
        Fornitore fornitore = new Fornitore();
        if (table.getSelectedRow() >= 0) {
            int index = table.getSelectedRow();
            fornitore.setNome(String.valueOf(table.getValueAt(index, 1)));
            fornitore.setCognome(String.valueOf(table.getValueAt(index, 2)));
            fornitore.setIndirizzo(String.valueOf(table.getValueAt(index, 3)));
            fornitore.setComune(String.valueOf(table.getValueAt(index, 4)));
            fornitore.setPiva(String.valueOf(table.getValueAt(index, 5)));
            fornitore.setMail(String.valueOf(table.getValueAt(index, 6)));
            fornitore.setTelefono(String.valueOf(table.getValueAt(index, 7)));
            fornitore.setFax(String.valueOf(table.getValueAt(index, 8)));
            fornitore.setWebsite(String.valueOf(table.getValueAt(index, 9)));
            fornitore.setNote(String.valueOf(table.getValueAt(index, 10)));
        }
        return fornitore;
    }

     */


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new RilevazionePane().getPanel());
            container.repaint();
        } else if (e.getSource() == btn_add) {
            new InfoCaricoPane();
        } else if (e.getSource() == btn_update) {
            if (table.getSelectedRow() == -1) {
                //showMessageDialog(null, "Selezionare il fornitore", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } /*else
                new FornitorePaneUpdate(getSelectedFornitore()); */
        }
    }
}

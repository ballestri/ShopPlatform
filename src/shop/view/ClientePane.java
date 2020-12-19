package shop.view;

import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import shop.view.Fornitore.FornitorePane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static shop.utils.DesktopRender.FONT_FAMILY;

public class ClientePane extends AContainer implements ActionListener {

    public JButton btn_prima, btn_close;


    // pannello interno
    private JPanel internPane, clientWrapper, wrapperPane, clientPane;
    private RoundedPanel searchPane;
    protected JTextField filterField;
    private static final Color JTF_COLOR = new Color(46, 134, 193);


    public static DefaultTableModel tableModel;
    JTableHeader tableHeader;
    public static JTable table;
    JScrollPane scrollPane;

    protected JButton btn_add,btn_update,btn_remove;


    // Pulsante di carica articolo
    private Font font;

    public ClientePane() {
        initPanel();
    }

    public void initPanel() {

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // I pulsanti della Toolbar
        JToolBar toolbar = new JToolBar();

        btn_prima = new JButton();
        btn_prima.setIcon(new ImageIcon(this.getClass().getResource("/images/prima.png")));
        toolbar.add(btn_prima);
        btn_prima.setFocusPainted(false);
        btn_prima.addActionListener(this);
        btn_prima.setToolTipText("Prima");
        toolbar.addSeparator();
        btn_prima.addActionListener(this);

        btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(evt -> System.exit(0));
        // Il Font dei pulsanti
        font = new Font(FONT_FAMILY, Font.BOLD, 16);

        // I pulsanti delle funzionalita'
        internPane = new JPanel();
        clientWrapper = new JPanel();
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

        internPane.setBounds(20, 110, 1400, 675);
        clientWrapper.setPreferredSize(new Dimension(200, 675));
        wrapperPane.setPreferredSize(new Dimension(1200, 675));
        internPane.setBackground(container.getBackground());
        clientWrapper.setBackground(container.getBackground());
        internPane.setLayout(new BorderLayout());
        internPane.add(clientWrapper, BorderLayout.WEST);
        internPane.add(wrapperPane, BorderLayout.CENTER);
        container.add(internPane);
    }

    private void buildClientArea() {

        clientWrapper.setLayout(new FlowLayout());
        btn_add = new JButton(DesktopRender.formatButton("+ New fornitore"));
        btn_update = new JButton(DesktopRender.formatButton("Update fornitore"));
        btn_remove = new JButton(DesktopRender.formatButton("Remove fornitore"));

        formatButton(btn_add);
        formatButton(btn_update);
        formatButton(btn_remove);
        clientWrapper.add(btn_add);
        clientWrapper.add(btn_update);
        clientWrapper.add(btn_remove);

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);
        wrapperPane.setLayout( new FlowLayout());

        clientPane.setBackground(wrapperPane.getBackground());
        clientPane.setPreferredSize(new Dimension(1150, 420));
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

       // filterField.setSize(new Dimension(1000,50));
        searchPane.add(lbl,c);
        searchPane.add(filterField,c);
        wrapperPane.add(searchPane, BorderLayout.NORTH);
        wrapperPane.add(clientPane, BorderLayout.CENTER);

    }


    void buildArticleDetails() {
        String[] header = {"ID", "Nome", "Cognome|RS", "Indirizzo", "Comune", "PIVA", "Mail", "Telefono", "Fax", "Sito web", "Note"};
        tableModel = new DefaultTableModel(new Object[][]{}, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

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

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(32);
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


        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());

        clientPane.add(scrollPane, BorderLayout.CENTER);

    }

    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.WHITE));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 50));
    }


    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new AnagraficaPane().getPanel());
            container.repaint();
        } else if (e.getSource() == btn_add){
            new FornitorePane();
        }

    }

}

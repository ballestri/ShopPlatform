package shop.view;

import shop.controller.article.RendererHighlighted;
import shop.controller.article.RowFilterUtil;
import shop.db.ConnectionManager;
import shop.model.Articolo;
import shop.utils.ComboRenderer;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;
import shop.view.articolo.CategoryPane;
import shop.view.articolo.PositionPane;
import shop.view.articolo.UnitPane;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;


public class ArticoloPane extends AContainer implements ActionListener {

    private static final Color JTF_COLOR = new Color(46, 134, 193);
    private static final String FONT_FAMILY = "HelveticaNeue";
    public static JComboBox<String> jcbCategoria, jcbUnita, jcbPosizione;
    protected Font font;
    // la JToolbar
    protected JToolBar toolbar;
    protected JButton btn_prima;
    protected JButton btn_close;
    // Informazioni sull'articolo
    protected JLabel lblCodice, lblDescrizione, lblCategoria, lblPosizione, lblUnita, lblFornitore, lblPrezzo, lblScorta, lblProvenienza;
    protected JTextField jtfCodice, jtfDescrizione, jtfFornitore, jtfProvenienza;

    protected JTextField filterField;

    protected JFormattedTextField jtfCurrency;
    protected JSpinner jspScorta;
    // Pannello delle funzionalita'
    JPanel internPanel, wrapperPane;
    RoundedPanel articlePane, informationPane;
    JButton btn_list_categoria, btn_list_posizione, btn_list_unita, btn_modifica, btn_elimina, btn_salva;
    JScrollPane scrollPane;
    RoundedPanel actionPaneWrapper;

    // Creazione della tabella che contiene le categorie
    DefaultTableModel tableModel;
    JTableHeader tableHeader;
    JTable table;

    // Gestione attributi del prodotto
    public enum Attribute {
        CATEGORIA("CATEGORIA"), UNITA("UNITA"), POSIZIONE("POSIZIONE");

        String attribute;

        Attribute(String s) {
            attribute = s;
        }

        public String getAttribute() {
            return attribute;
        }
    }

    // Informazioni sull'articolo

    public ArticoloPane() {
        initPanel();
    }

    public void initPanel() {

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // Toolbar
        // I pulsanti della Toolbar
        toolbar = new JToolBar();

        btn_prima = new JButton();
        btn_close = new JButton();

        // Il Font dei pulsanti
        font = new Font(FONT_FAMILY, Font.BOLD, 16);

        // I pulsanti delle funzionalita'
        wrapperPane = new JPanel();
        informationPane = new RoundedPanel();
        internPanel = new JPanel();
        articlePane = new RoundedPanel();

        btn_list_categoria = new JButton(DesktopRender.formatButton("Lista categorie"));
        btn_list_posizione = new JButton(DesktopRender.formatButton("Lista posizioni"));
        btn_list_unita = new JButton(DesktopRender.formatButton("Lista unita'"));

        build();
        buildProduct();
        //buildFonctionality();
        buildArticleDetails();

        toolbar.setFloatable(false);
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }

    public void build() {

        internPanel.setPreferredSize(new Dimension(1200, 675));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        internPanel.setBorder(border);
        internPanel.setBackground(new Color(39, 55, 70));
        articlePane.setPreferredSize(new Dimension(1150, 140));
        // articlePane.setBackground(new Color(128, 0, 128));
        //articlePane.setBackground(new Color(236, 240, 241  ));
        informationPane.setPreferredSize(new Dimension(1200, 70));

        // pannello delle azioni
        actionPaneWrapper = new RoundedPanel();
        actionPaneWrapper.setPreferredSize(new Dimension(1150, 70));
        actionPaneWrapper.setBackground(articlePane.getBackground());

        wrapperPane.setBounds(180, 80, 1200, 750);
        wrapperPane.setBackground(container.getBackground());

        // I pulsanti della JToolbar
        // Prima
        btn_prima.setIcon(new ImageIcon(this.getClass().getResource("/images/prima.png")));
        toolbar.add(btn_prima);
        btn_prima.setFocusPainted(false);
        btn_prima.addActionListener(this);
        btn_prima.setToolTipText("Prima");
        toolbar.addSeparator();

        // Close
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(evt -> System.exit(0));


        informationPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        formatButton(btn_list_categoria);
        formatButton(btn_list_posizione);
        formatButton(btn_list_unita);

        informationPane.add(btn_list_categoria);
        informationPane.add(btn_list_posizione);
        informationPane.add(btn_list_unita);

        wrapperPane.setLayout(new BorderLayout());
        informationPane.setBackground(articlePane.getBackground());
        wrapperPane.add(informationPane, BorderLayout.NORTH);

        wrapperPane.add(internPanel, BorderLayout.SOUTH);
        container.add(wrapperPane);
    }

    void buildProduct() {

        font = new Font(FONT_FAMILY, Font.BOLD, 16);

        lblCodice = new JLabel("Codice");
        //lblCodice.setForeground(Color.WHITE);
        lblCodice.setFont(font);

        // Testo
        jtfCodice = new JTextField(16);
        jtfCodice.setCaretColor(Color.BLACK);
        jtfCodice.setBackground(JTF_COLOR);
        jtfCodice.setBorder(new LineBorder(Color.BLACK));

        jtfCodice.setFont(font);

        lblDescrizione = new JLabel("Descrizione");
        //lblDescrizione.setForeground(Color.WHITE);
        lblDescrizione.setFont(font);

        // Testo
        jtfDescrizione = new JTextField(16);
        jtfDescrizione.setCaretColor(Color.BLACK);
        jtfDescrizione.setBackground(JTF_COLOR);
        jtfDescrizione.setFont(font);
        jtfDescrizione.setBorder(new LineBorder(Color.BLACK));

        lblCategoria = new JLabel("Categoria");
        //lblCategoria.setForeground(Color.WHITE);
        lblCategoria.setFont(font);


        jcbCategoria = new JComboBox<>(loadProductAttribute(Attribute.CATEGORIA.getAttribute()).toArray(new String[0]));
        jcbCategoria.setBorder(new LineBorder(Color.BLACK));
        jcbCategoria.setRenderer(new ComboRenderer());
        jcbCategoria.setFont(font);
        jcbCategoria.addActionListener(this);
        //jcbCategoria.revalidate();
        //jcbCategoria.repaint();
        // caricamento dei dati nel db

        lblPosizione = new JLabel("Posizione");
        //lblPosizione.setForeground(Color.WHITE);
        lblPosizione.setFont(font);
        // Testo

        jcbPosizione = new JComboBox<>(loadProductAttribute(Attribute.POSIZIONE.getAttribute()).toArray(new String[0]));
        jcbPosizione.setBorder(new LineBorder(Color.BLACK));
        jcbPosizione.setRenderer(new ComboRenderer());
        jcbPosizione.setFont(font);
        jcbPosizione.addActionListener(this);

        lblUnita = new JLabel("Unita'");
        //lblUnita.setForeground(Color.WHITE);
        lblUnita.setFont(font);

        //jcbUnita = new JComboBox(new String[]{"Kilogrammi", "Metri", "Pacchi"});
        jcbUnita = new JComboBox<>(loadProductAttribute(Attribute.UNITA.getAttribute()).toArray(new String[0]));
        jcbUnita.setBorder(new LineBorder(Color.BLACK));
        jcbUnita.setRenderer(new ComboRenderer());
        jcbUnita.setFont(font);
        jcbUnita.addActionListener(this);


        lblFornitore = new JLabel("Fornitore");
        //lblFornitore.setForeground(Color.WHITE);
        lblFornitore.setFont(font);
        // Testo
        jtfFornitore = new JTextField(16);
        jtfFornitore.setCaretColor(new Color(255, 255, 255));
        jtfFornitore.setBorder(new LineBorder(Color.BLACK));
        jtfFornitore.setBackground(JTF_COLOR);
        jtfFornitore.setFont(font);

        lblPrezzo = new JLabel("Prezzo");
        //lblPrezzo.setForeground(Color.WHITE);
        lblPrezzo.setFont(font);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        jtfCurrency = new JFormattedTextField(currencyFormat);
        jtfCurrency.setBorder(new EmptyBorder(0, 5, 0, 5));
        jtfCurrency.setBorder(new LineBorder(Color.BLACK));
        jtfCurrency.setBackground(JTF_COLOR);
        jtfCurrency.setFont(font);
        jtfCurrency.setHorizontalAlignment(SwingConstants.RIGHT);
        jtfCurrency.setPreferredSize(new Dimension(150, 25));
        jtfCurrency.setValue(0);
        jtfCurrency.addPropertyChangeListener(evt -> formattercurrency());


        lblScorta = new JLabel("Scorta");
        //lblScorta.setForeground(Color.WHITE);
        lblScorta.setFont(font);

        jspScorta = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        jspScorta.setBorder(new EmptyBorder(0, 5, 0, 0));
        jspScorta.setBorder(new LineBorder(Color.BLACK));
        jspScorta.setPreferredSize(new Dimension(120, 25));
        jspScorta.setFont(font);
        JTextField jtfScorta = ((JSpinner.DefaultEditor) jspScorta.getEditor()).getTextField();
        jtfScorta.setBackground(JTF_COLOR);
        jtfScorta.setCaretColor(new Color(255, 255, 255));


        lblProvenienza = new JLabel("Provenienza");
        //lblProvenienza.setForeground(Color.WHITE);
        lblProvenienza.setFont(font);
        // Testo
        jtfProvenienza = new JTextField(16);
        jtfProvenienza.setCaretColor(new Color(255, 255, 255));
        // jtfProvenienza.setBorder(new EmptyBorder(0, 5, 0, 0));
        jtfProvenienza.setBackground(JTF_COLOR);
        jtfProvenienza.setFont(font);
        jtfProvenienza.setBorder(new LineBorder(Color.BLACK));


        // aggiunto nel pannelli interno
        articlePane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblCodice, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblPosizione, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblPrezzo, gc);

        // second column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfCodice, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbPosizione, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        //articlePane.add(jtfPrezzo, gc);
        articlePane.add(jtfCurrency, gc);


        // Third column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 2;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblDescrizione, gc);

        gc.gridx = 2;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblUnita, gc);

        gc.gridx = 2;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblScorta, gc);

        // Four column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfDescrizione, gc);

        gc.gridx = 3;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbUnita, gc);

        gc.gridx = 3;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jspScorta, gc);

        // Five column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 4;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblCategoria, gc);

        gc.gridx = 4;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblFornitore, gc);

        gc.gridx = 4;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(lblProvenienza, gc);

        // Six column
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 5;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jcbCategoria, gc);

        gc.gridx = 5;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfFornitore, gc);

        gc.gridx = 5;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 5, 10);
        articlePane.add(jtfProvenienza, gc);

        internPanel.add(articlePane, BorderLayout.NORTH);
        internPanel.add(actionPaneWrapper);

    }

    void buildFonctionality() {

        actionPaneWrapper.setLayout(new FlowLayout());
        JPanel searchPane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 0, 3, 3);
        //searchPane.setBorder(BorderFactory.createRaisedBevelBorder());
        //searchPane.setPreferredSize(new Dimension(320, 60));
        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(font);

        //filterField= new JTextField(16);
        filterField.setBackground(JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));
        searchPane.add(lbl, c);
        searchPane.add(filterField, c);
        //actionPaneWrapper.setPreferredSize(new Dimension(1150, 70));

        actionPaneWrapper.add(searchPane, BorderLayout.WEST);

        btn_salva = new JButton(DesktopRender.formatButton("Salva"));
        btn_modifica = new JButton(DesktopRender.formatButton("Aggiorna"));
        btn_elimina = new JButton(DesktopRender.formatButton("Elimina"));

        JPanel actionPane = new JPanel();
        //actionPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        actionPane.setLayout(new GridBagLayout());
        GridBagConstraints ca = new GridBagConstraints();
        ca.insets = new Insets(5, 10, 3, 10);
        formatButton(btn_salva);
        formatButton(btn_modifica);
        formatButton(btn_elimina);

        actionPane.add(btn_salva, ca);
        actionPane.add(btn_modifica, ca);
        actionPane.add(btn_elimina, ca);

        //actionPaneWrapper.add(searchPane, BorderLayout.WEST);
        actionPaneWrapper.add(actionPane, BorderLayout.EAST);

        // Gestione degli eventi
        btn_salva.addActionListener(this);
        btn_modifica.addActionListener(this);
        btn_elimina.addActionListener(this);
    }

    void buildArticleDetails() {
        String[] header = {"Codice", "Descrizione", "Categoria", "Posizione", "Unita'", "Fornitore", "Prezzo", "Scorta", "Provenienza"};
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

        loadArticleFromDB().forEach(article -> tableModel.addRow(new String[]{article.getCodice(), article.getDescrizione(), article.getCategoria(), article.getPosizione(), article.getUnita(), article.getFornitore(), String.valueOf(article.getPrezzo()).concat(" €"), String.valueOf(article.getScorta()), article.getProvenienza()}));

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        //DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getDefaultRenderer(Object.class);
        filterField = RowFilterUtil.createRowFilter(table);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        table.setDefaultRenderer(Object.class, renderer);
        //internPanel.add(filterField);

        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buildFonctionality();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                Articolo articolo = new Articolo();
                articolo.setCodice(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 0)));
                articolo.setDescrizione(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 1)));
                articolo.setCategoria(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 2)));
                articolo.setPosizione(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 3)));
                articolo.setUnita(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 4)));
                articolo.setFornitore(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 5)));

                articolo.setPrezzo(Double.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString().replace("€", "").replace(",", ".")));
                articolo.setScorta(Integer.valueOf(tableModel.getValueAt(table.getSelectedRow(), 7).toString()));
                articolo.setProvenienza(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 8)));

                // set dei valori dell'articolo per l'aggiornamento
                jtfCodice.setText(articolo.getCodice());
                jtfDescrizione.setText(articolo.getDescrizione());
                jcbCategoria.setSelectedItem(articolo.getCategoria());
                jcbPosizione.setSelectedItem(articolo.getPosizione());
                jcbUnita.setSelectedItem(articolo.getUnita());
                jtfFornitore.setText(articolo.getFornitore());
                jtfCurrency.setText("€ ".concat(String.valueOf(articolo.getPrezzo())).replace(".", ","));
                jspScorta.setValue(articolo.getScorta());
                jtfProvenienza.setText(articolo.getProvenienza());

            }
        });


                scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1150, 420));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        scrollPane.getViewport().setBackground(table.getBackground());


        internPanel.add(scrollPane);
    }

    protected void formattercurrency() {
        jtfCurrency.setText(jtfCurrency.getText());
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

    public static ArrayList<String> loadProductAttribute(String attribute) {

        ArrayList<String> lista = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT* FROM %s_PRODOTTO", attribute));
            while (rs.next())
                lista.add(rs.getString(attribute));
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new AnagraficaPane().getPanel());
            container.repaint();
        } else if (e.getSource() == btn_salva) {
            insertArticleToDB();
            // inizializzazione del pannello
            initArticlePane();
        } else if (e.getSource() == btn_list_categoria) {
            new CategoryPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbCategoria.getItemCount()).mapToObj(i -> jcbCategoria.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_list_unita) {
            new UnitPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbUnita.getItemCount()).mapToObj(i -> jcbUnita.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_list_posizione) {
            new PositionPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbPosizione.getItemCount()).mapToObj(i -> jcbPosizione.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_elimina) {
            while (table.getSelectedRow() >= 0) {
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(String.format("DELETE FROM ARTICOLO WHERE CODICE='%s'", table.getValueAt(table.getSelectedRow(), 0)));
                    stmt.close();
                    con.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                tableModel.removeRow(table.getSelectedRow());
            }
        } else if (e.getSource() == btn_modifica) {
            initArticlePane();
            /*
            if (table.getSelectedRow() >= 0) {
                Articolo articolo = new Articolo();
                articolo.setCodice(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 0)));
                articolo.setDescrizione(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 1)));
                articolo.setCategoria(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 2)));
                articolo.setPosizione(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 3)));
                articolo.setUnita(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 4)));
                articolo.setFornitore(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 5)));
                //articolo.setPrezzo(Double.valueOf(table.getValueAt(table.getSelectedRow(), 6).toString()));
                articolo.setScorta(Integer.valueOf(tableModel.getValueAt(table.getSelectedRow(), 7).toString()));
                articolo.setProvenienza(String.valueOf(tableModel.getValueAt(table.getSelectedRow(), 8)));

                // set dei valori dell'articolo per l'aggiornamento
                jtfCodice.setText(articolo.getCodice());
                jtfDescrizione.setText(articolo.getDescrizione());
                jcbCategoria.setSelectedItem(articolo.getCategoria());
                jcbPosizione.setSelectedItem(articolo.getPosizione());
                jcbUnita.setSelectedItem(articolo.getUnita());
                /*
                jcbCategoria.setSelectedIndex(0);
                jcbPosizione.setSelectedIndex(0);
                jcbUnita.setSelectedIndex(0);


                jtfFornitore.setText(articolo.getFornitore());
                //jtfCurrency.setText(String.valueOf(articolo.getPrezzo()));
                jspScorta.setValue(articolo.getScorta());
                jtfProvenienza.setText(articolo.getProvenienza());

            }
            */
        }
    }


    public void insertArticleToDB() {
        try {
            Connection con = (new ConnectionManager()).getConnection();
            PreparedStatement preparedStmt = con.prepareStatement("INSERT INTO ARTICOLO VALUES (?, ?, ?, ?, ?,?,?,?,?)");
            preparedStmt.setString(1, jtfCodice.getText());
            preparedStmt.setString(2, jtfDescrizione.getText());
            preparedStmt.setString(3, String.valueOf(jcbCategoria.getSelectedItem()));
            preparedStmt.setString(4, String.valueOf(jcbPosizione.getSelectedItem()));
            preparedStmt.setString(5, String.valueOf(jcbUnita.getSelectedItem()));
            preparedStmt.setString(6, jtfFornitore.getText());
            preparedStmt.setDouble(7, Double.parseDouble(jtfCurrency.getText().replace("€", "").replace(",", ".").trim()));
            preparedStmt.setInt(8, Integer.parseInt(jspScorta.getValue().toString()));
            preparedStmt.setString(9, jtfProvenienza.getText());
            preparedStmt.execute();

            // Insert to table
            tableModel.addRow(new String[]{jtfCodice.getText(), jtfDescrizione.getText(), String.valueOf(jcbCategoria.getSelectedItem()),
                    String.valueOf(jcbPosizione.getSelectedItem()), String.valueOf(jcbUnita.getSelectedItem()), jtfFornitore.getText()
                    , jtfCurrency.getText().replace(",", ".").trim(), jspScorta.getValue().toString(), jtfProvenienza.getText()});

            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // inizializzazione del campi del pannello degli articoli
    void initArticlePane() {
        jtfCodice.setText(null);
        jtfDescrizione.setText(null);
        jcbCategoria.setSelectedIndex(0);
        jcbPosizione.setSelectedIndex(0);
        jcbUnita.setSelectedIndex(0);
        jtfFornitore.setText(null);
        jtfCurrency.setText(null);
        jspScorta.setValue(0);
        jtfProvenienza.setText(null);
    }

    public ArrayList<Articolo> loadArticleFromDB() {

        ArrayList<Articolo> list_articles = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT* FROM ARTICOLO");

            while (rs.next()) {
                Articolo article = new Articolo();
                article.setCodice(rs.getString("CODICE"));
                article.setDescrizione(rs.getString("DESCRIZIONE"));
                article.setCategoria(rs.getString("CATEGORIA"));
                article.setPosizione(rs.getString("POSIZIONE"));
                article.setUnita(rs.getString("UNITA"));
                article.setFornitore(rs.getString("FORNITORE"));
                article.setPrezzo(Double.valueOf(rs.getString("PREZZO")));
                article.setScorta(Integer.valueOf(rs.getString("SCORTA")));
                article.setProvenienza(rs.getString("PROVENIENZA"));
                list_articles.add(article);
            }
            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list_articles;
    }


    String formatTitleFieldPane(String field) {
        return field.replace("<html><center>", "").replace("</center></html>", "");
    }

}

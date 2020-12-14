package shop.view;

import shop.controller.article.*;
import shop.db.ConnectionManager;
import shop.model.*;
import shop.utils.*;
import shop.view.articolo.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.utils.DesktopRender.FONT_FAMILY;


public class ArticoloPane extends AContainer implements ActionListener {

    private static final Color JTF_COLOR = new Color(46, 134, 193);
    //private static final String FONT_FAMILY = "HelveticaNeue";
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
    JButton btn_list_categoria, btn_list_posizione, btn_list_unita, btn_nuovo, btn_aggiorna, btn_elimina, btn_salva;
    JScrollPane scrollPane;
    RoundedPanel actionPaneWrapper;

    // Creazione della tabella che contiene le categorie
    DefaultTableModel tableModel;
    JTableHeader tableHeader;
    JTable table;

    public ArticoloPane() {
        initPanel();
    }

    // Informazioni sull'articolo

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
        lblCategoria.setFont(font);

        jcbCategoria = new JComboBox<>(loadProductAttribute(Attribute.CATEGORIA.getAttribute()).toArray(new String[0]));
        jcbCategoria.setBorder(new LineBorder(Color.BLACK));
        jcbCategoria.setRenderer(new ComboRenderer());
        jcbCategoria.setFont(font);
        jcbCategoria.addActionListener(this);

        lblPosizione = new JLabel("Posizione");
        lblPosizione.setFont(font);
        // Testo

        jcbPosizione = new JComboBox<>(loadProductAttribute(Attribute.POSIZIONE.getAttribute()).toArray(new String[0]));
        jcbPosizione.setBorder(new LineBorder(Color.BLACK));
        jcbPosizione.setRenderer(new ComboRenderer());
        jcbPosizione.setFont(font);
        jcbPosizione.addActionListener(this);

        lblUnita = new JLabel("Unita'");
        lblUnita.setFont(font);

        //jcbUnita = new JComboBox(new String[]{"Kilogrammi", "Metri", "Pacchi"});
        jcbUnita = new JComboBox<>(loadProductAttribute(Attribute.UNITA.getAttribute()).toArray(new String[0]));
        jcbUnita.setBorder(new LineBorder(Color.BLACK));
        jcbUnita.setRenderer(new ComboRenderer());
        jcbUnita.setFont(font);
        jcbUnita.addActionListener(this);


        lblFornitore = new JLabel("Fornitore");
        lblFornitore.setFont(font);
        // Testo
        jtfFornitore = new JTextField(16);
        jtfFornitore.setCaretColor(new Color(255, 255, 255));
        jtfFornitore.setBorder(new LineBorder(Color.BLACK));
        jtfFornitore.setBackground(JTF_COLOR);
        jtfFornitore.setFont(font);

        lblPrezzo = new JLabel("Prezzo");
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

        lblScorta = new JLabel("Scorta");
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
        lblProvenienza.setFont(font);
        // Testo
        jtfProvenienza = new JTextField(16);
        jtfProvenienza.setCaretColor(new Color(255, 255, 255));
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
        c.insets = new Insets(6, 13, 3, 10);
        JLabel lbl = new JLabel("Ricerca");
        lbl.setFont(font);

        filterField.setBackground(JTF_COLOR);
        filterField.setFont(font);
        filterField.setBorder(new LineBorder(Color.BLACK));
        searchPane.add(lbl, c);
        searchPane.add(filterField, c);

        actionPaneWrapper.add(searchPane, BorderLayout.WEST);

        btn_nuovo = new JButton(DesktopRender.formatButton("+ Nuovo"));
        btn_salva = new JButton(DesktopRender.formatButton("Salva"));
        btn_aggiorna = new JButton(DesktopRender.formatButton("Aggiorna"));
        btn_elimina = new JButton(DesktopRender.formatButton("Elimina"));

        JPanel actionPane = new JPanel();
        actionPane.setLayout(new GridBagLayout());
        GridBagConstraints ca = new GridBagConstraints();
        ca.insets = new Insets(5, 10, 3, 10);
        formatButton(btn_salva);
        formatButton(btn_aggiorna);
        formatButton(btn_elimina);
        formatButton(btn_nuovo);

        actionPane.add(btn_salva, ca);
        actionPane.add(btn_aggiorna, ca);
        actionPane.add(btn_elimina, ca);
        actionPane.add(btn_nuovo, ca);

        // Gestione degli eventi
        btn_salva.addActionListener(e -> insertArticleToDB());
        btn_aggiorna.addActionListener(e ->updateArticleToDB() );
        btn_elimina.addActionListener(e->deleteArticleFromDB());
        btn_nuovo.addActionListener(e -> initArticlePane());

        actionPaneWrapper.add(actionPane, BorderLayout.EAST);
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

        loadArticleFromDB().forEach(article -> tableModel.addRow(new String[]{article.getCodice(), article.getDescrizione(), article.getCategoria(), article.getPosizione(), article.getUnita(), article.getFornitore(), String.valueOf(article.getPrezzo()).replace(".", ",").concat(" €"), String.valueOf(article.getScorta()), article.getProvenienza()}));

        tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(39, 55, 70));
        tableHeader.setForeground(Color.WHITE);

        filterField = RowFilterUtil.createRowFilter(table);
        filterField.setColumns(16);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 16));
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buildFonctionality();
        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() >= 0) {
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
                jtfCodice.setEditable(false);
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_prima) {
            container.removeAll();
            container.revalidate();
            container.add(new AnagraficaPane().getPanel());
            container.repaint();
        }else if (e.getSource() == btn_list_categoria) {
            new CategoryPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbCategoria.getItemCount()).mapToObj(i -> jcbCategoria.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_list_unita) {
            new UnitPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbUnita.getItemCount()).mapToObj(i -> jcbUnita.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        } else if (e.getSource() == btn_list_posizione) {
            new PositionPane(formatTitleFieldPane(e.getActionCommand()), IntStream.range(0, jcbPosizione.getItemCount()).mapToObj(i -> jcbPosizione.getItemAt(i)).collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    private void deleteArticleFromDB(){
        while (table.getSelectedRow() != -1) {
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
            initArticlePane();
        }
    }

    public void insertArticleToDB() {
        Articolo articolo = new Articolo();
        articolo.setCodice(jtfCodice.getText());
        articolo.setDescrizione(jtfDescrizione.getText());
        articolo.setCategoria(String.valueOf(jcbCategoria.getSelectedItem()));
        articolo.setPosizione(String.valueOf(jcbPosizione.getSelectedItem()));
        articolo.setUnita(String.valueOf(jcbUnita.getSelectedItem()));
        articolo.setFornitore(jtfFornitore.getText());
        articolo.setPrezzo(Double.valueOf(jtfCurrency.getText().replace("€", "").replace(",", ".")));
        articolo.setScorta(Integer.valueOf(jspScorta.getValue().toString()));
        articolo.setProvenienza(jtfProvenienza.getText());

        if (articolo.getCodice().isEmpty()) {
            showMessageDialog(container, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkCodice(articolo.getCodice())) {
                showMessageDialog(container, "Articolo già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    PreparedStatement preparedStmt = con.prepareStatement("INSERT INTO ARTICOLO VALUES (?, ?, ?, ?, ?,?,?,?,?)");
                    preparedStmt.setString(1, articolo.getCodice());
                    preparedStmt.setString(2, articolo.getDescrizione());
                    preparedStmt.setString(3, articolo.getCategoria());
                    preparedStmt.setString(4, articolo.getPosizione());
                    preparedStmt.setString(5, articolo.getUnita());
                    preparedStmt.setString(6, articolo.getFornitore());
                    preparedStmt.setDouble(7, articolo.getPrezzo());
                    preparedStmt.setInt(8, articolo.getScorta());
                    preparedStmt.setString(9, articolo.getProvenienza());
                    preparedStmt.execute();
                    con.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                tableModel.addRow(new String[]{articolo.getCodice(), articolo.getDescrizione(), articolo.getCategoria(), articolo.getPosizione(), articolo.getUnita(), articolo.getFornitore(), String.valueOf(articolo.getPrezzo()).replace(".", ",").concat(" €"), String.valueOf(articolo.getScorta()), articolo.getProvenienza()});
                showMessageDialog(container, "Articolo inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        initArticlePane();
    }

    private void updateArticleToDB() {
        Articolo articolo = new Articolo();
        articolo.setCodice(jtfCodice.getText());
        articolo.setDescrizione(jtfDescrizione.getText());
        articolo.setCategoria(String.valueOf(jcbCategoria.getSelectedItem()));
        articolo.setPosizione(String.valueOf(jcbPosizione.getSelectedItem()));
        articolo.setUnita(String.valueOf(jcbUnita.getSelectedItem()));
        articolo.setFornitore(jtfFornitore.getText());
        articolo.setPrezzo(Double.valueOf(jtfCurrency.getText().replace("€", "").replace(",", ".")));
        articolo.setScorta(Integer.valueOf(jspScorta.getValue().toString()));
        articolo.setProvenienza(jtfProvenienza.getText());

        if (articolo.getCodice().isEmpty()) {
            showMessageDialog(container, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkCodice(articolo.getCodice())) {
                if (table.getSelectedRow() == -1) {
                    showMessageDialog(container, "Selezionare l'articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    PreparedStatement ps = con.prepareStatement("UPDATE ARTICOLO SET DESCRIZIONE=?,CATEGORIA=?,POSIZIONE=?,UNITA=?,FORNITORE=?,PREZZO=?,SCORTA=?,PROVENIENZA=? WHERE CODICE=?");
                    ps.setString(1, articolo.getDescrizione());
                    ps.setString(2, articolo.getCategoria());
                    ps.setString(3, articolo.getPosizione());
                    ps.setString(4, articolo.getUnita());
                    ps.setString(5, articolo.getFornitore());
                    ps.setDouble(6, articolo.getPrezzo());
                    ps.setInt(7, articolo.getScorta());
                    ps.setString(8, articolo.getProvenienza());
                    ps.setString(9, articolo.getCodice());
                    ps.execute();

                    tableModel.setValueAt(articolo.getDescrizione(), table.getSelectedRow(), 1);
                    tableModel.setValueAt(articolo.getCategoria(), table.getSelectedRow(), 2);
                    tableModel.setValueAt(articolo.getPosizione(), table.getSelectedRow(), 3);
                    tableModel.setValueAt(articolo.getUnita(), table.getSelectedRow(), 4);
                    tableModel.setValueAt(articolo.getFornitore(), table.getSelectedRow(), 5);
                    tableModel.setValueAt(String.valueOf(articolo.getPrezzo()).replace(".", ",").concat(" €"), table.getSelectedRow(), 6);
                    tableModel.setValueAt(articolo.getScorta(), table.getSelectedRow(), 7);
                    tableModel.setValueAt(articolo.getProvenienza(), table.getSelectedRow(), 8);
                    con.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                showMessageDialog(container, "Articolo aggiornato", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessageDialog(container, "Articolo inesistente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            }
        }
        initArticlePane();
    }

    private boolean checkCodice(String codice) {

        boolean isPresente = false;
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT* FROM ARTICOLO WHERE CODICE='%s'", codice));
            while (rs.next()) isPresente = true;
            stmt.close();
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return isPresente;
    }

    // inizializzazione del campi del pannello degli articoli
    void initArticlePane() {
        table.getSelectionModel().clearSelection();
        jtfCodice.setText(null);
        jtfCodice.setEditable(true);
        jtfDescrizione.setText(null);
        jcbCategoria.setSelectedIndex(0);
        jcbPosizione.setSelectedIndex(0);
        jcbUnita.setSelectedIndex(0);
        jtfFornitore.setText(null);
        jtfCurrency.setValue(0);
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
}

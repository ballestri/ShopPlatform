package shop.view.rilevazione;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import shop.controller.ComboBoxFilterDecorator;
import shop.controller.CustomComboRenderer;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static shop.view.rilevazione.controller.CaricoDbOperation.*;

public class InfoCaricoPane extends JFrame implements ActionListener {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 720;
    Font font;
    private static final String DATE_FORMAT = "dd/MM/yyyy";


    JPanel wrapperPane, actionPane;
    RoundedPanel infoPane, internPane;
    protected JLabel lblCodice, lblDescrizione, lblData, lblQuantita, lblFornitore, lblNote;
    public static JTextField jtfDescrizione, jtfFornitore;
    public static JSpinner jspQuantita;
    public static JDateChooser jdcData;
    public static JComboBox<String> jcbCodice;
    protected JButton btn_save, btn_clear;
    public static JTextArea jtaNote;
    private static final Color JTF_COLOR = new Color(46, 134, 193);

    public InfoCaricoPane() {

        setTitle("Informazioni Carico");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(size);
        setSize(size);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/ico.png"))).getImage());
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);
        JToolBar toolbar = new JToolBar();

        JButton btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(e -> dispose());

        font = new Font("HelveticaNeue", Font.BOLD, 17);

        wrapperPane = new JPanel();
        internPane = new RoundedPanel();
        actionPane = new JPanel();
        infoPane = new RoundedPanel();

        initComponents();
        add(wrapperPane);
        getContentPane().setBackground(new Color(116, 142, 203));
        toolbar.setFloatable(false);
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        setVisible(true);
    }

    void initComponents() {
        wrapperPane.setBounds(20, 90, WIDTH - 40, HEIGHT - 160);
        internPane.setPreferredSize(new Dimension(WIDTH - 200, HEIGHT - 380));
        infoPane.setPreferredSize(new Dimension(WIDTH - 200, 60));

        wrapperPane.setBackground(new Color(39, 55, 70));
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(5, 10, 5, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        wrapperPane.setBorder(border);

        buildFornitore();

        wrapperPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        wrapperPane.add(infoPane);
        wrapperPane.add(internPane);
        wrapperPane.add(actionPane);
    }

    void buildFornitore() {
        infoPane.setLayout(new GridBagLayout());
        JLabel lblFormName = new JLabel("Operazioni di carico");
        lblFormName.setFont(new Font("HelveticaNeue", Font.BOLD, 18));
        infoPane.add(lblFormName);

        // pannello interno
        lblCodice = new JLabel("Codice prodotto");
        lblCodice.setFont(font);

        ArrayList<String> items = getListCodici();
        items.add(0, null);
        jcbCodice = new JComboBox<>(items.toArray(new String[0]));
        ComboBoxFilterDecorator<String> decorate = ComboBoxFilterDecorator.decorate(jcbCodice, InfoCaricoPane::codiceFilter);
        jcbCodice.setRenderer(new CustomComboRenderer(decorate.getFilterLabel()));
        jcbCodice.setBorder(new LineBorder(Color.BLACK));
        jcbCodice.setFont(font);
        jcbCodice.addActionListener(this);

        lblDescrizione = new JLabel("Descrizione");
        lblDescrizione.setFont(font);

        jtfDescrizione = new JTextField(18);
        jtfDescrizione.setCaretColor(Color.BLACK);
        jtfDescrizione.setBackground(JTF_COLOR);
        jtfDescrizione.setBorder(new LineBorder(Color.BLACK));
        jtfDescrizione.setFont(font);

        lblData = new JLabel("Data carico");
        lblData.setFont(font);

        jdcData = new JDateChooser();
        jdcData.setDateFormatString(DATE_FORMAT);
        jdcData.setPreferredSize(new Dimension(220, 30));

        jdcData.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        Date date = new Date();
        jdcData.setDate(date);
        jdcData.setMaxSelectableDate(new Date());
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) jdcData.getComponent(1);
        dateEditor.setHorizontalAlignment(JTextField.RIGHT);
        dateEditor.setFont(font);
        dateEditor.setBackground(JTF_COLOR);
        dateEditor.setBorder(new LineBorder(Color.BLACK));

        lblQuantita = new JLabel("Quantita' carico");
        lblQuantita.setFont(font);

        jspQuantita = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        jspQuantita.setBorder(new EmptyBorder(0, 5, 0, 0));
        jspQuantita.setBorder(new LineBorder(Color.BLACK));
        jspQuantita.setPreferredSize(new Dimension(120, 25));
        jspQuantita.setFont(font);
        JTextField jtfQuantita = ((JSpinner.DefaultEditor) jspQuantita.getEditor()).getTextField();
        jtfQuantita.setBackground(JTF_COLOR);
        jtfQuantita.setCaretColor(new Color(255, 255, 255));

        lblFornitore = new JLabel("Fornitore");
        lblFornitore.setFont(font);

        jtfFornitore = new JTextField(18);
        jtfFornitore.setCaretColor(Color.BLACK);
        jtfFornitore.setBackground(JTF_COLOR);
        jtfFornitore.setBorder(new LineBorder(Color.BLACK));
        jtfFornitore.setFont(font);

        lblNote = new JLabel("Note");
        lblNote.setFont(font);

        jtaNote = new JTextArea(3, 18);
        jtaNote.setLineWrap(true);
        jtaNote.setWrapStyleWord(true);

        jtaNote.setCaretColor(Color.BLACK);
        jtaNote.setBackground(JTF_COLOR);
        jtaNote.setBorder(new LineBorder(Color.BLACK));
        jtaNote.setFont(font);

       JScrollPane jScrollNote = new JScrollPane(jtaNote, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jScrollNote.setBorder(new EmptyBorder(0, 0, 0, 0));

        internPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        // first column
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 1;
        gc.weighty = 1;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblCodice, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblDescrizione, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblData, gc);

        gc.gridx = 0;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblQuantita, gc);

        gc.gridx = 0;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblFornitore, gc);

        gc.gridx = 0;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(2, 10, 2, 10);
        internPane.add(lblNote, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jcbCodice, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfDescrizione, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jdcData, gc);

        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jspQuantita, gc);

        gc.gridx = 1;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jtfFornitore, gc);

        gc.gridx = 1;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        internPane.add(jScrollNote, gc);

        btn_save = new JButton(DesktopRender.formatButton("Save"));
        btn_clear = new JButton(DesktopRender.formatButton("Clear"));

        formatButton(btn_clear);
        formatButton(btn_save);

        actionPane.setBackground(wrapperPane.getBackground());
        actionPane.setLayout(new GridBagLayout());

        GridBagConstraints ca = new GridBagConstraints();
        ca.insets = new Insets(5, 10, 15, 28);

        actionPane.add(btn_clear, ca);
        actionPane.add(btn_save, ca);

        jcbCodice.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                jtfDescrizione.setText(getProduct(String.valueOf(jcbCodice.getSelectedItem())).getDescrizione());
                jtfFornitore.setText(getProduct(String.valueOf(jcbCodice.getSelectedItem())).getFornitore());
                jtfDescrizione.setEditable(false);
                jtfFornitore.setEditable(false);

            }
        });

        btn_clear.addActionListener(e -> initInfoCaricoPane());
        btn_save.addActionListener(e -> {
            insertCaricoToDB();
            dispose();
                }
        );
    }

    void formatButton(JButton btn) {
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(Color.WHITE));
        btn.setBackground(new Color(0, 128, 128));
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));
    }

    public static void initInfoCaricoPane() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private static boolean codiceFilter(String codice, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getProcessDisplayText(codice).toLowerCase().contains(textToFilter.toLowerCase());
    }
}

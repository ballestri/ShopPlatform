package shop.view;

import shop.controller.ComboDataAccess;
import shop.utils.DesktopRender;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.util.*;


import shop.controller.ComboBoxFilterDecorator;
import shop.controller.CustomComboRenderer;


public class ClientePane extends AContainer implements ActionListener {

    private JButton btn_prima;

    // label articolo
    JLabel lblCode;
    JLabel lblDenom;
    JLabel lblAddress;
    JLabel lblZipcode;
    JLabel lblCountry;
    JLabel lblTelefono;
    JLabel lblPiva;

    // Label degli errori
    JLabel lblCodeError;
    JLabel lblDenomError;
    JLabel lblAddressError;
    JLabel lblZipcodeError;
    JLabel lblTelefonoError;
    JLabel lblPivaError;


    // jtextfield articolo
    JTextField jtfCode;
    JTextField jtfDenom;
    JTextField jtfAddress;
    JTextField jtfZipcode;
    JTextField jtfTelefono;
    JTextField jtfPiva;
    JComboBox jcbCountries;

    // pannello interno
    JPanel panel;

    // Pulsante di carica articolo
    JButton btn_registra;

    ArrayList<String> listCountries;
    private HashMap dictCountries;
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

        JButton btn_close = new JButton();
        btn_close.setIcon(new ImageIcon(this.getClass().getResource("/images/esci.png")));
        toolbar.add(btn_close);
        btn_close.setFocusPainted(false);
        btn_close.setToolTipText("Chiudi");
        toolbar.addSeparator();
        btn_close.addActionListener(evt -> System.exit(0));

        // Informazioni sul cliente
        // Pannello interno
        panel = new JPanel();


        // Gestione dei labels
        lblCode = new JLabel("Codice");
        lblDenom = new JLabel("Denominazione");
        lblAddress = new JLabel("Indirizzo");
        lblZipcode = new JLabel("Zip Code");
        lblCountry = new JLabel("Country");
        lblTelefono = new JLabel("Telefono");
        lblPiva = new JLabel("PIVA");

        lblCodeError = new JLabel("Inserire un valore");
        lblDenomError = new JLabel("Inserire un valore");
        lblAddressError = new JLabel("Inserire un valore");
        lblZipcodeError = new JLabel("Inserire un valore");
        lblTelefonoError = new JLabel("Inserire un valore");
        lblPivaError = new JLabel("Inserire un valore");


        // jtextfield sull'articolo
        jtfCode = new JTextField(24);
        jtfDenom = new JTextField(24);
        jtfAddress = new JTextField(24);
        jtfZipcode = new JTextField(6);
        jtfTelefono = new JTextField(24);
        jtfPiva = new JTextField(24);


        build();

        container.add(panel);
        toolbar.setFloatable(false);
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);
    }


    public void build() {

        panel.setBounds(375, 160, 825, 625);
        Border line = BorderFactory.createLineBorder(Color.WHITE);
        Border empty = new EmptyBorder(10, 10, 10, 10);
        CompoundBorder border = new CompoundBorder(line, empty);
        panel.setBorder(border);
        panel.setBackground(new Color(39, 55, 70));

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();


        // Lista dei paesi

        //Arrays.stream(Locale.getISOCountries()).forEach(countryCode -> listCountries.add((new Locale("", countryCode)).getISO3Country().toUpperCase()));

        //Arrays.stream(Locale.getISOCountries()).forEach(countryCode -> listCountries.add((new Locale("", countryCode)).getDisplayCountry(Locale.FRENCH).toUpperCase()));

        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry().toUpperCase(), iso);
        }
        listCountries = new ArrayList<>(countries.keySet());
        Collections.sort(listCountries);
        // carico i prefix
        // Gestione della combo dei processi
        try {
            dictCountries = (HashMap) ComboDataAccess.getInformations();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        jcbCountries = new JComboBox(listCountries.toArray(new String[listCountries.size()]));
        jcbCountries.setRenderer(new CustomComboRenderer((ComboBoxFilterDecorator.decorate(jcbCountries, ClientePane::countryFilter)).getFilterLabel()));


        // Font dei pulsanti

        // Gestione del font dei labels
        font = new Font("HelveticaNeue", Font.BOLD, 22);
        lblCode.setForeground(Color.WHITE);
        lblCode.setFont(font);
        lblDenom.setForeground(Color.WHITE);
        lblDenom.setFont(font);
        lblAddress.setForeground(Color.WHITE);
        lblAddress.setFont(font);
        lblZipcode.setForeground(Color.WHITE);
        lblZipcode.setFont(font);
        lblCountry.setForeground(Color.WHITE);
        lblCountry.setFont(font);
        jcbCountries.setPreferredSize(new Dimension(280, 50));
        jcbCountries.setBackground(Color.WHITE);
        //jcbCountries.setWrapStyleWord(true);
        jcbCountries.setBorder(new LineBorder(Color.BLACK));
        jcbCountries.setFont(new Font("HelveticaNeue", Font.BOLD, 16));
        jcbCountries.setMaximumRowCount(5);
        lblTelefono.setForeground(Color.WHITE);
        lblTelefono.setFont(font);
        lblPiva.setForeground(Color.WHITE);
        lblPiva.setFont(font);

        // Gestione dei jtextField
        jtfCode.setFont(font);
        jtfCode.setBorder(new LineBorder(Color.BLACK));
        jtfCode.setCaretColor(new Color(255, 255, 255));
        jtfCode.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfCode.setBackground(new Color(46, 134, 193));
        jtfCode.setPreferredSize(new Dimension(150, 50));

        jtfDenom.setBorder(new LineBorder(Color.BLACK));
        jtfDenom.setCaretColor(new Color(255, 255, 255));
        jtfDenom.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfDenom.setBackground(new Color(46, 134, 193));
        jtfDenom.setPreferredSize(new Dimension(150, 50));
        jtfDenom.setFont(font);

        jtfAddress.setFont(new Font("HelveticaNeue", Font.BOLD, 14));
        jtfAddress.setBorder(new LineBorder(Color.BLACK));
        jtfAddress.setCaretColor(new Color(255, 255, 255));
        jtfAddress.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfAddress.setBackground(new Color(46, 134, 193));
        jtfAddress.setPreferredSize(new Dimension(150, 50));
        jtfAddress.setFont(font);


        jtfZipcode.setBorder(new LineBorder(Color.BLACK));
        jtfZipcode.setCaretColor(new Color(255, 255, 255));
        jtfZipcode.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfZipcode.setBackground(new Color(46, 134, 193));
        jtfZipcode.setPreferredSize(new Dimension(150, 50));
        jtfZipcode.setFont(font);


        jtfTelefono.setFont(new Font("HelveticaNeue", Font.BOLD, 14));
        jtfTelefono.setBorder(new LineBorder(Color.BLACK));
        jtfTelefono.setCaretColor(new Color(255, 255, 255));
        jtfTelefono.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfTelefono.setBackground(new Color(46, 134, 193));
        jtfTelefono.setPreferredSize(new Dimension(150, 50));
        jtfTelefono.setFont(font);


        jtfPiva.setFont(new Font("HelveticaNeue", Font.BOLD, 14));
        jtfPiva.setBorder(new LineBorder(Color.BLACK));
        jtfPiva.setCaretColor(new Color(255, 255, 255));
        jtfPiva.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfPiva.setBackground(new Color(46, 134, 193));
        jtfPiva.setPreferredSize(new Dimension(150, 50));
        jtfPiva.setFont(font);


        // first column of the grid//
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblCode, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblDenom, gc);

        gc.gridx = 0;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblAddress, gc);

        gc.gridx = 0;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblZipcode, gc);

        gc.gridx = 0;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblCountry, gc);

        gc.gridx = 0;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblTelefono, gc);

        gc.gridx = 0;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(lblPiva, gc);

        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jtfCode, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jtfDenom, gc);

        gc.gridx = 1;
        gc.gridy = 2;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jtfAddress, gc);


        gc.gridx = 1;
        gc.gridy = 3;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jtfZipcode, gc);

        gc.gridx = 1;
        gc.gridy = 4;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jcbCountries, gc);


        gc.gridx = 1;
        gc.gridy = 5;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jtfTelefono, gc);

        gc.gridx = 1;
        gc.gridy = 6;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(jtfPiva, gc);

        // Aggiungo il pulsante di carica
        font = new Font("HelveticaNeue", Font.BOLD, 32);
        btn_registra = new JButton(DesktopRender.formatButton("Registra"));
        btn_registra.setPreferredSize(new Dimension(260, 100));
        btn_registra.setBackground(new Color(0, 128, 128));
        btn_registra.setForeground(Color.WHITE);
        btn_registra.setBorder(new LineBorder(Color.BLACK));
        btn_registra.setFocusPainted(false);
        btn_registra.setFont(font);

        gc.gridx = 1;
        gc.gridy = 7;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(btn_registra, gc);


        // Gestione del prefisso
        jtfTelefono.setText(Objects.requireNonNull(dictCountries.get(countries.get(listCountries.get(0)))).toString());
        jcbCountries.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                jtfTelefono.setText(Objects.requireNonNull(dictCountries.get(countries.get(jcbCountries.getSelectedItem()))).toString());

        });

    }

    private static boolean countryFilter(String country, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }
        return CustomComboRenderer.getProcessDisplayText(country).toLowerCase().contains(textToFilter.toLowerCase());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        container.removeAll();
        container.revalidate();
        if (e.getSource() == btn_prima)
            container.add(new AnagraficaPane().getPanel());

        container.repaint();
    }

}

package shop.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import shop.utils.CreateRoundButton;
import shop.utils.DesktopRender;

public class AnagraficaPane extends AContainer implements ActionListener {

    // Le funzionalita dell'app
    private JButton btn_articolo;
    private JButton btn_cliente;

    private JButton btn_prima;

    public AnagraficaPane() {

        initPanel();
    }

    public void initPanel() {

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(4000);

        // Toolbar
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

        // Pulsanti
        btn_articolo = new CreateRoundButton(DesktopRender.formatButton("Gestione", "Articoli"));
        btn_cliente = new CreateRoundButton(DesktopRender.formatButton("Gestione", "Clienti"));

        // Pannello interno
        JPanel panel = new JPanel();

        // Font dei pulsanti
        Font font = new Font("HelveticaNeue", Font.BOLD, 30);

        panel.setBounds(195, 105, 825, 625);
        Border whiteline = BorderFactory.createLineBorder(Color.WHITE);
        panel.setBorder(whiteline);
        panel.setBackground(new Color(128, 0, 128));


        // Pulsante dei HostMonitors
        btn_articolo.setPreferredSize(new Dimension( 260, 260));
        btn_articolo.setBackground(new Color(0, 128, 128));
        btn_articolo.setFont(font);
        btn_articolo.setForeground(Color.WHITE);
        btn_articolo.setBorder(new LineBorder(Color.BLACK));
        btn_articolo.setFocusPainted(false);
        btn_articolo.addActionListener(this);

        // Pulsante per il patching
        btn_cliente.setPreferredSize(new Dimension( 260, 260));
        btn_cliente.setBackground(new Color(39, 55, 70));
        btn_cliente.setFont(font);
        btn_cliente.setForeground(Color.WHITE);
        btn_cliente.setBorder(new LineBorder(Color.BLACK));
        btn_cliente.setFocusPainted(false);
        btn_cliente.addActionListener(this);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // first column of the grid//
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        panel.add(btn_articolo, gc);


        // second column//
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 3;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_START;
        panel.add(btn_cliente, gc);

        container.add(panel);

        container.setLayout(new BorderLayout());

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        container.removeAll();
        container.revalidate();
        if (e.getSource() == btn_prima)
            container.add(new MagazzinoPane().getPanel());
        else if (e.getSource() == btn_articolo)
            container.add(new ArticoloPane().getPanel());
        else if (e.getSource() == btn_cliente)
            container.add(new ClientePane().getPanel());

        container.repaint();
    }

}

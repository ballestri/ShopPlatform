package shop.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import shop.utils.DesktopRender;

public class MagazzinoPane extends AContainer implements ActionListener {

	// Le funzionalita' dell'app
	private JButton btn_anagrafica;
	private JButton btn_gestione;
	private JButton btn_carico;
	private JButton btn_richieste;

	private Font font;

	private JButton btn_prima;

	public MagazzinoPane() {

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
		btn_anagrafica = new JButton(DesktopRender.formatButton("Anagrafica"));
		btn_gestione = new JButton(DesktopRender.formatButton("Gestione","Storico"));
		btn_carico = new JButton(DesktopRender.formatButton("Carico"));
		btn_richieste = new JButton(DesktopRender.formatButton("Richieste"));

		// Font dei pulsanti
		font = new Font("HelveticaNeue", Font.BOLD, 24);

		// Pulsante di recupero bilanci
		btn_anagrafica.setBounds(115, 150, 240, 120);
		btn_anagrafica.setBackground(new Color(128, 0, 128));
		buttonFormatting(btn_anagrafica);

		btn_gestione.setBounds(445, 150, 240, 120);
		btn_gestione.setBackground(new Color(39, 55, 70));
		buttonFormatting(btn_gestione);

		btn_carico.setBounds(115, 348, 240, 120);
		btn_carico.setBackground(new Color(128, 0, 0));
		buttonFormatting(btn_carico);

		btn_richieste.setBounds(445, 348, 240, 120);
		btn_richieste.setBackground(new Color(0, 128, 128));
		buttonFormatting(btn_richieste);

		// Le varie funzionalita' dell'app

		container.add(btn_richieste);
		container.add(btn_anagrafica);
		container.add(btn_gestione);
		container.add(btn_carico);

		toolbar.setFloatable(false);
		container.setLayout(new BorderLayout());
		container.add(toolbar, BorderLayout.NORTH);

	}

	void buttonFormatting(JButton btn) {
		btn.setFont(font);
		btn.setForeground(Color.WHITE);
		btn.setBorder(new LineBorder(Color.BLACK));
		btn.setFocusPainted(false);
		btn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		container.removeAll();
		container.revalidate();

		if (e.getSource() == btn_anagrafica)
			container.add(new AnagraficaPane().getPanel());
		else if (e.getSource() == btn_prima)
			container.add(new Pannello().getPanel());

		container.repaint();

	}
}

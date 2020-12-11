package shop.view;

import shop.db.ConnectionManager;
import shop.utils.CreateRoundButton;
import shop.utils.DesktopRender;
import shop.utils.RoundedPanel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginPane extends AContainer implements ActionListener {

    protected Font font;

    JPanel wrapperPane;
    RoundedPanel loginPanel, informationPanel;

    JLabel lblFormName, lblUsername, lblPassword;

    JTextField jtfUsername;
    JPasswordField jtfPassword;

    CreateRoundButton btn_login;


    public LoginPane() {

        initPanel();
    }

    public void initPanel() {

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
        btn_close.addActionListener(evt -> System.exit(0));


        // Pannello interno
        wrapperPane = new JPanel();
        wrapperPane.setBounds(375, 160, 825, 625);
        wrapperPane.setBackground(container.getBackground());
        wrapperPane.setLayout(new BorderLayout());

        loginPanel = new RoundedPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(128, 0, 128));
        loginPanel.setPreferredSize(new Dimension(825, 100));
        font = new Font("HelveticaNeue", Font.BOLD, 36);
        lblFormName = new JLabel("Login Form");
        lblFormName.setForeground(Color.WHITE);
        lblFormName.setFont(font);
        loginPanel.add(lblFormName);

        wrapperPane.add(loginPanel, BorderLayout.NORTH);


        font = new Font("HelveticaNeue", Font.BOLD, 28);
        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        jtfUsername = new JTextField();
        jtfPassword = new JPasswordField();
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(font);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(font);

        font = new Font("HelveticaNeue", Font.BOLD, 20);
        jtfUsername.setBorder(new LineBorder(Color.BLACK));
        jtfUsername.setCaretColor(new Color(255, 255, 255));
        jtfUsername.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfUsername.setBackground(new Color(46, 134, 193));
        jtfUsername.setPreferredSize(new Dimension(350, 50));
        jtfUsername.setFont(font);


        jtfPassword.setBorder(new LineBorder(Color.BLACK));
        jtfPassword.setCaretColor(new Color(255, 255, 255));
        jtfPassword.setBorder(new EmptyBorder(0, 20, 0, 0));
        jtfPassword.setBackground(new Color(46, 134, 193));
        jtfPassword.setPreferredSize(new Dimension(350, 50));
        jtfPassword.setFont(font);


        informationPanel = new RoundedPanel();
        informationPanel.setBackground(new Color(39, 55, 70));
        informationPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // first column of the grid//
        gc.anchor = GridBagConstraints.EAST;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        informationPanel.add(lblUsername, gc);

        gc.gridx = 0;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(5, 10, 10, 10);
        informationPanel.add(lblPassword, gc);

        // column 2
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;

        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(5, 10, 10, 10);
        informationPanel.add(jtfUsername, gc);

        gc.gridx = 1;
        gc.gridy = 1;

        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(5, 10, 10, 10);
        informationPanel.add(jtfPassword, gc);

        // Aggiungo il pulsante di carica
        font = new Font("HelveticaNeue", Font.BOLD, 32);
        btn_login = new CreateRoundButton(DesktopRender.formatButton("Sign In"));
        btn_login.setPreferredSize(new Dimension(260, 260));
        btn_login.setBackground(new Color(0, 128, 128));
        btn_login.setForeground(Color.WHITE);
        btn_login.setBorder(new LineBorder(Color.BLACK));
        btn_login.setFocusPainted(false);
        btn_login.setFont(font);
        btn_login.setMnemonic(KeyEvent.VK_ENTER);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(5, 10, 10, 10);

        informationPanel.add(btn_login, gc);

        wrapperPane.add(informationPanel, BorderLayout.CENTER);
        btn_login.addActionListener(this);

        container.add(wrapperPane);
        toolbar.setFloatable(false);
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.NORTH);

    }

    void controlAccess() {

        Connection con = (new ConnectionManager()).getConnection();
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM Credentials");
            while (rs.next()) {
                if (jtfUsername.getText().equals(rs.getString("username")) && ((new String(jtfPassword.getPassword())).equals(rs.getString("password")))) {
                    container.removeAll();
                    container.revalidate();
                    container.add(new Pannello().getPanel());
                    container.repaint();
                }
            }
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_login) {
            controlAccess();
        }
    }
}

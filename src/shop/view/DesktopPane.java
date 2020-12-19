package shop.view;

import shop.db.ConnectionManager;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;
import javax.swing.*;
import java.sql.*;
import java.util.Properties;
import java.io.*;

import org.apache.ibatis.jdbc.ScriptRunner;

public class DesktopPane extends JFrame {

    private static final int WIDTH = 1575;
    private static final int HEIGHT = 960;

    public DesktopPane() {

        setTitle("Shop Platform v. 1.0");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(new Dimension(WIDTH, HEIGHT));
        setSize(size);
        setPreferredSize(size);
        setLocationByPlatform(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/ico.png"))).getImage());

        getContentPane().removeAll();
        getContentPane().add(new LoginPane().getPanel());
        getContentPane().doLayout();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String... args) {
        setDefaultLookAndFeelDecorated(true);
        (new DesktopPane()).setVisible(true);
        Properties prop = new Properties();
        try {
            prop.load(new BufferedReader(new InputStreamReader(Objects.requireNonNull(ConnectionManager.class.getClassLoader().getResourceAsStream("config/config.properties")))));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {

            Connection con = (new ConnectionManager()).getConnection();
            ScriptRunner sr = new ScriptRunner(con);
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(DesktopPane.class.getClassLoader().getResourceAsStream("config/shop.sql"))));
            sr.runScript(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }


        // Connessione al DB
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO Credentials VALUES ('" + prop.getProperty("db.username") + "','" + prop.getProperty("db.password") + "')");

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

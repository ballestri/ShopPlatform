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

@SuppressWarnings("serial")
public class DesktopPane extends JFrame {


    private static final String CREATE_CREDENTIALS_TABLE = "CREATE TABLE CREDENTIALS ("
            + "USERNAME VARCHAR(50) NOT NULL,"
            + "PASSWORD VARCHAR(50) NOT NULL,"
            + "PRIMARY KEY (USERNAME,PASSWORD))";

    private static final String CREATE_CATEGORIA_PRODOTTO_TABLE="CREATE TABLE CATEGORIA_PRODOTTO ("
            + "CATEGORIA VARCHAR(50) NOT NULL,"
            + "PRIMARY KEY (CATEGORIA))";

    private static final String CREATE_UNITA_PRODOTTO_TABLE="CREATE TABLE UNITA_PRODOTTO ("
            + "UNITA VARCHAR(50) NOT NULL,"
            + "PRIMARY KEY (UNITA))";

    private static final String CREATE_POSIZIONE_PRODOTTO_TABLE="CREATE TABLE POSIZIONE_PRODOTTO ("
            + "POSIZIONE VARCHAR(50) NOT NULL,"
            + "PRIMARY KEY (POSIZIONE))";




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
        } catch (NullPointerException ignored) {}


        // Connessione al DB
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();


            //stmt.executeUpdate("DROP TABLE CREDENTIALS");
            //stmt.executeUpdate(CREATE_CREDENTIALS_TABLE);


            //Initialize the script runner






            // Give the input file to Reader
            //Reader reader = new BufferedReader(new FileReader(DesktopPane.class.getClassLoader().getResourceAsStream("config/shop.sql")));

            // Exctute script

            /*
            stmt.executeUpdate("DROP TABLE CREDENTIALS");
            stmt.executeUpdate(CREATE_CREDENTIALS_TABLE);
            */

            // CATEGORIA DI PRODOTTI
           //stmt.executeUpdate("DROP TABLE UNITA_PRODOTTO");
            //stmt.executeUpdate(CREATE_UNITA_PRODOTTO_TABLE);
            //stmt.executeUpdate(CREATE_POSIZIONE_PRODOTTO_TABLE);

            /*
            stmt.executeUpdate("DROP TABLE CREDENTIALS");
            stmt.executeUpdate(CREATE_CREDENTIALS_TABLE);
            */

            stmt.executeUpdate("INSERT INTO Credentials VALUES ('" + prop.getProperty("db.username") + "','" + prop.getProperty("db.password") + "')");

            stmt.close();
            con.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }











    }

}

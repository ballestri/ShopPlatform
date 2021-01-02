package shop.view.carico.controller;

import shop.db.ConnectionManager;
import shop.model.Articolo;
import shop.model.Carico;
import shop.view.CaricoPane;
import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.CaricoPane.tableModel;
import static shop.view.CaricoPane.table;
import static shop.view.carico.InfoCaricoPane.*;


public class CaricoDbOperation {

    public static void deleteCaricoFromDB() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare un carico", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        while (table.getSelectedRow() != -1) {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                Statement stmt = con.createStatement();
                stmt.executeUpdate(String.format("DELETE FROM CARICO WHERE CODICE='%s'", table.getValueAt(table.getSelectedRow(), 1)));
                stmt.close();
                con.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            CaricoPane.tableModel.removeRow(table.getSelectedRow());
        }

        for (int index = 0; index < CaricoPane.tableModel.getRowCount(); index++) {
            CaricoPane.tableModel.setValueAt(index + 1, index, 0);
        }

        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static ArrayList<String> getListCodici() {
        ArrayList<String> codici = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CODICE FROM ARTICOLO");
            while (rs.next()) {
                codici.add(rs.getString("CODICE"));
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return codici;
    }

    public static Articolo getProduct(String codice) {
        Articolo articolo = new Articolo();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT DESCRIZIONE,FORNITORE FROM ARTICOLO WHERE CODICE='%s'", codice));
            if (rs.next()) {
                articolo.setDescrizione(rs.getString("DESCRIZIONE"));
                articolo.setFornitore(rs.getString("FORNITORE"));
            }
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return articolo;
    }



    public static ArrayList<Carico>  loadCaricoFromDB() {
        ArrayList<Carico> carichi = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM CARICO");

            while (rs.next()) {
                Carico carico = new Carico();
                carico.setCodice(rs.getString("CODICE"));
                carico.setDescrizione(rs.getString("DESCRIZIONE"));
                carico.setDatacarico(rs.getDate("DATACARICO"));
                carico.setQuantita(rs.getInt("QUANTITA"));
                carico.setFornitore(rs.getString("FORNITORE"));
                carico.setNote(rs.getString("NOTE"));

                carichi.add(carico);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return carichi;
    }

    public static int getCaricoCountItems() {
        int count=0;
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM CARICO");
            rs.next();
            count = rs.getInt(1);
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }


    private static boolean checkCodice(String piva) {

        boolean isPresente = false;
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT* FROM CARICO WHERE CODICE='%s' GROUP BY CODICE HAVING COUNT(*) > 0", piva));
            if (rs.next()) isPresente = true;
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return isPresente;
    }

    public static void insertCaricoToDB()  {

        Carico carico= new Carico();
        carico.setCodice(String.valueOf(jcbCodice.getSelectedItem()));
        carico.setDescrizione(jtfDescrizione.getText());
        carico.setDatacarico(jdcData.getDate());
        carico.setQuantita(Integer.valueOf(jspQuantita.getValue().toString()));
        carico.setFornitore(jtfFornitore.getText());
        carico.setNote(jtaNote.getText());


        if (carico.getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {

            if (checkCodice(carico.getCodice())) {
                showMessageDialog(null, "Codice già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    Connection con = (new ConnectionManager()).getConnection();
                    PreparedStatement preparedStmt = con.prepareStatement("INSERT INTO CARICO VALUES (?, ?, ?, ?, ?,?)");
                    preparedStmt.setString(1, carico.getCodice());
                    preparedStmt.setString(2, carico.getDescrizione());


                    java.util.Date utilStartDate = jdcData.getDate();
                    java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());

                    preparedStmt.setDate(3, sqlStartDate);

                    preparedStmt.setInt(4, carico.getQuantita());
                    preparedStmt.setString(5, carico.getFornitore());
                    preparedStmt.setString(6, carico.getNote());
                    preparedStmt.execute();
                    con.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                tableModel.addRow(new String[]{String.valueOf(getCaricoCountItems()), carico.getCodice(), carico.getDescrizione(), (new SimpleDateFormat("dd/MM/yyyy")).format(carico.getDatacarico()), String.valueOf(carico.getQuantita()), carico.getFornitore(), carico.getNote()});
                showMessageDialog(null, "Carico inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    public static Date convertToSqlDate(Date date){
        java.sql.Date data = new java.sql.Date(System.currentTimeMillis());
        data = new java.sql.Date(date.getTime());

        return data;
    }

    public static Date stringToDate(String s, String style) throws ParseException {
        DateFormat format = new SimpleDateFormat(style);
        Date date = (Date) format.parse(s);
        return date;
    }

    }


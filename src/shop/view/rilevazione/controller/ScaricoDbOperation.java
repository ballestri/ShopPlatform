package shop.view.rilevazione.controller;

import shop.db.ConnectionManager;
import shop.model.Articolo;
import shop.model.Scarico;
import shop.view.ScaricoPane;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.ScaricoPane.tableModel;
import static shop.view.ScaricoPane.table;
import static shop.view.rilevazione.InfoScaricoPane.jdcData;
import static shop.view.rilevazione.InfoScaricoPane.*;

public class ScaricoDbOperation {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static void deleteScaricoFromDB() {

        if (table.getSelectedRow() == -1) {
            showMessageDialog(null, "Selezionare uno scarico", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (table.getSelectedRow() != -1) {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                String QUERY = "DELETE FROM SCARICO WHERE ID=?";
                PreparedStatement preparedStmt = con.prepareStatement(QUERY);
                preparedStmt.setInt(1, Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
                preparedStmt.execute();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            tableModel.removeRow(table.getSelectedRow());
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
        } catch (SQLException e) {
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return articolo;
    }


    public static ArrayList<Scarico> loadScaricoFromDB() {
        ArrayList<Scarico> list_scarico = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT S.CODICE,S.DESCRIZIONE,S.DATASCARICO,S.QUANTITA,A.FORNITORE,S.NOTE FROM SCARICO S JOIN ARTICOLO A ON (S.CODICE=A.CODICE)");

            while (rs.next()) {
                Scarico scarico = new Scarico();
                scarico.setCodice(rs.getString("CODICE"));
                scarico.setDescrizione(rs.getString("DESCRIZIONE"));
                scarico.setDatascarico(rs.getDate("DATASCARICO"));
                scarico.setQuantita(rs.getInt("QUANTITA"));
                scarico.setFornitore(rs.getString("FORNITORE"));
                scarico.setNote(rs.getString("NOTE"));
                list_scarico.add(scarico);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list_scarico;
    }

    public static void insertScaricoToDB() {

        Scarico scarico = new Scarico();
        scarico.setCodice(String.valueOf(jcbCodice.getSelectedItem()));
        scarico.setDescrizione(jtfDescrizione.getText());
        scarico.setDatascarico(jdcData.getDate());
        scarico.setQuantita(Integer.valueOf(jspQuantita.getValue().toString()));
        scarico.setFornitore(jtfFornitore.getText());
        scarico.setNote(jtaNote.getText());

        if (scarico.getCodice().isEmpty()) {
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Connection con = (new ConnectionManager()).getConnection();
                String QUERY = "INSERT INTO SCARICO (CODICE,DESCRIZIONE,DATASCARICO,QUANTITA,FORNITORE,NOTE) VALUES (?,?, ?, ?, ?, ?)";
                PreparedStatement preparedStmt = con.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, scarico.getCodice());
                preparedStmt.setString(2, scarico.getDescrizione());
                preparedStmt.setDate(3, new Date(jdcData.getDate().getTime()));
                preparedStmt.setInt(4, scarico.getQuantita());
                preparedStmt.setString(5, scarico.getFornitore());
                preparedStmt.setString(6, scarico.getNote());
                preparedStmt.execute();
                ResultSet rs = preparedStmt.getGeneratedKeys();
                if (rs.next()) scarico.setID(rs.getInt(1));
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            ScaricoPane.tableModel.addRow(new String[]{String.valueOf(scarico.getID()), (new SimpleDateFormat(DATE_FORMAT)).format(scarico.getDatascarico()), scarico.getCodice(), scarico.getDescrizione(), String.valueOf(scarico.getQuantita()), scarico.getFornitore(), scarico.getNote()});
            showMessageDialog(null, "Scarico inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}


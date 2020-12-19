package shop.view.articolo.controller;

import shop.db.ConnectionManager;
import shop.model.Articolo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;
import static shop.view.ArticoloPane.*;

public class ArticleDbOperation {
    public static void deleteArticleFromDB(){
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
        showMessageDialog(null, "Cancellazione effettuata", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void insertArticleToDB() {
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
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkCodice(articolo.getCodice())) {
                showMessageDialog(null, "Articolo già presente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
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
                showMessageDialog(null, "Articolo inserito", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        initArticlePane();
    }

    public static void updateArticleToDB() {
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
            showMessageDialog(null, "Codice articolo vuoto", "Info Dialog", JOptionPane.ERROR_MESSAGE);
        } else {
            if (checkCodice(articolo.getCodice())) {
                if (table.getSelectedRow() == -1) {
                    showMessageDialog(null, "Selezionare l'articolo", "Info Dialog", JOptionPane.ERROR_MESSAGE);
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
                showMessageDialog(null, "Articolo aggiornato", "Info Dialog", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessageDialog(null, "Articolo inesistente", "Info Dialog", JOptionPane.ERROR_MESSAGE);
            }
        }
        initArticlePane();
    }

    private static boolean checkCodice(String codice) {

        boolean isPresente = false;
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT* FROM ARTICOLO WHERE CODICE='%s' GROUP BY CODICE HAVING COUNT(*) > 0", codice));
            if (rs.next()) isPresente = true;
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return isPresente;
    }

    // inizializzazione del campi del pannello degli articoli
    public static void initArticlePane() {
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

    public static ArrayList<Articolo> loadArticleFromDB() {
        ArrayList<Articolo> list_articles = new ArrayList<>();
        try {
            Connection con = (new ConnectionManager()).getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT* FROM ARTICOLO");

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
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        table.validate();
        table.repaint();
        return list_articles;
    }
}

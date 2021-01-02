package shop.model;

import java.util.Date;

public class Carico {
    private String codice;
    private String descrizione;
    private Date datacarico;
    private Integer quantita;
    private String fornitore;
    private String note;

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDatacarico() {
        return datacarico;
    }

    public void setDatacarico(Date datacarico) {
        this.datacarico = datacarico;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

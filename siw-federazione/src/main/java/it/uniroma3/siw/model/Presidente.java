package it.uniroma3.siw.model;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Presidente {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "presidente_generator")
    @SequenceGenerator(name = "presidente_generator", sequenceName = "presidente_seq", allocationSize = 1)
    public Long id;
    
    @Column(unique = true, nullable = false)
    @NotBlank
    public String codiceFiscale;

    @Column(nullable = false)
    @NotBlank
    public String luogoNascita;

    @OneToOne
    public Utente utente;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Presidente that = (Presidente) o;
        return Objects.equals(codiceFiscale, that.codiceFiscale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceFiscale);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
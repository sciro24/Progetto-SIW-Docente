package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"dataInizio", "dataFine","giocatore_id", "squadra_id"}))
public class Tesseramento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tesseramento_generator")
    @SequenceGenerator(name = "tesseramento_generator", sequenceName = "tesseramento_seq", allocationSize = 1)
    public Long id;

    @Column(nullable = false)
    @DateTimeFormat(pattern="dd-MM-yyyy")
    public LocalDate dataInizio;

    @Column(nullable = false)
    @DateTimeFormat(pattern="dd-MM-yyyy")
    public LocalDate dataFine;

    @ManyToOne
    public Giocatore giocatore;

    @ManyToOne
    public Squadra squadra;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Tesseramento that = (Tesseramento) o;
        return Objects.equals(dataInizio, that.dataInizio) && Objects.equals(dataFine, that.dataFine)
                && Objects.equals(squadra, that.squadra) && Objects.equals(giocatore, that.giocatore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataInizio, dataFine, squadra, giocatore);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }


    public Giocatore getGiocatore() {
        return giocatore;
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }
}
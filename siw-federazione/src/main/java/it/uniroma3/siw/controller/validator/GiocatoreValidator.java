package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Giocatore;

@Component
public class GiocatoreValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Giocatore.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Giocatore giocatore = (Giocatore) target;

        String nome = giocatore.getNome().trim();
        if (nome.isEmpty()) {
            errors.reject("NotBlank.giocatore.nome");
        } 

        String cognome = giocatore.getCognome().trim();
        if (cognome.isEmpty()) {
            errors.reject("NotBlank.giocatore.cognome");
        } 

        if (giocatore.getDataNascita() == null) {
            errors.reject("NotNull.giocatore.dataNascita");
        }

        String luogoNascita = giocatore.getLuogoNascita().trim();
        if (luogoNascita.isEmpty()) {
            errors.reject("NotBlank.giocatore.luogoNascita");
        }

        String ruolo = giocatore.getRuolo().trim();
        if (ruolo.isEmpty()) {
            errors.reject("NotBlank.giocatore.ruolo");
        }
        
        LocalDate inizio = giocatore.getInizioTesseramento();
        LocalDate fine = giocatore.getFineTesseramento();
        LocalDate oggi = LocalDate.now();

        if (inizio != null && fine != null) {
            if (fine.isBefore(inizio)) {
                errors.reject("Invalid.giocatore.fineTesseramento");
            }
            if (fine.isBefore(oggi)) {
                errors.reject("Future.giocatore.fineTesseramento");
            }
        }
    }
}

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
            errors.rejectValue("nome", "NotBlank.giocatore.nome", "Il nome è obbligatorio");
        } 

        String cognome = giocatore.getCognome().trim();
        if (cognome.isEmpty()) {
            errors.rejectValue("cognome", "NotBlank.giocatore.cognome", "Il cognome è obbligatorio");
        } 

        if (giocatore.getDataNascita() == null) {
            errors.rejectValue("dataNascita", "NotNull.giocatore.dataNascita", "La data di nascita è obbligatoria");
        }

        String luogoNascita = giocatore.getLuogoNascita().trim();
        if (luogoNascita.isEmpty()) {
            errors.rejectValue("luogoNascita", "NotBlank.giocatore.luogoNascita", "Il luogo di nascita è obbligatorio");
        }

        String ruolo = giocatore.getRuolo().trim();
        if (ruolo.isEmpty()) {
            errors.rejectValue("ruolo", "NotBlank.giocatore.ruolo", "Il ruolo è obbligatorio");
        }

        if (giocatore.getInizioTesseramento() != null && giocatore.getFineTesseramento() != null) {
            if (giocatore.getFineTesseramento().isBefore(giocatore.getInizioTesseramento())) {
                errors.rejectValue("fineTesseramento", "Invalid.giocatore.fineTesseramento", "La data di fine tesseramento deve essere successiva alla data di inizio");
            }
            if (giocatore.getFineTesseramento().isBefore(LocalDate.now())) {
                errors.rejectValue("fineTesseramento", "Future.giocatore.fineTesseramento", "La data di fine tesseramento deve essere nel futuro");
            }
        }
    }
}

package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Utente;

@Component
public class UtenteValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Utente.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    	
    	Utente utente = (Utente) target;
    	
        if (utente.getNome() == null || utente.getNome().trim().isEmpty()) {
            errors.reject("NotBlank.utente.nome");
        }

        if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) {
            errors.reject("NotBlank.utente.cognome");
        }

        if (utente.getDataNascita() == null) {
            errors.reject("NotNull.utente.dataNascita");
        } else if (utente.getDataNascita().isAfter(LocalDate.now())) {
        }

        if (utente.getEmail() == null || utente.getEmail().trim().isEmpty()) {
            errors.reject("NotBlank.utente.email");
        }
    }
}

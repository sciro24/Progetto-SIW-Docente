package it.uniroma3.siw.controller.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;

@Component
public class SquadraValidator implements Validator {

	@Autowired SquadraService squadraService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Squadra.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Squadra squadra = (Squadra) target;


		if (squadra.getNome() == null || squadra.getNome().trim().isEmpty()) {
		    errors.reject("NotBlank.squadra.nome");
		}

		if (squadra.getAnnoFondazione() == null || squadra.getAnnoFondazione().trim().isEmpty()) {
		    errors.reject("NotBlank.squadra.annoFondazione");
		} else if (!squadra.getAnnoFondazione().matches("\\d+")) {
		    System.out.println("Errore: Anno di fondazione non numerico");
		    errors.reject("Numeric.squadra.annoFondazione");
		}

		if (squadra.getIndirizzoSede() == null || squadra.getIndirizzoSede().trim().isEmpty()) {
		    errors.reject("NotBlank.squadra.indirizzoSede");
		}


	}

}

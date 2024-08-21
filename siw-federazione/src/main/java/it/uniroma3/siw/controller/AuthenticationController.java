package it.uniroma3.siw.controller;


import static it.uniroma3.siw.model.Credenziali.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credenziali.PRESIDENT_ROLE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {


	@Autowired private CredenzialiService credenzialiService;
	@Autowired private CredenzialiValidator credenzialiValidator;
	@Autowired private UtenteService utenteService;
	@Autowired private UtenteValidator utenteValidator;
	@Autowired private SquadraService squadraService;
	@Autowired private PresidenteService presidenteService;	

	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "formRegisterUser";
	}

	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index.html";
		}
		else {
			UserDetails  utenteDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credenziali credenziali = this.credenzialiService.getCredenziali(utenteDetails.getUsername());
			if (credenziali.getRole().equals(ADMIN_ROLE)) {
				model.addAttribute("utente", credenziali.getUtente());
				return "admin/indexAdmin.html";
			}
			if(credenziali.getRole().equals(PRESIDENT_ROLE)) {
				Utente utente = this.utenteService.findByEmail(credenziali.getUsername());
				Presidente presidente = this.presidenteService.findByUtente(utente);
				Squadra squadra = this.squadraService.findByPresidente(presidente);
				model.addAttribute("squadra", squadra);
				model.addAttribute("presidente", presidente);
				model.addAttribute("nome", squadra.getNome().toUpperCase());
				return "presidente/indexPresidente.html";
			}
		}
		List<Squadra> squadre = this.squadraService.findAll();
		model.addAttribute("squadre", squadre);
		return "indexUtente.html";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {

		UserDetails utenteDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali = this.credenzialiService.getCredenziali(utenteDetails.getUsername());
		if (credenziali.getRole().equals(ADMIN_ROLE)) {
			model.addAttribute("utente", credenziali.getUtente());
			return "admin/indexAdmin.html";
		}
		if(credenziali.getRole().equals(PRESIDENT_ROLE)) {
			Utente utente = this.utenteService.findByEmail(credenziali.getUsername());
			Presidente presidente = this.presidenteService.findByUtente(utente);
			Squadra squadra = this.squadraService.findByPresidente(presidente);
			model.addAttribute("squadra", squadra);
			model.addAttribute("presidente", presidente);
			model.addAttribute("nome", squadra.getNome().toUpperCase());
			return "presidente/indexPresidente.html";
		}
		List<Squadra> squadre = this.squadraService.findAll();
		model.addAttribute("squadre", squadre);
		return "indexUtente.html";
	}

	@PostMapping(value = { "/register" })
	public String registerUtente(@Valid @ModelAttribute("utente") Utente utente,
			BindingResult utenteBindingResult, @Valid
			@ModelAttribute("credenziali") Credenziali credenziali,
			BindingResult credenzialiBindingResult,
			Model model) {

		// se utente e credential hanno entrambi contenuti validi, memorizza utente e the Credenziali nel DB

		this.utenteValidator.validate(utente, utenteBindingResult);
		this.credenzialiValidator.validate(credenziali, credenzialiBindingResult);
		if(!utenteBindingResult.hasErrors() || !credenzialiBindingResult.hasErrors()) {
			String username = credenziali.getUsername();
			utente.setEmail(username);

			credenziali.setRole(Credenziali.DEFAULT_ROLE);

			this.utenteService.saveUser(utente);
			credenziali.setUtente(utente);
			this.credenzialiService.save(credenziali);
			model.addAttribute("utente", utente);
			return "registrationSuccessful.html";
		}
		model.addAttribute("utente", utente);
		model.addAttribute("credenziali", credenziali);
		return "formRegisterUser.html";
	}
}


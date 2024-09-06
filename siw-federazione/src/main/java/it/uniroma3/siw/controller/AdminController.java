package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.Credenziali.PRESIDENT_ROLE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.controller.validator.PresidenteValidator;
import it.uniroma3.siw.controller.validator.SquadraValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.TesseramentoService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

@Controller
public class AdminController {

	@Autowired PresidenteService presidenteService;
	@Autowired GiocatoreService giocatoreService;
	@Autowired SquadraRepository squadraRepository;
	@Autowired SquadraService squadraService;
	@Autowired UtenteService utenteService;
	@Autowired CredenzialiService credenzialiService;
	@Autowired TesseramentoService tesseramentoService;
	@Autowired EntityManager entityManager;
	@Autowired UtenteValidator utenteValidator;
	@Autowired CredenzialiValidator credenzialiValidator;
	@Autowired PresidenteValidator presidenteValidator;
	@Autowired SquadraValidator squadraValidator;

	@GetMapping("/admin/formNewPresidente")
	public String formNewPresidente(Model model) {
		model.addAttribute("presidente", new Presidente());
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "admin/formNewPresidente.html";
	}

	@PostMapping("/admin/presidente")
	public String newPresidente(@Valid @ModelAttribute("utente") Utente utente, @Valid @ModelAttribute("credenziali") Credenziali credenziali, @Valid @ModelAttribute("presidente")Presidente presidente, BindingResult utenteBindingResult, BindingResult credenzialiBindingResult, BindingResult presidenteBindingResult, Model model) {
		String username = credenziali.getUsername();
		utente.setEmail(username);
		this.utenteValidator.validate(utente, utenteBindingResult);
		this.credenzialiValidator.validate(credenziali, credenzialiBindingResult);
		this.presidenteValidator.validate(presidente, presidenteBindingResult);
		if(!utenteBindingResult.hasErrors() || !credenzialiBindingResult.hasErrors() || !presidenteBindingResult.hasErrors()) {
			credenziali.setRole(PRESIDENT_ROLE);
			credenziali.setUtente(utente);
			presidente.setUtente(utente);
			this.utenteService.saveUser(utente);
			this.presidenteService.save(presidente);
			this.credenzialiService.save(credenziali);
			model.addAttribute("utente", utente);
			model.addAttribute("credenziali", credenziali);
			model.addAttribute("presidente", presidente);
			return "admin/presidenteCreato.html";
		}
		model.addAttribute("utente", utente);
		model.addAttribute("credenziali", credenziali);
		model.addAttribute("presidente", presidente);
		return "/admin/formNewPresidente.html";
	}

	@GetMapping("/admin/formNewGiocatore")
	public String formNewGiocatore(Model model) {
		model.addAttribute("giocatore", new Presidente());
		return "admin/formNewGiocatore.html";
	}

	@PostMapping("/admin/giocatore")
	public String newGiocatore(@Valid @ModelAttribute("giocatore") Giocatore giocatore, BindingResult result, Model model) {

		// Verifica la presenza di errori di validazione
		if (result.hasErrors()) {
			return "/admin/formNewGiocatore.html"; // Torna al form in caso di errori
		}

		// Salva il nuovo giocatore nel database
		giocatoreService.save(giocatore);
		model.addAttribute("giocatore", giocatore);
		return "admin/giocatoreCreato.html";
	}



	@GetMapping("/admin/formNewSquadra")
	public String formNewSquadra(Model model) {
		model.addAttribute("squadra", new Squadra());
		return "admin/formNewSquadra.html";
	}

	@PostMapping("/admin/squadra")
	public String newSquadra(@Valid @ModelAttribute("squadra") Squadra squadra, BindingResult bindingResult, Model model) {
		this.squadraValidator.validate(squadra, bindingResult);
		if(bindingResult.hasErrors()) {
			return "admin/formNewSquadra.html";
		}
		this.squadraService.save(squadra);
		model.addAttribute("squadra", squadra);
		return "admin/squadraCreata.html";
	}


	@GetMapping("/admin/formModificaSquadra")
	public String formModificaSquadra(@RequestParam(value = "id", required = false) Long id, Model model){
		List<Squadra> squadre = squadraService.findAll();

		// Se non è stata selezionata nessuna squadra, crea un nuovo oggetto Squadra
		Squadra squadra;
		if (id != null) {
			squadra = squadraRepository.findById(id).orElse(new Squadra());
		} else {
			squadra = new Squadra();  // Oggetto vuoto per inizializzare il form
		}
	    model.addAttribute("squadra", squadra);
		model.addAttribute("squadre", squadre);
		return "admin/formModificaSquadra.html";
	}

	@PostMapping("/admin/modificaSquadra")
	public String modificaSquadra(@Valid @ModelAttribute("squadra") Squadra squadra, BindingResult result, Model model) {

			if (result.hasErrors()) {
				List<Squadra> squadre = squadraRepository.findAll();
				model.addAttribute("squadre", squadre);
				return "admin/formModificaSquadra.html"; 
			}

			squadraRepository.save(squadra);
			return "admin/squadraModificata.html"; // Redireziona alla lista di squadre o a una pagina di successo
		}
	



	@GetMapping("/admin/formAssegnaPresidente")
	public String formAssegnaPresidente(Model model) {
		model.addAttribute("squadre", this.squadraService.findSenzaPresidente());
		model.addAttribute("presidenti", this.presidenteService.findLiberi());
		model.addAttribute("errore", null);
		return "admin/formAssegnaPresidente.html";
	}

	@PostMapping("/admin/assegnaPresidente")
	public String assegnaPresidente(@RequestParam("id") String id, @RequestParam("codiceFiscale") String codiceFiscale, Model model) {
		if (codiceFiscale.trim().isEmpty() || id.trim().isEmpty()) {
			model.addAttribute("errore", "I due campi sono obbligatori!");
			return "admin/formAssegnaPresidente.html";
		}
		Squadra squadra = this.squadraService.findById(Long.valueOf(id));
		Presidente presidente = this.presidenteService.findByCodiceFiscale(codiceFiscale);
		squadra.setPresidente(presidente);
		this.squadraService.save(squadra);
		model.addAttribute("squadra", squadra);
		return "admin/presidenteAssegnato.html";
	}


	//    @GetMapping("/admin/formCambiaSquadra")
	//    public String formCambiaPresidente(Model model) {
	//        model.addAttribute("squadre", this.squadraService.findSenzaPresidente());
	//        model.addAttribute("presidenti", this.presidenteService.findOccupati());
	//        model.addAttribute("errore", null);
	//        return "admin/formCambiaSquadra.html";
	//    }
	//
	//    @PostMapping("/admin/cambiaSquadra")
	//    public String cambiaPresidente(@RequestParam("id") String id, @RequestParam("codiceFiscale") String codiceFiscale, Model model) {
	//        if (codiceFiscale.trim().isEmpty()) {
	//            model.addAttribute("errore", "Presidente è un campo obbligatorio!");
	//            return "admin/formCambiaSquadra.html";
	//        }
	//        Presidente presidente = this.presidenteService.findByCodiceFiscale(codiceFiscale);
	//        Squadra vecchia = this.squadraService.findByPresidente(presidente);
	//       if (!id.trim().isEmpty()) {
	//           Squadra nuova = this.squadraService.findById(Long.valueOf(id));
	//           nuova.setPresidente(presidente);
	//           this.squadraService.save(nuova);
	//       }
	//        vecchia.setPresidente(null);
	//        this.squadraService.save(vecchia);
	//       return "admin/squadraAssegnata.html";
	//    }


}
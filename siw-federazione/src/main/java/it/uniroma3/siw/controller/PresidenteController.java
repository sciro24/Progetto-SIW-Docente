package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.persistence.EntityManager;

@Controller
public class PresidenteController {

	@Autowired PresidenteService presidenteService;
	@Autowired GiocatoreService giocatoreService;
	@Autowired SquadraService squadraService;
	@Autowired SquadraRepository squadraRepository;
	@Autowired GiocatoreRepository giocatoreRepository;
	@Autowired UtenteService utenteService;
	@Autowired CredenzialiService credenzialiService;
	@Autowired EntityManager entityManager;


	private boolean controllaPresidente(Squadra squadra) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utente = this.utenteService.findByUsername(userDetails.getUsername());
		Presidente presidente = this.presidenteService.findByUtente(utente);
		return squadra.getPresidente().equals(presidente);
	}

	@GetMapping("/presidente/{id}/gestisciSquadra")
	public String gestisciSquadra(@PathVariable("id")Long id, Model model)  {
		Squadra squadra = this.squadraService.findById(id);
		model.addAttribute("squadra", squadra);
		if (!this.controllaPresidente(squadra)) {
			return "presidente/accessoNegato.html";
		}
		return "presidente/gestisciSquadra.html";
	}

	@GetMapping("presidente/{id}/formTesseraGiocatore")
	public String formTesseraGiocatore(@PathVariable("id") Long id, Model model) {
		// Trova la squadra con l'ID fornito
		Squadra squadra = squadraService.findById(id);

		// Verifica se la squadra esiste
		if (squadra == null) {
			model.addAttribute("error", "Squadra non trovata.");
			return "presidente/erroreTesseramento.html"; // Pagina di errore generica
		}

		// Verifica se il presidente ha accesso alla squadra
		if (!controllaPresidente(squadra)) {
			return "presidente/accessoNegato.html";
		}

		// Trova i giocatori non tesserati
		List<Giocatore> giocatoriLiberi = giocatoreService.findLiberi();


		// Aggiungi i dati al modello
		model.addAttribute("squadra", squadra);
		model.addAttribute("giocatoriLiberi", giocatoriLiberi);

		// Restituisce il nome del template
		return "presidente/formTesseraGiocatore.html";
	}

	@PostMapping("presidente/{id}/tesseraGiocatore")
	public String tesseraGiocatore(@PathVariable("id") Long id,@RequestParam("giocatoreId") Long giocatoreId,@RequestParam("inizioTesseramento") String inizioTesseramento,@RequestParam("fineTesseramento") String fineTesseramento, Model model) {

		// Trova la squadra con l'ID fornito
		Squadra squadra = squadraService.findById(id);

		// Verifica se la squadra esiste
		if (squadra == null) {
			model.addAttribute("error", "Squadra non trovata.");
			return "presidente/error.html"; // Pagina di errore generica
		}

		// Verifica se il presidente ha accesso alla squadra
		if (!controllaPresidente(squadra)) {
			return "presidente/accessoNegato.html";
		}
		// Trova il giocatore da tesserare
		Giocatore giocatore = giocatoreService.findById(giocatoreId);

		// Converti le date
		LocalDate inizio = LocalDate.parse(inizioTesseramento);
		LocalDate fine = LocalDate.parse(fineTesseramento);

		// Tesseramento del giocatore
		giocatore.setSquadra(squadra);
		giocatore.setInizioTesseramento(inizio);
		giocatore.setFineTesseramento(fine);
		giocatoreService.save(giocatore);

		return "presidente/giocatoreTesserato.html";

	}




	@GetMapping("/presidente/{id}/formSvincolaGiocatore")
	public String FormSvincolaGiocatore(@PathVariable("id") Long id, Model model) {
		// Trova la squadra con l'ID fornito
		Squadra squadra = squadraService.findById(id);

		// Verifica se la squadra esiste
		if (squadra == null) {
			model.addAttribute("error", "Squadra non trovata.");
			return "presidente/error.html"; // Pagina di errore generica
		}

		// Verifica se il presidente ha accesso alla squadra
		if (!controllaPresidente(squadra)) {
			return "presidente/accessoNegato.html";
		}

		// Trova i giocatori tesserati nella squadra
		List<Giocatore> giocatoriTesserati = giocatoreService.findBySquadra(squadra);

		// Aggiungi i dati al modello
		model.addAttribute("squadra", squadra);
		model.addAttribute("giocatoriTesserati", giocatoriTesserati);
		//		model.addAttribute("presidenteId", presidenteId);

		return "presidente/formSvincolaGiocatore.html";
	}


	@PostMapping("presidente/{id}/svincolaGiocatore")
	public String svincolaGiocatore(@PathVariable("id") Long id, @RequestParam("giocatoreId") Long giocatoreId, Model model) {

		// Trova la squadra con l'ID fornito
		Squadra squadra = squadraService.findById(id);

		// Verifica se la squadra esiste
		if (squadra == null) {
			model.addAttribute("error", "Squadra non trovata.");
			return "presidente/error.html"; // Pagina di errore generica
		}

		// Verifica se il presidente ha accesso alla squadra
		if (!controllaPresidente(squadra)) {
			return "presidente/accessoNegato.html";
		}

		// Trova il giocatore da svincolare
		Giocatore giocatore = giocatoreService.findById(giocatoreId);

		// Verifica se il giocatore è tesserato nella squadra
		if (giocatore.getSquadra() != null && giocatore.getSquadra().equals(squadra)) {
			// Rimuovi il giocatore dalla squadra e aggiorna il tesseramento
			giocatore.setSquadra(null);
			giocatore.setFineTesseramento(LocalDate.now());  // Setta la data di fine tesseramento a oggi
			giocatoreService.save(giocatore);
		} else {
			throw new RuntimeException("Il giocatore non è tesserato nella tua squadra.");
		}

		return "presidente/giocatoreSvincolato.html";
	}


}
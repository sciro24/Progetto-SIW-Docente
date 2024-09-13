package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
	
	public Optional<Utente> findById(Long id);

    public boolean existsByEmail(String email);

    public Utente findByEmail(String email);

    public List<Utente> findAll();

}

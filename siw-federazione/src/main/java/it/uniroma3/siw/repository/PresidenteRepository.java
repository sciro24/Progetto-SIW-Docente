package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Utente;


public interface PresidenteRepository extends CrudRepository<Presidente, Long> {

    public Presidente save(Presidente presidente);

    public boolean existsByCodiceFiscale(String codiceFiscale);

    public Presidente findByCodiceFiscale(String codiceFiscale);

    public Presidente findByUtente(Utente utente);

    public List<Presidente> findAll();

    public Optional<Presidente> findById(Long id);

}

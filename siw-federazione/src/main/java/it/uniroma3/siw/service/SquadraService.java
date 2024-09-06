package it.uniroma3.siw.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.SquadraRepository;
import jakarta.persistence.EntityManager;


@Service
public class SquadraService {

    @Autowired SquadraRepository squadraRepository;
    @Autowired EntityManager entityManager;

    @Transactional
    public Squadra save(Squadra squadra) {
       return this.squadraRepository.save(squadra);
    }

    public List<Squadra> findAll() {
        return this.squadraRepository.findAll();
    }

    public boolean existsByPresidente(Presidente presidente) {
        return this.squadraRepository.existsByPresidente(presidente);
    }

    public Squadra findByPresidente(Presidente presidente) {
        return this.squadraRepository.findByPresidente(presidente);
    }

    public Squadra findById(Long id) {
        return this.squadraRepository.findById(id).orElse(null);
    }

    public List<Squadra> findSenzaPresidente() {
        List<Squadra> tutte = this.findAll();
        List<Squadra> senza = new ArrayList<>();

        for (Squadra squadra : tutte) {
            if (squadra.getPresidente() == null){
                senza.add(squadra);
            }
        }
        return senza;
    }
}
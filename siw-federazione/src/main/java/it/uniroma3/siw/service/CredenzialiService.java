package it.uniroma3.siw.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.repository.CredenzialiRepository;

@Service
public class CredenzialiService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredenzialiRepository credenzialiRepository;
    
    public Credenziali findByUsername(String username) {
		Optional<Credenziali> user = this.credenzialiRepository.findByUsername(username);
		
		return user.orElse(null);
	}
    
    @Transactional
    public Credenziali getCredenziali(Long id) {
        Optional<Credenziali> result = this.credenzialiRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credenziali getCredenziali(String username) {
        Optional<Credenziali> result = this.credenzialiRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Credenziali save(Credenziali credenziali) {
        credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
        return this.credenzialiRepository.save(credenziali);
    }
    
    public List<Credenziali> findAll() {
        return this.credenzialiRepository.findAll();
    }

    public void delete(Credenziali credenziali) {
        this.credenzialiRepository.delete(credenziali);
    }
}

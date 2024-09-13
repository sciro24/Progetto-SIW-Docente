package it.uniroma3.siw.model;


import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Credenziali {

	public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String PRESIDENT_ROLE = "PRESIDENTE";
	public static final String ADMIN_ROLE = "AMMINISTRATORE";


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credenziali_generator")
	@SequenceGenerator(name = "credenziali_generator", sequenceName = "credenziali_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
	@NotBlank
	private String username;

	@NotBlank
	private String password;
	
	
	private String role;

	@OneToOne(cascade = CascadeType.ALL)
	private Utente utente;

	public String getUsername() {
		return username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credenziali other = (Credenziali) obj;
		return Objects.equals(id, other.id) && Objects.equals(username, other.username)
				&& Objects.equals(utente, other.utente);
	}

}

package it.uniroma3.siw.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Credenziali {

	public static final String PRESIDENT_ROLE = "PRESIDENT";
	public static final String ADMIN_ROLE = "ADMIN";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credenziali_generator")
	@SequenceGenerator(name = "credenziali_generator", sequenceName = "credenziali_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
	@NotBlank(message = "Username non può essere vuoto")
	@Size(min = 3, max = 50, message = "Username deve avere tra 3 e 50 caratteri")
	private String username;

	@NotBlank(message = "Password non può essere vuota")
    @Size(min = 3, message = "Password deve avere almeno 3 caratteri")
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

}

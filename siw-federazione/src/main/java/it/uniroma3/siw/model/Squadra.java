package it.uniroma3.siw.model;


import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Squadra {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "squadra_generator")
    @SequenceGenerator(name = "squadra_generator", sequenceName = "squadra_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private String annoFondazione;

    @NotBlank
    private String indirizzoSede;
    
	private String urlOfPicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "presidente_id", referencedColumnName = "id")
    private Presidente presidente;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAnnoFondazione() {
		return annoFondazione;
	}

	public void setAnnoFondazione(String annoFondazione) {
		this.annoFondazione = annoFondazione;
	}

	public String getIndirizzoSede() {
		return indirizzoSede;
	}

	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}

	public Presidente getPresidente() {
		return presidente;
	}

	public void setPresidente(Presidente presidente) {
		this.presidente = presidente;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, indirizzoSede, nome, presidente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Squadra other = (Squadra) obj;
		return Objects.equals(id, other.id) && Objects.equals(indirizzoSede, other.indirizzoSede)
				&& Objects.equals(nome, other.nome) && Objects.equals(presidente, other.presidente);
	}

	public String getUrlOfPicture() {
		return urlOfPicture;
	}

	public void setUrlOfPicture(String urlOfPicture) {
		this.urlOfPicture = urlOfPicture;
	}
    
}
package edu.uclm.esi.ds.games.entities;


import javax.validation.constraints.NotEmpty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", indexes = { @Index(columnList = "name", unique = true),
		 })
public class User {
	@Id
	@Column(length = 100)
	@NotEmpty
	private String name;

	public User() {
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}



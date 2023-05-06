package edu.uclm.esi.ds.games.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player extends User {

	public Player(String id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	@JsonIgnore
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

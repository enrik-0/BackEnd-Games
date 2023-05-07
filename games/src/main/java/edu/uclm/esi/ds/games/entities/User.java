package edu.uclm.esi.ds.games.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(
		schema = "account",
		name = "users")
public abstract class User {
	@Id
	protected String id;
	protected String name;
	protected String email;
	protected String pwd;
	
	
	public abstract String getId();
	public abstract void setId(String id);
	public abstract String getName();
	public abstract void setName(String name);
}

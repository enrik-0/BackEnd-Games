package edu.uclm.esi.ds.games.entities;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(
		schema = "account",
		name = "users")
public abstract class User {
	@Id @Column(length = 36)
	protected String id;
	@Column(length = 100)
	@NotEmpty
	protected String name;
	@Column(length = 140)
	@NotEmpty
	protected String email;
	@NotEmpty
	protected String pwd;
	
	
	public abstract String getId();
	public abstract void setId(String id);
	public abstract String getName();
	public abstract void setName(String name);
}

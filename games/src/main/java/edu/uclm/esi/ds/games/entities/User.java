package edu.uclm.esi.ds.games.entities;

public abstract class User {
	protected String id;
	protected String name;
	protected String email;
	
	abstract String getId();
	abstract void setId(String id);
	abstract String getName();
	abstract void setName(String name);
}

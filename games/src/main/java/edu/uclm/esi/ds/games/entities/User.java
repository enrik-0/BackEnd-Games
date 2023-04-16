package edu.uclm.esi.ds.games.entities;

public abstract class User {
	protected String id;
	protected String name;
	protected String email;
	
	public abstract String getId();
	public abstract void setId(String id);
	public abstract String getName();
	public abstract void setName(String name);
}

package edu.uclm.esi.ds.games.domain;

import java.util.HashMap;
import java.util.List;

public abstract class Match {

	String id;
	boolean ready;
	List<String> players;
	HashMap<String, Board> boards;
	
	abstract void addPlayer(String player);

	public List<Board> getBoards() {

		return this.boards.values().stream().toList();

	}
	abstract void buildBoards();
	
	
}

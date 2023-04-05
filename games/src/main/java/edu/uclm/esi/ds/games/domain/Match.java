package edu.uclm.esi.ds.games.domain;

import java.util.HashMap;
import java.util.List;

public abstract class Match {
	protected String id;
	protected boolean ready;
	protected List<String> players;
	protected HashMap<String, Board> boards;
	
	
	abstract public String getId();
	abstract public List<String> getPlayers();
	abstract public boolean isReady();
	abstract public void addPlayer(String player);
	abstract protected void buildBoards();
	abstract public List<Board> getBoards();
}

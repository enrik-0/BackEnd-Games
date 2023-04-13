package edu.uclm.esi.ds.games.domain;

import java.util.HashMap;
import java.util.List;

import edu.uclm.esi.ds.games.entities.Player;

public abstract class Match {
	protected String id;
	protected boolean ready;
	protected List<Player> players;
	// { "player_id": "board" }
	protected HashMap<String, Board> boards;
	
	
	abstract public String getId();
	abstract public List<Player> getPlayers();
	abstract public List<Board> getBoards();
	abstract public boolean isReady();
	abstract public void addPlayer(Player player);
	abstract protected void buildBoards();
}

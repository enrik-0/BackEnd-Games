package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import edu.uclm.esi.ds.games.entities.User;

public abstract class Match {
	protected String id;
	protected boolean ready;
	protected List<User> players;
	// { "player_id": "board" }
	protected HashMap<String, Board> boards;
	// { "player_id": Movements }
	protected HashMap<String, List<Movement>> movements;
	
	
	public abstract String getId();
	public abstract List<User> getPlayers();
	public abstract Board getPlayerBoard(String userId);
	public abstract List<Board> getBoards();
	public abstract boolean isReady();
	public abstract void addPlayer(User player);
	protected abstract void buildBoards();
	public abstract boolean isValidMovement(String userId, int i, int j) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	public abstract boolean updateUserBoard(String userId, int i, int j);
}

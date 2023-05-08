package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import edu.uclm.esi.ds.games.entities.MatchPlayer;
import edu.uclm.esi.ds.games.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(
		schema = "games",
		name = "matches")
public abstract class Match {
	@Id
	protected String id;
	@Transient
	protected boolean ready;

	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    protected List<MatchPlayer> players;
	// { "player_id": "board" }
	@Transient
	protected HashMap<String, Board> boards;
	// { "player_id": Movements }

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    protected List<MatchUserPosition> movements;
	
	
	public abstract String getId();
	public abstract List<MatchPlayer> getPlayers();
	public abstract MatchPlayer getPlayerById(String userId);
	public abstract Board getPlayerBoard(String userId);
	public abstract List<String> getPlayersNames();
	public abstract List<Board> getBoards();
	public abstract List<MatchUserPosition> getMovements();
	public abstract boolean isReady();
	public abstract void addPlayer(User player);
	protected abstract void buildBoards();
	public abstract boolean isValidMovement(User user, int i, int j) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	public abstract boolean updateUserBoard(String userId, int i, int j);
}

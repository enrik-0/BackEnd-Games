package edu.uclm.esi.ds.games.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.uclm.esi.ds.games.domain.Board;
import edu.uclm.esi.ds.games.domain.MovementNM;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table (schema = "games",
		name = "nm")
@PrimaryKeyJoinColumn(name = "id")
public class NumberMatch extends Match {
	@Transient
	private byte numbersAdded = 0;
	public NumberMatch() {
		this.id = UUID.randomUUID().toString();
		this.players = new LinkedList<>();
		this.boards = new HashMap<>();
		this.movements = new LinkedList<>();
	}

	public String getId() {
		return id;
	}

	public boolean isReady() {
		return this.ready;
	}
	
	@JsonIgnore
	public List<MatchPlayer> getPlayers() {
		return this.players;
	}

	@Override
	@JsonIgnore
	public MatchPlayer getPlayerById(String userId) {
		MatchPlayer player = null;

		for (MatchPlayer p : this.getPlayers()) {
			if (p.getId().getPlayer().equals(userId)) {
				player = p;
			}
		}

		return player;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
		if (this.ready)
			this.buildBoards();
	}

	public void addPlayer(User player) {
		this.players.add(new MatchPlayer(this, player));
		if (this.players.size() == 2)
			setReady(true);
	}

	public void buildBoards() {
		Board board = new Board();
		String player = this.players.get(0).getId().getPlayer();
		this.boards.put(player, board);

		player = this.players.get(1).getId().getPlayer();
		this.boards.put(player, board.copy());
	}

	@Override
	@JsonIgnore
	public Board getPlayerBoard(String userId) {
		return this.boards.get(userId);
	}

	@Override
	@JsonIgnore
	public List<String> getPlayersNames() {
		List<String> names = new ArrayList<String>();

		for (MatchPlayer user : this.players) {
			names.add(user.getPlayer().getName());
		}
		
		return names;
	}
	
	@JsonIgnore
	public List<Board> getBoards() {
		return this.boards.values().stream().toList();
	}

	/**
	 * Checks if user movement is legal and save it if true.
	 * 
	 * @param userId ID of the player
	 * @param i position of first number
	 * @param j position of second number
	 * @return true if is valid, false otherwise.
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public boolean isValidMovement(User user, int i, int j) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MovementNM move = new MovementNM(i, j);
		boolean isValid = false;

		if (move.isValid(this.getPlayerBoard(user.getId()).getDigits())) {
			MatchUserPosition movement = new MatchUserPosition(this, user, move);
			this.movements.add(movement);
			isValid = true;
		}
		
		return isValid;
	}
	

	/**
	 * Updates the board of a user given the two positions of the numbers.
	 * 
	 * @param userId: Identifier of the player
	 * @param i: position of the first number
	 * @param j: position of the second number
	 * 
	 * @return If user wins true, otherwise false.
	 */
	@Override
	public boolean updateUserBoard(String userId, int i, int j) {
		boolean isWin = false;
		Board board = this.boards.get(userId);

		if (board != null) {
			isWin = board.updateBoard(i, j);
		}
		
		return isWin;
	}

	@Override
	public List<MatchUserPosition> getMovements() {
		return this.movements;
	}

	public byte getNumbersAdded() {
		return numbersAdded;
	}

	public void setNumbersAdded(byte numbersAdded) {
		this.numbersAdded = numbersAdded;
	}
}

package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import edu.uclm.esi.ds.games.entities.Player;
import edu.uclm.esi.ds.games.entities.User;

public class NumberMatch extends Match {
	public NumberMatch() {
		this.id = UUID.randomUUID().toString();
		this.players = new LinkedList<>();
		this.boards = new HashMap<>();
		this.movements = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public boolean isReady() {
		return this.ready;
	}

	public List<User> getPlayers() {
		return this.players;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
		if (this.ready)
			this.buildBoards();
	}

	public void addPlayer(User player) {
		this.players.add(player);
		if (this.players.size() == 2)
			setReady(true);
	}

	protected void buildBoards() {
		Board board = new Board();
		this.boards.put(this.players.get(0).getId(), board);
		this.boards.put(this.players.get(1).getId(), board.copy());
	}

	public Board getPlayerBoard(String userId) {
		return this.boards.get(userId);
	}
	
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
	public boolean isValidMovement(String userId, int i, int j) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Movement move = new MovementNM(i, j);
		boolean isValid = false;

		if (move.isValid(this.getPlayerBoard(userId).getDigits())) {
			if (this.movements.get(userId) == null) {
				this.movements.put(userId, new ArrayList<>());
			}
			this.movements.get(userId).add(move);
			isValid = true;
		}
		
		return isValid;
	}
}

package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@JsonIgnore
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

	@Override
	@JsonIgnore
	public Board getPlayerBoard(String userId) {
		return this.boards.get(userId);
	}

	@Override
	public List<String> getPlayersNames() {
		List<String> names = new ArrayList<String>();

		for (User user : this.players) {
			names.add(user.getName());
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
}

package edu.uclm.esi.ds.games.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Match {

	private String id;
	private boolean ready;
	private List<String> players;
	private HashMap<String, Board> boards;

	public Match() {
		this.id = UUID.randomUUID().toString();
		this.players = new LinkedList<>();
		this.boards = new HashMap<>();

	}

	public String getId() {
		return id;
	}

	public boolean isReady() {

		return this.ready;
	}

	public List<String> getPlayers() {
		return this.players;
	}

	public List<Board> getBoards() {
		return this.boards.values().stream().toList();

	}

	public void setReady(boolean ready) {
		this.ready = ready;
		if (this.ready)
			this.buildBoards();
	}

	public void addPlayer(String player) {
		this.players.add(player);
		if (this.players.size() == 2)
			setReady(true);
	}

	private void buildBoards() {
		Board board = new Board();
		this.boards.put(this.players.get(0), board);
		this.boards.put(this.players.get(1), board.copy());

	}

}

package edu.uclm.esi.ds.games.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import edu.uclm.esi.ds.games.entities.Player;

public class NumberMatch extends Match {
	public NumberMatch() {
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

	public List<Player> getPlayers() {
		return this.players;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
		if (this.ready)
			this.buildBoards();
	}

	public void addPlayer(Player player) {
		this.players.add(player);
		if (this.players.size() == 2)
			setReady(true);
	}

	protected void buildBoards() {
		Board board = new Board();
		this.boards.put(this.players.get(0).getId(), board);
		this.boards.put(this.players.get(1).getId(), board.copy());
	}

	public List<Board> getBoards() {
		return this.boards.values().stream().toList();
	}
}

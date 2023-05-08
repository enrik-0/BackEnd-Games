package edu.uclm.esi.ds.games.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MatchPlayerId implements Serializable {

	private static final long serialVersionUID = 1L;
    
    @Column(name = "match")
    private String match;
    
    @Column(name = "player", length = 36)
    private String player;

    public MatchPlayerId(){};
    public MatchPlayerId(String match, String player) {

    	this.match = match;
    	this.player = player;
    }

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}
    


}

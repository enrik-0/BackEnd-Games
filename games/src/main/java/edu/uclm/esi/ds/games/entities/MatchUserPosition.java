package edu.uclm.esi.ds.games.entities;
import edu.uclm.esi.ds.games.domain.MovementNM;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "games", name = "match_positions")
public class MatchUserPosition {
	@EmbeddedId
	private MatchPlayerId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match", insertable = false, updatable = false)
    private Match match;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player", insertable = false, updatable = false)
    private User player;
	

    @Column(name = "position1")
    private byte position1;

    @Column(name = "position2")
    private byte position2;

    public MatchUserPosition() {}

    public MatchUserPosition(Match match, User player, MovementNM move) {
    	this.match = match;
    	this.player = player;
    	this.position1 = move.getFirst();
    	this.position2 = move.getSecond();
    	this.id = new MatchPlayerId(match.getId(), player.getId());
    	
    }
    
	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public User getPlayer() {
		return player;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public int getPosition1() {
		return position1;
	}

	public void setPosition1(int position1) {
		this.position1 = (byte) position1;
	}

	public int getPosition2() {
		return position2;
	}

	public void setPosition2(int position2) {
		this.position2 = (byte) position2;
	}

}

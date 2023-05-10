package edu.uclm.esi.ds.games.entities;
import edu.uclm.esi.ds.games.domain.Match;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
    schema = "games",
    name = "match_players"
)
public class MatchPlayer {
    @EmbeddedId
    private MatchPlayerId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match", insertable = false, updatable = false)
    private Match match;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player", insertable = false, updatable = false)
    private User player;
    
    private boolean winner = false;
    
    public MatchPlayer(){};
    public MatchPlayer(Match match, User player) {
    	this.id = new MatchPlayerId(match.getId(), player.getId());
    	this.match = match;
    	this.player = player;
    }
    
    public MatchPlayerId getId() {
        return id;
    }
    
    public void setId(MatchPlayerId id) {
        this.id = id;
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
	public boolean isWinner() {
		return winner;
	}
	public void setWinner(boolean winner) {
		this.winner = winner;
	}
}


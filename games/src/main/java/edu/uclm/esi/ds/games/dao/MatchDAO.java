package edu.uclm.esi.ds.games.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.uclm.esi.ds.games.domain.Match;

public interface MatchDAO extends JpaRepository<Match, String> {


}

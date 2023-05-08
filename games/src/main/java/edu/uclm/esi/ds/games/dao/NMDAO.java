package edu.uclm.esi.ds.games.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.games.domain.NumberMatch;


public interface NMDAO extends JpaRepository<NumberMatch, String> {


}

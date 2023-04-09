package edu.uclm.esi.ds.games.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.games.dao.UserDAO;
import edu.uclm.esi.ds.games.domain.GameName;
import edu.uclm.esi.ds.games.domain.Match;
import edu.uclm.esi.ds.games.services.GameService;

@RestController
@RequestMapping("games")
@CrossOrigin("*")
public class GameController {
	@Autowired
	private GameService gameService;
	@Autowired
	private UserDAO userDAO;

	@GetMapping("/requestGame")
	public Match requestGame(@RequestParam String game, @RequestParam String player) {
		// ...
		if (!checkGame(game))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found");

		if(!checkPlayer(player))
			throw new ResponseStatusException(HttpStatus.PERMANENT_REDIRECT,"go to login page");

		return this.gameService.requestGame(game, player);
	}
	
	private boolean checkGame(String game) {
		boolean exists = false;

		for (GameName name : GameName.values()) {
			if (game.equals(name.toString()))
				exists = true;
		}

		return exists;
	}
	
	private boolean checkPlayer(String player) {
		boolean exists = false;

		try {
			exists = this.userDAO.findByName(player).getName().equals(player) ? true : false;
		} catch (NullPointerException e) { }

		return exists;
	}
}

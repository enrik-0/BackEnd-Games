package edu.uclm.esi.ds.games.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.games.dao.UserDAO;
import edu.uclm.esi.ds.games.domain.GameName;
import edu.uclm.esi.ds.games.domain.NumberMatch;
import edu.uclm.esi.ds.games.entities.User;
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
	public NumberMatch requestGame(@RequestBody Map<String, String> info) {
		String game = info.get("game");
		String player = info.get("player");
		if (checkGame(game) == -1)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "game not found");
		if(!checkPlayer(player))
			throw new ResponseStatusException(HttpStatus.PERMANENT_REDIRECT,"go to login page");
		return this.gameService.requestGame(game, player);
	}
	
	private int checkGame(String game) {
		int select = -1;
		for(GameName name : GameName.values()) {
			if (game.equals(name.toString()))
				select = name.ordinal();
			
		}
		return select;
	}
	
	private boolean checkPlayer(String player) {
		boolean exits = false;
		try {
		exits = this.userDAO.findByName(player).getName().equals(player)?true:false;
		
	}catch (NullPointerException e) {}
		return exits;

}
}

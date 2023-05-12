package edu.uclm.esi.ds.games.http;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.games.domain.GameName;
import edu.uclm.esi.ds.games.entities.Match;
import edu.uclm.esi.ds.games.services.APIService;
import edu.uclm.esi.ds.games.services.GameService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("games")
@CrossOrigin("*")
public class GameController {
	@Autowired
	private GameService gameService;
	@Autowired
	private APIService apiService;

	@GetMapping("/requestGame")
	public Match requestGame(HttpServletRequest request, @RequestParam String game) {
		String sessionID = request.getHeader("sessionID");
		JSONObject userJson;

		if (!checkGame(game))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found");
		
		// check user is logged.
		userJson = this.apiService.getUser(sessionID);
		if (userJson == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged");
		}

		return this.gameService.requestGame(game, userJson);
	}
	
	@PostMapping("/createCustomMatch")
	public Match createCustomMatch(HttpServletRequest request, @RequestParam String game, @RequestParam int ammount) {
		String sessionID = request.getHeader("sessionID");
		JSONObject userJson;

		if (!checkGame(game))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found");
		
		// check user is logged.
		userJson = this.apiService.getUser(sessionID);
		if (userJson == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged");
		}
		
		// check it has enough points.
		if (userJson.getInt("points") < ammount) {
			throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "You don't have enough points");
		}
		
		if (!this.apiService.updatePoints(sessionID,ammount * -1)) {
			throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Unable to update points.");
		}
		
		return this.gameService.createCustomMatch(game, userJson);
	}
	
	@PutMapping("/joinCustomMatch")
	public Match joinCustomMatch(HttpServletRequest request, @RequestParam String game, @RequestParam String matchCode) {
		String sessionID = request.getHeader("sessionID");
		JSONObject userJson;

		if (!checkGame(game))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game Not Found");
		
		// check user is logged.
		userJson = this.apiService.getUser(sessionID);
		if (userJson == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged");
		}
		
		return this.gameService.joinCustomMatch(game, userJson, matchCode);
	}
	
	private boolean checkGame(String game) {
		boolean exists = false;

		for (GameName name : GameName.values()) {
			if (game.equals(name.toString()))
				exists = true;
		}

		return exists;
	}
}

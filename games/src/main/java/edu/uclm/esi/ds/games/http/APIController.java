package edu.uclm.esi.ds.games.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.games.dao.UserDAO;
import edu.uclm.esi.ds.games.entities.User;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:8080") 
public class APIController {
	@Autowired
	private UserDAO userDAO;
	@PutMapping("/newUser")
	public void newUser(@RequestHeader("origin") String origin,
			@RequestHeader("Property") String pwd
			,@RequestParam String name) {

		if (!pwd.equals("lkj"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		User user = new User();
		user.setName(name);
		userDAO.save(user);

	}

}

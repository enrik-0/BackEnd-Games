package edu.uclm.esi.ds.games.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import edu.uclm.esi.ds.games.services.APIService;
import edu.uclm.esi.ds.games.services.GameService;


@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer {
	
	@Autowired
	private GameService gameService;
	@Autowired
	private APIService apiService;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new WSGames(gameService, apiService), "/wsGames").
		setAllowedOrigins("*")
		.addInterceptors(new HttpSessionHandshakeInterceptor());
	}
}

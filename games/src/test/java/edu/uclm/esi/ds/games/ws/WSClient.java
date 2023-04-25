package edu.uclm.esi.ds.games.ws;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WSClient {
	private CountDownLatch latch = new CountDownLatch(1);
	private Session session;
	private IWSReceiver receiver;

	public void openConnection(String url) throws Exception {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(this, new URI(url));
		this.latch.await();
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		latch.countDown();
		this.receiver.receive(this, WSStates.ON_OPEN.ordinal(), null);
	}

	@OnError
	public void onError(Session session, Throwable t) {
		this.session = session;
		latch.countDown();
		this.receiver.receive(this, WSStates.ON_ERROR.ordinal(), t.getMessage());
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		this.receiver.receive(this, WSStates.ON_MESSAGE.ordinal(), message);
	}

	public Session getSession() {
		return session;
	}

	public void setReceiver(IWSReceiver receiver) {
		this.receiver = receiver;
	}

}
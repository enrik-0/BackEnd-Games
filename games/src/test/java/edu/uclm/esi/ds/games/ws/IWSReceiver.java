package edu.uclm.esi.ds.games.ws;

public interface IWSReceiver {
	void receive(WSClient client, int event, String message);
}

package edu.uclm.esi.ds.games.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MovementNMTest {

	@Test
	void testIsValid() {
		Match match = new  NumberMatch();
		match.addPlayer("qwe");
		match.addPlayer("qeeww");
		match.buildBoards();
		Board board= match.getBoards().get(0);
		Number[] q = board.getDigits();

		q[0] = new Number(3);
		q[2] = new Number(2);
		Movement move = new MovementNM(0,2);
		assertFalse(move.isValid(q));

		q[3] = new Number(5);
		q[4] = new Number(5);
		board.setDigits(q);
		move = new MovementNM(3,4);
		assertTrue(move.isValid(q));
		board.updateBoard(3, 4);
		assertTrue(q[3].isFree());
		assertTrue(q[4].isFree());

		q[3] = new Number(5);
		q[5] = new Number(5);
		q[4].setFree(true);
		assertTrue(move.isValid(q));
	
	}

}

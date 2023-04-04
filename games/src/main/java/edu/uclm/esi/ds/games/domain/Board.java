package edu.uclm.esi.ds.games.domain;

import java.security.SecureRandom;

public class Board {

	private Number[] digits;

	public Board() {

		SecureRandom dice = new SecureRandom();
		this.digits = new Number[81];
		for (int i = 0; i < 81; i++)
				this.digits[i] = new Number((byte) dice.nextInt(10));
	}

	public Number[] getDigits() {
		return digits;
	}

	public Board copy() {
		Board board = new Board();
		board.digits = digits.clone();
		return board;
	}
}

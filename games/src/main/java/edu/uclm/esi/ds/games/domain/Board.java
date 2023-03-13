package edu.uclm.esi.ds.games.domain;

import java.security.SecureRandom;

public class Board {

	private byte[][] digits;

	public Board() {
		SecureRandom dice = new SecureRandom();
		this.digits = new byte[9][9];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.digits[i][j] = (byte) dice.nextInt(1, 10);

	}

	public byte[][] getDigits() {
		return digits;
	}

	public Board copy() {

		Board board = new Board();
		board.digits = new byte[9][9];

		return board;
	}
}

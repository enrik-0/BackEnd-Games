package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;

public class Board {
	private Number[] digits;

	public Board() {
		SecureRandom dice = new SecureRandom();
		this.setDigits(new Number[81]);
		for (int i = 0; i < digits.length/3; i++)
			this.digits[i] = new Number(dice.nextInt(1, 10));
	}

	public Number[] getDigits() {
		return digits;
	}

	public Board copy() {
		Board board = new Board();
		board.setDigits(digits.clone());
		return board;
	}

	public void updateBoard(int first, int second) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Movement move = new MovementNM(first, second);
		if(move.isValid(digits)) {
			digits[first].setFree(true);
			digits[second].setFree(true);
		}
	}

	public void setDigits(Number[] digits) {
		this.digits = digits;
	}
}


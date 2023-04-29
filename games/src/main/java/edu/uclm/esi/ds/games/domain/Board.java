package edu.uclm.esi.ds.games.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import edu.uclm.esi.ds.games.exceptions.BoardIsFullException;

public class Board {
	private ArrayList<Number> digits;
	private final int MAX_BOARD_LEN = 81;

	public Board() {
		SecureRandom dice = new SecureRandom();
		this.setDigits(new ArrayList<Number>());
		for (int i = 0; i < MAX_BOARD_LEN/3; i++)
			this.digits.add(new Number(dice.nextInt(1, 10)));
	}

	public ArrayList<Number> getDigits() {
		return digits;
	}

	public void setDigits(ArrayList<Number> digits) {
		this.digits = digits;
	}

	public Board copy() {
		Board board = new Board();
		board.setDigits((ArrayList<Number>) digits.clone());
		return board;
	}

	public void addNumbers() throws BoardIsFullException {
		List<Number> aux = this.digits.stream()
				.filter(value -> !value.isFree())
				.map(value -> value.copy())
				.toList();
		
		if (this.digits.size() == MAX_BOARD_LEN) {
			throw new BoardIsFullException("Game Over!");
		}

		if (aux.size() + this.digits.size() < MAX_BOARD_LEN) {
			this.digits.addAll(aux);
		} else {
			this.digits.addAll(aux.subList(0, MAX_BOARD_LEN-this.digits.size()));
		}
	}

	public void updateBoard(int i, int j) {
		this.digits.get(i).setFree(true);
		this.digits.get(j).setFree(true);
		
		this.cleanFreeRows();
	}

	private void cleanFreeRows() {
		ArrayList<Number> aux = new ArrayList<Number>();

		// for to current rows
		for (int i = 0; i <= this.getNumRows(); i++) {
			if (!isRowFree(i)) {
				aux.addAll(this.getRow(i));
			}
		}
		
		this.digits = aux;
	}

	private int getNumRows() {
		return (this.digits.size()-1)/9;
	}

	private List<Number> getRow(int rowIndex) {
		return this.digits.subList(rowIndex*9, this.getMaxIndex(rowIndex));
	}

	private int getMaxIndex(int rowIndex) {
		int max = (rowIndex*9)+9;

		if (max > this.digits.size()) {
			max = this.digits.size();
		}
		
		return max;
	}

	/**
	 * Checks that the whole row has all free numbers.
	 * 
	 * @param rowIndex
	 * @return true if whole row is full of free numbers, false otherwise.
	 */
	private boolean isRowFree(int rowIndex) {
		boolean isFree = true;
		List<Number> aux = this.digits.subList(rowIndex*9, this.getMaxIndex(rowIndex));
		
		if (aux.stream().anyMatch(value -> !value.isFree())) {
			isFree = false;
		}

		return isFree;
	}	
}

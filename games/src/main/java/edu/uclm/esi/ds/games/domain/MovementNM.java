package edu.uclm.esi.ds.games.domain;

import java.util.ArrayList;
import java.util.HashSet;

public class MovementNM implements Movement{
	
	private byte[] position = new byte[2];

	public MovementNM(int i, int j) {
		//the lowest position at the 0 
		position[0] = (byte) (i >= j? j : i);
		position[1] = (byte) (j <= i? i : j);
	}
	public  boolean isValid(Number[] board) {
		return validate(board);
	}

	private boolean validate(Number[] board) {
		ArrayList<Byte> all = new ArrayList<Byte>();
		ArrayList<Byte> unique;
		boolean valid = true;
		if(!validateCombination(board)) {
			valid = false;
		}
		else {
			all.addAll(calcDiagonals(board));
			all.addAll(calcHorizontal(board));
			all.addAll(calcVertical(board));
			HashSet<Byte> noDuplicated = new HashSet<Byte>(all);
			unique = new ArrayList<Byte>(noDuplicated);
			if (unique.contains(position[1]))
				valid = true;
		}
		return valid;
	}
	private boolean validateCombination(Number[] board) {

		boolean valid = false;
		if(board[position[0]].getNumber()
				== board[position[1]].getNumber() ||
				board[position[0]].getNumber() +
				board[position[1]].getNumber() == 10)
			valid = true;
		return valid;
	}
	
	private ArrayList<Byte> calcHorizontal(Number[] board) {
		byte lengthH = 9;
		ArrayList<Byte> numbers = new ArrayList<Byte>();
		boolean next = true;
		byte current = position[0];
		do {
			current = calcHorizontalR(current, lengthH);
			if (current < 0)
				next = false;
			else
				if(!board[current].isFree()) {
					next = false;
					numbers.add(board[current].getNumber());
			}
		}while(next);
		
		next = true;
		current = position[0];
		do {
			current = calcHorizontalL(current);
			if (current < 0)
				next = false;
			else
				if(!board[current].isFree()) {
					next = false;
					numbers.add(board[current].getNumber());
			}
		}while(next);

		return numbers;
	}
	
	private ArrayList<Byte> calcVertical(Number[] board) {
		ArrayList<Byte> numbers = new ArrayList<Byte>();
		boolean next = true;
		byte lengthH = 9;
		byte lengthV = (byte) (board.length / lengthH);
		byte current = position[0];
		byte counter = 1;
		do {
			current = calcVerticalUp(current, lengthH, counter);
			if (current < 0)
				next = false;
			else
				if (!board[current].isFree()) {
					next = false;
					numbers.add(current);
			}

			counter++;

		}while(next);

		next = true;
		counter = 1;
		current = position[0];
		
		do {
			current = calcVerticalDown(current, lengthH, lengthV, counter);
			if(current < 0)
				next = false;
			else
				if(!board[current].isFree()) {
					next = false;
					numbers.add(current);
			}
			counter++;
			
		}while(next);

		return numbers;
	}

	private  byte calcHorizontalR(byte start, byte lengthH){
		byte result = (byte) (start + 1);
		if(result >= lengthH)
			result = -1;
		return result;

	}

	private byte calcHorizontalL(byte start) {
		return (byte) (start - 1);
	}

	private byte calcVerticalUp(byte start, byte  lengthH, byte k) {
		return (byte) (start - lengthH * k);
	}
	
	private byte  calcVerticalDown(byte start, byte lengthH, byte lengthV, byte k) {
		byte result = (byte) (start + lengthH * k);
		int i = start - (start % lengthH )* k;
		if  (i >= lengthV)
			result = -1;
		return result;
	}
	
	private byte calcDiagonalLDown(byte start, byte lengthH) {
		byte result = (byte) (start + lengthH - 1);
		if (start % lengthH ==  0)
			result = -1;
		return result;
	}
	
	private byte calcDiagonalLUp(byte start, byte lengthH) {
		byte result = (byte) (start - lengthH - 1);
		if(start % lengthH == 0)
			result = -1;
		return result;
	}
	
	private byte calcDiagonalRDown(byte start, byte lengthH) {
		byte result = (byte) (start + lengthH + 1);
		if(start % lengthH == lengthH - 1)
			result = -1;
		return result;
	}
	
	private byte calcDiagonalRUp(byte start, byte lengthH) {
		byte result = (byte) (start - lengthH + 1);
		if (start % lengthH == lengthH - 1)
			result = -1;
		return result;
		
	}
	
	private ArrayList<Byte> calcDiagonals(Number[] board) {
		ArrayList<Byte> numbers = new  ArrayList<Byte>();
		Byte lengthH = 9;
		byte lengthV = (byte) (board.length / lengthH);
		boolean next = true;
		byte current = position[0];
		do {
			current = calcDiagonalLUp(current, lengthH);
			if (current < 0)
				next = false;
			else
				if (!board[current].isFree()) {
					next = false;
					numbers.add(current);
				}
			
		}while(next);
		
		next = true;
		current = position[0];
		do {
			current = calcDiagonalLDown(current, lengthH);
			if (current < 0)
				next = false;
			else
				if (!board[current].isFree()) {
					next = false;
					numbers.add(current);
				}
			
		}while(next);

		next = true;
		current = position[0];
		do {
			current = calcDiagonalRUp(current, lengthH);
			if (current < 0)
				next = false;
			else
				if (!board[current].isFree()) {
					next = false;
					numbers.add(current);
				}
			
		}while(next);

		next = true;
		current = position[0];
		do {
			current = calcDiagonalRUp(current, lengthH);
			if (current < 0)
				next = false;
			else
				if (!board[current].isFree()) {
					next = false;
					numbers.add(current);
				}
			
		}while(next);

		return numbers;
	}
}


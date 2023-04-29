package edu.uclm.esi.ds.games.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.uclm.esi.ds.games.entities.Player;
import edu.uclm.esi.ds.games.exceptions.BoardIsFullException;

class PlayNumberMatchTest {
	@Test
	void testIsValid() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Match match = new NumberMatch();
		match.addPlayer(new Player("1234", "Pepe", "pepe@pepe.com"));
		match.addPlayer(new Player("abcd", "Ana", "ana@ana.com"));
		match.buildBoards();
		Board board= match.getBoards().get(0);
		ArrayList<Number> q = board.getDigits();

		/*
		 * positions are ilegal
		 * values are legal
		 * result: false
		 */
		q.set(0, new Number(5));
		q.set(2, new Number(5));
		Movement move = new MovementNM(0,2);
		assertFalse(move.isValid(q));

		/*
		 * position are ilegal
		 * values are ilegal
		 * result: false
		 */
		q.set(0, new Number(5));
		q.set(2, new Number(3));
		move = new MovementNM(0,2);
		assertFalse(move.isValid(q));
		
		/*
		 * position are legal
		 * values are legal
		 * result: true and those position must be marked as free
		 */
		q.set(3, new Number(5));
		q.set(4, new Number(5));
		board.setDigits(q);
		move = new MovementNM(3,4);
		assertTrue(move.isValid(q));
		board.updateBoard(3, 4);
		assertTrue(q.get(3).isFree());
		assertTrue(q.get(4).isFree());

		/*
		 * NOW this positions are legal because 4 is free
		 * values are legal
		 * result: true
		 */
		q.set(3, new Number(5));
		q.set(5, new Number(5));
		move = new MovementNM(3,5);
		assertTrue(move.isValid(q));
		
		
		/*
		 * change of row test
		 * position are legal
		 * values are legal
		 * result: true
		 */
		q.set(8, new Number(9));
		q.set(9, new Number(1));
		move = new MovementNM(8,9);
		assertTrue(move.isValid(q));
	}

	@Test
	void testUpdateBoard() {
		Board board = new Board();
		
		// Board is created with 3 rows.
		assertTrue(board.getDigits().size() == 27);

		// Update two numbers.
		board.getDigits().set(25, new Number(7));
		board.getDigits().set(26, new Number(3));
		System.out.println("\nORIGINAL BOARD");
		printBoard(board.getDigits());

		board.updateBoard(25, 26);
		hr();
		System.out.println("\nUPDATE BOARD (7, 3)");
		printBoard(board.getDigits());
		
		// Delete 1 row
		for (int i = 0; i < 7; i++) {
			board.getDigits().get(i).setFree(true);
		}

		board.getDigits().set(7, new Number(5));
		board.getDigits().set(8, new Number(5));
		hr();
		System.out.println("\nDELETE 1 ROW (5, 5)");
		printBoard(board.getDigits());

		board.updateBoard(7, 8);
		hr();
		System.out.println("\nBOARD UPDATED");
		printBoard(board.getDigits());

		// Delete 2 rows
		for (int i = 0; i < 8; i++) {
			board.getDigits().get(i).setFree(true);
		}
		for (int i = 10; i < 18; i++) {
			board.getDigits().get(i).setFree(true);
		}

		board.getDigits().remove(board.getDigits().size()-1);		board.getDigits().remove(board.getDigits().size()-1);
		board.getDigits().set(8, new Number(5));
		board.getDigits().set(9, new Number(5));
		hr();
		System.out.println("\nDELETE 2 ROWS (5, 5)");
		printBoard(board.getDigits());
		board.updateBoard(8, 9);

		hr();
		System.out.println("\nBOARD UPDATED");
		printBoard(board.getDigits());
	}
	
	@Test
	void addNumbers() throws BoardIsFullException {
		hr(); hr();
		System.out.println("\nADD NUMBERS");
		Board board = new Board();
		
		hr();
		System.out.println("\nBEFORE");
		printBoard(board.getDigits());

		board.addNumbers();
		assertTrue(board.getDigits().size() == 6*9);

		hr();
		System.out.println("\nAFTER");
		printBoard(board.getDigits());

		board.addNumbers();
		assertTrue(board.getDigits().size() == 9*9);

		hr();
		System.out.println("\nAFTER 2");
		printBoard(board.getDigits());
		
		assertThrows(BoardIsFullException.class, () -> { board.addNumbers(); });
		
		// Free last row
		for (int i = 0; i < 8; i++) {
			board.getDigits().get(board.getDigits().size()-(i+1)).setFree(true);;
		}

		hr();
		System.out.println("\nLast row FREE");
		printBoard(board.getDigits());
		
		try {
			board.addNumbers();
		} catch (BoardIsFullException e) {
			System.out.println("\n" + e.getMessage());
		}

		// remove last row
		board.getDigits().get(board.getDigits().size()-9).setFree(true);
		board.updateBoard(0, 1);
		hr();
		System.out.println("\nBOARD Without last row");
		printBoard(board.getDigits());

		board.addNumbers();

		hr();
		System.out.println("\nAFTER 3");
		printBoard(board.getDigits());
	}
	
	
	private void hr() {
		System.out.println("\n-----------------------------------------------");
	}
	
	private void printBoard(List<Number> board) {
		for (int i = 0; i < board.size(); i++) {
			if (i % 9 == 0) {
				System.out.println();
			}

			if (board.get(i) == null) {
				System.out.print(" 0 ");
			} else {
				if ((board.get(i).isFree()))
					System.out.print(board.get(i).getNumber() + "F ");
				else
					System.out.print(board.get(i).getNumber() + " ");
			}
		}
	}
}

package edu.uclm.esi.ds.games.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import edu.uclm.esi.ds.games.domain.Number;

public interface Movement {
	
	/**
	 * <h2> Function that says if the movement is legal <h1>
	 * @param board An array that contains the elements of the board
	 * @return If movement is legal true otherwise false
	 */
	boolean isValid(ArrayList<Number> board) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}

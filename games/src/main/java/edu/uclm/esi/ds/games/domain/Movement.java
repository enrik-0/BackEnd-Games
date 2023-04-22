package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;

public interface Movement {
	
	/**
	 * <h2> Function that says if the movement is legal <h1>
	 * @param board An array that contains the elements of the board
	 * @return If movement is legal true otherwise false
	 */
	boolean isValid(Number[] board) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}

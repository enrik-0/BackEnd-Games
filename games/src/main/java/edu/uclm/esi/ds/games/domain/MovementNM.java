package edu.uclm.esi.ds.games.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.uclm.esi.ds.games.exceptions.InvalidMoveException;

public class MovementNM implements Movement{
	private byte[] position = new byte[2];
	private Method[] methods;

	public MovementNM(int i, int j){
		//the lowest position at the 0 
		position[0] = (byte) (i >= j? j : i);
		position[1] = (byte) (j <= i? i : j);
		methods = createFunctions();
	}
	
	public MovementNM(int i) {
		position[0] = (byte) i;
	}

	public byte getFirst() {
		return position[0];
	}
	public byte getSecond() {
		return position[1];
	}
	public  boolean isValid(ArrayList<Number> board) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		return validate(board);
	}

	
	private boolean validPosition(ArrayList<Number> board) {

		boolean valid = false;
		
		if (sameNumber(board) || addUpTen(board))
			valid = true;
		
		return valid;
	}
	
	private boolean sameNumber(ArrayList<Number> board) {

		return board.get(position[0]).getNumber() == board.get(position[1]).getNumber();
	}
	
	private boolean addUpTen(ArrayList<Number> board) {

		return (board.get(position[0]).getNumber() +
				board.get(position[1]).getNumber()) == 10;
	}

	private boolean validate(ArrayList<Number> board) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		ArrayList<Byte> all;
		boolean valid = true;
		try {
			all = calcAllValidMoves(board);

			if (!all.contains(position[1]))
				valid = false;

		}catch(InvalidMoveException e) {
			valid = false;
		}

		return valid;
	}


	public byte calcHorizontalR(byte start, byte length){

		byte result = (byte) (start + 1);

		if  (result > length * length)
			result = -1;

		return result;
	}

	public byte calcHorizontalL(byte start) {

		return (byte) (start - 1);
	}

	public byte calcVerticalUp(byte start, byte  lengthH, byte k) {

		return (byte) (start - lengthH * k);
	}

	public byte  calcVerticalDown(byte start, byte lengthH, byte lengthV, byte k) {

		byte result = (byte) (start + lengthH);
		if(result >= lengthH * lengthV)
			result = -1;
		return result;
	}
	
	public byte calcDiagonalLDown(byte start, byte lengthH) {

		byte result;

		if (start % lengthH ==  0)
			result = -1;
		else
			result = (byte) (start + lengthH - 1);

		return result;
	}
	
	public byte calcDiagonalLUp(byte start, byte lengthH) {

		byte result;

		if(start % lengthH == 0)
			result = -1;
		else
			result = (byte) (start - lengthH - 1);

		return result;
	}
	
	public byte calcDiagonalRDown(byte start, byte lengthH) {

		byte result;

		if(start % lengthH == lengthH - 1)
			result = -1;
		else
			result = (byte) (start + lengthH + 1);

		return result;
	}
	
	public byte calcDiagonalRUp(byte start, byte lengthH) {

		byte result;

		if (start % lengthH == lengthH - 1)
			result = -1;
		else
			result = (byte) (start - lengthH + 1);

		return result;
	}
	
	public ArrayList<Byte> calcAllValidMoves(ArrayList<Number> board) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidMoveException {
		byte lengthH = 9;
		byte lengthV = (byte) Math.ceil(((double) board.size() / (double) lengthH));
		ArrayList<Byte> all = new ArrayList<Byte>();
		if(validPosition(board) && !board.get(position[0]).isFree()) { 
			//diagonal Up left
			all.addAll(this.launcher(this, methods[0], board, position[0], lengthH));
			//vertical up
			all.addAll(this.launcher(this, methods[1], board, position[0], lengthH));
			//diagonal Up right
			all.addAll(this.launcher(this, methods[2], board, position[0], lengthH));
			//horizontal right
			all.addAll(this.launcher(this, methods[3], board, position[0], lengthH));
			//diagonal down right
			all.addAll(this.launcher(this, methods[4], board, position[0], lengthH));
			//vertical down
			all.addAll(this.launcher(this, methods[5], board, position[0],
					lengthH, lengthV));
			//diagonal down left
			all.addAll(this.launcher(this, methods[6], board, position[0], lengthH));
			//horizontal left
			all.addAll(this.launcher(this, methods[7], board, position[0]));
			} else throw new InvalidMoveException();
		return all; 
	}

	private ArrayList<Byte> launcher(Object object, Method method, ArrayList<Number> board, byte... w) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{

		byte buffer;
		//vertical need a counter so we check if the method is vertical
		boolean isVertical = method.getName().toLowerCase().contains("vertical");
		boolean next = true;
		byte current = w[0];
		ArrayList<Byte> positions = new ArrayList<Byte>();
		
		// the parameters for the method
		Object[] parameters;

		if(isVertical) {
			parameters = new Object[w.length + 1];
			parameters[w.length] = Byte.parseByte("0");
		}
		else
			parameters = new Object[w.length];

		for (int i = 0; i < w.length; i++)
			parameters[i] = w[i];


		do {

			current = (byte) method.invoke(object, parameters);
			if (current != position[0]) {
				if (current < 0 || current >= board.size())
					next = false;
				else if (!board.get(current).isFree()) {
					next = false;
					positions.add(current);
				}
			}

			parameters[0] = current;

			if (isVertical) {
				try {
				 buffer = (byte) (Integer.parseInt(parameters[w.length].toString()) + 1);
				 parameters[w.length] = buffer ;
				 }catch(Exception e) {
					 System.out.println(e.getMessage());
				 }
			}

		}while(next);
		return positions;
	}

	private Method[] createFunctions() {
		Method[] methods = new Method[8];
		try{
			
			//diagonal up left
			methods[0] = createFunction("calcDiagonalLUp", 2);
			//vertical up
			methods[1] = createFunction("calcVerticalUp", 3);
			//diagonal  up right
			methods[2] = createFunction("calcDiagonalRUp", 2);
			//horizontal right
			methods[3] = createFunction("calcHorizontalR", 2);
			//diagonal down right
			methods[4] = createFunction("calcDiagonalRDown", 2);
			//vertical down
			methods[5] = createFunction("calcVerticalDown", 4);
			//diagonal down left
			methods[6] = createFunction("calcDiagonalLDown", 2);
			//horizontal left
			methods[7] = createFunction("calcHorizontalL", 1);
		}catch(NoSuchMethodException | SecurityException e) {}
		return methods;
	}

	private Method createFunction(String methodName, int elements) throws NoSuchMethodException, SecurityException {

		Class[] classes = new Class[elements];
		for (int i = 0; i < classes.length; i++)
			classes[i] = byte.class;
		Method method = MovementNM.class.getMethod(methodName, classes);
		
		return method;
	}
}

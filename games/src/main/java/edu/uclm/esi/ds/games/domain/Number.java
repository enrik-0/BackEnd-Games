package edu.uclm.esi.ds.games.domain;

public class Number {
	byte number;
	boolean free;

	public Number(int i) {
		this.number = (byte) i;
		this.free = false;
	}

	public byte getNumber() {
		return number;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
}

package edu.uclm.esi.ds.games.domain;

public class Number {
	byte number;
	boolean free;

	public Number(int i) {
		this.number = (byte) i;
		this.free = false;
	}

	public Number(int i, boolean free) {
		this.number = (byte) i;
		this.free = free;
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
	
	public Number copy() {
		return new Number(this.number, this.free);
	}
}

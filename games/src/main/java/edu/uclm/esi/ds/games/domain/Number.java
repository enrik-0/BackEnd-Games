package edu.uclm.esi.ds.games.domain;

public class Number {

	byte number;
	 boolean free = false;
	 
	 public Number(byte number) {
		 this.number = number;
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
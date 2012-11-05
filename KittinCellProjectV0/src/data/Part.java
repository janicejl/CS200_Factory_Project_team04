package data;

import java.io.Serializable;

import MoveableObjects.Part.PartType;

public class Part implements Serializable{

	public enum PartType{part1, part2, part3, part4, part5, part6, part7, part8, none};
	
	public PartType type;
	
	private String id;
	private double x;
	private double y;
	private boolean destination; // if part is at destination

	public void copy(Part p){
		id = p.getID();
		x = p.getX();
		y = p.getY();
		destination = p.getDestination();
	}
	
	public Part(String name) {
		id = name;
		x = 0;
		y = 0;
		destination = false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double nX) {
		x = nX;
	}

	public void setY(double nY) {
		y = nY;
	}

	public String getID() {
		return id;
	}

	public void setID(String nID) {
		id = nID;
	}

	public synchronized String getId() {
		return id;
	}

	public synchronized void setId(String id) {
		this.id = id;
	}

	public synchronized boolean getDestination() {
		return destination;
	}

	public synchronized void setDestination(boolean destination) {
		this.destination = destination;
	}
}
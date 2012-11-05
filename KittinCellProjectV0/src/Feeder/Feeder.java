package Feeder;

import java.io.Serializable;
import java.util.*;

import data.Part;



public class Feeder implements Serializable{
	
	private double x;
	private double y;
	private double partAmount;
	
	private boolean topLane;
	private boolean empty;
	private Vector<Part> parts;
	public Feeder(double nX, double nY){
		x = nX;
		y = nY;
		parts = new Vector<Part> ();
		
		partAmount = 0;
		empty = true;
		topLane = true;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double nX) {
		x = nX;
	}
	
	public void setY(double nY) {
		y = nY;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public double getPartAmount(){
		return partAmount;
	}
	
	public Part removePart(){
		if (partAmount > 0){
			partAmount--;
			return parts.remove(0);
		}
		else {
			System.out.println("Sorry! -Roy");
			return null;
		}
	}
	
	public void setParts(Vector<Part> parts) {
		this.parts = parts;
	}
	
	public void removeAll(){
		partAmount = 0;
	}
	
	public void addParts(Part part){
		partAmount++;
		parts.add(part);
	}
	
	// True sets the diverter to top lane
	// False sets the diverter to bottom lane
	public void divertLane(boolean l){
		if (l = true)
			topLane = true;
		else
			topLane = false;
	}
	
	// returns true if the diverter is set to Top lane
	// returns false if the diverter is set to Bottom Lane
	public boolean getLane(){
		return topLane;
	}
	
	public Vector<Part> getParts() {
		return parts;
	}

	public synchronized void setPartAmount(double partAmount) {
		this.partAmount = partAmount;
	}
}

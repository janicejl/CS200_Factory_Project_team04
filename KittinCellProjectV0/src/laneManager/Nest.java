package laneManager;

import data.Part;

import java.io.Serializable;
import java.util.ArrayList;

public class Nest implements Serializable{
	
	private double x;				//The x position for the nest
	private double y;				//The y position for the nest
	
	
	ArrayList<Part> parts;			//a list of parts that is currently in the nest. 
	
	public Nest(double nX, double nY) {
		x = nX;
		y = nY;
		parts = new ArrayList<Part>();
	}

	public Nest(double nX, double nY, int n) {
		x = nX;
		y = nY;
		parts = new ArrayList<Part>();
		
		//for v0
			Part p = new Part(""+n, "images/kt" + n + ".png");
			addPart(p);
		
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
	
	
	
	public void addPart(Part p) {
		
			parts.add(p);
		
		
	}
	
	public ArrayList<Part> getParts() {
		return parts;
	}
	
	public void purgeNest() {	//empties the array since the parts are just dumped onto the ground. 
		parts.clear();
	}
}

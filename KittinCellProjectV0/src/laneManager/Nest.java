package laneManager;

import data.Part;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Nest implements Serializable{
	
	private double x;				//The x position for the nest
	private double y;				//The y position for the nest
	
	
	ArrayList<Part> parts;			//a list of parts that is currently in the nest. 
	
//	ArrayList<Integer> partsCount;
	
	private Random random;
	
	public Nest(double nX, double nY) {
		x = nX;
		y = nY;
		parts = new ArrayList<Part>();
		random = new Random();
//		partsCount = new ArrayList<Integer>();
//		for (int i = 0; i < 8; i++) {
//			partsCount.add(0);
//		}
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
		
		if (parts.size() >= 9) {
			parts.get(parts.size() - 1).setX(this.x + (double)random.nextInt(100));
			parts.get(parts.size() - 1).setY(this.y + (double)random.nextInt(50));
		}
	}
	
	public ArrayList<Part> getParts() {
		return parts;
	}
	
	public void purgeNest() {	//empties the array since the parts are just dumped onto the ground. 
		parts.clear();
	}

	public int getSize() {
		return this.parts.size();
	}

//	public ArrayList<Integer> getPartsCount(){
//		return partsCount;
//	}
	
}

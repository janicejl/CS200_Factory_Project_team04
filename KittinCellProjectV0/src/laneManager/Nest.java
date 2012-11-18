package laneManager;

import data.Part;

import java.io.Serializable;
import java.util.ArrayList;

public class Nest implements Serializable{
	
	private double x;
	private double y;
	
	
	ArrayList<Part> parts;
	
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
	
	public void purgeNest() {
		parts.clear();
	}
}

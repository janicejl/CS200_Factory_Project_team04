package laneManager;

import data.Part;

import java.util.ArrayList;

public class Nest {
	
	private double x;
	private double y;
	
	private boolean full;
	
	ArrayList<Part> parts;
	
	public Nest(double nX, double nY) {
		x = nX;
		y = nY;
		parts = new ArrayList<Part>();
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
	
	public boolean isFull() {
		return full;
	}
	
	public void setFull(boolean f) {
		full = f;
	}
	
	public void addPart(Part p) {
		parts.add(p);
	}
}
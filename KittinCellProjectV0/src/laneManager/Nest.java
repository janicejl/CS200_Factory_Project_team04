package laneManager;

import data.Part;

import java.io.Serializable;
import java.util.Vector;

public class Nest implements Serializable{
	
	private double x;
	private double y;
	
	private boolean full;
	
	Vector<Part> parts;
	
	public Nest(double nX, double nY) {
		x = nX;
		y = nY;
		parts = new Vector<Part>();
	}

	public Nest(double nX, double nY, int n) {
		x = nX;
		y = nY;
		parts = new Vector<Part>();
		
		//for v0
		while (full != true) {
			Part p = new Part(""+n);
			addPart(p);
		}
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
		if (full != true) {
			parts.add(p);
		}
		if (parts.size() >= 8) {
			full = true;
		}
	}
	
	public synchronized Vector<Part> getParts() {
		return parts;
	}
}

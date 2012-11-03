package data;

import java.util.Vector;

public class Kit {
	private String id;
	private double x;
	private double y;
	
	Vector<Part> parts;
	
	public Kit(String name, double nX, double nY) {
		id = name;
		x = nX;
		y = nY;
		
		parts = new Vector<Part>();
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String name) {
		id = name;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double nX) {
		x = nX;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double nY) {
		y = nY;
	}
	
	public void addPart(Part p) {
		parts.add(p);
	}
}

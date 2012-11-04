package data;

import java.io.*;
import java.util.Vector;

public class Kit implements Serializable {
	private String id;
	private double x;
	private double y;
	
	private double destinationX;
	private double destinationY;
	
	Vector<Part> parts;
	
	public Kit(String name, double nX, double nY) {
		id = name;
		x = nX;
		y = nY;
		destinationX = nX;
		destinationY = nY;
		
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
	
	public double getDestinationX() {
		return destinationX;
	}
	
	public void setDestinationX(double nX) {
		destinationX = nX;
	}
	
	public double getDestinationY() {
		return destinationY;
	}
	
	public void setDestinationY(double nY) {
		destinationY = nY;
	}
	public void addPart(Part p) {
		parts.add(p);
	}
	
	public void update() {
		if (destinationX != x) {
			if (x < destinationX) {
				x++;
			} else {
				x--;
			}
		}
		
		if (destinationY != y) {
			if (y < destinationY) {
				y++;
			} else {
				y--;
			}
		}
	}
	
	public boolean finishMoving() {
		if (x == destinationX && y == destinationY) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setPosition(double nX, double nY) {
		x = nX;
		y = nY;
		//destinationX = nX;
		//destinationY = nY;
	}
}

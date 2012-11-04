package data;

import java.io.Serializable;

public class Part implements Serializable{

	private String id;
	private double x;
	private double y;

	public Part(String name) {
		id = name;
		x = 0;
		y = 0;
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
}
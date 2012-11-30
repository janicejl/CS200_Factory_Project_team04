package data;

import java.io.Serializable;


public class Part implements Serializable{

	public PartInfo info;
	
	private String id;
	private String imagePath;
	
	private double x;
	private double y;
	private boolean destination; // if part is at destination
	boolean partDropped = false;

	public void copy(Part p){
		id = p.getID();
		x = p.getX();
		y = p.getY();
		destination = p.getDestination();
	}
	
	public Part(PartInfo _info){
		info = _info;
		id = info.getName();
		imagePath = info.getImagePath();
		x = 0;
		y = 0;
		destination = false;
	}
	
	public Part(String name, String _imagePath) {
		id = name;
		imagePath = _imagePath;
		x = 0;
		y = 0;
		destination = false;
	}
	
	public void setPartDropped(boolean b){
		partDropped = b;
	}
	
	public boolean getPartDropped(){
		return partDropped;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getDestination() {
		return destination;
	}

	public void setDestination(boolean destination) {
		this.destination = destination;
	}

	public String getImagePath() {
		return imagePath;
	}



	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public PartInfo getInfo() {
		return info;
	}

	public void setInfo(PartInfo info) {
		this.info = info;
	}
}
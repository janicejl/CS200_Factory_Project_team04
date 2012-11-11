package data;

import java.io.*;

import data.Part.PartType;

public class PartInfo implements Serializable{

	String name;
	String imagePath;
	PartType type; 
	
	public PartInfo(String n, String i){
		name = n;
		imagePath = i;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized String getImagePath() {
		return imagePath;
	}

	public synchronized void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public synchronized PartType getType() {
		return type;
	}

	public synchronized void setType(PartType type) {
		this.type = type;
	}
}

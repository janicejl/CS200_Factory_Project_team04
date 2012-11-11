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
	
	public synchronized void setType(Integer type){
		switch(type){
		case 0:
			this.type = Part.PartType.part1;
			break;
		case 1:
			this.type = Part.PartType.part2;
			break;
		case 2:
			this.type = Part.PartType.part3;
			break;
		case 3:
			this.type = Part.PartType.part4;
			break;
		case 4:
			this.type = Part.PartType.part5;
			break;
		case 5:
			this.type = Part.PartType.part6;
			break;
		case 6:
			this.type = Part.PartType.part7;
			break;
		case 7:
			this.type = Part.PartType.part8;
			break;
		case 8:
			this.type = Part.PartType.none;
			break;
		}
	}
}

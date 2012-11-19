package data;

import java.io.*;


public class PartInfo implements Serializable{

	String name;
	String imagePath;
	int idNum;
	String description;
	
	public PartInfo(String n, String i){
		name = n;
		imagePath = i;
		idNum = 0;
		description = "";
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
	
	public synchronized void setDescription(String description){
		this.description = description;
	}
	
	public synchronized String getDescription(){
		return description;
	}
	
	public synchronized void setIdNumber(int idNumber){
		idNum = idNumber;
	}
	
	public synchronized int getIdNumber(){
		return idNum;
	}
	
	public synchronized void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	

	
	
}

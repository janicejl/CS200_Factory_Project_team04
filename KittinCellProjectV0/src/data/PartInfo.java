package data;

import java.io.*;

public class PartInfo implements Serializable{

	String name;
	String imagePath;
	
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
}

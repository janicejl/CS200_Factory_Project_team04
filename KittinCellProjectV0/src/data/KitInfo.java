package data;

import java.io.*;
import java.util.*;

public class KitInfo implements Serializable{
	
	String name;
	ArrayList<PartInfo> parts;
	Integer size;
	
	public KitInfo(String n){
		name = n;
		parts = new ArrayList<PartInfo>();
		size = new Integer(0);
	}
	
	public KitInfo(KitInfo k){
		name = k.getName();
		parts = k.getParts();
		size = k.getSize();
	}
	
	public void add(PartInfo p){
		parts.add(p);
		size++;
	}
	
	public void remove(int i){
		parts.remove(i);
		size--;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized ArrayList<PartInfo> getParts() {
		return parts;
	}

	public synchronized void setParts(ArrayList<PartInfo> parts) {
		this.parts = parts;
	}

	public synchronized Integer getSize() {
		return size;
	}

	public synchronized void setSize(Integer size) {
		this.size = size;
	}
	
	
}

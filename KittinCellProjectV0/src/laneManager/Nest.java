package laneManager;

import data.Part;
import data.PartInfo;

import java.io.Serializable;
import java.util.concurrent.*;
import java.util.*;

public class Nest implements Serializable{
	
	private double x;				//The x position for the nest
	private double y;				//The y position for the nest
	boolean purged = false;
	int purgedCount =0;
	boolean flip = true;
	
	CopyOnWriteArrayList<Part> parts;			//a list of parts that is currently in the nest. 
	
//	CopyOnWriteArrayList<Integer> partsCount;
	
	private Random random;
	
	public Nest(double nX, double nY) {
		x = nX;
		y = nY;
		parts = new CopyOnWriteArrayList<Part>();
		random = new Random();
//		partsCount = new CopyOnWriteArrayList<Integer>();
//		for (int i = 0; i < 8; i++) {
//			partsCount.add(0);
//		}
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
	
	
	
	public void addPart(Part p) {
		
		parts.add(p);
		
		if (parts.size() >= 9) {
			parts.get(parts.size() - 1).setX(this.x + (double)random.nextInt(70));
			parts.get(parts.size() - 1).setY(this.y + (double)random.nextInt(20));
		}
	}
	
	public CopyOnWriteArrayList<Part> getParts() {
		return parts;
	}
	
	public void purgeNest() {	//empties the array since the parts are just dumped onto the ground. 
		this.clear();
		
		int i=0;
		while(i<9)
		{
			this.addPart(new Part(new PartInfo("purging","images/flame1.png")));
			i++;
		}
		purged = true;
	}

	public int getSize() {
		return this.parts.size();
	}

//	public CopyOnWriteArrayList<Integer> getPartsCount(){
//		return partsCount;
//	}
	
	public boolean getPurged()
	{
		return purged;
	}
	
	public void setPurged(boolean b)
	{
		purged = b;
	}
	
	public int getPurgedCount()
	{
		return purgedCount;
	}
	
	public void setPurgedCount(int pc)
	{
		purgedCount = pc;
	}
	
	public void flip()
	{
		this.clear();
		if(flip == true)
		{
			int i=0;
			while(i<9)
			{
				this.addPart(new Part(new PartInfo("flames","images/flame2.png")));
				i++;
			}
			flip = false;
		}
		else if(flip==false)
		{
			int i=0;
			while(i<9)
			{
				this.addPart(new Part(new PartInfo("flames","images/flame1.png")));
				i++;
			}
			flip= true;
		}
	}
	
	public boolean getFlip()
	{
		return flip;
	}
	
	public void clear()
	{
		parts.clear();
		//partsCount.clear();
	}
}

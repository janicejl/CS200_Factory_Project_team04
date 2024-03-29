package Feeder;

import java.io.Serializable;
import java.util.*;
import data.PartInfo;
import data.Part;



public class Feeder implements Serializable{
	
	private double x;
	private double y;
	private double partAmount;
	private boolean empty;
	private Vector<Part> parts;
	private Random random;
	private PartInfo info;
	private boolean hasCrate;
	
	//diverter stuff
	int diverterX, diverterY;
	boolean previousPosition;
	private boolean moving;
	private boolean topLane;
	double diverterSpeed;
	
	public double getDiverterSpeed() {
		return diverterSpeed;
	}

	public void setDiverterSpeed(double diverterSpeed) {
		this.diverterSpeed = diverterSpeed;
	}

	public Feeder(double nX, double nY){
		x = nX;
		y = nY;
		parts = new Vector<Part> ();
		random = new Random();
		
		partAmount = 0;
		empty = true;
		moving = false;
		topLane = true;
		previousPosition = topLane;
		
		diverterX = (int)(getX() - 3);
		diverterY = (int)getY();
		diverterSpeed = 2;
		info = null;
		hasCrate = false;
	}
	
	public void updateDiverter(){
		if (previousPosition == topLane){
			moving = false;
		}
		else{
			moving = true;
			if (previousPosition){
				if (diverterY != y + 70){
					diverterY += diverterSpeed;
				}
				else{
					previousPosition = topLane;
				}	
			}
			else {
				if (diverterY != y){
					diverterY -= diverterSpeed;
				}
				else{
					previousPosition = topLane;
				}
			}
		}
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public void setX(double nX) {
		x = nX;
	}
	
	public void setY(double nY) {
		y = nY;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public double getPartAmount(){
		return partAmount;
	}
	
	public Part removePart(){
		if (partAmount > 0){
			partAmount--;
			return parts.remove(0);
		}
		else {
			System.out.println("Sorry! -Roy");
			return null;
		}
	}
	
	public void setParts(Vector<Part> parts) {
		this.parts = parts;
	}
	
	public void removeAll(){
		partAmount = 0;
	}
	
	public void addParts(Part part){
		partAmount++;
		part.setX(this.x + 25 + (double)random.nextInt(70));
		part.setY(this.y + 15 + (double)random.nextInt(75));
		parts.add(part);
	}
	
	// True sets the diverter to top lane
	// False sets the diverter to bottom lane
	public void divertLane(boolean l){
		if (l == true)
			topLane = true;
		else
			topLane = false;
	}
	
	// returns true if the diverter is set to Top lane
	// returns false if the diverter is set to Bottom Lane
	public boolean getLane(){
		return topLane;
	}
	
	public Vector<Part> getParts() {
		return parts;
	}

	public synchronized void setPartAmount(double partAmount) {
		this.partAmount = partAmount;
	}
	
	public void setTopLane(Boolean t) {
		topLane = t;
	}
	
	public Boolean getTopLane() {
		return topLane;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public int getDiverterX() {
		return diverterX;
	}

	public void setDiverterX(int diverterX) {
		this.diverterX = diverterX;
	}

	public int getDiverterY() {
		return diverterY;
	}

	public void setDiverterY(int diverterY) {
		this.diverterY = diverterY;
	}

	public boolean isPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(boolean previousPosition) {
		this.previousPosition = previousPosition;
	}

	public void setTopLane(boolean topLane) {
		this.topLane = topLane;
	}
	
	public void setHasCrate(boolean b)
	{
		hasCrate = b;
	}
	
	public void setInfo(PartInfo p)
	{
		info = p;
	}
	
	public boolean getHasCrate()
	{
		return hasCrate;
	}
	
	public PartInfo getInfo()
	{
		return info;
	}
}

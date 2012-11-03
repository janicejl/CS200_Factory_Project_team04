package Feeder;

public class Feeder {
	
	private double x;
	private double y;
	private double partAmount;
	
	private boolean topLane;
	private boolean empty;

	public Feeder(double nX, double nY){
		x = nX;
		y = nY;
		partAmount = 0;
		empty = true;
		topLane = true;
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
	
	public void removePart(){
		if (partAmount > 0){
			partAmount--;
		}
	}
	
	public void removeAll(){
		partAmount = 0;
	}
	
	public void addParts(double amt){
		partAmount += amt;
		if (partAmount > 25)	// 25 part maximum;
			partAmount = 25;
	}
	
	// True sets the diverter to top lane
	// False sets the diverter to bottom lane
	public void divertLane(boolean l){
		if (l = true)
			topLane = true;
		else
			topLane = false;
	}
	
	// returns true if the diverter is set to Top lane
	// returns false if the diverter is set to Bottom Lane
	public boolean getLane(){
		return topLane;
	}
}

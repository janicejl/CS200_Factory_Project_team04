package Feeder;

public class Feeder {
	
	private double x;
	private double y;
	private double partAmount;
	
	private boolean empty;

	public Feeder(double nX, double nY){
		x = nX;
		y = nY;
		partAmount = 0;
		empty = true;
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
	
	public void addParts(double amt){
		partAmount += amt;
	}
}

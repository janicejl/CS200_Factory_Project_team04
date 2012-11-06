package MoveableObjects;

import data.Part.PartType;


public class Bin {

	//Data
	PartType part;
	int quantity;
	
	//Constructor
	public Bin(PartType part, int quantity){
		this.part = part;
		this.quantity = quantity;
	}
	
	//Methods
	public PartType getPartType(){
		return part;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setPartType(PartType part){
		this.part = part;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	
	
}
package MoveableObjects;

import data.PartInfo;


public class Bin {

	//Data
	PartInfo part;
	int quantity;
	
	//Constructor
	public Bin(PartInfo part, int quantity){
		this.part = part;
		this.quantity = quantity;
	}
	
	//Methods
	public PartInfo getPartInfo(){
		return part;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setPartInfo(PartInfo part){
		this.part = part;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	
	
	
}
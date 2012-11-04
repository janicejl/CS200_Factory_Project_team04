package Interface.GantryFeederAgent;

public interface FeederLane {
	
	void msgHereIsAPart();
	void msgRemovePart();

	void setMaxQuantity(int quantity);
	String getName();
}

package MoveableObjects;

public class Part {

	public enum PartType{part1, part2, part3, part4, part5, part6, part7, part8, none};

	
	
	public PartType type;
	
	public Part(Part.PartType type){
		this.type = type;
	}
	public Part(){
		
	}
}

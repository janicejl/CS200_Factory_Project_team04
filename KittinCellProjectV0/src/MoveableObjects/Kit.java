package MoveableObjects;

import java.util.*;

public class Kit {
	List<Part> partsList = Collections.synchronizedList( new ArrayList<Part>() );
	
	public List<Part> peekParts() {
		return partsList;
	}

}

package Interface.KitRobotAgent;

import data.*;

import java.util.*;
 

public interface KitStand {

	void msgCanIPlaceKit();

	void msgPlacingKit(Kit kit);
	
	void msgIsThereEmptyKit();
	
	void msgHereAreParts(List<Part> parts, int index);

	void msgKitIsDone(int index);

	void msgKitMoved(int i);

	void msgKitRemovedFromInspection();
	
	public void msgPlacingBadKit(Kit k,int position, List<Part> parts_needed);


}

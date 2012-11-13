package Interface.KitRobotAgent;

import data.Kit;

public interface KitRobot {

	void msgHereIsAKit(Kit kit);

	void msgMoveKitToInspection(int position);

	void msgPlaceKitAtPosition(int i);
	
	public void msgKitInspected(boolean bis_good);

}

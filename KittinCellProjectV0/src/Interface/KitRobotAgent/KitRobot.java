package Interface.KitRobotAgent;

import MoveableObjects.Kit;

public interface KitRobot {

	void msgHereIsAKit(Kit kit);

	void msgMoveKitToInspection(int position);

	void PlaceKitAtPosition(int i);

}

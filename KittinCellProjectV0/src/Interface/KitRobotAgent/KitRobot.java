package Interface.KitRobotAgent;

import data.Kit;
import data.KitConfig;

public interface KitRobot {
	
	void msgGetKits(int count);

	void msgHereIsAKit(Kit kit);

	void msgMoveKitToInspection(int position);

	void msgPlaceKitAtPosition(int i);
	
	public void msgKitInspected(KitConfig kit_config);

	
	public void msgKitAtInspection();

}

package Interface.FCSAgent;

import java.util.*;

import data.KitInfo;
import data.Part;
import data.PartInfo;
import MoveableObjects.*;

public interface FCS {

	public void msgHereIsKitConfig(KitInfo kitInfo, int amount);
	
}

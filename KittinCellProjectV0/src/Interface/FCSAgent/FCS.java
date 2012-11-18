package Interface.FCSAgent;

import java.util.*;

import data.Part;
import data.PartInfo;
import MoveableObjects.*;

public interface FCS {

	public void msgHereAreBins(Vector<Bin> binsList);
	public void mgStartKitProduction(List<PartInfo> kitRecipe, int numKits);
	
}

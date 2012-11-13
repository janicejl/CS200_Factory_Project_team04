package Interface.FCSAgent;

import java.util.*;

import data.Part;
import MoveableObjects.*;

public interface FCS {

	public void msgHereAreBins(Vector<Bin> binsList);
	public void mgStartKitProduction(List<Part.PartType> kitRecipe, int numKits);
	
}

package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class KitConfig {

	public enum KitState {GOOD,MISSING_PARTS,BAD_PARTS,NOT_SET};
	
	public KitState kit_state;
	// adi, this will be the list you will put the parts that are missing from the kit
	public List<Part> missing_part_list = Collections.synchronizedList(new ArrayList<Part>());
	
	public KitConfig()
	{
		kit_state = KitState.NOT_SET;
	}

}

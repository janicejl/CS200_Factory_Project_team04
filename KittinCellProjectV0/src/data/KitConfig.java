package data;

public class KitConfig {

	public enum KitState {GOOD,MISSING_PARTS,BAD_PARTS,NOT_SET};
	
	public KitState kit_state;
	
	
	public KitConfig()
	{
		kit_state = KitState.NOT_SET;
	}

}

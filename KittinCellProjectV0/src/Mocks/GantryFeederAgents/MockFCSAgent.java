package Mocks.GantryFeederAgents;

import data.KitInfo;
import Interface.FCSAgent.FCS;
import MoveableObjects.Bin;

public class MockFCSAgent extends MockAgent implements FCS {

	public MockFCSAgent(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsKitConfig(KitInfo kitInfo, int amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgAddBin(Bin bin) {
		// TODO Auto-generated method stub

	}

}

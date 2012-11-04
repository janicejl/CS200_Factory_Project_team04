package Agents.GantryFeederAgents;

import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;

public class GantryAgent extends Agent implements Gantry {

	
	@Override
	public void msgGiveFeederParts(Feeder f1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForParts() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}


}

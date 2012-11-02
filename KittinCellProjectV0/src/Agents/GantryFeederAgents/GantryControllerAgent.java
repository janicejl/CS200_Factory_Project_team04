package Agents.GantryFeederAgents;

import java.util.ArrayList;

import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import MoveableObjects.Part.PartType;

public class GantryControllerAgent extends Agent implements GantryController {

	

	@Override
	public void msgBinConfiguration(ArrayList<Bin> bins,
			ArrayList<Gantry> gantries) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGantryAdded(Gantry gantry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedThisPart(PartType p, Feeder f1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneDeliveringParts(Gantry gantry) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}

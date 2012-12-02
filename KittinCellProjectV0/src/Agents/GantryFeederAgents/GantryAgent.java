package Agents.GantryFeederAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.PartInfo;
import server.Server;
import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class GantryAgent extends Agent implements Gantry{

	enum FeederState {NeedParts, GettingBin, NeedPurge};
	enum GantryEvents {GotBin, FeederReadyForBin, GantryAtFeeder}
	enum GantryState {GettingBin,None};
	List<MyFeeder> feeder_list = Collections.synchronizedList(new ArrayList<MyFeeder>());
	List<GantryEvents> gantry_events = Collections.synchronizedList(new ArrayList<GantryEvents>());
	GantryState gantry_state;
	Server server;
	MyFeeder current_feeder_servicing;
	int count;
	ArrayList<Boolean> feederBoxExists;
	EventLog log;
	
	class MyFeeder
	{
		public Feeder feeder;
		public PartInfo part_type;
		public FeederState state;
		//public boolean hasBin;
		
	}
	
	public GantryAgent(String name,Server server)
	{
		this.server = server;
		gantry_state = GantryState.None;
		feederBoxExists = new ArrayList<Boolean>();
		for(int i = 0; i < 4; i ++){
			feederBoxExists.add(new Boolean(false));
		}
	}
	
	
	
	/**
	 * This message is sent from the feeder indicating that it 
	 * needs a specific part type
	 * @param p this is the PartInfo that the feeder needs
	 * @param feeder this is the Feeder that needs parts 
	 */
	public void msgNeedThisPart(PartInfo p, Feeder feeder)
	{
		MyFeeder new_feeder = new MyFeeder();
		new_feeder.part_type = p;
		new_feeder.feeder = feeder;
		new_feeder.state = FeederState.NeedParts;
		feeder_list.add(new_feeder);
	/*	log.add(new LoggedEvent("msgNeedThisPart sent from Feeder " + feeder.getNumber()
				+ " requesting part " + p.getDescription()));*/
		stateChanged();
		
	}

	/**
	 * This message is sent from a feeder that needs a bin moved
	 * to the purge position?
	 * @param feeder is the feeder that needs the bin purged
	 */
	public void msgNeedBinPurged(Feeder feeder)
	{
		MyFeeder new_feeder = new MyFeeder();
		new_feeder.feeder = feeder;
		new_feeder.state = FeederState.NeedPurge;
		feeder_list.add(new_feeder);
	//	log.add(new LoggedEvent("msgNeedBinPurged sent from Feeder " + feeder.getNumber()));
		stateChanged();
	}

	/**
	 * This message is sent by the Server to indicate that the gantry
	 * animation has a bin
	 */
	public void msgAnimationHasBin()
	{
		print("Gantry: I gots the bin!!!");
		gantry_events.add(GantryEvents.GotBin);
	//	log.add(new LoggedEvent("msgAnimationHasBin sent from Server"));
		stateChanged();
	}
	
	/**
	 * This message is sent by the feeder to indicate that it is ready
	 * for parts?
	 */
	public void msgReadyForParts()
	{
		print("Gantry: Feeder ready for party");
		gantry_events.add(GantryEvents.FeederReadyForBin);
	//	log.add(new LoggedEvent("msgReadyForParts received from Feeder"));
		stateChanged();
	}
	
	/**
	 * This message is sent by the Server to indicate that the
	 * animated gantry is at the feeder.
	 */
	public void msgGantryAtFeeder()
	{
		print("Gantry: Gantry at feeder");
		gantry_events.add(GantryEvents.GantryAtFeeder);
	//	log.add(new LoggedEvent("msgGantryAtFeeder sent from Server"));
		stateChanged();
	}
	

	
	@Override
	protected boolean pickAndExecuteAnAction() {

		if(gantry_state == GantryState.GettingBin)
		{
			if(!gantry_events.isEmpty())
			{
				for(GantryEvents event:gantry_events)
				{
					if(event == GantryEvents.GotBin)
					{
						gantry_events.remove(event);
						AskFeederIfReadyForParts();
						return true;
					}
				}
			}
			
			if(!gantry_events.isEmpty())
			{
				for(GantryEvents event:gantry_events)
				{
					if(event == GantryEvents.FeederReadyForBin)
					{
						gantry_events.remove(event);
						MoveGantryToFeeder();
						return true;
					}
				}
			}

			if(!gantry_events.isEmpty())
			{
				for(GantryEvents event:gantry_events)
				{
					if(event == GantryEvents.GantryAtFeeder)
					{
						gantry_events.remove(event);
						GivePartsToFeeder();
						return true;
					}
				}
			}
		}
		
		
		if(gantry_state != GantryState.GettingBin)
		{
			if(!feeder_list.isEmpty())
			{
				for(MyFeeder feeder:feeder_list)
				{
					if(feeder.state == FeederState.NeedPurge)
					{
						MoveBinToPurge(feeder);
						return true;
					}
				}
			}
			if(!feeder_list.isEmpty())
			{
				for(MyFeeder feeder:feeder_list)
				{
					if(feeder.state == FeederState.NeedParts)
					{
						if(!feederBoxExists.get(feeder.feeder.getNumber())){
							GoGetBin(feeder);
							return true;
						}
					}
				}
			}
			
		}
		
		
		return false;
	}
	
	private void AskFeederIfReadyForParts()
	{
		//current_feeder_servicing.feeder.msgHaveParts(this);
	}
	
	private void MoveGantryToFeeder()
	{
		server.execute("Load Feeder", current_feeder_servicing.feeder.getNumber());
	}
	
	private void GivePartsToFeeder()
	{
		print("Gantry: Giving parts to feeder");
		feederBoxExists.set(current_feeder_servicing.feeder.getNumber(), true);
		Bin bin = new Bin(current_feeder_servicing.part_type, 10);
		//need to change how many parts to feed
		server.execute("Feed Feeder",current_feeder_servicing.feeder.getNumber(),current_feeder_servicing.part_type, 10);
		current_feeder_servicing.feeder.msgHereAreParts(bin);
		feeder_list.remove(current_feeder_servicing);
		current_feeder_servicing = null;
		gantry_state = GantryState.None;
	}
	
	private void MoveBinToPurge(MyFeeder feeder)
	{
		print("Gantry: Moving bin to purge phase");
		server.execute("Idle Bin",feeder.feeder.getNumber());
		feederBoxExists.set(feeder.feeder.getNumber(), false);
		gantry_state = GantryState.None;
		feeder_list.remove(feeder);
	}
	
	private void GoGetBin(MyFeeder feeder)
	{
		print("Gantry: Getting the bin");
		gantry_state = GantryState.GettingBin;
		feeder.state = FeederState.GettingBin;
		current_feeder_servicing = feeder;
		server.execute("Make PartsBox", feeder.part_type);
		server.execute("Pickup Box");
		//server.execute("Idle Bin",feeder.feeder.getNumber());
		
	}
	
	

}
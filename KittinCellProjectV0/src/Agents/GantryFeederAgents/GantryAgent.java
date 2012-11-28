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

public class GantryAgent extends Agent implements Gantry{

	enum FeederState {NeedParts, GettingBin};
	enum GantryEvents {
		GotBin, FeederReadyForBin, GantryAtFeeder}
	enum GantryState {GettingBin,None};
	List<MyFeeder> feeder_list = Collections.synchronizedList(new ArrayList<MyFeeder>());
	List<GantryEvents> gantry_events = Collections.synchronizedList(new ArrayList<GantryEvents>());
	GantryState gantry_state;
	Server server;
	MyFeeder current_feeder_servicing;
	int count;
	
	class MyFeeder
	{
		public Feeder feeder;
		public PartInfo part_type;
		public FeederState state;
		
	}
	
	public GantryAgent(String name,Server server)
	{
		this.server = server;
		gantry_state = GantryState.None;
	}
	
	
	
	public void msgNeedThisPart(PartInfo p, Feeder feeder)
	{
		MyFeeder new_feeder = new MyFeeder();
		new_feeder.part_type = p;
		new_feeder.feeder = feeder;
		new_feeder.state = FeederState.NeedParts;
		feeder_list.add(new_feeder);
		stateChanged();
		
	}

	public void msgAnimationHasBin()
	{
		print("Gantry: I gots the bin!!!");
		gantry_events.add(GantryEvents.GotBin);
		stateChanged();
	}
	
	public void msgReadyForParts()
	{
		print("Gantry: Feeder ready for party");
		gantry_events.add(GantryEvents.FeederReadyForBin);
		stateChanged();
	}
	
	public void msgGantryAtFeeder()
	{
		print("Gantry: Gantry at feeder");
		gantry_events.add(GantryEvents.GantryAtFeeder);
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
					if(feeder.state == FeederState.NeedParts)
					{
						GoGetBin(feeder);
						return true;
					}
				}
			}
		}
		
		
		return false;
	}
	
	private void AskFeederIfReadyForParts()
	{
		current_feeder_servicing.feeder.msgHaveParts(this);
	}
	
	private void MoveGantryToFeeder()
	{
		//play animation to move gantry to feeder
	}
	
	private void GivePartsToFeeder()
	{
		print("Gantry: Giving parts to feeder");
		
		Bin bin = new Bin(current_feeder_servicing.part_type, 10);
		for(int i=0;i<10;i++)
		{
			server.execute("Feed Feeder",current_feeder_servicing.feeder.getNumber(),current_feeder_servicing.part_type);
		}
		current_feeder_servicing.feeder.msgHereAreParts(bin);
		gantry_state = GantryState.None;
	}
	
	private void GoGetBin(MyFeeder feeder)
	{
		print("Gantry: Getting the bin");
		gantry_state = GantryState.GettingBin;
		feeder.state = FeederState.GettingBin;
		current_feeder_servicing = feeder;
		server.execute("Make PartsBox", feeder.part_type);
		server.execute("Load Feeder", feeder.feeder.getNumber());
		server.execute("Idle Bin",feeder.feeder.getNumber());
		
	}
	
	

	//Data
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*String name;
	Feeder currentFeeder;
	Bin currentBin;
	GantryController gc;
	Server app;
	enum FeederState{ready, pending, notReady};
	public enum AnimationState{noAction, loadingFeeder, atFeeder};
	FeederState fstate;
	public AnimationState astate;
	public EventLog log;
	
	public GantryAgent(String name, Server app){
		this.name = name;
		currentFeeder = null;
		currentBin = null;
		fstate = FeederState.notReady;
		this.app = app;
		log = new EventLog();
		astate = AnimationState.noAction;
		
	}
	
	//Messages
	
	@Override
	public void msgGiveFeederParts(Feeder f1, Bin b) {
		this.currentFeeder = f1;
		this.currentBin = b;
		log.add(new LoggedEvent("msgGiveFeederParts received from GantryController"));
		print("msgGiveFeederParts received from GantryController");
		stateChanged();
	}

	@Override
	public void msgReadyForParts() {
		fstate = FeederState.ready;
		log.add(new LoggedEvent("msgReadyForParts received from Feeder"));
		print("msgReadyForParts received from Feeder, feeder is ready");
		stateChanged();
	}
	
	public void msgGantryAtFeeder(){
		print("At Feeder, ready to dump parts");
		astate = AnimationState.atFeeder;
		stateChanged();
		System.out.println(currentFeeder);
		System.out.println(fstate);
	}
	
	
	
	
	//Scheduler
	
	@Override
	public boolean pickAndExecuteAnAction() {
		System.out.println("RUNNING");
		if(currentFeeder != null && fstate == FeederState.ready && astate == AnimationState.atFeeder){
			print("in Gantry scheduler, feeder is ready");
			GivePartsToFeeder();
			astate = AnimationState.noAction;
			return true;
		}
		else if(currentFeeder != null && fstate == FeederState.notReady){
			PrepareToGiveParts();
			return true;
		}
		
//		else if(astate == AnimationState.loadingFeeder){
//			astate = AnimationState.atFeeder;
//			return true;
//		}
		
		return false;
	}

	//Actions
	
	private void GivePartsToFeeder(){
		currentFeeder.msgHereAreParts(currentBin);
		print("Gave parts to feeder " + currentFeeder.getName());
		//DoFillFeeder()
		
		while(currentBin.getQuantity() > 0){
			app.execute("Feed Feeder", currentFeeder.getNumber(), currentBin.getPartInfo());
			currentBin.setQuantity(currentBin.getQuantity()-1);
		}
		
		app.execute("Idle Bin", currentFeeder.getNumber());
		currentFeeder = null;
		print("Feeder removed from gantry.");
		fstate = FeederState.notReady;
		gc.msgDoneDeliveringParts(this);
	}
	
	private void PrepareToGiveParts(){
		print("Preparing to give parts to feeder " + currentFeeder.getName());
		app.execute("Make PartsBox", currentBin.getPartInfo());
		app.execute("Load Feeder", currentFeeder.getNumber());
		currentFeeder.msgHaveParts(this);
		fstate = FeederState.pending;
		print("setting state to loading feeder");

		astate = AnimationState.loadingFeeder;

	}
	
	//Extras
	
	public void setFeeder(Feeder f1){
		this.currentFeeder = f1;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setGantryController(GantryController gc){
		this.gc = gc;
	}
	
	*/
}
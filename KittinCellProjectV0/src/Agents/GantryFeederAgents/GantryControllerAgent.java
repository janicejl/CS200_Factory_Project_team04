package Agents.GantryFeederAgents;

import java.util.Vector;

import server.Server;

import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import MoveableObjects.Part.PartType;

public class GantryControllerAgent extends Agent implements GantryController {

	//Data
	Vector<Bin> bins = new Vector<Bin>();
	Vector<MyGantry> gantries = new Vector<MyGantry>();
	Vector<MyFeeder> requests = new Vector<MyFeeder>();
	Server app;
	enum FeederState{requested, sentGantry};
	enum GantryState{waiting, delivering};
	
	class MyFeeder{
		Feeder f1;
		PartType type;
		FeederState fstate;
		
		public MyFeeder(Feeder f1, PartType p){
			this.f1 = f1;
			this.type = p;
			this.fstate = FeederState.requested;
		}		
	}
	
	class MyGantry{
		Gantry g1;
		GantryState gstate;
		
		public MyGantry(Gantry g1){
			this.g1 = g1;
			this.gstate = GantryState.waiting;
		}		
	}
	
	
	public GantryControllerAgent(Server app){
		this.app = app;
	}
	
	//Messages

	@Override
	public void msgBinConfiguration(Vector<Bin> bins) {
		this.bins = bins;
		stateChanged();
	}

	@Override
	public void msgGantryAdded(Gantry gantry) {
		MyGantry temp = new MyGantry(gantry);
		this.gantries.add(temp);
		stateChanged();
	}

	@Override
	public void msgNeedThisPart(PartType p, Feeder f1) {
		MyFeeder temp = new MyFeeder(f1, p);
		this.requests.add(temp);
		stateChanged();
	}

	@Override
	public void msgDoneDeliveringParts(Gantry gantry) {
		MyGantry temp = new MyGantry(gantry);
		for(int i = 0; i < gantries.size(); i++){
			if(gantries.get(i).equals(temp)){
				gantries.get(i).gstate = GantryState.waiting;
				i = gantries.size();
			}
		}
		stateChanged();		
	}
	
	
	//Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		for(MyFeeder f:requests){
			if(f.fstate == FeederState.requested){
				for(MyGantry g: gantries){
					if(g.gstate == GantryState.waiting){
						SendGantry(g, f);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	
	//Actions
	private void SendGantry(MyGantry g, MyFeeder f){
		for(int i = 0; i<bins.size(); i++){
			if(bins.get(i).equals(f.type)){
				g.g1.msgGiveFeederParts(f.f1, bins.get(i));
				f.fstate = FeederState.sentGantry;
				g.gstate = GantryState.delivering;
				requests.remove(f);
				i = bins.size();
			}
		}
	}
	
	//Extras

}
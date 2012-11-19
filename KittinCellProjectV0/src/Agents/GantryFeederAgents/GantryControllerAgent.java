package Agents.GantryFeederAgents;

import java.util.Vector;

import server.Server;

import Agent.Agent;
import Interface.FCSAgent.FCS;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;
import data.PartInfo;

public class GantryControllerAgent extends Agent implements GantryController {

	//Data
	Vector<Bin> bins = new Vector<Bin>();
	Vector<MyGantry> gantries = new Vector<MyGantry>();
	Vector<MyFeeder> requests = new Vector<MyFeeder>();
	Server app;
	enum FeederState{requested, sentGantry};
	enum GantryState{waiting, delivering};
	FCS fcs;
	public EventLog log;
	
	class MyFeeder{
		Feeder f1;
		PartInfo type;
		FeederState fstate;
		
		public MyFeeder(Feeder f1, PartInfo p){
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
		log = new EventLog();
	}
	
	//Messages

	@Override
	public void msgBinConfiguration(Vector<Bin> thebins) {
		for(int i = 0; i < thebins.size(); i++){
			this.bins.add(thebins.get(i));
		}
		log.add(new LoggedEvent("msgBinConfiguration received from FCS"));
		stateChanged();
	}

	@Override
	public void msgGantryAdded(Gantry gantry) {
		MyGantry temp = new MyGantry(gantry);
		this.gantries.add(temp);
		stateChanged();
	}

	@Override
	public void msgNeedThisPart(PartInfo p, Feeder f1) {
		MyFeeder temp = new MyFeeder(f1, p);
		this.requests.add(temp);
		log.add(new LoggedEvent("msgNeedThisPart received from Feeder"));
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
		log.add(new LoggedEvent("msgDoneDeliveringParts received from Gantry"));
		stateChanged();		
	}
	
	
	//Scheduler
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
		for(MyFeeder f:requests){
			print("MyFeeder found in requests in GC scheduler.");
			if(f.fstate == FeederState.requested){
				print("MyFeeder state is requested in GC scheduler.");
				for(MyGantry g: gantries){
					print("MyGantry found in gantries GC scheduler.");
					if(g.gstate == GantryState.waiting){
						print("MyGantry state is waiting in GC scheduler.");
						print("SendGantry() called");
						SendGantry(g, f);
						return true;
					}
				}
			}
		}
		print("Nothing Chosen in GC scheduler");
		return false;
	}
	
	
	//Actions
	private void SendGantry(MyGantry g, MyFeeder f){
		for(int i = 0; i<bins.size(); i++){
			print("inside for loop in GC, SendGantry");
			if(bins.get(i).getPartInfo().equals(f.type)){
				print("found bin of correct type.");
				g.g1.msgGiveFeederParts(f.f1, bins.get(i));
				f.fstate = FeederState.sentGantry;
				g.gstate = GantryState.delivering;
				requests.remove(f);
				i = bins.size();
			}
		}
	}
	
	//Extras

	public void setFCS(FCS fcs){
		this.fcs = fcs;
	}
}
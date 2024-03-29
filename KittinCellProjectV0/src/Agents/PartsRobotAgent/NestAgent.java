package Agents.PartsRobotAgent;

import  Agent.*;
import Agents.VisionAgent.VisionAgent;
import server.Server;
import data.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import Interface.PartsRobotAgent.*;
import Interface.VisionAgent.Vision;

public class NestAgent extends Agent implements Nest{

	//Data
	public PartInfo partinfo;
	public Part[] nestslots = new Part[8];
	public List<Part> extraparts = new ArrayList<Part>();
	PartsRobot partsrobot;
	Lane lane;
	Vision camera;
	String name;
	Server server;
	boolean hadeight = false;
		
	boolean allowextraparts = false;
	public int index;
	Random generator = new Random();
	

	public enum LaneStatus {hasPart, gettingParts, noAction}
	public enum PartsRobotStatus {wantsParts, waitingForParts, noAction,readyforpart}
	public enum NestStatus {badParts, noParts, needCheck, noAction}
	public enum AnimationStatus {purging, needPurge, needSettle, noAction}
	
	public LaneStatus lanestate = LaneStatus.noAction;
	public PartsRobotStatus partsrobotstate= PartsRobotStatus.noAction;
	public NestStatus neststate = NestStatus.noParts;
	public AnimationStatus animationstate = AnimationStatus.noAction;
	Random rn = new Random();
	Nest nest;
	
	public NestAgent(Lane lane, final Vision camera, int index)
	{
		this.lane = lane;
		this.camera = camera;
		this.index = index;
		name = "NestAgent " + index;
		for(int i = 0; i<8; i++){
			nestslots[i]=null;
		}

	}
	
	public NestAgent(int index,Server server)
	{
		this.lane = null;
		this.camera = null;
		this.index = index;
		this.server = server;
		name = "NestAgent " + index;
		for(int i = 0; i<8; i++){
			nestslots[i]=null;
		}
	}	

	public void setPartsRobotAgent(PartsRobot robot){
		this.partsrobot = robot;
	}
	//Messages
	public void msgCanIPlacePart(Lane lane)
	{
			lanestate = LaneStatus.hasPart;
			stateChanged();
	}
	
	public void msgHereIsPart(Part p)
	{
		boolean acceptedpart = false;
			for(int i = 0; i < 8; i++)
			{
				if(nestslots[i]== null)
				{
					nestslots[i] = p;
					acceptedpart =true;
					if(i == 0){
						neststate = NestStatus.needCheck;
					}
					if(i==7){
						hadeight = true;
					}
					break;
				}
			}
			if(!acceptedpart){
				extraparts.add(p);
			}

			stateChanged();
	}

	public void msgBadParts()
	{
			neststate = NestStatus.badParts;
			partsrobotstate = PartsRobotStatus.wantsParts;
			print("Received message badParts");
			stateChanged();
	}

	public void msgGetPart()
	{
		partsrobotstate = PartsRobotStatus.readyforpart;
		stateChanged();
	}

	public void msgNeedThisPart(PartInfo p)
	{
			boolean order = true;
			for(int i = 0; i<8; i++){
				if(nestslots[i] != null)
					order = false;
			}
			for(int i = 0; i<8; i++)
			{
				if(nestslots[i] != null){
					if(!nestslots[i].info.getName().equals(p.getName())){
						animationstate = AnimationStatus.needPurge;
						order = true;
					}
						
				}
			}
			if(animationstate != AnimationStatus.needPurge && !order){
				partsrobot.msgPartsApproved(this.index);
				camera.msgChangeNumberOfNests();
				camera.msgImFull(this);
			}
			if(order){
				partsrobotstate = PartsRobotStatus.wantsParts;
			}

					
			partinfo = p;
			
			stateChanged();
	}

	public void msgAnimationDone(){
		if(animationstate == AnimationStatus.purging){
			neststate = NestStatus.noParts;
			partsrobotstate = PartsRobotStatus.wantsParts;
			animationstate = AnimationStatus.noAction;

		}
		stateChanged();
	}
	public void msgToggleNestOverflow(){
		if(!allowextraparts){
		allowextraparts = true;
		}
		else{
			allowextraparts = false;
		}
		stateChanged();
	}
	
	public void msgPurge(){
		animationstate = AnimationStatus.needPurge;
		stateChanged();
	}
	

	
	//Scheduler:
	
	public boolean pickAndExecuteAnAction()
	{		
		
		if(neststate == NestStatus.badParts || animationstate == AnimationStatus.needPurge)
		{
			purgeNest();
			return true;
		}
		if(partsrobotstate == PartsRobotStatus.readyforpart)
		{
			givePart();
			return true;
		}
		if(animationstate == AnimationStatus.needSettle)
		{
			settleNest();
			return true;
		}
		
		if(partsrobotstate == PartsRobotStatus.wantsParts)
		{
			askForParts();
			return true;
		}
		
		if(lanestate == LaneStatus.hasPart)
		{
			for(int i = 0; i<8; i++)
			{
				if(nestslots[i] == null)
				{
				acceptPart();
				return true;
				}
			}
		}
		if(lanestate == LaneStatus.hasPart){
			int x = generator.nextInt(10);
			if(x==1 && false){            // =========SWITCH false with 'allowextraparts' boolean to enable nest Overflow =========================
				acceptPart();
				return true;
			}
		}
		
		if(neststate == NestStatus.needCheck)
		{
			askForInspection();
			return true;
			
		}
			
		if(nestslots[0] == null && nestslots[1]!= null)
		{
			settleNest();
			return true;
		}
		
		
		
		return false;
	}

	//Actions

	
	private void purgeNest(){
		//Animation Call
		server.execute("Purge Nest", index-1);
		print("Purging");

		
		for(int i = 0; i<8; i++)
		{
			nestslots[i] = null;
		}
		extraparts.clear();
		lane.msgPurge();
		animationstate = AnimationStatus.noAction;
		neststate = NestStatus.noParts;
		hadeight = false;

	}

	private void acceptPart()
	{	
		print("Ready to take the part" + lanestate);
		lanestate = LaneStatus.noAction;
		lane.msgReadyForPart();
	}
	
	private void askForInspection()
	{
		print("I am full and need inspection");
		neststate = NestStatus.noAction;
		if(camera!= null)
		camera.msgImFull(this);
	}
	
	private void settleNest()
	{
		//NestSettlingAnimation
		//gui.DoSettleNest(); // Settle Nest so that the parts behind in the nest move to the front
		for(int i = 0; i<7; i++){
			if(nestslots[i] == null && nestslots[i+1]!= null){
				nestslots[i] = nestslots[i+1];
				nestslots[i+1] = null;
				if(i==0)
					neststate = NestStatus.needCheck;
			}
			
		}
		if(nestslots[7]==null && extraparts.size()>0){
			nestslots[7]=extraparts.get(0);
			extraparts.remove(0);
		}
		
		//if(lanestate == LaneStatus.hasPart && nestslots[7]==null){
		//	acceptPart();
		//}
	}
	private void givePart()
	{
		print("Giving PartsRobot part");
		partsrobot.msgHereIsPart(nestslots[0]);
		nestslots[0] = null;
		partsrobotstate = PartsRobotStatus.waitingForParts;
		settleNest();
		boolean stillempty = true;
		for(int i = 0; i<8; i++){
			if(nestslots[i]!= null){
				stillempty = false;
			}
		}
		if(stillempty){
			hadeight = false;
			askForParts();
		}
		if(nestslots[0]!=null){
			neststate = NestStatus.needCheck;
		}
	}
		
	private void askForParts()
	{
		print("Please give me a part");
		if(lane!= null)
		lane.msgNeedThisPart (partinfo);
		partsrobotstate = PartsRobotStatus.waitingForParts;
	}
	
	public String getName(){
		return name;
	}

	public Integer getNumber() {
		return index;
	}

	public PartInfo getPartInfo() {
		return partinfo;
	}
	
	public void setVisionAgent(final VisionAgent camera){
		this.camera = camera;
		
	/*TimerTask timer_task = new TimerTask()
		{  
			public void run()
			{ System.out.println("boooom");
				 if(lanestate != LaneStatus.gettingParts)
				 {
					 System.out.println("boooom2");
					 int count = 0;
					 for(int i=0; i< nestslots.length;i++)
					 {
						 if(nestslots == null)
						 {
							 count++;
						 }
					 }
					 if(count!= 0 || partsrobotstate == PartsRobotStatus.waitingForParts )
					 {
						 System.out.println("boooom3");
						 camera.msgImFull(nest);
					 }
				 }
			
			}
		};*/
		
		/*timer.scheduleAtFixedRate(new TimerTask(){
			public void run()
			{ 
				 if(lanestate != LaneStatus.gettingParts)
				 {
					 
					 int count = 0;
					 for(int i=0; i< nestslots.length;i++)
					 {
						 if(nestslots != null)
						 {
							 count++;
						 }
					 }
					 System.out.println(count);
					 System.out.println(partsrobotstate);
					 System.out.println(neststate);
					 System.out.println(lanestate);
					 if(count!= 0 && partsrobotstate == PartsRobotStatus.waitingForParts && neststate == NestStatus.noAction && lanestate == LaneStatus.noAction)
					 {
						 System.out.println("boooom");
						 camera.msgImFull(nest);
					 }
				 }
			
			}
		
		}, 1000,1000);*/
		
		
		
		/*
		 public void run(){ 
			 if(lanestate != LaneStatus.gettingParts)
			 {
				 int count = 0;
				 for(int i=0; i< nestslots.length;i++)
				 {
					 if(nestslots == null)
					 {
						 count++;
					 }
				 }
				 if(count!= 0 && partsrobotstate == PartsRobotStatus.waitingForParts)
				 {
					 camera.msgImFull(nest);
				 }
			 }
		
		 }*/
		
		
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setLane(Lane newlane){
		this.lane = newlane;
	}
	private class TimerListener extends TimerTask{

		@Override
		public void run() {
			if(partinfo!=null&&nestslots[0]!=null){
				neststate = NestStatus.needCheck;
				stateChanged();
			}
		}

		
	}
	
}

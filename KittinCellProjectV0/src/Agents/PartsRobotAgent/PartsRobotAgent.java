package Agents.PartsRobotAgent;
import Agent.*;
import Interface.PartsRobotAgent.*;
import Interface.KitRobotAgent.*;
import data.*;
import Agents.KitRobotAgents.*;
import Agents.VisionAgent.VisionAgent;

import java.util.*;

import server.Server;

public class PartsRobotAgent extends Agent{

	String name = "PartsRobotAgent";
	
	
	List<VisionAgent> cameras = new ArrayList<VisionAgent>();
	KitStand kitstand;
	boolean camerahasrecipe = false;
	Server server;
	TestGUI gui;

	int count = 0;
	List <Part.PartType> recipe = new ArrayList<Part.PartType>();
	List <Part> camerarecipe = new ArrayList<Part>();
	List <MyNest> nests = new ArrayList<MyNest>();
	Gripper[] grippers = new Gripper[4];
	RobotState state;
	CurrentKit currentkit = CurrentKit.kit1;
	int currentnest = -1;
	public AnimationStatus animationstate = AnimationStatus.atHome;

	public enum AnimationStatus {atHome, movingToNest, atNest, movingToStand, atStand, movingHome,waitingForPart}//Will need to use when integrating with the animation

	private enum CurrentKit{kit1, kit2}
	private enum RobotState{mustOrderParts,PartsOrdered}

	//Constructor must instantiate all the MyNests and have references to all the nests
	private class MyNest
	{
		Part.PartType type;
		int index;
		NestAgent nest;
		NestStatus state = NestStatus.noAction;
		
		private MyNest(NestAgent nest,int ind){
			this.nest = nest;
			index = ind;
		}
		private MyNest (int ind){
			index = ind;
		}
		
	}
	private enum NestStatus{noAction, assigned, hasPart,skipped}
	private enum KitStatus{notAvailable, available, pending}


	public class MyKit
	{
		int index;
		List <Part.PartType> partsneeded;
		KitStatus state = KitStatus.notAvailable;
		public MyKit(int ind){
			index = ind;
			partsneeded = new ArrayList<Part.PartType>();
		}
	}
	MyKit kit1;
	MyKit kit2;

	private class Gripper
	{
		Part p;
		int destinationkit;
		boolean full = false;
		int nestindex = -1;
		private Gripper(){};
	}
	
	public PartsRobotAgent(KitStand stand, Server server)
	{
		kitstand = stand;
		this.server = server;
		kit1 = new MyKit(1);
		kit2 = new MyKit(2);
		for (int i = 0; i<4; i++)
		{
			grippers[i] = new Gripper();
		}
		for(int i = 0; i<8; i++){
			nests.add(new MyNest(i+1));
		}
	}
	
	public PartsRobotAgent(List <NestAgent> nestagents, KitStand stand,Server server)
	{
		int index = 1;
		for(NestAgent nest:nestagents)
		{
			MyNest mn = new MyNest(nest,index);
			nests.add(mn);
			index++;
		}
		kitstand = stand;
		this.server = server;
		kit1 = new MyKit(1);
		kit2 = new MyKit(2);
		for (int i = 0; i<4; i++)
		{
			grippers[i] = new Gripper();
		}
	}
	
	public PartsRobotAgent(List <NestAgent> nestagents, List <VisionAgent> cameralist, KitStand stand,Server server)
	{
		int index = 1;
		for(NestAgent nest:nestagents)
		{
			MyNest mn = new MyNest(nest,index);
			nests.add(mn);
			index++;
		}
		for(VisionAgent cam:cameralist){
			cameras.add(cam);
		}
		kitstand = stand;
		this.server = server;
		kit1 = new MyKit(1);
		kit2 = new MyKit(2);
		for (int i = 0; i<4; i++)
		{
			grippers[i] = new Gripper();
		}
	}
	
	
	//Messages:

	public void msgMakeThisKit(List<Part.PartType> kitrecipe, int ct){ //will pass in KitInfo instead of List of parts
		count = ct;
		recipe = kitrecipe;
		camerahasrecipe = false;
		kit1.partsneeded.clear();
		kit2.partsneeded.clear();
		for(Part.PartType type : kitrecipe){
			kit1.partsneeded.add(type);
			kit2.partsneeded.add(type);
		}
		for(Part.PartType type: kitrecipe){
			camerarecipe.add(new Part(type));
		}
		kit1.state = KitStatus.notAvailable;
		kit2.state = KitStatus.notAvailable;
		state = RobotState.mustOrderParts;
		stateChanged();
	}
	public void msgPartsApproved(int nestindex){
		nests.get(nestindex-1).state = NestStatus.hasPart;
		print("PartReady at nest " + nestindex);
		stateChanged();
	}

	public void msgEmptyKit(int position){
		if(position == 1)
		{
			kit1.state = KitStatus.available;
		}
		else
		{
			kit2.state = KitStatus.available;
		}
		stateChanged();
		
	}

	public void msgAnimationDone(){
		if(animationstate == AnimationStatus.movingToNest){
			print("Arrived at Nest");
			animationstate = AnimationStatus.atNest;
			stateChanged();
		}
		else if(animationstate == AnimationStatus.movingToStand){
			print("Arrived at Stand");
			animationstate = AnimationStatus.atStand;
			stateChanged();
		}
		else if(animationstate == AnimationStatus.movingHome){
			animationstate = AnimationStatus.atHome;
		}
		
	}
	public void msgHereIsPart(Part p)
	{
		for(int i = 0; i<4; i++)
		{
			if(grippers[i].nestindex == currentnest)
			{
				print("taking part");
				grippers[i].p = p;
			}
		}
		currentnest = -1;
		animationstate = AnimationStatus.movingHome;
		stateChanged();
	}

	//Scheduler:

	public boolean pickAndExecuteAnAction(){
		
	
	if(recipe!= null && state == RobotState.mustOrderParts)
	{
		orderParts();
		return true;
	}
	if(!recipe.isEmpty() && !camerahasrecipe)
	{
		giveCameraRecipe();
		return true;
	}
	if(count > 1 && (kit1.state == KitStatus.notAvailable || kit2.state == KitStatus.notAvailable)){
		requestEmptyKit();
		return true;
	}
	if(count == 1 && kit1.state == KitStatus.notAvailable && kit2.state == KitStatus.notAvailable){
		requestEmptyKit();
		return true;
	}
	if((kit1.partsneeded.isEmpty() || kit2.partsneeded.isEmpty()) && grippersEmpty() && !recipe.isEmpty())
	{
		count--;
		print("A kit is finished (" + count + " to go)");
		kitFinished();
		return true;
	}
	if(animationstate == AnimationStatus.atStand)
	{
		placeParts();
		return true;
	}
	if(animationstate == AnimationStatus.atNest){
		getPart();
		return true;
	}	
	if(allGrippersFull() && (kit1.state == KitStatus.available || kit2.state == KitStatus.available)){
		goToStand();
		return true;
	}
	if(count!= 0 && (animationstate == AnimationStatus.movingHome || animationstate == AnimationStatus.atHome) && !allGrippersFull()){
		for(MyNest mn: nests)
		{
			if(mn.state == NestStatus.hasPart)
			{
				checkAvailableParts(mn);
				return true;
			}
		}
	}
	if(animationstate == AnimationStatus.movingHome || animationstate == AnimationStatus.atHome)
	{
		//if(kit1.state == KitStatus.available || kit2.state == KitStatus.available)
		//{
			for(int i = 0; i<4; i++)
			{
				if(grippers[i].full)
				{
					goToStand();
					return true;
				}
			}
		//}
	}
	/*if(animationstate == AnimationStatus.atNest || animationstate == AnimationStatus.atStand)
	{
		returnToStart();
		return false;
	}*/
		//print("Nothing to do, sleeping");
		return false;
	}

	//Actions:

	private void orderParts()
	{
		print("Requesting Parts from Nests");
		for(int i = 0; i < recipe.size(); i++)
		{	
			print("Assigning Part " + recipe.get(i) + " to nest " + nests.get(i).index);
			nests.get(i).nest.msgNeedThisPart(recipe.get(i));
			nests.get(i).state = NestStatus.assigned;
			nests.get(i).type = recipe.get(i);
		}
		state = RobotState.PartsOrdered;
	}

	private void giveCameraRecipe()
	{
			
		print("Giving Recipe to VisionAgents");
		List<NestAgent> nestassignments = new ArrayList<NestAgent>();
		for(MyNest mn : nests)
		{
			nestassignments.add(mn.nest);
		}
		if(!cameras.isEmpty())
		{
			for(VisionAgent camera : cameras)
			{
				print("Giving to camera");
				camera.msgHereIsSchematic(camerarecipe, nestassignments);
			}
		}
		camerahasrecipe = true;
	}
	private void requestEmptyKit()
	{
		print("Asking if there is a free available kit");
		if(kit1.state == KitStatus.notAvailable)
			kit1.state = KitStatus.pending;
		else
			kit2.state = KitStatus.pending;
		kitstand.msgIsThereEmptyKit();
	}	
		
	private void checkAvailableParts(MyNest nest)
	{
		print("Checking if I need part at nest " + nest.index);
		Part.PartType type = nest.type;
		Part.PartType parttoget = null;
		boolean kit1needsthispart = false;
		boolean kit2needsthispart = false;
		if(currentkit == CurrentKit.kit1){
			for(Part.PartType pt: kit1.partsneeded){
				if(type == pt){
					kit1needsthispart = true;
					parttoget = pt;
				}
			}
			if(parttoget!= null){
				kit1.partsneeded.remove(parttoget);
			}
			if(kit1needsthispart){
				for(int i = 0; i<4; i++){
					if(!grippers[i].full){
						grippers[i].destinationkit = 1;
						grippers[i].nestindex = nest.index;
						grippers[i].full = true;
						currentnest = nest.index;
						print("Moving To Nest " + nest.index + " to pick up part for kit1");
						//Animation Call
						animationstate = AnimationStatus.movingToNest;
						server.execute("Get Part",nest.index-1,i);
						break;

					}
				}
			}
		
		}
		if(currentkit == CurrentKit.kit2)
		{
			for(Part.PartType pt: kit2.partsneeded){
				if(type == pt){
					kit2needsthispart = true;
					parttoget = pt;
				}
			}
			if(parttoget!= null){
				kit2.partsneeded.remove(parttoget);
			}
			if(kit2needsthispart){
				for(int i = 0; i<4; i++){
					if(!grippers[i].full){
						grippers[i].destinationkit = 2;
						grippers[i].nestindex = nest.index;
						grippers[i].full = true;
						currentnest = nest.index;
						print("Moving To Nest " + nest.index + " to pick up part for kit2");
						//Animation Call
						animationstate = AnimationStatus.movingToNest;
						server.execute("Get Part",nest.index-1,i);
						break;
					}
				}
			}			
			
			
		
		}
		if(kit1needsthispart || kit2needsthispart){
			nest.state = NestStatus.assigned;
		}
		else{
			print("Skipping part at nest " + nest.index + ". Will come back later");
			
			nest.state = NestStatus.skipped;
		}
		
	}
	private void getPart(){
		print("Picking up part");
		animationstate = AnimationStatus.waitingForPart;

		//gui.DoGetPart();//Any sort of animation for getting the part from the nest
		nests.get(currentnest-1).nest.msgGetPart();

		// Hack for v0 with no Nests yet
		//Part.PartType parttype = nests.get(currentnest-1).type;

		//this.msgHereIsPart(new Part(parttype));
		
		//End of Hack
		
		
	}
		
		

	private void goToStand(){
		//AnimationCall
		animationstate = AnimationStatus.movingToStand;

		boolean kit1 = false;
		for(int i = 0; i<4; i++){
			if(grippers[i].destinationkit == 1){
				kit1 = true;
			}
		}
		if(kit1)
		{
			server.execute("Load Kit 1");
		}
		else
		{
			server.execute("Load Kit 2");
		}
		print("Moving to Kit Stand");
		placeParts();
	}

	public void placeParts()
	{
		print("Giving parts to kitstand");	
		//AnimationCall
		List <Part> kit1parts = new ArrayList<Part>();
		for(int i = 0; i<4; i++)
		{
			if(grippers[i].destinationkit == 1){
				kit1parts.add(grippers[i].p);
				grippers[i].p = null;
				grippers[i].full = false;
				grippers[i].nestindex = -1;
			}
		}
			
		

		kitstand.msgHereAreParts(kit1parts,0);
		List<Part> kit2parts = new ArrayList<Part>();
		for(int i = 0; i<4; i++){
			if(grippers[i].destinationkit == 2){
				kit2parts.add(grippers[i].p);
				grippers[i].p = null;
				grippers[i].full = false;
				grippers[i].nestindex = -1;
			}
		} 
		kitstand.msgHereAreParts(kit2parts,0);
		
		for(MyNest mn : nests)
		{
			if(mn.state == NestStatus.skipped){
				mn.state = NestStatus.hasPart;
			}
		}
		returnToStart();
	}
	public void kitFinished(){

		if(kit1.partsneeded.isEmpty()){
			print("Kit 1 finished");
			kitstand.msgKitIsDone(1);
			for(Part.PartType type : recipe){
				kit1.partsneeded.add(type);
			}
			kit1.state = KitStatus.notAvailable;
			currentkit = CurrentKit.kit2;
		}
		if(kit2.partsneeded.isEmpty()){
			print("Kit 2 finished");
			kitstand.msgKitIsDone(2);
			for(Part.PartType type : recipe){
				kit2.partsneeded.add(type);
			}
			kit2.state = KitStatus.notAvailable;
			currentkit = CurrentKit.kit1;
		}
	}

	private void returnToStart()
	{	
		print("Returning to Home Position");
		//AnimationCall
		//gui.DoReturnToHome(); // Move to the original home position of the robot.
		animationstate = AnimationStatus.movingHome;
	}
	private boolean grippersEmpty()
	{
		for(int i = 0; i<4; i++)
		{
			if(grippers[i].full)
				return false;
		}
		return true;
	}
	private boolean allGrippersFull()
	{
		for(int i = 0; i<4; i++)
		{
			if(!grippers[i].full)
				return false;
		}
		return true;
	}
	public String getName()
	{
		return name;
	}
	public void setTestGUI(TestGUI test){
		gui = test;
	}
	public void setVisionAgents(List <VisionAgent> cams){
		for(VisionAgent cam : cams){
			cameras.add(cam);
		}
	}
}

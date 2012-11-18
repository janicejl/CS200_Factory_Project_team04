package Agents.PartsRobotAgent;
import Agent.*;
import Interface.VisionAgent.*;
import data.*;
import Interface.PartsRobotAgent.*;
import Interface.KitRobotAgent.*;
import data.*;
import Agents.VisionAgent.VisionAgent;

import java.util.*;

import server.Server;

public class PartsRobotAgent extends Agent implements PartsRobot {

	String name = "PartsRobotAgent";
	
	
	public List<Vision> cameras = new ArrayList<Vision>();
	public KitStand kitstand;
	public boolean camerahasrecipe = false;
	Server server = null;
	TestGUI gui;

	boolean executed = false;
	
	public int count = 0;
	public List <PartInfo> recipe = new ArrayList<PartInfo>();
	public List <Part> camerarecipe = new ArrayList<Part>();
	public List <MyNest> nests = new ArrayList<MyNest>();
	public Gripper[] grippers = new Gripper[4];
	public RobotState state;
	public CurrentKit currentkit = CurrentKit.kit1;
	int currentnest = -1;
	public AnimationStatus animationstate = AnimationStatus.atHome;

	public enum AnimationStatus {atHome, movingToNest, atNest, movingToStand, atStand, movingHome,waitingForPart,placingParts}//Will need to use when integrating with the animation

	public enum CurrentKit{kit1, kit2}
	private enum RobotState{mustOrderParts,PartsOrdered}

	//Constructor must instantiate all the MyNests and have references to all the nests
	public class MyNest
	{
		public PartInfo type;
		int index;
		public Nest nest;
		public NestStatus state = NestStatus.noAction;
		
		private MyNest(Nest nest,int ind){
			this.nest = nest;
			index = ind;
		}
		private MyNest (int ind){
			index = ind;
		}
		
	}
	public enum NestStatus{noAction, assigned, hasPart,skipped}
	public enum KitStatus{notAvailable, available, pending}


	public class MyKit
	{
		public int index;
		public List <PartInfo> partsneeded;
		public KitStatus state = KitStatus.notAvailable;
		public MyKit(int ind){
			index = ind;
			partsneeded = new ArrayList<PartInfo>();
		}
	}
	public MyKit kit1;
	public MyKit kit2;

	public class Gripper
	{
		public Part p;
		public int destinationkit;
		public boolean full = false;
		public int nestindex = -1;
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
		for(Nest nest:nestagents)
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
	
	public PartsRobotAgent(List <Nest> nestagents, List <Vision> cameralist, KitStand stand,Server server)
	{
		int index = 1;
		for(Nest nest:nestagents)
		{
			MyNest mn = new MyNest(nest,index);
			nests.add(mn);
			index++;
		}
		for(Vision cam:cameralist){
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
	
	
	public PartsRobotAgent(List <Nest> nestagents, List <Vision> cameralist, KitStand stand)
	{
		int index = 1;
		for(Nest nest:nestagents)
		{
			MyNest mn = new MyNest(nest,index);
			nests.add(mn);
			index++;
		}
		for(Vision cam:cameralist){
			cameras.add(cam);
		}
		kitstand = stand;
		kit1 = new MyKit(1);
		kit2 = new MyKit(2);
		for (int i = 0; i<4; i++)
		{
			grippers[i] = new Gripper();
		}
	}
	
	
	//Messages:

	public void msgMakeThisKit(KitInfo kit, int ct){ //will pass in KitInfo instead of List of parts
		count = ct;
		recipe.clear();
		for(PartInfo p:kit.getParts()){
			recipe.add(p);
		}
		camerahasrecipe = false;
		kit1.partsneeded.clear();
		kit2.partsneeded.clear();
		for(PartInfo type : recipe){
			kit1.partsneeded.add(type);
			kit2.partsneeded.add(type);
		}
		for(PartInfo type: recipe){
			camerarecipe.add(new Part(type)); //new Part(kitrecipe.get(i).getName(), kitrecipe.get(i).getImagePath())
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
		if(position == 0)
		{
			print("Kit 1 Available");
			kit1.state = KitStatus.available;
		}
		else
		{
			print("Kit 2 Available");
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
		
		
	}
	
	public void msgMovementDone(){
		if(animationstate == AnimationStatus.movingToStand){
			print("Reached at Stand");
			animationstate = AnimationStatus.atStand;
			stateChanged();
		}
		else if(animationstate == AnimationStatus.movingHome){
			animationstate = AnimationStatus.atHome;
		}
	}
	
	public void msgPartsDropped(){
		if(animationstate == AnimationStatus.placingParts){
			print("Finished at Stand");
			animationstate = AnimationStatus.movingHome;
			stateChanged();
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
	if((kit1.partsneeded.isEmpty() || kit2.partsneeded.isEmpty()) && grippersEmpty() && !recipe.isEmpty()&& animationstate != AnimationStatus.atStand && animationstate != AnimationStatus.movingToStand&& animationstate != AnimationStatus.placingParts)
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
	if(allGrippersFull() && (kit1.state == KitStatus.available || kit2.state == KitStatus.available)&& (animationstate == AnimationStatus.movingHome || animationstate == AnimationStatus.atHome)){
		goToStand();
		return true;
	}
	if(count!= 0 && (animationstate == AnimationStatus.movingHome || animationstate == AnimationStatus.atHome) && !allGrippersFull()){
		print("Checking parts");
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
		if(kit1.state == KitStatus.available || kit2.state == KitStatus.available)
		{
			for(int i = 0; i<4; i++)
			{
				if(grippers[i].full)
				{
					goToStand();
					return true;
				}
			}
		}
	}
	/*if(animationstate == AnimationStatus.atNest || animationstate == AnimationStatus.atStand)
	{
		returnToStart();
		return true;
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
			print("" + recipe.size());
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
		List<Nest> nestassignments = new ArrayList<Nest>();
		for(MyNest mn : nests)
		{
			nestassignments.add(mn.nest);
		}
		if(!cameras.isEmpty())
		{
			for(Vision camera : cameras)
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
		PartInfo type = nest.type;
		PartInfo parttoget = null;
		boolean kit1needsthispart = false;
		boolean kit2needsthispart = false;
		if(currentkit == CurrentKit.kit1){
			for(PartInfo pt: kit1.partsneeded){
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
						if(server!= null)
						server.execute("Get Part",nest.index-1,i);
						break;

					}
				}
			}
		
		}
		if(currentkit == CurrentKit.kit2)
		{
			for(PartInfo pt: kit2.partsneeded){
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
						if(server!= null)
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
		print("Picking up part at nest" + nests.get(currentnest-1).index);
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
		if(!executed){
			if(kit1)
			{
				if(server!= null)
				server.execute("Load Kit 1");
				executed = true;
			}
			else
			{
				if(server!= null)
				server.execute("Load Kit 2");
				executed = true;
			}
		}
		print("Moving to Kit Stand");
		
	}

	public void placeParts()
	{
		executed = false;
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
		animationstate = AnimationStatus.placingParts;
	}
	public void kitFinished(){

		if(kit1.partsneeded.isEmpty()){
			print("Kit 1 finished");
			kitstand.msgKitIsDone(0);
			for(PartInfo type : recipe){
				kit1.partsneeded.add(type);
			}
			kit1.state = KitStatus.notAvailable;
			currentkit = CurrentKit.kit2;
		}
		if(kit2.partsneeded.isEmpty()){
			print("Kit 2 finished");
			kitstand.msgKitIsDone(1);
			for(PartInfo type : recipe){
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
	public boolean grippersEmpty()
	{
		for(int i = 0; i<4; i++)
		{
			if(grippers[i].full)
				return false;
		}
		return true;
	}
	public boolean allGrippersFull()
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
	public void setMockVisionAgents(List <Vision> cams){
		for(Vision cam : cams){
			cameras.add(cam);
		}
	}
	
	public void setVisionAgents(List <VisionAgent> cams){
		for(VisionAgent cam : cams){
			cameras.add(cam);
		}
	}

	@Override
	public void msgMakeThisKit(List<PartInfo> kitrecipe, int ct) {
		// TODO Auto-generated method stub
		
	}
}

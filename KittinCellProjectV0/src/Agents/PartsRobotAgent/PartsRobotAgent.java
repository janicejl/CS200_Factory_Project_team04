package Agents.PartsRobotAgent;
import Agent.*;
import MoveableObjects.*;
import Agents.KitRobotAgents.*;
import java.util.*;



public class PartsRobotAgent extends Agent{

	String name = "PartsRobotAgent";

	VisionAgent camera;
	KitStandAgent kitstand;
	boolean camerahasrecipe = false;
	//PartsRobotGUI gui;

	int count = 0;
	List <Part.PartType> recipe = new ArrayList<Part.PartType>();
	List <MyNest> nests;
	Gripper[] grippers = new Gripper[4];
	RobotState state;
	CurrentKit currentkit;
	int currentnest;
	AnimationStatus animationstate = AnimationStatus.atHome;

	private enum AnimationStatus {atHome, movingToNest, atNest, movingToStand, atStand, movingHome}//Will need to use when integrating with the animation

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
		
	}
	private enum NestStatus{noAction, assigned, hasPart}
	private enum KitStatus{notAvailable, available, pending}


	public class MyKit
	{
		int index;
		List <Part.PartType> partsneeded;
		KitStatus state = KitStatus.notAvailable;
		public MyKit(int ind){
			index = ind;
		}
	}
	MyKit kit1;
	MyKit kit2;

	private class Gripper{
		Part p;
		int destinationkit;
		boolean full = false;
		int nestindex = -1;
		private Gripper(){};
	}
	public PartsRobotAgent(List <NestAgent> nestagents, VisionAgent visionagent, KitStandAgent stand)
	{
		int index = 0;
		for(NestAgent nestagent:nestagents)
		{
			nests.add(new MyNest(nestagent,index));
			index++;
		}
		camera = visionagent;
		kitstand = stand;
		kit1 = new MyKit(1);
		kit2 = new MyKit(2);
		for (int i = 0; i<4; i++)
		{
			grippers[i] = new Gripper();
		}
	}
	
	
	//Messages:

	public void msgMakeThisKit(List<Part.PartType> kitrecipe, int ct){
		count = ct;
		recipe = kitrecipe;
		camerahasrecipe = false;
		kit1.partsneeded = recipe;
		kit1.state = KitStatus.notAvailable;
		kit2.partsneeded = recipe;
		kit2.state = KitStatus.notAvailable;
		state = RobotState.mustOrderParts;
		stateChanged();
	}
	public void msgPartsApproved(int nestindex){
		nests.get(nestindex).state = NestStatus.hasPart;
		stateChanged();
	}

	public void msgEmptyKit(int position){
		if(position == 0)
			kit1.state = KitStatus.available;
		else
			kit2.state = KitStatus.available;
		
	}

	public void msgAnimationDone(){
		if(animationstate == AnimationStatus.movingToNest){
			animationstate = AnimationStatus.atNest;
		}
		if(animationstate == AnimationStatus.movingToStand){
			animationstate = AnimationStatus.atStand;
		}
		if(animationstate == AnimationStatus.movingHome){
			animationstate = AnimationStatus.atHome;
		}
	}
	public void msgHereIsPart(Part p)
	{
		for(int i = 0; i<4; i++)
		{
			if(grippers[i].nestindex == currentnest)
			{
				grippers[i].p = p;
			}
		}
		currentnest = -1;
	}

	//Scheduler:

	public boolean pickAndExecuteAnAction(){
		
	if(recipe != null && !camerahasrecipe)
	{
		giveCameraRecipe();
		return true;
	}
	if(recipe!= null && state == RobotState.mustOrderParts)
	{
		orderParts();
		return true;
	}
	
	if(count > 0 && (kit1.state == KitStatus.notAvailable || kit2.state == KitStatus.notAvailable)){
		requestEmptyKit();
		return true;
	}
	if((kit1.partsneeded.isEmpty() || kit2.partsneeded.isEmpty()) && grippersEmpty())
	{
		
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
	for(MyNest mn: nests)
	{
		if(mn.state == NestStatus.hasPart)
		{
			checkAvailableParts();
			return true;
		}
	}
	for(int i = 0; i<4; i++)
	{
		if(grippers[i].full)
		{
			goToStand();
			return true;
		}
	}
	if(animationstate != AnimationStatus.atHome)
	{
		returnToStart();
		return false;
	}
	}

	//Actions:

	private void orderParts()
	{
		print("Requesting Parts from Nests");
		for(int i = 0; i < recipe.size(); i++)
		{
			nests.get(i).nest.msgNeedThisPart(recipe.get(i));
			nests.get(i).state = NestStatus.assigned;
			nests.get(i).type = recipe.get(i);
		}
		state = RobotState.PartsOrdered;
	}

	private void giveCameraRecipe()
	{
			
		print("Giving Recipe to VisionAgent");
		List<Part.PartType> nestassignments;
		for(MyNest mn : nests)
		{
			nestassignments.add(mn.type);
		}
		camera.msgHereIsSchematic(recipe, nestassignments);
		camerahasrecipe = true;
	}
	private void requestEmptyKit()
	{
		kitstand.msgIsThereEmptyKit();
		if(kit1.state == KitStatus.notAvailable)
			kit1.state = KitStatus.pending;
		else
			kit2.state = KitStatus.pending;
	}	
		
	private void checkAvailableParts(){
		for(MyNest nest : nests)
		{
			if(currentkit == CurrentKit.kit1)
			{
				for(Part.PartType p: kit1.partsneeded)
				{
					if(p == nest.type)
					{
						//Animation Call
						for(int i = 0; i<4; i++)
						{
							if(!grippers[i].full)
							{
								grippers[i].full = true;
								grippers[i].destinationkit = 1;
								grippers[i].nestindex = nest.index;
								break;
							}
						}
						print("Moving to Nest " + nest.index + " to pick up part");
						//gui.DoGoToNest(nest.index); Move to the indicated nest
						animationstate = AnimationStatus.movingToNest;
						currentnest = nest.index;
						kit1.partsneeded.remove(p);
					}
				}
			}
			else 
			{
				for(Part.PartType p: kit2.partsneeded)
				{
					if(p == nest.type)
					{
						for(int i = 0; i<4; i++){
							if(!grippers[i].full){
								grippers[i].full = true;
								grippers[i].destinationkit = 2;
								grippers[i].nestindex = nest.index;
								break;
							}
						}
						print("Moving to Nest " + nest.index + " to pick up part");
						//Animation Call
						//gui.DoGoToNest(nest.index); // Move to the indicated nest
						animationstate = AnimationStatus.movingToNest;
						currentnest = nest.index;
						kit2.partsneeded.remove(p);
					}
				}
			}
		}
	}
	private void getPart(){
		print("Picking up part");
		//gui.DoGetPart();//Any sort of animation for getting the part from the nest
		nests.get(currentnest).nest.msgGetPart();
	}
		
		

	private void goToStand(){
		//AnimationCall
		//gui.DoGoToStand(); //Move to Kit Stand
		print("Moving to Kit Stand");
		animationstate = AnimationStatus.movingToStand;
	}

	public void placeParts()
	{
		print("Giving parts to kitstand");	
		//AnimationCall
		//gui.DoPlaceParts(); //Any sort of part placing animation
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
		returnToStart();
	}
	public void kitFinished(){

		if(kit1.partsneeded.isEmpty()){
			print("Kit 1 finished");
			kitstand.msgKitIsDone(0);
			kit1.partsneeded = recipe;
			kit1.state = KitStatus.notAvailable;
		}
		if(kit2.partsneeded.isEmpty()){
			print("Kit 2 finished");
			kitstand.msgKitIsDone(1);
			kit2.partsneeded = recipe;
			kit2.state = KitStatus.notAvailable;
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
}

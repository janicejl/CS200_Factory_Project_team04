package Agents.PartsRobotAgent;

import Agent.*;

public class TestGUI extends Agent{

	PartsRobotAgent partsrobot;
	boolean movingToNest = false;
	boolean movingToStand = false;
	
	public TestGUI(PartsRobotAgent robot){
		partsrobot = robot;
	}
	
	public void DoMoveToNest(int index){
		movingToNest = true;
		stateChanged();
	}

	public void DoGoToStand(){
		movingToStand = true;
		stateChanged();
	}
	public boolean pickAndExecuteAnAction(){
		if(movingToNest){
			partsrobot.msgAnimationDone();
			movingToNest = false;
			return true;
		}
		if(movingToStand){
			partsrobot.msgAnimationDone();
			movingToStand = false;
			return true;
		}
		return false;
		
	}
	
}

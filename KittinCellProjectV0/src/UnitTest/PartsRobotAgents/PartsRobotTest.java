package UnitTest.PartsRobotAgents;

import static org.junit.Assert.*;
import server.Server;

import org.junit.Test;

import java.util.*;
import Agents.PartsRobotAgent.*;
import Agents.KitRobotAgents.*;
import MoveableObjects.*;

public class PartsRobotTest {
	
	
	
	public static void fourPartTestRecipe()
	{
		PartsRobotAgent partsrobotagent;
		MockKitStandV0 kitstand;
		NestAgent nest1;
		NestAgent nest2;
		NestAgent nest3;
		NestAgent nest4;
		NestAgent nest5;
		NestAgent nest6;
		NestAgent nest7;
		NestAgent nest8;
		MockLaneAgentV0 lane1;
		MockLaneAgentV0 lane2;
		MockLaneAgentV0 lane3;
		MockLaneAgentV0 lane4;
		MockLaneAgentV0 lane5;
		MockLaneAgentV0 lane6;
		MockLaneAgentV0 lane7;
		MockLaneAgentV0 lane8;
		MockVisionAgentV0 camera = new MockVisionAgentV0();
		kitstand = new MockKitStandV0();
		lane1 = new MockLaneAgentV0(1);
		nest1 = new NestAgent(lane1,camera,1);
		lane1.setNest(nest1);
		lane2 = new MockLaneAgentV0(2);
		nest2 = new NestAgent(lane2,camera,2);
		lane2.setNest(nest2);
		lane3 = new MockLaneAgentV0(3);
		nest3 = new NestAgent(lane3,camera,3);
		lane3.setNest(nest3);
		lane4 = new MockLaneAgentV0(4);
		nest4 = new NestAgent(lane4,camera,4);
		lane4.setNest(nest4);
		lane5 = new MockLaneAgentV0(5);
		nest5 = new NestAgent(lane5,camera,5);
		lane5.setNest(nest5);
		lane6 = new MockLaneAgentV0(6);
		nest6 = new NestAgent(lane6,camera,6);
		lane6.setNest(nest6);
		lane7 = new MockLaneAgentV0(7);
		nest7 = new NestAgent(lane7,camera,7);
		lane7.setNest(nest7);
		lane8 = new MockLaneAgentV0(8);
		nest8 = new NestAgent(lane8,camera,8);
		lane8.setNest(nest8);
		List<NestAgent> nests = new ArrayList<NestAgent>();
		nests.add(nest1);
		nests.add(nest2);
		nests.add(nest3);
		nests.add(nest4);
		nests.add(nest5);
		nests.add(nest6);
		nests.add(nest7);
		nests.add(nest8);
		partsrobotagent = new PartsRobotAgent(nests,camera,kitstand);
		TestGUI gui = new TestGUI(partsrobotagent);
		partsrobotagent.setTestGUI(gui);
		nest1.setPartsRobotAgent(partsrobotagent);
		nest2.setPartsRobotAgent(partsrobotagent);
		nest3.setPartsRobotAgent(partsrobotagent);
		nest4.setPartsRobotAgent(partsrobotagent);
		nest5.setPartsRobotAgent(partsrobotagent);
		nest6.setPartsRobotAgent(partsrobotagent);
		nest7.setPartsRobotAgent(partsrobotagent);
		nest8.setPartsRobotAgent(partsrobotagent);
		kitstand.setPartsRobot(partsrobotagent);
		camera.setPartsRobot(partsrobotagent);
		partsrobotagent.startThread();
		kitstand.startThread();
		camera.startThread();
		lane1.startThread();
		lane2.startThread();
		lane3.startThread();
		lane4.startThread();
		lane5.startThread();
		lane6.startThread();
		lane7.startThread();
		lane8.startThread();
		nest1.startThread();
		nest2.startThread();
		nest3.startThread();
		nest4.startThread();
		nest5.startThread();
		nest6.startThread();
		nest7.startThread();
		nest8.startThread();
		gui.startThread();
		List<Part.PartType> recipe = new ArrayList<Part.PartType>();
		Part.PartType part1 = Part.PartType.part1;
		Part.PartType part2 = Part.PartType.part2;
		Part.PartType part3 = Part.PartType.part3;
		Part.PartType part4 = Part.PartType.part4;
		Part.PartType part5 = Part.PartType.part5;
		//Part.PartType part6 = Part.PartType.part6;
		//Part.PartType part7 = Part.PartType.part7;
		//Part.PartType part8 = Part.PartType.part8;
		recipe.add(part1);
		recipe.add(part2);
		recipe.add(part3);
		recipe.add(part4);
		recipe.add(part5);
		//recipe.add(part6);
		//recipe.add(part7);
		//recipe.add(part8);
		partsrobotagent.msgMakeThisKit(recipe,2);
		
		
	}
	

	public static void main(String [] args)
	{
		fourPartTestRecipe();
	}
		

}

package UnitTest.PartsRobotAgents;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;
import Agents.PartsRobotAgent.*;
import Agents.KitRobotAgents.*;
import MoveableObjects.*;

public class PartsRobotTest {
	
	
	
	public static void fourPartTestRecipe()
	{
		PartsRobotAgent partsrobotagent;
		KitStandAgent kitstand;
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
		VisionAgent camera = new VisionAgent();
		kitstand = new KitStandAgent();
		lane1 = new MockLaneAgentV0();
		nest1 = new NestAgent(lane1,camera,1);
		lane1.setNest(nest1);
		lane2 = new MockLaneAgentV0();
		nest2 = new NestAgent(lane2,camera,2);
		lane2.setNest(nest2);
		lane3 = new MockLaneAgentV0();
		nest3 = new NestAgent(lane1,camera,3);
		lane3.setNest(nest3);
		lane4 = new MockLaneAgentV0();
		nest4 = new NestAgent(lane1,camera,4);
		lane4.setNest(nest4);
		lane5 = new MockLaneAgentV0();
		nest5 = new NestAgent(lane1,camera,5);
		lane5.setNest(nest5);
		lane6 = new MockLaneAgentV0();
		nest6 = new NestAgent(lane1,camera,6);
		lane6.setNest(nest6);
		lane7 = new MockLaneAgentV0();
		nest7 = new NestAgent(lane1,camera,7);
		lane7.setNest(nest7);
		lane8 = new MockLaneAgentV0();
		nest8 = new NestAgent(lane1,camera,8);
		lane8.setNest(nest8);
		List<NestAgent> nests = new ArrayList<NestAgent>();
		nests.add(nest1);
		nests.add(nest2);
		nests.add(nest3);
		nests.add(nest4);
		nests.add(nest5);
		nests.add(nest6);
		nests.add(nest7);
		partsrobotagent = new PartsRobotAgent(nests,camera,kitstand);
		nest1.setPartsRobotAgent(partsrobotagent);
		nest2.setPartsRobotAgent(partsrobotagent);
		nest3.setPartsRobotAgent(partsrobotagent);
		nest4.setPartsRobotAgent(partsrobotagent);
		partsrobotagent.startThread();
		kitstand.startThread();
		camera.startThread();
		lane1.startThread();
		lane2.startThread();
		lane3.startThread();
		lane4.startThread();
		nest1.startThread();
		nest2.startThread();
		nest3.startThread();
		nest4.startThread();
		List<Part.PartType> recipe = new ArrayList<Part.PartType>();
		Part.PartType part1 = Part.PartType.part1;
		Part.PartType part2 = Part.PartType.part2;
		Part.PartType part3 = Part.PartType.part3;
		Part.PartType part4 = Part.PartType.part4;
		recipe.add(part1);
		recipe.add(part2);
		recipe.add(part3);
		recipe.add(part4);
		assertTrue(!recipe.isEmpty());
		partsrobotagent.msgMakeThisKit(recipe,4);
		
	}
	

	public static void main(String [] args)
	{
		fourPartTestRecipe();
	}
		

}

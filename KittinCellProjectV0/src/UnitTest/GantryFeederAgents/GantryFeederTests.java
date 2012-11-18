package UnitTest.GantryFeederAgents;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({FeederTests.class})
public class GantryFeederTests {
	
	public static Test suite() {
		return new TestSuite(FeederTests.class);
	}

}

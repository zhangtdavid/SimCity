package city.tests;

import city.tests.animations.AptAnimationTest;
import city.tests.animations.HouseAnimationTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AnimationSuite {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(AnimationSuite.class.getName());
		suite.addTestSuite(AptAnimationTest.class);
		suite.addTestSuite(HouseAnimationTest.class);
		return suite;
	}
}

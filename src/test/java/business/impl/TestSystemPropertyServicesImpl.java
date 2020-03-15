/**
 * 
 */
package business.impl;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import business.SystemPropertyService;


/**
 * @author ningyuan
 *
 */
public class TestSystemPropertyServicesImpl {
	
	private static SystemPropertyService sps;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sps = new SystemPropertyServiceImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link business.impl.SystemPropertyServiceImpl#getSystemProperties()}.
	 */
	@Test
	public void testGetSystemProperties() {
		String julConfFile = sps.getSystemProperties().getProperty("java.util.logging.config.file");
		
		Assert.assertNull(julConfFile);
	}

}

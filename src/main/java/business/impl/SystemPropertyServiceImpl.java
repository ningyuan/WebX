/**
 * 
 */
package business.impl;

import java.util.Properties;

import business.SystemPropertyService;

/**
 * @author ningyuan
 *
 */
public class SystemPropertyServiceImpl implements SystemPropertyService {

	@Override
	public Properties getSystemProperties() {
		return System.getProperties();
	}

}

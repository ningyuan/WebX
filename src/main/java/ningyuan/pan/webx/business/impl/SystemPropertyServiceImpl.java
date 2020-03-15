/**
 * 
 */
package ningyuan.pan.webx.business.impl;

import java.util.Properties;

import ningyuan.pan.webx.business.SystemPropertyService;

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

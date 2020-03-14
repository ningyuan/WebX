/**
 * 
 */
package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 * @author ningyuan
 *
 */
public class XServletContextListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(XServletContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.trace("contextInitialized()");
		LOGGER.debug("contextInitialized()");
		LOGGER.info("contextInitialized()");
		LOGGER.warn("contextInitialized()");
		LOGGER.error("contextInitialized()");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.debug("contextDestroyed()");
	}
	
}

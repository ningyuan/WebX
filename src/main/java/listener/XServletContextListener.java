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
		LOGGER.trace(getClass().getName()+".contextInitialized()");
		LOGGER.debug(getClass().getName()+".contextInitialized()");
		LOGGER.info(getClass().getName()+".contextInitialized()");
		LOGGER.warn(getClass().getName()+".contextInitialized()");
		LOGGER.error(getClass().getName()+".contextInitialized()");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info(getClass().getName()+".contextDestroyed()");
	}
	
}

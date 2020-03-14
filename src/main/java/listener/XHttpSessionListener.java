/**
 * 
 */
package listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ningyuan
 *
 */
public class XHttpSessionListener implements HttpSessionListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(XHttpSessionListener.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		LOGGER.debug(getClass().getName()+".sessionCreated()");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		LOGGER.debug(getClass().getName()+".sessionDestroyed()");
	}

}

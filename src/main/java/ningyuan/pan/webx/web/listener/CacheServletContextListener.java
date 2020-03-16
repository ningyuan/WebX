/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.redis.RedisCache;
import ningyuan.pan.webx.util.cache.redis.RedisCache1;

/**
 * @author ningyuan
 *
 */
public class CacheServletContextListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheServletContextListener.class);
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.debug("contextInitialized()");
		
		String cacheName = sce.getServletContext().getInitParameter("cacheName");
		
		Cache cache = new RedisCache1();
		
		cache.open();
		
		sce.getServletContext().setAttribute(cacheName, cache);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.debug("contextDestroyed()");
		
		Cache cache = (Cache)sce.getServletContext().getAttribute(sce.getServletContext().getInitParameter("cacheName"));
		
		cache.close();
	}

}

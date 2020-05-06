/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.CacheName;
import ningyuan.pan.webx.util.cache.redis.JedisCache;
import ningyuan.pan.webx.util.cache.redis.LettuceCache;

/**
 * @author ningyuan
 *
 */
public class CacheServletContextListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheServletContextListener.class);
	
	private String cacheName;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.debug("contextInitialized()");
		
		cacheName = sce.getServletContext().getInitParameter("cache.name");
		
		if(CacheName.REDIS.getName().equals(cacheName)) {
			Cache cache = null;
			
			try {
				
				cache = new JedisCache(sce.getServletContext().getInitParameter("redis.properties.file"));
	    	
				//cache = new LettuceCache(sce.getServletContext().getInitParameter("redis.properties.file"));
			
				cache.open();
	    		
				sce.getServletContext().setAttribute(CacheName.REDIS.getName(), cache);
			}
			catch (Exception e) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
				
				if(cache != null) {
					cache.close();
				}
				
				sce.getServletContext().removeAttribute(CacheName.REDIS.getName());
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.debug("contextDestroyed()");
		
		Cache cache = (Cache)sce.getServletContext().getAttribute(cacheName);
		
		if(cache != null) {
			cache.close();
		}
		
		sce.getServletContext().removeAttribute(cacheName);
	}
}

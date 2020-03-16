/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.redis.JedisCache;

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
		String configPropertyFile = sce.getServletContext().getInitParameter("configPropertyFile");
		
		Properties configProp;
		try {
			configProp = new Properties();
        	configProp.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(configPropertyFile)));
		} catch (IOException e) {
            e.printStackTrace();
            return;
        }
		
		if(cacheName.equals("RedisCache")) {
			Cache cache = new JedisCache();
	    	
			cache.open(configProp.getProperty("redis.host"), configProp.getProperty("redis.port"));
	    		
			sce.getServletContext().setAttribute(cacheName, cache);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.debug("contextDestroyed()");
		
		Cache cache = (Cache)sce.getServletContext().getAttribute(sce.getServletContext().getInitParameter("cacheName"));
		
		if(cache != null) {
			cache.close();
		}
	}
}

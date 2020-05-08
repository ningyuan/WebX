/**
 * 
 */
package ningyuan.pan.webx.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;

/**
 * @author ningyuan
 *
 */
public class StaticResourceCacheFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StaticResourceCacheFilter.class);
	
	private Cache cache = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.debug("init()");
		
		String cacheName = filterConfig.getServletContext().getInitParameter("cache.name");
		cache = (Cache)filterConfig.getServletContext().getAttribute(cacheName);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.debug("doFilter()");
		
		chain.doFilter(request, response);
		
	}
	
}

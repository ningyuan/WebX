/**
 * 
 */
package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ningyuan
 *
 */
public class Filter1 implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Filter1.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.debug("init()");
		LOGGER.debug("filterName: "+filterConfig.getInitParameter("filterName"));
	}

	@Override
	public void destroy() {
		LOGGER.debug("destory()");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		LOGGER.debug("doFilter()");
		
		chain.doFilter(request, response);
		
		LOGGER.debug("doFilter()");
	}

}

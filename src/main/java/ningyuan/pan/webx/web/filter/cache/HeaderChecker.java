/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ningyuan
 *
 */
class HeaderChecker implements CacheableChecker {
	
	private final CacheableChecker checker;
	
	public HeaderChecker(CacheableChecker checker) {
		this.checker = checker;
	}
	
	@Override
	public boolean check(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
	        String header = headers.nextElement();
	        
	        // the browser may use its cache
	        if("if-modified-since".equalsIgnoreCase(header)) {
	        	return false;
	        }
	        // the browser may use its cache
	        if("if-none-match".equalsIgnoreCase(header)) {
	        	return false;
	        }
	        if("cache-control".equalsIgnoreCase(header)) {
	        	Enumeration<String> values = request.getHeaders(header);
	        	
	        	while(values.hasMoreElements()) {
	        		String value = values.nextElement();
	        		
	        		// do not use a cache
	        		if("no-cache".equalsIgnoreCase(value)) {
	        			return false;
	        		}
	        	}
	        }
	    }
		
		return checker.check(request);
	}

}

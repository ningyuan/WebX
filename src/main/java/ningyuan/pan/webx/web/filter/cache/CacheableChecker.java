/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ningyuan
 *
 */
interface CacheableChecker {
	
	boolean check(HttpServletRequest request);
	
}

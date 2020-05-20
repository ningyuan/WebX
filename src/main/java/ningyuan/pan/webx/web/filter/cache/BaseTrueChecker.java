/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ningyuan
 *
 */
class BaseTrueChecker implements CacheableChecker {

	@Override
	public boolean check(HttpServletRequest request) {
		return true;
	}

}

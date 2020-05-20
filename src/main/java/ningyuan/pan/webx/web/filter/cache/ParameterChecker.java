/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ningyuan
 *
 */
class ParameterChecker implements CacheableChecker {
	
	private final CacheableChecker checker;
	
	ParameterChecker(CacheableChecker checker) {
		this.checker = checker;
	}
	
	@Override
	public boolean check(HttpServletRequest request) {
		if(request.getParameterNames().hasMoreElements()) {
			return false;
		}
		else {
			return checker.check(request);
		}
	}

}

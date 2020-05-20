/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ningyuan.pan.util.net.MediaType;
import ningyuan.pan.webx.util.cache.Cache;

/**
 * @author ningyuan
 *
 */
interface ResourceProcessor {

	public void process(Cache cache, HttpServletRequest request, HttpServletResponse response, FilterChain chain, 
			File resource, MediaType mediaType) throws IOException, ServletException;
}

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.util.net.MediaType;
import ningyuan.pan.webx.util.cache.Cache;

/**
 * @author ningyuan
 *
 */
class TextStaticResourceProcessor implements ResourceProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TextStaticResourceProcessor.class);
	
	@Override
	public void process(Cache cache, HttpServletRequest request, HttpServletResponse response, FilterChain chain, 
			File resource, MediaType mediaType) throws IOException, ServletException {
		
		String uri = request.getRequestURI();
		
		String text = null;
		
		try {
			text = cache.getText(uri);
		}
		catch (Exception e) {
			LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			
			// the cache dose not work properly
			chain.doFilter(request, response);
			return;
		}
		
		if(text == null) {
			ContentHttpServletResponseWrapper wrapper = new ContentHttpServletResponseWrapper(response);
		
			chain.doFilter(request, wrapper);
			
			text = wrapper.getContentInString();
			
			// update the cache
			if(text != null && !text.isEmpty()) {
				try {
					cache.putText(uri, text);
				}
				catch (Exception e) {
					LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
				}
			}
		}
		else {
			// cache hit and ask the browser to use its cache if possible
			response.addHeader("Cache-Control", "max-age=60");
			response.addDateHeader("Last-Modified", resource.lastModified());
		}
		
		response.setCharacterEncoding("UTF-8");
        response.setContentType(mediaType.getType()+";charset=utf-8");
        response.getWriter().write(text);

	}
}

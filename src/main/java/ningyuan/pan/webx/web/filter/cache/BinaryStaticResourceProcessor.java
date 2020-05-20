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
class BinaryStaticResourceProcessor implements ResourceProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryStaticResourceProcessor.class);
	
	@Override
	public void process(Cache cache, HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			File resource, MediaType mediaType) throws IOException, ServletException {
		
		String uri = request.getRequestURI();
		
		byte[] data = null;
		
		try {
			data = cache.getBinary(uri);
		}
		catch (Exception e) {
			LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			
			chain.doFilter(request, response);
			return;
		}
		
		if(data == null) {
			ContentHttpServletResponseWrapper wrapper = new ContentHttpServletResponseWrapper(response);
		
			chain.doFilter(request, wrapper);
			
			data = wrapper.getContentInByte();
				
			if(data != null && data.length > 0) {
				try {
					cache.putBinary(uri, data);
				}
				catch (Exception e) {
					LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
				}
			}
		}
		else {
			response.addHeader("Cache-Control", "max-age=60");
			response.addDateHeader("Last-Modified", resource.lastModified());
		}
		
		response.setContentType(mediaType.getType());
		response.getOutputStream().write(data);

	}
}

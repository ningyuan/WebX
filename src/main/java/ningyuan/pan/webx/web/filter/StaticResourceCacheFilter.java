/**
 * 
 */
package ningyuan.pan.webx.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
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
public class StaticResourceCacheFilter extends HttpFilter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(StaticResourceCacheFilter.class);
	
	private static final String TYPE_NONE_STATIC = "ns";
	
	private static final String TYPE_TEXT = "text";
	
	private static final String TYPE_BINARY = "binary";
	
	private static final MediaType[] TEXT_MEDIA_TYPES = {MediaType.HTML, MediaType.CSS, MediaType.JAVASCRIPT};
	
	private static final MediaType[] BINARY_MEDIA_TYPES = {MediaType.BMP, MediaType.JPEG};
	
	private Cache cache = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.debug("init()");
		
		String cacheName = filterConfig.getServletContext().getInitParameter("cache.name");
		cache = (Cache)filterConfig.getServletContext().getAttribute(cacheName);
	}
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.debug("doFilter()");
		
		String uri = request.getRequestURI();
		
		System.out.println(uri);
		Enumeration<String> headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        System.out.println(key+" "+value);
	    }
	    
		/*
		 * close browser cache
		 * firefox: browser.cache.memory.enable
		 * 			browser.cache.disk.enable
		 */
		if(cache != null) {
			MediaType mediaType = getResourceMeidaType(uri);
			String type = getResourceType(mediaType);
			
			if(TYPE_TEXT.equals(type)) {
				String text = null;
				
				try {
					text = cache.getText(uri);
				}
				catch (Exception e) {
					LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
					
					chain.doFilter(request, response);
					return;
				}
				
				if(text == null) {
					ContentHttpServletResponseWrapper wrapper = new ContentHttpServletResponseWrapper(response);
				
					chain.doFilter(request, wrapper);
					
					text = wrapper.getContentInString();
					
					try {
						cache.putText(uri, text);
					}
					catch (Exception e) {
						LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
					}
				}
				
				response.setCharacterEncoding("UTF-8");
		        response.setContentType(mediaType.getType()+";charset=utf-8");
		        response.getWriter().write(text);
			}
			else if(TYPE_BINARY.equals(type)) {
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
						
					try {
						cache.putBinary(uri, data);
					}
					catch (Exception e) {
						LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
					}
				}
				
				response.getOutputStream().write(data);
			}
			else {
				chain.doFilter(request, response);
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	private MediaType getResourceMeidaType(String uri) {
		String [] res = uri.split("/+");
		
		String fileName = res[res.length-1];
		
		if(fileName.indexOf(".") != -1) {
			res = fileName.split("\\.");
			
			String suffix = res[res.length-1];
			
			MediaType[] types = MediaType.values();
			
			for(MediaType type : types) {
				String[] suffixes = type.getSuffixes();
				
				for(String s : suffixes) {
					if(suffix.equalsIgnoreCase(s)) {
						return type;
					}
				}
			}
		}
		
		return MediaType.UNKNOWN;
	}
	
	private String getResourceType(MediaType mediaType) {
		for(MediaType tType : TEXT_MEDIA_TYPES) {
			if(mediaType == tType) {
				return TYPE_TEXT;
			}
		}
		
		for(MediaType bType : BINARY_MEDIA_TYPES) {
			if(mediaType == bType) {
				return TYPE_BINARY;
			}
		}
		
		return TYPE_NONE_STATIC;
	}
}

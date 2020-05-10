/**
 * 
 */
package ningyuan.pan.webx.web.filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Properties;

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
	
	/*
	 * The mapping between http request uri and static file location.
	 * Keep this mapping updated when you change the request uri or
	 * the static resource file. (the mapping file is conf/requestURI.staticFile.mapping)
	 */
	private Properties URIResourceMapping = new Properties();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.debug("init()");
		
		String cacheName = filterConfig.getServletContext().getInitParameter("cache.name");
		cache = (Cache)filterConfig.getServletContext().getAttribute(cacheName);
		
		try {
			URIResourceMapping.load(new InputStreamReader(StaticResourceCacheFilter.class.getClassLoader().getResourceAsStream("conf/cache.requestURI.staticResource.mapping")));
		} 
		catch (IOException ioe) {
			LOGGER.debug(ExceptionUtils.printStackTraceToString(ioe));
		}
	}
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.debug("doFilter()");
		
		String uri = request.getRequestURI();
		
		String resourceName = URIResourceMapping.getProperty(uri);
		
		File resource = null;
		
		if(resourceName != null) {
			try {
				resource = new File(StaticResourceCacheFilter.class.getClassLoader().getResource(resourceName).toURI());
			} 
			catch (URISyntaxException use) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(use));
			}
		}
	
		
		System.out.println(uri);
		
		Enumeration<String> headers = request.getHeaderNames();
	    while (headers.hasMoreElements()) {
	        String key = headers.nextElement();
	        String value = request.getHeader(key);
	        System.out.println(key+" "+value);
	    }
	    
		/*
		 * close browser cache
		 * firefox: browser.cache.memory.enable
		 * 			browser.cache.disk.enable
		 */
		if(cache != null && resource != null) {
			if(isCacheable(request)) {
				process(request, response, chain, resource);
			}
			else {
				chain.doFilter(request, response);
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	private boolean isCacheable(HttpServletRequest request) {
		// requests with parameters are not cached
		if(request.getParameterNames().hasMoreElements()) {
			return false;
		}
		
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
	        		if("no-store".equalsIgnoreCase(value)) {
	        			return false;
	        		}
	        	}
	        }
	    }
		
		return true;
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response, FilterChain chain, File resource) throws IOException, ServletException {
		String uri = request.getRequestURI();
		
		// the media type of the requested resource
		MediaType mediaType = getResourceMeidaType(uri);
		// the type of the static resource
		String type = getStaticResourceType(mediaType);
		
		if(TYPE_TEXT.equals(type)) {
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
	
	private String getStaticResourceType(MediaType mediaType) {
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

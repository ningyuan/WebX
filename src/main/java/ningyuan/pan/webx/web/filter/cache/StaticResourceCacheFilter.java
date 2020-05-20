/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

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
	
	private Cache cache = null;
	
	private CacheableChecker cacheableChecker = new ParameterChecker(new HeaderChecker(new BaseTrueChecker()));
	
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
		if(cache != null) {
			if(cacheableChecker.check(request)) {
				File resource = getResourceFile(uri);
				
				if(resource != null) {
					MediaType mediaType = getResourceMeidaType(uri);
					ResourceProcessor resourceProcessor = ResourceProcessorFactory.getInstence(mediaType);
					resourceProcessor.process(cache, request, response, chain, resource, mediaType);
				}
				else {
					chain.doFilter(request, response);
				}
			}
			else {
				chain.doFilter(request, response);
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	private File getResourceFile(String uri) {
		File resource = null;
		
		String resourceName = URIResourceMapping.getProperty(uri);
		
		if(resourceName != null) {
			try {
				resource = new File(StaticResourceCacheFilter.class.getClassLoader().getResource(resourceName).toURI());
			} 
			catch (URISyntaxException use) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(use));
			}
		}
		
		return resource;
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
}

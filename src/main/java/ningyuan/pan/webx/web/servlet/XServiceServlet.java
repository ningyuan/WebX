/**
 * 
 */
package ningyuan.pan.webx.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.servicex.XService;

/**
 * @author ningyuan
 *
 */
//@WebServlet("/xservice")
public class XServiceServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XServiceServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOGGER.debug("doGet()");
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/html;charset=utf-8");
        
		String cacheName = getServletContext().getInitParameter("cache.name");
		Cache cache = (Cache)getServletContext().getAttribute(cacheName);
		
		XService service = (XService)getServletContext().getAttribute("ServiceX");
		
		PrintWriter out = response.getWriter();
	    
		if(cache != null) {
			// check cache first
			String name = cache.get("name");
			if(name == null) {
				
				name = service.getName();
				
				cache.put("name", name);
				
				out.write("Call service and set cache: "+name);
				
			}
			else {
				 out.write("Hit cache: "+name);
			}
		}
		else {
			out.write("No cache: "+service.getName());
		}
        
        out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doPost()");
		
		doGet(request, response);
	}

}

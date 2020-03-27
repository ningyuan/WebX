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
import ningyuan.pan.servicex.ServiceX;

/**
 * @author ningyuan
 *
 */
//@WebServlet("/servicex")
public class ServiceXServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceXServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOGGER.debug("doGet()");
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/html;charset=utf-8");
        
		String cacheName = getServletContext().getInitParameter("cache.name");
		Cache cache = (Cache)getServletContext().getAttribute(cacheName);
	    
		PrintWriter out = response.getWriter();
	       
		// check cache first
		String name = cache.get("name");
		if(name == null) {
			ServiceX service = (ServiceX)getServletContext().getAttribute("ServiceX");
			name = service.getName();
			
			cache.put("name", name);
			
			out.write("Call service and set cache: "+name);
			
		}
		else {
			 out.write("Hit cache: "+name);
		}
        
        out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doPost()");
		
		doGet(request, response);
	}

}

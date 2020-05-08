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

import ningyuan.pan.webx.util.ServiceName;
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
        
		XService service = (XService)getServletContext().getAttribute(ServiceName.X_SERVICE.getName());
		
		PrintWriter out = response.getWriter();
	    
		if(service != null) {
			String name = service.getName();
					
			out.write(name);
		}
		else {
			out.write("No service");
		}
        
        out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doPost()");
		
		doGet(request, response);
	}

}

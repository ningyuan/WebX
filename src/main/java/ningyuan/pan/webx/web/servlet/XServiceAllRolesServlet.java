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

import ningyuan.pan.servicex.XService;
import ningyuan.pan.util.text.TextType;
import ningyuan.pan.webx.util.ServiceName;

/**
 * @author ningyuan
 *
 */
public class XServiceAllRolesServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(XServiceAllRolesServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("application/json;charset=utf-8");
		
		XService service = (XService)getServletContext().getAttribute(ServiceName.X_SERVICE.getName());
		
		PrintWriter out = response.getWriter();
	    
		// empty roles as an array
		String ret = "[]";
		
		if(service != null) {
			ret = service.getAllRoles(TextType.JSON.getName());
					
			out.write(ret);		
				
		}
		else {
			out.write(ret);
		}
        
        out.close();
	}

	@Override
	protected void doPost(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
		doGet(reqest, response);
	}
}

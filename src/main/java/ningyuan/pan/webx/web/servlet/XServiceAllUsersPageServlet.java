/**
 * 
 */
package ningyuan.pan.webx.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ningyuan
 *
 */
public class XServiceAllUsersPageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XServiceAllUsersPageServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/html;charset=utf-8");
		
		// path starting with '/' is interpreted as relative to current context root
		request.getRequestDispatcher("/pages/allUsers.html").forward(request, response);
	}

}

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
//@WebServlet("/")
public class StartPageServlet extends HttpServlet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StartPageServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		LOGGER.debug(request.getContextPath());
		LOGGER.debug(request.getPathInfo());
		LOGGER.debug(request.getPathTranslated());
		LOGGER.debug(request.getRequestURL().toString());
		LOGGER.debug(request.getRequestURI());
		
	
		// path starting with '/' is interpreted as relative to current context root
		request.getRequestDispatcher("/pages/start.html").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		doGet(request, response);
	}

}

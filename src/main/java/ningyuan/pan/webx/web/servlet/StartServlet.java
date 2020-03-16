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
public class StartServlet extends HttpServlet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StartServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		String cacheName = getServletContext().getInitParameter("cacheName");
		
		getServletContext().setAttribute(cacheName, new Object());
		
		request.getRequestDispatcher("./pages/start.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		doGet(request, response);
	}

}

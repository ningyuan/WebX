/**
 * 
 */
package ningyuan.pan.webx.web.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.util.exception.ExceptionUtils;


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
	
	/*
	 * @Resource(lookup = "java:/comp/env", name = "jdbc/XAdatasource")
	 * private DataSource dataSource;
	 */
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		LOGGER.debug(request.getContextPath());
		LOGGER.debug(request.getPathInfo());
		LOGGER.debug(request.getPathTranslated());
		LOGGER.debug(request.getRequestURL().toString());
		LOGGER.debug(request.getRequestURI());
		
		Context initContext;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");  
			DataSource dataSource = (DataSource)envContext.lookup("jdbc/XAdatasource"); 
			
			LOGGER.debug(dataSource.toString());
		} catch (NamingException e) {
			ExceptionUtils.printStackTraceToString(e);
		}  
		
		
		// path starting with '/' is interpreted as relative to current context root
		request.getRequestDispatcher("/pages/start.html").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		doGet(request, response);
	}

}

/**
 * 
 */
package ningyuan.pan.webx.web.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.util.exception.ExceptionUtils;

/**
 * @author ningyuan
 *
 */
//@WebServlet("/rsxservice")
public class RSXServiceServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RSXServiceServlet.class);
	
	// client is designed as a heavy object for reuse. 
	// The implementation of this object must be thread safe
	private Client RSClient;
	
	private Properties RSServiceURIs;
	
	@Override
	public void init() throws ServletException {
		LOGGER.debug("init()");
		
		try {
			RSClient = ClientBuilder.newClient();
		
			RSServiceURIs = new Properties();
		
			String RSServiceURIFile = getServletContext().getInitParameter("restful.webservice.target.uris.file");
				
			RSServiceURIs.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(RSServiceURIFile)));
        		
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
        }	
	}

	@Override
	public void destroy() {
		LOGGER.debug("destroy()");
		
		if(RSClient != null) {
			RSClient.close();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/plain;charset=utf-8");
        
		PrintWriter out = response.getWriter();
		
		try {
			WebTarget serviceBase = RSClient.target(RSServiceURIs.getProperty("xservice.target.uri"));
			
			String name = serviceBase.path(RSServiceURIs.getProperty("xservice.op.getName"))
					                 .request(MediaType.TEXT_PLAIN)
					                 .get(String.class);
			
			out.write(name+"   ");
			
			String user = serviceBase.path(RSServiceURIs.getProperty("xservice.op.getUser"))			  
				                     .request(MediaType.APPLICATION_JSON)
				                     .get(String.class);
			
			out.write(user);
		}
		catch (Exception e) {
			LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
		}
		finally {
			out.close();
		}	  
	}	
}

/**
 * 
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import listener.XServletContextListener;

/**
 * @author ningyuan
 *
 */
//@WebServlet("/servlet1")
public class Servlet1 extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Servlet1.class);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		LOGGER.debug("doGet()");
		
		HttpSession session = request.getSession();
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/html;charset=utf-8");
        
        String contextPara = getServletContext().getInitParameter("projectName");
        String servletPara = getInitParameter("servletName");
        
        PrintWriter out = response.getWriter();
        
        out.write(contextPara);
        out.write("   ");
        out.write(servletPara);
        out.close();
        
        session.invalidate();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doGet(request, response);
	}
}

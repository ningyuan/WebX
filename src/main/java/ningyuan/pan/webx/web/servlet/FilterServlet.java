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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;


/**
 * @author ningyuan
 *
 */
//@WebServlet("/filter")
public class FilterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		LOGGER.debug("doGet()");
		
		HttpSession session = request.getSession();
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/html;charset=utf-8");
        
        String contextPara = getServletContext().getInitParameter("projectName");
        String cacheName = getServletContext().getInitParameter("cacheName");
        String servletPara = getInitParameter("servletName");
        
        Cache cache = (Cache)getServletContext().getAttribute(cacheName);
        
        PrintWriter out = response.getWriter();
        
        if(cache != null) {
        	 out.write("cache name: "+cache.get("name"));
             out.write("</br>");
        }
       
        out.write("context para projectName: "+contextPara);
        out.write("</br>");
        out.write("servlet para servletName: "+servletPara);
        out.close();
        
        session.invalidate();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		LOGGER.debug("doPost()");
		
		doGet(request, response);
	}
}

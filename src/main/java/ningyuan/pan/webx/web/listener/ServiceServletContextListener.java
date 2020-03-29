/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ningyuan.pan.servicex.XService;
import ningyuan.pan.servicex.impl.XServiceImpl;


/**
 * @author ningyuan
 *
 */
public class ServiceServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		XService serviceX = new XServiceImpl();
		
		sce.getServletContext().setAttribute("ServiceX", serviceX);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("ServiceX");
	}

}

/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ningyuan.pan.servicex.ServiceX;
import ningyuan.pan.servicex.impl.ServiceXImpl;

/**
 * @author ningyuan
 *
 */
public class ServiceServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServiceX serviceX = new ServiceXImpl();
		
		sce.getServletContext().setAttribute("ServiceX", serviceX);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().removeAttribute("ServiceX");
	}

}

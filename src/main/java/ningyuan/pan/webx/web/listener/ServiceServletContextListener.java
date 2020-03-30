/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.session.SqlSession;

import ningyuan.pan.servicex.XService;
import ningyuan.pan.servicex.impl.XServiceJDBCImpl;
import ningyuan.pan.servicex.impl.XServiceMybatisImpl;
import ningyuan.pan.servicex.persistence.dao.UserDAO;
import ningyuan.pan.servicex.persistence.dao.impl.UserDAOJDBCImpl;
import ningyuan.pan.servicex.util.GlobalObjectName;
import ningyuan.pan.servicex.util.ServiceXUtil;
import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.util.persistence.DataSourceManager;
import ningyuan.pan.util.persistence.JDBCDataSourceManager;
import ningyuan.pan.util.persistence.MybatisDataSourceManager;


/**
 * @author ningyuan
 *
 */
public class ServiceServletContextListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DataSourceManager<SqlSession> dataSourceManager = null;
		//DataSourceManager<Connection> dataSourceManager = null;
		
		try {
			dataSourceManager = new MybatisDataSourceManager();
			//dataSourceManager = new JDBCDataSourceManager();
			
			//UserDAO userDAO = dataSourceManager.initAndGetThreadLocalConnection().getMapper(UserDAO.class);
			
			XService serviceX = new XServiceMybatisImpl();
			//XService serviceX = new XServiceJDBCImpl(userDAO);
			
			ServiceXUtil.getInstance().setGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER, dataSourceManager);
			//ServiceXUtil.getInstance().setGelobalObject(GlobalObjectName.JDBC_DATA_SOURCE_MANAGER, dataSourceManager);
			
			sce.getServletContext().setAttribute("ServiceX", serviceX);
		}
		catch(Exception e) {
			ExceptionUtils.printStackTraceToString(e);
			
			if(dataSourceManager != null) {
				dataSourceManager.removeAndCloseThreadLocalConnection();
			}
			
			sce.getServletContext().setAttribute("ServiceX", new XServiceJDBCImpl());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		@SuppressWarnings("unchecked")
		DataSourceManager<SqlSession> dataSourceManager = (DataSourceManager<SqlSession>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
		//DataSourceManager<Connection> dataSourceManager = (DataSourceManager<Connection>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.JDBC_DATA_SOURCE_MANAGER);
		
		if(dataSourceManager != null) {
			dataSourceManager.removeAndCloseThreadLocalConnection();
		}
		
		sce.getServletContext().removeAttribute("ServiceX");
	}

}

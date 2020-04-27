/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.servicex.XService;
import ningyuan.pan.servicex.impl.XServiceDAOImpl;
import ningyuan.pan.servicex.impl.XServiceMybatisImpl;
import ningyuan.pan.servicex.persistence.dao.UserDAO;
import ningyuan.pan.servicex.persistence.dao.impl.UserDAOJDBCImpl;
import ningyuan.pan.servicex.util.GlobalObjectName;
import ningyuan.pan.servicex.util.ServiceXUtil;
import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.util.persistence.DataSourceManager;
import ningyuan.pan.util.persistence.JDBCDataSourceManager;
import ningyuan.pan.util.persistence.MybatisDataSourceManager;
import ningyuan.pan.webx.util.ServiceName;
import ningyuan.pan.webx.web.servlet.XServiceServlet;


/**
 * @author ningyuan
 *
 */
public class ServiceServletContextListener implements ServletContextListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceServletContextListener.class);
	
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
			
			sce.getServletContext().setAttribute(ServiceName.X_SERVICE.getName(), serviceX);
		}
		catch(Exception e) {
			LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			
			if(dataSourceManager != null) {
				try {
					dataSourceManager.close();
				} catch (Exception e1) {
					LOGGER.debug(ExceptionUtils.printStackTraceToString(e1));
				}
			}
			
			ServiceXUtil.getInstance().removeGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
			sce.getServletContext().removeAttribute(ServiceName.X_SERVICE.getName());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		@SuppressWarnings("unchecked")
		DataSourceManager<SqlSession> dataSourceManager = (DataSourceManager<SqlSession>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
		//DataSourceManager<Connection> dataSourceManager = (DataSourceManager<Connection>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.JDBC_DATA_SOURCE_MANAGER);
		
		if(dataSourceManager != null) {
			try {
				dataSourceManager.close();
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			}
		}
		
		ServiceXUtil.getInstance().removeGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
		sce.getServletContext().removeAttribute(ServiceName.X_SERVICE.getName());
	}

}

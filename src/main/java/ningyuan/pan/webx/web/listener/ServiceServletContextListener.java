/**
 * 
 */
package ningyuan.pan.webx.web.listener;

import java.sql.Connection;

import javax.jms.Session;
import javax.persistence.EntityManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.servicex.XService;
import ningyuan.pan.servicex.impl.XServiceDAOImpl;
import ningyuan.pan.servicex.impl.XServiceMybatisImpl;
import ningyuan.pan.servicex.persistence.dao.RoleDAO;
import ningyuan.pan.servicex.persistence.dao.UserDAO;
import ningyuan.pan.servicex.persistence.dao.impl.RoleDAOJPAImpl;
import ningyuan.pan.servicex.persistence.dao.impl.UserDAOJDBCImpl;
import ningyuan.pan.servicex.persistence.dao.impl.UserDAOJPAImpl;
import ningyuan.pan.servicex.util.GlobalObjectName;
import ningyuan.pan.servicex.util.ServiceXUtil;
import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.util.persistence.ActiveMQXADataSourceManager;
import ningyuan.pan.util.persistence.DataSourceManager;
import ningyuan.pan.util.persistence.JDBCDataSourceManager;
import ningyuan.pan.util.persistence.JPADataSourceManager;
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
		//DataSourceManager<SqlSession> dataSourceManager = null;
		//DataSourceManager<Connection> dataSourceManager = null;
		DataSourceManager<EntityManager> XADataSourcemanager = null;
		DataSourceManager<Session> JMSXADataSourceManager = null;
		
		try {
			//dataSourceManager = new MybatisDataSourceManager();
			//dataSourceManager = new JDBCDataSourceManager();
			XADataSourcemanager = new JPADataSourceManager("dev-jta-hibernate");
			JMSXADataSourceManager = new ActiveMQXADataSourceManager();
			
			//UserDAO userDAO = dataSourceManager.initAndGetThreadLocalConnection().getMapper(UserDAO.class);
			UserDAO userDAO = new UserDAOJPAImpl(XADataSourcemanager);
			RoleDAO roleDAO = new RoleDAOJPAImpl(XADataSourcemanager);
			
			//XService serviceX = new XServiceMybatisImpl();
			//XService serviceX = new XServiceJDBCImpl(userDAO, roleDAO);
			XService serviceX = new XServiceDAOImpl(userDAO, roleDAO);
			
			//ServiceXUtil.getInstance().setGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER, dataSourceManager);
			//ServiceXUtil.getInstance().setGelobalObject(GlobalObjectName.JDBC_DATA_SOURCE_MANAGER, dataSourceManager);
			ServiceXUtil.getInstance().setGelobalObject(GlobalObjectName.XA_DATA_SOURCE_MANAGER, XADataSourcemanager);
			ServiceXUtil.getInstance().setGelobalObject(GlobalObjectName.JMS_XA_DATA_SOURCE_MANAGER, JMSXADataSourceManager);
			
			System.out.println("!!!"+ServiceName.X_SERVICE.getName());
			sce.getServletContext().setAttribute(ServiceName.X_SERVICE.getName(), serviceX);
		}
		catch(Exception e) {
			LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			
			/*if(dataSourceManager != null) {
				try {
					dataSourceManager.close();
				} catch (Exception e1) {
					LOGGER.debug(ExceptionUtils.printStackTraceToString(e1));
				}
			}*/
			
			ServiceXUtil.getInstance().removeGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
			sce.getServletContext().removeAttribute(ServiceName.X_SERVICE.getName());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		@SuppressWarnings("unchecked")
		//DataSourceManager<SqlSession> dataSourceManager = (DataSourceManager<SqlSession>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
		//DataSourceManager<Connection> dataSourceManager = (DataSourceManager<Connection>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.JDBC_DATA_SOURCE_MANAGER);
		DataSourceManager<EntityManager> XADataSourcemanager = (DataSourceManager<EntityManager>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.XA_DATA_SOURCE_MANAGER);
		DataSourceManager<Session> JMSXADataSourceManager = (DataSourceManager<Session>)ServiceXUtil.getInstance().getGelobalObject(GlobalObjectName.JMS_XA_DATA_SOURCE_MANAGER);
		
		
		/*if(dataSourceManager != null) {
			try {
				dataSourceManager.close();
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			}
		}*/
		
		if(XADataSourcemanager != null) {
			try {
				XADataSourcemanager.close();
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			}
		}
		
		if(JMSXADataSourceManager != null) {
			try {
				JMSXADataSourceManager.close();
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
			}
		}
		
		//ServiceXUtil.getInstance().removeGelobalObject(GlobalObjectName.MYBATIS_DATA_SOURCE_MANAGER);
		ServiceXUtil.getInstance().removeGelobalObject(GlobalObjectName.XA_DATA_SOURCE_MANAGER);
		ServiceXUtil.getInstance().removeGelobalObject(GlobalObjectName.JMS_XA_DATA_SOURCE_MANAGER);
		
		sce.getServletContext().removeAttribute(ServiceName.X_SERVICE.getName());
	}

}

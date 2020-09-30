/**
 * 
 */
package ningyuan.pan.webx.web.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
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
//@WebServlet(urlPatterns={"/rsxservice"}, asyncSupported=true)
public class RSXServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RSXServiceServlet.class);
	
	// client is designed as a heavy object for reuse. 
	// The implementation of this object must be thread safe
	private Client RSClient;
	
	private Properties RSServiceURIs;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(50);
	
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
		
		threadPool.shutdownNow();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet()");
		
		response.setCharacterEncoding("UTF-8");
        
		response.setContentType("text/plain;charset=utf-8");
		
		final AsyncContext asyncContext = request.startAsync(request, response);
		
		asyncContext.setTimeout(5000);
		
		asyncContext.addListener(new AsyncListener() {
			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				LOGGER.debug("Async task timeout " + event);
				
				/*
				 * XXX cause:
				 *     PM org.apache.catalina.connector.CoyoteAdapter checkRecycled
				 *	   INFO: Encountered a non-recycled response and recycled it forcedly.
                 *     org.apache.catalina.connector.CoyoteAdapter$RecycleRequiredException
				 */
				try (PrintWriter out = response.getWriter()) {
					response.getWriter().write("Async task timeout");
				}
				finally {
					asyncContext.complete();
				}
			}
			
			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {}
			
			@Override
			public void onError(AsyncEvent event) throws IOException {
				LOGGER.debug("Async task error " + event);
				
				try (PrintWriter out = response.getWriter()) {
					response.getWriter().write("Async task error");
				}
				finally {
					asyncContext.complete();
				}
			}
			
			@Override
			public void onComplete(AsyncEvent event) throws IOException {}
		});
		
		AsyncTask task = new AsyncTask(response, asyncContext);
		
		threadPool.execute(task);
	}
	
	private class AsyncTask implements Runnable {
		
		private final HttpServletResponse response;
		
		private final AsyncContext asyncContext;
		
		AsyncTask(HttpServletResponse response, AsyncContext asyncContext) {
			this.response = response;
			this.asyncContext = asyncContext;
		}
		
		@Override
		public void run() {
			PrintWriter out = null;
			
			try {
				out = response.getWriter();
				
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
				try {
					if(out != null) {
						out.close();
					}
				}
				catch(Exception e) {
					LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
				}
				finally {
					try {
						asyncContext.complete();
					}
					catch(Exception e) {
						LOGGER.debug(ExceptionUtils.printStackTraceToString(e));
					}
				}
			}
		}
	}
}

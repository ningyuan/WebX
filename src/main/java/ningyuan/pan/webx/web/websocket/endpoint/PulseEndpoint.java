/**
 * 
 */
package ningyuan.pan.webx.web.websocket.endpoint;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.util.exception.ExceptionUtils;

/**
 * @author ningyuan
 *
 */
@ServerEndpoint("/websocket/pulse")
public class PulseEndpoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(PulseEndpoint.class);
	
	private static final String MESSAGE_THREAD = "mt";
	
	@OnOpen
	public void open(Session session, EndpointConfig conf) { 
		LOGGER.debug("open()");
		
		Thread mt = (Thread)session.getUserProperties().get(MESSAGE_THREAD);
		
		if(mt == null) {
			mt = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(!Thread.currentThread().isInterrupted()) {
						try {
							String num = String.valueOf(Math.random());
							session.getBasicRemote().sendText(num);
							
							Thread.sleep(200);
						}
						catch (IOException ioe) {
							LOGGER.debug(ExceptionUtils.printStackTraceToString(ioe));
							break;
						} catch (InterruptedException ie) {
							break;
						}
					}
				}
			});
			
			session.getUserProperties().put(MESSAGE_THREAD, mt);
			mt.start();
		}
	}
	
	@OnMessage
	public void message(Session session, String msg) { 
		LOGGER.debug("message() "+msg);
	}
	
	@OnError
	public void error(Session session, Throwable error) { 
		LOGGER.debug("error()");
	}
	
	@OnClose
	public void close(Session session, CloseReason reason) {
		LOGGER.debug("close()");
		Thread mt = (Thread)session.getUserProperties().get(MESSAGE_THREAD);
		
		if(mt != null) {
			mt.interrupt();
		}
		
		session.getUserProperties().remove(MESSAGE_THREAD);
	}
}

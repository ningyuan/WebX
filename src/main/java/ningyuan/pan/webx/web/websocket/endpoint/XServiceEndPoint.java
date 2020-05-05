/**
 * 
 */
package ningyuan.pan.webx.web.websocket.endpoint;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ningyuan
 *
 */
public class XServiceEndPoint extends Endpoint{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XServiceEndPoint.class);
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		
	}
	
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		// TODO Auto-generated method stub
		super.onClose(session, closeReason);
	}

	@Override
	public void onError(Session session, Throwable thr) {
		// TODO Auto-generated method stub
		super.onError(session, thr);
	}

}

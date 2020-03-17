/**
 * 
 */
package ningyuan.pan.webx.util.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ningyuan
 *
 */
public class Utils {
	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
	
	public static String printStackTraceToString(Exception exception) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		exception.printStackTrace(new PrintStream(out));
		
		String ret = out.toString();
		
		try {
			out.close();
		} catch (IOException e) {
			LOGGER.error(e.getCause().getClass().getName());
			LOGGER.error(e.getCause().getMessage());
		}
		
		return ret;
	}
}

/**
 * 
 */
package ningyuan.pan.webx.util.cache;

/**
 * @author ningyuan
 *
 */
public interface Cache {

	public void open(String... args);
	
	public void clear() throws Exception;
	
	public void setExpire(int seconds);
	
	public void close(String... args);
	
	public String getText(String key) throws Exception;
	
	public boolean putText(String key, String value) throws Exception;
	
	public byte[] getBinary(String key) throws Exception;
	
	public boolean putBinary(String key, byte[] value) throws Exception;
	
	public boolean remove(String key) throws Exception;
	
}

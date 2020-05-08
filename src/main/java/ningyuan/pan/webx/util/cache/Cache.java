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
	
	public void clear();
	
	public void setExpire(int seconds);
	
	public void close(String... args);
	
	public String getText(String key);
	
	public boolean putText(String key, String value);
	
	public byte[] getBinary(String key);
	
	public boolean putBinary(String key, byte[] value);
	
	public boolean remove(String key);
	
}

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
	
	public String get(String key);
	
	public boolean put(String key, String value);
	
	public boolean remove(String key);
	
	public void close(String... args);

}

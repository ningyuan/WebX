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
	
	public void close(String... args);

}

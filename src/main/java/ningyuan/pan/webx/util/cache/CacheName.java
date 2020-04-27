/**
 * 
 */
package ningyuan.pan.webx.util.cache;

/**
 * @author ningyuan
 *
 */
public enum CacheName {
	REDIS("Redis");
	
	private String name;
	
	private CacheName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

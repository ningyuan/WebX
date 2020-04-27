/**
 * 
 */
package ningyuan.pan.webx.util;

/**
 * @author ningyuan
 *
 */
public enum ServiceName {
	X_SERVICE("XService");
	
	private String name;
	
	private ServiceName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	} 
}

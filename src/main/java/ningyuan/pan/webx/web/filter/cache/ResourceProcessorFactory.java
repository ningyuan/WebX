/**
 * 
 */
package ningyuan.pan.webx.web.filter.cache;

import ningyuan.pan.util.net.MediaType;

/**
 * @author ningyuan
 *
 */
class ResourceProcessorFactory {
	private static final String TYPE_NONE_STATIC = "ns";
	
	private static final String TYPE_TEXT = "text";
	
	private static final String TYPE_BINARY = "binary";
	
	private static final MediaType[] TEXT_MEDIA_TYPES = {MediaType.HTML, MediaType.CSS, MediaType.JAVASCRIPT};
	
	private static final MediaType[] BINARY_MEDIA_TYPES = {MediaType.BMP, MediaType.JPEG};
	
	
	private static TextStaticResourceProcessor tsrp = new TextStaticResourceProcessor();
	
	private static BinaryStaticResourceProcessor bsrp = new BinaryStaticResourceProcessor();
	
	private static NonStaticResourceProcessor nsrp = new NonStaticResourceProcessor();
	
	static ResourceProcessor getInstence(MediaType mediaType) {
		
		// the type of the static resource
		String type = getStaticResourceType(mediaType);
		
		if(TYPE_TEXT.equals(type)) {
			return tsrp;
		}
		else if(TYPE_BINARY.equals(type)) {
			return bsrp;
		}
		else {
			return nsrp;
		}
	}
	
	static private String getStaticResourceType(MediaType mediaType) {
		for(MediaType tType : TEXT_MEDIA_TYPES) {
			if(mediaType == tType) {
				return TYPE_TEXT;
			}
		}
		
		for(MediaType bType : BINARY_MEDIA_TYPES) {
			if(mediaType == bType) {
				return TYPE_BINARY;
			}
		}
		
		return TYPE_NONE_STATIC;
	}
}

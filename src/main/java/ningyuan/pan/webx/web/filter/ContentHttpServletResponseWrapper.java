/**
 * 
 */
package ningyuan.pan.webx.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author ningyuan
 *
 */
class ContentHttpServletResponseWrapper extends HttpServletResponseWrapper {
	private ServletOutputStream outputStream = null;
	private PrintWriter writer = null;
	// the content of ByteArrayOutputStream could be retrieved
	private ByteArrayOutputStream content = null;
	
	public ContentHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		
		content = new ByteArrayOutputStream();
		outputStream = new ServletOutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				content.write(b);
			}
			
			@Override
			public void setWriteListener(WriteListener writeListener) {}
			
			@Override
			public boolean isReady() {
				return false;
			}
		};
		
		writer = new PrintWriter(new OutputStreamWriter(content));
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}
	
	public String getContentInString() {
		try {
			content.flush();
		} 
		catch (IOException e) {}
		
		return content.toString(StandardCharsets.UTF_8);
	}
	
	public byte[] getContentInByte() {
		try {
			content.flush();
		}
		catch (IOException e) {}
		
		return content.toByteArray();
	}
}
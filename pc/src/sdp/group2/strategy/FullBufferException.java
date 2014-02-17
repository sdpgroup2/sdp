package sdp.group2.strategy;

public class FullBufferException extends Exception {

	public FullBufferException(String message) {
		super(message);
	}
	
	public FullBufferException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FullBufferException(Throwable cause) {
		super(cause);
	}
	
}

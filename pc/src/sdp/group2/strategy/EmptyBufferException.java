package sdp.group2.strategy;

public class EmptyBufferException extends Exception {
	
	private static final long serialVersionUID = 666;

	public EmptyBufferException(String message) {
		super(message);
	}
	
	public EmptyBufferException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EmptyBufferException(Throwable cause) {
		super(cause);
	}

}

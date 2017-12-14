package com.bpm.framework.exception;

/**
 * 
 * 
 * @author Gin
 * @since 1.0
 * @version 2015-06-15 
 */
public class PicRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3479228711437215059L;

	public PicRuntimeException() {
		super();
	}

	public PicRuntimeException(String message) {
		super(message);
	}

	public PicRuntimeException(Throwable cause) {
		super(cause);
	}

	public PicRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}

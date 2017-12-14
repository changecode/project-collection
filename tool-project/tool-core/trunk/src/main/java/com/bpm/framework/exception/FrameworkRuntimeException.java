package com.bpm.framework.exception;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 * @version 2015-01-26 18:51:00
 */
public class FrameworkRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3479228711437215059L;

	public FrameworkRuntimeException() {
		super();
	}

	public FrameworkRuntimeException(String message) {
		super(message);
	}

	public FrameworkRuntimeException(Throwable cause) {
		super(cause);
	}

	public FrameworkRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}

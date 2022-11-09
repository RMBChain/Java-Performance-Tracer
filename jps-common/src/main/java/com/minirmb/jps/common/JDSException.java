package com.minirmb.jps.common;

public class JDSException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1516933448131520911L;

	public JDSException() {
		super();
	}

	public JDSException(String message) {
		super(message);
	}

	public JDSException(String message, Throwable cause) {
		super(message, cause);
	}

	public JDSException(Throwable cause) {
		super(cause);
	}

	protected JDSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

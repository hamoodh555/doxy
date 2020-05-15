package com.xerago.rsa.util;

public class PortalException extends Exception {
	// private static final Log LOGGER =
	// LogFactory.getLog(PortalException.class);

	private static final long serialVersionUID = 1L;

	public PortalException() {
		super();
	}

	public PortalException(String s) {
		super(s);
		// LOGGER.error(s);
	}

	public PortalException(Throwable throwable) {
		super(throwable.getMessage());
		// LOGGER.error("error message --->>>>   ", throwable);
	}
}

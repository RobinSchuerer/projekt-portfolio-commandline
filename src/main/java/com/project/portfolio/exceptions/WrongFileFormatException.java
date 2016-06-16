package com.project.portfolio.exceptions;

public class WrongFileFormatException extends RuntimeException {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongFileFormatException(String message) {
	        super(message);
	    }
	 
	 public WrongFileFormatException(String message, Throwable cause) {
	        super(message, cause);
	    }
	

}

package com.test.framwork.cucumber.exception;

/**
 * TestException is the single custom exception class for the entire framework
 * All framework-related errors throw this exception
 * 
 * Usage:
 * - throw new TestException("Error message");
 * - throw new TestException("Error message", cause);
 * - throw new TestException(cause);
 */
public class TestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with error message
	 * 
	 * @param message - detailed error message
	 */
	public TestException(String message) {
		super(message);
	}

	/**
	 * Constructor with error message and root cause
	 * 
	 * @param message - detailed error message
	 * @param cause - root cause of exception
	 */
	public TestException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with root cause only
	 * 
	 * @param cause - root cause of exception
	 */
	public TestException(Throwable cause) {
		super(cause);
	}

}

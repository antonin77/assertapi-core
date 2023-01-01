package org.usf.assertapi.core;

import lombok.Getter;

/**
 * 
 * @author u$f
 * @since 1.0
 *
 */
@Getter
@SuppressWarnings("serial")
public final class ApiAssertionError extends AssertionError {

	private final boolean skipped;
	private final transient Object expected;
	private final transient Object actual;

	ApiAssertionError(Object expected, Object actual, String msg) {
		this(false, expected, actual, msg);
	}
	
	private ApiAssertionError(boolean skipped, Object expected, Object actual, String msg) {
		super(msg);
		this.skipped = skipped;
		this.expected = expected;
		this.actual = actual;
	}
	
	public static ApiAssertionError skippedAssertionError(String msg) {
		return new ApiAssertionError(true, null, null, msg);
	}
	
}

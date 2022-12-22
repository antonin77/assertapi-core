package org.usf.assertapi.core;

import static java.util.Optional.ofNullable;

import lombok.Getter;

/**
 * 
 * @author u$f
 *
 */
@Getter
public final class ExecutionConfig {

	private boolean enable;
	private final boolean parallel;
	
	public ExecutionConfig(Boolean enable, Boolean parallel) {
		this.enable = ofNullable(enable).orElse(true);
		this.parallel = ofNullable(parallel).orElse(true);
	}
	
	static ExecutionConfig defaultConfig() {
		return new ExecutionConfig(null, null);
	}

	public ExecutionConfig disable() {
		this.enable = false;
		return this;
	}

	public ExecutionConfig enable() {
		this.enable = true;
		return this;
	}
}
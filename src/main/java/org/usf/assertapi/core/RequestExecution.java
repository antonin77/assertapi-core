package org.usf.assertapi.core;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor // ??
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public final class RequestExecution {
	
	private final String host;
	private long start;
	private long end;

	@Override
	public String toString() {
		return "run on " + host + " in " + (end - start) + " ms";
	}
}
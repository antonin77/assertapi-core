package org.usf.assertapi.core;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.toList;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.usf.assertapi.core.ContentComparator.ResponseType.JSON;
import static org.usf.assertapi.core.Module.defaultJsonParser;
import static org.usf.assertapi.core.ReleaseTarget.LATEST;
import static org.usf.assertapi.core.ReleaseTarget.STABLE;

import java.util.stream.Stream;

import org.json.JSONException;

import com.jayway.jsonpath.DocumentContext;

import lombok.Getter;

/**
 * 
 * @author u$f
 * @since 1.0
 *
 */
@Getter
public final class JsonContentComparator implements ContentComparator<String> {

	private final boolean strict;
	private final ResponseTransformer<DocumentContext>[] transformers;

	public JsonContentComparator(Boolean strict, ResponseTransformer<DocumentContext>[] transformers) {
		this.strict = requireNonNullElse(strict, true);
		this.transformers = transformers;
	}
	
	@Override
	public CompareResult compare(String expected, String actual) throws JSONException {
		try {
			if(transformers != null) {
				expected = transform(expected, STABLE);
				actual = transform(actual, LATEST);
			}
			assertEquals(expected, actual, strict);
			return new CompareResult(expected, actual, true);
		} catch (AssertionError e) {
			return new CompareResult(expected, actual, false);
		}
	}
	
	private final String transform(String resp, ReleaseTarget target){
		if(resp != null) {
			var list = Stream.of(transformers)
					.filter(t-> t.matchTarget(target))
					.collect(toList());
			if(!list.isEmpty()) {
				var json = defaultJsonParser().parse(resp);
				list.forEach(t-> t.transform(json));
				return json.jsonString();
			}
		}
		return resp;
	}

	@Override
	public String getType() {
		return JSON.name();
	}
}
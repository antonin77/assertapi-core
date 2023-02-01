package org.usf.assertapi.core;

import static org.usf.assertapi.core.JsonDataComparator.jsonParser;
import static org.usf.junit.addons.AssertExt.assertThrowsWithMessage;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.usf.junit.addons.ConvertWithObjectMapper;
import org.usf.junit.addons.FolderSource;
import org.usf.junit.addons.ThrowableMessage;

class JsonPathMoverTest {

	@Test
	void testJsonPathMover() {
//		fail("Not yet implemented");
	}

	@Test
	void testGetType() {
//		fail("Not yet implemented");
	}
	

	@ParameterizedTest
	@FolderSource(path="json/path-mover")
	void testTransform(String origin, String expected,
			@ConvertWithObjectMapper ThrowableMessage exception,
			@ConvertWithObjectMapper(clazz=Utils.class, method="defaultMapper") JsonPathMover transformer) throws JSONException {
		var json = jsonParser.parse(origin);
		if(exception == null) {
			JSONAssert.assertEquals(expected, transformer.transform(json).jsonString(), true);
		}
		else {
			assertThrowsWithMessage(exception, ()-> transformer.transform(json));
		}
	}

}
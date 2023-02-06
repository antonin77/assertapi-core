package org.usf.assertapi.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.usf.assertapi.core.Utils.defaultMapper;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;

class HttpRequestTest {
	
	@Test
	void testContructor_uri() {
		assertThrows(IllegalArgumentException.class, ()-> new HttpRequest(null, null, null, null, null));
		assertEquals("/",  new HttpRequest("", null, null, null, null).getUri());
		assertEquals("/", new HttpRequest("\t", null, null, null, null).getUri());
		assertEquals("/api", new HttpRequest("api", null, null, null, null).getUri());
		assertEquals("/api", new HttpRequest("\tapi", null, null, null, null).getUri());
		assertEquals("/api", new HttpRequest("/api\t", null, null, null, null).getUri());
	}
	
	@Test
	void testContructor_method() {
		assertEquals("GET",  new HttpRequest("", "get", null, null, null).getMethod());
		assertEquals("POST", new HttpRequest("", "\tPost", null, null, null).getMethod());
		assertEquals("DELETE", new HttpRequest("", "DELETE\t", null, null, null).getMethod());
	}
	
//	@Test
//	void testContructor_acceptableStatus() {
//		assertArrayEquals(new int[] {200},  new HttpRequest("", null, null, null).getAcceptableStatus());
//		assertArrayEquals(new int[] {200},  new HttpRequest("", null, null, null, new int[]{}).getAcceptableStatus());
//		assertArrayEquals(new int[] {200,404}, new HttpRequest("", null, null, null, new int[] {200,404}).getAcceptableStatus());
//	}

	@Test
	void testHasHeaders() {
		assertFalse(new HttpRequest("", null, null, null, null).hasHeaders());
		assertFalse(new HttpRequest("", null, emptyMap(), null, null).hasHeaders());
		assertTrue(new HttpRequest("", null, Map.of("hdr1", asList("value1")), null, null).hasHeaders());
	}
	
	@ParameterizedTest
	@CsvSource({
	    "GET   , v1/api        ,[GET] /v1/api",
	    "post  , v2/api/exemple,[POST] /v2/api/exemple",
	    "Delete, v3/resource/r1,[DELETE] /v3/resource/r1"})
	void testToRequestUri(String method, String uri, String expected) {
		var req = new HttpRequest(uri, method, null, null, null);
		assertEquals(expected, req.toRequestUri());
		assertEquals(expected, req.toString());
	}
	
//	@ParameterizedTest
//	@CsvSource({
//	    "500,500,true",
//	    "500,200,false",
//	    "200;404,200,true",
//	    "200;404,500,false"})
//	void testAcceptStatus(@ConvertWith(IntArrayConverter.class) int[] arr, int code, boolean expected) {
//		var req = new HttpRequest("", null, null, null, arr);
//		assertEquals(expected, req.acceptStatus(code));
//	}
	
	@ParameterizedTest
	@MethodSource("serializeTestcases")
	void testSerialize(HttpRequest req, String expected) throws JsonProcessingException, JSONException {
		var json = defaultMapper().writeValueAsString(req);
		JSONAssert.assertEquals(expected, json, true);
	}
	
	@ParameterizedTest
	@MethodSource({"serializeTestcases", "deserializeTestcases"})
	void testDeserialize(HttpRequest expected, String json) throws JSONException, IOException {
		var hr = defaultMapper().readValue(json, HttpRequest.class);
		assertEquals(expected.getUri(), hr.getUri());
		assertEquals(expected.getMethod(), hr.getMethod());
		assertEquals(expected.getHeaders(), hr.getHeaders());
		JSONAssert.assertEquals(expected.bodyAsString(), hr.bodyAsString(), true);
//		assertArrayEquals(expected.getAcceptableStatus(), hr.getAcceptableStatus());
	}
	
	static class IntArrayConverter implements ArgumentConverter {
		@Override
		public int[] convert(Object source, ParameterContext context) throws ArgumentConversionException {
			return Stream.of(source.toString().split(";")).mapToInt(Integer::parseInt).toArray();
		}
	}
	
	static Stream<Arguments> serializeTestcases() throws JsonProcessingException {
		return Stream.of(
				Arguments.of(new HttpRequest("/api", null, null, null, null),
						defaultMapper().writeValueAsString(Map.of(
								"uri", "/api", 
								"method", "GET"))),
				Arguments.of(new HttpRequest("/api", "POST", Map.of("hdr", asList("value")), null, null), 
						defaultMapper().writeValueAsString(Map.of(
								"uri", "/api", 
								"method", "POST", 
								"headers", Map.of("hdr", asList("value"))))),
				Arguments.of(new HttpRequest("/api", "PUT", null, "{\"arrFld\":[\"v1\",\"v2\",\"v3\"],\"intFld\":1234,\"boolFld\":true,\"strFld\":\"value\"}".getBytes(), null), 
						defaultMapper().writeValueAsString(Map.of(
								"uri", "/api", 
								"method", "PUT", 
								"body", Map.of("arrFld", new String[] {"v1", "v2", "v3"}, "intFld", 1234, "boolFld", true, "strFld", "value"))))
		);
	}
	
	static Stream<Arguments> deserializeTestcases() throws JsonProcessingException {
		return Stream.of(Arguments.of(new HttpRequest("/api", "GET", null, null, null), defaultMapper().writeValueAsString(Map.of("uri", "/api"))));
	}
	
}

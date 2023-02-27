package org.usf.assertapi.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.usf.assertapi.core.ExecutionConfig.DEFAULT_CONFIG;

import org.junit.jupiter.api.Test;

class ExecutionConfigTest {

	@Test
	void testDefaultConfig() {
		assertTrue(DEFAULT_CONFIG.isEnabled());
		assertTrue(DEFAULT_CONFIG.isParallel());
	}
	
	@Test
	void testContructor_enabled() {
		assertFalse(new ExecutionConfig(false, null).isEnabled());
		assertTrue(new ExecutionConfig(true, null).isEnabled());
	}

	@Test
	void testContructor_parallel() {
		assertFalse(new ExecutionConfig(null, false).isParallel());
		assertTrue(new ExecutionConfig(null, true).isParallel());
	}

	@Test
	void testEnabled() {
		assertTrue(new ExecutionConfig(false, null).enable().isEnabled());
		assertTrue(new ExecutionConfig(true, null).enable().isEnabled());
	}

	@Test
	void testDisabled() {
		assertFalse(new ExecutionConfig(false, null).disable().isEnabled());
		assertFalse(new ExecutionConfig(true, null).disable().isEnabled());
	}
	
	@Test
	void testToString() {
		assertEquals("ExecutionConfig(enabled=true, parallel=true)", DEFAULT_CONFIG.toString());
	}
}

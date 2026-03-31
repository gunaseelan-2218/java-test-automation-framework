package com.test.framwork.cucumber.context;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.test.framwork.cucumber.utils.LoggerUtil;

/**
 * ScenarioContext stores scenario-specific data shared across step definitions
 * Initialized once per scenario during context creation (Step 1)
 * Cleared after scenario execution in @After hook
 * 
 * Used to pass data between different steps in a scenario
 * Uses SLF4J for logging
 */
public class ScenarioContext {

	private static final Logger logger = LoggerUtil.getLogger(ScenarioContext.class);
	private Map<String, Object> context = new HashMap<>();

	/**
	 * Set a key-value pair in scenario context Used to pass data between steps
	 * 
	 * Example: testContext.getScenarioContext().set("productName", "Tomatoes");
	 * 
	 * @param key   - unique identifier for the data
	 * @param value - data to store (any object type)
	 */
	public void set(String key, Object value) {
		context.put(key, value);
		logger.debug("ScenarioContext.set() - Key: {}, Value: {}", key, value);
	}

	/**
	 * Get a value from scenario context (returns Object) Requires manual casting
	 * 
	 * Example: String product = (String)
	 * testContext.getScenarioContext().get("productName");
	 * 
	 * @param key - unique identifier
	 * @return - stored object or null if not found
	 */
	public Object get(String key) {
		Object value = context.get(key);
		if (value != null) {
			logger.debug("ScenarioContext.get() - Key: {}, Value: {}", key, value);
		} else {
			logger.warn("ScenarioContext.get() - Key not found: {}", key);
		}
		return value;
	}

	/**
	 * Get value with automatic type casting (Type-safe)
	 * Preferred method - no manual casting required
	 * 
	 * Example: String product = testContext.getScenarioContext().get("productName", String.class);
	 * Example: Integer price = testContext.getScenarioContext().get("productPrice", Integer.class);
	 * 
	 * @param key - unique identifier
	 * @param type - expected class type for casting
	 * @return - cast object or null if not found
	 * @throws ClassCastException if stored value is not of expected type
	 */
	public <T> T get(String key, Class<T> type) {
		Object value = context.get(key);
		if (value == null) {
			logger.warn("ScenarioContext.get() - Key not found: {}", key);
			return null;
		}
		try {
			T castValue = type.cast(value);
			logger.debug("ScenarioContext.get() - Key: {}, Type: {}, Value: {}", key, type.getSimpleName(), castValue);
			return castValue;
		} catch (ClassCastException e) {
			String errorMsg = String.format("Failed to cast value for key '%s' to type %s", key, type.getSimpleName());
			logger.error(errorMsg, e);
			throw new ClassCastException(errorMsg);
		}
	}

	/**
	 * Check if a key exists in scenario context
	 * Useful for conditional step execution
	 * 
	 * Example: if (testContext.getScenarioContext().isKeyPresent("cartTotal")) { ... }
	 * 
	 * @param key - unique identifier
	 * @return - true if key exists, false otherwise
	 */
	public boolean isKeyPresent(String key) {
		boolean exists = context.containsKey(key);
		logger.debug("ScenarioContext.isKeyPresent() - Key: {}, Exists: {}", key, exists);
		return exists;
	}

	/**
	 * Remove a specific key from context
	 * 
	 * @param key - unique identifier to remove
	 */
	public void remove(String key) {
		if (context.containsKey(key)) {
			context.remove(key);
			logger.debug("ScenarioContext.remove() - Key removed: {}", key);
		} else {
			logger.warn("ScenarioContext.remove() - Key not found: {}", key);
		}
	}

	/**
	 * Clear all context data
	 * Called during @After hook for cleanup between scenarios
	 * Ensures no data leakage between scenarios
	 */
	public void clear() {
		logger.debug("Clearing ScenarioContext - Current size: {}", context.size());
		context.clear();
		logger.debug("ScenarioContext cleared successfully");
	}

	/**
	 * Get the size of context (number of stored items)
	 * Useful for debugging
	 * 
	 * @return - number of key-value pairs
	 */
	public int size() {
		int size = context.size();
		logger.debug("ScenarioContext.size() - Total items: {}", size);
		return size;
	}

}

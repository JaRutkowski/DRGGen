package com.javafee.controller.utils.cache;

import java.util.HashMap;

public class Cache implements BaseCache {
	private static Cache cache = null;

	private HashMap<String, Boolean> swapObjectMap = new HashMap<>();
	private HashMap<String, Object> currentObjectMap = new HashMap<>();
	private HashMap<String, Object> previousObjectMap = new HashMap<>();

	private Cache() {
	}

	public static Cache getInstance() {
		if (cache == null) {
			cache = new Cache();
		}
		return cache;
	}

	public void cache(String key, Object value) {
		if (currentObjectMap.get(key) != null && previousObjectMap.get(key) != null
				&& currentObjectMap.get(key).equals(previousObjectMap.get(key)))
			swapObjectMap.put(key, true);
		previousObjectMap.put(key, swapObjectMap.get(key) != null && swapObjectMap.get(key) ? currentObjectMap.get(key) : value);
		currentObjectMap.put(key, value);
	}

	public Object get(String key) {
		return previousObjectMap.get(key);
	}

	public void clear(String key) {
		currentObjectMap.remove(key);
		previousObjectMap.remove(key);
	}
}

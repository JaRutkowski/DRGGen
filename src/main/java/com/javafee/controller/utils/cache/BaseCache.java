package com.javafee.controller.utils.cache;

public interface BaseCache {
	void cache(String key, Object value);

	Object get(String key);

	void clear(String key);
}

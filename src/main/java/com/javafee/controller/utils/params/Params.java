package com.javafee.controller.utils.params;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Params implements BaseParams {
	private static Params comParams = null;
	private Map<String, Object> params = new HashMap<String, Object>();

	private Params() {
	}

	public static Params getInstance() {
		if (comParams == null) {
			comParams = new Params();
		}
		return comParams;
	}

	@Override
	public void add(String key, Object value) {
		params.put(key, value);
	}

	@Override
	public Object get(String key) {
		return params.get(key);
	}

	@Override
	public boolean contains(String key) {
		return params.containsKey(key);
	}

	@Override
	public void remove(String key) {
		params.remove(key);
	}

	@Override
	public Collection<String> keySet() {
		return Collections.unmodifiableCollection(params.keySet());
	}

	@Override
	public Map<String, Object> getMap() {
		return Collections.unmodifiableMap(params);
	}
}

package com.javafee.model.foodb.driver;

import java.io.IOException;

import com.javafee.model.foodb.FooDBUtils;

public class FooDB {
	protected String name;

	public FooDB(String name) throws IOException {
		FooDBUtils.initializeDataBase(name);
		this.name = name;
	}
}

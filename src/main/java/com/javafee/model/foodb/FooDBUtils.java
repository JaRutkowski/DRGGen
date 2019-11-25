package com.javafee.model.foodb;

import java.io.File;
import java.io.IOException;

public class FooDBUtils {
	public static void initializeDataBase(String name) throws IOException {
		try {
			File fooDB = new File(name);
			if (!fooDB.exists())
				fooDB.createNewFile();
		} catch (IOException e) {
			throw e;
		}
	}
}

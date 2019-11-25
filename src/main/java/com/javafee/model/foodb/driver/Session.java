package com.javafee.model.foodb.driver;

import java.io.IOException;
import java.util.List;

public interface Session {
	void saveAll(List list) throws IOException;

	List findAll() throws IOException, ClassNotFoundException;
}

package com.javafee.model.foodb.dao;

import java.io.IOException;
import java.util.List;

public interface Dao<T> {
	List<T> findAll() throws IOException, ClassNotFoundException;

	void saveAll(List<T> tList) throws IOException;
}

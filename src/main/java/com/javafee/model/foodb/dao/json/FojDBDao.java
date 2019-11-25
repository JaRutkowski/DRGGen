package com.javafee.model.foodb.dao.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javafee.controller.utils.Constants;
import com.javafee.model.foodb.dao.Dao;
import com.javafee.model.foodb.driver.FooDB;

public class FojDBDao extends FooDB implements Dao {
	public FojDBDao(String name) throws IOException {
		super(name.endsWith(Constants.FOJDB_EXTENSION) ? name : name + Constants.FOJDB_EXTENSION);
	}

	@Override
	public List findAll() throws FileNotFoundException {
		List oList;
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Object o = gson.fromJson(new FileReader(super.name), Object.class);
			oList = o != null ? (List) o : null;
		} catch (IOException e) {
			throw e;
		}
		return oList;
	}

	@Override
	public void saveAll(List ts) throws IOException {
		if (!ts.isEmpty()) {
			try (Writer writer = new FileWriter(super.name)) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				gson.toJson(ts, writer);
			} catch (IOException e) {
				throw e;
			}
		}
	}
}

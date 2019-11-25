package com.javafee.model.foodb.dao.text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.javafee.controller.utils.Constants;
import com.javafee.model.foodb.dao.Dao;
import com.javafee.model.foodb.driver.FooDB;

public class FotDBDao extends FooDB implements Dao {
	public FotDBDao(String name) throws IOException {
		super(name.endsWith(Constants.FOTDB_EXTENSION) ? name : name + Constants.FOTDB_EXTENSION);
	}

	@Override
	public List findAll() {
		return null;
	}

	@Override
	public void saveAll(List list) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(super.name))) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Object o : list)
				stringBuilder.append(o != null ? o.toString() + "\n" : "");
			writer.write(stringBuilder.toString());
		} catch (IOException e) {
			throw e;
		}
	}

	public FotDBDao save(Object object, boolean appendMode) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(super.name), StandardOpenOption.APPEND)) {
			StringBuilder stringBuilder = new StringBuilder(object != null ? object.toString() + "\n" : "");
			if (appendMode)
				writer.write(stringBuilder.toString());
			else
				writer.append(stringBuilder.toString());
		} catch (IOException e) {
			throw e;
		}
		return this;
	}

	public FotDBDao saveAll(List list, boolean appendMode) throws IOException {
		if (appendMode)
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(super.name), StandardOpenOption.APPEND)) {
				StringBuilder stringBuilder = new StringBuilder();
				for (Object o : list)
					stringBuilder.append(o != null ? o.toString() + "\n" : "");
				writer.append(stringBuilder.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			saveAll(list);
		return this;
	}
}

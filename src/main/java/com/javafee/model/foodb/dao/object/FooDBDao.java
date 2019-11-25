package com.javafee.model.foodb.dao.object;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.javafee.controller.utils.Constants;
import com.javafee.model.foodb.dao.Dao;
import com.javafee.model.foodb.driver.FooDB;

public class FooDBDao extends FooDB implements Dao {
	public FooDBDao(String name) throws IOException {
		super(name.endsWith(Constants.FOODB_EXTENSION) ? name : name + Constants.FOODB_EXTENSION);
	}

	@Override
	public List findAll() throws IOException, ClassNotFoundException {
		List oList;
		try (ObjectInputStream dbInput = new ObjectInputStream(new FileInputStream(name))) {
			Object o = dbInput.readObject();
			oList = o != null ? (List) o : null;
		} catch (ClassNotFoundException | IOException e) {
			throw e;
		}
		return oList;
	}

	@Override
	public void saveAll(List ts) throws IOException {
		if (!ts.isEmpty()) {
			try (ObjectOutputStream dbOutput = new ObjectOutputStream(new FileOutputStream(name))) {
				dbOutput.writeObject(ts);
			} catch (IOException e) {
				throw e;
			}
		}
	}
}

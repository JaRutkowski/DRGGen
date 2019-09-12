package com.javafee.controller.algorithm.decisionrules;

import java.util.List;
import java.util.Vector;

public interface DecisionRulesGenerator {
	void setTimeMeasure(long timeMeasure);

	long getTimeMeasure();

	List<List<Object>> generate(Vector<Vector> data);
}

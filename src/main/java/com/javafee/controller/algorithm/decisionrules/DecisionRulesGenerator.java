package com.javafee.controller.algorithm.decisionrules;

import java.util.List;
import java.util.Vector;

public interface DecisionRulesGenerator {
	List<List<Object>> generate(Vector<Vector> data);
}

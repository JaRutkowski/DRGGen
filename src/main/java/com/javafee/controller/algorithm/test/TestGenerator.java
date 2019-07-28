package com.javafee.controller.algorithm.test;

import java.util.List;
import java.util.Vector;

import com.javafee.controller.algorithm.datastructure.RowPairsSet;

public interface TestGenerator {
	List<RowPairsSet> generate(Vector<Vector> data);
}

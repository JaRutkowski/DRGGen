package com.javafee.controller.algorithm.datapreprocessing.inconsistency;

import java.util.Vector;

import com.javafee.controller.algorithm.exception.AlgorithmException;

public interface InconsistencyGenerator {
	Vector<Vector> generate(Vector<Vector> data) throws AlgorithmException;
}

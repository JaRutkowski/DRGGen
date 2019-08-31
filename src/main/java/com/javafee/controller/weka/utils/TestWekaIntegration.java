package com.javafee.controller.weka.utils;

public class TestWekaIntegration {
	public static void main(String[] args) {
		TestWekaIntegrationDataSplitService testWekaIntegrationDataSplitService = new TestWekaIntegrationDataSplitService();
		testWekaIntegrationDataSplitService.trainTestSplit();
	}
}

package com.javafee.controller.utils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import com.javafee.model.JpaUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemProperties {
	/**
	 * Decision attribute index parameter, <code>null</code> as default.
	 */
	@Setter
	@Getter
	private Integer systemParameterDecisionAttributeIndex = null;

	/**
	 * Data type parameter, <code>Constants.APPLICATION_SET_TYPE</code> as default.
	 *
	 * @see Constants.SetType
	 */
	@Setter
	@Getter
	private Constants.SetType systemParameterSetType = Constants.APPLICATION_SET_TYPE;

	/**
	 * Training set double percentage parameter, <code>Constants.APPLICATION_TRAINING_PERCENTAGE</code> as default.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private double systemParameterTrainingPercentage = Constants.APPLICATION_TRAINING_PERCENTAGE;

	/**
	 * Test set double percentage parameter, <code>Constants.APPLICATION_TEST_PERCENTAGE</code> as default.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private double systemParameterTestPercentage = Constants.APPLICATION_TEST_PERCENTAGE;

	/**
	 * Shuffle parameter, <code>Constants.APPLICATION_SHUFFLE</code> as default. Determines if rows are shuffled
	 * before data division to training and test sets process.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterShuffle = Constants.APPLICATION_SHUFFLE;

	/**
	 * Data type - consistency - parameter, <code>Constants.APPLICATION_SET_TYPE</code> as default.
	 *
	 * @see Constants.SetTypeConsistency
	 */
	@Setter
	@Getter
	private Constants.SetTypeConsistency systemParameterSetTypeConsistency = Constants.APPLICATION_SET_TYPE_CONSISTENCY;

	/**
	 * Shuffle parameter, <code>Constants.APPLICATION_SHUFFLE</code> as default. Determines if rows are shuffled
	 * before data division to training and test sets process.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterShowDeclaredSetTypeConsistencyInDataParameterPanel = Constants.APPLICATION_SHOW_SET_TYPE_CONSISTENCY_IN_DATA_PARAMETERS_PANEL;

	/**
	 * Inconsistency generator first attribute or all attributes removal parameter, <code>Constants.APPLICATION_REMOVE_FIRST_ATTRIBUTE</code>
	 * as default. Determines if inconsistency generation algorithm removes only first attribute with maximum occurrence
	 * value or all attributes with the same maximum occurrence value.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterRemoveFirstAttributeInconsistencyGenerator = Constants.APPLICATION_REMOVE_FIRST_ATTRIBUTE;

	/**
	 * Inconsistency generator recursive removal parameter, <code>Constants.APPLICATION_RECURSIVE_REMOVAL_ATTRIBUTE</code>
	 * as default. Determines if inconsistency generation algorithm removes attributes recursively until getting
	 * inconsistency in the data.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterRecursiveRemovalInconsistencyGenerator = Constants.APPLICATION_RECURSIVE_REMOVAL_ATTRIBUTE;

	/**
	 * Inconsistency generation report parameter, <code>Constants.APPLICATION_GENERATE_INCONSISTENCY_GENERATION_REPORT_ATTRIBUTE</code>
	 * as default. Determines if inconsistency generation process is finished with generated report.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterGenerateInconsistencyGenerationReport = Constants.APPLICATION_GENERATE_INCONSISTENCY_GENERATION_REPORT_ATTRIBUTE;

	/**
	 * Decision rules data range list parameter, contains only <code>Constants.DecisionRulesDataRange.DECISION_RULES</code>
	 * as default.
	 *
	 * @see Constants.DecisionRulesDataRange
	 */
	@Setter
	@Getter
	private Set<Constants.DecisionRulesDataRange> systemParameterDecisionRulesDataRangeList = new HashSet<>(Arrays.asList(Constants.DecisionRulesDataRange.DECISION_RULES));

	/**
	 * Calculate quality measure for each decision rules parameter, <code>Constants.APPLICATION_QUALITY_MEASURE_FOR_EACH_ATTRIBUTE</code>
	 * as default.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterCalculateQualityMeasureForEachDecisionRules = Constants.APPLICATION_QUALITY_MEASURE_FOR_EACH_ATTRIBUTE;

	/**
	 * Show decision rules generation time parameter, <code>Constants.APPLICATION_SHOW_DECISION_RULES_GENERATION_TIME</code>
	 * as default.
	 *
	 * @see Constants
	 */
	@Setter
	@Getter
	private boolean systemParameterShowDecRulesGenerationTime = Constants.APPLICATION_SHOW_DECISION_RULES_GENERATION_TIME;

	/**
	 * Resource bundle system parameter. <code>Constants.LANGUAGE_RESOURCE_BUNDLE</code>, language <code>Constants.APPLICATION_LANGUAGE</code>
	 * as default.
	 *
	 * @see Constants
	 */
	@Getter
	private ResourceBundle resourceBundle = ResourceBundle.getBundle(
			Constants.LANGUAGE_RESOURCE_BUNDLE,
			new Locale(Constants.APPLICATION_LANGUAGE));

	public void initializeDataBase(String url, String userName, String password) throws SQLException, ClassNotFoundException {
		JpaUtils.initializeDataBase(url, userName, password);
	}

	public Integer getSystemParameterDecisionAttributeIndex(Integer columnCount) {
		return columnCount - 1;
	}
}

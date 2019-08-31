package com.javafee.forms.mainform.utils;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class DoubleJSpinner extends JSpinner {
	private static final double STEP_RATIO = 0.1;

	private SpinnerNumberModel model;

	public DoubleJSpinner() {
		super();

		model = new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1);
		this.setModel(model);

		this.addChangeListener(e -> {
			Double value = getDouble();
			long magnitude = Math.round(Math.log10(value));
			double stepSize = STEP_RATIO * Math.pow(10, magnitude);
			model.setStepSize(stepSize);
		});
	}

	/**
	 * Returns current <code>Double</code> value of <code>DoubleJSpinner</code>.
	 *
	 * @return {@link Double}
	 */
	public Double getDouble() {
		return (Double) getValue();
	}

}

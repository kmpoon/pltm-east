package org.latlab.data;

import java.util.Iterator;
import java.util.List;

import org.latlab.util.DiscreteVariable;
import org.latlab.util.SingularContinuousVariable;
import org.latlab.util.Variable;

public class Instance {

	public static final double MISSING = Double.NaN;

	private double weight = 1;
	private double[] values;

	public static Instance create(List<Variable> variables,
			List<String> tokens, double weight) {
		double[] values = new double[variables.size()];

		Iterator<Variable> v = variables.iterator();
		Iterator<String> t = tokens.iterator();

		int i = 0;
		while (t.hasNext()) {
			Variable variable = v.next();
			String token = t.next();

			if (token == null) {
				values[i] = MISSING;
			} else if (variable instanceof DiscreteVariable) {
				values[i] = ((DiscreteVariable) variable).indexOf(token);
			} else if (variable instanceof SingularContinuousVariable) {
				values[i] = Double.parseDouble(token);
			}

			i++;
		}

		return new Instance(weight, values);
	}

	public static Instance create(double weight, double[] values) {
		return new Instance(weight, values.clone());
	}

	public Instance(int length) {
		this(1, new double[length]);
	}

	private Instance(double weight, double[] values) {
		this.values = values;
		this.weight = weight;
	}

	public double weight() {
		return weight;
	}

	public double value(int index) {
		return values[index];
	}

	public boolean isMissing(int index) {
		return Double.isNaN(values[index]);
	}

	public boolean hasMissing() {
		for (int i = 0; i < values.length; i++) {
			if (isMissing(i))
				return true;
		}

		return false;
	}
}

package org.latlab.learner.geast;

import org.latlab.learner.geast.operators.SearchCandidate;
import org.latlab.util.Evaluator;

/**
 * Evaluates a search candidate using the BIC score.
 * 
 * @author leonard
 * 
 */
public class BicEvaluator implements Evaluator<SearchCandidate> {

	public double evaluate(SearchCandidate candidate) {
		return candidate.estimation().BicScore();
	}

}

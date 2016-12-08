/**
 * 
 */
package org.latlab.learner.geast.procedures;

import java.util.Arrays;

import org.latlab.learner.geast.IModelWithScore;
import org.latlab.learner.geast.UnitImprovementEvaluator;
import org.latlab.learner.geast.context.IProcedureContext;
import org.latlab.learner.geast.context.ISearchOperatorContext;
import org.latlab.learner.geast.operators.NodeCombiner;
import org.latlab.learner.geast.operators.NodeIntroducer;
import org.latlab.learner.geast.operators.RestrictedNodeCombiner;
import org.latlab.learner.geast.operators.RestrictedNodeRelocator;
import org.latlab.learner.geast.operators.SearchCandidate;
import org.latlab.learner.geast.operators.SearchOperator;
import org.latlab.learner.geast.operators.StateIntroducer;
import org.latlab.util.Evaluator;

/**
 * Improves the base model by expansion.
 * 
 * @author leonard
 * 
 */
public class ExpandProcedure extends IterativeProcedure {

	private final ISearchOperatorContext searchOperatorContext;

	/**
	 * @param context
	 */
	public <T extends ISearchOperatorContext & IProcedureContext> ExpandProcedure(
			T context) {
		super(context, Arrays.asList(new StateIntroducer(context),
				new NodeIntroducer(context), new NodeCombiner(context)));
		searchOperatorContext = context;
	}

	/**
	 * Refines the candidate model by repeatedly improves the model with
	 * restricted adjustment search.
	 */
	@Override
	protected SearchCandidate refine(SearchCandidate candidate, double score,
			Evaluator<SearchCandidate> evaluator) {

		SearchOperator operator = null;
		String name = "";

		if (candidate instanceof NodeIntroducer.Candidate) {
			NodeIntroducer.Candidate c = (NodeIntroducer.Candidate) candidate;
			operator =
					new RestrictedNodeRelocator(searchOperatorContext,
							c.origin, c.introduced);
			name = "RefineNodeIntroductionProcedure";
		} else if (candidate instanceof NodeCombiner.Candidate) {
			NodeCombiner.Candidate c = (NodeCombiner.Candidate) candidate;
			operator =
					new RestrictedNodeCombiner(searchOperatorContext,
							c.parentVariable, c.newVariable);
			name = "RefineNodeCombinationProcedure";
		} else {
			return candidate;
		}

		Procedure procedure =
				new RefinementProcedure(name, context, operator,
						(UnitImprovementEvaluator) evaluator, score);
		return procedure.run(candidate.estimation());
	}

	/**
	 * Returns an evaluator which adjusts the improvement by the increase in
	 * complexity of model.
	 */
	@Override
	protected Evaluator<SearchCandidate> getEvaluator(final IModelWithScore base) {
		return new UnitImprovementEvaluator(base);
	}
}

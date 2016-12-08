package org.latlab.learner.geast;

import java.util.Arrays;

import org.latlab.data.MixedDataSet;
import org.latlab.learner.geast.context.Context;
import org.latlab.learner.geast.context.IProcedureContext;
import org.latlab.learner.geast.context.ISearchOperatorContext;
import org.latlab.learner.geast.operators.NodeIntroducer;
import org.latlab.learner.geast.operators.RestrictedNodeRelocator;
import org.latlab.learner.geast.operators.SearchCandidate;
import org.latlab.learner.geast.operators.SearchOperator;
import org.latlab.learner.geast.operators.StateIntroducer;
import org.latlab.learner.geast.procedures.IterativeProcedure;
import org.latlab.learner.geast.procedures.NodeDeletionProcedure;
import org.latlab.learner.geast.procedures.Procedure;
import org.latlab.learner.geast.procedures.RefinementProcedure;
import org.latlab.learner.geast.procedures.SequentialProcedure;
import org.latlab.learner.geast.procedures.StateDeletionProcedure;
import org.latlab.model.Gltm;
import org.latlab.util.Evaluator;

public class GeastWithoutPouch {
	private final Geast geast;

	private static class ExpandProcedure extends IterativeProcedure {

		private final ISearchOperatorContext searchOperatorContext;

		public <T extends IProcedureContext & ISearchOperatorContext> ExpandProcedure(
				T context) {
			super(context, Arrays.asList(new StateIntroducer(context),
					new NodeIntroducer(context)));
			searchOperatorContext = context;
		}

		/**
		 * Refines the candidate model by repeatedly improves the model with
		 * restricted adjustment search.
		 */
		@Override
		protected SearchCandidate refine(SearchCandidate candidate,
				double score, Evaluator<SearchCandidate> evaluator) {

			SearchOperator operator = null;
			String name = "";

			if (candidate instanceof NodeIntroducer.Candidate) {
				NodeIntroducer.Candidate c =
						(NodeIntroducer.Candidate) candidate;
				operator =
						new RestrictedNodeRelocator(searchOperatorContext,
								c.origin, c.introduced);
				name = "RefineNodeIntroductionProcedure";
			} else {
				return candidate;
			}

			Procedure procedure =
					new RefinementProcedure(name, context, operator,
							(UnitImprovementEvaluator) evaluator, score);
			return procedure.run(candidate.estimation());
		}

		protected Evaluator<SearchCandidate> getEvaluator(IModelWithScore base) {
			return new UnitImprovementEvaluator(base);
		}
	}

	private static class SimplifyProcedure extends SequentialProcedure {

		public <T extends IProcedureContext & ISearchOperatorContext> SimplifyProcedure(
				T context) {
			super(Arrays.asList(new NodeDeletionProcedure(context),
					new StateDeletionProcedure(context)));
		}

	}

	public GeastWithoutPouch(MixedDataSet data, Log log) {
		this(Geast.DEFAULT_THREADS, Geast.DEFAULT_THRESHOLD, data, log,
				new FullEm(data, true, 64, 500, 0.01));
	}

	public GeastWithoutPouch(int threads, double threshold, MixedDataSet data,
			Log log, EmFramework em) {
		Context context =
				new Context(threads, Geast.DEFAULT_SCREENING, threshold, data,
						log, em, em, em);

		Procedure[] procedures =
				new Procedure[] { new ExpandProcedure(context),
						new SimplifyProcedure(context) };

		geast = new Geast(context, procedures);
	}

	public IModelWithScore learn() {
		Gltm initial =
				Gltm.constructLocalIndependenceModel(geast.context().data().getNonClassVariables());
		return geast.learn(initial);
	}

	public void setCommandLine(String commandLine) {
		geast.commandLine = commandLine;
	}

}

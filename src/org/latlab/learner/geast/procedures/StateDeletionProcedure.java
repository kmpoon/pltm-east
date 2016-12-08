package org.latlab.learner.geast.procedures;

import org.latlab.learner.geast.context.IProcedureContext;
import org.latlab.learner.geast.context.ISearchOperatorContext;
import org.latlab.learner.geast.operators.StateDeletor;

public class StateDeletionProcedure extends IterativeProcedure {
	public <T extends ISearchOperatorContext & IProcedureContext> StateDeletionProcedure(
			T context) {
		super(context, new StateDeletor(context));
	}
}

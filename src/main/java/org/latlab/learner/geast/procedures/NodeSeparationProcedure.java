package org.latlab.learner.geast.procedures;

import org.latlab.learner.geast.context.IProcedureContext;
import org.latlab.learner.geast.context.ISearchOperatorContext;
import org.latlab.learner.geast.operators.NodeSeparator;

public class NodeSeparationProcedure extends IterativeProcedure {

	public <T extends ISearchOperatorContext & IProcedureContext> NodeSeparationProcedure(
			T context) {
		super(context, new NodeSeparator(context));
	}

}

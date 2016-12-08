/**
 * 
 */
package org.latlab.learner.geast.procedures;

import org.latlab.learner.geast.context.IProcedureContext;
import org.latlab.learner.geast.context.ISearchOperatorContext;
import org.latlab.learner.geast.operators.NodeRelocator;

/**
 * @author leonard
 * 
 */
public class AdjustProcedure extends IterativeProcedure {

	/**
	 * @param context
	 */
	public <T extends IProcedureContext & ISearchOperatorContext> AdjustProcedure(
			T context) {
		super(context, new NodeRelocator(context));
	}
}

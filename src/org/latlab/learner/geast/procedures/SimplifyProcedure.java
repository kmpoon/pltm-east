/**
 * 
 */
package org.latlab.learner.geast.procedures;

import java.util.Arrays;

import org.latlab.learner.geast.context.IProcedureContext;
import org.latlab.learner.geast.context.ISearchOperatorContext;

/**
 * @author leonard
 * 
 */
public class SimplifyProcedure extends SequentialProcedure {

	/**
	 * @param context
	 */
	public <T extends ISearchOperatorContext & IProcedureContext> SimplifyProcedure(
			T context) {
		super(Arrays.asList(new NodeSeparationProcedure(context),
				new NodeDeletionProcedure(context), new StateDeletionProcedure(
						context)));
	}
}

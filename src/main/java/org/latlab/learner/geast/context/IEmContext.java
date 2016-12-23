package org.latlab.learner.geast.context;

import java.util.concurrent.Executor;

import org.latlab.learner.geast.EmFramework;

public interface IEmContext {

	public EmFramework screeningEm();

	public EmFramework selectionEm();

	public Executor searchExecutor();

}

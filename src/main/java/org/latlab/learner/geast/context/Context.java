package org.latlab.learner.geast.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.latlab.data.MixedDataSet;
import org.latlab.learner.geast.CovarianceConstrainer;
import org.latlab.learner.geast.EmFramework;
import org.latlab.learner.geast.Estimation;
import org.latlab.learner.geast.Log;
import org.latlab.learner.geast.ParameterGenerator;
import org.latlab.learner.geast.procedures.IterativeProcedure;

/**
 * Holds the context, including the parameters and the strategy objects, of this
 * algorithm.
 * 
 * @author leonard
 * 
 */
public class Context implements ISearchOperatorContext, IProcedureContext {
	private final MixedDataSet data;
	private final EmFramework screeningEm;
	private final EmFramework selectionEm;
	private final EmFramework estimationEm;
	private final ParameterGenerator parameterGenerator;
	private final Log log;
	public final int threads;
	private final ExecutorService executorService;
	private final int screeningSize;

	/**
	 * Stops if the BIC does not improve by this threshold.
	 */
	private final double threshold;

	public Context(int threads, int screening, double threshold,
			MixedDataSet data, Log log, EmFramework screeningEm,
			EmFramework selectionEm, EmFramework estimationEm) {
		this.threads = threads;
		this.screeningSize = screening;
		this.threshold = threshold;
		this.data = data;
		this.log = log;
		this.screeningEm = screeningEm;
		this.selectionEm = selectionEm;
		this.estimationEm = estimationEm;

		executorService = Executors.newFixedThreadPool(this.threads);

		this.screeningEm.setMultithreading(this.threads, executorService);
		this.selectionEm.setMultithreading(this.threads, executorService);
		this.estimationEm.setMultithreading(this.threads, executorService);

		parameterGenerator = new ParameterGenerator(data);
	}

	/**
	 * Returns the data the GEAST algorithm is running on.
	 * 
	 * @return data GEAST algorithm is running on
	 */
	public MixedDataSet data() {
		return data;
	}

	/**
	 * The candidate search algorithm stops if the current step fails to have an
	 * improvement larger than this threshold.
	 * 
	 * @return threshold for stopping candidate search
	 * 
	 * @see IterativeProcedure#run(Estimation)
	 */
	public double threshold() {
		return threshold;
	}

	/**
	 * Returns the EM algorithm used for screening candidates.
	 * 
	 * @return
	 */
	public EmFramework screeningEm() {
		return screeningEm;
	}

	/**
	 * Returns the EM algorithm used for selecting the best candidate from a
	 * search operator.
	 * 
	 * @return
	 */
	public EmFramework selectionEm() {
		return selectionEm;
	}

	/**
	 * Returns the EM algorithm used for estimating the parameters of a model.
	 * This is used for estimating the parameters of the best model found in a
	 * search step, and those of the model returned from the whole search.
	 * 
	 * @return
	 */
	public EmFramework estimationEm() {
		return estimationEm;
	}

	/**
	 * Returns the constraint on the covariance matrix used to avoid spurious
	 * local maxima. It assumes that all the EM procedures use the same
	 * constraints.
	 * 
	 * @return
	 */
	public CovarianceConstrainer covarianceConstrainer() {
		return selectionEm().constrainer();
	}

	/**
	 * The number of best candidates kept in the screening stage
	 * 
	 * @return
	 */
	public int screeningSize() {
		return screeningSize;
	}

	/**
	 * Used to generate parameters.
	 * 
	 * @return
	 */
	public ParameterGenerator parameterGenerator() {
		return parameterGenerator;
	}

	/**
	 * Used to log the progress of the search.
	 * 
	 * @return log
	 */
	public Log log() {
		return log;
	}

	/**
	 * Returns the thread executor.
	 * 
	 * @return thread executor
	 */
	public ExecutorService executor() {
		return executorService;
	}
	
	/**
	 * Returns the executor for the search operators. 
	 * @return {@code null}
	 */
	public ExecutorService searchExecutor() {
		return null;
	}

}

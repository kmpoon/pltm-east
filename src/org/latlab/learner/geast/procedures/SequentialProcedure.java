package org.latlab.learner.geast.procedures;

import java.util.List;

import org.latlab.learner.geast.IModelWithScore;
import org.latlab.learner.geast.operators.GivenCandidate;
import org.latlab.learner.geast.operators.SearchCandidate;

public class SequentialProcedure implements Procedure {

    private final List<? extends Procedure> procedures;
    private boolean succeeded = true;

    public SequentialProcedure(List<? extends Procedure> procedures) {
        this.procedures = procedures;
    }

    public void reset() {
        succeeded = true;
    }

    public SearchCandidate run(IModelWithScore base) {
        succeeded = false;
        SearchCandidate best = new GivenCandidate(base);
        
        for (Procedure procedure : procedures) {
            best = procedure.run(best.estimation());
            succeeded |= procedure.succeeded();
        }
        
        return best;
    }

    public boolean succeeded() {
        return succeeded;
    }

    public String name() {
        return getClass().getSimpleName();
    }

}

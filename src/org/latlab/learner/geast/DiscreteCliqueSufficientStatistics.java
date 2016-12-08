package org.latlab.learner.geast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.latlab.util.DiscreteVariable;
import org.latlab.util.Function;
import org.latlab.util.HashCodeGenerator;
import org.latlab.util.Potential;

public class DiscreteCliqueSufficientStatistics implements SufficientStatistics {

    private Function statistics;

    private final double smoothing;

    public DiscreteCliqueSufficientStatistics(
        List<DiscreteVariable> variables, double smoothing) {
        this.smoothing = smoothing;

        statistics = Function.createFunction(variables);
        statistics.fill(smoothing);
    }

    public void reset() {
        statistics.fill(smoothing);
    }

    public void add(Potential potential, double weight) {
        add(potential.function(), weight);
    }

    public void add(Function potential, double weight) {
        statistics.plusMult(potential, weight);
    }

    public Function computePotential(
        DiscreteVariable variable, Collection<DiscreteVariable> parents) {
        Function potential = statistics.clone();

        // sum out any irrelevant variables
        for (DiscreteVariable v : new ArrayList<DiscreteVariable>(potential
            .getVariables())) {
            if (v != variable && !parents.contains(v)) {
                potential = potential.sumOut(v);
            }
        }

        potential.normalize(variable);
        return potential;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof DiscreteCliqueSufficientStatistics))
            return false;

        DiscreteCliqueSufficientStatistics other =
            (DiscreteCliqueSufficientStatistics) o;
        
        return statistics.equals(other.statistics);
    }
    
    @Override
    public int hashCode() {
        HashCodeGenerator generator = new HashCodeGenerator();
        generator.addField(statistics);
        return generator.current();
    }
    
    /**
     * Exposed the field for testing only.
     */
    Function statistics() {
        return statistics;
    }
}

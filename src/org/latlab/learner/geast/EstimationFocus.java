package org.latlab.learner.geast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.latlab.util.Variable;

public class EstimationFocus {
    /**
     * Maps the variables to its parent, if exists
     */
    private final Map<Variable, Variable> map =
        new HashMap<Variable, Variable>();

    public boolean contains(Variable variable) {
        return map.containsKey(variable);
    }

    public Set<Variable> variables() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public Variable parentOf(Variable variable) {
        return map.get(variable);
    }

    public void add(Variable variable, Variable parent) {
        map.put(variable, parent);
    }
}

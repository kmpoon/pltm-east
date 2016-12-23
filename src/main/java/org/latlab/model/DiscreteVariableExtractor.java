package org.latlab.model;

import org.latlab.util.Converter;
import org.latlab.util.DiscreteVariable;

/**
 * Extracts the variable from a belief node.
 * 
 * @author leonard
 * 
 */
public class DiscreteVariableExtractor
    implements Converter<DiscreteBeliefNode, DiscreteVariable> {

    /**
     * Converts a belief node to its variable.
     * 
     * @param node
     *            node to convert from
     * @return variable of the belief node
     */
    public DiscreteVariable convert(DiscreteBeliefNode node) {
        return node.getVariable();
    }

}

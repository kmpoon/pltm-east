package org.latlab.model;

import java.util.Collection;

import org.latlab.util.DiscreteVariable;
import org.latlab.util.JointContinuousVariable;
import org.latlab.util.Potential;
import org.latlab.util.SingularContinuousVariable;

/**
 * A belief node corresponding to continuous variable. It can be a composite
 * node, which represents a multidimensional continuous variable.
 * 
 * <p>
 * It holds a modifiable version of the joint variable. However, this member can
 * only be accessed outside this class through its superclass {@code
 * JointContinuousVariable}, so that only this node can modify the content of
 * the joint variable.
 * 
 * @author leonard
 * 
 */
public class ContinuousBeliefNode extends BeliefNode {

    /**
     * Joint variable held by this node.
     */
    private JointContinuousVariable joint;

    /**
     * Potential representing the conditional probability distribution.
     */
    private CGPotential potential;

    /**
     * Constructs a continuous belief node.
     * 
     * @param network
     *            Bayesian network containing this node
     * @param variable
     *            a single dimensional continuous variable for this node
     */
    public ContinuousBeliefNode(
        BayesNet network, SingularContinuousVariable variable) {
        this(network, new JointContinuousVariable(variable));
    }

    /**
     * Constructs a continuous belief node.
     * 
     * @param network
     *            Bayesian network containing this node
     * @param variable
     *            a single dimensional continuous variable for this node
     */
    public ContinuousBeliefNode(
        BayesNet network, Collection<SingularContinuousVariable> variables) {
        this(network, new JointContinuousVariable(variables));
    }

    public ContinuousBeliefNode(
        BayesNet network, JointContinuousVariable variable) {
        super(network, variable);

        joint = variable;
        potential = new CGPotential(joint, null);
    }

    /**
     * Returns the number of free parameters related to this node.
     * 
     * <p>
     * This is not the number of singular variables in the joint variables.
     */
    @Override
    public int computeDimension() {
        int numberOfVariable = joint.variables().size();
        int dimensionPerConfig = numberOfVariable * (numberOfVariable + 3) / 2;
        return dimensionPerConfig
            * DiscreteVariable.getCardinality(getDiscreteParentVariables());
    }

    /**
     * @see org.latlab.model.BeliefNode#getVariable()
     */
    @Override
    public JointContinuousVariable getVariable() {
        return joint;
    }

    /**
     * @see BeliefNode#potential()
     */
    @Override
    public CGPotential potential() {
        return potential;
    }

    /*
     * (non-Javadoc)
     * @see org.latlab.model.BeliefNode#setPotential(org.latlab.util.Potential)
     */
    @Override
    public void setPotential(Potential potential) {
        setPotential((CGPotential) potential);
    }

    public void setPotential(CGPotential potential) {
        this.potential = potential;
        expireNetworkLogLikelihoods();
    }

//    /**
//     * Adds a (possibly multidimensional) continuous variable to this node. The
//     * name of this node will be changed.
//     * 
//     * <p>
//     * Note: This function does not update the maps in containing Bayesian
//     * network.
//     * 
//     * @param variable
//     *            variable to be added
//     */
//    public void addVariable(ContinuousVariable variable) {
//        joint.add(variable.variables());
//        potential.addHeadVariable(variable.variables());
//
//        setName(joint.getName());
//        expireNetworkLogLikelihoods();
//    }

//    /**
//     * Removes a (possibly multidimensional) continuous variable to this node.
//     * The name of this node will be changed.
//     * 
//     * <p>
//     * The dimension of the joint variable should be at least one.
//     * 
//     * <p>
//     * Note: This function does not update the maps in containing Bayesian
//     * network.
//     * 
//     * @param variable
//     *            variable to be added
//     */
//    public void removeVariables(Collection<SingularContinuousVariable> variables) {
//        joint.remove(variables);
//        potential.removeHeadVariable(variables);
//
//        setName(joint.getName());
//        expireNetworkLogLikelihoods();
//    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}

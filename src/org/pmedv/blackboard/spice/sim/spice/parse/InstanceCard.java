/*
 * InstanceCard.java
 *
 * Created on Oct 14, 2007, 1:55:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;

/**
 *
 * @author Kristopher T. Beck
 */
public abstract class InstanceCard extends Card {

    protected String instName;
    protected String modelName;
    protected int nodeCount;
    protected String[] nodes;
    protected String prefix;

    public InstanceCard() {
    }

    public InstanceCard(String cardString) throws ParserException {
        super(cardString);
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public String[] getNodes() {
        if (nodes == null) {
            nodes = new String[nodeCount];
            initNodes();
        }
        return nodes;
    }

    public String getPrefix() {
        return prefix;
    }

    public abstract Instance createInstance(Deck deck);

    public abstract void parse(String cardString) throws ParserException;

    protected abstract void initNodes();
}

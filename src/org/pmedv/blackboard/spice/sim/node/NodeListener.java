/*
 * NodeListener.java
 *
 * Created on Jun 1, 2007, 10:39:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.node;

/**
 *
 * @author Kristopher T. Beck
 */
public interface NodeListener {
    public void outputUpdated(NodeEvent evt);
}

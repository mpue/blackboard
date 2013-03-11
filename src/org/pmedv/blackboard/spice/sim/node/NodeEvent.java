/*
 * NodeEvent.java
 * 
 * Created on Jun 1, 2007, 10:43:06 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.node;

import java.util.EventObject;

/**
 *
 * @author Kristopher T. Beck
 */
public class NodeEvent extends EventObject{

    /**
     * 
     * @param node 
     */
    public NodeEvent(Node node) {
        super(node);
    }

}

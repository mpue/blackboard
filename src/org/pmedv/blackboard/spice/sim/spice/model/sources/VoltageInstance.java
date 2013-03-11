/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.sources;

/**
 *
 * @author owenbad
 */
public interface VoltageInstance extends SourceInstance {

    public int findBranch(String name);
}

/*
 * Count.java
 *
 * Created on Jul 29, 2007, 8:22:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.output;

/**
 *
 * @author Kristopher T. Beck
 */
public class Count {
    int count;
    
    public Count() {
    }
    
    public int increment(){
        return ++count;
    }
    
    public int decrement(){
        return --count;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
}

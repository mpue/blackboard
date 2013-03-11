/*
 * Varable.java
 *
 * Created on January 29, 2007, 5:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.output;

import javolution.context.ObjectFactory;
import javolution.lang.Realtime;
import javolution.text.Text;
import ktb.math.numbers.Numeric;
/**
 *
 * @author Kristopher T. Beck
 */
public class Variable implements Realtime{
    public static final ObjectFactory<Variable> FACTORY = new ObjectFactory<Variable>(){
        protected Variable create(){
            return new Variable();
        }
    };
    protected Numeric value;
    protected String name;
    /** Creates a new instance of Varable */
    public Variable() {
    }
    
    public static Variable valueOf(String name, Numeric value){
        Variable v = FACTORY.object();
        v.name = name;
        v.value = value;
        return v;
    }

    public Text toText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

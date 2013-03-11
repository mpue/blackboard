/*
 * Value.java
 *
 * Created on June 26, 2006, 2:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim;

import javolution.context.ObjectFactory;
import javolution.lang.Realtime;
import javolution.text.Text;
import ktb.math.numbers.Complex;
import ktb.math.numbers.Real;
/**
 *
 * @author Kristopher T. Beck
 */
public class Value implements Realtime{
    public static final ObjectFactory<Value> FACTORY = new ObjectFactory<Value>(){
        protected Value create(){
            return new Value();
        }

    };
    Object value;
    /** Creates a new instance of Value */
    public Value() {
    }
    
    public Value valueOf(Object obj){
        Value v = FACTORY.object();
        value = String.valueOf(obj);
        return v;
    }
    
    public Value valueOf(double real){
        Value v = FACTORY.object();
        value = String.valueOf(real);
        return v;
    }
    
    public Value valueOf(float real){
        Value v = FACTORY.object();
        value = String.valueOf(real);
        return v;
    }
    
    public Value valueOf(int integer){
        Value v = FACTORY.object();
        value = String.valueOf(integer);
        return v;
    }
    
    public Value valueOf(long longint){
        Value v = FACTORY.object();
        value = String.valueOf(longint);
        return v;
    }
    
    public Value valueOf(Real real){
        Value v = FACTORY.object();
        value = String.valueOf(real);
        return v;
    }
    
    public Value valueOf(Complex cmplx){
        Value v = FACTORY.object();
        value = String.valueOf(cmplx);
        return v;
    }
    
    public Value valueOf(String str){
        Value v = FACTORY.object();
        value = String.valueOf(str);
        return v;
    }
    
    public Object getObject(){
        return value;
    }
    
    public String getString(){
        return value.toString();
    }

    public Text toText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

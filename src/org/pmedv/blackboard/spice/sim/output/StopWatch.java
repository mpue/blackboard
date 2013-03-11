/*
 * StopWatch.java
 *
 * Created on June 1, 2006, 6:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.output;

import javolution.context.ObjectFactory;
import javolution.lang.Realtime;
import javolution.text.Text;

/**
 *
 * @author Kristopher T. Beck
 */
public class StopWatch implements Realtime{
public static final ObjectFactory<StopWatch> FACTORY = new ObjectFactory<StopWatch> (){
    protected StopWatch create (){
        return new StopWatch();
    }
};
    boolean running;
    long startTime;
    long totalTime;

    public static StopWatch newInstance(){
        return FACTORY.object();
    }
    
    public double getNano() {
        return nano;
    }

    public boolean isRunning() {
        return running;
    }

    public void setResultTime(long resultTime) {
        this.totalTime = resultTime;
    }

    public long getResultTime() {
        return totalTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public double getTime(){
        if(isRunning())
        return ((System.nanoTime() - startTime) + totalTime) * nano;
        return totalTime * nano;
    }
    
    double nano = 1e-9;
    
    public double start(){
        running = true;
        startTime = System.nanoTime();
        return totalTime * nano;
    }
    
    public double stop(){
        totalTime = (System.nanoTime() - startTime) + totalTime;
        running = false;
        return totalTime * nano;
    }
        
    public void reset(){
        totalTime = 0;
        startTime = 0;
        running = false;
    }
        
    /** Creates a new instance of StopWatch */
    public StopWatch() {
    }

    public Text toText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

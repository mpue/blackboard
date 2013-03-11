/*
 * Statistics.java
 *
 * Created on March 29, 2006, 3:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.output;

import javolution.util.FastMap;
import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class Statistics {

    static FastMap<String, Count> counts = new FastMap<String, Count>();
    static FastMap<String, StopWatch> timers = new FastMap<String, StopWatch>();
    int opAlternations;    /* Total alternations between event and analog */

    int opLoadCalls;      /* Total load calls in DCOP analysis */

    int opEventPasses;    /* Total passes through event iteration loop */

    int tranLoadCalls;    /* Total inst calls in transient analysis */

    int tranTimeBackups;  /* Number of transient timestep cuts */

    int STATETYPES = 2;
    long lteTime;
    int numIters = STATETYPES;
    int state = 0;

    /**
     * Creates a new instance of Statistics
     */
    public Statistics() {
    }

    public static Count addCount(String id) {
        Count c = new Count();
        counts.put(id, c);
        return c;
    }

    public static Count removeCount(String id) {
        return counts.remove(id);
    }

    public static int increment(String tag) {
        return counts.get(tag).increment();
    }

    public static int decrement(String tag) {
        return counts.get(tag).decrement();
    }

    public int getCount(String tag) {
        return counts.get(tag).getCount();
    }

    public static StopWatch addTime(String id) {
        StopWatch sw = StopWatch.newInstance();
        timers.put(id, sw);
        return sw;
    }

    public static StopWatch removeTime(String id) {
        return timers.remove(id);
    }

    public static void start(String op) {
        if (!timers.containsKey(op)) {
            timers.put(op, StopWatch.newInstance());
        }
        timers.get(op).start();
    }

    public static double stop(String id) {
        if (!timers.containsKey(id)) {
            StandardLog.warning(id + " has not been started.");
            return 0;
        }
        return timers.get(id).stop();
    }

    public void stopAll() {
        StopWatch watch = null;
        for (FastMap.Entry<String, StopWatch> n = timers.head();
                n != timers.tail(); watch = n.getNext().getValue()) {
            if (watch.isRunning()) {
                watch.stop();
            }
        }
    }

    public double getTime(String tag) {
        if (!timers.containsKey(tag)) {
            StandardLog.warning(tag + " not started.");
            return 0;
        }
        return timers.get(tag).getTime();
    }
}

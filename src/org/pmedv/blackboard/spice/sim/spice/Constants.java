/*
 * Constants.java
 *
 * Created on January 31, 2007, 4:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice;

/**
 *
 * @author Kristopher T. Beck
 */
public class Constants {

    public static enum TYPE {

        CURRENT, TIME, FREQUENCY, TEMP, RES, VOLTAGE
    };

    public static enum DATA {

        BOOLEAN, INTEGER, REAL, COMPLEX, STRING
    };

    public static enum INOUT_DIRECTION {

        IN, OUT, INOUT
    };

    public static enum INOUT_TYPE {

        ANALOG, DIGITAL, HYBRIDE
    };

    public static enum SCALE {

        LIN, LOG
    };

    public static enum UID {

        ANALYSIS, TASK, DEVICE, MODEL, NODE, SIGNAL, OTHER
    };
    /* epsilon zero F/m */
    public static final double EPS_ZERO = 8.854214871e-12;
    /* epsilon SiO2 F/m */
    public static final double EPS_SiO2 = 3.4531479969e-11;
    /* MuZero H/m       */
    public static final double muZero = 1.25663706143592e-6;
    public static final double CtoK = 273.15;
    /* 27 degrees C */
    public static final double REF_TEMP = 300.15;
    public static final double CHARGE = 1.602191770e-19;
    /* J/K */
    public static final double BOLTZMANN = 1.38062259e-23;
    public static final double VELOSITY_LIGHT = 2.997924562e8;
    public static final double ELECTRON_MASS = 9.10955854e-31;
    public static final double ZERO_CELSIUS = 273.15;
    public static final double EPS_REL_SI = 11.7;
    public static final double EPS_SI = EPS_ZERO * EPS_REL_SI;
    public static final double EPS_GAAS = EPS_ZERO * 12.244;
    public static final double EPS_REL_GA = 10.9;
    public static final double EPS_GA = EPS_ZERO * EPS_REL_GA;
    public static final double EPS_REL_OX = 3.9;
    public static final double EPS_REL_NI = 7.5;
    public static final double EPS_NI = EPS_ZERO * EPS_REL_NI;
    public static final double ROOT2 = Math.sqrt(2.0);
    public static final double VT0 = BOLTZMANN * (27 + ZERO_CELSIUS) / CHARGE;
    public static final double KoverQ = BOLTZMANN / CHARGE;
    public static final double EXP_THRESHOLD = 34.0;
    //  CONSTe = exp((double)1.0);
}

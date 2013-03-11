/*
 * EnumConsts.java
 *
 * Created on May 3, 2006, 7:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim.spice;

/**
 *
 * @author Kristopher T. Beck
 */
public class EnumConsts {
    public static enum MODE{AC, TRAN, DC, DCOP, TRANOP, DCTRANCURVE, INIT_FLOAT,
    INIT_JCT, INIT_FIX, INIT_SMSIG, INIT_TRAN, INIT_PRED};
        
    public static enum KEY{INIT, CASE1, CASE2, CASE3, CASE4, CASE5, CASE6,
    CASE7, CASE8, CASE9, CASE10, CASE11, CASE12, NEXT, NEXT1, NEXT2, FINAL,
    RESUME, LOAD, DONE, BYPASS};
    
    
    //public static enum NDATA{LNLSTDENS, INNOIZ, OUTNOIZ, NSTATVARS};
    
    public static final int N_TYPE = 1;
    public static final int P_TYPE = -1;
    /*    public static enum MOSTYPE{NMOS, PMOS};
    public static final int NMOS = 1;
    public static final int PMOS = -1;
    public static final int PNP = -1;
    public static final int NPN = 1;
    public static final int PJC = -1;
    public static final int NJC = 1;
    public static final int NHFET = 1;
    public static final int PHFET = -1;
 **/
    public static final int VERTICAL = 1;
    public static final int LATERAL = -1;
    /*public static enum NZ_DATA{LNLSTDENS, OUTNOIZ, INNOIZ, STATVARS};
  
    public static enum NZ_STATES{OUTPUT, OUTREF, INPUT, START, STOP, STEPS,
    PTSPERSUM, DEC, OCT, LIN};
    
    public static enum NZ_MODE{DENS, INT_NOIZ, SHOTNOISE, THERMNOISE, GAIN};
    
    public static enum NZ_OPER{OPEN, CALC, CLOSE};
    */
        
    public static enum DOMAIN{NONE, TIME, FREQUENCY, SWEEP};
        
    public static enum ANAL{DC, AC, TRAN, /*DCTRANCURVE, DISTOR, NOISE, SEN,*/ NONE};
    
    public static enum STAT{SETUP, START, DC, TRAN, AC, STOP};
        
    public static enum INTEG{TRAPEZOIDAL, BDF, GEAR};
    /*
    public static enum CALLTYPE{ANALOG, DIGITAL, HYBRIDE};
    
    public static enum PORTTYPE{VOLTAGE, DIFF_VOLTAGE, CURRENT, DIFF_CURRENT,
    VSOURCE_CURRENT, CONDUCTANCE, DIFF_CONDUCTANCE, RESISTANCE, DIFF_RESISTANCE,
    DIGITAL, USER_DEFINED};
    
    public static enum DISTOR {STEPS, F2OVRF1, SETUP,
    ONE, F1, F2, TWOF1, THRF1, F1PF2, F1MF2, TWOF1MF2, RHSF1, RHSF2};
    
    public static enum ENHTYPE{ANALOG_NODE, EVENT_NODE, ANALOG_BRANCH,
    ANALOG_INSTANCE, EVENT_INSTANCE, HYBRID_INSTANCE};
    */
    public static enum STEP{DC, LINEAR, DECADE, OCTAVE};
    
    /*
    public static enum SENS{POS, NEG, SRC, NAME, START, STOP, STEPS,
    DC, DEFTOL, DEFPERT, DEFPERTURB, DEVDEFTOL,
    DEVDEFPERT, TYPE, DEVICE, PARAM, TOL, PERT};
    */
    //public static enum IODIRECT{IN, OUT, INOUT, NC};
       /*
    public static enum DEVICE{
        RESISTOR/*{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        CAPASITOR/*{
            public Device createModel(){
                return Capasitor.newInstance();
            }
        },
        INDUCTOR/*{
            public Device createModel(){
                return Inductor.newInstance();
            }
        },
        VSRC/*{
            public Device createModel(){
                return VSRC.newInstance();
            }
        },
        ISRC/*{
            public Device createModel(){
                return ISRC.newInstance();
            }
        };
        VCVS{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
   VCIS{
            public Device createModel(){
                return VCIS.newInstance();
            }
        },
       ICVS{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
       ICIS{
            public Device createModel(){
                return ICIS.newInstance();
            }
        };
        /*,
        TRANSFORMER{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        DIODE{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        TRANSISTOR{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        SCR{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        MOSFET{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        ZENER{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        SHOTTSKEYBARRIER{
            public Device createModel(){
                return Resistor.newInstance();
            }
        },
        XDEVICE{
            public Device createModel(){
                return Resistor.newInstance();
            
        };
              public abstract Device createModel();
    };*/

}

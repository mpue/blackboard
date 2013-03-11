/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mos;

import javolution.lang.MathLib;
import ktb.math.Bool;
import ktb.math.numbers.Real;

/**
 *
 * @author Kristopher T. Beck
 */
public class MOSUtils {

    
    public static double vdsLimit(double vNew, double vOld) {
        if (vOld >= 3.5) {
            if (vNew > vOld) {
                vNew = MathLib.min(vNew, (3 * vOld) + 2);
            } else {
                if (vNew < 3.5) {
                    vNew = MathLib.max(vNew, 2);
                }
            }
        } else {
            if (vNew > vOld) {
                vNew = MathLib.min(vNew, 4);
            } else {
                vNew = MathLib.max(vNew, -.5);
            }
        }
        return vNew;
    }

    public static double pnjLimit(double vNew, double vOld, double vt,
            double vCrit, Bool iCheck) {
        double arg;

        if ((vNew > vCrit) && (MathLib.abs(vNew - vOld) > (vt + vt))) {
            if (vOld > 0) {
                arg = (vNew - vOld) / vt;
                if (arg > 0) {
                    vNew = vOld + vt * (2 + MathLib.log(arg - 2));
                } else {
                    vNew = vOld - vt * (2 + MathLib.log(2 - arg));
                }
            } else {
                vNew = vt * MathLib.log(vNew / vt);
            }
            iCheck.setTrue();
        } else {
            if (vNew < 0) {
                if (vOld > 0) {
                    arg = -1 * vOld - 1;
                } else {
                    arg = 2 * vOld - 1;
                }
                if (vNew < arg) {
                    vNew = arg;
                    iCheck.setTrue();
                } else {
                    iCheck.setFalse();
                }
            } else {
                iCheck.setFalse();
            }
        }
        return vNew;
    }

    public static double fetLimit(double vNew, double vOld, double vto) {
        double vtsthi = MathLib.abs(2 * (vOld - vto)) + 2;
        double vtstlo = MathLib.abs(vOld - vto) + 1;
        double vtox = vto + 3.5;
        double delv = vNew - vOld;
        double vtemp;
        if (vOld >= vto) {
            if (vOld >= vtox) {
                if (delv <= 0) {
                    /* going off */
                    if (vNew >= vtox) {
                        if (-delv > vtstlo) {
                            vNew = vOld - vtstlo;
                        }
                    } else {
                        vNew = MathLib.max(vNew, vto + 2);
                    }
                } else {
                    /* staying on */
                    if (delv >= vtsthi) {
                        vNew = vOld + vtsthi;
                    }
                }
            } else {
                // Middle region
                if (delv <= 0) {
                    // Decreasing
                    vNew = MathLib.max(vNew, vto - 0.5);
                } else {
                    // Increasing
                    vNew = MathLib.min(vNew, vto + 4);
                }
            }
        } else {
            // Off
            if (delv <= 0) {
                if (-delv > vtsthi) {
                    vNew = vOld - vtsthi;
                }
            } else {
                vtemp = vto + 0.5;
                if (vNew <= vtemp) {
                    if (delv > vtstlo) {
                        vNew = vOld + vtstlo;
                    }
                } else {
                    vNew = vtemp;
                }
            }
        }
        return vNew;
    }

    public static void qMeyer(double vgs, double vgd, double vgb, double von,
            double vdsat, Real capgs, Real capgd, Real capgb,
            double phi, double cox) {
        double vds;
        double vddif;
        double vddif1;
        double vddif2;

        double VDS = 0.025;

        double vgst = vgs - von;
        vdsat = MathLib.max(vdsat, VDS);
        if (vgst <= -phi) {
            capgb.set(cox / 2);
            capgs.set(0);
            capgd.set(0);
        } else if (vgst <= -phi / 2) {
            capgb.set(-vgst * cox / (2 * phi));
            capgs.set(0);
            capgd.set(0);
        } else if (vgst <= 0) {
            capgb.set(-vgst * cox / (2 * phi));
            capgs.set(vgst * cox / (1.5 * phi) + cox / 3);
            vds = vgs - vgd;
            if (vds >= vdsat) {
                capgd.set(0);
            } else {
                vddif = 2.0 * vdsat - vds;
                vddif1 = vdsat - vds;
                vddif2 = vddif * vddif;
                capgd.set(capgs.get() * (1.0 - vdsat * vdsat / vddif2));
                capgs.set(capgs.get() * (1.0 - vddif1 * vddif1 / vddif2));
            }
        } else {
            vds = vgs - vgd;
            vdsat = MathLib.max(vdsat, VDS);
            if (vdsat <= vds) {
                capgs.set(cox / 3);
                capgd.set(0);
                capgb.set(0);
            } else {
                vddif = 2.0 * vdsat - vds;
                vddif1 = vdsat - vds;
                vddif2 = vddif * vddif;
                capgd.set(cox * (1.0 - vdsat * vdsat / vddif2) / 3);
                capgs.set(cox * (1.0 - vddif1 * vddif1 / vddif2) / 3);
                capgb.set(0);
            }
        }
    }
}

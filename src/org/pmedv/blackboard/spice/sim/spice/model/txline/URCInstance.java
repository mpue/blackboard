/*
 * urc.java
 *
 * Created on August 28, 2006, 2:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.txline;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.spice.Circuit;
import org.pmedv.blackboard.spice.sim.spice.Mode;
import org.pmedv.blackboard.spice.sim.spice.WorkEnv;
import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.discrete.CapInstance;
import org.pmedv.blackboard.spice.sim.spice.model.discrete.ResInstance;
import org.pmedv.blackboard.spice.sim.spice.model.semi.DiodeInstance;

import javolution.lang.MathLib;
import javolution.util.FastSet;
import javolution.util.StandardLog;

/**
 *
 * @author Kristopher T. Beck
 */
public class URCInstance extends URCModelValues implements Instance {

    private Circuit ckt;
    private WorkEnv wrk;
    //FastList<Instance> modNames = FastList.newInstance();
    /** Creates a new instance of urc */
    public URCInstance() {
    }

    public boolean init(Circuit ckt) {
        this.ckt = ckt;
        wrk = ckt.getWrk();
        Node lowr;
        Node hil;
        String _name;
        String namehi;
        Node nodehi;
        Node nodelo;
        String namelo;
        double r;
        double c;
        double wnorm;
        int i;
        double p = k;
        double r0 = length * rPerL;
        double c0 = length * cPerL;
        double i0 = length * iSatPerL;
        if (lumps == 0) {
            wnorm = fmax * r0 * c0 * 2.0 * MathLib.PI;
            lumps = (int) MathLib.max(3.0, MathLib.log(wnorm * (((p - 1) / p) * ((p - 1) / p))) / MathLib.log(p));
            if (wnorm < 35) {
                lumps = 3;
            }
            if (lumps > 100) {
                lumps = 100;
            }
        }
        double r1 = (r0 * (p - 1)) / ((2 * (MathLib.pow(p, (double) lumps))) - 2);
        double c1 = (c0 * (p - 1)) / ((MathLib.pow(p, (double) (lumps - 1))) * (p + 1) - 2);
        double i1 = (i0 * (p - 1)) / ((MathLib.pow(p, (double) (lumps - 1))) * (p + 1) - 2);
        double rd = length * lumps * rsPerL;
        if (c1 <= 0) {
            StandardLog.error("c1 < 0");
        }
        double prop = 1;
        String dModName = getInstName() + ":diodemod";
        String cModName = getInstName() + ":capmod";
        String rModName = getInstName() + ":resmod";
        Node lowl = ckt.getNode(posIndex);
        Node hir = ckt.getNode(negIndex);
        for (i = 1; i <= lumps; i++) {
            namehi = "hi" + i;
            nodehi = ckt.makeVoltNode(getInstName() + namehi);
            hil = nodehi;
            if (i == lumps) {
                lowr = hil;
            } else {
                namelo = getInstName() + ":lo" + i;
                nodelo = ckt.makeVoltNode(namelo);
                lowr = nodelo;
            }
            r = prop * r1;
            c = prop * c1;
            ResInstance rfastl = new ResInstance();
            rfastl.setModelName(rModName);
            _name = getInstName() + ":rlo" + i;
            rfastl.setInstName(_name);
            rfastl.setPosIndex(posIndex);
            rfastl.setNegIndex(lowr.getIndex());
            rfastl.setResist(r);
            ResInstance rfasth = new ResInstance();
            rfasth.setModelName(rModName);
            _name = getInstName() + ":rhi" + i;
            rfasth.setInstName(_name);
            rfasth.setPosIndex(hil.getIndex());
            rfasth.setNegIndex(negIndex);
            rfasth.setResist(r);
            if (iSatPerL != 0) {
                DiodeInstance dfast = new DiodeInstance();
                dfast.setModelName(dModName);
                dfast.setJunctionCap(c1);
                dfast.setResist(rd);
                dfast.setSatCur(i1);
                _name = getInstName() + ":dlo" + i;
                dfast.setInstName(_name);
                dfast.setPosIndex(lowr.getIndex());
                dfast.setNegIndex(gndIndex);
                dfast.setArea(prop);
            } else {
                CapInstance cfast = new CapInstance();
                cfast.setModelName(cModName);
                _name = getInstName() + ":clo" + i;
                cfast.setInstName(_name);
                cfast.setPosIndex(lowr.getIndex());
                cfast.setNegIndex(gndIndex);
                cfast.setCapAC(c);
            }
            if (i != lumps) {
                if (iSatPerL != 0) {
                    DiodeInstance dfast = new DiodeInstance();
                    dfast.setModelName(dModName);
                    _name = getInstName() + ":dhi" + i;
                    dfast.setInstName(_name);
                    dfast.setPosIndex(hil.getIndex());
                    dfast.setNegIndex(gndIndex);
                    dfast.setArea(prop);
                    dfast.setJunctionCap(c1);
                    dfast.setResist(rd);
                    dfast.setSatCur(i1);
                } else {
                    CapInstance cfast = new CapInstance();
                    cfast.setModelName(cModName);
                    _name = getInstName() + ":chi" + i;
                    cfast.setInstName(_name);
                    cfast.setPosIndex(hil.getIndex());
                    cfast.setNegIndex(gndIndex);
                    cfast.setCapAC(c);
                }
            }
            prop *= p;
            lowl = lowr;
            hir = hil;
        }
        return true;
    }

    public String newName(String baseName, FastSet<String> names) {
        String _name = baseName;
        int i = 1;
        while (names.contains(_name)) {
            _name = baseName + "_" + i++;
        }
        if (!_name.equals(baseName)) {
            names.add(_name);
        }
        return _name;
    }

    public boolean accept(Mode mode) {
        return true;
    }

    public boolean unSetup() {
        return true;
    }

    public boolean load(Mode mode) {
        return true;
    }

    public boolean acLoad(Mode mode) {
        return true;
    }

    public boolean temperature() {
        return true;
    }

    public double truncateTimeStep(double timeStep) {
        return timeStep;
    }

    public boolean convTest(Mode mode) {
        return true;
    }

    public boolean loadInitCond() {
        return true;
    }
}

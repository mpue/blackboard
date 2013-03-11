/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.model.semi.mesfet;

import static org.pmedv.blackboard.spice.sim.spice.EnumConsts.*;

import org.pmedv.blackboard.spice.sim.spice.model.Model;

/**
 *
 * @author owenbad
 */
public class MESModelValues extends MESFETValues implements Model {

    protected int type = N_TYPE;
    protected double drnResist;
    protected double srcResist;
    protected double beta;
    protected double vto;
    protected double lambda;
    protected double alpha;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getDrnResist() {
        return drnResist;
    }

    public void setDrnResist(double drnResist) {
        this.drnResist = drnResist;
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double getSrcResist() {
        return srcResist;
    }

    public void setSrcResist(double srcResist) {
        this.srcResist = srcResist;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getVto() {
        return vto;
    }

    public void setVto(double vto) {
        this.vto = vto;
    }

}

package org.pmedv.blackboard.spice.sim.spice.model.sources.asrc;


public interface ASRCElement {

    public boolean init();

    public boolean acload(double deriv);

    public boolean load(double deriv);
}

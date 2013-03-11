package org.pmedv.blackboard.spice.sim.spice.model.sources;

import ktb.math.numbers.Complex;

public class VoltageElement extends SourceElement {

    protected int node1Index;
    protected int node2Index;

    public VoltageElement() {
    }

    public int getNode1Index() {
        return node1Index;
    }

    public void setNode1Index(int node1Index) {
        this.node1Index = node1Index;
    }

    public int getNode2Index() {
        return node2Index;
    }

    public void setNode2Index(int node2Index) {
        this.node2Index = node2Index;
    }

    @Override
    public Complex getValue() {
        if (node1Index != 0 && node2Index != 0) {
            return wrk.getRhsOldAt(node1Index).minus(wrk.getRhsOldAt(node2Index));
        } else if (node1Index != 0) {
            return wrk.getRhsOldAt(node1Index);
        } else if (node2Index != 0) {
            return wrk.getRhsOldAt(node2Index);
        }
        return Complex.zero();
    }

    @Override
    public String getName() {
        if (name == null) {
            if (node1Index != 0 && node2Index != 0) {
                name = "V(" + node1Index + ", " + node2Index + ")";
            } else if (node1Index != 0) {
                name = "V(" + node1Index + ")";
            } else if (node2Index != 0) {
                name = "V(" + node2Index + ")";
            }
        }
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

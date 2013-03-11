package org.pmedv.blackboard.provider;

import org.pmedv.blackboard.spice.SpiceSimulator;

public class SimulatorProvider extends AbstractElementProvider<SpiceSimulator> {

	public SimulatorProvider() {
		super(SpiceSimulator.class, "simulators");
	}
	
}

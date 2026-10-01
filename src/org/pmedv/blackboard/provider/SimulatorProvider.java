package org.pmedv.blackboard.provider;

import java.io.File;

import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.core.context.AppContext;

public class SimulatorProvider extends AbstractElementProvider<SpiceSimulator> {

	public SimulatorProvider() {
		super(SpiceSimulator.class, new File(AppContext.getCatalogDir(), "simulators"));
	}
	
}

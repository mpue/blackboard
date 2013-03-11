package org.pmedv.blackboard.test;

import java.io.File;
import java.util.ArrayList;

import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceUtil;

public class SubCircuitTest {



	public static void main(String[] args) throws Exception {

		File f = new File("models/siemens.lib");

		ArrayList<String> subcircuits = SpiceUtil.readRawSubcircuitsFromLib(f);

		System.out.println("found " + subcircuits.size() + " subcircuits.");

		ArrayList<Model> models = SpiceUtil.convertRawSubcircuits(subcircuits);
		SpiceUtil.importModels(models);
		
		
	}



}

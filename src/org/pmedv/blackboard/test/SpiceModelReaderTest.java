package org.pmedv.blackboard.test;

import java.io.File;
import java.util.ArrayList;

import org.pmedv.blackboard.spice.Model;
import org.pmedv.blackboard.spice.SpiceUtil;

public class SpiceModelReaderTest {

	public static void main(String[] args) throws Exception {

		File f = new File("models/siemens.lib");

		ArrayList<String> raw = SpiceUtil.readRawModelsFromLib(f);
		ArrayList<Model> models = SpiceUtil.convertRawModels(raw);
		SpiceUtil.importModels(models);

	}

}

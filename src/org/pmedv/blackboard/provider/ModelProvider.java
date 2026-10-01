package org.pmedv.blackboard.provider;

import java.io.File;

import org.pmedv.blackboard.spice.Model;
import org.pmedv.core.context.AppContext;

public class ModelProvider extends AbstractElementProvider<Model> {

	public ModelProvider() {
		super(Model.class, new File(AppContext.getCatalogDir(), "models"));
	}
	
}

package org.pmedv.blackboard.test;

import org.pmedv.blackboard.provider.ModelProvider;
import org.pmedv.blackboard.spice.Model;

public class SpiceModelProviderTest {

	public static void main(String[] args) {
		
		ModelProvider provider = new ModelProvider();
		
		provider.loadElements();
		
		for (Model m : provider.getElements()) {
			System.out.println(m);
		}
		
		
	}
	
}

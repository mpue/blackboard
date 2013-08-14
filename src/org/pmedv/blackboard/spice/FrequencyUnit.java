package org.pmedv.blackboard.spice;

public enum FrequencyUnit {

	HZ(1),
	KHZ(1000),
	MHZ(1000000),
	GHZ(1000000000);

	private int value;
	
	FrequencyUnit(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}

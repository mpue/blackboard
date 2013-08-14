package org.pmedv.blackboard.spice;

public enum TimeUnit {
	
	nS(0.000000001d),
	uS(0.000001d),
	ms(0.001d),
	S(1.0d);	
	
	private double value;
	
	TimeUnit(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}

package org.pmedv.blackboard.components;

public enum LineEdgeType {

	ROUND_DOT,
	SIMPLE_ARROW,
	STRAIGHT;

	public static LineEdgeType getDefault() {
		return ROUND_DOT;
	}
	
}

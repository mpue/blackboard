package org.pmedv.blackboard.components;

public class WireConnection {

	public static enum WireConnectionType {
		START,
		END,
		START_START,
		START_END,
		END_END,
		END_START
	}
	
	private Line line;
	private WireConnectionType type;
	
	public WireConnection(Line line, WireConnectionType type) {
		this.line = line;
		this.type = type;
	}
	/**
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
	}
	/**
	 * @return the type
	 */
	public WireConnectionType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(WireConnectionType type) {
		this.type = type;
	}

	
	
}

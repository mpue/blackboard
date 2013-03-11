package org.pmedv.blackboard.spice;

import java.util.ArrayList;
import java.util.List;

import org.pmedv.blackboard.components.Line;

public class Net {
	
	private int index;
	private List<Line> lines;

	public Net() {
		lines = new ArrayList<Line>();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> list) {
		this.lines = list;
	}
	
	
}

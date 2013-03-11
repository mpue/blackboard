package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;

import org.pmedv.blackboard.components.Line;
import org.pmedv.core.commands.AbstractCommand;

public class MoveLineCommand extends AbstractCommand {

	private static final long serialVersionUID = 6500497587724032900L;

	public static enum Mode {
		DRAG_START,
		DRAG_END,
		MOVE
	}
	
	private Mode mode;
	private Line line;

	private int oldLineStartX;
	private int oldLineStartY;
	private int oldLineStopX;
	private int oldLineStopY;
	private int oldX;
	private int oldY;
	
	
	@Override
	public void execute(ActionEvent e) {

		if (mode.equals(Mode.DRAG_START)) {

		}
		else if (mode.equals(Mode.DRAG_END)) {

		}
		else {
			
		}
		
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}


	
	
}

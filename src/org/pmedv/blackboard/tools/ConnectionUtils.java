package org.pmedv.blackboard.tools;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Resistor;

public class ConnectionUtils {

	/**
	 * Gets all lines that are connected with a given line
	 * 
	 * @param line the line to check for connections
	 * @param candidates potential candidates that might be connected
	 * 
	 * @return a list of lines that are connected to that line
	 */
	public static List<Line> getConnectedLines(Line line, List<Line> candidates) {
		
		LinkedList<Line> connections = new LinkedList<Line>();				
		checkConnections(line, candidates, connections);

		for (int i = 0 ; i < connections.size();i++) {
			checkConnections(connections.get(i), candidates, connections);
		}
		
		return connections;
		
	}
	
	private static void checkConnections(Line line, List<Line> input, List<Line> output) {
		
		for (Iterator<Line> it = input.iterator();it.hasNext();) {
			
			Line candidate = (Line)it.next();
			
			if (!(candidate instanceof Resistor)) {
				if (line.containsPoint(candidate.getStart(),4) || line.containsPoint(candidate.getEnd(),4) || 
					candidate.containsPoint(line.getStart(),4) || candidate.containsPoint(line.getEnd(),4)) {				
					output.add(candidate);
					it.remove();
				}
				
			}

		}
		
	}
	
}

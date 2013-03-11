/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.output;

import java.awt.event.ActionEvent;

import net.infonode.docking.View;

import org.jfree.chart.plot.PlotOrientation;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.spice.sim.output.MultiGraph;
import org.pmedv.core.commands.AbstractOpenEditorCommand;

/**
 *
 * @author owenbad
 */
public class TranPlotOutput extends PlotOutput {

    public TranPlotOutput(final String title) {
        super(title);
        
        AbstractOpenEditorCommand cmd = new AbstractOpenEditorCommand() {
			
			@Override
			public void execute(ActionEvent e) {				
				BoardEditor editor = EditorUtils.getCurrentActiveEditor();				
				graph = new MultiGraph(title, "Time", "Value", PlotOrientation.HORIZONTAL);		
				View v = new View(editor.getCurrentFile().getName(), null, graph);				
				openEditor(v, editor.getCurrentFile().getName().hashCode());
			}
		};

		cmd.execute(null);
    }
}

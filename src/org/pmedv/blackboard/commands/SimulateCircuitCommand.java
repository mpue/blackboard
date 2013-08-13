package org.pmedv.blackboard.commands;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.InputStream;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.apache.commons.io.FileUtils;
import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.blackboard.spice.SpiceSimulator.SimulatorType;
import org.pmedv.blackboard.spice.SpiceUtil;
import org.pmedv.blackboard.spice.sim.spice.Simulator;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.core.commands.AbstractEditorCommand;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.util.ErrorUtils;

@SuppressWarnings("serial")
public class SimulateCircuitCommand extends AbstractEditorCommand {

	private SpiceSimulator simulator;
	private String data;
	
	private final SimulatorProvider provider = AppContext.getContext().getBean(SimulatorProvider.class);
	
	public SimulateCircuitCommand() {
		putValue(Action.NAME, resources.getResourceByKey("SimulateCircuitCommand.name"));
		putValue(Action.SMALL_ICON, resources.getIcon("icon.simulate"));
		putValue(Action.SHORT_DESCRIPTION, resources.getResourceByKey("SimulateCircuitCommand.description"));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK));
		setEnabled(false);
	}

	@Override
	public void execute(ActionEvent e) {

 		BoardEditor editor = EditorUtils.getCurrentActiveEditor();

 		if (simulator == null) {
 			// simulator has not been selected by context menu or is null by any other cause
 			// now try to find the default simulator.
 			for (SpiceSimulator sim : provider.getElements()) {
 				if (sim.isDefaultSimulator()) {
 					simulator = sim;
 					break;
 				}
 			}
 			
 		}
 		// somehow the simulator ist still null
 		// use the default internal simulator
 		if (simulator == null) {
 			simulator = new SpiceSimulator("Internal","internal","Internal");
 			simulator.setType(SimulatorType.INTERNAL);
 		}

 		if (editor != null) {
			
			try {				
				doSimulation(editor, simulator);				
			}
			catch (Exception e1) {
				ErrorUtils.showErrorDialog(e1);
				return;
			}
			
		}
		else {
			// command has been called from netlist editor 
			if (data != null) {				
				try {				
					doSimulation(simulator);				
				}
				catch (Exception e1) {
					ErrorUtils.showErrorDialog(e1);
					return;
				}
			}
		}

		simulator = null;
	}

	private void doSimulation(final BoardEditor editor, final SpiceSimulator simulator) throws Exception {
		
		final StringBuffer data = SpiceUtil.createSpiceNetList(editor);

		new Thread(new Runnable() {
			
			@Override
			public void run() {

				if (simulator.getType().equals(SimulatorType.INTERNAL)) {
					doInternalSim(data);
				}
				else {
					SpiceUtil.addSpiceDefaultControl(data, BoardUtil.getTextParts(editor.getModel()));
					try {
						doSpiceSim(data,simulator);
					}
					catch (Exception e) {
						ErrorUtils.showErrorDialog(e);
					}					
					
				}
			}
		}).start();
		
	}
	
	private void doSimulation(final SpiceSimulator simulator) throws Exception {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				if (simulator.getType().equals(SimulatorType.INTERNAL)) {
					doInternalSim(new StringBuffer(data));
				}
				else {
					try {
						doSpiceSim(new StringBuffer(data),simulator);
					}
					catch (Exception e) {
						ErrorUtils.showErrorDialog(e);
					}					
					
				}
			}
		}).start();
		
	}
	

	private void doInternalSim(StringBuffer data) {
		Simulator sim = new Simulator();
		try {
			Deck deck = sim.parseFile(data.toString());
		    sim.assembleDeck(deck);
		    sim.run();
		}
		catch (ParserException e1) {
			ErrorUtils.showErrorDialog(e1);
		}
	}
	
	private void doSpiceSim(StringBuffer data, SpiceSimulator simulator) throws Exception {

		String path = System.getProperty("user.home")+"/."+AppContext.getName()+"/temp";		
		File f = new File(path,"temp_"+System.currentTimeMillis()+".cir");
		
		FileUtils.writeStringToFile(f, data.toString());

		String[] command = { simulator.getPath(), simulator.getParameters(), f.getAbsolutePath()};
		Process proc = Runtime.getRuntime().exec(command,null,new File(simulator.getPath()).getParentFile());
		
		StringBuffer outputReport = new StringBuffer();
		StringBuffer errorBuffer  = new StringBuffer();
		
		int inBuffer, errBuffer;
		
		InputStream is = proc.getInputStream();
		InputStream es = proc.getErrorStream();

		while ((inBuffer = is.read()) != -1) {
	        outputReport.append((char) inBuffer);
	    }

	    while ((errBuffer = es.read()) != -1) {
	        errorBuffer.append((char) errBuffer);
	    }
		
		System.out.println(outputReport);
		System.out.println(errorBuffer);
		
		f.deleteOnExit();
			
	}

	/**
	 * @return the simulator
	 */
	public SpiceSimulator getSimulator() {
		return simulator;
	}

	/**
	 * @param simulator the simulator to set
	 */
	public void setSimulator(SpiceSimulator simulator) {
		this.simulator = simulator;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	
}

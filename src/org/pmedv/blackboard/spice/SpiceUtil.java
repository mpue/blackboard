package org.pmedv.blackboard.spice;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.commons.io.FileUtils;
import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.commands.ExportNetlistCommand;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Pin;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.components.TextPart;
import org.pmedv.blackboard.components.TextPart.TextType;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.spice.sim.spice.Simulator;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

public class SpiceUtil {

	private static final Pattern splitModelsPattern = Pattern.compile("\\.MODEL .*?(.*).*?(.*)");
	private static final Pattern identifyModelPattern = Pattern.compile("\\.MODEL.*?(.*).*?(.*).*\\(",Pattern.MULTILINE);
	private static final Pattern identifySubCircuitPattern = Pattern.compile("\\.SUBCKT.*?(.*)");
	private static final ResourceService resources = AppContext.getBean(ResourceService.class);	
	private static final HashMap<String, SpiceType> typeMap = new HashMap<String, SpiceType>();	
	private static final Marshaller marshaller;
	
	static  {
		try {
			marshaller = JAXBContext.newInstance(Model.class).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unable to create marshaller for "+Model.class);
		}		
		
		typeMap.put("R",SpiceType.RESISTOR);
		typeMap.put("C",SpiceType.CAPACITOR);
		typeMap.put("L",SpiceType.INDUCTOR);
		typeMap.put("D",SpiceType.DIODE);
		typeMap.put("NPN",SpiceType.NPN);
		typeMap.put("PNP",SpiceType.PNP);
		typeMap.put("SW",SpiceType.VOLTAGE_CONTROLLED_SWITCH);
		typeMap.put("CSW",SpiceType.CURRENT_CONTROLLED_SWITCH);
		
	}
	
	private static boolean inSubCircuit = false;
	
	public static void importModels(ArrayList<Model> models) throws JAXBException, PropertyException, FileNotFoundException {
		for (Model model : models) {			
			marshaller.marshal(model, new FileOutputStream(new File(System.getProperty("user.home")+"/."+AppContext.getName()+"/models/", model.getName() + ".xml")));			
		}
	}

	public static ArrayList<Model> convertRawSubcircuits(ArrayList<String> subcircuits) {
		ArrayList<Model> models = new ArrayList<Model>();
		
		for (String subcircuit : subcircuits) {

			Matcher m = identifySubCircuitPattern.matcher(subcircuit);

			if (m.find()) {

				/**
				 * replace all multiple whitespaces by just one, split the
				 * result by spaces, take the second item split by "/" and take
				 * the first item, which is the name.
				 */

				String name = m.group().replaceAll("\\+", "").split(" ")[1].replaceAll("/", "");

				// name correction, we do not want names with slashes in it
				subcircuit = subcircuit.replaceAll(m.group().replaceAll("\\+", "").split(" ")[1], name);
				
				Model model = new Model();
				model.setName(name);
				model.setType(SpiceType.SUBCIRCUIT);
				model.setContent(subcircuit);

				models.add(model);

			}

		}
		return models;
	}

	public static ArrayList<String> readRawSubcircuitsFromLib(File f) throws IOException {
		boolean inSubCircuit = false;

		List<?> lines = FileUtils.readLines(f, "UTF-8");

		StringBuffer subCircuit = null;

		ArrayList<String> subcircuits = new ArrayList<String>();

		for (Object o : lines) {

			String line = (String) o;

			if (line.startsWith(".SUBCKT") || line.startsWith(".subckt")) {
				subCircuit = new StringBuffer();
				subCircuit.append(line);
				subCircuit.append("\n");
				inSubCircuit = true;
				continue;
			}
			else if (line.startsWith(".ENDS") || line.startsWith(".ends")) {
				
				if (subCircuit == null) {
					// seems the file is fucked up somehow, simply skip and continue
					inSubCircuit = false;
					continue;					
				}
				
				inSubCircuit = false;
				subCircuit.append(line);
				subcircuits.add(subCircuit.toString());
				continue;
			}

			if (inSubCircuit) {
				subCircuit.append(line);				
				subCircuit.append("\n");
			}

		}
		return subcircuits;
	}
	
	public static ArrayList<String> readRawModelsFromLib(File libFile) throws Exception {

		List<?> lines = FileUtils.readLines(libFile, "UTF-8");

		StringBuffer model = null;

		ArrayList<String> models = new ArrayList<String>();

		for (Object o : lines) {
			
			String line = (String) o;

			if (line.startsWith(".SUBCKT") || line.startsWith(".subckt")) {
				inSubCircuit = true;
				continue;
			}
			else if (line.startsWith(".ENDS") || line.startsWith(".ends")) {
				inSubCircuit = false;
				continue;
			}			
			if (inSubCircuit) {
				continue;
			}
			if (line.startsWith("*")) {
				continue;
			}
			
			Matcher m = splitModelsPattern.matcher(line);
			if (m.find()) {
				if (model != null) {
					models.add(model.toString().replaceAll("/", ""));
				}
				model = new StringBuffer();
				model.append(line + "\n");
			}
			else {
				if (model != null) {
					model.append(line + "\n");
				}
			}

		}

		return models;

	}

	public static ArrayList<Model> convertRawModels(ArrayList<String> rawData) {

		ArrayList<Model> models = new ArrayList<Model>();

		for (String s : rawData) {

			Matcher m = identifyModelPattern.matcher(s);

			if (m.find()) {

				String data = m.group().substring(0,m.group().lastIndexOf("("));				
				data = data.trim().replaceAll(" +", " ");			
				String[] ident = data.split(" ");
				SpiceType type = typeMap.get(ident[2]);

				if (type != null) {
					s = s.replaceAll("\\+", ""); 					
					Model model = new Model(ident[1].replace("/", ""), s, type);
					models.add(model);					
				}
				
			}

		}
		return models;
	}

	public static void addSpiceDefaultControl(StringBuffer data, List<TextPart> textparts) {
		
		data.append(System.getProperty("line.separator"));
		data.append(".control");
		data.append(System.getProperty("line.separator"));
		data.append("set noaskquit");
		data.append(System.getProperty("line.separator"));
		data.append("run");
		data.append(System.getProperty("line.separator"));
		
		for (TextPart t : textparts)  {
			if (t.getType().equals(TextPart.TextType.CONTROL)) {
				data.append(t.getText());
				data.append(System.getProperty("line.separator"));				
			}
		}
		
		data.append(".endc");
		data.append(System.getProperty("line.separator"));
	}
	
	/**
	 * Creates a SPICE netlist in ASCII format to be simulatest with the {@link Simulator} or
	 * to be exported by the {@link ExportNetlistCommand}
	 * 
	 * @param editor the current active {@link BoardEditor}
	 * @param model
	 * @return
	 */
	public static StringBuffer createSpiceNetList(BoardEditor editor) throws Exception {
		
		if (editor.getCurrentFile() == null) {
			throw new IllegalArgumentException(resources.getResourceByKey("msg.board.notsaved"));
		}
		
		BoardEditorModel model = editor.getModel();
		
		StringBuffer data = new StringBuffer();

		data.append(editor.getCurrentFile().getName());
		data.append(System.getProperty("line.separator"));

		List<Net> nets = BoardUtil.createNetList(model);
		List<Symbol> symbols = BoardUtil.getSymbols(model);
		
		boolean grounded = findGroundNodes(nets,symbols);
		
		if (!grounded) {
			throw new CircuitNotGroundedException(resources.getResourceByKey("msg.circuit.notgrounded")); 
		}
		
		BoardUtil.createPinIndices(model, nets);

		List<Model> models = new ArrayList<Model>();

		// collect the models distributed over the schematic
		for (Symbol symbol : symbols) {
			if (symbol.isCustomModel() && symbol.getModel() != null) {
				// we need onl one definition per model
				if (!models.contains(symbol.getModel())) {
					models.add(symbol.getModel());
				}				
			}
		}	
		
		// finally add the collected models to the output
		for (Model mod : models) {
			String content = mod.getContent();
			if (!mod.getType().equals(SpiceType.SUBCIRCUIT)) {
				// do some cleanup, since the ngspice parser is a big mess
				content = content.replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("/","").replaceAll("\\s+", " ");			
			}
			data.append(content);
			data.append(System.getProperty("line.separator"));							
		}
		
		for (Symbol symbol : symbols) {

			if (symbol.getType() != null) {

				switch (symbol.getType()) {
					case VOLTAGE_SOURCE:
						createVoltageSource(data, symbol);
						break;
					case RESISTOR:
						createResistor(data, symbol);
						break;
					case CAPACITOR:
						createCapacitor(data, symbol);
						break;				
					case INDUCTOR:
						createInductor(data, symbol);
						break;
					case DIODE: 
						createDiode(data, symbol);
						break;
					case NPN : 
					case PNP : 
						createBJT(data, symbol);
						break;
					case SUBCIRCUIT:
						createSubcircuit(data, symbol);
						break;
					default:
						break;
				}

			}

		}

		List<TextPart> textParts = BoardUtil.getTextParts(model);
		
		for (TextPart part : textParts) {			
			if (part.getType().equals(TextType.SPICE_DIRECTIVE)) {
				data.append(part.getText());
				data.append(System.getProperty("line.separator"));
			}			
		}

		data.append(System.getProperty("line.separator"));
		data.append(".end");
		data.append(System.getProperty("line.separator"));
		return data;
	}	
	
	/**
	 * <p>
	 * Performs a lookup of all wires, that are connected with a ground node. First 
	 * all net indices of all wires, which are connected to a ground node are being collected.
	 * <p>
	 * Second step is to correct all indices of all wires sharing these indices.
	 * </p>
	 * 
	 * @param nets The list of {@link Net}s being looked up for ground nodes
	 * @param symbols The list of {@link Symbol}s containing the ground nodes to associate
	 * @return
	 */
	private static boolean findGroundNodes(List<Net> nets, List<Symbol> symbols) {

		boolean grounded = false;
		
		ArrayList<Integer> groundIndices = new ArrayList<Integer>();
		
		for (Net net : nets) {
			
			for (Line line : net.getLines()) {
				
				for (Symbol symbol : symbols) {

					if (symbol.getName().equalsIgnoreCase("gnd")) {
					
						Pin pin = symbol.getConnections().getPin().get(0);
						
						if (symbol.getRotation() != 0) {
							float rot = symbol.getRotation();
							
							if (rot == 90 || rot == 270) {
								rot -= 180;
							}
							
							// we need to rotate the points of the lines of the rotated symbol to determine the correct locations.
							Point p = BoardUtil.rotatePoint(pin.getX(),pin.getY(), 0, 0, rot);
							
							if (line.containsPoint((float)p.getX()+symbol.getxLoc(),(float)p.getY()+symbol.getYLoc())) {
								groundIndices.add(line.getNetIndex());							
							}
					
						}
						else {
							if (line.containsPoint(pin.getX()+symbol.getxLoc(),pin.getY()+symbol.getYLoc())) {
								groundIndices.add(line.getNetIndex());															
							}						
						}
						
					}
					
				}
				
			}
			
		}
		
		if (groundIndices.size() > 0) { // found at least grounded net, now correct indiced of grounded wires 
			
			for (Net net : nets) {
			
				for (Integer index : groundIndices) {					
					if (net.getIndex() == index) {
						net.setIndex(0);
						grounded = true;
						for (Line line : net.getLines()) {
							line.setNetIndex(0);
						}					
						break;
					}
				}
				
				
			}
			
		}
		
		return grounded;
	}

	/**
	 * Creates an ASCII subcircuit in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the subcircuit in
	 * @param symbol the {@link Symbol} to create the subcircuit for
	 */
	private static void createSubcircuit(StringBuffer data, Symbol symbol) {
		if (symbol.getModel() == null) {
			throw new IllegalStateException("Symbol "+symbol.getName()+" has no model.");
		}

		data.append(symbol.getName());
		data.append(" ");
		
		Collections.sort(symbol.getConnections().getPin());
		
		for (Pin pin : symbol.getConnections().getPin()) {
			data.append(pin.getNetIndex());
			data.append(" ");
		}

		data.append(symbol.getModel().getName());
		
		data.append(System.getProperty("line.separator"));
		
	}
	
	/**
	 * Creates an ASCII transistor in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the transistor in
	 * @param symbol the {@link Symbol} to create the transistor for
	 */

	public static void createBJT(StringBuffer data, Symbol symbol) throws Exception {
		
		if (symbol.getModel() == null) {
			throw new IllegalStateException("Symbol "+symbol.getName()+" has no model.");
		}
		
		data.append(symbol.getName());
		data.append(" ");
		
		int pinC = 0;
		int pinB = 0;
		int pinE = 0;
		
		for (Pin pin : symbol.getConnections().getPin()) {
			
			if (pin.getName().equalsIgnoreCase("c")) {
				pinC = pin.getNetIndex();
			}
			else if (pin.getName().equalsIgnoreCase("b")) {
				pinB = pin.getNetIndex();
			}
			else if (pin.getName().equalsIgnoreCase("e")) {
				pinE = pin.getNetIndex();
			}
			
		}
		data.append(pinC);
		data.append(" ");
		data.append(pinB);
		data.append(" ");
		data.append(pinE);
		data.append(" ");
		data.append(symbol.getModel().getName().replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("/","").replaceAll("\\s+", " "));
		data.append(System.getProperty("line.separator"));
		
	}

	/**
	 * Creates an ASCII resistor in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the resistor in
	 * @param symbol the {@link Symbol} to create the resistor for
	 */

	public static void createResistor(StringBuffer data, Symbol symbol) {

		data.append(symbol.getName());
		data.append(" ");

		int pinA = 0;
		int pinB = 0;

		for (Pin pin : symbol.getConnections().getPin()) {

			if (pin.getNum() == 1) {
				pinA = pin.getNetIndex();
			}
			if (pin.getNum() == 2) {
				pinB = pin.getNetIndex();
			}

		}

		data.append(pinA);
		data.append(" ");
		data.append(pinB);
		data.append(" ");
		data.append(symbol.getValue());
		data.append(System.getProperty("line.separator"));
		
	}
	
	/**
	 * Creates an ASCII inductor in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the inductor in
	 * @param symbol the {@link Symbol} to create the inductor for
	 */

	public static void createInductor(StringBuffer data, Symbol symbol) {

		data.append(symbol.getName());
		data.append(" ");

		int pinA = 0;
		int pinB = 0;

		for (Pin pin : symbol.getConnections().getPin()) {

			if (pin.getNum() == 1) {
				pinA = pin.getNetIndex();
			}
			if (pin.getNum() == 2) {
				pinB = pin.getNetIndex();
			}

		}

		data.append(pinA);
		data.append(" ");
		data.append(pinB);
		data.append(" ");
		
		if (symbol.isCustomModel()) {
			data.append(symbol.getModel().getName().replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("/","").replaceAll("\\s+", " "));
		}
		else {
			data.append(symbol.getValue());			
		}
		
		data.append(System.getProperty("line.separator"));
		
	}	
	
	/**
	 * Creates an ASCII inductor in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the inductor in
	 * @param symbol the {@link Symbol} to create the inductor for
	 */

	public static void createDiode(StringBuffer data, Symbol symbol) throws Exception {

		if (symbol.getModel() == null) {
			throw new IllegalArgumentException("Symbol "+symbol.getName()+" : " + resources.getResourceByKey("msg.model.notdefined"));
		}
		
		data.append(symbol.getName());
		data.append(" ");

		int pinA = 0;
		int pinB = 0;

		for (Pin pin : symbol.getConnections().getPin()) {

			if (pin.getName().equalsIgnoreCase("A")) {
				pinA = pin.getNetIndex();
			}
			if (pin.getName().equalsIgnoreCase("K")) {
				pinB = pin.getNetIndex();
			}

		}

		data.append(pinA);
		data.append(" ");
		data.append(pinB);
		data.append(" ");

		data.append(symbol.getModel().getName().replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("/","").replaceAll("\\s+", " "));		
		data.append(System.getProperty("line.separator"));
		
	}		

	/**
	 * Creates an ASCII capacitor in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the capacitor in
	 * @param symbol the {@link Symbol} to create the capacitor for
	 */

	public static void createCapacitor(StringBuffer data, Symbol symbol) {

		data.append(symbol.getName());
		data.append(" ");

		int pinA = 0;
		int pinB = 0;

		for (Pin pin : symbol.getConnections().getPin()) {

			if (pin.getNum() == 1) {
				pinA = pin.getNetIndex();
			}
			if (pin.getNum() == 2) {
				pinB = pin.getNetIndex();
			}

		}

		data.append(pinA);
		data.append(" ");
		data.append(pinB);
		data.append(" ");
		data.append(symbol.getValue());
		data.append(System.getProperty("line.separator"));
		
	}	
	
	/**
	 * Creates an ASCII voltage source in SPICE notation based on a given
	 * {@link Symbol} into the {@link StringBuffer} denoted by the param data. 
	 * 
	 * @param data the {@link StringBuffer} to store the voltage source in
	 * @param symbol the {@link Symbol} to create the voltage source for
	 */
	public static void createVoltageSource(StringBuffer data, Symbol symbol) {

		data.append(symbol.getName());
		data.append(" ");

		int pinA = 0;
		int pinB = 0;

		// TODO : Probably the pin orientation is wrong for DC sources!
		
		for (Pin pin : symbol.getConnections().getPin()) {

			if (pin.getNum() == 1) {
				pinA = pin.getNetIndex();
			}
			if (pin.getNum() == 2) {
				pinB = pin.getNetIndex();
			}

		}

		data.append(pinA);
		data.append(" ");
		data.append(pinB);
		data.append(" ");

		if (symbol.getProperties().getProperty(VoltageSourceProperties.MODE).equalsIgnoreCase("DC")) {
			data.append("DC");
			data.append(" ");
			data.append(symbol.getProperties().getProperty(VoltageSourceProperties.DC_VOLTAGE));
			data.append(System.getProperty("line.separator"));
			return;
		}
		else {
			data.append("AC");
			data.append(" ");
		}

		String shape = symbol.getProperties().getProperty(VoltageSourceProperties.SHAPE);
		Double dcOffset  = Double.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.DC_OFFSET));
		Double amplitude = Double.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.AC_AMPLITUDE)); 
		
		Double frequency = Double.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.FREQUENCY));
		FrequencyUnit u = FrequencyUnit.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.FREQUENCY_UNIT));
		frequency = frequency * u.getValue();
		
		Integer dutyCycle;
		
		Double riseTime;
		Double fallTime;
		
		try {
			dutyCycle = Integer.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.DUTY_CYCLE));
		}
		catch (Exception e) {
			dutyCycle = 50;
		}

		try {
			riseTime = Double.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.RISE_TIME));
			TimeUnit t = TimeUnit.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.RISE_TIME_UNIT));
			riseTime = riseTime * t.getValue();
		}
		catch (Exception e) {
			riseTime = 0d;
		}

		try {
			fallTime = Double.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.FALL_TIME));
			TimeUnit t = TimeUnit.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.FALL_TIME_UNIT));
			fallTime = fallTime * t.getValue();
		}
		catch (Exception e) {
			fallTime = 0d;
		}
		
		data.append(shape);
		data.append("(");
		
		double degrees = Double.valueOf(symbol.getProperties().getProperty(VoltageSourceProperties.AC_PHASE));
		
		if (shape.equalsIgnoreCase("sin")) {		
			data.append(dcOffset);
			data.append(" ");
			data.append(amplitude);
			data.append(" ");
			data.append(frequency);
			data.append(" ");
			data.append("0");
			data.append(" ");
			data.append("0");
			data.append(" ");
			data.append(degrees);
		}
		/**
		 * For the moment, we keep the pulse voltage source as simple as possible. For now
		 * it has to be as much user friendly as possible. From the beginner's point of view 
		 * the pulse voltage source is only a simple ac voltage source with a pulse shape,
		 * a frequency and an offset.
		 * 
		 * So we let the user only change the same parameters like for a sinus voltage source.
		 * 
		 * TODO : Provide an advanced mode for SPICE experts
		 * TODO : What about the phase of a pulse voltage source
		 * 
		 */
		else if (shape.equalsIgnoreCase("pulse")) {
			
			double period = 1d / frequency; // f = 1/t => t = 1/f
			double pulsewidth = (period / 100.0d) * Double.valueOf(dutyCycle).doubleValue();

			data.append(-amplitude+dcOffset);		// initial value	
			data.append(" ");
			data.append( amplitude+dcOffset);		// pulsed value
			data.append(" ");
			data.append("0");						// delay time
			data.append(" ");
			data.append(riseTime);						// rise time
			data.append(" ");
			data.append(fallTime);						// fall time
			data.append(" ");
			data.append(" ");
			data.append("0");
			data.append(" ");
			data.append("0");
			data.append(" ");
			data.append(degrees);
			
			DecimalFormat df = new DecimalFormat("#.############");
			DecimalFormatSymbols custom = new DecimalFormatSymbols();
			custom.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(custom);
			data.append(df.format(pulsewidth));
			data.append(" ");
			data.append(df.format(period));
		}
		
		data.append(")");
		data.append(System.getProperty("line.separator"));
	}	
}

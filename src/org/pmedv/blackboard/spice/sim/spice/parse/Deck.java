/*
 * Deck.java
 * 
 * Created on Oct 31, 2007, 7:35:51 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.model.sources.CurrentElement;
import org.pmedv.blackboard.spice.sim.spice.model.sources.SourceElement;
import org.pmedv.blackboard.spice.sim.spice.model.sources.VoltageElement;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.AcCard;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.AnalysisCard;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.DcOpCard;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.DcTransCurveCard;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.TransFuncCard;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.TransientCard;
import org.pmedv.blackboard.spice.sim.spice.parse.discrete.*;
import org.pmedv.blackboard.spice.sim.spice.parse.output.OutputCard;
import org.pmedv.blackboard.spice.sim.spice.parse.output.PlotCard;
import org.pmedv.blackboard.spice.sim.spice.parse.output.PrintCard;
import org.pmedv.blackboard.spice.sim.spice.parse.output.RawCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.DiodeCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.DiodeModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt.BJT1ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt.BJT2ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.bjt.BJTCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.jfet.JFET1ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.jfet.JFET2ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.jfet.JFETCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet.MES1ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet.MESAModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mesfet.MESFETCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOS1ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOS2ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOS3ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOS6ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOS9ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.MOSCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.bsim.BSIM1ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.semi.mos.bsim.BSIM2ModelCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.IAMCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.ICISCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.IExpCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.IPWLCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.IPulseCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.ISFFMCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.ISineCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.ISrcCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.isrc.VCISCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.ICVSCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VAMCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VCVSCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VExpCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VPulseCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VPwlCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VSFFMCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VSineCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.vsrc.VSrcCard;

import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.FastTable;

/**
 *
 * @author Kristopher T. Beck
 */
public class Deck {

    private String title = "";
    private FastTable<String> nodeTable = new FastTable<String>();
    private FastList<InstanceCard> instanceCards = new FastList<InstanceCard>();
    private FastMap<String, ModelCard> modelCards = new FastMap<String, ModelCard>();
    private FastList<AnalysisCard> analysisCards = new FastList<AnalysisCard>();
    private FastList<OutputCard> outputCards = new FastList<OutputCard>();
    private FastList<SourceElement> srcElements = new FastList<SourceElement>();
    private String[] elements;
    private String previousLine = "";
    private boolean continueLine;
    //private char[] instPrefixes = {'R', 'C', 'I', 'V', 'I', 'D', 'K', 'E', 'F', 'G', 'H', 'I', 'Q', 'Z'};
    public static final String TWO_NODE_V_STRING = "V\\((\\w+), ?(\\w+)\\)";
    public static final String ONE_NODE_V_STRING = "V\\((\\w+)\\)";
    public static final String CURRENT_NODE_STRING = "I\\((\\w+)\\)";
    public static final Pattern LEVEL_PATTERN = Pattern.compile("LEVEL\\s*=\\s*(" + Parser.DOUBLE_STRING+")");
    public static final Pattern ONE_NODE_V_PATTERN = Pattern.compile(ONE_NODE_V_STRING);
    public static final Pattern TWO_NODE_V_PATTERN = Pattern.compile(TWO_NODE_V_STRING);
    public static final Pattern CURRENT_NODE_PATTERN = Pattern.compile(CURRENT_NODE_STRING);

    public Deck() {
        nodeTable.add("gnd");
    }

    public Deck(String str) throws ParserException {
        super();
        elements = str.split("\\n");
        for (String line : elements) {
            if (!readLine(line)) {
                break;
            }
        }
    }

    public boolean readLine(String line) throws ParserException {
        if (title.isEmpty()) {
            title = line;
            return true;
        }
        if (line.startsWith("*") || line.startsWith(" ")) {
            return true;
        }
        line = conditionLine(line);
        if (continueLine) {
            line = previousLine + " " + line;
            previousLine = "";
        } else if (line.startsWith("+")) {
            previousLine = previousLine + " " + line.substring(1);
            return true;
        }
        if (line.endsWith("+")) {
            if (!previousLine.isEmpty()) {
                parseLine(previousLine);
            }
            previousLine = line.substring(0, line.length() - 2);
            continueLine = true;
            return true;
        }
        if (line.equals(".END")) {
            parseLine(previousLine);
            return false;
        }
        String parsable = previousLine;
        previousLine = line;
        if (parsable.isEmpty()) {
            return true;
        }
        return parseLine(parsable);
    }

    private boolean parseLine(String line) throws ParserException {
        if (line.startsWith(".")) {
            if (line.startsWith(".END SUB")) {
                throw new UnsupportedOperationException();
            } else if (line.startsWith(".END")) {
                return false;
            } else if (line.startsWith(".OPTION")) {
                //TODO parse option line
            } else if (line.startsWith(".OP")) {
                AnalysisCard acard = new DcOpCard(line);
                analysisCards.add(acard);
            } else if (line.startsWith(".AC")) {
                AnalysisCard acard = new AcCard(line);
                analysisCards.add(acard);
            } else if (line.startsWith(".TF")) {
                AnalysisCard acard = new TransFuncCard(line);
                analysisCards.add(acard);
            } else if (line.startsWith(".TRAN")) {
                AnalysisCard acard = new TransientCard(line);
                analysisCards.add(acard);
            } else if (line.startsWith(".DC")) {
                AnalysisCard acard = new DcTransCurveCard(line);
                analysisCards.add(acard);
            } else if (line.startsWith(".NOISE")) {
                throw new ParserException("Noise not yet implemented", new UnsupportedOperationException());
            } else if (line.startsWith(".SAVE")) {
                outputCards.add(new RawCard(line));
            } else if (line.startsWith(".PRINT")) {
                outputCards.add(new PrintCard(line));
            } else if (line.startsWith(".PLOT")) {
                outputCards.add(new PlotCard(line));
            } else if (line.startsWith(".MODEL")) {
                ModelCard model = createModelCard(line);
                if (model != null) {
                    modelCards.put(model.getName(), model);
                }
            }
        } else {
            InstanceCard card = createInstanceCard(line.charAt(0), line);
            if (card != null) {
                addCard(card);
            }
        }
        return true;
    }

    public FastList<AnalysisCard> getAnalysisCards() {
        return analysisCards;
    }

    public FastList<InstanceCard> getInstanceCards() {
        return instanceCards;
    }

    public FastMap<String, ModelCard> getModelCards() {
        return modelCards;
    }

    public FastTable<String> getNodes() {
        return nodeTable;
    }

    public FastList<OutputCard> getOutputCards() {
        return outputCards;
    }

    public FastList<SourceElement> getSrcElements() {
        return srcElements;
    }

    public void setSrcElements(FastList<SourceElement> srcElements) {
        this.srcElements = srcElements;
    }

    public String getTitle() {
        return title;
    }

    public String conditionLine(String str) {
        str = str.trim();
        str = str.toUpperCase();
        str = str.replaceAll("\\t| +", " ");
        Matcher m = Pattern.compile("(\\d+) ?E ?\\+?(\\-?[\\d]+)").matcher(str);
        if (m.find()) {
//            str = m.replaceAll(m.group(1) + 'E' + m.group(2));
        }
        return str;
    }

    public void addNodes() {
    }

    public void addCard(InstanceCard card) {

        instanceCards.add(card);
    }

    public InstanceCard getInstanceCard(String name) {
        for (FastList.Node<InstanceCard> n = instanceCards.head(),
                end = instanceCards.tail(); (n = n.getNext()) != end;) {
            InstanceCard card = n.getValue();
            if (card.getInstName().equals(name)) {
                return card;
            }
        }
        return null;
    }

    public ModelCard getModelCard(String name) {
        return modelCards.get(name);
    }

    public int getNodeIndex(String name) {
        if (name.equals("gnd") || name.equals("0")) {
            return 0;
        }
        int i = nodeTable.indexOf(name);
        if (i == -1) {
            i = nodeTable.size();
            nodeTable.add(name);
        }
        return i;
    }

    public InstanceCard createInstanceCard(char c, String line) throws ParserException {
        switch (c) {
            // TODO  finish suncircuit
            case 'X':
                throw new ParserException("Error: " + line + " is not yet implemented", new UnsupportedOperationException());
            //return new SubCkt(str);
            case 'B':
                // TODO  finish asrc
                throw new ParserException("Error: " + line + " is not yet implemented", new UnsupportedOperationException());
            //return new ASrcCard(str);

            case 'R':
                return new ResCard(line);
            case 'C':
                return new CapCard(line);
            case 'L':
                return new IndCard(line);
            case 'K':
                return new MutualCard(line);
            case 'D':
                return new DiodeCard(line);
            case 'E':
                return new VCVSCard(line);
            case 'F':
                return new ICISCard(line);
            case 'G':
                return new VCISCard(line);
            case 'H':
                return new ICVSCard(line);
            case 'I':
                if (line.contains("AM")) {
                    return new IAMCard(line);
                } else if (line.contains("EXP")) {
                    return new IExpCard(line);
                } else if (line.contains("PWL")) {
                    return new IPWLCard(line);
                } else if (line.contains("PULSE")) {
                    return new IPulseCard(line);
                } else if (line.contains("SFFM")) {
                    return new ISFFMCard(line);
                } else if (line.contains("SIN")) {
                    return new ISineCard(line);
                } else {
                    return new ISrcCard(line);
                }
            case 'V':
                if (line.contains("AM")) {
                    return new VAMCard(line);
                } else if (line.contains("EXP")) {
                    return new VExpCard(line);
                } else if (line.contains("PWL")) {
                    return new VPwlCard(line);
                } else if (line.contains("PULSE")) {
                    return new VPulseCard(line);
                } else if (line.contains("SFFM")) {
                    return new VSFFMCard(line);
                } else if (line.contains("SIN")) {
                    return new VSineCard(line);
                } else {
                    return new VSrcCard(line);
                }
            case 'Q':
                return new BJTCard(line);
            case 'M':
                return new MOSCard(line);
            case 'J':
                return new JFETCard(line);
            case 'Z':
                return new MESFETCard(line);
            case 'S':
                return new SWCard(line);
            case 'W':
                return new CSWCard(line);
            case 'U':
                return new URCCard(line);
            default:
                throw new ParserException("Error: " + line + " cannot be parsed.");
        }
    }

    public ModelCard createModelCard(String line) throws ParserException {
        int level = 0;
        Matcher m = ModelCard.MODEL_PATTERN.matcher(line);
        if (m.find()) {
            String type = m.group(2);
            if (type.contentEquals(ModelCard.R_TYPE)) {
                return new ResModelCard(line);
            } else if (type.contentEquals(ModelCard.C_TYPE)) {
                return new CapModelCard(line);
            } else if (type.contentEquals(ModelCard.L_TYPE)) {
                return new IndModelCard(line);
            } else if (type.contentEquals(ModelCard.D_TYPE)) {
                return new DiodeModelCard(line);
            } else if (type.contentEquals(ModelCard.SW_TYPE)) {
                return new SwModelCard(line);
            } else if (type.contentEquals(ModelCard.CW_TYPE)) {
                return new CSWModelCard(line);
            } else if (type.contentEquals(ModelCard.URC_TYPE)) {
                return new URCModelCard(line);
            } else if (type.contentEquals(ModelCard.LTRA_TYPE)) {
                //TODO create ltra model card
                //return new lt
                throw new ParserException("No LTRA model card yet", new UnsupportedOperationException());
            } else if (type.contentEquals(ModelCard.NPN_TYPE) || type.contentEquals(ModelCard.PNP_TYPE)) {
                level = getLevel(line);
                if (level == 0 || level == 1) {
                    return new BJT1ModelCard(line);
                } else if (level == 2) {
                    return new BJT2ModelCard(line);
                }
            } else if (type.contentEquals(ModelCard.NMOS_TYPE) || type.contentEquals(ModelCard.PMOS_TYPE)) {
                level = getLevel(line);
                if (level == 0 || level == 1) {
                    return new MOS1ModelCard(line);
                } else if (level == 2) {
                    return new MOS2ModelCard(line);
                } else if (level == 3) {
                    return new MOS3ModelCard(line);
                } else if (level == 6) {
                    return new MOS6ModelCard(line);
                } else if (level == 9) {
                    return new MOS9ModelCard(line);
                } else if (level == 4) {
                    return new BSIM1ModelCard(line);
                } else if (level == 5) {
                    return new BSIM2ModelCard(line);
                } else if (level == 8) {
                    //                      return new BSIM3ModelCard(str);
                } else if (level == 14) {
//                        return new BSIM4ModelCard(str);
                }

                /*        if(level == 0 || level == 1){
                return HFET1ModelCard.parse(str, deck);
                }else if(level == 2){
                return HFET2ModelCard.parse(str, deck);
                }
                return null;
                }*/
            } else if (type.contentEquals(ModelCard.NJF_TYPE) || type.contentEquals(ModelCard.PJF_TYPE)) {
                level = getLevel(line);
                if (level == 0 || level == 1) {
                    return new JFET1ModelCard(line);
                } else if (level == 2) {
                    return new JFET2ModelCard(line);
                }
            } else if (type.contentEquals(ModelCard.NMP_TYPE) || type.contentEquals(ModelCard.PMF_TYPE)) {
                level = getLevel(line);
                if (level == 0 || level == 1) {
                    return new MES1ModelCard(line);
                } else if (level == 2) {
                    return new MESAModelCard(line);
                }
            }
        }
        return null;
    }

    public int getLevel(String str) {
        Matcher m = LEVEL_PATTERN.matcher(str);
        if (m.find()) {
            return (int) Parser.parseDouble(m.group());
        }
        return 0;
    }

    public SourceElement createSourceElement(String str) {
        if (str.matches(ONE_NODE_V_STRING)) {
            Matcher m = ONE_NODE_V_PATTERN.matcher(str);
            if (m.find()) {
                VoltageElement elem = new VoltageElement();
                int node1Index = getNodeIndex(m.group(1));
                elem.setNode1Index(node1Index);
                srcElements.add(elem);
                return elem;
            }
        } else if (str.matches(TWO_NODE_V_STRING)) {
            Matcher m = TWO_NODE_V_PATTERN.matcher(str);
            if (m.find()) {
                VoltageElement elem = new VoltageElement();
                int node1Index = getNodeIndex(m.group(1));
                elem.setNode1Index(node1Index);
                int node2Index = getNodeIndex(m.group(2));
                elem.setNode2Index(node2Index);
                srcElements.add(elem);
                return elem;
            }
        } else if (str.matches(CURRENT_NODE_STRING)) {
            Matcher m = CURRENT_NODE_PATTERN.matcher(str);
            if (m.find()) {
                CurrentElement elem = new CurrentElement();
                elem.setContName(m.group(1));
                srcElements.add(elem);
                return elem;
            }
        }
        return null;
    }
}

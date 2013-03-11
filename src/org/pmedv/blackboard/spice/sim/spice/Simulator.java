/*
 * Simulator.java
 *
 * Created on March 25, 2006, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *Origional copyrights compiled from multiple source codes.
 *Copyright 1990 Regents of the University of California.  All rights reserved.
 **/
package org.pmedv.blackboard.spice.sim.spice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pmedv.blackboard.spice.sim.spice.analysis.ACAnalysis;
import org.pmedv.blackboard.spice.sim.spice.analysis.Analysis;
import org.pmedv.blackboard.spice.sim.spice.analysis.DcTrCurv;
import org.pmedv.blackboard.spice.sim.spice.analysis.TransientAnalysis;
import org.pmedv.blackboard.spice.sim.spice.model.SpiceCircuit;
import org.pmedv.blackboard.spice.sim.spice.model.sources.SourceElement;
import org.pmedv.blackboard.spice.sim.spice.output.DefaultOutput;
import org.pmedv.blackboard.spice.sim.spice.output.SpiceOutput;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.InstanceCard;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;
import org.pmedv.blackboard.spice.sim.spice.parse.analysis.AnalysisCard;
import org.pmedv.blackboard.spice.sim.spice.parse.discrete.MutualCard;
import org.pmedv.blackboard.spice.sim.spice.parse.output.OutputCard;
import org.pmedv.blackboard.spice.sim.spice.parse.source.SourceCard;

import javolution.util.FastList;
import javolution.util.FastTable;
import javolution.util.StandardLog;

/**
 *
 * @author AdMathLib.ministrator
 */
public class Simulator {

    String name;
    public Analysis analysis;
    FastList<Analysis> analyses = new FastList<Analysis>();
    FastList<SourceElement> srcElements = new FastList<SourceElement>();
    public final SpiceCircuit ckt = new SpiceCircuit();
    double time;
    DefaultOutput defaultOutput;

    /*
    //TODO XSpice
    FastTable<XNode> nodes;
    FastMap<String, XInstance> instTable = new FastMap<String, XInstance>();
    FastMap<String, XNode> nodeTable = new FastMap<String, XNode>();
    FastTable<Statistics> statistics = new FastTable<Statistics>();
    FastTable<XInstance> hybrids = new FastTable<XInstance>();
    FastTable<XNode> analogNodes = new FastTable<XNode>();
    FastTable<XNode> eventNodes = new FastTable<XNode>();
    FastMap<DEVICE, XDevice> deviceTable = new FastMap<DEVICE, XDevice>();
    InstanceQueue instQueue = new InstanceQueue();
    NodeQueue nodeQueue = new NodeQueue();
    OutputQueue outputQueue = new OutputQueue();
    //    FastTable<Real> hybridIndex = new FastTable<Real>);
    //    FastTable<Port> portTable = new FastTable<Port>();
    //    FastTable<Output> outputTable;

    int numHybridOutputs;
    int maxEventPasses;
    int maxOpAlternations;
    int opPasses;
     *
     */
//    FastTable<State> states;
//    boolean opAlternate;
//    public EnumMap<DEVICE, Class> devMap = new EnumMap(DEVICE.class);
    private static Simulator sim;

    /**
     * Creates a new instance of Simulator
     */
    public Simulator() {
    }

    public static Simulator getSimulator() {
        if (sim == null) {
            sim = new Simulator();
        }
        return sim;
    }

    public static void main(String args[]) {
        Simulator s = getSimulator();
        Deck deck = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            try {
                //            if (arg.equals("-f")) {
                deck = s.parseFile(new File(arg)); //            }
                //            }
            } catch (ParserException ex) {
                Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (deck != null) {
            s.assembleDeck(deck);
            s.run();
        }
    }

    public Deck parseFile(String file) throws ParserException {
        Deck deck = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(file));
            deck = new Deck();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (!deck.readLine(line)) {
                        break;
                    }
                }
            }
        } catch (java.lang.ExceptionInInitializerError ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex.getCause());
        } catch (FileNotFoundException ex) {
            throw new ParserException(ex);
        } catch (IOException ex) {
            throw new ParserException(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new ParserException(ex);
            }
        }
        return deck;
    }
    
    
    public Deck parseFile(File file) throws ParserException {
        Deck deck = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            deck = new Deck();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (!deck.readLine(line)) {
                        break;
                    }
                }
            }
        } catch (java.lang.ExceptionInInitializerError ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex.getCause());
        } catch (FileNotFoundException ex) {
            throw new ParserException(ex);
        } catch (IOException ex) {
            throw new ParserException(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new ParserException(ex);
            }
        }
        return deck;
    }

    public boolean assembleDeck(Deck deck) {
        defaultOutput = new DefaultOutput(deck.getTitle());
        FastList<InstanceCard> saveForLast = new FastList<InstanceCard>();
        FastList<InstanceCard> instCards = deck.getInstanceCards();
        for (FastList.Node<InstanceCard> n = instCards.head(),
                end = instCards.tail(); (n = n.getNext()) != end;) {
            InstanceCard icard = n.getValue();
            if (icard instanceof MutualCard || icard instanceof SourceCard) {
                saveForLast.add(icard);
            } else {
                ckt.addInstance(icard.createInstance(deck));
            }
        }
        for (FastList.Node<InstanceCard> n = saveForLast.head(),
                end = saveForLast.tail(); (n = n.getNext()) != end;) {
            InstanceCard icard = n.getValue();
            ckt.addInstance(icard.createInstance(deck));
        }
        FastTable<String> nodes = deck.getNodes();
        ckt.initNodes(nodes);
        FastList<AnalysisCard> analCards = deck.getAnalysisCards();
        for (FastList.Node<AnalysisCard> n = analCards.head(),
                end = analCards.tail(); (n = n.getNext()) != end;) {
            AnalysisCard acard = n.getValue();
            Analysis a = acard.createAnalysis();
            a.addNonConverganceEventListener(defaultOutput);
            analyses.add(a);
        }
        FastList<OutputCard> outCards = deck.getOutputCards();
        if (outCards.size() > 0) {
            for (FastList.Node<OutputCard> n = outCards.head(),
                    end = outCards.tail(); (n = n.getNext()) != end;) {
                OutputCard ocard = n.getValue();
                SpiceOutput output = ocard.createOutput(deck);
                String analType = ocard.getAnalysisString();
                for (FastList.Node<Analysis> m = analyses.head(),
                        e = analyses.tail(); (m = m.getNext()) != e;) {
                    Analysis anal = m.getValue();
                    if (analType.equals("DC") && anal instanceof DcTrCurv) {
                        anal.addAnalysisEventListener(output);
                        continue;
                    } else if (analType.equals("AC") && anal instanceof ACAnalysis) {
                        anal.addAnalysisEventListener(output);
                        continue;
                    } else if (analType.equals("TRAN") && anal instanceof TransientAnalysis) {
                        anal.addAnalysisEventListener(output);
                        continue;
                    }
                }
            }
        } else {
            for (FastList.Node<Analysis> n = analyses.head(),
                    end = analyses.tail(); (n = n.getNext()) != end;) {
                Analysis anal = n.getValue();
                anal.addAnalysisEventListener(defaultOutput);
            }
        }
        srcElements = deck.getSrcElements();
        return true;
    }

    /**
	 * @return the analyses
	 */
	public FastList<Analysis> getAnalyses() {
		return analyses;
	}

	public void run() {
        StandardLog debugLog = new StandardLog();//Logger.getLogger(Logger.GLOBAL_LOGGER_NAME));
        StandardLog.enter(debugLog);
        try {
            ckt.init();
            for (FastList.Node<SourceElement> n = srcElements.head(),
                    end = srcElements.tail(); (n = n.getNext()) != end;) {
                n.getValue().init(ckt);
            }
            ckt.temperature();
            for (FastList.Node<Analysis> n = analyses.head(),
                    end = analyses.tail(); (n = n.getNext()) != end;) {
                Analysis anal = n.getValue();
                analysis = anal;
                analysis.init(ckt);
                analysis.analyze(true);
            }
        } finally {
            StandardLog.exit(debugLog);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.output;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.output.SpiceOutput;
import org.pmedv.blackboard.spice.sim.spice.parse.Card;
import org.pmedv.blackboard.spice.sim.spice.parse.Deck;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.util.FastList;

/**
 *
 * @author owenbad
 */
public abstract class OutputCard extends Card {

    public static final String OUTPUT_PATTERN_STRING = "(\\w+)\\s+(\\w+)(.+)";//SRC_ELEMENT_PATTERN_STRING + ")+";
    public static final Pattern OUTPUT_PATTERN = Pattern.compile(OUTPUT_PATTERN_STRING);
    private String analysisString;
    private FastList<String> srcElements = new FastList<String>();
    protected String type;

    public OutputCard() {
    }

    public OutputCard(String cardString) {
        super(cardString);
        try {
            parseOutputs(cardString);
        } catch (ParserException ex) {
            Logger.getLogger(OutputCard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract SpiceOutput createOutput(Deck deck);

    public void parseOutputs(String cardString) throws ParserException {
        Matcher m = OUTPUT_PATTERN.matcher(cardString);
        if (m.find()) {
/*            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println(i + " = " + m.group(i));
            }
   */
            analysisString = m.group(2);
            m = SRC_ELEMENT_PATTERN.matcher(m.group(3));
            while (m.find()) {
                srcElements.add(m.group());
            }
        } else {
            throw new ParserException();
        }
    }

    public String getAnalysisString() {
        return analysisString;
    }

    public void setAnalysisString(String analysisString) {
        this.analysisString = analysisString;
    }

    protected void initParameters(SpiceOutput output, Deck deck) {
        for (FastList.Node<String> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            String elem = n.getValue();
            output.addElement(deck.createSourceElement(elem));
        }
    }

    public Text toText() {
        TextBuilder text = TextBuilder.newInstance();
        text.append("." + type);
        for (FastList.Node<String> n = srcElements.head(),
                end = srcElements.tail(); (n = n.getNext()) != end;) {
            String src = " " + n.getValue();
            text.append(src);
        }
        return text.toText();
    }

    @Override
    public String toString() {
        return toText().toString();
    }
}

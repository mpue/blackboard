/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.analysis.DcTrCurv;
import org.pmedv.blackboard.spice.sim.spice.parse.ParserException;

import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.util.FastTable;

/**
 *
 * @author Kristopher T. Beck
 */
public class DcTransCurveCard extends AnalysisCard {

    public static final String SRC_STRING = "(\\s*((\\w+)\\s+"
            + DOUBLE_VALUE_STRING + "\\s+" + DOUBLE_VALUE_STRING
            + "\\s+" + DOUBLE_VALUE_STRING + "))";
    public static final String PATTERN_STRING = "^\\.DC\\s+"
            + SRC_STRING + "+$";
    public static final Pattern SRC_PATTERN = Pattern.compile(SRC_STRING);
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);
    private FastTable<TranSrc> sources = new FastTable<TranSrc>();

    public DcTransCurveCard(String cardString) throws ParserException {
        super(cardString);
        parse(cardString);
    }

    public FastTable<TranSrc> getSources() {
        return sources;
    }

    public void setSources(FastTable<TranSrc> sources) {
        this.sources = sources;
    }

    @Override
    public DcTrCurv createAnalysis() {
        DcTrCurv anal = new DcTrCurv();
        DcTrCurv.TransSrcs[] srcs = new DcTrCurv.TransSrcs[sources.size()];
        for (int i = 0; i < srcs.length; i++) {
            TranSrc s = sources.get(i);
            DcTrCurv.TransSrcs src = new DcTrCurv.TransSrcs();
            src.name = s.name;
            src.start = parseDouble(s.start);
            src.stop = parseDouble(s.stop);
            src.step = parseDouble(s.step);
            srcs[i] = src;
        }
        anal.setTrvcs(srcs);
        return anal;
    }

    @Override
    public void parse(String cardString) throws ParserException {
        if (cardString.matches(PATTERN_STRING)) {
            Matcher m = SRC_PATTERN.matcher(cardString);
            while (m.find()) {
/*                for (int i = 0; i < m.groupCount(); i++) {
                    System.out.println(i + " = " + m.group(i));
                }
*/
                TranSrc src = new TranSrc();
                src.name = m.group(3);
                src.start = m.group(4);
                src.stop = m.group(13);
                src.step = m.group(22);
                sources.add(src);
            }
        } else {
            throw new ParserException();
        }
    }

    @Override
    public Text toText() {
        TextBuilder text = new TextBuilder(".TF ");
        for (int i = 0; i < sources.size(); i++) {
            TranSrc src = sources.get(i);
            text.append(" " + src.name + " " + src.start
                    + " " + src.stop + " " + src.step);
        }
        return text.toText();
    }

    public class TranSrc {

        private String name;
        private String start;
        private String stop;
        private String step;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.spice.model.Instance;
import org.pmedv.blackboard.spice.sim.spice.model.Model;

import javolution.util.FastMap;

/**
 *
 * @author owenbad
 */
public abstract class ModelCard<I extends Instance, L extends Model> extends Card {

    protected FastMap<String, String> properties = FastMap.newInstance();
    protected String type;
    protected String name;
    public static final String R_TYPE = "R";
    public static final String C_TYPE = "C";
    public static final String L_TYPE = "L";
    public static final String SW_TYPE = "SW";
    public static final String CW_TYPE = "CW";
    public static final String URC_TYPE = "URC";
    public static final String LTRA_TYPE = "LTRA";
    public static final String D_TYPE = "D";
    public static final String NPN_TYPE = "NPN";
    public static final String PNP_TYPE = "PNP";
    public static final String NJF_TYPE = "NJF";
    public static final String PJF_TYPE = "PJF";
    public static final String NMOS_TYPE = "NMOS";
    public static final String PMOS_TYPE = "PMOS";
    public static final String NMP_TYPE = "NMP";
    public static final String PMF_TYPE = "PMF";
    public static final String MODEL_STRING = "\\.MODEL\\s+(\\w+)\\s+(\\w+)\\s*\\(?(\\s*[A-Z0-9\\- \\.=]+)\\)?";
    public static final Pattern MODEL_PATTERN = Pattern.compile(MODEL_STRING);

    public ModelCard(String cardString) throws ParserException {
        super(cardString);
        parse(cardString);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FastMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(FastMap<String, String> properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void initModelValues(L model) {
        model.setModelName(name);
        for (FastMap.Entry<String, String> entry = properties.head(),
                end = properties.tail(); (entry = entry.getNext()) != end;) {
            String key = entry.getKey();
            String value = entry.getValue();
            setProperty(model, key, value);
        }
    }

    public abstract I createInstance();

    public abstract void setProperty(L model, String name, String value);

    public void parse(String cardString) throws ParserException {
        Matcher m = MODEL_PATTERN.matcher(cardString);
        if (m.find()) {
            name = m.group(1);
            type = m.group(2);
            String props = m.group(3);
            m = PROPERTY_PATTERN.matcher(m.group(3));
            while (m.find()) {
                properties.put(m.group(2), m.group(3));
            }
        } else {
            throw new ParserException();
        }
    }
}

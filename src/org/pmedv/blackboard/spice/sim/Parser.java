/*
 * Parser.java
 *
 * Created on Sep 30, 2007, 10:06:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.spice.Circuit;

import javolution.util.FastMap;

/**
 *
 * @author Kristopher T. Beck
 */
public class Parser {
    FastMap<String, Properties> models = FastMap.newInstance();
    FastMap<String, Node> nodes = FastMap.newInstance();
    Circuit ckt = new Circuit();
    
    public Parser() {
    }
    public void parse(String str){
        Matcher m;
        Pattern p = Pattern.compile("(\\s*\\n\\s*\\+\\s*)|(\\s*\\+\\n\\s*)");
        m = p.matcher(str);
        if(m.find()){
            str = m.replaceAll(" ");
        }
        p = Pattern.compile("\\n\\s*\\.model\\s+((\\w)[\\w\\d]+)(\\s+[\\w\\d]+\\s*=\\s*[\\.\\w\\d]+)");
        m = p.matcher(str);
        while(m.find()){
            Properties props = parseProperties(m.group(3));
            models.put(m.group(1), props);
        }
        p = Pattern.compile(
                "\\n\\s*((\\w)[\\w\\d]+)(\\s+[\\w\\d]+\\s*=\\s*[\\.\\w\\d]+)");
        while(m.find()){
        }
    }
    
    protected Properties parseProperties(String str){
        Matcher m = Pattern.compile("([\\w\\d]+)\\s*=\\s*([\\.\\w\\d]+)").matcher(str);
        Properties props = new Properties();
        while(m.find()){
            props.setProperty(m.group(1).toLowerCase(), m.group(2));
        }
        return props;
    }
}

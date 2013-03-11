 /* EDATerminal.java
 *
 * Created on April 20, 2007, 1:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim;

import java.awt.Color;

import javax.swing.SwingConstants;

import org.pmedv.blackboard.spice.sim.node.Node;
import org.pmedv.blackboard.spice.sim.node.NodeEvent;
import org.pmedv.blackboard.spice.sim.node.NodeListener;
import org.pmedv.blackboard.spice.sim.spice.Constants.INOUT_DIRECTION;
import org.pmedv.blackboard.spice.sim.spice.Constants.INOUT_TYPE;

import ktb.math.numbers.Complex;

/**
 *
 * @author Kristopher T. Beck
 */
public class Terminal implements NodeListener, SwingConstants {
    Node node;
    Device device;
    String name;
    Color highlight;
    boolean selected;
    Complex admitance;
    INOUT_TYPE inOutType;
    INOUT_DIRECTION ioDirection;
    int length = 10;
    int orientation = LEFT;
    /** Creates a new instance of Terminal */
    public Terminal() {
//        setBounds(0, 0, 10, 20);
    }
    
      /*  
    public void paint(Graphics2D g) {
        Color color = g.getColor();
        if(selected){
            g.setColor(highlight);
        }
        if(orientation == LEFT || orientation == RIGHT){
            g.drawLine(0, getHeight() / 2, getWidth(), getHeight()/ 2);
        }
        if(orientation == TOP || orientation == BOTTOM){
            g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        }
        g.setColor(color);
    }
*/
    public void outputUpdated(NodeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}

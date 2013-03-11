/*
 * Node.java
 *
 * Created on Jun 1, 2007, 7:49:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.node;

import org.pmedv.blackboard.spice.sim.Terminal;

import javolution.context.ObjectFactory;
import javolution.lang.Realtime;
import javolution.text.Text;
import javolution.util.FastList;
import ktb.math.numbers.Numeric;

/**
 *
 * @param N 
 * @author Kristopher T. Beck
 */
public class Node<N extends Numeric<N>>  implements Realtime {

    protected String id;
    protected int index;
    protected N value;
    protected FastList<Terminal> terminals;

    static final ObjectFactory<Node> FACTORY = new ObjectFactory<Node>() {

        protected Node create() {
            return new Node();
        }
    };

    public Node(String id, int index, N value) {
        this.id = id;
        this.index = index;
        this.value = value;
    }
    FastList<NodeListener> listeners;

    public Node() {
    }

    public static <N extends Numeric<N>>  Node valueOf(String name, int index, N value) {
        Node e = FACTORY.object();
        e.id = name;
        e.index = index;
        e.value = value;
        return e;
    }

    public Node copy() {
        Node e = FACTORY.object();
        e.value.set(value);
        return e;
    }

    /**
     *
     * @param listener
     * @return
     */
    public boolean addNodeListener(NodeListener listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(NodeListener listener) {
        return listeners.remove(listener);
    }

    protected void fireNodeEvent(NodeEvent evt) {
        for (FastList.Node<NodeListener> n = listeners.head(), end = listeners.tail();
                (n = n.getNext()) != end;) {
            n.getValue().outputUpdated(evt);
        }
    }

    /**
     *
     * @param id
     */
    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public N getValue() {
        return value;
    }

    public void setValue(N value) {
        this.value = value;
    }

    public Text toText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


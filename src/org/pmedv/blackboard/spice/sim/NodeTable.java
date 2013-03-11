/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pmedv.blackboard.spice.sim;

import java.util.Arrays;

import org.pmedv.blackboard.spice.sim.node.Node;

import javolution.util.FastList;

/**
 *
 * @author Kristopher T. Beck
 */
public class NodeTable<N extends Node> {
    N[][] nodes;
    FastList<NodeRow> rows = FastList.newInstance();
    boolean modified;
    
    public NodeTable() {
    }
    
    public void init(){
        int size = rows.size();
        nodes[0] = (N[]) new Node[size];
        nodes[1] = (N[]) new Node[size];
        nodes[2] = (N[]) new Node[size];
        modified = false;
    }
    
    public N get(int tag, int index){
        if(modified)
            init();
        return nodes[tag][index];
    }
    
    public void set(int tag, int index, N value){
        nodes[tag][index] = value;
    }
    
    public void copy(int src, int dest){
        nodes[dest] = Arrays.copyOf(nodes[src], rows.size());
    }
    
    public void swap(int st1, int st2){
        N[] tmp = nodes[st1];
        nodes[st1] = nodes[st2];
        nodes[st2] = tmp;
    }
    
    public class NodeRow{
        int tag;
        
        public N get(int index){
            if(modified)
                init();
            return nodes[tag][index];
        }
        
        public void set(int index, N value){
            nodes[tag][index] = value;
        }
    }
    
    public NodeRow createRow(){
        NodeRow row = new NodeRow();
        rows.add(row);
        row.tag = rows.size() - 1;
        modified = true;
        return row;
    }
    
    public void deleteRow(NodeRow row){
        rows.remove(row);
        int tag = 0;
        for(FastList.Node<NodeRow> r = rows.head(), end = rows.tail();
        (r = r.getNext()) != end;)
            r.getValue().tag = tag++;
        modified = true;
    }

}

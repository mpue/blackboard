/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pmedv.blackboard.spice.sim.spice.parse;

/**
 *
 * @author owenbad
 */
public class ParserException extends Exception {

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException() {
        super();
    }
}

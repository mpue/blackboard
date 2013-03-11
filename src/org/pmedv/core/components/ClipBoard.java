package org.pmedv.core.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * The <code>ClipBoard</code> simply extends the {@link Stack} class and adds a mode to it. Thus the
 * stack can be used as a clipboard with cut and copy functions.
 * 
 * @author Matthias Pueski
 * 
 */
public class ClipBoard<T> extends Stack<T> {

	public static enum Mode {
		CUT, COPY
	}

	private static final long serialVersionUID = 3947356818820543009L;

	private Mode mode;

	public ClipBoard() {
		super();
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public void copy(T item) {
		clear();
		push(item);
	}
	
	public void copyAll(List<T> items) {
		clear();
		for (T item : items)
			push(item);
	}
	
	public List<T> paste() {
		
		ArrayList<T> items = new ArrayList<T>();

		for (Iterator<T> it = iterator(); it.hasNext();) {			
			items.add(it.next());			
		}
		
		return items;
	}
	
}



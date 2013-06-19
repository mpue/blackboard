package org.pmedv.blackboard.models;

/**
 * Interface for reorderable tables
 * 
 * @author pueskma
 *
 */
public interface Reorderable {
		
	/**
	 * Reorder a given row
	 * 
	 * @param fromIndex
	 * @param toIndex
	 */
	public void reorder(int fromIndex, int toIndex);	
}

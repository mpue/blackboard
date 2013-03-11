/**

	BlackBoard BreadBoard Designer
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2010-2011 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.core.util;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Utility class for tree operations.
 * 
 * @author Matthias Pueski
 *
 */
public class TreeUtils {

	/**
	 * <p>
	 * Sets the expanded state of a tree.
	 * </p>
	 * <p>
	 * If expand is true, expands all nodes in the tree,
	 * otherwise, collapses all nodes in the tree.
	 * </p>
	 * 
	 * @param tree    the tree to operate on 
	 * @param expand  the state to set
	 */
	public static void setExpandedState(JTree tree, boolean expand) {

		TreeNode root = (TreeNode) tree.getModel().getRoot();

		// Traverse tree from root

		expandAll(tree, new TreePath(root), expand);

	}

	/** 
	 * Sets the expanded state of a tree.
	 * 
	 * If expand is true, expands all nodes in the tree,
	 * otherwise, collapses all nodes in the tree.
	 * 
	 * @param tree   the tree to operate on
	 * @param parent the parent path
	 * @param expand the state to set
	 */
	private static void expandAll(JTree tree, TreePath parent, boolean expand) {

		// Traverse children

		TreeNode node = (TreeNode) parent.getLastPathComponent();

		if (node.getChildCount() >= 0) {

			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {

				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);

			}

		}

		// Expansion or collapse must be done bottom-up

		if (expand) {
			tree.expandPath(parent);
		} 
		else {
			tree.collapsePath(parent);
		}

	}
	



	

	
	
}

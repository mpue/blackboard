package org.pmedv.core.preferences.models;

import java.util.Arrays;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * The model for the module tree of the preferences browser
 * 
 * @author Matthias Pueski
 * 
 */
public class ModuleTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = -2718325087320159934L;
	private String[] modules;

	public ModuleTreeModel(DefaultMutableTreeNode root) {
		super(root);
	}

	/**
	 * @return the nodes
	 */
	public String[] getModules() {
		return modules;
	}

	public void addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
		insertNodeInto(child, (DefaultMutableTreeNode) parent, parent.getChildCount());
		nodeChanged(parent);
		nodeStructureChanged(parent);
	}

	public void addModule(DefaultMutableTreeNode node, String module) {
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(module);
		child.setUserObject(module);
		insertNodeInto(child, (DefaultMutableTreeNode) node, node.getChildCount());
		nodeChanged(node);
		nodeStructureChanged(node);
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setModules(String[] modules, DefaultMutableTreeNode node) {
		this.modules = modules;
		Arrays.sort(modules, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				return s1.compareTo(s2);
			}
		});
		/**
		 * remove existing folders
		 */
		int childrenCount = root.getChildCount();
		for (int i = 0; i < childrenCount; i++) {
			removeNodeFromParent((DefaultMutableTreeNode) root.getChildAt(0));
		}
		nodeChanged(node);
		nodeStructureChanged(node);
		if (this.modules != null && this.modules.length > 0) {
			for (int i = 0; i < modules.length; i++) {
				if (modules[i] != null) {
					DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(modules[i]);
					rootNode.setUserObject(modules[i]);
					insertNodeInto(rootNode, (DefaultMutableTreeNode) node, node.getChildCount());
					nodeChanged(node);
					nodeStructureChanged(node);
				}
			}
		}
	}

	public ModuleTreeModel(TreeNode root) {
		super(root);
	}
}

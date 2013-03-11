package org.pmedv.core.preferences.renderer;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

/**
 * The <code>ModuleTreeRenderer</code> displays the modules in a human readable
 * way
 * 
 * @author Matthias Pueski
 * 
 */
public class ModuleTreeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
	private static final long serialVersionUID = -5216972896403506408L;
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if (value instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (node.getUserObject() instanceof String) {
				String name = (String) node.getUserObject();
				if (!name.equals("Perspectives")) {
					setText(resources.getResourceByKey(name));
					setIcon(resources.getIcon("icon." + name));
				}
			}
		}
		return this;
	}
}

package org.pmedv.blackboard;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;


/**
 * The class <code>UIUtils</code> contains convenience and helper methods
 * for the Swing UI.
 * 
 * @author Matthias Pueski
 *
 */
public class UIUtils {
	
	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	private static final ImageIcon separator = resources.getIcon("toolbar.separator");
	
	/**
	 * Creates a visible toolbar separator (yes!)
	 * 
	 * @return a label with an icon which looks like a separator
	 */
	public static JLabel createToolbarSeparator() {
		JLabel l = new JLabel(separator);
		return l;
	}
	
	/**
	 * Flattens the border of a <code>JSplitPane</code>
	 * 
	 * @param jSplitPane
	 */
	public static void flattenSplitPane(JSplitPane jSplitPane) {
		jSplitPane.setUI(new BasicSplitPaneUI() {

			@Override
			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {

					private static final long serialVersionUID = 6618661343340588167L;

					@Override
					public void setBorder(Border b) {
					}
				};
			}
		});
		jSplitPane.setBorder(null);
	}
	
}

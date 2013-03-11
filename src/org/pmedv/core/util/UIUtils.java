package org.pmedv.core.util;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


/**
 * The class <code>UIUtils</code> contains convenience and helper methods
 * for the Swing UI.
 * 
 * @author Matthias Pueski
 *
 */
public class UIUtils {
	
	/**
	 * Flattens the border of a <code>JSplitPane</code>
	 * 
	 * @param jSplitPane
	 */
	public static void flattenSplitPane(JSplitPane jSplitPane) {
		jSplitPane.setUI(new BasicSplitPaneUI() {

			public BasicSplitPaneDivider createDefaultDivider() {
				return new BasicSplitPaneDivider(this) {

					public void setBorder(Border b) {
					}
				};
			}
		});
		jSplitPane.setBorder(null);
	}
	
}

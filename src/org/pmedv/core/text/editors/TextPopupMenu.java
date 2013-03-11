package org.pmedv.core.text.editors;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;

import org.pmedv.core.context.AppContext;
import org.pmedv.core.services.ResourceService;

/**
 * This is the default {@link JPopupMenu} for the {@link TextEditor}
 * 
 * @author Matthias Pueski
 * 
 */
public class TextPopupMenu extends JPopupMenu {

	private static final ResourceService resources = AppContext.getContext().getBean(ResourceService.class);
	private static final long serialVersionUID = -358480533871291692L;

	private JTextComponent jTextComponent;
	private String cut;
	private String copy;
	private String paste;
	private JMenuItem cutItem;
	private JMenuItem copyItem;
	private JMenuItem pasteItem;

	public TextPopupMenu(JTextComponent jTextComponent) {
		this(jTextComponent, resources.getResourceByKey("icon.cut"), resources.getResourceByKey("icon.copy"), resources
				.getResourceByKey("icon.paste"));
	}

	public TextPopupMenu(JTextComponent jTextComponent, String cut, String copy, String paste) {
		super();
		this.jTextComponent = jTextComponent;
		this.cut = cut;
		this.copy = copy;
		this.paste = paste;
		jTextComponent.add(this);

		MouseListener listener = new MouseListener();
		jTextComponent.addMouseListener(listener);

		init();
	}

	private void init() {
		MouseListener listener = new MouseListener();

		cutItem = new JMenuItem(cut, resources.getIcon("icon.popupmenu.cut"));
		cutItem.addActionListener(listener);
		add(cutItem);

		copyItem = new JMenuItem(copy, resources.getIcon("icon.popupmenu.copy"));
		copyItem.addActionListener(listener);
		add(copyItem);

		pasteItem = new JMenuItem(paste, resources.getIcon("icon.popupmenu.paste"));
		pasteItem.addActionListener(listener);
		add(pasteItem);

		resetItem();
	}

	private void resetItem() {
		boolean isTestSel = jTextComponent.getSelectedText() != null;
		boolean isEditable = jTextComponent.isEditable();
		cutItem.setEnabled(isTestSel && isEditable);
		copyItem.setEnabled(isTestSel);
		pasteItem.setEnabled(isEditable);
	}

	private void copy() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		TransferHandler transferHandler = jTextComponent.getTransferHandler();
		transferHandler.exportToClipboard(jTextComponent, clipboard, TransferHandler.COPY);
	}

	private void paste() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		TransferHandler transferHandler = jTextComponent.getTransferHandler();
		transferHandler.importData(jTextComponent, clipboard.getContents(null));
	}

	private class MouseListener extends MouseAdapter implements ActionListener {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				resetItem();
				Point point = jTextComponent.getMousePosition();
				if (point != null)
					show(jTextComponent, point.x, point.y);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				resetItem();
				Point point = jTextComponent.getMousePosition();
				if (point != null)
					show(jTextComponent, point.x, point.y);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String source = e.getActionCommand();
			if (source.equals(copy))
				copy();

			else if (source.equals(paste))
				paste();

			else if (source.equals(cut)) {
				copy();
				jTextComponent.replaceSelection("");
			}
		}
	}
}
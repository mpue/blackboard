package org.pmedv.blackboard.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class FillViewportHeightDemo extends JFrame implements ActionListener {

	private DefaultListModel model = new DefaultListModel();
	private int count = 0;
	private JTable table;
	private JCheckBoxMenuItem fillBox;
	private DefaultTableModel tableModel;

	private static String getNextString(int count) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 5; i++) {
			buf.append(String.valueOf(count));
			buf.append(",");
		}

		// remove last newline
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	private static DefaultTableModel getDefaultTableModel() {
		String[] cols = { "Foo", "Toto", "Kala", "Pippo", "Boing" };
		return new DefaultTableModel(null, cols);
	}

	public FillViewportHeightDemo() {
		super("Empty Table DnD Demo");

		tableModel = getDefaultTableModel();
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setDropMode(DropMode.INSERT_ROWS);

		table.setTransferHandler(new TransferHandler() {

			public boolean canImport(TransferSupport support) {
				// for the demo, we'll only support drops (not clipboard paste)
				if (!support.isDrop()) {
					return false;
				}

				// we only import Strings
				if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					return false;
				}

				return true;
			}

			public boolean importData(TransferSupport support) {
				// if we can't handle the import, say so
				if (!canImport(support)) {
					return false;
				}

				// fetch the drop location
				JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();

				int row = dl.getRow();

				// fetch the data and bail if this fails
				String data;
				try {
					data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
				} catch (UnsupportedFlavorException e) {
					return false;
				} catch (IOException e) {
					return false;
				}

				String[] rowData = data.split(",");
				tableModel.insertRow(row, rowData);

				Rectangle rect = table.getCellRect(row, 0, false);
				if (rect != null) {
					table.scrollRectToVisible(rect);
				}

				// demo stuff - remove for blog
				model.removeAllElements();
				model.insertElementAt(getNextString(count++), 0);
				// end demo stuff

				return true;
			}
		});

		JList dragFrom = new JList(model);
		dragFrom.setFocusable(false);
		dragFrom.setPrototypeCellValue(getNextString(100));
		model.insertElementAt(getNextString(count++), 0);
		dragFrom.setDragEnabled(true);
		dragFrom.setBorder(BorderFactory.createLoweredBevelBorder());

		dragFrom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (SwingUtilities.isLeftMouseButton(me) && me.getClickCount() % 2 == 0) {
					String text = (String) model.getElementAt(0);
					String[] rowData = text.split(",");
					tableModel.insertRow(table.getRowCount(), rowData);
					model.removeAllElements();
					model.insertElementAt(getNextString(count++), 0);
				}
			}
		});

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		JPanel wrap = new JPanel();
		wrap.add(new JLabel("Drag from here:"));
		wrap.add(dragFrom);
		p.add(Box.createHorizontalStrut(4));
		p.add(Box.createGlue());
		p.add(wrap);
		p.add(Box.createGlue());
		p.add(Box.createHorizontalStrut(4));
		getContentPane().add(p, BorderLayout.NORTH);

		JScrollPane sp = new JScrollPane(table);
		getContentPane().add(sp, BorderLayout.CENTER);
		fillBox = new JCheckBoxMenuItem("Fill Viewport Height");
		fillBox.addActionListener(this);

		JMenuBar mb = new JMenuBar();
		JMenu options = new JMenu("Options");
		mb.add(options);
		setJMenuBar(mb);

		JMenuItem clear = new JMenuItem("Reset");
		clear.addActionListener(this);
		options.add(clear);
		options.add(fillBox);

		getContentPane().setPreferredSize(new Dimension(260, 180));
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == fillBox) {
			table.setFillsViewportHeight(fillBox.isSelected());
		} else {
			tableModel.setRowCount(0);
			count = 0;
			model.removeAllElements();
			model.insertElementAt(getNextString(count++), 0);
		}
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		FillViewportHeightDemo test = new FillViewportHeightDemo();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		test.pack();
		test.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}

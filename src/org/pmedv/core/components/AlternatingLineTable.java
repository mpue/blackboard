package org.pmedv.core.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;
@SuppressWarnings("unused")
public class AlternatingLineTable extends JXTable {
	
	private static final long serialVersionUID = -3221298344485063709L;
	
	private static final Color lightGray   = new Color(230, 230, 230);
	private static final Color lightOrange = new Color(243, 152, 100);
	private static final Color lightBlue   = new Color(139, 171, 225);
	private static final Color blackboardBlue = new Color(84, 113, 167);
	
	public AlternatingLineTable() {
		super();
	}
	
	public AlternatingLineTable(TableModel model) {
		super(model);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		
		Component c = super.prepareRenderer(renderer, row, column);

		if (row % 2 == 0 && !isCellSelected(row, column)) {
			c.setBackground(Color.DARK_GRAY);
		}
		else {
			if (isCellSelected(row, column)) {
				c.setBackground(blackboardBlue);
				c.setForeground(Color.WHITE);
			}
			else {
				c.setBackground(getBackground());
				c.setForeground(getForeground());
			}
		}
		
		return c;

	}
	
}

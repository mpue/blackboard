/**

	BlackBoard breadboard designer
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
package org.pmedv.blackboard.provider;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.beans.SymbolList;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.core.util.ImageUtils;

/**
 * <p>
 * The <code>SymbolProvider</code> provides access to the {@link SymbolList}.
 * </p>
 * <p>
 * It allows to load and store the {@link SymbolList} as well as adding and
 * removing {@link SymbolBean} objects.
 * </p>
 * <p>
 * This is a spring managed bean and is handled as a singleton.
 * </p>
 * 
 * @author
 * 
 */
public class SymbolProvider extends AbstractElementProvider<SymbolBean> {

	private HashMap<String, ImageIcon> symbolMap;

	public SymbolProvider() {
		super(SymbolBean.class, "symbols");
		symbolMap = new HashMap<String, ImageIcon>();
	}

	private final ArrayList<String> categories = new ArrayList<String>();

	@Override
	public void loadElements() {
		// TODO Auto-generated method stub
		super.loadElements();
		for (SymbolBean sym : getElements()) {
			addIcon(sym);
			if (!categories.contains(sym.getCategory())) {
				categories.add(sym.getCategory());
			}
		}

	}

	@Override
	public void addElement(SymbolBean t) throws Exception {
		super.addElement(t);
		if (!categories.contains(t.getCategory())) {
			categories.add(t.getCategory());
		}
		addIcon(t);
	}

	@Override
	public void removeElement(SymbolBean t) throws Exception {
		super.removeElement(t);
		symbolMap.remove(t);
	}

	@Override
	public void storeElement(SymbolBean t) throws Exception {
		// new symbol
		if (t.getFilename() == null) {
			t.setFilename(t.getName().replaceAll(" ", "") + ".xml");
		}

		super.storeElement(t);
	}

	private void addIcon(SymbolBean sym) {
		BufferedImage b = new BufferedImage(sym.getWidth() + 10, sym.getHeight() + 10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = (Graphics2D) b.createGraphics();
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHints(rh);
		Symbol s = new Symbol(sym);
		s.draw(graphics,true,false,false,false,false);
		graphics.dispose();

		float proportion = 0.75f;

		if (sym.getHeight() > 128) {
			proportion = (float) 128 / (float) sym.getHeight();
			proportion *= 0.75;
		}

		b = ImageUtils.scale(b, proportion);
		symbolMap.put(sym.getName(), new ImageIcon(b));
	}

	public HashMap<String, ImageIcon> getSymbolMap() {
		return symbolMap;
	}

	/**
	 * @return the categories
	 */
	public ArrayList<String> getCategories() {
		return categories;
	}

}

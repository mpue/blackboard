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
package org.pmedv.blackboard;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.View;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.blackboard.app.EditorMode;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.beans.SymbolBean;
import org.pmedv.blackboard.commands.AddDiodeCommand;
import org.pmedv.blackboard.commands.AddItemEdit;
import org.pmedv.blackboard.commands.AddResistorCommand;
import org.pmedv.blackboard.commands.AddTextCommand;
import org.pmedv.blackboard.commands.BreakSymbolCommand;
import org.pmedv.blackboard.commands.BrowsePartsCommand;
import org.pmedv.blackboard.commands.ConvertToPartCommand;
import org.pmedv.blackboard.commands.ConvertToSymbolCommand;
import org.pmedv.blackboard.commands.CreatePartListCommand;
import org.pmedv.blackboard.commands.CreateRatsnestCommand;
import org.pmedv.blackboard.commands.CreateScaleCommand;
import org.pmedv.blackboard.commands.EditPropertiesCommand;
import org.pmedv.blackboard.commands.ExportImageCommand;
import org.pmedv.blackboard.commands.MoveToLayerCommand;
import org.pmedv.blackboard.commands.PrintBoardCommand;
import org.pmedv.blackboard.commands.RedoCommand;
import org.pmedv.blackboard.commands.RotateCCWCommand;
import org.pmedv.blackboard.commands.RotateCWCommand;
import org.pmedv.blackboard.commands.SaveAsCommand;
import org.pmedv.blackboard.commands.SaveBoardCommand;
import org.pmedv.blackboard.commands.SelectAllCommand;
import org.pmedv.blackboard.commands.SelectAllLinesCommand;
import org.pmedv.blackboard.commands.SetColorCommand;
import org.pmedv.blackboard.commands.SetConnectionCheckModeCommand;
import org.pmedv.blackboard.commands.SetDrawEllipseModeCommand;
import org.pmedv.blackboard.commands.SetDrawMeasureModeCommand;
import org.pmedv.blackboard.commands.SetDrawModeCommand;
import org.pmedv.blackboard.commands.SetDrawRectangleModeCommand;
import org.pmedv.blackboard.commands.SetMoveModeCommand;
import org.pmedv.blackboard.commands.SetSelectModeCommand;
import org.pmedv.blackboard.commands.ShowLayersCommand;
import org.pmedv.blackboard.commands.SimulateCircuitCommand;
import org.pmedv.blackboard.commands.ToggleGridCommand;
import org.pmedv.blackboard.commands.ToggleMagneticCommand;
import org.pmedv.blackboard.commands.ToggleMirrorCommand;
import org.pmedv.blackboard.commands.ToggleSnapToGridCommand;
import org.pmedv.blackboard.commands.UndoCommand;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Layer;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Pin;
import org.pmedv.blackboard.components.Symbol;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.panels.CenterPanel;
import org.pmedv.core.components.CmdJButton;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.perspectives.AbstractPerspective;
import org.pmedv.core.provider.ApplicationToolbarProvider;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * This is a static helper class, which contains some convenience methods for
 * editor, view and perspective handling. Actually this sits on top of the
 * InfoNode docking window framework and performs some recursive tasks to
 * control the docking windows.
 * </p>
 * <p>
 * <b>TODO : Read this!</b> Notice to myself : We should make this much more
 * generic, such that any type of an abstract editor kind can be queried.
 * </p>
 * 
 * @author Matthias Pueski (29.05.2011)
 * 
 */
public class EditorUtils {

	private static final Log log = LogFactory.getLog(EditorUtils.class);

	private static final ApplicationContext ctx = AppContext.getContext();

	/**
	 * Gets the current selected and active editor
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public static BoardEditor getCurrentActiveEditor() {
		final ApplicationContext ctx = AppContext.getContext();
		final ApplicationWindowAdvisor advisor = ctx.getBean(ApplicationWindowAdvisor.class);
		View view = (View) advisor.getCurrentEditorArea().getSelectedWindow();
		if (view != null) {
			if (!(view.getComponent() instanceof JScrollPane))
				return null;
			JScrollPane pane = (JScrollPane) view.getComponent();
			if (!(pane.getViewport().getComponent(0) instanceof CenterPanel))
				return null;
			CenterPanel panel = (CenterPanel) pane.getViewport().getComponent(0);
			BoardEditor editor = panel.getBoardEditor();
			return editor;
		}
		return null;
	}

	/**
	 * Gets a list of all open {@link BoardEditor} views for a specified docking
	 * window. This method runs recursively and usually needs a given
	 * {@link RootWindow}
	 * 
	 * @param editors a list of {@link BoardEditor} objects, might be empty on
	 *        first call.
	 * @param win
	 */
	private static void getOpenEditors(ArrayList<BoardEditor> editors, DockingWindow win) {
		for (int i = 0; i < win.getChildWindowCount(); i++) {
			if (win.getChildWindow(i) instanceof View) {
				final View view = (View) win.getChildWindow(i);
				if (view != null) {
					if (!(view.getComponent() instanceof JScrollPane))
						return;
					JScrollPane pane = (JScrollPane) view.getComponent();
					if (!(pane.getViewport().getComponent(0) instanceof CenterPanel))
						return;
					CenterPanel panel = (CenterPanel) pane.getViewport().getComponent(0);
					BoardEditor editor = panel.getBoardEditor();
					editors.add(editor);
				}
			}
			else
				getOpenEditors(editors, win.getChildWindow(i));
		}
	}

	/**
	 * Gets a list of all open views for a specified docking window. This method
	 * runs recursively and usually needs a given {@link RootWindow}
	 * 
	 * @param editors a list of {@link BoardEditor} objects, might be empty on
	 *        first call.
	 * @param win
	 */
	private static void getOpenViews(ArrayList<View> views, DockingWindow win) {
		for (int i = 0; i < win.getChildWindowCount(); i++) {
			if (win.getChildWindow(i) instanceof View) {
				final View view = (View) win.getChildWindow(i);
				if (view != null) {
					views.add(view);
				}
			}
			else
				getOpenViews(views, win.getChildWindow(i));
		}
	}

	/**
	 * Gets the first available view for an {@link AbstractPerspective}
	 * 
	 * @param a the perspective to get the first {@link View} for.
	 * @return
	 */
	public static View getFirstAvailableView(AbstractPerspective a) {
		ArrayList<View> openViews = getCurrentPerspectiveViews(a);
		if (openViews.size() > 0)
			return openViews.get(0);
		else
			return null;
	}

	/**
	 * Gets all open editors for a given {@link AbstractPerspective}
	 * 
	 * @param a the perspective to get the open editors for
	 * 
	 * @return a list of {@link BoardEditor} objects
	 */
	public static ArrayList<BoardEditor> getCurrentPerspectiveOpenEditors(AbstractPerspective a) {
		ArrayList<BoardEditor> openEditors = new ArrayList<BoardEditor>();
		for (int i = 0; i < a.getRootWindow().getChildWindowCount(); i++) {
			getOpenEditors(openEditors, a.getRootWindow().getChildWindow(i));
		}
		return openEditors;
	}

	/**
	 * gets all {@link View} objects for a given {@link AbstractPerspective}
	 * 
	 * @param a the perspective to get the views for
	 * 
	 * @return a list of {@link View} objects.
	 */
	public static ArrayList<View> getCurrentPerspectiveViews(AbstractPerspective a) {
		ArrayList<View> openViews = new ArrayList<View>();
		if (a != null)
			for (int i = 0; i < a.getRootWindow().getChildWindowCount(); i++) {
				getOpenViews(openViews, a.getRootWindow().getChildWindow(i));
			}
		return openViews;
	}

	/**
	 * Updates the toolbar button states.
	 * 
	 * @param editor
	 */
	public static void setToolbarButtonState(BoardEditor editor) {
		final ApplicationContext ctx = AppContext.getContext();
		final ApplicationToolbarProvider provider = (ApplicationToolbarProvider) ctx.getBean("toolBarProvider");
		JToolBar toolbar = provider.getToolbar();
		for (int i = 0; i < toolbar.getComponentCount(); i++) {
			if (toolbar.getComponent(i) instanceof CmdJButton) {
				CmdJButton button = (CmdJButton) toolbar.getComponent(i);
				if (button.getAction() instanceof SetDrawModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.DRAW_LINE));
				}
				if (button.getAction() instanceof SetSelectModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.SELECT));
				}
				if (button.getAction() instanceof SetDrawRectangleModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.DRAW_RECTANGLE));
				}
				if (button.getAction() instanceof SetDrawEllipseModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.DRAW_ELLIPSE));
				}
				if (button.getAction() instanceof SetConnectionCheckModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.CHECK_CONNECTIONS));
				}
				if (button.getAction() instanceof SetDrawMeasureModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.DRAW_MEASURE));
				}
				if (button.getAction() instanceof SetMoveModeCommand) {
					button.setBorderPainted(editor.getEditorMode().equals(EditorMode.MOVE));
				}

			}
		}
	}

	public static void registerEditorListeners(BoardEditor editor) {
		final ApplicationContext ctx = AppContext.getContext();
		editor.addEditorChangeListener(ctx.getBean(ShowLayersCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ApplicationWindow.class));
		editor.addEditorChangeListener(ctx.getBean(UndoCommand.class));
		editor.addEditorChangeListener(ctx.getBean(RedoCommand.class));
		editor.addEditorChangeListener(ctx.getBean(MoveToLayerCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ToggleGridCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ToggleSnapToGridCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ToggleMirrorCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ToggleMagneticCommand.class));
		editor.addEditorChangeListener(ctx.getBean(CreateRatsnestCommand.class));
		editor.addEditorChangeListener(ctx.getBean(AddResistorCommand.class));
		editor.addEditorChangeListener(ctx.getBean(AddDiodeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ExportImageCommand.class));
		editor.addEditorChangeListener(ctx.getBean(CreatePartListCommand.class));
		editor.addEditorChangeListener(ctx.getBean(PrintBoardCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetColorCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetDrawModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SaveAsCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetSelectModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetDrawRectangleModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetMoveModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetDrawEllipseModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetConnectionCheckModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SetDrawMeasureModeCommand.class));
		editor.addEditorChangeListener(ctx.getBean(AddTextCommand.class));
		editor.addEditorChangeListener(ctx.getBean(EditPropertiesCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SelectAllCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SelectAllLinesCommand.class));
		editor.addEditorChangeListener(ctx.getBean(CreateScaleCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ConvertToPartCommand.class));
		editor.addEditorChangeListener(ctx.getBean(MoveToLayerCommand.class));
		editor.addEditorChangeListener(ctx.getBean(RotateCWCommand.class));
		editor.addEditorChangeListener(ctx.getBean(RotateCCWCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SimulateCircuitCommand.class));
		editor.addEditorChangeListener(ctx.getBean(BrowsePartsCommand.class));
		editor.addEditorChangeListener(ctx.getBean(SaveBoardCommand.class));
		editor.addEditorChangeListener(ctx.getBean(BreakSymbolCommand.class));
		editor.addEditorChangeListener(ctx.getBean(ConvertToSymbolCommand.class));		
	}

	/**
	 * Initializes the toolbar
	 */
	public static void initToolbar() {
		ApplicationToolbarProvider provider = ctx.getBean(ApplicationToolbarProvider.class);
		JToolBar toolbar = provider.getToolbar();
		for (int i = 0; i < toolbar.getComponentCount(); i++) {
			if (toolbar.getComponent(i) instanceof CmdJButton) {
				CmdJButton button = (CmdJButton) toolbar.getComponent(i);
				if (button.getAction() instanceof SetDrawModeCommand || button.getAction() instanceof SetSelectModeCommand || button.getAction() instanceof SetDrawRectangleModeCommand
						|| button.getAction() instanceof SetDrawEllipseModeCommand || button.getAction() instanceof SetConnectionCheckModeCommand
						|| button.getAction() instanceof SetDrawMeasureModeCommand || button.getAction() instanceof ToggleMagneticCommand || button.getAction() instanceof ToggleGridCommand
						|| button.getAction() instanceof ToggleMirrorCommand || button.getAction() instanceof ToggleSnapToGridCommand
						|| button.getAction() instanceof SetMoveModeCommand)
					button.setShowFeedback(false);
				if (button.getAction() instanceof SetDrawModeCommand)
					button.setBorderPainted(true);
			}
		}
	}

	/**
	 * Configures the drop target for new symbols to be dragged from the symbol
	 * table to the editor.
	 */
	public static DropTarget configureDropTarget(final BoardEditor editor, final int raster) {

		DropTarget dt = new DropTarget(editor, new DropTargetAdapter() {

			final DataFlavor flavors[] = { DataFlavor.imageFlavor };

			@Override
			public void drop(DropTargetDropEvent e) {

				int xloc = BoardUtil.snap((int) e.getLocation().getX(), raster);
				int yloc = BoardUtil.snap((int) e.getLocation().getY(), raster);

				SymbolBean symbol = null;
				try {
					symbol = (SymbolBean) e.getTransferable().getTransferData(flavors[0]);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}

				Symbol sym = new Symbol(symbol);

				sym.setXLoc(xloc);
				sym.setYLoc(yloc);

				for (Item subItem : sym.getItems()) {

					if (subItem instanceof Line) {
						Line line = (Line) subItem;
						line.setStart(new Point((int) line.getStart().getX() + xloc, (int) line.getStart().getY() + yloc));
						line.setEnd(new Point((int) line.getEnd().getX() + +xloc, (int) line.getEnd().getY() + yloc));
						line.setOldstart(new Point(line.getStart()));
						line.setOldEnd(new Point(line.getEnd()));
					}
					else {
						subItem.setXLoc(subItem.getXLoc() + xloc);
						subItem.setYLoc(subItem.getYLoc() + yloc);
						subItem.setOldXLoc(subItem.getXLoc());
						subItem.setOldYLoc(subItem.getYLoc());
						subItem.setOldWidth(subItem.getWidth());
						subItem.setOldHeight(subItem.getHeight());
					}

				}

				editor.getModel().getCurrentLayer().getItems().add(sym);
				sym.setLayer(editor.getModel().getCurrentLayer().getIndex());
				editor.refresh();
				e.dropComplete(true);
				e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

				// Switch to select mode after drop
				ctx.getBean(SetSelectModeCommand.class).execute(null);

				if (!editor.getUndoManager().addEdit(new AddItemEdit(sym))) {
					log.info("Could not add edit " + this.getClass());
				}
				editor.setFileState(FileState.DIRTY);

				ctx.getBean(RedoCommand.class).setEnabled(editor.getUndoManager().canRedo());
				ctx.getBean(UndoCommand.class).setEnabled(editor.getUndoManager().canUndo());
			}

		});

		return dt;

	}
	
	/**
	 * Finds the mouse hovered {@link Pin} for the current {@link BoardEditor}
	 * 
	 * @param e The mouse event to find the pin for
	 * @param editor the current {@link BoardEditor}
	 * @return the pin, or null if no {@link Pin} is being hovered
	 * 
	 */
	public static Pin findPin(int x, int y, BoardEditor editor) {
		
		
		
		for (Layer layer : editor.getModel().getLayers()) {
			
			for (Item item : layer.getItems()) {
					
				if (item instanceof Part) {
					
					Part p = (Part)item;
					
					
					for (Pin pin : p.getConnections().getPin()) {

						Point point = new Point(pin.getX(), pin.getY());
						
						int rot	= p.getRotation();
						
						if (rot != 0) {
							
							if (rot == 90 || rot == 270) {
								rot -= 180;
							}
							
							point = BoardUtil.rotatePoint((int)point.getX(), (int)point.getY(), 0, 0, rot);
						}
						
						if (x >= (item.getXLoc() + point.getX() - 3) && 
							x <= (item.getXLoc() + point.getX() + 3) && 
							y >= (item.getYLoc() + point.getY() - 3) && 
							y <= (item.getYLoc() + point.getY()) + 3) {
							return pin;
						}
						
					}
					
				}
				
			}
			
		}
		
		return null;
	}
	
	/**
	 * Finds the mouse hovered {@link Line} for the current {@link BoardEditor}
	 * 
	 * @param e The mouse event to find the line for
	 * @param editor the current {@link BoardEditor}
	 * @return the line, or null if no {@link Line} is being hovered
	 * 
	 */
	public static Line findMouseOverLine(MouseEvent e, BoardEditor editor) {

		Point point = new Point(e.getX(), e.getY());

		for (Layer layer : editor.getModel().getLayers()) {

			for (Item item : layer.getItems()) {
				
				if (item instanceof Line) {
					Line line = (Line) item;
					
					if (line.containsPoint(point)) {
						return line;
					}					
				}

			}

		}

		return null;
	}
	
	/**
	 * Determines the next free item index for a given {@link BoardEditorModel}
	 * 
	 * @param model the model to check for the index
	 * 
	 * @return the next free index.
	 */
	public static int getFreeIndex(BoardEditorModel model) {
		/**
		 * Determine next free index
		 */
		int maxIndex = 0;
		for (Layer layer : model.getLayers()) {
			for (Item item : layer.getItems()) {
				if (item.getIndex() > maxIndex) {
					maxIndex = item.getIndex();
				}
			}
		}
		maxIndex++;
		return maxIndex;
	}

	
}

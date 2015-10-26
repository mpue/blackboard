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
package org.pmedv.blackboard.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import net.infonode.docking.View;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.plaf.ext.transform.DefaultTransformModel;
import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.ShapeUtil;
import org.pmedv.blackboard.app.EditorMode;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.app.SelectionState;
import org.pmedv.blackboard.commands.AddItemCommand;
import org.pmedv.blackboard.commands.AddResistorCommand;
import org.pmedv.blackboard.commands.AddSymbolToLibraryCommand;
import org.pmedv.blackboard.commands.AddTextCommand;
import org.pmedv.blackboard.commands.BreakSymbolCommand;
import org.pmedv.blackboard.commands.BrowsePartsCommand;
import org.pmedv.blackboard.commands.ConvertToPartCommand;
import org.pmedv.blackboard.commands.ConvertToSymbolCommand;
import org.pmedv.blackboard.commands.CopyCommand;
import org.pmedv.blackboard.commands.DeleteCommand;
import org.pmedv.blackboard.commands.DuplicateCommand;
import org.pmedv.blackboard.commands.EditPropertiesCommand;
import org.pmedv.blackboard.commands.ExportImageCommand;
import org.pmedv.blackboard.commands.FlipHorizontalCommand;
import org.pmedv.blackboard.commands.FlipVerticalCommand;
import org.pmedv.blackboard.commands.MoveItemEdit;
import org.pmedv.blackboard.commands.MoveLineEdit;
import org.pmedv.blackboard.commands.MoveMultipleItemsEdit;
import org.pmedv.blackboard.commands.MoveToLayerCommand;
import org.pmedv.blackboard.commands.PasteCommand;
import org.pmedv.blackboard.commands.RedoCommand;
import org.pmedv.blackboard.commands.RotateCCWCommand;
import org.pmedv.blackboard.commands.RotateCWCommand;
import org.pmedv.blackboard.commands.SaveBoardCommand;
import org.pmedv.blackboard.commands.SetColorCommand;
import org.pmedv.blackboard.commands.SetDrawModeCommand;
import org.pmedv.blackboard.commands.SetSelectModeCommand;
import org.pmedv.blackboard.commands.SimulateCircuitCommand;
import org.pmedv.blackboard.commands.ToggleGridCommand;
import org.pmedv.blackboard.commands.ToggleMirrorCommand;
import org.pmedv.blackboard.commands.ToggleSnapToGridCommand;
import org.pmedv.blackboard.commands.UndoCommand;
import org.pmedv.blackboard.components.WireConnection.WireConnectionType;
import org.pmedv.blackboard.events.EditorChangedEvent;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.events.EditorChangedListener;
import org.pmedv.blackboard.models.BoardEditorModel;
import org.pmedv.blackboard.models.BoardEditorModel.BoardType;
import org.pmedv.blackboard.panels.ShapePropertiesPanel;
import org.pmedv.blackboard.provider.SimulatorProvider;
import org.pmedv.blackboard.spice.SpiceSimulator;
import org.pmedv.blackboard.tools.BoardGen;
import org.pmedv.blackboard.tools.ConnectionUtils;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindow;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * This is the main editor component of blackboard. 
 * <p>
 * This component was never meant to be so big and complicated. Since
 * it only started as a small &quot;just for fun&quot; project. 
 * Therefore some parts of this components are a little bit hacky ond obscure at the moment, sorry.
 * <p>
 * At the moment we should not extend its functionality anymore and
 * leave the editor just as it is for version 1.0 and only fix pending bugs if possible.
 * </p>
 * <p>
 * For a later version a big rewrite is planned which should take concern
 * of the following aspects:
 * </p>
 * <p>
 * <ul>
 * <li>
 * The editor should have a real state machine to take track of the current editor
 * state, like selection behaviour, moving, rotating copying and deleting elements.
 * </li>
 * <li>
 * The keyboard movement of parts does not really work at the moment.
 * </li>
 * <li>
 * Elements should be rotateable freely instead of 90 degree steps.
 * </li>
 * <li>
 * The labels of any element should be moveable in some way.
 * </li>
 * <li>
 * There should be a real print preview and page setup for the selected
 * board. It should be taken care of the real dimensions of the board
 * in order to be able to print a well sized board.
 * </li>
 * <li>
 * The mechanism to remember the last position of any component is a big mess
 * </li>
 * <li>We should not distinguish between a single selected item and a list of selected
 * items.</li>
 * </ul>
 * </p>
 * <p>
 * Maybe if anything of the above has been well rewritten, Blackboard could be extended such
 * that real PCB layouts are possible (a PSpice integration would be also very nice ;) ).
 * </p>
 * 
 * @author Matthias Pueski
 *
 */
@SuppressWarnings({ "unused", "unchecked" })
public class BoardEditor extends JPanel implements MouseMotionListener{
	private JXLayer<?> zoomLayer;
	private static final long serialVersionUID = -6034450048001248857L;
	private static final Log log = LogFactory.getLog(BoardEditor.class);
	// blank background color
	private static final Color BLANK = new Color(1.0f,1.0f,1.0f,0.0f);
	// raster size
	private int raster = 16;
	// some convenience objects
	private ApplicationContext ctx = AppContext.getContext();
	private ApplicationWindow win = ctx.getBean(ApplicationWindow.class);
	private ResourceService resources = ctx.getBean(ResourceService.class);
	private Palette palette = ctx.getBean(Palette.class);
	// current mode
	private EditorMode editorMode;
	// popup for the editor
	private JPopupMenu popupMenu;
	// we need an addlinecommand
	private AddItemCommand addItemCommand;
	private DeleteCommand deleteCommand;
	private SaveBoardCommand saveBoardCommand;
	// is the part being edited?
	private boolean editPart = false;
	// is the drag inside any handle?
	private boolean draggingStartHandle = false;
	private boolean draggingEndHandle = false;
	private boolean draggingNorthWestEdge = false;
	private boolean draggingNorthEastEdge = false;
	private boolean draggingSouthWestEdge = false;
	private boolean draggingSouthEastEdge = false;
	// grid issues
	boolean snapToGrid = true;
	boolean gridVisible = false;
	// are the symbols magnetic?
	private boolean magnetic = false;
	// which button is being pressed?
	private boolean button1Pressed = false;
	private boolean button2Pressed = false;
	private boolean button3Pressed = false;
	// current mouse coordinates
	private int mouseX;
	private int mouseY;	
	// where did the drag of the mouse happen?
	private int dragStartX;
	private int dragStartY;
	// where was the dragging stopped?
	private int dragStopX;
	private int dragStopY;
	// start of the line in case of a paint event
	private int lineStartX;
	private int lineStartY;
	// stop of the line being painted
	private int lineStopX;
	private int lineStopY;
	// where did the context click happen?
	private int contextClickX = 0;
	private int contextClickY = 0;
	// the current selected and moving item
	private Item currentMovingItem;
	// the selected item
	private Item selectedItem;
	// background of the board
	private Image backgroundImage;
	private String backgroundImageName;
	// did the user press control?
	protected boolean ctrlPressed = false;
	// list holding all current selected items
	private ArrayList<Item> selectedItems = new ArrayList<Item>();
	// state of the current selection
	private SelectionState state;
	// the state of the current file
	private FileState fileState;
	private File currentFile;
	// the border indicating the current selection range
	protected Rectangle selectionBorder = new Rectangle();
	// the model of the board
	private BoardEditorModel model;
	// docking view
	private View view;
	private ArrayList<EditorChangedListener> listeners;
	private final float[] zoomLevels = { 0.1f, 0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 4.0f, 6.0f, 8.0f };
	private int currentZoomIndex = 4;
	private final ShapePropertiesPanel shapesPanel = ctx.getBean(ShapePropertiesPanel.class);
	private final Line currentDrawingLine = new Line(0, 0, 0, 0, 0);

	private final Font miniFont = new Font("SansSerif",Font.PLAIN,7);
	
	private List<Line> connectedLines = null;
	
	private UndoManager undoManager;
	
	private Item mouseOverItem = null;
	private Pin mouseOverPin = null;
	private Line mouseOverLine = null;
	
	private boolean drawing = false;
	private boolean skip = false;
	
	private final StringBuffer tooltipBuffer = new StringBuffer();	
	private final List<Line> magenticLines = new LinkedList<Line>();
	
	private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f);
	
	private Pin selectedPin = null;
	
	/**
	 * Creates a new board editor based on an existing model
	 * 
	 * @param model
	 */
	public BoardEditor(final BoardEditorModel model) {
		super(null);
		setOpaque(false);
		listeners = new ArrayList<EditorChangedListener>();
		this.model = model;
		model.setCurrentLayer(model.getLayer(BoardEditorModel.TOP_LAYER));
		
		if (model.getType() != null && !model.getType().equals(BoardType.SCHEMATICS)) {
			if (model.getType().equals(BoardType.CUSTOM)) {
				setBackgroundImage(model.getBackgroundImage());
			}
			else
				setBackgroundImage(BoardGen.generateBoard(model.getWidth(), model.getHeight(), model.getType(),false));			
		}
		
		// set initial states
		state = SelectionState.NOTHING_SELECTED;
		editorMode = EditorMode.SELECT;
		// init commands
		saveBoardCommand = ctx.getBean(SaveBoardCommand.class);
		deleteCommand = ctx.getBean(DeleteCommand.class);
		addItemCommand = new AddItemCommand();
		// setup the editor view
		Dimension size = new Dimension(model.getWidth(), model.getHeight());
		setSize(size);
		setBounds(new Rectangle(size));
		setPreferredSize(size);
		initListeners();
		initActions();
		EditorUtils.initToolbar();
		hookContextMenu();
		updateStatusBar();
		setTransferHandler(new BoardEditorSymbolTransferHandler());
		EditorUtils.configureDropTarget(this,raster);
		undoManager = new UndoManager();
		win.getViewLabel().setText(resources.getResourceByKey("BoardDesignerPerspective.topView"));
		addMouseMotionListener(this);
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				cancelDrawing();
			}
		};
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);		
	}

	/**
	 * @return the undoManager
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}

	public void cancelDrawing() {
		skip = true;				
		handleReleaseDrawEvent(null);
		lineStopX = 0;
		lineStopY = 0;
		refresh();		
	}
	
	/**
	 * Setup the listeners
	 */
	private void initListeners() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				handleMouseDragged(e);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				
				Point p = e.getPoint();					
				p = BoardUtil.mirrorTransform(p, zoomLayer, e);
							
				if (editorMode.equals(EditorMode.CHECK_CONNECTIONS)) {
					mouseOverLine = EditorUtils.findMouseOverLine(e, BoardEditor.this);
				}
				else {
					mouseOverPin = EditorUtils.findPin(e.getX(),e.getY(), BoardEditor.this);					
				}
				
				win.getCustomLabel().setText("x : "+e.getX()+" y : "+e.getY());
				
				if (mouseOverPin != null) {
					tooltipBuffer.delete(0, tooltipBuffer.length());
					tooltipBuffer.append(mouseOverPin.getNum());					
					if (mouseOverPin.getName() != null) {
						 tooltipBuffer.append(" "+mouseOverPin.getName());
					}
					BoardEditor.this.setToolTipText(tooltipBuffer.toString());
					ToolTipManager.sharedInstance().mouseMoved(
					        new MouseEvent(BoardEditor.this, 0, 0, 0,
					                (int)p.getX(), (int)p.getY(), // X-Y of the mouse for the tool tip
					                0, false));					
				}
				else if (mouseOverLine != null) {
					tooltipBuffer.delete(0, tooltipBuffer.length());
					tooltipBuffer.append("net : "+mouseOverLine.getNetIndex());
					BoardEditor.this.setToolTipText(tooltipBuffer.toString());
									
					ToolTipManager.sharedInstance().mouseMoved(
					        new MouseEvent(BoardEditor.this, 0, 0, 0,
					                (int)p.getX(), (int)p.getY(), // X-Y of the mouse for the tool tip
					                0, false));					
					
				}
				else {
					BoardEditor.this.setToolTipText(null);
				}
				
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseReleased(e);
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				
				Boolean invertMouse = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.invertMouse");

				if (invertMouse) {
					if (currentZoomIndex - e.getWheelRotation() < zoomLevels.length && currentZoomIndex - e.getWheelRotation() > 0) {
						currentZoomIndex -= e.getWheelRotation();
					}					
				}
				else {
					if (currentZoomIndex + e.getWheelRotation() < zoomLevels.length && currentZoomIndex + e.getWheelRotation() > 0) {
						currentZoomIndex += e.getWheelRotation();
					}					
				}
				
				JXLayer<?> layer = getZoomLayer();
				TransformUI ui = (TransformUI)(Object)layer.getUI();
				DefaultTransformModel model = (DefaultTransformModel) ui.getModel();				
				model.setScale(zoomLevels[currentZoomIndex]);
				ctx.getBean(ApplicationWindow.class).getZoomCombo().setSelectedItem(zoomLevels[currentZoomIndex]);
			}
		});
		addMouseMotionListener(AppContext.getContext().getBean(AddTextCommand.class));
	}

	/**
	 * Wire needed actions with the keyboard
	 */
	@SuppressWarnings("serial")
	private void initActions() {
		
		// keyboard associations
		
		ApplicationWindow window = AppContext.getBean(ApplicationWindow.class);
//		
//		window.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "upAction");
//		window.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "downAction");
//		window.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "leftAction");
//		window.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "rightAction");
//
//		window.getRootPane().getActionMap().put("upAction", new AbstractAction() {
//			public void actionPerformed(ActionEvent event) {
//				moveItemByKey("up");
//			}
//		});
//		window.getRootPane().getActionMap().put("downAction", new AbstractAction() {
//			public void actionPerformed(ActionEvent event) {
//				moveItemByKey("down");
//			}
//		});
//		window.getRootPane().getActionMap().put("leftAction", new AbstractAction() {
//			public void actionPerformed(ActionEvent event) {
//				moveItemByKey("left");
//			}
//		});
//		window.getRootPane().getActionMap().put("rightAction", new AbstractAction() {
//			public void actionPerformed(ActionEvent event) {
//				moveItemByKey("right");
//			}
//		});
		
		registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveItemByKey("up");
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveItemByKey("down");
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveItemByKey("left");
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveItemByKey("right");
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	private void moveItemByKey(String direction) {
		if (selectedItem != null) {
		    
			updateStatusBar();
		}
		if (selectedItems.size() > 0) {
			for (Item i : selectedItems) {

			}
			updateStatusBar();
		}
	}

	private void handleMouseDragged(MouseEvent e) {
		if (editorMode.equals(EditorMode.SELECT) || 
			editorMode.equals(EditorMode.MOVE))
			handleDragEvent(e);
		else if (editorMode.equals(EditorMode.DRAW_LINE) ||
				 editorMode.equals(EditorMode.DRAW_MEASURE) ||
				 editorMode.equals(EditorMode.DRAW_ELLIPSE) ||
				 editorMode.equals(EditorMode.DRAW_RECTANGLE)) {									
			handleDrawEvent(e);
		}
		refresh();
	}

	private void handleMousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			handleContextClick(e);
		}			
		if (e.isControlDown()) {
			ctrlPressed = true;
		}
		else {
			ctrlPressed = false;
		}
		if (e.getButton() == 1) {
			if (!button1Pressed) {
				button1Pressed = true;
				if (editorMode.equals(EditorMode.SELECT)) {
					handleClickSelectionEvent(e);
				}
				else if (editorMode.equals(EditorMode.MOVE)) {
					dragStartX = e.getX();
					dragStartY = e.getY();
					dragStopX  = e.getX();
					dragStopY = e.getY();
				}
				else if (editorMode.equals(EditorMode.DRAW_LINE) || 
						 editorMode.equals(EditorMode.DRAW_MEASURE) ||
						 editorMode.equals(EditorMode.DRAW_RECTANGLE) || 
						 editorMode.equals(EditorMode.DRAW_ELLIPSE)) {
					if (!model.getCurrentLayer().getName().equalsIgnoreCase("Board")) {
						if (!drawing) {
							handleClickDrawEvent(e);						
						}
						else {
							handleReleaseDrawEvent(e);
						}						
					}
				}
				else if (editorMode.equals(EditorMode.CHECK_CONNECTIONS)) {
					handleCheckConnectionEvent(e);
				}
			}
			
		}
		else if (e.getButton() == 2) {
			button2Pressed = true;
		}
		else if (e.getButton() == 3) {
			button3Pressed = true;
		}
		if (e.getClickCount() == 2 && e.isShiftDown()) {
			if (selectedItem != null || getSelectedItems().size() > 0) {
				AppContext.getContext().getBean(EditPropertiesCommand.class).execute(null);
			}
		}
		refresh();
	}

	/**
	 * <p>
	 * Connection check mode : If any click occurs in this mode, check if
	 * the click happened on any line. If that happened, determine all lines that 
	 * are connected with that line and add them to the list off connected lines.
	 * </p>
	 * <p>
	 * If the list of connected lines contains any line while the board is being painted,
	 * they are painted in some bright manner in order to view them easily.
	 * 
	 * @param e
	 */
	private void handleCheckConnectionEvent(MouseEvent e) {

		LinkedList<Line> lines = new LinkedList<Line>();
		
		Line selectedLine = null;
		
		// First collect all lines.
		
		for (Layer layer : model.getLayers()) {
			
			for (Item item : layer.getItems()) {
				
				if (item instanceof Line) {					
					Line line = (Line) item;					
					lines.add(line);
				}
				
			}
			
		}
		
		// Then check which line has been selected
		
		for (Layer layer : model.getLayers()) {
			
			for (Item item : layer.getItems()) {
				
				if (item instanceof Line) {					
					Line line = (Line) item;					
					if (line.containsPoint(e.getX(), e.getY())) {
						selectedLine = line;
					}
				}
				
			}
			
		}		
		// And finally determine the connections to that line
		if (selectedLine != null && lines.size() > 1) {
			connectedLines = ConnectionUtils.getConnectedLines(selectedLine, lines);
			selectedItems.clear();
			// for convenience, we select all connected lines
			selectedItems.addAll(connectedLines);
			updateEditCommands();
		}
		
		if (selectedLine == null) {
			connectedLines.clear();
		}
		
	}

	private void handleMouseReleased(MouseEvent e) {
		
		ArrayList<UndoableEdit> edits = new ArrayList<UndoableEdit>();
		
		for (Item item : selectedItems) {
						
			Line currentLine = null;
			
			if (item instanceof Line) {
				currentLine = (Line)item;
			}
			// just add edit if item or line has been moved
			if (item.getOldXLoc() > 0 || (currentLine != null && !currentLine.getOldstart().equals(currentLine.getStart()))) {
				if (item instanceof Line) {
					Line l = (Line) item;
					MoveLineEdit ml = new MoveLineEdit(l, 
							 l.getOldstart().x, l.getOldstart().y,
							 l.getStart().x, l.getStart().y, 
							 l.getOldEnd().x, l.getOldEnd().y, 
							 l.getEnd().x, l.getEnd().y,true);
					edits.add(ml);
					
					for (WireConnection wc : l.getWireConnections()) {					
						Line l1 = wc.getLine();
						ml = new MoveLineEdit(l1,  
								 l1.getOldstart().x, l1.getOldstart().y,
								 l1.getStart().x,  l1.getStart().y, 
								 l1.getOldEnd().x, l1.getOldEnd().y, 
								 l1.getEnd().x, l1.getEnd().y,true);
						edits.add(ml);
						l1.setOldstart(new Point(l1.getStart()));
						l1.setOldEnd(new Point(l1.getEnd()));					
					}
					
				}
				else {
					MoveItemEdit me = new MoveItemEdit(item,item.getOldXLoc(),item.getOldYLoc(),
							item.getXLoc(),item.getYLoc(),true);
					
					if (item instanceof Part) {
						
						Part part = (Part)item;
						
						for (WireConnection wc : part.getWireConnections()) {					
							Line l1 = wc.getLine();
							MoveLineEdit m = new MoveLineEdit(l1,  
									l1.getOldstart().x, l1.getOldstart().y,
									l1.getStart().x,  l1.getStart().y, 
									l1.getOldEnd().x, l1.getOldEnd().y, 
									l1.getEnd().x, l1.getEnd().y,true);
							edits.add(m);
							l1.setOldstart(new Point(l1.getStart()));
							l1.setOldEnd(new Point(l1.getEnd()));					
						}
						
						edits.add(me);
						
					}
					
				}
				setFileState(FileState.DIRTY);				
			}			
											
		}
	
		if (edits.size() > 0) {
			MoveMultipleItemsEdit mme = new MoveMultipleItemsEdit(edits);
			
			if (!undoManager.addEdit(mme)) {
				log.error("could not add edit to undo manager");				
			}
			else {
				log.info("added edit : "+this.getClass());
			}			
		}
		
		if (editorMode.equals(EditorMode.SELECT)) {
			if (selectedItem != null)
				state = SelectionState.SINGLE_ITEM_SELECTED;
			else
				state = SelectionState.NOTHING_SELECTED;
		}
		if (e.isPopupTrigger()) {
			handleContextClick(e);
		}
		if (e.getButton() == 1) {
			button1Pressed = false;
			if (editorMode.equals(EditorMode.SELECT)) {
				// handleClickSelectionEvent(e);
				handleReleaseSelectionEvent(e,edits);
			}
		}
		else if (e.getButton() == 2) {
			button2Pressed = false;
		}
		else if (e.getButton() == 3) {
			button3Pressed = false;
		}
		refresh();
	}

	private void handleDrawEvent(MouseEvent e) {
		lineStopX = e.getX();
		lineStopY = e.getY();
		if (snapToGrid) {
			lineStopX = BoardUtil.snap(lineStopX, raster);
			lineStopY = BoardUtil.snap(lineStopY, raster);
		}		
		selectedPin = EditorUtils.findPin(lineStopX, lineStopY, this);
	}

	private void handleDragEvent(MouseEvent e) {
		int deltaX = e.getX() - dragStartX;
		int deltaY = e.getY() - dragStartY;
		if (snapToGrid) {
			deltaX = BoardUtil.snap(deltaX, raster);
			deltaY = BoardUtil.snap(deltaY, raster);
		}
		if (currentMovingItem != null) {
			// if (editorMode.equals(EditorMode.MOVE)) {
				setFileState(FileState.DIRTY);
				if (currentMovingItem instanceof Line && currentMovingItem == selectedItem) {
					Line l = (Line) currentMovingItem;
					moveLine(deltaX, deltaY, l,false);
					setFileState(FileState.DIRTY);
				}
				else if ((currentMovingItem instanceof Part || 
						currentMovingItem instanceof Box  || 
						currentMovingItem instanceof Ellipse) &&
						currentMovingItem == selectedItem) {
					moveOrResizeItem(deltaX, deltaY, currentMovingItem);
					setFileState(FileState.DIRTY);
				}				
			//}
		}
		else {
			if (e.getX() <= model.getWidth())
				dragStopX = e.getX();
			else
				dragStopX = model.getWidth() - 1;
			if (e.getY() <= model.getHeight())
				dragStopY = e.getY();
			else
				dragStopY = model.getHeight() - 1;

			if (state.equals(SelectionState.DRAGGING_NEW_SELECTION)) {
				for (Layer layer : model.getLayers()) {
					if (!layer.isVisible() || layer.isLocked()) {
						continue;
					}
					for (Item item : layer.getItems()) {
						if (ShapeUtil.collides(selectionBorder, item.getBoundingBox())) {
							if (!selectedItems.contains(item))
								selectedItems.add(item);
						}
						else {
							if (selectedItems.contains(item))
								selectedItems.remove(item);
						}
					}
				}
			}
			// one or many items selected, move them
			else {
				if (selectedItems.size() > 1) {
					// if (editorMode.equals(EditorMode.MOVE)) {
						for (Item item : selectedItems) {
							if (item instanceof Line) {
								Line l = (Line) item;
								moveLine(deltaX, deltaY, l,false);
							}
							else {
								moveOrResizeItem(deltaX, deltaY, item);
							}
							setFileState(FileState.DIRTY);
							
						}						
					// }					
				}
				else {
					getCurrentMovingItem(e);
				}
			}
		}
		updateEditCommands();
	}

	/**
	 * Determines the item currently being moved, this is in any case
	 * the same as the selected item, if theres no selected item, we
	 * do not have a moving item.
	 * 
	 * @param e
	 */
	private void getCurrentMovingItem(MouseEvent e) {
		
		for (Layer layer : model.getLayers()) {
			for (Item item : layer.getItems()) {
				if (item instanceof Line) {
					Line line = (Line) item;
					if (line.containsPoint(e.getX(), e.getY()) || line.isInsideEndHandle(e.getX(), e.getY())
							|| line.isInsideStartHandle(e.getX(), e.getY())) {
						if (button1Pressed) {
							if (item == selectedItem) {
								currentMovingItem = item;
								break;
							}
						}
					}
				}
				else {
					if (item.isInside(e.getX(), e.getY()) || item.isInsideNorthEastHandle(e.getX(), e.getY())
							|| item.isInsideNorthWestHandle(e.getX(), e.getY())
							|| item.isInsideSouthEastHandle(e.getX(), e.getY())
							|| item.isInsideSouthWestHandle(e.getX(), e.getY())) {
						if (button1Pressed) {
							if (item == selectedItem) {
								currentMovingItem = item;
								break;
							}
						}
					}
				}
			}
			if (currentMovingItem != null) {
				break;
			}
		}
	}

	private void moveLine(int deltaX, int deltaY, Line line,boolean subItem) {		
		
		int newLineStartX = (int) line.getOldstart().getX() + deltaX;
		int newLineStartY = (int) line.getOldstart().getY() + deltaY;
		int newLineStopX = (int) line.getOldEnd().getX() + deltaX;
		int newLineStopY = (int) line.getOldEnd().getY() + deltaY;
	
		// prevent subitems from being distorted and line elements resized if multiple lines are selected 
		if (draggingStartHandle && !subItem && selectedItems.size() == 0) {
			if (magnetic) {
				
				for (WireConnection wc : line.getWireConnections()) {
					
					Line otherLine = wc.getLine();
					
					int _newLineStartX = (int) otherLine.getOldstart().getX() + deltaX;
					int _newLineStartY = (int) otherLine.getOldstart().getY() + deltaY;
					int _newLineStopX = (int) otherLine.getOldEnd().getX() + deltaX;
					int _newLineStopY = (int) otherLine.getOldEnd().getY() + deltaY;
					
					if (wc.getType().equals(WireConnectionType.START_START)) {
						otherLine.getStart().setLocation(_newLineStartX,_newLineStartY);
					}
					else if (wc.getType().equals(WireConnectionType.END_START)) {
						otherLine.getEnd().setLocation(_newLineStopX,_newLineStopY);
					}
					
				}
				
			}
			line.getStart().setLocation(newLineStartX, newLineStartY);


		}
		else if (draggingEndHandle && !subItem && selectedItems.size() == 0) {
			
			if (magnetic) {
				
				for (WireConnection wc : line.getWireConnections()) {
					
					Line otherLine = wc.getLine();
					
					int _newLineStartX = (int) otherLine.getOldstart().getX() + deltaX;
					int _newLineStartY = (int) otherLine.getOldstart().getY() + deltaY;
					int _newLineStopX = (int) otherLine.getOldEnd().getX() + deltaX;
					int _newLineStopY = (int) otherLine.getOldEnd().getY() + deltaY;
					
					if (wc.getType().equals(WireConnectionType.START_END)) {
						otherLine.getStart().setLocation(_newLineStartX,_newLineStartY);
					}
					else if (wc.getType().equals(WireConnectionType.END_END)) {
						otherLine.getEnd().setLocation(_newLineStopX,_newLineStopY);
					}
					
				}
				
			}			
			line.getEnd().setLocation(newLineStopX, newLineStopY);
		}
		else {
			
			if (magnetic) {
				
				for (WireConnection wc : line.getWireConnections()) {
					
					Line otherLine = wc.getLine();
					
					int _newLineStartX = (int) otherLine.getOldstart().getX() + deltaX;
					int _newLineStartY = (int) otherLine.getOldstart().getY() + deltaY;
					int _newLineStopX = (int) otherLine.getOldEnd().getX() + deltaX;
					int _newLineStopY = (int) otherLine.getOldEnd().getY() + deltaY;
					
					if (wc.getType().equals(WireConnectionType.START_START)) {
						otherLine.getStart().setLocation(_newLineStartX,_newLineStartY);
					}
					else if (wc.getType().equals(WireConnectionType.START_END)) {
						otherLine.getStart().setLocation(_newLineStartX,_newLineStartY);
					}
					else if (wc.getType().equals(WireConnectionType.END_END)) {
						otherLine.getEnd().setLocation(_newLineStopX,_newLineStopY);
					}
					else if (wc.getType().equals(WireConnectionType.END_START)) {
						otherLine.getEnd().setLocation(_newLineStopX,_newLineStopY);
					}
					
				}
				
			}			
			
			line.getStart().setLocation(newLineStartX, newLineStartY);
			line.getEnd().setLocation(newLineStopX, newLineStopY);
		}
		
	}
	
	/**
	 * Moves or resizes an {@link Item} based on given delta 
	 * 
	 * @param deltaX the delta in x-direction
	 * @param deltaY the delta in y-direction
	 * 
	 * @param item the item to be moved or resized
	 */
	private void moveOrResizeItem(int deltaX, int deltaY, Item item) {
		int xLoc = item.getOldXLoc() + deltaX;
		int yLoc = item.getOldYLoc() + deltaY;
		int width = item.getOldWidth() + deltaX;
		int height = item.getOldHeight() + deltaY;
		if (width <= 8)
			width = 8;
		if (height <= 8)
			height = 8;
		if (draggingNorthWestEdge && item.isResizable()) {
			item.setWidth(item.getOldWidth() - deltaX);
			item.setHeight(item.getOldHeight() - deltaY);
			item.setYLoc(yLoc);
			item.setXLoc(xLoc);
		}
		else if (draggingNorthEastEdge && item.isResizable()) {
			item.setWidth(width);
			item.setHeight(item.getOldHeight() - deltaY);
			item.setYLoc(yLoc);
		}
		else if (draggingSouthWestEdge && item.isResizable()) {
			item.setHeight(height);
			item.setWidth(item.getOldWidth() - deltaX);
			item.setXLoc(xLoc);
		}
		else if (draggingSouthEastEdge && item.isResizable()) {
			item.setWidth(width);
			item.setHeight(height);
		}
		else {
			item.setXLoc(xLoc);
			item.setYLoc(yLoc);

			if (item instanceof Part) {
				
				Part part = (Part)item;
				
				if (magnetic) {
					
					for (WireConnection wc : part.getWireConnections()) {
						
						Line otherLine = wc.getLine();
						
						int newLineStartX = (int) otherLine.getOldstart().getX() + deltaX;
						int newLineStartY = (int) otherLine.getOldstart().getY() + deltaY;
						int newLineStopX = (int) otherLine.getOldEnd().getX() + deltaX;
						int newLineStopY = (int) otherLine.getOldEnd().getY() + deltaY;
						
						if (wc.getType().equals(WireConnectionType.START)) {
							otherLine.getStart().setLocation(newLineStartX, newLineStartY);								
						}
						else {
							otherLine.getEnd().setLocation(newLineStopX, newLineStopY);
						}
						
					}
					
				}
				
			}
			
			if (item instanceof Symbol) {
				
				Symbol symbol = (Symbol)item;
				
				for (Item subItem : symbol.getItems()) {
					if (subItem instanceof Line) {															
						Line line = (Line)subItem;
						moveLine(deltaX, deltaY, line,true);
					}
					else {
						// restore position
						xLoc = subItem.getOldXLoc() + deltaX;
						yLoc = subItem.getOldYLoc() + deltaY;
						subItem.setXLoc(xLoc);
						subItem.setYLoc(yLoc);				
					}			
					
				}
			}
		}
	}

	/**
	 * Click happened, change state of selected item or select another one
	 * 
	 * @param e
	 */
	private void handleClickSelectionEvent(MouseEvent e) {
		
		if (ctrlPressed) {
			if (selectedItem != null) {
				selectedItems.add(selectedItem);
				selectedItem = null;
			}
			Item selection = selectItem(e);
			if (selection != null) {
				selectedItems.add(selection);	
			}						
			refresh();
		}
		else {			
			if (selectedItem != null) { // only one item selected		
				changeSelectedItemState(e);
			}
			else {
				
				Item selection = null;
				
				selection = selectItem(e);					
				if (selection != null) {
					if (!selectedItems.contains(selection)) {
						selectedItems.clear();							
						selectedItem = selection;
						changeSelectedItemState(e);
					}
				}
				
				if (selection == null) {
					selectedItems.clear();
					selectedItem = null;
					clearSelectionBorder();
					state = SelectionState.DRAGGING_NEW_SELECTION;
					
				}
				
			}
			
		}
		
		if (selectedItem == null && selectedItems.size() == 0) {
			clearSelectionBorder();
		}
		
		updateRelatedUI();		
		dragStartX = e.getX();
		dragStartY = e.getY();
		dragStopX  = e.getX();
		dragStopY = e.getY();
		updateEditCommands();
	}

	/**
	 * Change the state of selected items based on an already selected item.
	 * If the click happens on the same item the state might be changed, otherwise
	 * another item is selected or the selection is cleared, if no item was hit.
	 * 
	 * @param e
	 */
	private void changeSelectedItemState(MouseEvent e) {
		if (selectedItem instanceof Line) {
			Line line = (Line) selectedItem;
			if (line.isInsideStartHandle(e.getX(), e.getY())) {
				draggingStartHandle = true;
				draggingEndHandle = false;
			}
			else if (line.isInsideEndHandle(e.getX(), e.getY())) {
				draggingStartHandle = false;
				draggingEndHandle = true;
			}
			// Click did not happen on same line, select another
			else { 
				draggingStartHandle = false;
				draggingEndHandle = false;				
				selectedItem = selectItem(e);
				refresh();
			}			
			line.setOldstart(new Point(line.getStart()));
			line.setOldEnd(new Point(line.getEnd()));
		}
		else if (selectedItem instanceof Part || 
				 selectedItem instanceof Box || 
				 selectedItem instanceof Ellipse || 
				 selectedItem instanceof Symbol) {
			
			draggingNorthWestEdge = selectedItem.isInsideNorthWestHandle(e.getX(), e.getY());
			draggingNorthEastEdge = selectedItem.isInsideNorthEastHandle(e.getX(), e.getY());
			draggingSouthWestEdge = selectedItem.isInsideSouthWestHandle(e.getX(), e.getY());
			draggingSouthEastEdge = selectedItem.isInsideSouthEastHandle(e.getX(), e.getY());
			
			selectedItem = selectItem(e);
			
			refresh();

			BoardUtil.syncItemState(model);
		}
	}

	private Item selectItem(MouseEvent e) {

		Item currentItem = null;
		
		log.info("Select item.");
		
		ArrayList<Item> possibleSelectedItems = new ArrayList<Item>();
		
		// first collect all items below clicked point 
		
		for (Layer layer : model.getLayers()) {
			if (!layer.isVisible() || layer.isLocked())
				continue;
			for (Item item : layer.getItems()) {
				if (item instanceof Line) {
					Line line = (Line) item;
					if (line.containsPoint(e.getX(), e.getY())) {
						possibleSelectedItems.add(line);
					}
				}
				else {
					if (item.isInside(e.getX(), e.getY()) 
							|| item.isInsideNorthEastHandle(e.getX(), e.getY())
							|| item.isInsideNorthWestHandle(e.getX(), e.getY())
							|| item.isInsideSouthEastHandle(e.getX(), e.getY())
							|| item.isInsideSouthWestHandle(e.getX(), e.getY())) {
						possibleSelectedItems.add(item);
					}
				}				
			}
		}
		

		// No item has been hit
		if (possibleSelectedItems.isEmpty()) {
			return null;
		}
		
		Collections.sort(possibleSelectedItems);
		Collections.reverse(possibleSelectedItems);
		
		// find item with max z-order
		
		int z = Integer.MIN_VALUE;

		// already an item selected, select the next one below
		// if the selected one is the at the bottom, select the topmost one
		if (selectedItem != null) {
			for (Item item : possibleSelectedItems) {
				if (item.getIndex() < selectedItem.getIndex()) {
					currentItem = item;
					break;
				}
			}	
			// there was no item below, find the topmost one
			if (currentItem == null) {
				for (Item item : possibleSelectedItems) {
					if (item.getIndex() > z) {
						currentItem = item;
						break;
					}
				}							
			}
			
		}
		// no item selected, find the one which is on top
		else {
			for (Item item : possibleSelectedItems) {
				if (item.getIndex() > z) {
					currentItem = item;
				}
			}			
		}
			
		if (currentItem instanceof Line) {
			Line line = (Line) currentItem;
			// click happened on line
			line.setOldstart(new Point(line.getStart()));
			line.setOldEnd(new Point(line.getEnd()));
		}
		else if (currentItem instanceof Part) {
			
			currentItem.setOldXLoc(currentItem.getXLoc());
			currentItem.setOldYLoc(currentItem.getYLoc());
			
			if (currentItem instanceof Symbol) {
				
				Symbol symbol = (Symbol)currentItem;
				
				for (Item subItem : symbol.getItems()) {
					// restore position
					if (subItem instanceof Line) {									
						Line line = (Line)subItem;						
						line.setOldstart(new Point(line.getStart()));
						line.setOldEnd(new Point(line.getEnd()));
					}
					else {
						subItem.setOldXLoc(subItem.getXLoc());
						subItem.setOldYLoc(subItem.getYLoc());
						subItem.setOldWidth(subItem.getWidth());
						subItem.setOldHeight(subItem.getHeight());						
					}			
				
				}
				
			}
			
		}
		
		if (magnetic) {						
			if (currentItem != null) {
				if (currentItem instanceof Line) {
					Line line = (Line)currentItem;
					BoardUtil.findConnectionsToLine(line, model);			
				}
				else {
					BoardUtil.findConnectionsToItem(currentItem, model);					
				}				
			}				
		}
		
		return currentItem;
	}
	
	/**
	 * Updates the UI element state based on the selected item
	 */
	private void updateRelatedUI() {
		if (selectedItem == null) {
			ctx.getBean(ShapePropertiesPanel.class).getObjectField().setText(resources.getResourceByKey("ShapePropertiesPanel.items.none"));
			ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().setEnabled(false);
			ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().setEnabled(false);				
		}
		else {
			ctx.getBean(ShapePropertiesPanel.class).getObjectField().setText(selectedItem.getClass().getSimpleName());
			if (selectedItem instanceof Ellipse) {
				Ellipse el = (Ellipse)selectedItem;				
				ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().setEnabled(true);
				ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().setEnabled(true);
				ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().setValue(Integer.valueOf(el.getRotation()));
				ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().setValue(Integer.valueOf(el.getStartAngle()));				
			}
			else {
				ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().setEnabled(false);
				ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().setEnabled(false);				
			}
			
			if (selectedItem instanceof Shape) {
				Shape shape = (Shape)selectedItem;
				ctx.getBean(ShapePropertiesPanel.class).getStyleCombo().setSelectedItem(shape.getStyle());
				ctx.getBean(ShapePropertiesPanel.class).getThicknessCombo().setSelectedItem(shape.getStroke());
			}
			
			if (selectedItem instanceof Line) {
				Line line = (Line)selectedItem;
				ctx.getBean(ShapePropertiesPanel.class).getStartLineCombo().setSelectedItem(line.getStartType());
				ctx.getBean(ShapePropertiesPanel.class).getEndLineCombo().setSelectedItem(line.getEndType());
			}
			
		}
	}

	public void clearSelectionBorder() {
		selectionBorder.setLocation(0, 0);
		selectionBorder.setSize(0, 0);
	}

	private void handleReleaseSelectionEvent(MouseEvent e, ArrayList<UndoableEdit> edits) {
		
		if (currentMovingItem != null) {
			
			if (currentMovingItem instanceof Line) {
				
				Line line = (Line)currentMovingItem;				
				
				MoveLineEdit ml = new MoveLineEdit(line, line.getOldstart().x, line.getOldstart().y,
														 line.getStart().x, line.getStart().y, 
														 line.getOldEnd().x, line.getOldEnd().y, 
														 line.getEnd().x, line.getEnd().y,false);
				
				for (WireConnection wc : line.getWireConnections()) {					
					Line l1 = wc.getLine();
					MoveLineEdit m = new MoveLineEdit(l1,  
							 l1.getOldstart().x, l1.getOldstart().y,
							 l1.getStart().x,  l1.getStart().y, 
							 l1.getOldEnd().x, l1.getOldEnd().y, 
							 l1.getEnd().x, l1.getEnd().y,true);
					edits.add(m);
					l1.setOldstart(new Point(l1.getStart()));
					l1.setOldEnd(new Point(l1.getEnd()));					
				}
				

				if (edits.size() > 0) {
					
					edits.add(ml);
					
					MoveMultipleItemsEdit mme = new MoveMultipleItemsEdit(edits);
					
					if (!undoManager.addEdit(mme)) {
						log.error("could not add edit to undo manager");				
					}
					else {
						log.info("added edit : "+this.getClass());
					}
					
				}
				else {
					if (!undoManager.addEdit(ml)) {
						log.error("could not add edit to undo manager");				
					}
					else {
						log.info("added edit : "+this.getClass());
					}
					
				}
				
				ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
				ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());

				line.setOldstart(new Point(line.getStart()));
				line.setOldEnd(new Point(line.getEnd()));
				
			}
			else {
				MoveItemEdit me = new MoveItemEdit(currentMovingItem,currentMovingItem.getOldXLoc(),currentMovingItem.getOldYLoc(),
						   currentMovingItem.getXLoc(),currentMovingItem.getYLoc(),false);

				if (currentMovingItem instanceof Part) {
					
					Part part = (Part)currentMovingItem;
					
					for (WireConnection wc : part.getWireConnections()) {					
						Line l1 = wc.getLine();
						MoveLineEdit m = new MoveLineEdit(l1,  
								 l1.getOldstart().x, l1.getOldstart().y,
								 l1.getStart().x,  l1.getStart().y, 
								 l1.getOldEnd().x, l1.getOldEnd().y, 
								 l1.getEnd().x, l1.getEnd().y,true);
						edits.add(m);
						l1.setOldstart(new Point(l1.getStart()));
						l1.setOldEnd(new Point(l1.getEnd()));					
					}
					

					if (edits.size() > 0) {
						
						edits.add(me);
						
						MoveMultipleItemsEdit mme = new MoveMultipleItemsEdit(edits);
						
						if (!undoManager.addEdit(mme)) {
							log.error("could not add edit to undo manager");				
						}
						else {
							log.info("added edit : "+this.getClass());
						}
						
					}
					else {
						if (!undoManager.addEdit(me)) {
							log.error("could not add edit to undo manager");				
						}
						else {
							log.info("added edit : "+this.getClass());
						}
						
					}
					
				}
				
				ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
				ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());
				
			}
			currentMovingItem = null;
		}
		else if (selectedItems.size() > 0) {
			
			BoardUtil.syncItemState(selectedItems);
			
			ctx.getBean(ShapePropertiesPanel.class).getObjectField().setText(selectedItems.size() + " "+resources.getResourceByKey("ShapePropertiesPanel.items"));

			if (edits.size() > 0) {
				MoveMultipleItemsEdit mme = new MoveMultipleItemsEdit(edits);
				
				if (!undoManager.addEdit(mme)) {
					log.error("could not add edit to undo manager");				
				}
				else {
					log.info("added edit : "+this.getClass());
				}
				
				ctx.getBean(RedoCommand.class).setEnabled(undoManager.canRedo());
				ctx.getBean(UndoCommand.class).setEnabled(undoManager.canUndo());													
			}

		}
		
		BoardUtil.syncItemState(model);
	}

	private void handleClickDrawEvent(MouseEvent e) {
		
		if (e == null)		
			return;
		
		drawing = true;

		// if we are drawing a continous line, make sure the 
		// next line segment starts at the previous segments end

		Boolean drawContinuous = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.dawContinuousLines");
		
		if (drawContinuous && lineStopX > 0 && lineStopY > 0) {
			lineStartX = lineStopX;
			lineStartY = lineStopY;
			return;
		}
		 // start of a new line, mouse coordinates are starting point
		else {
			lineStartX = e.getX();
			lineStartY = e.getY();			
		}
		// snap the point to the grid
		if (snapToGrid) {
			lineStartX = BoardUtil.snap(lineStartX, raster);
			lineStartY = BoardUtil.snap(lineStartY, raster);			
		}
	}

	private void handleReleaseDrawEvent(MouseEvent e) {
		drawing = false;
		if (!skip) {						 
			if (lineStartX > 0 && lineStartY > 0 && lineStopX > 0 && lineStopY > 0) {
				if (editorMode.equals(EditorMode.DRAW_LINE)) {
					Line line = new Line(lineStartX, lineStartY, lineStopX, lineStopY, model.getCurrentLayer().getItems().size() - 1);
					line.setOldstart(new Point(line.getStart()));
					line.setOldEnd(new Point(line.getEnd()));
					addItemCommand.setItem(line);
					addItemCommand.execute(null);
				}
				else if(editorMode.equals(EditorMode.DRAW_MEASURE)) {
					Measure measure = new Measure(lineStartX, lineStartY, lineStopX, lineStopY, model.getCurrentLayer().getItems().size() - 1);
					measure.setOldstart(new Point(measure.getStart()));
					measure.setOldEnd(new Point(measure.getEnd()));
					addItemCommand.setItem(measure);
					addItemCommand.execute(null);
					
				}
				else if (editorMode.equals(EditorMode.DRAW_RECTANGLE) || editorMode.equals(EditorMode.DRAW_ELLIPSE)) {
					Item newItem;
					if (editorMode.equals(EditorMode.DRAW_RECTANGLE)) {
						newItem = new Box();
					}
					else {
						newItem = new Ellipse();
					}
					newItem.setXLoc(lineStartX);
					newItem.setYLoc(lineStartY);
					newItem.setOldXLoc(newItem.getXLoc());
					newItem.setOldYLoc(newItem.getYLoc());
					newItem.setHeight(lineStopY - lineStartY);
					newItem.setWidth(lineStopX - lineStartX);
					newItem.setOldWidth(newItem.getWidth());
					newItem.setOldHeight(newItem.getHeight());
					
					Boolean useLayerColor = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor"); 
					
					if (useLayerColor)
						newItem.setColor(model.getCurrentLayer().getColor());
					else
						newItem.setColor(palette.getCurrentColor());
					
					addItemCommand.setItem(newItem);
					addItemCommand.execute(null);
				}
			}
		}
		skip = false;
		
		lineStartX = 0;
		lineStartY = 0;
		
		Boolean drawContinuous = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.dawContinuousLines");		

		if (!drawContinuous) {
			lineStopX = 0;
			lineStopY = 0;			
		}
		
		// draw continuously if desired
		if (drawContinuous && selectedPin == null && editorMode.equals(EditorMode.DRAW_LINE) ) {
			handleClickDrawEvent(e);			
		}
		else {
			lineStopX = 0;
			lineStopY = 0;			
			lineStartX = 0;
			lineStartY = 0;			
		}
	}

	@Override
	protected void paintComponent(Graphics g) {		
		Boolean useLayerColor = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.useLayerColor");
		
		// clear all		
		// g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(BLANK);
		g.fillRect(0, 0, getWidth(), getHeight());		
		// some nice anti aliasing...
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);

		// TODO : Should this be done here?
		// Sort layers by z-index

		g2.setColor(Color.LIGHT_GRAY);
		g2.setStroke(BoardUtil.stroke_1_0f);
						
		Collections.sort(model.getLayers());
		for (int i = model.getLayers().size() - 1; i >= 0; i--) {
			// Sort items by z-index
			Collections.sort(model.getLayers().get(i).getItems());
			drawLayer(model.getLayers().get(i), g2);
		}
		
		// draw selection border
		if (state.equals(SelectionState.DRAGGING_NEW_SELECTION) && button1Pressed) {
			g2.setColor(Color.GREEN);
			g2.setStroke(BoardUtil.stroke_1_0f);
			if (dragStopX < dragStartX && dragStopY > dragStartY) {
				selectionBorder.setSize(dragStartX - dragStopX, dragStopY - dragStartY);
				selectionBorder.setLocation(dragStopX, dragStartY);
			}
			else if (dragStopY < dragStartY && dragStopX > dragStartX) {
				selectionBorder.setSize(dragStopX - dragStartX, dragStartY - dragStopY);
				selectionBorder.setLocation(dragStartX, dragStopY);
			}
			else if (dragStopX < dragStartX && dragStopY < dragStartY) {
				selectionBorder.setSize(dragStartX - dragStopX, dragStartY - dragStopY);
				selectionBorder.setLocation(dragStopX, dragStopY);
			}
			else {
				selectionBorder.setSize(dragStopX - dragStartX, dragStopY - dragStartY);
				selectionBorder.setLocation(dragStartX, dragStartY);
			}
			g2.draw(selectionBorder);
		}
		// display shape currently being drawed
		if (lineStartX > 0 && lineStartY > 0 && lineStopX > 0 && lineStopY > 0) {
			 			
			if (useLayerColor)
				g2.setColor(model.getCurrentLayer().getColor());
			else
				g2.setColor(palette.getCurrentColor());

			g2.setStroke((BasicStroke) shapesPanel.getThicknessCombo().getSelectedItem());
			// draw new line
			if (editorMode.equals(EditorMode.DRAW_LINE)) {
								
				if (useLayerColor)
					currentDrawingLine.setColor(model.getCurrentLayer().getColor());					
				else
					currentDrawingLine.setColor(palette.getCurrentColor());
				
				currentDrawingLine.setStartType((LineEdgeType) shapesPanel.getStartLineCombo().getSelectedItem());
				currentDrawingLine.setEndType((LineEdgeType) shapesPanel.getEndLineCombo().getSelectedItem());
				currentDrawingLine.setStroke((BasicStroke) shapesPanel.getThicknessCombo().getSelectedItem());
				currentDrawingLine.getStart().setLocation(lineStartX, lineStartY);
				currentDrawingLine.getEnd().setLocation(lineStopX, lineStopY);
				currentDrawingLine.draw(g2);
			}
			else if (editorMode.equals(EditorMode.DRAW_MEASURE)) {
				currentDrawingLine.setStroke(Measure.DEFAULT_STROKE);
				currentDrawingLine.getStart().setLocation(lineStartX, lineStartY);
				currentDrawingLine.getEnd().setLocation(lineStopX, lineStopY);
				currentDrawingLine.draw(g2);				
			}
			// draw new box or ellipse
			else if (editorMode.equals(EditorMode.DRAW_RECTANGLE) || editorMode.equals(EditorMode.DRAW_ELLIPSE)) {				
				int xLoc = lineStartX;
				int yLoc = lineStartY;
				int width = lineStopX - lineStartX;
				int height = lineStopY - lineStartY;
				ShapeStyle style = (ShapeStyle) shapesPanel.getStyleCombo().getSelectedItem();
				if (style == null || style.equals(ShapeStyle.FILLED)) {
					if (editorMode.equals(EditorMode.DRAW_RECTANGLE)) {
						g2.fillRect(xLoc, yLoc, width, height);
					}
					else {
						g2.fillOval(xLoc, yLoc, width, height);
					}
				}
				else if (style.equals(ShapeStyle.OUTLINED)) {
					g2.setStroke((BasicStroke) shapesPanel.getThicknessCombo().getSelectedItem());
					if (editorMode.equals(EditorMode.DRAW_RECTANGLE)) {
						g2.drawRect(xLoc, yLoc, width, height);
					}
					else {
						g2.drawOval(xLoc, yLoc, width, height);
					}
				}
			}
		}
		// draw selection handles
		if (selectedItem != null) {
			g2.setStroke(BoardUtil.stroke_1_0f);
			g2.setColor(Color.GREEN);			
			selectedItem.drawHandles(g2,8);
		}

		// draw border
		
		if (zoomLayer != null) {
			TransformUI ui = (TransformUI)(Object) zoomLayer.getUI();
			DefaultTransformModel xmodel = (DefaultTransformModel) ui.getModel();

			if (xmodel.isMirror()) {
				g2.setColor(Color.RED);	
			}
			else {
				g2.setColor(Color.GREEN);
			}			
		}
		else {
			g2.setColor(Color.GREEN);
		}
		
		g2.setStroke(DEFAULT_STROKE);

		Rectangle border = new Rectangle(0, 0, model.getWidth() - 1, model.getHeight() - 1);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g2.draw(border);
		
		if (model.getType().equals(BoardType.STRIPES) || 
			model.getType().equals(BoardType.HOLES)) { 
			
			g2.setColor(Color.BLACK);
			g2.setFont(miniFont);
			
			int index = 1;
			
			for (int x = 12; x < model.getWidth() - 16;x+=16) {				
				g2.drawString(String.valueOf(index++), x,8);				
			}

			index = 1;
			
			for (int y = 18; y < model.getHeight(); y+=16) {				
				g2.drawString(String.valueOf(index++), 3,y);				
			}

		}
		
		if (editorMode.equals(EditorMode.CHECK_CONNECTIONS)) {			
			if (connectedLines != null) {
				for (Line line : connectedLines) {
					line.drawFat(g2);					
				}				
			}			
		}
		
		if (editorMode.equals(EditorMode.DRAW_LINE)) {

			g2.setColor(Color.BLUE);
			g2.setStroke(DEFAULT_STROKE);
			
			if (selectedPin != null) {
				
				if (drawing) {
					g2.drawRect(lineStopX - 8,lineStopY - 8, 16, 16);					
				}
				else {
					g2.drawRect(mouseX - 8,mouseY - 8, 16, 16);
				}
			}
			
		}
		
		super.paintComponents(g2);
	}

	/**
	 * Draws the selected item to the graphics context
	 * 
	 * @param layer the layer to be drawn
	 * @param g2d the graphics context
	 */
	private void drawLayer(Layer layer, Graphics2D g2d) {
		if (!layer.isVisible())
			return;
		
		DefaultTransformModel xmodel = null;
		
		if (zoomLayer != null) {
			TransformUI ui = (TransformUI)(Object) zoomLayer.getUI();
			xmodel = (DefaultTransformModel) ui.getModel();			
		}
		
		if (layer.getName().equalsIgnoreCase("board")) {
			if (backgroundImage != null) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.getOpacity()));
				g2d.drawImage(backgroundImage, 0, 0, this);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}
			else {
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
			
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.setStroke(BoardUtil.stroke_1_0f);
					
			Boolean dotGrid = (Boolean)Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.dotGrid");
			// draw grid
			if (gridVisible) {
				BoardUtil.drawGrid(g2d, raster, model.getWidth(), model.getHeight(),dotGrid.booleanValue());
			}
			
			return;
		}

		if (layer.getName().equalsIgnoreCase("ratsnest")) {
			if (layer.getImage() != null) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layer.getOpacity()));
				g2d.drawImage(layer.getImage(), 0, 0, this);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}
			return;
		}

		
		for (Item item : layer.getItems()) {
			
			if (xmodel != null)
				item.setMirror(xmodel.isMirror());
			
			item.setOpacity(layer.getOpacity());
			
			if (item instanceof Line) {
				Line line = (Line) item;
				if (line.getStroke() != null)
					g2d.setStroke(line.getStroke());
				else
					g2d.setStroke(BoardUtil.stroke_3_0f);
				g2d.setColor(item.getColor());
			}
			
			item.draw(g2d);
			
			if (selectedItems.contains(item)) {
				g2d.setColor(Color.GREEN);
				g2d.setStroke(BoardUtil.stroke_1_0f);
				item.drawHandles(g2d,8);
			}
		}
	}

	/**
	 * Setup the context menu
	 * 
	 * TODO : Should be externalized into an XML based configuration file.
	 */
	private void hookContextMenu() {
		popupMenu = new JPopupMenu();
		popupMenu.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		popupMenu.add(ctx.getBean(SetSelectModeCommand.class));
		popupMenu.add(ctx.getBean(SetDrawModeCommand.class));
//		popupMenu.add(ctx.getBean(SetMoveModeCommand.class));
		popupMenu.add(ctx.getBean(SetColorCommand.class));
		popupMenu.addSeparator();
		popupMenu.add(ctx.getBean(ToggleSnapToGridCommand.class));
		popupMenu.add(ctx.getBean(ToggleGridCommand.class));
		popupMenu.add(ctx.getBean(ToggleMirrorCommand.class));
		popupMenu.addSeparator();
		popupMenu.add(ctx.getBean(BrowsePartsCommand.class));
		popupMenu.add(ctx.getBean(AddResistorCommand.class));
		popupMenu.add(ctx.getBean(ExportImageCommand.class));
		popupMenu.add(ctx.getBean(AddTextCommand.class));
		popupMenu.addSeparator();
		popupMenu.add(ctx.getBean(CopyCommand.class));
		popupMenu.add(ctx.getBean(PasteCommand.class));
		popupMenu.addSeparator();
		popupMenu.add(ctx.getBean(UndoCommand.class));
		popupMenu.add(ctx.getBean(RedoCommand.class));
		popupMenu.addSeparator();
		popupMenu.add(deleteCommand);
		popupMenu.addSeparator();
		popupMenu.add(ctx.getBean(RotateCWCommand.class));
		popupMenu.add(ctx.getBean(RotateCCWCommand.class));
		popupMenu.add(ctx.getBean(FlipHorizontalCommand.class));
		popupMenu.add(ctx.getBean(FlipVerticalCommand.class));		
		popupMenu.addSeparator();
		popupMenu.add(ctx.getBean(MoveToLayerCommand.class));
		popupMenu.add(ctx.getBean(ConvertToPartCommand.class));
		popupMenu.add(ctx.getBean(ConvertToSymbolCommand.class));
		popupMenu.add(ctx.getBean(BreakSymbolCommand.class));
		popupMenu.add(ctx.getBean(AddSymbolToLibraryCommand.class));		
		popupMenu.addSeparator();
		// popupMenu.add(ctx.getBean(EditPartCommand.class));
		popupMenu.add(ctx.getBean(EditPropertiesCommand.class));
		popupMenu.addSeparator();
		
		final SimulateCircuitCommand simulateCommand = ctx.getBean(SimulateCircuitCommand.class);
		
		final JMenu simulatorMenu = new JMenu(resources.getResourceByKey("SimulateCircuitCommand.name"));
		simulatorMenu.setIcon(resources.getIcon("icon.simulate"));
		final SimulatorProvider provider = AppContext.getContext().getBean(SimulatorProvider.class);

		for (final SpiceSimulator simulator : provider.getElements()) {
			 
			final JMenuItem item = new JMenuItem(simulator.getName());
			
			item.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					simulateCommand.setSimulator(simulator);
					simulateCommand.execute(null);					
				}
			});
			
			simulatorMenu.add(item);
		}
		
		popupMenu.add(simulatorMenu);
		
	}

	/**
	 * Occurs usually if the right mouse button has been pressed
	 * 
	 * @param event
	 */
	private void handleContextClick(MouseEvent event) {
		Point point = event.getPoint();
		point = BoardUtil.mirrorTransform(point, zoomLayer, event);
		contextClickX = (int) point.x;
		contextClickY = (int) point.y;
		deleteCommand.setEnabled(selectedItem != null || selectedItems.size() > 0);
		popupMenu.show(zoomLayer, point.x, point.y);					
	}

	/**
	 * @return the snapToGrid
	 */
	public boolean isSnapToGrid() {
		return snapToGrid;
	}

	/**
	 * @param snapToGrid the snapToGrid to set
	 */
	public void setSnapToGrid(boolean snapToGrid) {
		this.snapToGrid = snapToGrid;
	}

	/**
	 * @return the selectedItem
	 */
	public Item getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Item item) {
		selectedItem = item;
	}

	/**
	 * @return the gridVisible
	 */
	public boolean isGridVisible() {
		return gridVisible;
	}

	/**
	 * @param gridVisible the gridVisible to set
	 */
	public void setGridVisible(boolean gridVisible) {
		this.gridVisible = gridVisible;
	}

	/**
	 * @return the selectedItems
	 */
	public ArrayList<Item> getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param selectedItems the selectedItems to set
	 */
	public void setSelectedItems(ArrayList<Item> selectedItems) {
		this.selectedItems = selectedItems;
	}

	/**
	 * @return the fileState
	 */
	public FileState getFileState() {
		return fileState;
	}

	/**
	 * @param fileState the fileState to set
	 */
	public void setFileState(FileState fileState) {
		if (view != null) {
			if (fileState.equals(FileState.DIRTY)) {
				saveBoardCommand.setEnabled(true);
				if (currentFile != null)
					view.getViewProperties().setTitle(currentFile.getName() + "*");
				else {
					if (!view.getViewProperties().getTitle().contains("*"))
						view.getViewProperties().setTitle(view.getViewProperties().getTitle() + "*");
				}
			}
			else {
				saveBoardCommand.setEnabled(false);
				if (currentFile != null)
					view.getViewProperties().setTitle(currentFile.getName());
				else
					view.getViewProperties().setTitle("untitled");
			}
		}
		this.fileState = fileState;
	}

	/**
	 * @return the currentFile
	 */
	public File getCurrentFile() {
		return currentFile;
	}

	/**
	 * @param currentFile the currentFile to set
	 */
	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * @return the backgroundImage
	 */
	public Image getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	/**
	 * @return the backgroundImageName
	 */
	public String getBackgroundImageName() {
		return backgroundImageName;
	}

	/**
	 * @param backgroundImageName the backgroundImageName to set
	 */
	public void setBackgroundImageName(String backgroundImageName) {
		this.backgroundImageName = backgroundImageName;
	}

	public void updateStatusBar() {
		log.debug("editor update");
		StringBuffer text = new StringBuffer();
		if (snapToGrid)
			text.append(resources.getResourceByKey("mode.snap"));
		else
			text.append(resources.getResourceByKey("mode.nosnap"));
		text.append(" | ");
		if (editorMode.equals(EditorMode.SELECT))
			text.append(resources.getResourceByKey("mode.select"));
		else if (editorMode.equals(EditorMode.DRAW_LINE))
			text.append(resources.getResourceByKey("mode.line"));
		else if (editorMode.equals(EditorMode.DRAW_RECTANGLE))
			text.append(resources.getResourceByKey("mode.box"));
		else if (editorMode.equals(EditorMode.DRAW_ELLIPSE))
			text.append(resources.getResourceByKey("mode.ellipse"));
		else if (editorMode.equals(EditorMode.CHECK_CONNECTIONS))
			text.append(resources.getResourceByKey("mode.check"));
		else if (editorMode.equals(EditorMode.MOVE))
			text.append(resources.getResourceByKey("mode.move"));		
		
		text.append(" | ");
		
		if (magnetic)
			text.append(resources.getResourceByKey("mode.magnetic"));
		
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(palette.getCurrentColor());
		g.fillRect(0, 0, 16, 16);
		win.getStatusLabel().setText(text.toString());
		win.getStatusLabel().setIcon(new ImageIcon(image));
		if (saveBoardCommand != null && fileState != null)
			saveBoardCommand.setEnabled(fileState.equals(FileState.DIRTY));
		refresh();
	}

	/**
	 * @return the model
	 */
	public BoardEditorModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(BoardEditorModel model) {
		this.model = model;
	}

	public void setEditorMode(EditorMode mode) {
		this.editorMode = mode;
	}

	public EditorMode getEditorMode() {
		return this.editorMode;
	}

	/**
	 * @return the contextClickX
	 */
	public int getContextClickX() {
		return contextClickX;
	}

	/**
	 * @return the contextClickY
	 */
	public int getContextClickY() {
		return contextClickY;
	}

	public void refresh() {
		invalidate();
		repaint();
	}

	public int getRaster() {
		return raster;
	}

	public void setRaster(int raster) {
		this.raster = raster;
	}

	public JXLayer<?> getZoomLayer() {
		return zoomLayer;
	}

	public void setZoomLayer(JXLayer<?> zoomLayer) {
		this.zoomLayer = zoomLayer;		
	}

	public void updateEditCommands() {
		boolean isSingleItemSelected = selectedItem != null && !editPart;
		ctx.getBean(RotateCWCommand.class).setEnabled(isSingleItemSelected && !(selectedItem instanceof Line));
		ctx.getBean(RotateCCWCommand.class).setEnabled(isSingleItemSelected && !(selectedItem instanceof Line));
		ctx.getBean(DeleteCommand.class).setEnabled(isSingleItemSelected || selectedItems.size() > 0);
		ctx.getBean(CopyCommand.class).setEnabled(isSingleItemSelected || selectedItems.size() > 0);
		ctx.getBean(DuplicateCommand.class).setEnabled(isSingleItemSelected || selectedItems.size() > 0);
		ctx.getBean(MoveToLayerCommand.class).setEnabled(isSingleItemSelected || selectedItems.size() > 0);
		ctx.getBean(ConvertToPartCommand.class).setEnabled((isSingleItemSelected || selectedItems.size() > 0) && !(selectedItem instanceof Symbol));
		ctx.getBean(PasteCommand.class).setEnabled(AppContext.getClipboard().size() > 0);
		
		boolean canConvert = true;

		for (Item item : selectedItems) {
			if ((!(item instanceof Shape) && !(item instanceof TextPart)) || item instanceof Resistor || item instanceof Diode) {
				canConvert = false;
				break;
			}					
		}
		
		ctx.getBean(ConvertToSymbolCommand.class).setEnabled(canConvert && selectedItems.size() > 0);
		ctx.getBean(BreakSymbolCommand.class).setEnabled(isSingleItemSelected && selectedItem instanceof Symbol);
		ctx.getBean(AddSymbolToLibraryCommand.class).setEnabled(isSingleItemSelected && selectedItem instanceof Symbol);
		ctx.getBean(FlipHorizontalCommand.class).setEnabled(isSingleItemSelected && selectedItem instanceof Symbol);
		ctx.getBean(FlipVerticalCommand.class).setEnabled(isSingleItemSelected && selectedItem instanceof Symbol);
	}

	public void addEditorChangeListener(EditorChangedListener listener) {
		listeners.add(listener);
	}

	public void notifyListeners(EventType type) {
		BoardEditor editor = null;
		if (type == EventType.EDITOR_CHANGED)
			editor = this;
		for (EditorChangedListener ecl : listeners) {
			ecl.editorChanged(new EditorChangedEvent(editor, type));
		}
	}

	public boolean isMagnetic() {
		return magnetic;
	}

	public void setMagnetic(boolean magnetic) {
		this.magnetic = magnetic;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(drawing) {
			handleDrawEvent(e);
			refresh();
		}		
		else {
			handleMouseMoveEvent(e);
			refresh();
		}
	}

	private void handleMouseMoveEvent(MouseEvent e) {			
		
		int x = e.getX();
		int y = e.getY();
		
		if (snapToGrid) {
			x = BoardUtil.snap(x, raster);
			y = BoardUtil.snap(y, raster);
		}
		
		mouseX = x;
		mouseY = y;
		
		selectedPin = EditorUtils.findPin(x, y, this);
		
	}
	
}

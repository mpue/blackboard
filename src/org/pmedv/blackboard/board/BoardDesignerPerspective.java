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
package org.pmedv.blackboard.board;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.SoftBlueIceDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.pmedv.blackboard.BoardUtil;
import org.pmedv.blackboard.EditorUtils;
import org.pmedv.blackboard.ShapeStyle;
import org.pmedv.blackboard.app.FileState;
import org.pmedv.blackboard.commands.AddDiodeCommand;
import org.pmedv.blackboard.commands.AddResistorCommand;
import org.pmedv.blackboard.commands.AddTextCommand;
import org.pmedv.blackboard.commands.ChooseColorCommand;
import org.pmedv.blackboard.commands.CreateBoardCommand;
import org.pmedv.blackboard.commands.OpenBoardCommand;
import org.pmedv.blackboard.commands.SaveBoardCommand;
import org.pmedv.blackboard.commands.ShowLayersCommand;
import org.pmedv.blackboard.components.BoardEditor;
import org.pmedv.blackboard.components.Ellipse;
import org.pmedv.blackboard.components.Item;
import org.pmedv.blackboard.components.Line;
import org.pmedv.blackboard.components.LineEdgeType;
import org.pmedv.blackboard.components.Part;
import org.pmedv.blackboard.components.Shape;
import org.pmedv.blackboard.events.EditorChangedEvent.EventType;
import org.pmedv.blackboard.panels.ModelListPanel;
import org.pmedv.blackboard.panels.ShapePropertiesPanel;
import org.pmedv.blackboard.panels.SymbolListPanel;
import org.pmedv.blackboard.tools.PartFactory;
import org.pmedv.core.beans.FileList;
import org.pmedv.core.components.IMemento;
import org.pmedv.core.context.AppContext;
import org.pmedv.core.gui.ApplicationWindowAdvisor;
import org.pmedv.core.perspectives.AbstractPerspective;
import org.pmedv.core.preferences.Preferences;
import org.pmedv.core.provider.ApplicationWindowConfigurationProvider;
import org.pmedv.core.services.ResourceService;
import org.springframework.context.ApplicationContext;

public class BoardDesignerPerspective extends AbstractPerspective implements IMemento {

	private static final long serialVersionUID = -200105829758192919L;

	private static final Log log = LogFactory.getLog(BoardDesignerPerspective.class);

	private ApplicationContext ctx;
	private ResourceService resources;
	private ApplicationWindowAdvisor advisor;
	private ApplicationWindowConfigurationProvider configProvider;

	private JSplitPane horizontalSplitPane;
	private JTabbedPane toolTabPane;

	private boolean providerInit = false;
	
	private RSyntaxTextArea commandArea;
	
	public BoardDesignerPerspective() {
		ID = "boardDesignerPerspective";
		setName("boardDesignerPerspective");
		log.info("Initializing " + getName());
		configProvider = AppContext.getContext().getBean(ApplicationWindowConfigurationProvider.class);
	}

	@Override
	protected void initializeComponents() {
		setLayout(new BorderLayout());
		toolTabPane = new JTabbedPane(JTabbedPane.BOTTOM);
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		ctx = AppContext.getContext();
		resources = ctx.getBean(ResourceService.class);
		advisor = ctx.getBean(ApplicationWindowAdvisor.class);

		final String position = (String) Preferences.values.get("org.pmedv.blackboard.BoardDesignerPerspective.layerPanelPlacement");

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				viewMap = new ViewMap();

				rootWindow = DockingUtil.createRootWindow(viewMap, true);
				rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
				rootWindow.getWindowProperties().setMinimizeEnabled(true);

				DockingWindowsTheme theme = new SoftBlueIceDockingTheme();

				rootWindow.getRootWindowProperties().addSuperObject(theme.getRootWindowProperties());
				rootWindow.getWindowProperties().getTabProperties().getHighlightedButtonProperties().getCloseButtonProperties().setVisible(false);
				rootWindow.getWindowProperties().getTabProperties().getNormalButtonProperties().getCloseButtonProperties().setVisible(false);

				editorArea = new TabWindow();
				editorArea.getWindowProperties().getTabProperties().getNormalButtonProperties().getCloseButtonProperties().setVisible(false);
				editorArea.getWindowProperties().getTabProperties().getHighlightedButtonProperties().getCloseButtonProperties().setVisible(false);
				editorArea.getWindowProperties().getTabProperties().getNormalButtonProperties().getMinimizeButtonProperties().setVisible(true);
				editorArea.getWindowProperties().getTabProperties().getHighlightedButtonProperties().getMinimizeButtonProperties().setVisible(true);

				DockingWindowAdapter dockingAdapter = new DockingWindowAdapter() {
					@Override
					public void windowClosing(DockingWindow window) throws OperationAbortedException {

					}
				};

				editorArea.addListener(dockingAdapter);
				setDockingListener(dockingAdapter);

				rootWindow.setWindow(editorArea);

				
				if (position.equalsIgnoreCase("left")) {
					horizontalSplitPane.setRightComponent(rootWindow);
				} else {
					horizontalSplitPane.setLeftComponent(rootWindow);
				}

				advisor.setCurrentEditorArea(editorArea);

			}

		});

		JXTaskPaneContainer taskpanecontainer = new JXTaskPaneContainer();
		taskpanecontainer.setBackground(new Color(182, 191, 205));

		JXTaskPane shapePane = new JXTaskPane();
		shapePane.setTitle(resources.getResourceByKey("BoardDesignerPerspective.shapes.title"));
		shapePane.add(ctx.getBean(ShapePropertiesPanel.class));
		taskpanecontainer.add(shapePane);

		ctx.getBean(ShapePropertiesPanel.class).getStartLineCombo().setSelectedItem(LineEdgeType.STRAIGHT);
		ctx.getBean(ShapePropertiesPanel.class).getEndLineCombo().setSelectedItem(LineEdgeType.STRAIGHT);
		ctx.getBean(ShapePropertiesPanel.class).getThicknessCombo().setSelectedItem(new BasicStroke(2.0f));
		
		JXTaskPane layerPane = new JXTaskPane();
		layerPane.setTitle(resources.getResourceByKey("BoardDesignerPerspective.layers"));
		layerPane.add(ctx.getBean(ShowLayersCommand.class).getLayerPanel());
		taskpanecontainer.add(layerPane);

		JScrollPane scrollPane = new JScrollPane(taskpanecontainer);

		if (position.equalsIgnoreCase("left")) {
			horizontalSplitPane.setLeftComponent(toolTabPane);
		} else {
			horizontalSplitPane.setRightComponent(toolTabPane);
		}

		horizontalSplitPane.setOneTouchExpandable(true);
		horizontalSplitPane.setDividerSize(10);

		toolTabPane.addTab(resources.getResourceByKey("tooltab.forms"),resources.getIcon("icon.paint"), scrollPane);
		
		final SymbolListPanel symbolListPanel = ctx.getBean(SymbolListPanel.class);
		toolTabPane.addTab(resources.getResourceByKey("tooltab.symbols"),resources.getIcon("icon.symbols"), symbolListPanel);

		final ModelListPanel modelListPanel = ctx.getBean(ModelListPanel.class);
		toolTabPane.addTab(resources.getResourceByKey("tooltab.models"),resources.getIcon("icon.model"), modelListPanel);
		
		add(horizontalSplitPane, BorderLayout.CENTER);

		commandArea = new RSyntaxTextArea();
		commandArea.setRows(1);		
		commandArea.setColumns(100);
		
		JPanel commandPanel = new JPanel(new BorderLayout());
		commandPanel.add(new JLabel("Command :"), BorderLayout.WEST);
		commandPanel.add(commandArea, BorderLayout.CENTER);
		commandArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,2,2,2),BorderFactory.createLineBorder(Color.BLACK)));
		add(commandPanel, BorderLayout.NORTH);
		setupAutoComplete();
		
		commandArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					if (commandArea.getText().startsWith("add ") && commandArea.getText().length() > 4 && commandArea.hasFocus()) {

						PartFactory pf = AppContext.getContext().getBean(PartFactory.class);

						String tokens[] = commandArea.getText().split(" ");
						StringBuffer partName = new StringBuffer();

						for (int i = 1; i < tokens.length; i++) {
							partName.append(tokens[i] + " ");
						}

						String name = partName.toString().trim();

						if (pf.getPartnames().contains(name)) {
							e.consume();
							commandArea.setText("");
							BoardUtil.addPart(name, EditorUtils.getCurrentActiveEditor());
						}

					}
					else if(commandArea.getText().equals("new")) {
						e.consume();
						commandArea.setText("");
						AppContext.getContext().getBean(CreateBoardCommand.class).execute(null);
					}
					else if(commandArea.getText().equals("resistor")) {
						e.consume();
						commandArea.setText("");
						AppContext.getContext().getBean(AddResistorCommand.class).execute(null);
					}
					else if(commandArea.getText().equals("diode")) {
						e.consume();
						commandArea.setText("");
						AppContext.getContext().getBean(AddDiodeCommand.class).execute(null);
					}
					else if(commandArea.getText().equals("text")) {
						e.consume();
						commandArea.setText("");
						AppContext.getContext().getBean(AddTextCommand.class).execute(null);
					}
					else if(commandArea.getText().equals("open")) {
						e.consume();
						commandArea.setText("");
						new OpenBoardCommand().execute(null);
					}
					else if(commandArea.getText().equals("save")) {
						e.consume();
						commandArea.setText("");
						AppContext.getContext().getBean(SaveBoardCommand.class).execute(null);
					}
					else if(commandArea.getText().equals("color")) {
						e.consume();
						commandArea.setText("");
						AppContext.getContext().getBean(ChooseColorCommand.class).execute(null);
					}

				}
				
			}

		});
		
		
		horizontalSplitPane.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equalsIgnoreCase("dividerLocation")) {
					configProvider.getConfig().setDividerLocation(horizontalSplitPane.getDividerLocation());
				}
			}
		});
		


		ctx.getBean(ShapePropertiesPanel.class).getObjectField().setText(resources.getResourceByKey("ShapePropertiesPanel.items.none"));
		ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().setEnabled(false);
		ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().setEnabled(false);

		initListeners();

	}

	private void initListeners() {
		ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (EditorUtils.getCurrentActiveEditor() != null) {

					BoardEditor editor = EditorUtils.getCurrentActiveEditor();

					if (editor.getSelectedItem() != null && editor.getSelectedItem() instanceof Ellipse) {
						Ellipse el = (Ellipse) editor.getSelectedItem();
						int angle = ((Integer) ctx.getBean(ShapePropertiesPanel.class).getStartAngleSpinner().getValue()).intValue();
						el.setStartAngle(angle);
						editor.refresh();
						editor.setFileState(FileState.DIRTY);
					}

				}
			}
		});

		ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				BoardEditor editor = EditorUtils.getCurrentActiveEditor();
				if (EditorUtils.getCurrentActiveEditor() != null) {
					if (editor.getSelectedItem() != null && editor.getSelectedItem() instanceof Ellipse) {
						Ellipse el = (Ellipse) editor.getSelectedItem();
						int rotation = ((Integer) ctx.getBean(ShapePropertiesPanel.class).getRotationSpinner().getValue()).intValue();
						el.setRotation(rotation);
						editor.refresh();
						editor.setFileState(FileState.DIRTY);
					}
				}
			}
		});

		ctx.getBean(ShapePropertiesPanel.class).getStyleCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (EditorUtils.getCurrentActiveEditor() != null) {
					BoardEditor editor = EditorUtils.getCurrentActiveEditor();
					if (editor.getSelectedItem() != null) {
						if (editor.getSelectedItem() instanceof Shape) {
							Shape shape = (Shape) editor.getSelectedItem();
							shape.setStyle((ShapeStyle) ctx.getBean(ShapePropertiesPanel.class).getStyleCombo().getSelectedItem());
							editor.refresh();
							editor.setFileState(FileState.DIRTY);
						}
					}
					else if (editor.getSelectedItems().size() > 0) {
						for (Item item : editor.getSelectedItems()) {
							if (item instanceof Shape) {
								Shape shape = (Shape)item;
								shape.setStyle((ShapeStyle) ctx.getBean(ShapePropertiesPanel.class).getStyleCombo().getSelectedItem());								
							}
						}
						editor.setFileState(FileState.DIRTY);
						editor.refresh();
					}
				}
			}
		});

		ctx.getBean(ShapePropertiesPanel.class).getThicknessCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ShapePropertiesPanel panel = ctx.getBean(ShapePropertiesPanel.class);
				
				if (EditorUtils.getCurrentActiveEditor() != null) {
					BoardEditor editor = EditorUtils.getCurrentActiveEditor();
					if (editor.getSelectedItem() != null) {
						if (editor.getSelectedItem() instanceof Shape) {
							Shape shape = (Shape) editor.getSelectedItem();
							shape.setStroke((BasicStroke) panel.getThicknessCombo().getSelectedItem());
							editor.refresh();
						}
					}
					else if (editor.getSelectedItems().size() > 0) {
						for (Item item : editor.getSelectedItems()) {
							if (item instanceof Shape) {
								Shape shape = (Shape)item;
								shape.setStroke((BasicStroke) panel.getThicknessCombo().getSelectedItem());								
							}							
						}
						editor.setFileState(FileState.DIRTY);
						editor.refresh();
					}

				}
				// Preserve settings				
				if (panel.getThicknessCombo().getSelectedItem() != null) {
					panel.setLastSelectedStroke((BasicStroke)panel.getThicknessCombo().getSelectedItem());					
				}
			}
		});

		ctx.getBean(ShapePropertiesPanel.class).getStartLineCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ShapePropertiesPanel panel = ctx.getBean(ShapePropertiesPanel.class);
				
				if (EditorUtils.getCurrentActiveEditor() != null) {
					BoardEditor editor = EditorUtils.getCurrentActiveEditor();
					if (editor.getSelectedItem() != null) {
						if (editor.getSelectedItem() instanceof Line) {
							Line line = (Line) editor.getSelectedItem();
							line.setStartType((LineEdgeType) panel.getStartLineCombo().getSelectedItem());
							editor.refresh();
						}
					}
					else if (editor.getSelectedItems().size() > 0) {
						for (Item item : editor.getSelectedItems()) {
							if (item instanceof Line) {
								Line line = (Line) item;
								line.setStartType((LineEdgeType) panel.getStartLineCombo().getSelectedItem());
							}
						}
						editor.setFileState(FileState.DIRTY);
						editor.refresh();
					}

				}
				// Preserve settings				
				if (panel.getStartLineCombo().getSelectedItem() != null) {
					panel.setLastSelectedLineStartStyle((LineEdgeType)panel.getStartLineCombo().getSelectedItem());	
				}				
			}
		});

		ctx.getBean(ShapePropertiesPanel.class).getEndLineCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ShapePropertiesPanel panel = ctx.getBean(ShapePropertiesPanel.class);
				
				if (EditorUtils.getCurrentActiveEditor() != null) {
					BoardEditor editor = EditorUtils.getCurrentActiveEditor();
					if (editor.getSelectedItem() != null) {
						if (editor.getSelectedItem() instanceof Line) {
							Line line = (Line) editor.getSelectedItem();
							line.setEndType((LineEdgeType) panel.getEndLineCombo().getSelectedItem());
							editor.refresh();
						}
					}
					else if (editor.getSelectedItems().size() > 0) {
						for (Item item : editor.getSelectedItems()) {
							if (item instanceof Line) {
								Line line = (Line) item;
								line.setEndType((LineEdgeType) panel.getEndLineCombo().getSelectedItem());
							}
						}
						editor.setFileState(FileState.DIRTY);
						editor.refresh();
					}
				}
				// Preserve settings
				if (panel.getEndLineCombo().getSelectedItem() != null) {
					panel.setLastSelectedLineEndStyle((LineEdgeType)panel.getEndLineCombo().getSelectedItem());	
				}
				
			}
		});
	}

	@Override
	public void loadState() {

		String dir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";

		try {
			final Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(FileList.class).createUnmarshaller();
			final FileList files = (FileList) u.unmarshal(new FileInputStream(new File(dir + ID + ".xml")));
			
			int count = 0;
			
			for (final String path : files.getFiles()) {
				log.info("Opening editor for :" + path);						
				OpenBoardCommand cmd = new OpenBoardCommand(path, new File(path));
				
				// the last command needs a trigger for perspective restore
				if (count == files.getFiles().size() - 1) {
					cmd.addPostConfigurator(new LoadViewStateEvent());
					cmd.addPostConfigurator(new ViewStateDoneEvent());
				}
				
				cmd.execute(null);
				count++;
			}
			
			horizontalSplitPane.setDividerLocation(configProvider.getConfig().getDividerLocation());

		} 
		catch (Exception e) {
			log.info("Could not restore perspective state :" + e.getMessage());
		}

	}

	@Override
	public void saveState() {

		String dir = System.getProperty("user.home") + "/." + AppContext.getName() + "/";

		FileList files = new FileList();

		ArrayList<BoardEditor> editors = EditorUtils.getCurrentPerspectiveOpenEditors(this);

		for (BoardEditor editor : editors) {

			if (editor.getCurrentFile() != null) {
				log.info("Adding editor : " + editor.getCurrentFile());
				files.getFiles().add(editor.getCurrentFile().getAbsolutePath());
			}

		}

		try {

			Marshaller m = (Marshaller) JAXBContext.newInstance(FileList.class).createMarshaller();
			m.marshal(files, new FileOutputStream(new File(dir + ID + ".xml")));
			if (viewMap.getViewCount() > 0)
				saveViewState();
			else
				deleteViewState();
		} catch (Exception e) {
			log.info("Could not persist perspective state.");
		}

	}
	
	private class LoadViewStateEvent implements Runnable {
		@Override
		public void run() {
			loadViewState();
		}		
	}
	
	private class ViewStateDoneEvent implements Runnable {
		@Override
		public void run() {

			ArrayList<View> views = EditorUtils.getCurrentPerspectiveViews(BoardDesignerPerspective.this);

			log.debug("open views " + views.size());

			ArrayList<BoardEditor> openEditors = EditorUtils.getCurrentPerspectiveOpenEditors(BoardDesignerPerspective.this);

			/**
			 * Now for simplicity, select the first available editor, set
			 * the current editor area, request the focus for the current
			 * editor and notify all pending listeners that the current
			 * editor has changed.
			 * 
			 */
			if (openEditors.size() >= 1) {
				BoardEditor editor = openEditors.get(0);
				log.info("Selected editor : " + editor.getCurrentFile());
				TabWindow tw = DockingUtil.getTabWindowFor(editor.getView());
				advisor.setCurrentEditorArea(tw);
				editor.getView().requestFocus();
				editor.notifyListeners(EventType.EDITOR_CHANGED);
			}
			/**
			 * Rebuild titles and icons.
			 */
			for (BoardEditor editor : EditorUtils.getCurrentPerspectiveOpenEditors(BoardDesignerPerspective.this)) {
				editor.getView().getViewProperties().setTitle(editor.getCurrentFile().getName());
				editor.getView().getViewProperties().setIcon(resources.getIcon("icon.editor"));
			}

		}
	}
	
	private void setupAutoComplete() {
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(commandArea);		
	}

	private CompletionProvider createCompletionProvider() {
		
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		provider.addCompletion(new BasicCompletion(provider, "add"));
		provider.addCompletion(new BasicCompletion(provider, "new"));
		provider.addCompletion(new BasicCompletion(provider, "resistor"));
		provider.addCompletion(new BasicCompletion(provider, "diode"));
		provider.addCompletion(new BasicCompletion(provider, "open"));
		provider.addCompletion(new BasicCompletion(provider, "save"));
		provider.addCompletion(new BasicCompletion(provider, "text"));
		provider.addCompletion(new BasicCompletion(provider, "color"));

		try {
			ArrayList<Part> parts = AppContext.getContext().getBean(PartFactory.class).getAvailableParts();			
			for (Part part : parts) {
				provider.addCompletion(new BasicCompletion(provider, part.getName()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		providerInit = true;
		
		return provider;

	}

	/**
	 * @return the providerInit
	 */
	public boolean isProviderInit() {
		return providerInit;
	}
	
}

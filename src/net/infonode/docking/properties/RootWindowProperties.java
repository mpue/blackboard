/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */


// $Id: RootWindowProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $
package net.infonode.docking.properties;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import net.infonode.docking.DefaultButtonFactories;
import net.infonode.docking.action.CloseWithAbortWindowAction;
import net.infonode.docking.action.DockWithAbortWindowAction;
import net.infonode.docking.action.MaximizeWithAbortWindowAction;
import net.infonode.docking.action.MinimizeWithAbortWindowAction;
import net.infonode.docking.action.RestoreViewWithAbortTitleBarAction;
import net.infonode.docking.action.RestoreWithAbortWindowAction;
import net.infonode.docking.action.UndockWithAbortWindowAction;
import net.infonode.docking.drop.AcceptAllDropFilter;
import net.infonode.docking.internalutil.InternalDockingUtil;
import net.infonode.gui.DynamicUIManager;
import net.infonode.gui.DynamicUIManagerListener;
import net.infonode.gui.UIManagerUtil;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.gui.componentpainter.SolidColorComponentPainter;
import net.infonode.properties.base.Property;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapListener;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.tabbedpanel.TabAreaVisiblePolicy;
import net.infonode.tabbedpanel.TabDropDownListVisiblePolicy;
import net.infonode.tabbedpanel.TabSelectTrigger;
import net.infonode.tabbedpanel.TabbedPanelButtonProperties;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.TabbedUIDefaults;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import net.infonode.tabbedpanel.titledtab.TitledTabSizePolicy;
import net.infonode.util.Direction;

/**
 * Properties and property values for a root window.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 */
public class RootWindowProperties extends PropertyMapContainer {
  /**
   * The size of the default window tab button icons.
   */
  public static final int DEFAULT_WINDOW_TAB_BUTTON_ICON_SIZE = InternalDockingUtil.DEFAULT_BUTTON_ICON_SIZE;

  /**
   * Property group containing all root window properties.
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Root Window Properties", "");

  /**
   * The root window component property values.
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Component Properties",
                              "The root window component property values.",
                              ComponentProperties.PROPERTIES);

  /**
   * The root window shaped panel property values.
   */
  public static final PropertyMapProperty SHAPED_PANEL_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Shaped Panel Properties",
                              "The root window shaped panel property values.",
                              ShapedPanelProperties.PROPERTIES);

  /**
   * The window area component property values. The window area is the area inside the WindowBars.
   */
  public static final PropertyMapProperty WINDOW_AREA_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Window Area Properties",
                              "The window area component property values. The window area is the area inside the WindowBars.",
                              ComponentProperties.PROPERTIES);

  /**
   * The window area shaped panel property values. The window area is the area inside the WindowBars.
   */
  public static final PropertyMapProperty WINDOW_AREA_SHAPED_PANEL_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Window Area Shaped Panel Properties",
                              "The window area shaped panel property values. The window area is the area inside the WindowBars.",
                              ShapedPanelProperties.PROPERTIES);

  /**
   * Shaped panel properties for the drag rectangle. Setting a painter disables the default drag rectangle.
   *
   * @since IDW 1.2.0
   */
  public static final PropertyMapProperty DRAG_RECTANGLE_SHAPED_PANEL_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Drag Rectangle Shaped Panel Properties",
                              "Shaped panel properties for the drag rectangle. Setting a painter disables the default drag rectangle.",
                              ShapedPanelProperties.PROPERTIES);

  /**
   * The width of the drag rectangle border.
   */
  public static final IntegerProperty DRAG_RECTANGLE_BORDER_WIDTH =
      IntegerProperty.createPositive(PROPERTIES,
                                     "Drag Rectangle Border Width",
                                     "The width of the drag rectangle border. The drag rectangle will only " +
                                     "be painted if the painter of the '" +
                                     DRAG_RECTANGLE_SHAPED_PANEL_PROPERTIES.getName() +
                                     "' property is not set.",
                                     2,
                                     PropertyMapValueHandler.INSTANCE);

  /**
   * The window drag label property values.
   */
  public static final PropertyMapProperty DRAG_LABEL_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Drag Label Properties",
                              "The window drag label property values.",
                              ComponentProperties.PROPERTIES);

  /**
   * Default property values for DockingWindows inside this root window.
   */
  public static final PropertyMapProperty DOCKING_WINDOW_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Docking Window Properties",
                              "Default property values for DockingWindows inside this RootWindow.",
                              DockingWindowProperties.PROPERTIES);

  /**
   * Default property values for tab windows inside this root window.
   */
  public static final PropertyMapProperty TAB_WINDOW_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Tab Window Properties",
                              "Default property values for TabWindows inside this RootWindow.",
                              TabWindowProperties.PROPERTIES);

  /**
   * Default property values for split windows inside this root window.
   */
  public static final PropertyMapProperty SPLIT_WINDOW_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Split Window Properties",
                              "Default property values for SplitWindows inside this RootWindow.",
                              SplitWindowProperties.PROPERTIES);

  /**
   * Default property values for floating windows inside this root window.
   *
   * @since IDW 1.4.0
   */
  public static final PropertyMapProperty FLOATING_WINDOW_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Floating Window Properties",
                              "Default property values for FloatingWindows inside this RootWindow.",
                              FloatingWindowProperties.PROPERTIES);

  /**
   * Default property values for views inside this root window.
   */
  public static final PropertyMapProperty VIEW_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "View Properties",
                              "Default property values for Views inside this RootWindow.",
                              ViewProperties.PROPERTIES);

  /**
   * Double clicking on a minimized window in a window bar restores it.
   */
  public static final BooleanProperty DOUBLE_CLICK_RESTORES_WINDOW =
      new BooleanProperty(PROPERTIES,
                          "Double Click Restores Window",
                          "Double clicking on a minimized window in a window bar restores it.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * If true, makes it possible for the user to create tab windows inside other tab windows when dragging windows.
   * If false, only one level of tab windows is allowed.
   * Changing the value of this property does not alter the window tree.
   */
  public static final BooleanProperty RECURSIVE_TABS_ENABLED =
      new BooleanProperty(PROPERTIES,
                          "Recursive Tabs Enabled",
                          "If true, makes it possible for the user to create tab windows inside other tab windows by " +
                          "dragging windows. If false, only one level of tab windows is allowed. Changing the value of " +
                          "this property does not alter the window tree.",
                          PropertyMapValueHandler.INSTANCE);

  /**
   * Inside this distance from the window edge a mouse drag will trigger a window split.
   */
  public static final IntegerProperty EDGE_SPLIT_DISTANCE =
      IntegerProperty.createPositive(PROPERTIES,
                                     "Edge Split Distance",
                                     "Inside this distance from the window edge a mouse drag will trigger a window split.",
                                     3,
                                     PropertyMapValueHandler.INSTANCE);

  /**
   * Key code for the key that aborts a drag.
   */
  public static final IntegerProperty ABORT_DRAG_KEY =
      IntegerProperty.createPositive(PROPERTIES,
                                     "Abort Drag Key Code",
                                     "Key code for the key that aborts a drag.",
                                     3,
                                     PropertyMapValueHandler.INSTANCE);

  /**
   * The default window bar property values.
   *
   * @since IDW 1.1.0
   */
  public static final PropertyMapProperty WINDOW_BAR_PROPERTIES =
      new PropertyMapProperty(PROPERTIES,
                              "Window Bar Properties",
                              "Default property values for WindowBars inside this RootWindow.",
                              WindowBarProperties.PROPERTIES);

  private static RootWindowProperties DEFAULT_VALUES;

  private static void setTabProperties() {
    WindowTabProperties tabProperties = DEFAULT_VALUES.getTabWindowProperties().getTabProperties();

    PropertyMapProperty[] buttonProperties = {WindowTabStateProperties.CLOSE_BUTTON_PROPERTIES,
                                              WindowTabStateProperties.MINIMIZE_BUTTON_PROPERTIES,
                                              WindowTabStateProperties.RESTORE_BUTTON_PROPERTIES,
                                              WindowTabStateProperties.UNDOCK_BUTTON_PROPERTIES,
                                              WindowTabStateProperties.DOCK_BUTTON_PROPERTIES};

    for (int i = 0; i < buttonProperties.length; i++) {
      for (int j = 0; j < WindowTabButtonProperties.PROPERTIES.getPropertyCount(); j++) {
        Property property = WindowTabButtonProperties.PROPERTIES.getProperty(j);

        // Highlighted properties inherits from normal properties
        buttonProperties[i].get(tabProperties.getHighlightedButtonProperties().getMap()).
            createRelativeRef(property,
                              buttonProperties[i].get(tabProperties.getNormalButtonProperties().getMap()),
                              property);

        // Focus properties inherits from highlight properties
        buttonProperties[i].get(tabProperties.getFocusedButtonProperties().getMap()).
            createRelativeRef(property,
                              buttonProperties[i].get(tabProperties.getHighlightedButtonProperties().getMap()),
                              property);
      }
    }

    tabProperties.getTitledTabProperties().getNormalProperties()
        .setToolTipEnabled(true)
        .getComponentProperties().setInsets(new Insets(0, 3, 0, 2));

    tabProperties.getTitledTabProperties().setSizePolicy(TitledTabSizePolicy.INDIVIDUAL_SIZE);

    tabProperties.getNormalButtonProperties().getCloseButtonProperties()
        .setFactory(DefaultButtonFactories.getCloseButtonFactory())
        .setTo(CloseWithAbortWindowAction.INSTANCE);

    tabProperties.getNormalButtonProperties().getUndockButtonProperties()
        .setFactory(DefaultButtonFactories.getUndockButtonFactory())
        .setVisible(false)
        .setTo(UndockWithAbortWindowAction.INSTANCE);

    tabProperties.getNormalButtonProperties().getDockButtonProperties()
        .setFactory(DefaultButtonFactories.getDockButtonFactory())
        .setVisible(false)
        .setTo(DockWithAbortWindowAction.INSTANCE);

    tabProperties.getNormalButtonProperties().getRestoreButtonProperties()
        .setFactory(DefaultButtonFactories.getRestoreButtonFactory())
        .setVisible(false)
        .setTo(RestoreWithAbortWindowAction.INSTANCE);

    tabProperties.getNormalButtonProperties().getMinimizeButtonProperties()
        .setFactory(DefaultButtonFactories.getMinimizeButtonFactory())
        .setVisible(false)
        .setTo(MinimizeWithAbortWindowAction.INSTANCE);

    tabProperties.getTitledTabProperties().setFocusable(false);
    tabProperties.getHighlightedButtonProperties().getCloseButtonProperties().setVisible(true);
    tabProperties.getHighlightedButtonProperties().getMinimizeButtonProperties().setVisible(true);
    tabProperties.getHighlightedButtonProperties().getRestoreButtonProperties().setVisible(true);
    tabProperties.getHighlightedButtonProperties().getUndockButtonProperties().setVisible(true);
    tabProperties.getHighlightedButtonProperties().getDockButtonProperties().setVisible(true);
  }

  private static void setTabbedPanelProperties() {
    TabWindowProperties tabWindowProperties = DEFAULT_VALUES.getTabWindowProperties();

    tabWindowProperties.getTabbedPanelProperties()
        .setTabDropDownListVisiblePolicy(TabDropDownListVisiblePolicy.TABS_NOT_VISIBLE)
        .setTabSelectTrigger(TabSelectTrigger.MOUSE_RELEASE)
        .setEnsureSelectedTabVisible(true)
        .setTabReorderEnabled(false)
        .setHighlightPressedTab(false)
        .setShadowEnabled(true);

    tabWindowProperties.getTabbedPanelProperties().getTabAreaComponentsProperties().getComponentProperties()
        .setInsets(new Insets(1, 3, 1, 3));

    tabWindowProperties.getCloseButtonProperties()
        .setFactory(DefaultButtonFactories.getCloseButtonFactory())
        .setVisible(true)
        .setTo(CloseWithAbortWindowAction.INSTANCE);

    tabWindowProperties.getRestoreButtonProperties()
        .setFactory(DefaultButtonFactories.getRestoreButtonFactory())
        .setVisible(true)
        .setTo(RestoreWithAbortWindowAction.INSTANCE);

    tabWindowProperties.getMinimizeButtonProperties()
        .setFactory(DefaultButtonFactories.getMinimizeButtonFactory())
        .setVisible(true)
        .setTo(MinimizeWithAbortWindowAction.INSTANCE);

    tabWindowProperties.getMaximizeButtonProperties()
        .setFactory(DefaultButtonFactories.getMaximizeButtonFactory())
        .setVisible(true)
        .setTo(MaximizeWithAbortWindowAction.INSTANCE);

    tabWindowProperties.getUndockButtonProperties()
        .setFactory(DefaultButtonFactories.getUndockButtonFactory())
        .setVisible(true)
        .setTo(UndockWithAbortWindowAction.INSTANCE);

    tabWindowProperties.getDockButtonProperties()
        .setFactory(DefaultButtonFactories.getDockButtonFactory())
        .setVisible(true)
        .setTo(DockWithAbortWindowAction.INSTANCE);

    TabbedPanelButtonProperties buttonProps = tabWindowProperties.getTabbedPanelProperties().getButtonProperties();
    buttonProps.getTabDropDownListButtonProperties().setToolTipText("Tab List");
    buttonProps.getScrollLeftButtonProperties().setToolTipText("Scroll Left");
    buttonProps.getScrollRightButtonProperties().setToolTipText("Scroll Right");
    buttonProps.getScrollUpButtonProperties().setToolTipText("Scroll Up");
    buttonProps.getScrollDownButtonProperties().setToolTipText("Scroll Down");
  }

  private static void setWindowBarProperties() {
    {
      WindowBarProperties p = DEFAULT_VALUES.getWindowBarProperties();

      p.setMinimumWidth(4);
      p.setContentPanelEdgeResizeEdgeDistance(6);
      p.setContinuousLayoutEnabled(true);
      p.setDragIndicatorColor(Color.DARK_GRAY);

      p.getTabWindowProperties().getTabbedPanelProperties()
          .setTabDeselectable(true)
          .setAutoSelectTab(false)

          .getTabAreaComponentsProperties()
          .setStretchEnabled(true)

          .getComponentProperties()
          .setBorder(new TabAreaLineBorder());

      p.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties().getComponentProperties()
          .setInsets(new Insets(4, 4, 4, 4));

      p.getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties().getShapedPanelProperties()
          .setOpaque(true);

      p.getTabWindowProperties().getTabbedPanelProperties().getTabAreaProperties().setTabAreaVisiblePolicy(
          TabAreaVisiblePolicy.ALWAYS);
    }

    {
      WindowTabProperties p = DEFAULT_VALUES.getWindowBarProperties().getTabWindowProperties().getTabProperties();

      p.getTitledTabProperties()
          .setSizePolicy(TitledTabSizePolicy.EQUAL_SIZE)
//          .addSuperObject(HighlightedTabSetup.createTabProperties())
          .setHighlightedRaised(0);

/*      p.getFocusedProperties()
          .setBackgroundColor(Color.YELLOW);

  */
      p.getTitledTabProperties().getNormalProperties()
          .getComponentProperties().setInsets(new Insets(1, 4, 1, 4));

      p.getNormalButtonProperties().getCloseButtonProperties().setVisible(true);
      p.getNormalButtonProperties().getRestoreButtonProperties().setVisible(true);
      p.getNormalButtonProperties().getUndockButtonProperties().setVisible(true);
      p.getNormalButtonProperties().getDockButtonProperties().setVisible(true);
    }
  }

  private static void setFloatingWindowProperties() {
    for (int i = 0; i < ComponentProperties.PROPERTIES.getPropertyCount(); i++) {
      Property property = ComponentProperties.PROPERTIES.getProperty(i);

      FloatingWindowProperties.COMPONENT_PROPERTIES.get(DEFAULT_VALUES.getFloatingWindowProperties().getMap()).
          createRelativeRef(property,
                            RootWindowProperties.WINDOW_AREA_PROPERTIES.get(DEFAULT_VALUES.getMap()),
                            property);
    }
    for (int i = 0; i < ShapedPanelProperties.PROPERTIES.getPropertyCount(); i++) {
      Property property = ShapedPanelProperties.PROPERTIES.getProperty(i);

      FloatingWindowProperties.SHAPED_PANEL_PROPERTIES.get(DEFAULT_VALUES.getFloatingWindowProperties().getMap()).
          createRelativeRef(property,
                            RootWindowProperties.WINDOW_AREA_SHAPED_PANEL_PROPERTIES.get(DEFAULT_VALUES.getMap()),
                            property);
    }

    DEFAULT_VALUES.getFloatingWindowProperties().setAutoCloseEnabled(true);
    DEFAULT_VALUES.getFloatingWindowProperties().setUseFrame(false);
  }

  private static void setViewTitleBarProperties() {
    ViewTitleBarProperties props = DEFAULT_VALUES.getViewProperties().getViewTitleBarProperties();

    props.setOrientation(Direction.UP).setDirection(Direction.RIGHT).getNormalProperties().setTitleVisible(true)
        .setIconVisible(true);

    ViewTitleBarStateProperties stateProps = props.getNormalProperties();

    stateProps.setButtonSpacing(0);

    stateProps.getUndockButtonProperties()
        .setFactory(DefaultButtonFactories.getUndockButtonFactory())
        .setVisible(true)
        .setTo(UndockWithAbortWindowAction.INSTANCE);

    stateProps.getDockButtonProperties()
        .setFactory(DefaultButtonFactories.getDockButtonFactory())
        .setVisible(true)
        .setTo(DockWithAbortWindowAction.INSTANCE);

    stateProps.getCloseButtonProperties()
        .setFactory(DefaultButtonFactories.getCloseButtonFactory())
        .setVisible(true)
        .setTo(CloseWithAbortWindowAction.INSTANCE);

    stateProps.getRestoreButtonProperties()
        .setFactory(DefaultButtonFactories.getRestoreButtonFactory())
        .setVisible(true)
        .setTo(RestoreViewWithAbortTitleBarAction.INSTANCE);

    stateProps.getMinimizeButtonProperties()
        .setFactory(DefaultButtonFactories.getMinimizeButtonFactory())
        .setVisible(true)
        .setTo(MinimizeWithAbortWindowAction.INSTANCE);

    stateProps.getMaximizeButtonProperties()
        .setFactory(DefaultButtonFactories.getMaximizeButtonFactory())
        .setVisible(true)
        .setTo(MaximizeWithAbortWindowAction.INSTANCE);

    stateProps.getMap().createRelativeRef(ViewTitleBarStateProperties.TITLE,
                                          DEFAULT_VALUES.getViewProperties().getMap(),
                                          ViewProperties.TITLE);

    stateProps.getMap().createRelativeRef(ViewTitleBarStateProperties.ICON,
                                          DEFAULT_VALUES.getViewProperties().getMap(),
                                          ViewProperties.ICON);
  }

  private static void updateVisualProperties() {
    DEFAULT_VALUES.getWindowBarProperties().getTabWindowProperties().getTabProperties().getTitledTabProperties()
        .getNormalProperties().getComponentProperties().setBackgroundColor(
        TabbedUIDefaults.getHighlightedStateBackground());

    Color shadowColor = UIManagerUtil.getColor("controlDkShadow", Color.BLACK);

    DEFAULT_VALUES.getWindowAreaProperties()
        .setBorder(new LineBorder(shadowColor))
        .setBackgroundColor(UIManagerUtil.getColor("Desktop.background", "control"));

    DEFAULT_VALUES.getWindowAreaShapedPanelProperties().setOpaque(true);

    DEFAULT_VALUES.getDragLabelProperties()
        .setBorder(new LineBorder(shadowColor))
        .setFont(UIManagerUtil.getFont("ToolTip.font"))
        .setForegroundColor(UIManagerUtil.getColor("ToolTip.foreground", "controlText"))
        .setBackgroundColor(UIManagerUtil.getColor("ToolTip.background", "control"));

    DEFAULT_VALUES
        .setRecursiveTabsEnabled(true);
  }

  private static void updateFont() {
    Font font = TitledTabProperties.getDefaultProperties().getHighlightedProperties().
        getComponentProperties().getFont();

    if (font != null)
      font = font.deriveFont(Font.BOLD);

    DEFAULT_VALUES.getTabWindowProperties().getTabProperties().getTitledTabProperties().
        getHighlightedProperties().getComponentProperties().setFont(font);
  }

  private static void updateViewTitleBarProperties() {
    ViewTitleBarProperties props = DEFAULT_VALUES.getViewProperties().getViewTitleBarProperties();
    Font font = TabbedUIDefaults.getFont();
    if (font != null)
      font = font.deriveFont(Font.BOLD);


    props.getNormalProperties().getComponentProperties().setFont(font).setForegroundColor(
        UIManager.getColor("InternalFrame.inactiveTitleForeground"))
        .setBackgroundColor(UIManager.getColor("InternalFrame.inactiveTitleBackground"))
        .setInsets(new Insets(2, 2, 2, 2));
    props.getFocusedProperties().getComponentProperties().setForegroundColor(
        UIManager.getColor("InternalFrame.activeTitleForeground"))
        .setBackgroundColor(UIManager.getColor("InternalFrame.activeTitleBackground"));

    Color c1 = UIManager.getColor("InternalFrame.inactiveTitleBackground");
    Color c2 = UIManager.getColor("InternalFrame.inactiveTitleGradient");
    ComponentPainter backgroundPainter;

    if (c1 == null)
      backgroundPainter = null;
    else if (c1.equals(c2) || c2 == null)
      backgroundPainter = new SolidColorComponentPainter(new FixedColorProvider(c1));
    else
      backgroundPainter = new GradientComponentPainter(c2, c2, c1, c1);
    props.getNormalProperties().getShapedPanelProperties().setComponentPainter(backgroundPainter).setOpaque(true);

    Color focused1 = UIManager.getColor("InternalFrame.activeTitleBackground");
    Color focused2 = UIManager.getColor("InternalFrame.activeTitleGradient");
    ComponentPainter focusedPainter;

    if (focused1 == null)
      focusedPainter = null;
    else if (focused1.equals(focused2) || focused2 == null)
      focusedPainter = new SolidColorComponentPainter(new FixedColorProvider(focused1));
    else
      focusedPainter = new GradientComponentPainter(focused2, focused2, focused1, focused1);
    props.getFocusedProperties().getShapedPanelProperties().setComponentPainter(focusedPainter);
    props.getFocusedProperties().getComponentProperties().setForegroundColor(
        UIManager.getColor("InternalFrame.activeTitleForeground"));

    props.setContentTitleBarGap(0).getNormalProperties().setIconTextGap(TabbedUIDefaults.getIconTextGap());
  }

  static {
    DEFAULT_VALUES = new RootWindowProperties(PROPERTIES.getDefaultMap());

    DEFAULT_VALUES
        .setAbortDragKey(TabbedPanelProperties.getDefaultProperties().getAbortDragKey())
        .setEdgeSplitDistance(6)
        .setDragRectangleBorderWidth(5);

    DEFAULT_VALUES.getShapedPanelProperties().setOpaque(true);

    DEFAULT_VALUES.getDockingWindowProperties()
        .setMaximizeEnabled(true)
        .setMinimizeEnabled(true)
        .setCloseEnabled(true)
        .setRestoreEnabled(true)
        .setDragEnabled(true)
        .setUndockEnabled(true)
        .setUndockOnDropEnabled(true)
        .setDockEnabled(true);

    DEFAULT_VALUES.getDockingWindowProperties().getDropFilterProperties()
        .setChildDropFilter(AcceptAllDropFilter.INSTANCE)
        .setInsertTabDropFilter(AcceptAllDropFilter.INSTANCE)
        .setInteriorDropFilter(AcceptAllDropFilter.INSTANCE)
        .setSplitDropFilter(AcceptAllDropFilter.INSTANCE);

    DEFAULT_VALUES.getWindowAreaProperties()
        .setInsets(new Insets(6, 6, 2, 2));

    DEFAULT_VALUES.getDragLabelProperties()
        .setInsets(new Insets(4, 6, 4, 6));

    DEFAULT_VALUES.getDragRectangleShapedPanelProperties().setOpaque(false);

    DEFAULT_VALUES.getSplitWindowProperties()
        .setContinuousLayoutEnabled(true)
        .setDividerSize(4)
        .setDividerLocationDragEnabled(true)
        .setDragIndicatorColor(Color.DARK_GRAY);

    DEFAULT_VALUES.getViewProperties().setAlwaysShowTitle(true);

    setTabbedPanelProperties();
    setTabProperties();
    setWindowBarProperties();
    setViewTitleBarProperties();
    setFloatingWindowProperties();

    updateVisualProperties();

    updateViewTitleBarProperties();

    updateFont();

    TitledTabProperties.getDefaultProperties().getHighlightedProperties().getComponentProperties().getMap().
        addListener(new PropertyMapListener() {
          public void propertyValuesChanged(PropertyMap propertyObject, Map changes) {
            updateFont();
          }
        });

    DynamicUIManager.getInstance().addListener(new DynamicUIManagerListener() {
      public void lookAndFeelChanged() {
        PropertyMapManager.runBatch(new Runnable() {
          public void run() {
            updateVisualProperties();
            updateViewTitleBarProperties();
          }
        });
      }

      public void propertiesChanged() {
        PropertyMapManager.runBatch(new Runnable() {
          public void run() {
            updateVisualProperties();
            updateViewTitleBarProperties();
          }
        });
      }

      public void propertiesChanging() {
      }

      public void lookAndFeelChanging() {
      }
    });
  }

  /**
   * Creates a property object that inherits default property values.
   *
   * @return a new property object that inherits default property values
   */
  public static RootWindowProperties createDefault() {
    return new RootWindowProperties(DEFAULT_VALUES);
  }

  /**
   * Creates an empty property object.
   */
  public RootWindowProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Creates a property object containing the map.
   *
   * @param map the property map
   */
  public RootWindowProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Creates a property object which inherits property values from another object.
   *
   * @param inheritFrom the object which from to inherit property values
   */
  public RootWindowProperties(RootWindowProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param properties the object from which to inherit property values
   * @return this
   */
  public RootWindowProperties addSuperObject(RootWindowProperties properties) {
    getMap().addSuperMap(properties.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   * @since IDW 1.1.0
   * @deprecated Use {@link #removeSuperObject(RootWindowProperties)} instead.
   */
  public RootWindowProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes a super object.
   *
   * @param superObject the super object to remove
   * @return this
   * @since IDW 1.3.0
   */
  public RootWindowProperties removeSuperObject(RootWindowProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Replaces a super object.
   *
   * @param oldSuperObject the super object to be replaced
   * @param newSuperObject the super object to replace it with
   * @return this
   * @since IDW 1.3.0
   */
  public RootWindowProperties replaceSuperObject(RootWindowProperties oldSuperObject,
                                                 RootWindowProperties newSuperObject) {
    getMap().replaceSuperMap(oldSuperObject.getMap(), newSuperObject.getMap());
    return this;
  }

  /**
   * Returns the default property values for tab windows.
   *
   * @return the default property values for tab windows
   */
  public TabWindowProperties getTabWindowProperties() {
    return new TabWindowProperties(TAB_WINDOW_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the default property values for split windows.
   *
   * @return the default property values for split windows
   */
  public SplitWindowProperties getSplitWindowProperties() {
    return new SplitWindowProperties(SPLIT_WINDOW_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the default property values for floating windows.
   *
   * @return the default property values for floating windows
   * @since IDW 1.4.0
   */
  public FloatingWindowProperties getFloatingWindowProperties() {
    return new FloatingWindowProperties(FLOATING_WINDOW_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the default property values for views.
   *
   * @return the default property values for views
   */
  public ViewProperties getViewProperties() {
    return new ViewProperties(VIEW_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the default property values for docking windows.
   *
   * @return the default property values for docking windows
   */
  public DockingWindowProperties getDockingWindowProperties() {
    return new DockingWindowProperties(DOCKING_WINDOW_PROPERTIES.get(getMap()));
  }

  /**
   * Sets the border width of the drag rectangle.
   *
   * @param width the border width
   * @return this
   */
  public RootWindowProperties setDragRectangleBorderWidth(int width) {
    DRAG_RECTANGLE_BORDER_WIDTH.set(getMap(), width);
    return this;
  }

  /**
   * Returns the border width of the drag rectangle.
   *
   * @return the border width of the drag rectangle
   */
  public int getDragRectangleBorderWidth() {
    return DRAG_RECTANGLE_BORDER_WIDTH.get(getMap());
  }

  /**
   * Returns true if the user is allowed to place tab windows inside other tab windows.
   *
   * @return true if tab windows are allowed to be placed in other tab windows
   */
  public boolean getRecursiveTabsEnabled() {
    return RECURSIVE_TABS_ENABLED.get(getMap());
  }

  /**
   * Returns true if double clicking on a window tab in a window bar restores the window.
   *
   * @return true if double clicking on a window tab in a window bar restores the window
   */
  public boolean getDoubleClickRestoresWindow() {
    return DOUBLE_CLICK_RESTORES_WINDOW.get(getMap());
  }

  /**
   * If set to true, double clicking on a window tab in a window bar restores the window.
   *
   * @param enabled if true, double clicking on a window tab in a window bar restores the window
   * @return this
   */
  public RootWindowProperties setDoubleClickRestoresWindow(boolean enabled) {
    DOUBLE_CLICK_RESTORES_WINDOW.set(getMap(), enabled);
    return this;
  }

  /**
   * If set to true, the user is allowed to place tab windows inside other tab windows.
   *
   * @param enabled if true, the user is allowed to place tab windows inside other tab windows
   * @return this
   */
  public RootWindowProperties setRecursiveTabsEnabled(boolean enabled) {
    RECURSIVE_TABS_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Returns the property values for the drag label.
   *
   * @return the property values for the drag label
   */
  public ComponentProperties getDragLabelProperties() {
    return new ComponentProperties(DRAG_LABEL_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the root window component.
   *
   * @return the property values for the root window component
   */
  public ComponentProperties getComponentProperties() {
    return new ComponentProperties(COMPONENT_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the property values for the root window shaped panel.
   *
   * @return the property values for the root window shaped panel
   * @since IDW 1.2.0
   */
  public ShapedPanelProperties getShapedPanelProperties() {
    return new ShapedPanelProperties(SHAPED_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the component property values for the window area component.
   *
   * @return the component property values for the window area component
   */
  public ComponentProperties getWindowAreaProperties() {
    return new ComponentProperties(WINDOW_AREA_PROPERTIES.get(getMap()));
  }

  /**
   * Returns the shaped panel property values for the window area component.
   *
   * @return the shaped panel property values for the window area component
   */
  public ShapedPanelProperties getWindowAreaShapedPanelProperties() {
    return new ShapedPanelProperties(WINDOW_AREA_SHAPED_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * Sets the distance from the window edge inside which a mouse drag will trigger a window split.
   *
   * @param size the distance from the window edge inside which a mouse drag will trigger a window split
   * @return this
   */
  public RootWindowProperties setEdgeSplitDistance(int size) {
    EDGE_SPLIT_DISTANCE.set(getMap(), size);
    return this;
  }

  /**
   * Returns the distance from the window edge inside which a mouse drag will trigger a window split.
   *
   * @return the distance from the window edge inside which a mouse drag will trigger a window split
   */
  public int getEdgeSplitDistance() {
    return EDGE_SPLIT_DISTANCE.get(getMap());
  }

  /**
   * Returns the key code for the key that aborts a drag.
   *
   * @return the key code for the key that aborts a drag
   */
  public int getAbortDragKey() {
    return ABORT_DRAG_KEY.get(getMap());
  }

  /**
   * Sets the key code for the key that aborts a drag.
   *
   * @param key the key code for the key that aborts a drag
   * @return this
   */
  public RootWindowProperties setAbortDragKey(int key) {
    ABORT_DRAG_KEY.set(getMap(), key);
    return this;
  }

  /**
   * Returns the default window bar property values.
   *
   * @return the default window bar property values
   * @since IDW 1.1.0
   */
  public WindowBarProperties getWindowBarProperties() {
    return new WindowBarProperties(WINDOW_BAR_PROPERTIES.get(getMap()));
  }

  /**
   * Shaped panel properties for the drag rectangle. Setting a painter disables the default drag rectangle.
   *
   * @return the drag rectangle shaped panel properties
   * @since IDW 1.2.0
   */
  public ShapedPanelProperties getDragRectangleShapedPanelProperties() {
    return new ShapedPanelProperties(DRAG_RECTANGLE_SHAPED_PANEL_PROPERTIES.get(getMap()));
  }

}

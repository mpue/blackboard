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


// $Id: TabbedPanelProperties.java,v 1.3 2011-09-07 19:56:08 mpue Exp $

package net.infonode.tabbedpanel;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.border.CompoundBorder;

import net.infonode.gui.DynamicUIManager;
import net.infonode.gui.DynamicUIManagerListener;
import net.infonode.gui.border.HighlightBorder;
import net.infonode.gui.hover.HoverListener;
import net.infonode.gui.icon.button.ArrowIcon;
import net.infonode.gui.icon.button.BorderIcon;
import net.infonode.gui.icon.button.DropDownIcon;
import net.infonode.properties.base.Property;
import net.infonode.properties.gui.util.ButtonProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.ColorProperty;
import net.infonode.properties.types.DirectionProperty;
import net.infonode.properties.types.FloatProperty;
import net.infonode.properties.types.HoverListenerProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.tabbedpanel.border.OpenContentBorder;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.util.ArrayUtil;
import net.infonode.util.Direction;

/**
 * TabbedPanelProperties holds all properties for a {@link TabbedPanel}. A
 * TabbedPanelProperties object contains separate property objects for the
 * content area, the tab area, the tab area components and the buttons of
 * the TabbedPanel.
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TabbedPanel
 * @see #getContentPanelProperties
 * @see #getTabAreaProperties
 * @see #getTabAreaComponentsProperties
 * @see #getButtonProperties
 */
public class TabbedPanelProperties extends PropertyMapContainer {
  /**
   * A property group for all properties in TabbedPanelProperties
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Tabbed Panel Properties",
                                                                         "Properties for the TabbedPanel class.");

  /**
   * Tab reorder property
   *
   * @see #setTabReorderEnabled
   * @see #getTabReorderEnabled
   */
  public static final BooleanProperty TAB_REORDER_ENABLED = new BooleanProperty(PROPERTIES,
                                                                                "Tab Reorder Enabled",
                                                                                "Tab reorder enabled or disabled",
                                                                                PropertyMapValueHandler.INSTANCE);

  /**
   * Abort drag key code property
   *
   * @see #setAbortDragKey
   * @see #getAbortDragKey
   */
  public static final IntegerProperty ABORT_DRAG_KEY = IntegerProperty.createPositive(PROPERTIES,
                                                                                      "Abort Drag Key Code",
                                                                                      "Key code for aborting drag",
                                                                                      3,
                                                                                      PropertyMapValueHandler.INSTANCE);

  /**
   * Tab layout property
   *
   * @see #setTabLayoutPolicy
   * @see #getTabLayoutPolicy
   */
  public static final TabLayoutPolicyProperty TAB_LAYOUT_POLICY = new TabLayoutPolicyProperty(PROPERTIES,
                                                                                              "Layout Policy",
                                                                                              "Tab layout in tab area",
                                                                                              PropertyMapValueHandler.INSTANCE);

  /**
   * Tab drop down list visible property
   *
   * @see #setTabDropDownListVisiblePolicy
   * @see #getTabDropDownListVisiblePolicy
   * @since ITP 1.1.0
   */
  public static final TabDropDownListVisiblePolicyProperty TAB_DROP_DOWN_LIST_VISIBLE_POLICY = new TabDropDownListVisiblePolicyProperty(
      PROPERTIES,
      "Tab Drop Down List Visible Policy",
      "Determins when a drop down list with tabs should be visible in the tab area",
      PropertyMapValueHandler.INSTANCE);

  /**
   * Tab select trigger
   *
   * @see #setTabSelectTrigger
   * @see #getTabSelectTrigger
   * @since ITP 1.1.0
   */
  public static final TabSelectTriggerProperty TAB_SELECT_TRIGGER = new TabSelectTriggerProperty(PROPERTIES,
                                                                                                 "Tab Select Trigger",
                                                                                                 "Determins when a tab should be selected",
                                                                                                 PropertyMapValueHandler.INSTANCE);

  /**
   * Tab scrolling offset property
   *
   * @see #setTabScrollingOffset
   * @see #getTabScrollingOffset
   */
  public static final IntegerProperty TAB_SCROLLING_OFFSET = IntegerProperty.createPositive(PROPERTIES,
                                                                                            "Scroll Offset",
                                                                                            "Number of pixels to be shown for the last scrolled tab",
                                                                                            3,
                                                                                            PropertyMapValueHandler.INSTANCE);

  /**
   * Ensure selected visible property
   *
   * @see #setEnsureSelectedTabVisible
   * @see #getEnsureSelectedTabVisible
   */
  public static final BooleanProperty ENSURE_SELECTED_VISIBLE = new BooleanProperty(PROPERTIES, "Ensure Selected Visible", "Upon select, the selected tab will be scrolled into the visible area.",
                                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * Tab area orientation property
   *
   * @see #setTabAreaOrientation
   * @see #getTabAreaOrientation
   */
  public static final DirectionProperty TAB_AREA_ORIENTATION = new DirectionProperty(PROPERTIES, "Tab Area Orientation", "Tab area's orientation relative to the content area.",
                                                                                     PropertyMapValueHandler.INSTANCE);

  /**
   * Tab spacing property
   *
   * @see #setTabSpacing
   * @see #getTabSpacing
   */
  public static final IntegerProperty TAB_SPACING = new IntegerProperty(PROPERTIES,
                                                                        "Tab Spacing",
                                                                        "Number of pixels between tabs in tab area. A negative value will result in tab overlapping.",
                                                                        Integer.MIN_VALUE,
                                                                        Integer.MAX_VALUE,
                                                                        3,
                                                                        PropertyMapValueHandler.INSTANCE);

  /**
   * Tab depth order.
   *
   * @see #setAutoSelectTab
   * @see #getAutoSelectTab
   * @since ITP 1.2.0
   */
  public static final TabDepthOrderPolicyProperty TAB_DEPTH_ORDER = new TabDepthOrderPolicyProperty(PROPERTIES,
                                                                                                    "Tab Depth Order",
                                                                                                    "Tabs will overlap when tab spacing is negative. Depth order tells if first tab should be the top most and the other tabs in descending order or if the first tab should be bottom most and the other tabs in ascending order.",
                                                                                                    PropertyMapValueHandler.INSTANCE);

  /**
   * Auto select tab property
   *
   * @see #setAutoSelectTab
   * @see #getAutoSelectTab
   */
  public static final BooleanProperty AUTO_SELECT_TAB = new BooleanProperty(PROPERTIES,
                                                                            "Auto Select Tab",
                                                                            "When enabled the first tab that i added will be selected automatically. "
                                                                            +
                                                                            "If the selected tab is removed then the tab next to the removed tab will be selected automatically.",
                                                                            PropertyMapValueHandler.INSTANCE);

  /**
   * If true the tab pressed with the mouse will be highlighted, otherwise it
   * remains unchanged.
   *
   * @see #setHighlightPressedTab
   * @see #getHighlightPressedTab
   */
  public static final BooleanProperty HIGHLIGHT_PRESSED_TAB = new BooleanProperty(PROPERTIES,
                                                                                  "Highlight Pressed Tab",
                                                                                  "If true the tab pressed with the mouse will be highlighted, otherwise it remains unchanged.",
                                                                                  PropertyMapValueHandler.INSTANCE);

  /**
   * Tab deselectable property
   *
   * @see #setTabDeselectable
   * @see #getTabDeselectable
   */
  public static final BooleanProperty TAB_DESELECTABLE = new BooleanProperty(PROPERTIES, "Tab Deselectable", "When enabled the selected tab can be deselected by clicking on it.",
                                                                             PropertyMapValueHandler.INSTANCE);

  /**
   * Content area properties
   *
   * @see #getContentPanelProperties
   */
  public static final PropertyMapProperty CONTENT_PANEL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                             "Content Panel Properties",
                                                                                             "Content panel properties.",
                                                                                             TabbedPanelContentPanelProperties.PROPERTIES);

  /**
   * Tab area properties
   *
   * @see #getTabAreaProperties
   */
  public static final PropertyMapProperty TAB_AREA_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                        "Tab Area Properties",
                                                                                        "Tab area properties.",
                                                                                        TabAreaProperties.PROPERTIES);

  /**
   * Tab area components properties
   *
   * @see #getTabAreaComponentsProperties
   * @since ITP 1.1.0
   */
  public static final PropertyMapProperty TAB_AREA_COMPONENTS_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                                   "Tab Area Components Properties",
                                                                                                   "Tab area components properties.",
                                                                                                   TabAreaComponentsProperties.PROPERTIES);

  /**
   * Button properties
   *
   * @see #getButtonProperties
   * @since ITP 1.3.0
   */
  public static final PropertyMapProperty BUTTON_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                      "Tabbed Panel Button Properties",
                                                                                      "Tabbed panel button properties.",
                                                                                      TabbedPanelButtonProperties.PROPERTIES);

  /**
   * Shadow enabled property
   *
   * @see #setShadowEnabled
   * @see #getShadowEnabled
   */
  public static final BooleanProperty SHADOW_ENABLED = new BooleanProperty(PROPERTIES,
                                                                           "Shadow Enabled",
                                                                           "Indicates that a shadow is painted for the selected tab and the content panel.\n"
                                                                           +
                                                                           "The shadow is partially painted using alpha transparency which can be slow on some systems.",
                                                                           PropertyMapValueHandler.INSTANCE);

  /**
   * Hover listener property
   *
   * @see #setHoverListener
   * @see #getHoverListener
   * @since ITP 1.3.0
   */
  public static final HoverListenerProperty HOVER_LISTENER = new HoverListenerProperty(PROPERTIES,
                                                                                       "Hover Listener",
                                                                                       "Hover Listener to be used for tracking mouse hovering over the tabbed panel.",
                                                                                       PropertyMapValueHandler.INSTANCE);

  /**
   * Tabbed panel hover policy.
   *
   * @see #setHoverPolicy
   * @see #getHoverPolicy
   * @since ITP 1.3.0
   */
  public static final TabbedPanelHoverPolicyProperty HOVER_POLICY = new TabbedPanelHoverPolicyProperty(PROPERTIES,
                                                                                                       "Hover Policy",
                                                                                                       "Policy for when the Tabbed Panel is considerd hovered by the mouse.",
                                                                                                       PropertyMapValueHandler.INSTANCE);

  /**
   * Paint a shadow for the tab area. If this property is set to false a
   * shadow is painted for the highlighted tab and the tab area components
   * panel.
   *
   * @see #setPaintTabAreaShadow(boolean)
   * @see #getPaintTabAreaShadow()
   */
  public static final BooleanProperty PAINT_TAB_AREA_SHADOW = new BooleanProperty(PROPERTIES,
                                                                                  "Paint Tab Area Shadow",
                                                                                  "Paint a shadow for the tab area. If this property is set to false a shadow is painted for " +
                                                                                  "the highlighted tab and the tab area components panel.",
                                                                                  PropertyMapValueHandler.INSTANCE);

  /**
   * Shadow size property
   *
   * @see #setShadowSize
   * @see #getShadowSize
   */
  public static final IntegerProperty SHADOW_SIZE = IntegerProperty.createPositive(PROPERTIES,
                                                                                   "Shadow Size",
                                                                                   "The size of the tab shadow.",
                                                                                   2,
                                                                                   PropertyMapValueHandler.INSTANCE);

  /**
   * Shadow blend area size property
   *
   * @see #setShadowBlendAreaSize
   * @see #getShadowBlendAreaSize
   */
  public static final IntegerProperty SHADOW_BLEND_AREA_SIZE = IntegerProperty.createPositive(PROPERTIES,
                                                                                              "Shadow Blend Size",
                                                                                              "The size of the tab shadow blend area.",
                                                                                              2,
                                                                                              PropertyMapValueHandler.INSTANCE);

  /**
   * Shadow color property
   *
   * @see #setShadowColor
   * @see #getShadowColor
   */
  public static final ColorProperty SHADOW_COLOR = new ColorProperty(PROPERTIES,
                                                                     "Shadow Color",
                                                                     "The color of the shadow.",
                                                                     PropertyMapValueHandler.INSTANCE);

  /**
   * Shadow strength property
   *
   * @see #setShadowStrength
   * @see #getShadowStrength
   */
  public static final FloatProperty SHADOW_STRENGTH = new FloatProperty(PROPERTIES,
                                                                        "Shadow Strength",
                                                                        "The strength of the shadow. 0 means the shadow color is the same as the backgound color, "
                                                                        + "1 means the shadow color is '" +
                                                                        SHADOW_COLOR +
                                                                        "'.",
                                                                        PropertyMapValueHandler.INSTANCE,
                                                                        0,
                                                                        1);

  /**
   * Array with all properties that controls the functional behavior
   */
  public static final Property[] FUNCTIONAL_PROPERTIES = {TAB_REORDER_ENABLED,
                                                          ABORT_DRAG_KEY,
                                                          TAB_LAYOUT_POLICY,
                                                          ENSURE_SELECTED_VISIBLE,
                                                          AUTO_SELECT_TAB,
                                                          TAB_DESELECTABLE,
                                                          TAB_SELECT_TRIGGER,
                                                          HOVER_POLICY};

  /**
   * Array with all properties that controls the shadow
   */
  public static final Property[] SHADOW_PROPERTIES = {SHADOW_ENABLED,
                                                      SHADOW_SIZE,
                                                      SHADOW_BLEND_AREA_SIZE,
                                                      SHADOW_COLOR,
                                                      SHADOW_STRENGTH};

  /**
   * Array with all properties that controls the visual apperance except for
   * shadow
   */
  public static final Property[] TABS_VISUAL_PROPERTIES = {TAB_SCROLLING_OFFSET,
                                                           TAB_SPACING,
                                                           TAB_AREA_ORIENTATION,
                                                           TAB_AREA_PROPERTIES,
                                                           TAB_AREA_COMPONENTS_PROPERTIES,
                                                           TAB_LAYOUT_POLICY,
                                                           CONTENT_PANEL_PROPERTIES,
                                                           TAB_DROP_DOWN_LIST_VISIBLE_POLICY};

  /**
   * Array with all properties that controls the visual apperance including
   * shadow
   */
  public static final Property[] VISUAL_PROPERTIES = (Property[]) ArrayUtil.append(TABS_VISUAL_PROPERTIES,
                                                                                   SHADOW_PROPERTIES,
                                                                                   new Property[TABS_VISUAL_PROPERTIES.length +
                                                                                                SHADOW_PROPERTIES.length]);

  private static final TabbedPanelProperties DEFAULT_PROPERTIES = new TabbedPanelProperties(PROPERTIES.getDefaultMap());

  static {
    DynamicUIManager.getInstance().addListener(new DynamicUIManagerListener() {
      public void lookAndFeelChanged() {
        updateVisualProperties();
      }

      public void propertiesChanged() {
        updateVisualProperties();
      }

      public void propertiesChanging() {
      }

      public void lookAndFeelChanging() {
      }
    });

    updateVisualProperties();
    updateFunctionalProperties();
  }

  /**
   * Creates a properties object with default properties based on the current
   * look and feel
   *
   * @return properties object
   */
  public static TabbedPanelProperties getDefaultProperties() {
    return new TabbedPanelProperties(DEFAULT_PROPERTIES);
  }

  private static void updateVisualProperties() {
    PropertyMapManager.runBatch(new Runnable() {
      public void run() {
        DEFAULT_PROPERTIES.getContentPanelProperties().getComponentProperties().setBorder(
            new OpenContentBorder(TabbedUIDefaults.getDarkShadow(), TabbedUIDefaults.getHighlight()))
            .setInsets(TabbedUIDefaults.getContentAreaInsets())
            .setBackgroundColor(TabbedUIDefaults.getContentAreaBackground());
        DEFAULT_PROPERTIES.getContentPanelProperties().getShapedPanelProperties().setOpaque(true);

        DEFAULT_PROPERTIES.getTabAreaComponentsProperties().setStretchEnabled(false).getComponentProperties()
            .setBorder(new CompoundBorder(new TabAreaLineBorder(TabbedUIDefaults.getDarkShadow()),
                                          new HighlightBorder(false, TabbedUIDefaults.getHighlight())))
            .setBackgroundColor(TabbedUIDefaults.getContentAreaBackground());
        DEFAULT_PROPERTIES.getTabAreaComponentsProperties().getShapedPanelProperties().setOpaque(true);

        DEFAULT_PROPERTIES.getTabAreaProperties().getShapedPanelProperties().setOpaque(false);
      }
    });
  }

  private static void updateFunctionalProperties() {
    DEFAULT_PROPERTIES.setTabReorderEnabled(false)
        .setAbortDragKey(KeyEvent.VK_ESCAPE)
        .setTabLayoutPolicy(TabLayoutPolicy.SCROLLING)
        .setTabDropDownListVisiblePolicy(TabDropDownListVisiblePolicy.NEVER)
        .setTabSelectTrigger(TabSelectTrigger.MOUSE_PRESS)
        .setTabScrollingOffset(10).setTabSpacing(-1)
        .setTabDepthOrderPolicy(TabDepthOrderPolicy.DESCENDING)
        .setEnsureSelectedTabVisible(false)
        .setTabAreaOrientation(Direction.UP)
        .setAutoSelectTab(true)
        .setHighlightPressedTab(true)

        .setHoverPolicy(TabbedPanelHoverPolicy.NO_HOVERED_CHILD)

        .setShadowEnabled(false)
        .setShadowSize(3)
        .setShadowBlendAreaSize(2)
        .setShadowColor(Color.BLACK)
        .setShadowStrength(0.4F);

    DEFAULT_PROPERTIES.getTabAreaProperties().setTabAreaVisiblePolicy(TabAreaVisiblePolicy.ALWAYS);

    HashMap buttonMap = new HashMap();
    buttonMap.put(Direction.DOWN, DEFAULT_PROPERTIES.getButtonProperties().getScrollDownButtonProperties());
    buttonMap.put(Direction.UP, DEFAULT_PROPERTIES.getButtonProperties().getScrollUpButtonProperties());
    buttonMap.put(Direction.RIGHT, DEFAULT_PROPERTIES.getButtonProperties().getScrollRightButtonProperties());
    buttonMap.put(Direction.LEFT, DEFAULT_PROPERTIES.getButtonProperties().getScrollLeftButtonProperties());

    int iconSize = TabbedUIDefaults.getButtonIconSize();

    Direction[] directions = Direction.getDirections();
    for (int i = 0; i < directions.length; i++) {
      ArrowIcon disabledIcon = new ArrowIcon(iconSize - 2, directions[i], false);
      disabledIcon.setShadowEnabled(false);

      ((ButtonProperties) buttonMap.get(directions[i]))
          .setFactory(TabbedPanelDefaultButtonFactories.getScrollDownButtonFactory())
          .setIcon(new ArrowIcon(iconSize, directions[i]))
          .setDisabledIcon(new BorderIcon(disabledIcon, 1));
    }

    DEFAULT_PROPERTIES.getButtonProperties().getTabDropDownListButtonProperties()
        .setFactory(TabbedPanelDefaultButtonFactories.getScrollDownButtonFactory())
        .setIcon(new DropDownIcon(Color.black, TabbedUIDefaults.getButtonIconSize(), Direction.DOWN))
        .setDisabledIcon(null);
  }

  /**
   * Constructs an empty TabbedPanelProperties object
   */
  public TabbedPanelProperties() {
    super(PROPERTIES);
  }

  /**
   * Constructs a TabbedPanelProperties map with the given map as property
   * storage
   *
   * @param map map to store properties in
   */
  public TabbedPanelProperties(PropertyMap map) {
    super(map);
  }

  /**
   * Constructs a TabbedPanelProperties object that inherits its properties
   * from the given TabbedPanelProperties object
   *
   * @param inheritFrom TabbedPanelProperties object to inherit properties from
   */
  public TabbedPanelProperties(TabbedPanelProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param superObject the object from which to inherit property values
   * @return this
   */
  public TabbedPanelProperties addSuperObject(TabbedPanelProperties superObject) {
    getMap().addSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public TabbedPanelProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes the given super object.
   *
   * @param superObject super object to remove
   * @return this
   * @since ITP 1.3.0
   */
  public TabbedPanelProperties removeSuperObject(TabbedPanelProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Replaces the given super objects.
   *
   * @param oldSuperObject old super object
   * @param newSuperObject new super object
   * @return this
   * @since ITP 1.4.0
   */
  public TabbedPanelProperties replaceSuperObject(TabbedPanelProperties oldSuperObject, TabbedPanelProperties newSuperObject) {
    getMap().replaceSuperMap(oldSuperObject.getMap(), newSuperObject.getMap());
    return this;
  }

  /**
   * <p>
   * Sets the shadow strength. 0 means the shadow color is the same as the
   * backgound color and 1 means the shadow color is the same as shadow color.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @param strength the strength between 0 and 1
   * @return this TabbedPanelProperties
   * @see #setShadowColor
   * @see #setShadowEnabled
   */
  public TabbedPanelProperties setShadowStrength(float strength) {
    SHADOW_STRENGTH.set(getMap(), strength);
    return this;
  }

  /**
   * <p>
   * Sets the shadow blend area size, i.e. number of pixels for the shadow
   * color fading.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @param size the shadow blend area size in pixels
   * @return this TabbedPanelProperties
   * @see #setShadowEnabled
   */
  public TabbedPanelProperties setShadowBlendAreaSize(int size) {
    SHADOW_BLEND_AREA_SIZE.set(getMap(), size);
    return this;
  }

  /**
   * <p>
   * Sets the shadow size, i.e. the width/height of the shadow in pixels.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @param size shadow size in pixels
   * @return this TabbedPanelProperties
   * @see #setShadowEnabled
   */
  public TabbedPanelProperties setShadowSize(int size) {
    SHADOW_SIZE.set(getMap(), size);
    return this;
  }

  /**
   * <p>
   * Sets the shadow color.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @param color the shadow color
   * @return this TabbedPanelProperties
   * @see #setShadowEnabled
   */
  public TabbedPanelProperties setShadowColor(Color color) {
    SHADOW_COLOR.set(getMap(), color);
    return this;
  }

  /**
   * Sets shadow enabled
   *
   * @param value true for enabled, otherwise false
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setShadowEnabled(boolean value) {
    SHADOW_ENABLED.set(getMap(), value);
    return this;
  }

  /**
   * Sets if automatic selection of a tab is enabled. Automatic selection
   * means that if no tab is selected and a tab is added to the TabbedPanel,
   * then the added tab will automatically be selected. If a selected tab is
   * removed from the TabbedPanel then the tab next to the selected tab will
   * automatically be selected.
   *
   * @param value true for automactic selection, otherwise false
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setAutoSelectTab(boolean value) {
    AUTO_SELECT_TAB.set(getMap(), value);
    return this;
  }

  /**
   * Sets if tab is deselectable. This means that if the selected tab is
   * clicked then the selected tab will be deselected. Clicking it again will
   * select the tab again.
   *
   * @param value true for deselectable, otherwise false
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setTabDeselectable(boolean value) {
    TAB_DESELECTABLE.set(getMap(), value);
    return this;
  }

  /**
   * <p>
   * Sets if a tab should be made visible if it is selected, i.e. if scrolling
   * is enabled, a tab will be scrolled into the visible part of the tab area
   * when it becomes selected.
   * </p>
   * <p>
   * <strong>Note: </strong> This will only have effect if scolling is
   * enabled.
   * </p>
   *
   * @param value true for selected visible, otherwise false
   * @return this TabbedPanelProperties
   * @see #setTabLayoutPolicy
   */
  public TabbedPanelProperties setEnsureSelectedTabVisible(boolean value) {
    ENSURE_SELECTED_VISIBLE.set(getMap(), value);
    return this;
  }

  /**
   * <p>
   * Sets number of pixels to be shown for the scrolled out tab next to the
   * first visible tab.
   * </p>
   * <p>
   * <strong>Note: </strong> This will only have effect if scolling is
   * enabled.
   * </p>
   *
   * @param offset number of pixels
   * @return this TabbedPanelProperties
   * @see #setTabLayoutPolicy
   */
  public TabbedPanelProperties setTabScrollingOffset(int offset) {
    TAB_SCROLLING_OFFSET.set(getMap(), offset);
    return this;
  }

  /**
   * Sets if the tabs can be reordered using the mouse
   *
   * @param enabled true for enabled, otherwise disabled
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setTabReorderEnabled(boolean enabled) {
    TAB_REORDER_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Set to true if the tab pressed with the mouse should be highlighted,
   * otherwise it's not changed.
   *
   * @param highlightEnabled true if the tab pressed with the mouse should be highlighted
   * @return this
   */
  public TabbedPanelProperties setHighlightPressedTab(boolean highlightEnabled) {
    HIGHLIGHT_PRESSED_TAB.set(getMap(), highlightEnabled);
    return this;
  }

  /**
   * <p>
   * Sets the key code for aborting a tab drag or reorder operation.
   * </p>
   * <p>
   * <strong>Note: </strong> The right mouse button can also be used to abort
   * the operation.
   * </p>
   *
   * @param keyCode key code
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setAbortDragKey(int keyCode) {
    ABORT_DRAG_KEY.set(getMap(), keyCode);
    return this;
  }

  /**
   * Sets the tab layout policy for the tab area, i.e. how the line of tabs
   * should be laid out
   *
   * @param policy the tab area layout policy
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setTabLayoutPolicy(TabLayoutPolicy policy) {
    TAB_LAYOUT_POLICY.set(getMap(), policy);
    return this;
  }

  /**
   * <p>
   * Sets the tab drop down list visible policy, i.e. when a drop down list
   * with the tabs should be visible
   * </p>
   *
   * <p>
   * The drop down list will show an icon for the tab if the tab implements
   * the {@link net.infonode.gui.icon.IconProvider} and the text will be retrieved by calling
   * toString() on the tab.
   * </p>
   *
   * @param policy the tab drop down list visible policy
   * @return this TabbedPanelProperties
   * @since ITP 1.1.0
   */
  public TabbedPanelProperties setTabDropDownListVisiblePolicy(TabDropDownListVisiblePolicy policy) {
    TAB_DROP_DOWN_LIST_VISIBLE_POLICY.set(getMap(), policy);
    return this;
  }

  /**
   * Sets the tab select trigger, i.e. what triggers a tab selection
   *
   * @param trigger the tab select trigger
   * @return this TabbedPanelProperties
   * @since ITP 1.1.0
   */
  public TabbedPanelProperties setTabSelectTrigger(TabSelectTrigger trigger) {
    TAB_SELECT_TRIGGER.set(getMap(), trigger);
    return this;
  }

  /**
   * Sets the tab area orientation, i.e. if the tab area should be placed up,
   * down, left or right of the content area.
   *
   * @param direction the orientation
   * @return this TabbedPanelProperties
   */
  public TabbedPanelProperties setTabAreaOrientation(Direction direction) {
    TAB_AREA_ORIENTATION.set(getMap(), direction);
    return this;
  }

  /**
   * <p>
   * Sets the tab spacing, i.e. number of pixels between the tabs in the tab
   * area.
   * </p>
   *
   * <p>
   * This can be a negative value i.e. tabs will be overlapping each other. The
   * depth order can be controlled with the property TAB_DEPTH_ORDER.
   * </p>
   *
   * @param value number of pixels. A negative value reuslts in tabs
   *              overlapping each other with the number of pixels.
   * @return this TabbedPanelProperties
   * @see #setTabDepthOrderPolicy
   */
  public TabbedPanelProperties setTabSpacing(int value) {
    TAB_SPACING.set(getMap(), value);
    return this;
  }

  /**
   * <p>
   * Sets the tab depth order policy to be used when tabs are overlapping i.e.
   * negative tab spacing.
   * </p>
   *
   * <p>
   * If the depth order is descending, the first tab will be the top most and
   * the last tab the bottom most. If the depth order is ascending, then the
   * first tab will be the bottom most and the last tab the top most. Note that
   * if a tab is highlighted, it will always be shown as the top most tab.
   * </p>
   *
   * @param policy the tab depth order policy
   * @return this TabbedPanelProperties
   * @see #setTabSpacing
   * @since ITP 1.2.0
   */
  public TabbedPanelProperties setTabDepthOrderPolicy(TabDepthOrderPolicy policy) {
    TAB_DEPTH_ORDER.set(getMap(), policy);
    return this;
  }

  /**
   * <p>
   * Gets the shadow strength. 0 means the shadow color is the same as the
   * backgound color and 1 means the shadow color is the same as shadow color.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @return the shadow strength between 0 and 1
   * @see #getShadowColor
   * @see #getShadowEnabled
   */
  public float getShadowStrength() {
    return SHADOW_STRENGTH.get(getMap());
  }

  /**
   * <p>
   * Gets the shadow blend area size, i.e. number of pixels for the shadow
   * color fading.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @return the shadow blend area size in pixels
   * @see #getShadowEnabled
   */
  public int getShadowBlendAreaSize() {
    return SHADOW_BLEND_AREA_SIZE.get(getMap());
  }

  /**
   * <p>
   * Gets the shadow size, i.e. the width/height of the shadow in pixels.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @return shadow size in pixels
   * @see #getShadowEnabled
   */
  public int getShadowSize() {
    return SHADOW_SIZE.get(getMap());
  }

  /**
   * <p>
   * Gets the shadow color.
   * </p>
   * <p>
   * <strong>Note: </strong> This property will only have effect if shadow is
   * enabled.
   * </p>
   *
   * @return the shadow color
   * @see #getShadowEnabled
   */
  public Color getShadowColor() {
    return SHADOW_COLOR.get(getMap());
  }

  /**
   * Gets shadow enabled
   *
   * @return true if shadow is enabled, otherwise false
   */
  public boolean getShadowEnabled() {
    return SHADOW_ENABLED.get(getMap());
  }

  /**
   * Gets if automatic selection of a tab is enabled. Automatic selection
   * means that if no tab is selected and a tab is added to the TabbedPanel,
   * then the added tab will automatically be selected. If a selected tab is
   * removed from the TabbedPanel then the tab next to the selected tab will
   * automatically be selected.
   *
   * @return true if automactic selection, otherwise false
   */
  public boolean getAutoSelectTab() {
    return AUTO_SELECT_TAB.get(getMap());
  }

  /**
   * Gets if the tab pressed with the mouse will be highlighted.
   *
   * @return true if the tab pressed with the mouse will be highlighted
   */
  public boolean getHighlightPressedTab() {
    return HIGHLIGHT_PRESSED_TAB.get(getMap());
  }

  /**
   * Gets if tab is deselectable. This means that if the selected tab is
   * clicked then the selected tab will be deselected. Clicking it again will
   * select the tab again.
   *
   * @return true if deselectable, otherwise false
   */
  public boolean getTabDeselectable() {
    return TAB_DESELECTABLE.get(getMap());
  }

  /**
   * <p>
   * Gets if a tab should be made visible if it is selected, i.e. if scrolling
   * is enabled, a tab will be scrolled into the visible part of the tab area
   * when it becomes selected.
   * </p>
   * <p>
   * <strong>Note: </strong> This will only have effect if scolling is
   * enabled.
   * </p>
   *
   * @return true if selected visible should be made visible, otherwise false
   * @see #getTabLayoutPolicy
   */
  public boolean getEnsureSelectedTabVisible() {
    return ENSURE_SELECTED_VISIBLE.get(getMap());
  }

  /**
   * Returns true if a shadow is painted for the tab area, false if a shadow
   * is painted for the highlighted tab and the tab area components panel.
   *
   * @return true if a shadow is painted for the tab area, false if a shadow
   *         is painted for the highlighted tab and the tab area components
   *         panel
   * @since ITP 1.1.0
   */
  public boolean getPaintTabAreaShadow() {
    return PAINT_TAB_AREA_SHADOW.get(getMap());
  }

  /**
   * Set to true if a shadow should be painted for the tab area, false if a
   * shadow should be painted for the highlighted tab and the tab area
   * components panel.
   *
   * @param paintShadow true if a shadow should be painted for the tab area, false if
   *                    a shadow should be painted for the highlighted tab and the tab
   *                    area components panel
   * @return this
   * @since ITP 1.1.0
   */
  public TabbedPanelProperties setPaintTabAreaShadow(boolean paintShadow) {
    PAINT_TAB_AREA_SHADOW.set(getMap(), paintShadow);
    return this;
  }

  /**
   * <p>
   * Gets number of pixels to be shown for the last scrolled tab.
   * </p>
   * <p>
   * <strong>Note: </strong> This will only have effect if scolling is
   * enabled.
   * </p>
   *
   * @return number of pixels
   * @see #getTabLayoutPolicy
   */
  public int getTabScrollingOffset() {
    return TAB_SCROLLING_OFFSET.get(getMap());
  }

  /**
   * Gets if the tabs can be reorder using the mouse.
   *
   * @return true if enabled, otherwise disabled
   */
  public boolean getTabReorderEnabled() {
    return TAB_REORDER_ENABLED.get(getMap());
  }

  /**
   * <p>
   * Gets the key code for aborting a tab drag or reorder operation.
   * </p>
   * <p>
   * Note that the right mouse button can also be used to abort the operation.
   * </p>
   *
   * @return the key code
   */
  public int getAbortDragKey() {
    return ABORT_DRAG_KEY.get(getMap());
  }

  /**
   * Gets the tab layout policy for the tab area, i.e. how the line of tabs
   * should be laid out
   *
   * @return the tab area layout policy
   */
  public TabLayoutPolicy getTabLayoutPolicy() {
    return TAB_LAYOUT_POLICY.get(getMap());
  }

  /**
   * <p>
   * Gets the tab drop down list visible policy, i.e. when a drop down list
   * with the tabs should be visible.
   * </p>
   *
   * <p>
   * The drop down list will show an icon for the tab if the tab implements
   * the {@link net.infonode.gui.icon.IconProvider} and the text will be retrieved by calling
   * toString() on the tab.
   * </p>
   *
   * @return the tab drop down list visible policy
   * @since ITP 1.1.0
   */
  public TabDropDownListVisiblePolicy getTabDropDownListVisiblePolicy() {
    return TAB_DROP_DOWN_LIST_VISIBLE_POLICY.get(getMap());
  }

  /**
   * Gets the tab select trigger, i.e. what triggers a tab selection
   *
   * @return the tab select trigger
   * @since ITP 1.1.0
   */
  public TabSelectTrigger getTabSelectTrigger() {
    return TAB_SELECT_TRIGGER.get(getMap());
  }

  /**
   * Gets the tab area orientation, i.e. if the tab area should be placed up,
   * down, left or right of the content area
   *
   * @return the orientation
   */
  public Direction getTabAreaOrientation() {
    return TAB_AREA_ORIENTATION.get(getMap());
  }

  /**
   * <p>
   * Gets the tab spacing, i.e. number of pixels between the tabs in the tab
   * area.
   * </p>
   *
   * <p>
   * This can be a negative value i.e. tabs will be overlapping each other. The
   * depth order can be controlled with the property TAB_DEPTH_ORDER.
   * </p>
   *
   * @return number of pixels, can be negative i.e. tabs will be overlapping
   * @see #getTabDepthOrderPolicy
   */
  public int getTabSpacing() {
    return TAB_SPACING.get(getMap());
  }

  /**
   * <p>
   * Gets the tab depth order policy to be used when tabs are overlapping i.e.
   * negative tab spacing.
   * </p>
   *
   * <p>
   * If the depth order is descending, the first tab will be the top most and
   * the last tab the bottom most. If the depth order is ascending, then the
   * first tab will be the bottom most and the last tab the top most. Note that
   * if a tab is highlighted, it will always be shown as the top most tab.
   * </p>
   *
   * @return the tab depth order policy
   * @see #getTabSpacing
   * @since ITP 1.2.0
   */
  public TabDepthOrderPolicy getTabDepthOrderPolicy() {
    return TAB_DEPTH_ORDER.get(getMap());
  }

  /**
   * <p>Sets the hover listener that will be triggered when the tabbed panel is hoverd by the mouse.</p>
   *
   * <p>The hovered tabbed panel will be the source of the hover event sent to the
   * hover listener.</p>
   *
   * @param listener the hover listener
   * @return this TabbedPanelProperties
   * @since ITP 1.3.0
   */
  public TabbedPanelProperties setHoverListener(HoverListener listener) {
    HOVER_LISTENER.set(getMap(), listener);
    return this;
  }

  /**
   * <p>Gets the hover listener that will be triggered when the tabbed panel is hovered by the mouse.</p>
   *
   * <p>The hovered tabbed panel will be the source of the hover event sent to the
   * hover listener.</p>
   *
   * @return the hover listener
   * @since ITP 1.3.0
   */
  public HoverListener getHoverListener() {
    return HOVER_LISTENER.get(getMap());
  }

  /**
   * <p>Sets the hover policy.</p>
   *
   * <p>The hover policy determines when the tabbed panel is considered hovered by the mouse and the
   * hover listener is called. The default hover policy is NO_HOVERED_CHILD.</p>
   *
   * @param hoverPolicy the hover policy
   * @return this TabbedPanelProperties
   * @since ITP 1.3.0
   */
  public TabbedPanelProperties setHoverPolicy(TabbedPanelHoverPolicy hoverPolicy) {
    HOVER_POLICY.set(getMap(), hoverPolicy);
    return this;
  }

  /**
   * <p>Gets the hover policy.</p>
   *
   * <p>The hover policy determines when the tabbed panel is considered hovered by the mouse and the
   * hover listener is called. The default hover policy is NO_HOVERED_CHILD.</p>
   *
   * @return the hover policy
   * @since ITP 1.3.0
   */
  public TabbedPanelHoverPolicy getHoverPolicy() {
    return HOVER_POLICY.get(getMap());
  }

  /**
   * Gets the properties getMap() with properties for the tabbed panel's
   * content area
   *
   * @return the properties for the content area
   */
  public TabbedPanelContentPanelProperties getContentPanelProperties() {
    return new TabbedPanelContentPanelProperties(CONTENT_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the properties getMap() with properties for the tabbed panel's tab
   * area
   *
   * @return the properties for the tab area
   */
  public TabAreaProperties getTabAreaProperties() {
    return new TabAreaProperties(TAB_AREA_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the properties getMap() with properties for the area in a tabbed
   * panel's tab area where the tab area components are shown.
   *
   * @return the properties for the tab area components
   * @since ITP 1.1.0
   */
  public TabAreaComponentsProperties getTabAreaComponentsProperties() {
    return new TabAreaComponentsProperties(TAB_AREA_COMPONENTS_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the properties getMap() with properties for all the buttons in a
   * tabbed panel.
   *
   * @return the properties for the buttons
   * @since ITP 1.3.0
   */
  public TabbedPanelButtonProperties getButtonProperties() {
    return new TabbedPanelButtonProperties(BUTTON_PROPERTIES.get(getMap()));
  }
}
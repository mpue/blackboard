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

// $Id: TitledTabProperties.java,v 1.3 2011-09-07 19:56:10 mpue Exp $
package net.infonode.tabbedpanel.titledtab;

import javax.swing.border.CompoundBorder;

import net.infonode.gui.DimensionProvider;
import net.infonode.gui.DynamicUIManager;
import net.infonode.gui.DynamicUIManagerListener;
import net.infonode.gui.hover.HoverListener;
import net.infonode.properties.base.Property;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapContainer;
import net.infonode.properties.propertymap.PropertyMapFactory;
import net.infonode.properties.propertymap.PropertyMapGroup;
import net.infonode.properties.propertymap.PropertyMapManager;
import net.infonode.properties.propertymap.PropertyMapProperty;
import net.infonode.properties.propertymap.PropertyMapValueHandler;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.DimensionProviderProperty;
import net.infonode.properties.types.HoverListenerProperty;
import net.infonode.properties.types.IntegerProperty;
import net.infonode.tabbedpanel.TabbedUIDefaults;
import net.infonode.tabbedpanel.border.TabAreaLineBorder;
import net.infonode.tabbedpanel.border.TabHighlightBorder;
import net.infonode.util.Alignment;
import net.infonode.util.Direction;

/**
 * <p>
 * TitledTabProperties holds all properties for a {@link TitledTab}.
 * </p>
 *
 * <p>
 * A titled tab can have three states, normal, highlighted and disabled. Each state is represented by a {@link
 * TitledTabStateProperties} object containing all properties that can be set for a state.
 * </p>
 *
 * <p>
 * By default the property values in the highlighted and disabled state are references to corresponding values in the
 * normal state. This means that if you set a property value in the normal state, then highlighted and the disabled
 * state will use that property value if the property has not been set in the highlighted or disabled state.
 * </p>
 *
 * <p>
 * Example:<br> Setting the background color in the normal state means that normal, highlighted and disabled state will
 * use that color as background color. If you set background color for highlighted state, then the highlighted state
 * will use that color regardless of the background color for the normal state.
 * </p>
 *
 * <p>
 * By default the tool tip text in all states is the same as the tab text in the normal state. For example, if you
 * change the tab text in the highlighted state and want the tooltip to display the same text, you must set the "Tool
 * Tip Text" property {@link TitledTabStateProperties#TOOL_TIP_TEXT} in the highlighted state.
 * </p>
 *
 * @author $Author: mpue $
 * @version $Revision: 1.3 $
 * @see TitledTab
 * @see TitledTabStateProperties
 */
public class TitledTabProperties extends PropertyMapContainer {
  /**
   * A property group for all properties in TitledTabProperties
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Titled Tab Properties",
                                                                         "Properties for the TitledTab class.");

  /**
   * Focusabled property
   *
   * @see #setFocusable
   * @see #getFocusable
   */
  public static final BooleanProperty FOCUSABLE = new BooleanProperty(PROPERTIES, "Focusable", "Tab focusable",
                                                                      PropertyMapValueHandler.INSTANCE);

  /**
   * Focus Marker Enabled property
   *
   * @see #setFocusMarkerEnabled
   * @see #getFocusMarkerEnabled
   * @since ITP 1.4.0
   */
  public static final BooleanProperty FOCUS_MARKER_ENABLED = new BooleanProperty(PROPERTIES, "Focus Marker Enabled", "Enables or disables the focus marker when the tab has focus.",
                                                                                 PropertyMapValueHandler.INSTANCE);

  /**
   * Normal state properties
   *
   * @see #getNormalProperties
   */
  public static final PropertyMapProperty NORMAL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                      "Normal Properties",
                                                                                      "Normal tab properties.",
                                                                                      TitledTabStateProperties.PROPERTIES);

  /**
   * Highlighted state properties
   *
   * @see #getHighlightedProperties
   */
  public static final PropertyMapProperty HIGHLIGHTED_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                           "Highlighted Properties",
                                                                                           "Highlighted tab properties.",
                                                                                           TitledTabStateProperties.PROPERTIES);

  /**
   * Disabled state properties
   *
   * @see #getDisabledProperties
   */
  public static final PropertyMapProperty DISABLED_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                        "Disabled Properties",
                                                                                        "Disabled tab properties.",
                                                                                        TitledTabStateProperties.PROPERTIES);

  /**
   * Size policy property
   *
   * @see #setSizePolicy
   * @see #getSizePolicy
   */
  public static final TitledTabSizePolicyProperty SIZE_POLICY = new TitledTabSizePolicyProperty(PROPERTIES,
                                                                                                "Size Policy",
                                                                                                "Tab size policy",
                                                                                                PropertyMapValueHandler.INSTANCE);

  /**
   * Border size policy property
   *
   * @see #setBorderSizePolicy
   * @see #getBorderSizePolicy
   */
  public static final TitledTabBorderSizePolicyProperty BORDER_SIZE_POLICY = new TitledTabBorderSizePolicyProperty(
      PROPERTIES,
      "Border Size Policy",
      "Border size policy.",
      PropertyMapValueHandler.INSTANCE);

  /**
   * Tab minimum size property
   *
   * @see #setMinimumSizeProvider(DimensionProvider)
   * @see #getMinimumSizeProvider()
   */
  public static final DimensionProviderProperty MINIMUM_SIZE_PROVIDER = new DimensionProviderProperty(PROPERTIES,
                                                                                                      "Minimum Size",
                                                                                                      "Tab minimum size.",
                                                                                                      PropertyMapValueHandler.INSTANCE);

  /**
   * Highlighted raised amount property
   *
   * @see #setHighlightedRaised
   * @see #getHighlightedRaised
   */
  public static final IntegerProperty HIGHLIGHTED_RAISED_AMOUNT = IntegerProperty.createPositive(PROPERTIES,
                                                                                                 "Highlighted Raised",
                                                                                                 "Number of raised pixels for highlighted tab.",
                                                                                                 2,
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
                                                                                       "Hover Listener to be used for tracking mouse hovering over the tab.",
                                                                                       PropertyMapValueHandler.INSTANCE);
  /**
   * TitledTab enabled property
   *
   * @see #setEnabled
   * @see #getEnabled
   * @since ITP 1.5.0
   */
  public static final BooleanProperty ENABLED = new BooleanProperty(PROPERTIES,
                                                                       "Enabled",
                                                                       "TitledTab enabled or disabled",
                                                                        PropertyMapValueHandler.INSTANCE);

  private static final TitledTabProperties DEFAULT_VALUES = new TitledTabProperties(PROPERTIES.getDefaultMap());

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

    DEFAULT_VALUES.getNormalProperties().getMap().createRelativeRef(TitledTabStateProperties.TOOL_TIP_TEXT,
                                                                    DEFAULT_VALUES.getNormalProperties().getMap(),
                                                                    TitledTabStateProperties.TEXT);

    DEFAULT_VALUES.getHighlightedProperties().getMap().addSuperMap(DEFAULT_VALUES.getNormalProperties().getMap());
    DEFAULT_VALUES.getDisabledProperties().getMap().addSuperMap(DEFAULT_VALUES.getNormalProperties().getMap());

    {
      Property[] refProperties = TitledTabStateProperties.PROPERTIES.getProperties();

      for (int i = 0; i < refProperties.length; i++) {
        DEFAULT_VALUES.getHighlightedProperties().getMap().createRelativeRef(refProperties[i],
                                                                             DEFAULT_VALUES.getNormalProperties()
                                                                             .getMap(),
                                                                             refProperties[i]);
        DEFAULT_VALUES.getDisabledProperties().getMap().createRelativeRef(refProperties[i],
                                                                          DEFAULT_VALUES.getNormalProperties().getMap(),
                                                                          refProperties[i]);
      }
    }

    {
      Property[] refProperties = ComponentProperties.PROPERTIES.getProperties();

      for (int i = 0; i < refProperties.length; i++) {
        DEFAULT_VALUES.getHighlightedProperties().getComponentProperties().
            getMap().createRelativeRef(refProperties[i],
                                       DEFAULT_VALUES.getNormalProperties().getComponentProperties().getMap(),
                                       refProperties[i]);
        DEFAULT_VALUES.getDisabledProperties().getComponentProperties().
            getMap().createRelativeRef(refProperties[i],
                                       DEFAULT_VALUES.getNormalProperties().getComponentProperties().getMap(),
                                       refProperties[i]);
      }
    }

    {
      Property[] refProperties = ShapedPanelProperties.PROPERTIES.getProperties();

      for (int i = 0; i < refProperties.length; i++) {
        DEFAULT_VALUES.getHighlightedProperties().getShapedPanelProperties().
            getMap().createRelativeRef(refProperties[i],
                                       DEFAULT_VALUES.getNormalProperties().getShapedPanelProperties().getMap(),
                                       refProperties[i]);
        DEFAULT_VALUES.getDisabledProperties().getShapedPanelProperties().
            getMap().createRelativeRef(refProperties[i],
                                       DEFAULT_VALUES.getNormalProperties().getShapedPanelProperties().getMap(),
                                       refProperties[i]);
      }
    }

    updateVisualProperties();
    updateFunctionalProperties();
  }

  /**
   * Constructs an empty TitledTabProperties object
   */
  public TitledTabProperties() {
    super(PropertyMapFactory.create(PROPERTIES));
  }

  /**
   * Constructs a TitledTabProperties object with the give object as property storage
   *
   * @param object object to store properties in
   */
  public TitledTabProperties(PropertyMap object) {
    super(object);
  }

  /**
   * Constructs a TitledTabProperties object that inherits its properties from the given TitledTabProperties object
   *
   * @param inheritFrom TitledTabProperties object to inherit properties from
   */
  public TitledTabProperties(TitledTabProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param superObject the object from which to inherit property values
   * @return this
   */
  public TitledTabProperties addSuperObject(TitledTabProperties superObject) {
    getMap().addSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public TitledTabProperties removeSuperObject() {
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
  public TitledTabProperties removeSuperObject(TitledTabProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Replaces the given super objects.
   *
   * @param oldSuperObject super object to replace
   * @param newSuperObject new super object
   * @return this
   * @since ITP 1.4.0
   */
  public TitledTabProperties replaceSuperObject(TitledTabProperties oldSuperObject, TitledTabProperties newSuperObject) {
    getMap().replaceSuperMap(oldSuperObject.getMap(), newSuperObject.getMap());
    return this;
  }

  /**
   * Creates a properties object with default properties based on the current look and feel
   *
   * @return properties object
   */
  public static TitledTabProperties getDefaultProperties() {
    return new TitledTabProperties(DEFAULT_VALUES);
  }

  /**
   * Gets the properties for the normal state
   *
   * @return the normal state properties
   */
  public TitledTabStateProperties getNormalProperties() {
    return new TitledTabStateProperties(NORMAL_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the properties for the highlighted state
   *
   * @return the highlighted state properties
   */
  public TitledTabStateProperties getHighlightedProperties() {
    return new TitledTabStateProperties(HIGHLIGHTED_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the properties for the disabled state
   *
   * @return the disabled state properties
   */
  public TitledTabStateProperties getDisabledProperties() {
    return new TitledTabStateProperties(DISABLED_PROPERTIES.get(getMap()));
  }

  /**
   * Sets if this TitledTab should be focusable
   *
   * @param value true for focusable, otherwise false
   * @return this TitledTabProperties
   */
  public TitledTabProperties setFocusable(boolean value) {
    FOCUSABLE.set(getMap(), value);

    return this;
  }

  /**
   * Gets if this TitledTab is focusable
   *
   * @return true for focusable, otherwise false
   */
  public boolean getFocusable() {
    return FOCUSABLE.get(getMap());
  }

  /**
   * <p>
   * Sets if this TitledTab should show its built-in focus marker when this tab has focus.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> Disabling the focus marker is useful when for example creating a
   * theme that draws its own focus marker.
   * </p>
   *
   * @param value true for enabled, otherwise false
   * @return this TitledTabProperties
   * @since ITP 1.4.0
   */
  public TitledTabProperties setFocusMarkerEnabled(boolean value) {
    FOCUS_MARKER_ENABLED.set(getMap(), value);

    return this;
  }

  /**
   * <p>
   * Gets if this TitledTab should show its built-in focus marker when this tab has focus.
   * </p>
   *
   * <p>
   * <strong>Note:</strong> Disabling the focus marker is useful when for example creating a
   * theme that draws its own focus marker.
   * </p>
   *
   * @return true for enabled, otherwise false
   * @since ITP 1.4.0
   */
  public boolean getFocusMarkerEnabled() {
    return FOCUS_MARKER_ENABLED.get(getMap());
  }

  /**
   * Sets the size policy for this TitledTab
   *
   * @param sizePolicy the size policy
   * @return this TitledTabProperties
   */
  public TitledTabProperties setSizePolicy(TitledTabSizePolicy sizePolicy) {
    SIZE_POLICY.set(getMap(), sizePolicy);

    return this;
  }

  /**
   * Gets the size policy for this TitledTab
   *
   * @return the size policy
   */
  public TitledTabSizePolicy getSizePolicy() {
    return SIZE_POLICY.get(getMap());
  }

  /**
   * Sets the border size policy for this TitledTab
   *
   * @param sizePolicy the border size policy
   * @return this TitledTabProperties
   */
  public TitledTabProperties setBorderSizePolicy(TitledTabBorderSizePolicy sizePolicy) {
    BORDER_SIZE_POLICY.set(getMap(), sizePolicy);

    return this;
  }

  /**
   * Gets the border size policy for this TitledTab
   *
   * @return the border size policy
   */
  public TitledTabBorderSizePolicy getBorderSizePolicy() {
    return BORDER_SIZE_POLICY.get(getMap());
  }

  /**
   * Sets the tab's minimum size dimension provider
   *
   * @param size the minimum size dimension provider or null if tab's default minimum size should be used instead
   * @return this TitledTabProperties
   */
  public TitledTabProperties setMinimumSizeProvider(DimensionProvider size) {
    MINIMUM_SIZE_PROVIDER.set(getMap(), size);

    return this;
  }

  /**
   * Gets the dimension provider for the tab's minimum size
   *
   * @return the minimum size provider or null if default tab minimum size is to be used instead
   */
  public DimensionProvider getMinimumSizeProvider() {
    return MINIMUM_SIZE_PROVIDER.get(getMap());
  }

  /**
   * Sets how many pixels higher this TitledTab will be when it is in its highlighted state compared to its normal and
   * disabled state
   *
   * @param amount number of pixels
   * @return this TitledTabProperties
   */
  public TitledTabProperties setHighlightedRaised(int amount) {
    HIGHLIGHTED_RAISED_AMOUNT.set(getMap(), amount);

    return this;
  }

  /**
   * Gets how many pixels higher this TitledTab will be when it is in its highlighted state compared to its normal and
   * disabled state
   *
   * @return number of pixels
   */
  public int getHighlightedRaised() {
    return HIGHLIGHTED_RAISED_AMOUNT.get(getMap());
  }

  /**
   * <p>
   * Sets if this TitledTab should be enabled or not.
   * </p>
   * 
   * <p>
   * <strong>Note:</strong> Calling {@link TitledTab#setEnabled(boolean)} will modify this property for the tab.
   * </p>
   *
   * @param value true for enabled, otherwise false
   * @return this TitledTabProperties
   * @since ITP 1.5.0
   */
  public TitledTabProperties setEnabled(boolean value) {
    ENABLED.set(getMap(), value);

    return this;
  }

  /**
   * Gets if this TitledTab is enabled or disabled
   *
   * @return true for enabled, otherwise false
   * @since ITP 1.5.0
   */
  public boolean getEnabled() {
    return ENABLED.get(getMap());
  }

  /**
   * <p>Sets the hover listener that will be triggered when the tab is hovered by the mouse.</p>
   *
   * <p>The hovered titled tab will be the source of the hover event sent to the hover listener.</p>
   *
   * @param listener the hover listener
   * @return this TitledTabProperties
   * @since ITP 1.3.0
   */
  public TitledTabProperties setHoverListener(HoverListener listener) {
    HOVER_LISTENER.set(getMap(), listener);
    return this;
  }

  /**
   * <p>Gets the hover listener that will be triggered when the tab is hovered by the mouse.</p>
   *
   * <p>The hovered titled tab will be the source of the hover event sent to the hover listener.</p>
   *
   * @return the hover listener
   * @since ITP 1.3.0
   */
  public HoverListener getHoverListener() {
    return HOVER_LISTENER.get(getMap());
  }

  private static void updateVisualProperties() {
    PropertyMapManager.runBatch(new Runnable() {
      public void run() {
        int gap = TabbedUIDefaults.getIconTextGap();

        DEFAULT_VALUES.getNormalProperties().getShapedPanelProperties().setOpaque(true);

        DEFAULT_VALUES.getNormalProperties().setIconTextGap(gap).setTextTitleComponentGap(gap)
            .setIconVisible(true).setTextVisible(true).setTitleComponentVisible(true).getComponentProperties()
            .setFont(TabbedUIDefaults.getFont())
            .setForegroundColor(TabbedUIDefaults.getNormalStateForeground())
            .setBackgroundColor(TabbedUIDefaults.getNormalStateBackground())
            .setBorder(new TabAreaLineBorder())
            .setInsets(TabbedUIDefaults.getTabInsets());

        DEFAULT_VALUES.getHighlightedProperties().getComponentProperties()
            .setBackgroundColor(TabbedUIDefaults.getHighlightedStateBackground())
            .setBorder(new CompoundBorder(new TabAreaLineBorder(),
                                          new TabHighlightBorder(TabbedUIDefaults.getHighlight(), true)));

        DEFAULT_VALUES.getDisabledProperties().getComponentProperties()
            .setForegroundColor(TabbedUIDefaults.getDisabledForeground()).setBackgroundColor(
                TabbedUIDefaults.getDisabledBackground());
      }
    });
  }

  private static void updateFunctionalProperties() {
    DEFAULT_VALUES.setEnabled(true).setFocusable(true).setFocusMarkerEnabled(true).setSizePolicy(TitledTabSizePolicy.EQUAL_SIZE)
        .setBorderSizePolicy(TitledTabBorderSizePolicy.EQUAL_SIZE).setHighlightedRaised(2);

    DEFAULT_VALUES.getNormalProperties().setHorizontalAlignment(Alignment.LEFT).setVerticalAlignment(Alignment.CENTER)
        .setIconTextRelativeAlignment(Alignment.LEFT).setTitleComponentTextRelativeAlignment(Alignment.RIGHT)
        .setDirection(Direction.RIGHT);
  }
}

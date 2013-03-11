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


// $Id: ButtonFactory.java,v 1.3 2011-09-07 19:56:09 mpue Exp $
package net.infonode.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;

import net.infonode.gui.border.HighlightBorder;
import net.infonode.util.ColorUtil;

public class ButtonFactory {
  private ButtonFactory() {
  }

  private static class ButtonHighlighter implements ComponentListener, HierarchyListener {
/*    private static final Color HIGHLIGHTED_COLOR = new Color(140, 160, 255);
    private static final Color PRESSED_COLOR = new Color(60, 80, 200);
*/
    private JButton button;
    private Border pressedBorder;
    private Border highlightedBorder;
    private Border normalBorder;
    private boolean rollover;
    private long rolloverStart; // Ugly hack to avoid false rollover callbacks which occur when the button is moved 

    ButtonHighlighter(JButton button, int padding) {
      this.button = button;

      normalBorder = new EmptyBorder(padding + 2, padding + 2, padding + 2, padding + 2);
      pressedBorder = new EmptyBorder(padding + 2, padding + 2, padding, padding);
      highlightedBorder = new EmptyBorder(padding + 1, padding + 1, padding + 1, padding + 1);

      button.setContentAreaFilled(false);
      setNormalState();

      button.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          rollover = (System.currentTimeMillis() - rolloverStart) > 20 &&
                     ButtonHighlighter.this.button.getModel().isRollover();
          update();

          if (ButtonHighlighter.this.button.getModel().isRollover())
            rolloverStart = 0;
        }
      });

      button.addHierarchyListener(this);
      button.addComponentListener(this);
    }

    private void setNormalState() {
      button.setBackground(null);
      button.setOpaque(false);
      button.setBorder(normalBorder);
      rollover = false;
    }

    public void componentHidden(ComponentEvent e) {
      setNormalState();
      rolloverStart = System.currentTimeMillis();
    }

    public void componentMoved(ComponentEvent e) {
      setNormalState();
      rolloverStart = System.currentTimeMillis();
    }

    public void componentResized(ComponentEvent e) {
      setNormalState();
      rolloverStart = System.currentTimeMillis();
    }

    public void componentShown(ComponentEvent e) {
      setNormalState();
      rolloverStart = System.currentTimeMillis();
    }

    public void hierarchyChanged(HierarchyEvent e) {
      setNormalState();
      rolloverStart = System.currentTimeMillis();
    }

    private void update() {
      boolean pressed = button.getModel().isArmed();

      if (button.isEnabled() && (rollover || pressed)) {
        button.setOpaque(true);
        Color backgroundColor = ComponentUtil.getBackgroundColor(button.getParent());
        backgroundColor = backgroundColor == null ?
                          UIManagerUtil.getColor("control", Color.LIGHT_GRAY) : backgroundColor;
        button.setBackground(ColorUtil.mult(backgroundColor, pressed ? 0.8 : 1.15));

        button.setBorder(pressed ?
                         new CompoundBorder(new LineBorder(ColorUtil.mult(backgroundColor, 0.3)),
                                            pressedBorder) :
                         new CompoundBorder(new LineBorder(ColorUtil.mult(backgroundColor, 0.5)),
                                            highlightedBorder));
      }
      else {
        setNormalState();
      }
    }

  }

  private static final Border normalBorder = new CompoundBorder(new LineBorder(new Color(70, 70, 70)),
                                                                new CompoundBorder(new HighlightBorder(),
                                                                                   new EmptyBorder(1, 6, 1, 6)));
  private static final Border pressedBorder = new CompoundBorder(new LineBorder(new Color(70, 70, 70)),
                                                                 new CompoundBorder(new HighlightBorder(true),
                                                                                    new EmptyBorder(2, 7, 0, 5)));

  private static JButton initButton(final JButton button) {
    button.setMargin(null);
    button.setBorder(normalBorder);
    button.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        button.setBorder(pressedBorder);
      }

      public void mouseReleased(MouseEvent e) {
        button.setBorder(normalBorder);
      }
    });

    return button;
  }

  private static JButton newButton(String text) {
    return initButton(new JButton(text));
  }

  private static JButton newButton(Icon icon) {
    return initButton(new JButton(icon));
  }

  private static JButton newButton(Icon icon, String text) {
    return initButton(new JButton(text, icon));
  }

  public static final JButton createDialogButton(String text, ActionListener action) {
    JButton b = new JButton(text);
    b.setFont(b.getFont().deriveFont(Font.BOLD));
    b.addActionListener(action);
    return b;
  }

  public static final JButton createButton(String text, ActionListener action) {
    return createButton(text, true, action);
  }

  public static final JButton createButton(String text, boolean opaque, ActionListener action) {
    JButton b = newButton(text);
    b.setOpaque(opaque);
    b.addActionListener(action);
    return b;
  }

  public static final JButton createButton(String iconResource, String text, ActionListener action) {
    URL iconURL = ButtonFactory.class.getClassLoader().getResource(iconResource);
    return createButton(iconURL == null ? null : new ImageIcon(iconURL), text, action);
  }

  public static final JButton createButton(Icon icon, String text, ActionListener action) {
    JButton b;

    if (icon != null) {
      b = newButton(icon);
      b.setToolTipText(text);
    }
    else {
      b = newButton(text);
    }

    b.addActionListener(action);
    return b;
  }

  public static final JButton createButton(Icon icon, String tooltipText, boolean opaque, ActionListener action) {
    JButton b = newButton(icon);
    b.setToolTipText(tooltipText);
    b.addActionListener(action);
    b.setOpaque(opaque);
    return b;
  }

  public static final JButton createFlatHighlightButton(Icon icon, String tooltipText, int padding,
                                                        ActionListener action) {
    final JButton b = new JButton(icon) {
      public void setUI(ButtonUI ui) {
        super.setUI(new FlatIconButtonUI());
      }
    };
    b.setVerticalAlignment(SwingConstants.CENTER);
    b.setToolTipText(tooltipText);
    b.setMargin(new Insets(0, 0, 0, 0));
    new ButtonHighlighter(b, padding);

    b.setRolloverEnabled(true);

    if (action != null)
      b.addActionListener(action);

    return b;
  }

  public static final void applyButtonHighlighter(JButton b, int padding) {
    b.setVerticalAlignment(SwingConstants.CENTER);
    b.setMargin(new Insets(0, 0, 0, 0));
    new ButtonHighlighter(b, padding);

    b.setRolloverEnabled(true);
  }

  public static final JButton createFlatHighlightButton(Icon icon, String tooltipText, int padding,
                                                        boolean focusable, ActionListener action) {
    final JButton b = createFlatHighlightButton(icon, tooltipText, padding, action);
    b.setFocusable(focusable);
    return b;
  }

  public static final JButton createHighlightButton(String text, ActionListener action) {
    JButton b = newButton(text);
    b.addActionListener(action);
    return b;
  }

  public static final JButton createHighlightButton(Icon icon, ActionListener action) {
    JButton b = newButton(icon);
    b.addActionListener(action);
    return b;
  }

  public static final JButton createHighlightButton(Icon icon, String text, ActionListener action) {
    JButton b = newButton(icon, text);
    b.addActionListener(action);
    return b;
  }

  public static final JButton createFlatIconHoverButton(Icon icon, Icon hovered, Icon pressed) {
    final JButton b = new JButton(icon) {
      public void setUI(ButtonUI ui) {
        super.setUI(new FlatIconButtonUI());
      }
    };
    b.setPressedIcon(pressed);
    b.setRolloverEnabled(true);
    b.setRolloverIcon(hovered);
    b.setVerticalAlignment(SwingConstants.CENTER);
    return b;
  }
}

package org.pmedv.blackboard.connections;

//
//  (C) 1997 Sergey Solyanik.
//
//  This program is not in public domain.
//
//  All rights to distribute are hereby granted under GNU Public License.
//
//  THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF
//  ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
//  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//  PARTICULAR PURPOSE.
//
import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;

public class SweepFrontEnd
extends Applet
implements FeedBack
    {
    private static final int PSZ = 2;

    private RandomSegmentList   rsl;
    private RandomPointList     rpl;

    private int xCurrent;
    private int yCurrent;
    private int xLast;
    private int yLast;

    private Button recalc;
    private Button clear;

    public void report (String s)
        {
        showStatus (s);
        }

    public void init()
        {
        super.init();

        Debug.f = this;

        rsl = null;
        xCurrent = yCurrent = xLast = yLast = 0;

        recalc = new Button ("Do!");
        clear  = new Button ("Clear");

        this.add (recalc);
        this.add (clear);
        }

    public void paint (Graphics g)
        {
        g.setColor (Color.pink);
        g.fillRect (0, 0, 200, 200);

        g.setColor (Color.black);
        
        RandomSegmentList rs = rsl;
        
        while (rs != null)
            {
            g.drawLine((int)rs.x1, (int)rs.y1, (int)rs.x2, (int)rs.y2);
            rs = rs.next;
            }

        g.setColor (Color.red);

        RandomPointList rp = rpl;
        
        while (rp != null)
            {
            int x = (int)rp.x;
            int y = (int)rp.y;

            g.drawLine(x - PSZ, y, x + PSZ, y);
            g.drawLine(x, y - PSZ, x, y + PSZ);

            rp = rp.next;
            }
        }

    @SuppressWarnings("deprecation")
	public boolean action (java.awt.Event event, Object arg)
        {
        if (event.target == clear )
            {
            rsl = null;
            rpl = null;
            repaint ();
            return true;
            }
        else if (event.target == recalc)
            {
            rpl = Sweep.process(rsl);
            repaint ();
            return true;
            }
        else
            return super.action (event, arg);
        }

    public boolean mouseDown(java.awt.Event e, int x, int y)
        {
        xLast = x;
        yLast = y;
        xCurrent = xLast;
        yCurrent = yLast;

        return true;
        }


    public boolean mouseDrag (java.awt.Event e, int x, int y)
        {
        Graphics g = getGraphics ();
        
        g.setXORMode (Color.white);
        
        if ((xCurrent != xLast) || (yCurrent != yLast))
            g.drawLine (xCurrent, yCurrent, xLast, yLast);

        xCurrent = x;
        yCurrent = y;

        g.drawLine (xCurrent, yCurrent, xLast, yLast);

		showStatus ("Coord: " + x + ", " + y);

        return true;
        }

    public boolean mouseUp (java.awt.Event e, int x, int y)
        {
        if ((xCurrent != xLast) || (yCurrent != yLast))
            {
            Graphics g = getGraphics ();

            g.setXORMode (Color.white);
            g.drawLine (xCurrent, yCurrent, xLast, yLast);

            g.setColor (Color.black);
            g.drawLine (x, y, xLast, yLast);

            rsl = new RandomSegmentList ((double)x, (double)y,
                                (double)xLast, (double)yLast, rsl);

            xCurrent = xLast;
            yCurrent = yLast;
            }

        return true;
        }

    }

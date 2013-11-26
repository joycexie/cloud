/* NODEVIEW */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class NodeView extends Canvas implements Observer {
	public NodeView(Node nd, AntPanel ap) {
		if (nd == null) throw new NullPointerException("nd is null");
		if (ap == null) throw new NullPointerException("ap is null");
		node      = nd;
		antPanel  = ap;
		minSize   = new Dimension(2*RAD, 2*RAD);
		nodeColor = Color.black;
		x         = antPanel.scaleX(node.getX()); // x coord of centre
		y         = antPanel.scaleY(node.getY()); // y coord of centre

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				// Node selected for movement
				antPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				nodeColor = Color.red;
				startX    = me.getX();
				startY    = me.getY();
				repaint();
			}
			public void mouseReleased(MouseEvent me) {
				antPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				nodeColor = Color.black;
				int endX  = me.getX();
				int endY  = me.getY();
				if (endX != startX || endY != startY) {
					x += endX - startX;
					y += endY - startY;
					x = (x >= RAD) ? x : RAD;
					y = (y >= RAD) ? y : RAD;
					x = (x <= (antPanel.wid-RAD)) ? x : (antPanel.wid-RAD);
					y = (y <= (antPanel.hgt-RAD)) ? y : (antPanel.hgt-RAD);
					node.move(antPanel.invScaleX(x), antPanel.invScaleY(y));
				}            
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {
				int endX  = me.getX();
				int endY  = me.getY();
				if (endX != startX || endY != startY) {
					x += endX - startX;
					y += endY - startY;
					x = (x >= RAD) ? x : RAD;
					y = (y >= RAD) ? y : RAD;
					x = (x <= (antPanel.wid-RAD)) ? x : (antPanel.wid-RAD);
					y = (y <= (antPanel.hgt-RAD)) ? y : (antPanel.hgt-RAD);
					node.move(antPanel.invScaleX(x), antPanel.invScaleY(y));
				}
			}
		});

		node.addObserver(this);
	}

	public void paint(Graphics g) {
		Color gc = g.getColor();
		g.setColor(nodeColor);
		g.fillOval(0, 0, 2*RAD, 2*RAD);
		int    id  = node.getId();
		String ids = String.valueOf(id);
		int    ca  = g.getFontMetrics().getAscent();
		int    sw  = g.getFontMetrics().stringWidth(ids);
		g.setColor(Color.white);
		g.drawString(ids, RAD-sw/2, RAD+ca/2-1);
		g.setColor(gc);
	}

	public final Dimension getPreferredSize() {
		return(new Dimension(minSize));
	}

	public final Dimension getMinimumSize() {
		return(new Dimension(minSize));
	}

	public final int getX() {
		return(x-RAD);
	}

	public final int getY() {
		return(y-RAD);
	}

	public void update(Observable o, Object arg) {
		setLocation(new Point(getX(), getY()));
	}

	public static final int RAD = 8;

	private final Node      node;
	private final AntPanel  antPanel;
	private final Dimension minSize;
	private Color           nodeColor;
	private int             x;
	private int             y;
	private int             startX;
	private int             startY;

	private static final long serialVersionUID = 1L;
}

/* BESTTOURLABEL */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.awt.*;
import java.util.*;

class BestTourLabel extends Label implements Observer {
	public BestTourLabel(Network net) {
		super();
		this.net = net;
		setText(label());
		net.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		setText(label());
	}

	private String label() {
		Node[]       bt  = net.getBestTour();
		StringBuffer buf = new StringBuffer();
		if (bt == null)
			buf.append("null");
		else {
			buf.append("[").append(bt[0].getId());
			for (int i=1; i<bt.length; i++)
				buf.append(",").append(bt[i].getId());
			buf.append("]");
		}
		return(buf.toString());
	}

	private Network net;
	private static final long serialVersionUID = 1L;
}

/* CYCLELABEL */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.awt.*;
import java.util.*;

class CycleLabel extends Label implements Observer {
	public CycleLabel(Network net) {
		super(String.valueOf(net.getNumCycles()));
		this.net = net;
		net.addObserver(this);
	}

	public void update(Observable o, Object arg) {
		setText(String.valueOf(net.getNumCycles()));
	}

	private Network net;
	private static final long serialVersionUID = 1L;
}

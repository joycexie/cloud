/* NODE */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.util.*;

class Node extends Observable {
	public Node(Network net, double x, double y) {
		if (net == null) throw new NullPointerException("net is null");
		Contract.require("x >= 0.0", x >= 0.0);
		Contract.require("y >= 0.0", y >= 0.0);
		this.net = net;
		id       = net.getNextNodeId();
		this.x   = x;
		this.y   = y;
		ants     = new Vector<Ant>();
	}

	public final Network getNet() {
		return(net);
	}

	public final int getId() {
		return(id);
	}

	public synchronized final double getX() {
		return(x);
	}

	public synchronized final double getY() {
		return(y);
	}

	public void move(double x, double y) {
		Contract.require("x >= 0.0", x >= 0.0);
		Contract.require("y >= 0.0", y >= 0.0);
		synchronized(this) {
			this.x = x;	   
			this.y = y;
		}
		net.resetBestEverTour();
		setChanged();
		notifyObservers(null);
	}

	public synchronized void addAnt(Ant a) {
		if (a == null) throw new NullPointerException("a is null");
		Contract.require("a in Node's Network", a.getNet() == getNet());
		Contract.require("Node doesn't contain a", !contains(a));
		Contract.require("correct Node for not-allowed a",
				a.allowed() || this == a.getFirstNode());
		ants.addElement(a);
		if (a.allowed())
			a.addToTabu(this);
	}

	public synchronized Ant removeAnt() {
		Contract.require("numAnts > 0", numAnts() > 0);
		Ant a = ants.firstElement();
		ants.removeElement(a);
		return(a);
	}

	public synchronized final boolean contains(Ant a) {
		if (a == null) throw new NullPointerException("a is null");
		return((ants.contains(a)) ? true : false);
	}

	public synchronized final int numAnts() {
		return(ants.size());
	}

	public synchronized void deposit() {
		Enumeration<Ant> e = ants.elements();
		while (e.hasMoreElements()) {
			Ant a = e.nextElement();
			Contract.require("tours complete", !a.allowed());
			a.deposit();
		}
	}

	public synchronized Ant bestAnt() {
		// best Ant (in terms of their tours)
		Contract.require("numAnts > 0", numAnts() > 0);
		Ant              ba = ants.firstElement();
		Enumeration<Ant> e  = ants.elements();
		while (e.hasMoreElements()) {
			Ant a = e.nextElement();
			Contract.require("tours complete", !a.allowed());
			if (a.getTourLength() < ba.getTourLength())
				ba = a;
		}
		return(ba);
	}

	public synchronized void resetTabus() {
		Enumeration<Ant> e = ants.elements();
		while (e.hasMoreElements()) {
			Ant a = (Ant)e.nextElement();
			Contract.require("tours complete", !a.allowed());
			Contract.require("Ants at first Nodes", 
					a.getFirstNode().contains(a));      
			a.resetTabu();
			a.addToTabu(this);
		}
	}

	public synchronized String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Node(").append(id);
		buf.append(",").append(x);
		buf.append(",").append(y);
		if (numAnts() > 0) {
			buf.append(",ants:");
			Enumeration<Ant> e = ants.elements();
			buf.append(e.nextElement());
			while (e.hasMoreElements())
				buf.append(",").append(e.nextElement());
		}
		buf.append(")");
		return(buf.toString());
	}

	private final Network net;
	private final int     id;
	private double        x;
	private double        y;
	private Vector<Ant>   ants;
}

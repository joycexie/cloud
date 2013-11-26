/* ANT */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.util.*;

class Ant {
	public Ant(Network net, Random rnd) {
		if (net == null) throw new NullPointerException("net is null");
		if (rnd == null) throw new NullPointerException("rnd is null");
		this.net = net;
		id       = net.getNextAntId();
		tabu     = new Vector<Node>();
		tourLen  = 0.0;
		this.rnd = rnd;
	}

	public final Network getNet() {
		return(net);
	}

	public final double getId() {
		return(id);
	}

	public synchronized boolean allowed(Node nd) {
		// Is this Ant allowed to move to Node nd?
		if (nd == null) throw new NullPointerException("nd is null");
		Contract.require("nd in Ant's Network", nd.getNet() == getNet());
		return((tabu.contains(nd)) ? false : true);
	}

	public synchronized boolean allowed() {
		// Is this Ant allowed to move?
		return((tabu.size() == net.numNodes()) ? false : true);
	}

	public synchronized Node getFirstNode() {
		Contract.check("tabu.size > 0", tabu.size() > 0);
		return((Node)tabu.firstElement());
	}

	public synchronized Node getLastNode() {
		Contract.check("tabu.size > 0", tabu.size() > 0);
		return((Node)tabu.lastElement());
	}

	public synchronized void addToTabu(Node nd) {
		if (nd == null) throw new NullPointerException("nd is null");
		Contract.require("Ant at nd", nd.contains(this)); 
		Contract.require("tour incomplete", tabu.size() < net.numNodes());
		Contract.require("nd not in tabu", !tabu.contains(nd));
		int size   = tabu.size();
		int currId = nd.getId();
		if (size > 0) {
			// add Link length
			int lastId  = getLastNode().getId();
			tourLen    += net.getLink(lastId, currId).length();
		}
		if (size == net.numNodes()-1) {
			// this addition will complete tour; add length of final Link
			int firstId = getFirstNode().getId();
			tourLen    += net.getLink(currId, firstId).length();        
		}
		tabu.addElement(nd);
	}

	public synchronized void resetTabu() {
		Contract.require("tour complete", tabu.size() == net.numNodes());
		Contract.require("Ant at first Node",
				getFirstNode().contains(this));      
		tabu.removeAllElements();
		tourLen = 0.0;
	}

	public synchronized Node chooseNode() {
		Contract.require("Ant allowed to move", allowed());
		int nn      = net.numNodes();
		int nodeIdS = getLastNode().getId();
		int nodeIdT;

		// Establish probability weights to each new Node
		double sumPij = 0.0;
		double pij[]  = new double[nn];
		for (nodeIdT=0; nodeIdT < nn; nodeIdT++) {
			pij[nodeIdT] = (allowed(net.getNode(nodeIdT))) ?
					net.getLink(nodeIdS, nodeIdT).weight() : 0.0;
			sumPij += pij[nodeIdT];
		}

		// Normalise
		for (nodeIdT=0; nodeIdT < nn; nodeIdT++)
			pij[nodeIdT] /= sumPij;

		// Select destination
		double rn    = rnd.nextDouble();
		sumPij       = 0.0;
		int chosenId = 0;
		for (nodeIdT=0; nodeIdT < nn; nodeIdT++) {
			sumPij += pij[nodeIdT];
			if (allowed(net.getNode(nodeIdT)))
				chosenId = nodeIdT; // insurance against rounding error!
			if (rn <= sumPij)
				break;
		}
		return(net.getNode(chosenId));
	}

	public synchronized Node[] getTour() {
		Contract.require("tour complete", tabu.size() == net.numNodes());
		Node[] na = new Node[net.numNodes()];
		tabu.copyInto(na);
		return(na);
	}

	public synchronized double getTourLength() {
		Contract.require("tour complete", tabu.size() == net.numNodes());
		return(tourLen);
	}

	public synchronized void deposit() {
		Contract.require("tour complete", tabu.size() == net.numNodes());
		Contract.require("Ant at first Node", getFirstNode().contains(this));      
		int               nodeIdS = getLastNode().getId();
		Enumeration<Node> e       = tabu.elements();
		while (e.hasMoreElements()) {
			int  nodeIdT = e.nextElement().getId();
			Link lnk     = net.getLink(nodeIdS, nodeIdT);
			lnk.deposit(QCONST/getTourLength());
			nodeIdS      = nodeIdT;
		}
	}

	public synchronized String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Ant(").append(id);
		if (tabu.size() > 0) {
			buf.append(",[");
			Enumeration<Node> e = tabu.elements();
			buf.append(e.nextElement().getId());
			while (e.hasMoreElements())
				buf.append(",").append(e.nextElement().getId());
			buf.append("]");
			if (tabu.size() == net.numNodes())
				buf.append(",").append(getTourLength());
		}
		buf.append(")");
		return(buf.toString());
	}

	private final double  QCONST = 100.0;

	private final Network net;
	private final int     id;
	private Vector<Node>  tabu;
	private double        tourLen;
	private final Random  rnd;
}

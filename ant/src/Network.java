/* NETWORK */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.util.*;

class Network extends Observable implements Runnable {
	public Network() {
		nodes = new Vector<Node>();
	}

	public synchronized void addNode(Node nd) {
		if (nd == null) throw new NullPointerException("nd is null");
		Contract.require("nd in this Network", nd.getNet() == this);
		Contract.require("nd's id valid", nd.getId() == numNodes());
		Contract.require("not complete", !isComplete());
		nodes.addElement(nd);
		if (numNodes() == 1) {
			adj            = new Vector<Vector<Link>>();
			Vector<Link> v = new Vector<Link>();
			v.addElement(null);
			adj.addElement(v);
		} else {
			int nn = numNodes();
			for (int i=0; i<nn-1; i++)
				adj.elementAt(i).addElement(new Link(getNode(i), nd));
			Vector<Link> v = new Vector<Link>();
			for (int j=0; j<nn-1; j++)
				v.addElement(adj.elementAt(j).lastElement());
			v.addElement(null);
			adj.addElement(v);
		}
	}

	public synchronized void completed() {
		Contract.require("not complete", !isComplete());
		Contract.require("numNodes >= 2", numNodes() >= 2);
		complete = true;

		// Calculate minX, maxX, minY & maxY
		int nn = numNodes();
		maxX = minX = getNode(0).getX();
		maxY = minY = getNode(0).getY();
		for (int nodeId=1; nodeId<nn; nodeId++) {
			double x = getNode(nodeId).getX();
			double y = getNode(nodeId).getY();
			if (x < minX)
				minX = x;
			else if (x > maxX)
				maxX = x;
			if (y < minY)
				minY = y;
			else if (y > maxY)
				maxY = y;
		}
	}

	public synchronized final boolean isComplete() {
		return(complete);
	}

	public synchronized int getNextNodeId() {
		Contract.ensure("nextNodeId correct", nextNodeId == numNodes());
		return(nextNodeId++);
	}

	public synchronized int getNextAntId() {
		Contract.ensure("nextAntId correct", nextAntId == numAnts());
		return(nextAntId++);
	}

	public synchronized int numNodes() {
		return(nodes.size());
	}

	public synchronized Node getNode(int id) {
		Contract.require("id valid", id >= 0 && id < numNodes());
		return((Node)nodes.elementAt(id));
	}

	public synchronized Link getLink(int nodeIdS, int nodeIdT) {
		Contract.require("nodeIdS valid",
				nodeIdS >= 0 && nodeIdS < numNodes());
		Contract.require("nodeIdT valid",
				nodeIdT >= 0 && nodeIdT < numNodes());
		Contract.require("nodeIdS != nodeIdT", nodeIdS != nodeIdT);
		return(adj.elementAt(nodeIdS).elementAt(nodeIdT));
	}

	public synchronized final double getMinX() {
		Contract.require("is complete", isComplete());
		return(minX);
	}

	public synchronized final double getMaxX() {
		Contract.require("is complete", isComplete());
		return(maxX);
	}

	public synchronized final double getMinY() {
		Contract.require("is complete", isComplete());
		return(minY);
	}

	public synchronized final double getMaxY() {
		Contract.require("is complete", isComplete());
		return(maxY);
	}

	public synchronized final int getNumCycles() {
		return(numCycles);
	}

	public synchronized final Node[] getBestTour() {
		Node[] bt = null;
		if (bestTour != null) {
			bt = new Node[bestTour.length];
			System.arraycopy(bestTour, 0, bt, 0, bestTour.length);
		}
		return(bt);
	}

	public synchronized final double getBestTourLen() {
		return(bestTourLen);
	}

	public synchronized final Node[] getBestEverTour() {
		Node[] bet = null;
		if (bestEverTour != null) {
			bet = new Node[bestEverTour.length];
			System.arraycopy(bestEverTour, 0, bet, 0, bestEverTour.length);
		}
		return(bet);
	}

	public synchronized final double getBestEverTourLen() {
		return(bestEverTourLen);
	}

	private synchronized int numAnts() {
		int na = 0;
		Enumeration e = nodes.elements();
		while (e.hasMoreElements())
			na += ((Node)e.nextElement()).numAnts();
		return(na);
	}

	public synchronized void start() {
		if (netThread == null) {
			timeToDie  = false;
			timeToWait = false;
			netThread  = new Thread(this);
			netThread.start();
		}
	}

	public synchronized void suspend() {
		timeToWait = true;
	}

	public synchronized void resume() {
		timeToWait = false;
		notify();
	}

	public synchronized void stop() {
		timeToDie = true;
		notify();
	}

	public void run() {
		placeAnts();

		while (!timeToDie) {
			try {
				synchronized(this) {
					while (timeToWait && !timeToDie)
						wait();
				}
			} catch(InterruptedException e) {
				// do nothing!
			}
			if (timeToDie)
				continue;
			buildTours();
			deposit();
			update();

			synchronized(this) {
				// Best tour
				Ant ba      = bestAnt();
				bestTour    = ba.getTour();
				bestTourLen = ba.getTourLength();

				// Best ever tour
				if ((bestEverTour == null) ||
						(bestTourLen < bestEverTourLen)) {
					bestEverTour    = bestTour;
					bestEverTourLen = bestTourLen;
				}
			}
			resetTabus();
			setChanged();
			notifyObservers(null);
			if (timeToDie)
				continue;
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				// do nothing!
			}
			synchronized(this) {
				numCycles++;
			}
		}
	}

	public synchronized void reset() {
		numCycles = 0;
		int nn = numNodes();
		for (int i=0; i<nn; i++) {
			for (int j=i+1; j<nn; j++) {
				Link lnk = adj.elementAt(i).elementAt(j);
				lnk.resetTau();
			}
		}
		bestEverTour    = null;
		bestEverTourLen = 0.0;
		setChanged();
		notifyObservers(null);
	}

	public synchronized void resetBestEverTour() {
		bestEverTour    = null;
		bestEverTourLen = 0.0;
		setChanged();
		notifyObservers(null);
	}

	private synchronized void placeAnts() {
		// place one Ant on each Node
		Contract.require("no Ants yet", numAnts() == 0);
		int    nn  = numNodes();
		Random rnd = new Random(1);
		for (int nodeId=0; nodeId<nn; nodeId++)
			getNode(nodeId).addAnt(new Ant(this, rnd));
	}

	private synchronized void buildTours() {
		Contract.require("some Ants", numAnts() > 0);
		int nn = numNodes();
		for (int step=1; step <= nn; step++) {  // extra step to get Ants
			// back to their first Nodes
			for (int nodeIdS=0; nodeIdS < nn; nodeIdS++) {
				Node nodeS = getNode(nodeIdS);
				// move one Ant at a time
				int na = nodeS.numAnts();
				for (int i=0; i<na; i++) {
					Ant a = nodeS.removeAnt();
					if (!a.allowed()) {
						// Ant a isn't allowed to move
						a.getFirstNode().addAnt(a);
					} else {
						// Move Ant a to chosen destination
						a.chooseNode().addAnt(a);
					}
				}
			}
		}
	}

	private synchronized void deposit() {
		Contract.require("some Ants", numAnts() > 0);
		// require Ants to have completed their tours
		int nn = numNodes();
		for (int nodeId=0; nodeId < nn; nodeId++) {
			Node nd = getNode(nodeId);
			nd.deposit();
		}
	}

	private synchronized void update() {
		Contract.require("some Ants", numAnts() > 0);
		// require Ants to have completed their tours
		// require deposit to have been run
		int nn = numNodes();
		for (int i=0; i<nn; i++) {
			for (int j=i+1; j<nn; j++) {
				Link lnk = adj.elementAt(i).elementAt(j);
				lnk.update();
			}
		}
	}

	private synchronized Ant bestAnt() {
		// best Ant (in terms of their tours)
		Contract.require("some Ants", numAnts() > 0);
		// require Ants to have completed their tours
		int nn = numNodes();
		Ant ba = null;
		for (int nodeId=0; nodeId < nn; nodeId++) {
			Node nd = getNode(nodeId);
			if (nd.numAnts() > 0) {
				Ant a = nd.bestAnt();
				if (ba == null || a.getTourLength() < ba.getTourLength())
					ba = a;
			}
		}
		return(ba);
	}

	private synchronized void resetTabus() {
		Contract.require("some Ants", numAnts() > 0);
		// require Ants to have completed their tours
		// require deposit to have been run
		// require update to have been run
		int nn = numNodes();
		for (int nodeId=0; nodeId < nn; nodeId++)
			getNode(nodeId).resetTabus();
	}

	public synchronized double dist(int nodeIdS, int nodeIdT) {
		Contract.require("nodeIdS valid",
				nodeIdS >= 0 && nodeIdS < numNodes());
		Contract.require("nodeIdT valid",
				nodeIdT >= 0 && nodeIdT < numNodes());
		Contract.require("nodeIdS != nodeIdT", nodeIdS != nodeIdT);
		Node nodeS = getNode(nodeIdS);
		Node nodeT = getNode(nodeIdT);
		double x1  = nodeS.getX();
		double y1  = nodeS.getY();
		double x2  = nodeT.getX();
		double y2  = nodeT.getY();
		double len = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		Contract.ensure("len >= 0.0", len >= 0.0);
		return(len);
	}

	public synchronized String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Network(");
		if (numNodes() > 0) {
			buf.append("nodes:");
			Enumeration e = nodes.elements();
			buf.append(e.nextElement());
			while (e.hasMoreElements())
				buf.append(",\n").append(e.nextElement());
		}
		if (numNodes() > 1) {
			buf.append("\nadj:");
			int nn = numNodes();
			buf.append(adj.elementAt(0).elementAt(1));
			for (int j=2; j<nn; j++) {
				buf.append(",");
				buf.append(adj.elementAt(0).elementAt(j));
			}
			for (int i=1; i<nn-1; i++) {
				buf.append("\n");
				buf.append(adj.elementAt(i).elementAt(i+1));
				for (int j=i+2; j<nn; j++) {
					buf.append(",");
					buf.append(adj.elementAt(i).elementAt(j));
				}
			}
		}
		buf.append(")");
		return(buf.toString());
	}

	private int                  nextNodeId;
	private int                  nextAntId;
	private Vector<Node>         nodes;
	private Vector<Vector<Link>> adj;             // adjacency matrix
	private boolean              complete;
	private int                  numCycles;
	private double               minX;
	private double               maxX;
	private double               minY;
	private double               maxY;
	private Node[]               bestTour;
	private double               bestTourLen;
	private Node[]               bestEverTour;
	private double               bestEverTourLen;
	private Thread               netThread;
	private volatile boolean     timeToDie;
	private volatile boolean     timeToWait;
}

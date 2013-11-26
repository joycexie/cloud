/* LINK */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

class Link {
	public Link(Node nodeS, Node nodeT) {
		if (nodeS == null) throw new NullPointerException("nodeS is null");
		if (nodeT == null) throw new NullPointerException("nodeT is null");
		Contract.require("same Network", nodeS.getNet() == nodeT.getNet());
		Contract.require("nodeIdS < nodeIdT", nodeS.getId() < nodeT.getId());
		this.nodeS  = nodeS;
		this.nodeT  = nodeT;
		tau         = INITTAU;
		deltaTau    = 0.0;
	}

	public final Node getNodeS() {
		return(nodeS);
	}

	public final Node getNodeT() {
		return(nodeT);
	}

	public double length() {
		double x1  = nodeS.getX();
		double y1  = nodeS.getY();
		double x2  = nodeT.getX();
		double y2  = nodeT.getY();
		double len = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		Contract.ensure("len >= 0.0", len >= 0.0);
		return(len);
	}

	public synchronized final double getTau() {
		Contract.ensure("tau >= 0.0", tau >= 0.0);
		return(tau);
	}

	public synchronized final void resetTau() {
		tau = INITTAU;
	}

	public void update() {
		tau      = RHO * getTau() + deltaTau;
		deltaTau = 0.0;
	}

	public void deposit(double d) {
		Contract.require("d > 0.0", d > 0.0);
		deltaTau += d;
	}

	public double weight() {
		double len = length();
		len = (len >= MINLEN) ? len : MINLEN;
		return(Math.pow(tau, ALPHA) * Math.pow((1.0/len), BETA));
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Link(").append(nodeS.getId());
		buf.append(",").append(nodeT.getId());
		buf.append(";").append(length());
		buf.append(",").append(getTau());
		buf.append(",").append(deltaTau);
		buf.append(")");
		return(buf.toString());
	}

	private final double ALPHA   = 1.0;
	private final double BETA    = 2.0;
	private final double INITTAU = 0.1; // is this a good value?
	private final double RHO     = 0.5;
	private final double MINLEN  = 0.01;

	private final Node nodeS;
	private final Node nodeT;
	private double     tau;
	private double     deltaTau;
}

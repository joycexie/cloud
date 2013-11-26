/* ANTTSP */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class AntTSP extends Applet {
    public void init() {
        // build Network
        net = new Network();
        
	    net.addNode(new Node(net,   0, 158));
	    net.addNode(new Node(net,  54, 108));
	    net.addNode(new Node(net, 108,  88));
	    net.addNode(new Node(net, 144, 116));
	    net.addNode(new Node(net, 288, 168));
	    net.addNode(new Node(net, 270, 108));
	    net.addNode(new Node(net, 282,   0));
	    net.addNode(new Node(net, 204,  62));
	    net.addNode(new Node(net, 170,  20));
	    net.addNode(new Node(net,   0,   0));

        net.completed();

	    // build interface
	    showStatus("Press 'Run' to start ACO");

	    setLayout(new BorderLayout());
	    antPanel = new AntPanel(net);
	    add(antPanel, BorderLayout.CENTER);

	    bottomPanel = new Panel();
	    bottomPanel.setLayout(new BorderLayout());

	    labelPanel = new Panel();
	    labelPanel.setLayout(new GridLayout(0, 2));

	    labelPanel.add(new Label("# Cycles: ", Label.RIGHT));
	    cycleLabel = new CycleLabel(net);
	    labelPanel.add(cycleLabel);

	    labelPanel.add(new Label("Best tour: ", Label.RIGHT));
	    bestTourLabel = new BestTourLabel(net);
	    labelPanel.add(bestTourLabel);

	    labelPanel.add(new Label("Best tour length: ", Label.RIGHT));
	    bestTourLenLabel = new BestTourLenLabel(net);
	    labelPanel.add(bestTourLenLabel);

	    labelPanel.add(new Label("Best ever tour: ", Label.RIGHT));
	    bestEverTourLabel = new BestEverTourLabel(net);
	    labelPanel.add(bestEverTourLabel);

	    labelPanel.add(new Label("Best ever tour length: ", Label.RIGHT));
	    bestEverTourLenLabel = new BestEverTourLenLabel(net);
	    labelPanel.add(bestEverTourLenLabel);

	    bottomPanel.add(labelPanel, BorderLayout.CENTER);

        buttonPanel = new Panel();
	    buttonPanel.setLayout(new GridLayout(0, 2));

        runButton   = new Button("Run");
	    runButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
		    // Run ants
		    net.start();
		    runButton.setEnabled(false);
		    showStatus("ACO running");
	        }
	    });
        buttonPanel.add(runButton);

        resetButton = new Button("Reset");
	    resetButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
		    // Reset pheremones to initial levels, numCycles to zero,
		    // and best ever tour & length
		    net.reset();
		    showStatus("ACO reset");
	        }
	    });
        buttonPanel.add(resetButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
	    add(bottomPanel, BorderLayout.SOUTH);
    }

    public void start() {
	    net.resume();
	    showStatus("ACO running");
    }

    public void stop() {
	    net.suspend();
    }
    
    public void destroy() {
	    net.stop();
    }
    
    static double trim(double d) {
	    double f = 100.0;
        d       *= f;
	    long dl  = Math.round(d);
	    return((double) dl/f);
    }

    private Network              net;
    private AntPanel             antPanel;
    private Panel                bottomPanel;
    private Panel                labelPanel;
    private CycleLabel           cycleLabel;
    private BestTourLabel        bestTourLabel;
    private BestTourLenLabel     bestTourLenLabel;
    private BestEverTourLabel    bestEverTourLabel;
    private BestEverTourLenLabel bestEverTourLenLabel;
    private Panel                buttonPanel;
    private Button               runButton;
    private Button               resetButton;
    
	private static final long    serialVersionUID = 1L;
}

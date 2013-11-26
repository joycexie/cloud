/* CONTRACTFRAME */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.awt.*;
import java.awt.event.*;

class ContractFrame extends Frame {
	public ContractFrame(String s) {
		super("Contract Exception");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		Panel panel = new Panel();
		panel.setLayout(new GridLayout(0, 1));
		panel.add(new Label("Sorry! (programming error)", Label.CENTER));
		panel.add(new Label(s, Label.CENTER));
		add(panel, BorderLayout.CENTER);

		Button okButton = new Button("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ok
				setVisible(false);
				dispose();
			}
		});
		add(okButton, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	public static void main(String argv[]) {
		new ContractFrame("hello");
	}

	private static final long serialVersionUID = 1L;
}

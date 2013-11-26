/* APPLETFRAME */
/* modified from Core Java 2 v1 */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;

public class AppletFrame extends Frame
implements AppletStub, AppletContext {
	AppletFrame(Applet a, int x, int y) {
		setTitle(a.getClass().getName());
		setSize(x, y);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		add(a);
		a.setStub(this);
		a.init();
		setVisible(true);
		a.start();
	}

	// AppletStub methods
	public void appletResize(int width, int height) {}
	public AppletContext getAppletContext() { return this; }
	public URL getCodeBase() { return null; }
	public URL getDocumentBase() { return null; }
	public String getParameter(String name) { return ""; }
	public boolean isActive() { return true; }

	// AppletContext methods
	public Applet getApplet(String name) { return null; }
	public Enumeration<Applet> getApplets() { return null; }
	public AudioClip getAudioClip(URL url) { return null; }
	public Image getImage(URL url) { return null; }
	public InputStream getStream(String key) { return null; }
	public Iterator<String> getStreamKeys() { return null; }
	public void setStream(String key, InputStream stream) throws IOException {}
	public void showDocument(URL url) {}
	public void showDocument(URL url, String target) {}
	public void showStatus(String status) {}

	private static final long    serialVersionUID = 1L;
}

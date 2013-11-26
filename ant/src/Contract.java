/* CONTRACT */
/* Part of the AntTSP project */
/* Dr Mark C. Sinclair, NPIC, Cambodia, v1.0, November 2006, mcs@ieee.org */
/* You may make use of this code for any purpose, but if you use it, */
/* you must cite the use: Sinclair, M.C., AntTSP Java Applet v1.0, */
/* http://uk.geocities.com/markcsinclair/aco.html, November, 2006 */

class Contract {
	public static void require(String s, boolean b) {
		if (!b) {
			String msg = "require: \"" + s + "\"";
			throw new ContractException(msg);
		}
	}

	public static void ensure(String s, boolean b) {
		if (!b) {
			String msg = "ensure: \"" + s + "\"";
			throw new ContractException(msg);
		}
	}

	public static void check(String s, boolean b) {
		if (!b) {
			String msg = "check: \"" + s + "\"";
			throw new ContractException(msg);
		}
	}
}

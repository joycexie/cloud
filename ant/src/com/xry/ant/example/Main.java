package com.xry.ant.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 主程序 调用ACO求解问题
 * 
 * @author FashionXu
 */
public class Main {
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		ACO aco;
		aco = new ACO();
		try {
			aco.init("F://eclipse_workspace//ant//src//myeil51.tsp", 20);
			aco.run(1);
			aco.ReportResult();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
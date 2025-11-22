package com.simpfi.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
/**
 * Custom Frame class that inherits <@link javax.swing.JFrame>.
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * Constructor customed to match our software's style and place the frame in the center.
	 */
	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1120, 660);
		this.setTitle("Simpfi - Traffic Simulation");
		this.setLayout(new BorderLayout(10, 10));

		// Set Frame location to center
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) screenSize.getWidth() / 2 - this.getWidth() / 2,
			(int) screenSize.getHeight() / 2 - this.getHeight() / 2);

		setVisible(true);
	}
	// /**
	//  * Prints out the text in the clicked button (Phuc & Khanh check this).
	//  * @param evt refers to the action, in this case 
	//  */
	// public void Test_Button(ActionEvent evt) {
	// 	System.out.println(evt.getActionCommand());
	// }
	// /**
	//  * The method is intended to output information details to users
	//  * @param evt same as above
	//  */
	// public void ShowInformation(ActionEvent evt) {

	// }
}
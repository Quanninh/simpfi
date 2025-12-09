package com.simpfi.ui;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class InformationPopUp extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InformationPopUp(String title, Boolean modal) {
		this.setTitle(title);
		this.setSize(400,500);
		this.setModal(modal);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}

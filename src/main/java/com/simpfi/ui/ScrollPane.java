package com.simpfi.ui;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ScrollPane extends JScrollPane {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Panel contentPanel;

	public ScrollPane() {
		// Create inner panel
		contentPanel = new Panel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		// contentPanel.setPreferredSize(new Dimension(200, 300));

		// Set content panel as viewport
		setViewportView(contentPanel);

		init();
	}

<<<<<<< HEAD
    public void addCheckBoxLists(CheckBox[] options){
        for(CheckBox i : options){
            contentPanel.add(i);
        }
        // contentPanel.revalidate();
        // contentPanel.repaint();
    }
=======
	private void init() {
		getVerticalScrollBar().setUnitIncrement(16);
		setFocusable(false);
	}

	public void addCheckBoxLists(CheckBox[] options) {
		for (CheckBox i : options) {
			contentPanel.add(i);
		}
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	public void addItem(JComponent item) {
		contentPanel.add(item);
	}
>>>>>>> 7b22c15536eff7616fed199cfdfe01fea751e678
}
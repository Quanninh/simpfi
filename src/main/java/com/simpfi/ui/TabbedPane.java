package com.simpfi.ui;

import javax.swing.JTabbedPane;

import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

/**
 * A custom Tabbed Pane with tabs placed on the left side and scrollable tab
 * layout. The class inherits {@link javax.swing.JTabbedPane}.
 */
public class TabbedPane extends JTabbedPane {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new tabbed pane.
	 */
	public TabbedPane() {
		this.setTabPlacement(JTabbedPane.LEFT);
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		// Stretch all tabs to fill the available space to better match the UI mockup
		this.setUI(new FlatTabbedPaneUI() {
			@Override
			protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
				int tabCount = getTabCount();
				if (tabCount > 0) {
					return getHeight() / tabCount;
				}
				return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
			}
		});
	}

}

package kr.or.visitkorea.admin.client.manager.preview.composite;

import gwt.material.design.client.ui.MaterialPanel;

public abstract class AbstractPreviewPanel extends MaterialPanel {

	public AbstractPreviewPanel() {
		super();
		init();
	}

	public AbstractPreviewPanel(String... initialClass) {
		super(initialClass);
		init();
	}

	/**
	 * 
	 */
	private void init() {

		
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
	}

	
}

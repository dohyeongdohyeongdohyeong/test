package kr.or.visitkorea.admin.client.manager.preview.composite.siteMain;

import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.preview.composite.AbstractPreviewPanel;

public class ShowcasePreview extends AbstractPreviewPanel {

	public ShowcasePreview() {
		super();
		init();
	}

	public ShowcasePreview(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {

		MaterialPanel contentsPanel = new MaterialPanel();
		contentsPanel.setHeight("100%");
		
		
	}
	
}

package kr.or.visitkorea.admin.client.manager.widgets;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PreviewWindow extends MaterialExtentsWindow {

	private String url;

	public PreviewWindow() {
		super(null, 0);
		init();
	}

	public PreviewWindow(String title, String url) {
		super(title);
		this.url = url;
		this.setWidth("800px");
		this.setHeight("600px");
	}

	private void init() {
		UrlPanel contentPanel = new UrlPanel();
		contentPanel.setUrl(this.url);
		this.add(contentPanel);
	}

}

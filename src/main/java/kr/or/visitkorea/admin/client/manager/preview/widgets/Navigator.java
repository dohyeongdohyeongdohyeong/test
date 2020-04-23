package kr.or.visitkorea.admin.client.manager.preview.widgets;

import com.google.gwt.dom.client.Document;

import gwt.material.design.client.base.MaterialWidget;

public class Navigator extends MaterialWidget {

	private String url;

	public Navigator() {
        super(Document.get().createIFrameElement());
		init();
	}

	public Navigator(String prevUrl) {
        super(Document.get().createIFrameElement());
		init();
		this.setUrl(prevUrl);
	}

	private void init() {
		
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void navigate() {
		this.getElement().setAttribute("src", this.url);
	}

}

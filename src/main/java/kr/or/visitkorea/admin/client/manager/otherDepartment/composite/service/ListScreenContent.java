package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.service;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.preview.widgets.Navigator;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;

public class ListScreenContent extends AbstractContentPanel {

	private MaterialPanel previewPanel;
	private MaterialPanel detailPanel;
	private Navigator navi;

	@Override
	public void init() {
		
		this.previewPanel = new MaterialPanel();
		this.previewPanel.setLayoutPosition(Position.ABSOLUTE);
		this.previewPanel.setLeft(0);
		this.previewPanel.setTop(0);
		this.previewPanel.setWidth("400px");
		this.previewPanel.setBottom(0);
		this.previewPanel.setBackgroundColor(Color.ORANGE_ACCENT_1);
		this.add(this.previewPanel);

		this.navi = new Navigator();
		this.navi.setLayoutPosition(Position.ABSOLUTE);
		this.navi.setLeft(0);
		this.navi.setRight(0);
		this.navi.setTop(0);
		this.navi.setBottom(0);
		this.navi.setDisplay(Display.BLOCK);
		this.navi.setWidth("100%");
		this.navi.setHeight("100%");
		this.navi.setUrl("https://www.visitkorea.or.kr/");
		this.navi.navigate();
		
		previewPanel.add(this.navi);
		
		this.detailPanel = new MaterialPanel();
		this.detailPanel.setLayoutPosition(Position.ABSOLUTE);
		this.detailPanel.setLeft(410);
		this.detailPanel.setTop(0);
		this.detailPanel.setRight(0);
		this.detailPanel.setBottom(0);
		this.detailPanel.setBackgroundColor(Color.ORANGE_ACCENT_2);
		this.add(this.detailPanel);
		
	}
	
	public void setUrl(String url) {
		this.navi.setUrl(url);
		this.navi.navigate();
	}


}

package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.service;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRange;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.preview.widgets.Navigator;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;

public class MainScreenContent extends AbstractContentPanel implements Navigatable {

	private MaterialPanel previewPanel;
	private MaterialPanel detailPanel;
	private Navigator navi;
	private int previewWidth = 600;

	@Override
	public void init() {
		
		this.previewWidth = 600;
		this.previewPanel = new MaterialPanel();
		this.previewPanel.setLayoutPosition(Position.ABSOLUTE);
		this.previewPanel.setLeft(0);
		this.previewPanel.setTop(0);
		this.previewPanel.setWidth(previewWidth +"px");
		this.previewPanel.setBottom(0);
		this.add(this.previewPanel);

		initTopMenu();
		
		this.navi = new Navigator();
		this.navi.setLayoutPosition(Position.ABSOLUTE);
		this.navi.setLeft(0);
		this.navi.setRight(0);
		this.navi.setTop(35);
		this.navi.setBottom(0);
		this.navi.setDisplay(Display.BLOCK);
		this.navi.setWidth("100%");
		this.navi.getElement().getStyle().setProperty("height", "calc(100% - 35px)");
		this.navi.setUrl("https://www.visitkorea.or.kr/");
		this.navi.navigate();
		
		previewPanel.add(this.navi);
		
		this.detailPanel = new MaterialPanel();
		this.detailPanel.setLayoutPosition(Position.ABSOLUTE);
		this.detailPanel.setLeft(previewWidth + 10);
		this.detailPanel.setTop(0);
		this.detailPanel.setRight(0);
		this.detailPanel.setBottom(0);
		this.add(this.detailPanel);
		
	}
	
	private MaterialPanel topMenu;

	private void initTopMenu() {

		MaterialRange range = new MaterialRange();
		range.setLineHeight(40);
		range.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		range.setMax(100);
		range.setMin(50);
		range.setValue(51, true);
		range.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {

				int eventValueInteger = event.getValue();
				double eventValueDouble = eventValueInteger / 100.0;
				com.google.gwt.dom.client.Style naviStyle = navi.getElement().getStyle();
				naviStyle.setProperty("transform", "scale("+eventValueDouble+")");
				
			}
		});
		
		topMenu = new MaterialPanel();
		topMenu.setHeight("40px");
		topMenu.setTop(0);
		topMenu.setLeft(0);
		topMenu.setRight(0);
		topMenu.setBorderBottom("1px solid #efefef");
		topMenu.add(range);
		
		this.previewPanel.add(topMenu);
	}

	public void setUrl(String url) {
		this.navi.setUrl(url);
	}

	public int getPreviewWidth() {
		return previewWidth;
	}

	public void setPreviewWidth(int previewWidth) {
		this.previewWidth = previewWidth;
	}

}

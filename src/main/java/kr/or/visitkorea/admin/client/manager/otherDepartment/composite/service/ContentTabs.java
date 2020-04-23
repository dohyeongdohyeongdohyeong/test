package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.service;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.StatusChangeEvent;

public class ContentTabs  extends AbstractContentPanel {

	private HashMap<String, Object> tabValueMap = new HashMap<String, Object>();
	private MaterialPanel mainContentPanel;
	private SelectionPanel box;

	public ContentTabs(ContentTab ... tabs) {
		
		for (int i=0; i<tabs.length; i++) {
			
			this.tabValueMap.put(tabs[i].getTitle(), tabs[i]);
		
		}

		buildContent();
		
	}

	private void buildContent() {

		this.box = new SelectionPanel();
		this.box.setElementMargin(5);
		this.box.setElementPadding(10);
		this.box.setElementRadius(8);
		this.box.setTextAlign(TextAlign.LEFT);
		this.box.setSingleSelection(true);
		this.box.setValues(tabValueMap);
		this.box.setMarginLeft(10);
		this.box.setMarginTop(10);
		this.box.setLeft(10);
		this.box.setLineHeight(46.25);
		this.box.setHeight("46.25px");
		this.box.addStatusChangeEvent(new StatusChangeEvent() {

			@Override
			public void fire(Object selecteBaseLink) {
				
				MaterialPanel tgrWidget = (MaterialPanel)box.getSelectedValue();
				tgrWidget.setLayoutPosition(Position.ABSOLUTE);
				tgrWidget.setLeft(0);
				tgrWidget.setTop(0);
				tgrWidget.setRight(0);
				tgrWidget.setBottom(0);

				mainContentPanel.clear();
				mainContentPanel.add(tgrWidget);
			}
			
		});
		
		this.add(box);

		this.mainContentPanel = new MaterialPanel();
		this.mainContentPanel.setLayoutPosition(Position.ABSOLUTE);
		this.mainContentPanel.setLeft(10);
		this.mainContentPanel.setTop(60);
		this.mainContentPanel.setRight(20);
		this.mainContentPanel.setBottom(10);
		this.add(this.mainContentPanel);
	
	}
	
	@Override
	public void init() {}

	public void setSelection(int index) {
		
		String keyTitle = (String) tabValueMap.keySet().toArray()[index];
		
		this.box.setSelectionOnSingleMode(keyTitle);
		this.mainContentPanel.clear();
		MaterialPanel tgrWidget = (MaterialPanel) tabValueMap.get(keyTitle);
		tgrWidget.setLayoutPosition(Position.ABSOLUTE);
		tgrWidget.setLeft(0);
		tgrWidget.setTop(0);
		tgrWidget.setRight(0);
		tgrWidget.setBottom(0);
		this.mainContentPanel.add(tgrWidget);
		
	}

}

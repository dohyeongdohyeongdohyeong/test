package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import kr.or.visitkorea.admin.client.manager.event.widgets.FullTitleContentRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public class ContentsBaseTemplate extends FullTitleContentRow {
	private SelectionPanel templatePanel;
	
	public ContentsBaseTemplate() {
		super();
		init();
	}

	public ContentsBaseTemplate(AbstractEventContents host, String title, Color color) {
		super(host, title, color);
		init();
	}
	
	private void init() {
		HashMap<String, Object> templateMap = new HashMap<>();
		templateMap.put("템플릿A", "A");
		templateMap.put("템플릿B", "B");
		templateMap.put("템플릿C", "C");
		templateMap.put("자유형", "FR");
		
		templatePanel = addSelectionPanel(this.getContentRow(), "s12", TextAlign.LEFT, templateMap, 5, 25, 5, true);
		templatePanel.setTop(15);
		templatePanel.setRight(0);
		templatePanel.setLeft(0);
		templatePanel.setHeight("300px");
		templatePanel.setLayoutPosition(Position.RELATIVE);
	}
	
	@Override
	protected ClickHandler addSaveIconClickEvent() {
		return null;
	}

	@Override
	protected ClickHandler addEditIconClickEvent() {
		return null;
	}

	@Override
	protected JSONObject buildModel() {
		return null;
	}

	@Override
	protected void setValue(JSONObject obj) {
		
	}

	public SelectionPanel getTemplatePanel() {
		return templatePanel;
	}

}

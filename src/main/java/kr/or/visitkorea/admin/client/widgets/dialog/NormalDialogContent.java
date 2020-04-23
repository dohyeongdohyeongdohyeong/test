package kr.or.visitkorea.admin.client.widgets.dialog;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;

public class NormalDialogContent extends DialogContent {

	@Override
	public void init() {
		
		this.setBackgroundColor(Color.WHITE);
		this.setPadding(20);
		this.setPaddingBottom(60);
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {

		String titleString = (String) parameters.get("title");
		
		if (titleString != null) {
			
			MaterialLabel panelTitle = new MaterialLabel(titleString);
			panelTitle.setFontSize("1.2em");
			panelTitle.setFontWeight(FontWeight.BOLD);
			panelTitle.setTextColor(Color.BLUE);
			panelTitle.setPaddingLeft(10);
			panelTitle.setPaddingBottom(10);
			this.add(panelTitle);
			
		}

		super.setParameters(parameters);
		String[] messages = (String[])parameters.get("messages");
		List<ButtonInfo> btnInfos = (List<ButtonInfo>)parameters.get("buttonInfos");
		
		boolean firstMent = true;
		for (String ment : messages) {
			MaterialLabel mentObj = new MaterialLabel();
			mentObj.setText(ment);
			if (firstMent) {
				mentObj.setPaddingLeft(20);
				mentObj.setFontSize("1.1em");
				mentObj.setFontWeight(FontWeight.BOLD);
				mentObj.setMarginBottom(20);
				firstMent = false;
			}else {
				mentObj.setPaddingLeft(40);
			}
			this.add(mentObj);
		}
		
		for (ButtonInfo info : btnInfos) {
			addButton(this, info.getName(), info.getColor(), info.getClickHandler(), info.getFloat());
		}

	}

	@Override
	public int getHeight() {
		return 250;
	}

}

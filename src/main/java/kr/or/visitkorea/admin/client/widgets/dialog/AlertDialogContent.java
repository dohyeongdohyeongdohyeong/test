package kr.or.visitkorea.admin.client.widgets.dialog;

import java.util.Map;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;

public class AlertDialogContent extends DialogContent {

	private MaterialLabel ment;


	@Override
	public void init() {
		this.setBackgroundColor(Color.WHITE);
		this.setPadding(20);
		this.setPaddingBottom(60);
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		String[] messages = (String[])parameters.get("messages");
		
		for (String ment : messages) {
			MaterialLabel mentObj = new MaterialLabel();
			mentObj.setText(ment);
			this.add(mentObj);
		}
		
	}


	@Override
	public int getHeight() {
		return 200;
	}

}

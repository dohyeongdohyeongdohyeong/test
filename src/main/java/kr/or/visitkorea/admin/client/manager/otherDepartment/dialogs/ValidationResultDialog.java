package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ValidationResultDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialLabel contentsInfo;

	public ValidationResultDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		contentsInfo.setText((String) getParameters().get("CONTENT_INFO"));
		
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 확인");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		
		contentsInfo = new MaterialLabel();
		contentsInfo.setFontSize("1.1em");
		contentsInfo.setFontWeight(FontWeight.BOLD);
		contentsInfo.setLayoutPosition(Position.ABSOLUTE);
		contentsInfo.setLeft(30);
		contentsInfo.setTop(70);
		this.add(contentsInfo);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}


	@Override
	public int getHeight() {
		return 220;
	}

}

package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SaveContentDialog extends DialogContent {
	private MaterialLabel dialogTitle;
	private MaterialButton selectButton;
	private ClickHandler targetHandler;
	private MaterialLabel alertLabel;

	public SaveContentDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	private void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("- 컨텐츠 저장");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
		alertLabel = new MaterialLabel();
		alertLabel.setLayoutPosition(Position.ABSOLUTE);
		alertLabel.setTextColor(Color.RED_LIGHTEN_2);
		alertLabel.setFontWeight(FontWeight.BOLD);
		alertLabel.setPaddingTop(100);
		alertLabel.setPaddingLeft(30);
		alertLabel.setFontSize("20px");
		alertLabel.setWidth("100%");
		alertLabel.setText("");
		this.add(alertLabel);
		
		selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		this.addButton(selectButton);
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		if (parameters.containsKey("SAVE_CLICK_HANDLER") && targetHandler == null) {
			this.targetHandler = (ClickHandler) parameters.get("SAVE_CLICK_HANDLER");
			selectButton.addClickHandler(targetHandler);
		}
	}

	@Override
	public int getHeight() {
		return 300;
	}

}

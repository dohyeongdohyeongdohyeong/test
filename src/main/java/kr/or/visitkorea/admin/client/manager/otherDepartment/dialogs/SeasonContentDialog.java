package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.FestivalContentLeftArea;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.FestivalDetailSelectContent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListImageCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SeasonContentDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private FestivalContentLeftArea leftArea;
	private FestivalDetailSelectContent rightArea;

	public SeasonContentDialog(MaterialExtentsWindow window) {
		super(window);
		leftArea = new FestivalContentLeftArea(getMaterialExtentsWindow(), this);
		rightArea = new FestivalDetailSelectContent(getMaterialExtentsWindow(), this);
		leftArea.setRightPanel(rightArea); 
		rightArea.setLeftPanel(leftArea);
		buildContent();
	}

	@Override
	public void init() {
	}

	private void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("- 컨텐츠 설정");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		
		leftArea.setWidth("420px");
		leftArea.setHeight("450px");
		leftArea.setMarginLeft(20);
		leftArea.setMarginRight(20);
		leftArea.setMarginTop(20);
		leftArea.setFloat(Float.LEFT);
		
		rightArea.setWidth("620px");
		rightArea.setHeight("450px");
		rightArea.setFloat(Float.LEFT);
		
		this.add(leftArea);
		this.add(rightArea);
		
		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			TagListRow tagListRow = (TagListRow)this.getParameters().get("SELECTED_CONDITION");
			TagListLabelCell  cklc = (TagListLabelCell)tagListRow.getCell(3);
			cklc.setText(rightArea.getRowCount() + "");
			rightArea.save();
			getMaterialExtentsWindow().closeDialog();
		});
		
		this.addButton(selectButton);
		
		MaterialLabel help = new MaterialLabel("* 빨간색 글씨로 되어있는 컨텐츠는 현재 노출상태가 아닌 컨텐츠입니다.");
		help.setTextColor(Color.RED_LIGHTEN_1);
		help.setFontWeight(FontWeight.BOLD);
		help.setFontSize("15px");
		getButtonArea().add(help);
		
	}

	@Override
	public int getHeight() {
		return 600;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		leftArea.setParamters(parameters);
		rightArea.setParamters(parameters);

	}
}

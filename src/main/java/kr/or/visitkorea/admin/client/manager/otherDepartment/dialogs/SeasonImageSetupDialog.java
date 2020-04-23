package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.FestivalContentLeftAreaForImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.FestivalContentRightArea;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.FestivalDetailContentImages;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SeasonImageSetupDialog extends DialogContent {
	private MaterialLabel dialogTitle;
	private FestivalDetailContentImages fdciLeft;

	public SeasonImageSetupDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	private void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("- 계절 이미지 설정");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		
		FestivalContentRightArea rightArea = new FestivalContentRightArea();
		rightArea.setWidth("520px");
		rightArea.setHeight("450px");
		rightArea.setMarginLeft(20);
		rightArea.setMarginRight(20);
		rightArea.setMarginTop(20);
		rightArea.setFloat(Float.LEFT);
		
		fdciLeft = new FestivalDetailContentImages();
		rightArea.setPanel(fdciLeft);
		
		this.add(rightArea);
		
		MaterialLabel guideLabel = new MaterialLabel();
		guideLabel.setFontSize("1.0em");
		guideLabel.setFontWeight(FontWeight.BOLD);
		guideLabel.setTextColor(Color.BLUE);
		guideLabel.setPaddingLeft(20);
		guideLabel.setPaddingBottom(10);
		guideLabel.setText("배경업로드 사이즈 추가 : 940 X 645");
		
		this.add(guideLabel);
		
		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			getParameters().put("BG_IMG_NAME", fdciLeft.getUploadBackgroundImage().getUUID());
			getParameters().put("BG_SAVE_NAME", fdciLeft.getUploadBackgroundImage().getSaveName());
			getMaterialExtentsWindow().closeDialog();
		});

		this.addButton(selectButton);
	}

	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		fdciLeft.setParameter(parameters);
	}

	@Override
	public int getHeight() {
		return 600;
	}
	
	@Override
	public void onLoad() {
	}
}

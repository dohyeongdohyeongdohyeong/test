package kr.or.visitkorea.admin.client.widgets.dialog;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.UploadPanel;

public class ImageDialogContent extends DialogContent {

	private UploadPanel uploadBackgroundImage;
	private MaterialLabel guideLabel;

	@Override
	public void init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
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
		
		uploadBackgroundImage = new UploadPanel("");
		uploadBackgroundImage.setFloat(Float.LEFT);
		uploadBackgroundImage.setWidth("100%");
		uploadBackgroundImage.setHeight("100%");
		uploadBackgroundImage.setParameter(parameters);
		
		MaterialImage materialImage = (MaterialImage) parameters.get("materialImage");
		if (titleString != null) {
			uploadBackgroundImage.setSource(materialImage);
			uploadBackgroundImage.setPrevUrl(materialImage.getUrl());
		}
		this.add(uploadBackgroundImage);
		
		guideLabel = new MaterialLabel();
		guideLabel.setFontSize("1.0em");
		guideLabel.setFontWeight(FontWeight.BOLD);
		guideLabel.setTextColor(Color.BLUE);
		guideLabel.setLayoutPosition(Position.ABSOLUTE);
		guideLabel.setBottom(67);
		guideLabel.setLeft(30);
		guideLabel.setText("태그아이콘 사이즈 추가 : 37 X 37");
		guideLabel.setVisible(false);
		
		this.add(guideLabel);
		
		super.setParameters(parameters);
		
	}

	public MaterialLabel getGuideLabel() {
		return guideLabel;
	}
	
	public void setGuideLabelVisible(boolean visibleFlag) {
		guideLabel.setVisible(visibleFlag);
	}

	@Override
	public int getHeight() {
		return 250;
	}

	public void setBounds(int width, int height) {
		uploadBackgroundImage.setImageWidth(width - 45);
		uploadBackgroundImage.setImageHeight(height - 165);
		this.diagTop = (height * -2);
	}
	
	public void apply() {
		uploadBackgroundImage.apply();
	}

}

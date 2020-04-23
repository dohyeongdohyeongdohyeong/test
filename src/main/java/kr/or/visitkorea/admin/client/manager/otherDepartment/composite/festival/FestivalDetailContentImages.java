package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;

import gwt.material.design.client.ui.MaterialPanel;

public class FestivalDetailContentImages extends MaterialPanel {
	private UploadPanel uploadBackgroundImage;
	
	public FestivalDetailContentImages() {
		super();
		init();
	}
	
	private void init() {
		uploadBackgroundImage = new UploadPanel("- 배경 이미지");
		uploadBackgroundImage.setFloat(Float.LEFT);
		uploadBackgroundImage.setWidth("100%");
		uploadBackgroundImage.setHeight("100%");
		this.add(uploadBackgroundImage);
	}
	
	public UploadPanel getUploadBackgroundImage() {
		return this.uploadBackgroundImage;
	}

	public void setParameter(Map<String, Object> parameters) {
		this.uploadBackgroundImage.setParameter(parameters);
	}
}

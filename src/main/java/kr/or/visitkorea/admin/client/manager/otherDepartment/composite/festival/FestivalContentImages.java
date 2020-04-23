package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class FestivalContentImages extends MaterialPanel {

	private String label1;
	private String label2;

	public FestivalContentImages(String label1, String label2) {
		super();
		this.label1 = label1;
		this.label2 = label2;
		init();
	}

	private void init() {
		
		MaterialPanel leftImage = new MaterialPanel();
		leftImage.setDisplay(Display.INLINE_BLOCK);
		leftImage.setPaddingRight(2.5);
		leftImage.setWidth("50%");
		leftImage.setMaxWidth("209px");
		leftImage.setMaxHeight("184px");
		leftImage.setFloat(Float.LEFT);
		leftImage.setBorder("1px solid #efefef");
		this.add(leftImage);
		
		MaterialLabel imageLabel1 = new MaterialLabel(this.label1);
		imageLabel1.setTextAlign(TextAlign.LEFT);
		imageLabel1.setWidth("100%");
		imageLabel1.setMarginBottom(5);
		imageLabel1.setFontWeight(FontWeight.BOLD);
		
		MaterialImage targetImage1 = new MaterialImage();
		targetImage1.setDisplay(Display.INLINE_BLOCK);
		targetImage1.setUrl(GWT.getHostPageBaseURL() + "images/default-image.png");
		targetImage1.setWidth("auto");
		targetImage1.setHeight("auto");
		targetImage1.setMaxHeight("100%");
		targetImage1.setMaxWidth("100%");

		MaterialPanel leftImagePanel1 = new MaterialPanel();
		leftImagePanel1.setLayoutPosition(Position.RELATIVE);
		leftImagePanel1.setPaddingRight(2.5);
		leftImagePanel1.setLineHeight(180);
		leftImagePanel1.setWidth("100%");
		leftImagePanel1.setHeight("180px");
		leftImagePanel1.setBackgroundColor(Color.WHITE);
		leftImagePanel1.setBorder("1px solid #efefef");
		leftImagePanel1.setTextAlign(TextAlign.CENTER);
		leftImagePanel1.add(targetImage1);
		
		leftImage.add(imageLabel1);
		leftImage.add(leftImagePanel1);
		
		MaterialPanel rightImage = new MaterialPanel();
		rightImage.setDisplay(Display.INLINE_BLOCK);
		rightImage.setPaddingLeft(2.5);
		rightImage.setWidth("50%");
		rightImage.setMaxWidth("209px");
		rightImage.setMaxHeight("184px");
		rightImage.setFloat(Float.LEFT);
		rightImage.setBorder("1px solid #efefef");
		this.add(rightImage);
		
		MaterialLabel imageLabel2 = new MaterialLabel(this.label2);
		imageLabel2.setTextAlign(TextAlign.LEFT);
		imageLabel2.setWidth("100%");
		imageLabel2.setMarginBottom(5);
		imageLabel2.setFontWeight(FontWeight.BOLD);
		
		MaterialImage targetImage2 = new MaterialImage();
		targetImage2.setDisplay(Display.INLINE_BLOCK);
		targetImage2.setUrl(GWT.getHostPageBaseURL() + "images/default-image.png");
		targetImage2.setWidth("auto");
		targetImage2.setHeight("auto");
		targetImage2.setMaxHeight("100%");
		targetImage2.setMaxWidth("100%");

		MaterialPanel leftImagePanel2 = new MaterialPanel();
		leftImagePanel2.setLayoutPosition(Position.RELATIVE);
		leftImagePanel2.setPaddingRight(2.5);
		leftImagePanel2.setLineHeight(180);
		leftImagePanel2.setWidth("100%");
		leftImagePanel2.setHeight("180px");
		leftImagePanel2.setBackgroundColor(Color.WHITE);
		leftImagePanel2.setBorder("1px solid #efefef");
		leftImagePanel2.setTextAlign(TextAlign.CENTER);
		leftImagePanel2.add(targetImage2);
		
		rightImage.add(imageLabel2);
		rightImage.add(leftImagePanel2);
		
	}

}

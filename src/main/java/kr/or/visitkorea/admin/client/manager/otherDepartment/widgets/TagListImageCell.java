package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialImage;

public class TagListImageCell extends MaterialImage implements VisitKoreaListCell{

	private boolean divBorder;

	public TagListImageCell(String value, Float alignFloat, String width, String height, int lineHeight, FontWeight fontWeight, boolean isDivBorder) {
		super(value);
		this.setDisplay(Display.BLOCK);
		this.setHeight("30px");
		this.setPaddingLeft(5);
		this.setPaddingRight(5);
		this.setUrl(value);
		this.setFloat(alignFloat);
		this.getElement().getStyle().setProperty("width", width);
		//this.setFontWeight(fontWeight);
		this.setPaddingTop(15 );
		//this.setHeight(height + "px");
		this.setLineHeight(lineHeight);
		this.divBorder = isDivBorder;
	}

	public boolean isDivBorder() {
		return divBorder;
	}

	public void setDivBorder(boolean divBorder) {
		this.divBorder = divBorder;
	}

	@Override
	public String getCellWidth() {
		return this.getElement().getStyle().getProperty("width");
	}

	@Override
	public String getValue() {
		return super.getValue();
	}
}
	
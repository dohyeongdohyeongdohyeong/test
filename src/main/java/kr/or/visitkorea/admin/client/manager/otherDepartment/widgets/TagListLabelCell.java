package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;

public class TagListLabelCell extends MaterialLabel implements VisitKoreaListCell{

	private boolean divBorder;

	public TagListLabelCell(String value, Float alignFloat, String width, String height, int lineHeight, FontWeight fontWeight, boolean isDivBorder, TextAlign textAlign) {
		super(value);
		this.setText(value);
		this.setTextAlign(textAlign);
		this.setPaddingLeft(5);
		this.setPaddingRight(5);
		this.setFloat(alignFloat);
		this.getElement().getStyle().setProperty("width", width);
		this.setFontWeight(fontWeight);
		this.setTop(0);
		this.setHeight(height + "px");
		this.setLineHeight(lineHeight);
		this.divBorder = isDivBorder;
	}
	
	public TagListLabelCell(String value, Float alignFloat, String width, String height, int lineHeight, FontWeight fontWeight, boolean isDivBorder, TextAlign textAlign, Color textColor) {
		super(value);
		this.setText(value);
		this.setTextAlign(textAlign);
		this.setPaddingLeft(5);
		this.setPaddingRight(5);
		this.setFloat(alignFloat);
		this.getElement().getStyle().setProperty("width", width);
		this.setFontWeight(fontWeight);
		this.setTop(0);
		this.setHeight(height + "px");
		this.setLineHeight(lineHeight);
		this.divBorder = isDivBorder;
		this.setTextColor(textColor);
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

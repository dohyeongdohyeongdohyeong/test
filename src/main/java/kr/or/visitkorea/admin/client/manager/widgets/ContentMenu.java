package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;

public class ContentMenu extends AbstractContentPanel {

	@Override
	public void init() {

		this.setHeight("26px");
		this.setLayoutPosition(Position.RELATIVE);

	}

	public void addIcon(IconType iconType, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat) {

		MaterialIcon icon = new MaterialIcon();
		icon.setIconType(iconType);
		icon.setTooltip(tooltip);
		icon.setVerticalAlign(VerticalAlign.MIDDLE);
		icon.setFontSize("1.0em");
		icon.setHeight("26px");
		icon.setMargin(0);
		icon.setWidth("26px");
		icon.setFloat(styleFloat);
		icon.setLineHeight(26);
		icon.setBorder("1px solid #aaaaaa");

		this.add(icon);
		
	}

	public void addIcon(MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float align) {
		addIcon(icon, tooltip, align, "1.0em", true);
	}

	public void addIcon(MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, boolean isBorder) {
		 addIcon(icon, tooltip, styleFloat, fontSize, "26px", 24, isBorder);
	}

	public void addIcon(MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, String size, int lineHeight, boolean isBorder) {
		icon.setTooltip(tooltip);
		icon.setVerticalAlign(VerticalAlign.MIDDLE);
		icon.setFontSize(fontSize);
		icon.setHeight(size);
		icon.setMarginLeft(2);
		icon.setMarginRight(2);
		icon.setWidth(size);
		icon.setFloat(styleFloat);
		icon.setLineHeight(24);
		if (isBorder) {
			if (isBorder && styleFloat.equals(com.google.gwt.dom.client.Style.Float.LEFT)) {
				icon.setBorderRight("1px solid #e0e0e0");
			} else {
				icon.setBorderLeft("1px solid #e0e0e0");
			}
		}
		
		this.add(icon);
	}
	public void addLabel(MaterialLabel label, com.google.gwt.dom.client.Style.Float align) {
		addLabel(label, align, "1.0em", true);
	}
	public void addLabel(MaterialLabel label, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, boolean isBorder) {
		addLabel(label, styleFloat, fontSize, "26px", 24, isBorder);
	}
	public void addLabel(MaterialLabel label, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, String size, int lineHeight, boolean isBorder) {
		label.setVerticalAlign(VerticalAlign.MIDDLE);
		label.setFontSize(fontSize);
		label.setHeight(size);
		label.setMarginLeft(2);
		label.setMarginRight(2);
		label.setFloat(styleFloat);
		label.setLineHeight(24);
		this.add(label);
	}
	public void addButton(MaterialButton btn, com.google.gwt.dom.client.Style.Float align) {
		addButton(btn, align, "1.0em", true);
	}
	public void addButton(MaterialButton btn, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, boolean isBorder) {
		addButton(btn, styleFloat, fontSize, "26px", 24, isBorder);
	}
	public void addButton(MaterialButton btn, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, String size, int lineHeight, boolean isBorder) {
		btn.setVerticalAlign(VerticalAlign.MIDDLE);
		btn.setFontSize(fontSize);
		btn.setHeight(size);
		btn.setMarginLeft(2);
		btn.setMarginRight(2);
		btn.setFloat(styleFloat);
		btn.setLineHeight(24);
		this.add(btn);
	}
	public void addIcon(MaterialIcon icon) {
		this.add(icon);
	}

	public Widget getMenu(int i) {
		return this.getWidget(i);
	}


}

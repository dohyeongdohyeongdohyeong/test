package kr.or.visitkorea.admin.client.manager.event.dashboard.panel;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.event.EventContentBundle;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookMain;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class EventDashboardBasePanel extends AbstractContentPanel implements IEventConstant {


	protected AbstractContentPanel host;

	protected MaterialExtentsWindow window;

	static {
		MaterialDesignBase.injectCss(EventContentBundle.INSTANCE.eventCss());
	}
	
	EventDashboardBasePanel(AbstractContentPanel host) {
		this.host = host;
		setPadding(18);
		setWidth("100%");
	}
	
	EventDashboardBasePanel(MaterialExtentsWindow window) {
		this.window = window;
		setPadding(18);
		setWidth("100%");
	}
	
	
	
	
	protected MaterialComboBox<Object> addColumn(MaterialWidget parent, MaterialComboBox<Object> combo , String grid) {
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(combo);
		parent.add(col);
		return combo;
	}
	
	protected MaterialComboBox<Object> addCombobox(MaterialWidget parent, String grid, String label) {
		MaterialComboBox<Object> combo = new MaterialComboBox<Object>();
		combo.setLabel(label);
		combo.setMargin(0);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(combo);
		parent.add(col);
		return combo;
	}

	protected MaterialTextBox addTextbox(MaterialWidget parent, String grid, String label) {
		MaterialTextBox tbox = new MaterialTextBox();
		tbox.setMargin(0);
		tbox.setLabel(label);
		tbox.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tbox);
		parent.add(col);
		return tbox;
	}
	
	protected MaterialInput addInputDate(MaterialWidget parent, String grid, String label) {
		MaterialInput input = new MaterialInput(InputType.DATE);
		input.setMargin(0);
		input.setTooltip(label);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(input);
		parent.add(col);
		return input;
	}
	
	protected MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
	}
	

	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}	
	
	
	protected MaterialTextBox addInputText(MaterialRow row, String text, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setText(text);
		box.setReadOnly(true);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}

	protected MaterialTextBox addInputText(MaterialRow row, String text, String grid, boolean isReadOnly) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setPlaceholder(text);
		box.setReadOnly(isReadOnly);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected MaterialComboBox<Object> addCombobox(MaterialRow row, String grid, List<Object> valueList) {
		MaterialComboBox<Object> combo = new MaterialComboBox<Object>();
		combo.addItems(valueList);
		combo.setMargin(0);
		combo.setHeight("46.25px");
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(combo);
		row.add(col);
		return combo;
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
	
	protected String convertDateFormat(String strDate) {
		if (strDate.equals("") || strDate == null) return "";
		Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(strDate);
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}

}
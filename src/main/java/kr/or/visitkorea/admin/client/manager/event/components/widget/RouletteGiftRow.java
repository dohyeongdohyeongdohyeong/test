package kr.or.visitkorea.admin.client.manager.event.components.widget;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.event.model.EventGift;

public class RouletteGiftRow extends MaterialRow {
	private int index;
	private MaterialTextBox titleBox;
	private MaterialIcon orderUpIcon;
	private MaterialIcon orderDownIcon;
	private EventGift gift;
	
	public RouletteGiftRow(int index, EventGift gift) {
		super();
		this.index = index;
		this.gift = gift;
		this.init();
	}

	private void init() {
		this.setMargin(0);
		this.addLabel(this, (index + 1) + "", "s2");
		
		this.titleBox = addTextbox(this, this.gift.getTitle(), "s7");
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid("s3");
		column.setHeight("46px");
		column.setDisplay(Display.FLEX);
		column.setFlexAlignItems(FlexAlignItems.CENTER);
		this.add(column);
		
		this.orderUpIcon = addIcon(column, IconType.ARROW_UPWARD, Float.NONE);
		this.orderDownIcon = addIcon(column, IconType.ARROW_DOWNWARD, Float.NONE);
	}

	public void setEnabled(boolean isEnabled) {
		this.titleBox.setEnabled(isEnabled);
		this.orderUpIcon.setEnabled(isEnabled);
		this.orderDownIcon.setEnabled(isEnabled);
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public MaterialTextBox getTitleBox() {
		return titleBox;
	}

	public void setTitleBox(MaterialTextBox titleBox) {
		this.titleBox = titleBox;
	}

	public MaterialIcon getOrderUpIcon() {
		return orderUpIcon;
	}

	public void setOrderUpIcon(MaterialIcon orderUpIcon) {
		this.orderUpIcon = orderUpIcon;
	}

	public MaterialIcon getOrderDownIcon() {
		return orderDownIcon;
	}

	public void setOrderDownIcon(MaterialIcon orderDownIcon) {
		this.orderDownIcon = orderDownIcon;
	}

	protected MaterialIcon addIcon(MaterialWidget parent, IconType iconType, Float floatAlign) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setFloat(floatAlign);
		icon.setMargin(0);
		icon.setFontSize("26px");
		parent.add(icon);
		return icon;
	}
	
	private MaterialLabel addLabel(MaterialWidget parent, String text, String grid) {
		MaterialLabel label = new MaterialLabel();
		label.setText(text);
		label.setLineHeight(46);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		parent.add(col);
		return label;
	}
	
	private MaterialTextBox addTextbox(MaterialWidget parent, String value, String grid) {
		MaterialTextBox tbox = new MaterialTextBox();
		tbox.setValue(value);
		tbox.setMargin(0);
		tbox.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tbox);
		parent.add(col);
		return tbox;
	}
}

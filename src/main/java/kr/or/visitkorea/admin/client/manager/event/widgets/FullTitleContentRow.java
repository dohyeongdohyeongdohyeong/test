package kr.or.visitkorea.admin.client.manager.event.widgets;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.event.composite.AbstractEventContents;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public abstract class FullTitleContentRow extends MaterialPanel {
	protected AbstractEventContents host;
	protected MaterialRow titleRow;
	private MaterialLabel titleLabel;
	protected MaterialRow contentRow;
	private MaterialCheckBox checkbox;
	private MaterialIcon editIcon;
	private MaterialIcon saveIcon;
	private HashMap<String, Object> internalMap = new HashMap<>();
	
	public FullTitleContentRow() {
		init();
	}

	public FullTitleContentRow(AbstractEventContents host, String title, Color color) {
		init();
		this.host = host;
		this.titleLabel.setText(title);
		this.titleRow.setBackgroundColor(color);
	}
	
	protected abstract ClickHandler addSaveIconClickEvent();
	protected abstract ClickHandler addEditIconClickEvent();
	protected abstract JSONObject buildModel();
	protected abstract void setValue(JSONObject obj);
	
	private void init() {
		this.setGrid("s12");
		
		titleRow = new MaterialRow();
		titleRow.setMargin(0);
		titleRow.setPadding(5);
		titleRow.setTextAlign(TextAlign.CENTER);

		checkbox = new MaterialCheckBox();
		checkbox.addValueChangeHandler(event -> {
			this.isVisibleContent(event.getValue());
		});
		checkbox.getElement().getStyle().setTop(4, Unit.PX);
		checkbox.getElement().getStyle().setPosition(Position.RELATIVE);
		
		MaterialColumn col = new MaterialColumn();
		col.setDisplay(Display.FLEX); 
		col.setFlexAlignItems(FlexAlignItems.CENTER);
		col.add(checkbox);
		titleRow.add(col);
		
		titleLabel = new MaterialLabel();
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setFontSize("1.4em");
		titleLabel.setVerticalAlign(VerticalAlign.MIDDLE);
		titleLabel.setDisplay(Display.INLINE_BLOCK);
		titleRow.add(titleLabel);

		editIcon = new MaterialIcon(IconType.EDIT);
		editIcon.setFontSize("1.8em");
		editIcon.setVerticalAlign(VerticalAlign.MIDDLE);
		editIcon.setDisplay(Display.INLINE_BLOCK);
		editIcon.setFloat(Float.RIGHT);
		editIcon.addClickHandler(this.addEditIconClickEvent());
		editIcon.addClickHandler(event -> {
			this.editIcon.setVisible(false);
			this.saveIcon.setVisible(true);
		});
		
		saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setFontSize("1.8em");
		saveIcon.setVerticalAlign(VerticalAlign.MIDDLE);
		saveIcon.setDisplay(Display.INLINE_BLOCK);
		saveIcon.setFloat(Float.RIGHT);
		saveIcon.setVisible(false);
		saveIcon.addClickHandler(this.addSaveIconClickEvent());
		saveIcon.addClickHandler(event -> {
			this.editIcon.setVisible(true);
			this.saveIcon.setVisible(false);
		});
		titleRow.add(editIcon);
		titleRow.add(saveIcon);
			
		contentRow = new MaterialRow();
		contentRow.setBackgroundColor(Color.WHITE);
		contentRow.setPadding(10);
		
		this.add(titleRow);
		this.add(contentRow);
	}

	protected MaterialCheckBox addCheckbox(MaterialWidget parent, String name, List<Widget> targetList) {
		MaterialCheckBox box = addCheckbox(parent, name);
		targetList.add(box);
		return box;
	}
	
	protected MaterialCheckBox addCheckbox(MaterialWidget parent, String name) {
		MaterialCheckBox box = new MaterialCheckBox();
		box.setText(name);
		
		MaterialColumn col = new MaterialColumn();
		col.setPadding(5);
		col.setGrid("s2");
		col.add(box);
		parent.add(col);
		return box;
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

	protected MaterialLink addLink(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLink tmpLabel = new MaterialLink(defaultValue);
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

	protected MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		row.setHeight("46.25px");
		parent.add(row);
		return row;
	}

	protected MaterialTextBox addInputText(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setPlaceholder(placeholder);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}

	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap) {
		return addSelectionPanel(row, grid, align, valueMap, 5, 5, 8, true);
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, String fontSize, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setFontSize(fontSize);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	public void isVisibleContent(boolean isVisible) {
		this.checkbox.setValue(isVisible, true);
		this.contentRow.setVisible(isVisible);
	}
	
	public MaterialCheckBox getCheckbox() {
		return checkbox;
	}

	public MaterialRow getTitleRow() {
		return titleRow;
	}

	public MaterialRow getContentRow() {
		return contentRow;
	}

	public MaterialIcon getEditIcon() {
		return editIcon;
	}

	public MaterialIcon getSaveIcon() {
		return saveIcon;
	}

	public HashMap<String, Object> getInternalMap() {
		return internalMap;
	}

	public void setTitle(String title) {
		this.titleLabel.setText(title);
	}
	
	public void setVisibleEditIcon(boolean isVisible) {
		this.getEditIcon().setVisible(isVisible);
	}
	
}

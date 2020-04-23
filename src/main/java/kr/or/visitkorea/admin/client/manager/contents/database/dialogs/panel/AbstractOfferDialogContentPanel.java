package kr.or.visitkorea.admin.client.manager.contents.database.dialogs.panel;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsETC;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectInformOfferDialog;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;

public abstract class AbstractOfferDialogContentPanel extends MaterialPanel {
	protected SelectInformOfferDialog dContent;
	protected MaterialRow selectFormRow;
	protected ContentTable table;
	protected MaterialLabel countLabel;
	
	public AbstractOfferDialogContentPanel(SelectInformOfferDialog dContent) {
		super();
		this.dContent = dContent;
		init();
	}
	
	public void init() {
		selectFormRow = new MaterialRow();
		selectFormRow.setWidth("100%");
		selectFormRow.setHeight("100%");
		selectFormRow.setMargin(0);
		this.add(selectFormRow);
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setWidth("100%");
		table.setHeight(415);
		this.add(table);

		MaterialIcon moreBtn = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreBtn.setTextAlign(TextAlign.CENTER);
		moreBtn.addClickHandler(e -> {
			qryList(false);
		});
		table.getButtomMenu().addIcon(moreBtn, "다음 20개", Float.RIGHT);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		buildSelectForm();
		buildContentTable();
	}
	
	public abstract void buildSelectForm();
	public abstract void buildContentTable();
	public abstract void qryList(boolean bstart);

	public void offerSelectProcess(String snsId,String snsUserName) {
		ContentsETC etc = (ContentsETC) this.dContent.getParameters().get("contentsEtc");
		etc.setSnsId(snsId);
		etc.setSnsUserName(snsUserName);
			this.dContent.getMaterialExtentsWindow().closeDialog();
	}
	
	
	public MaterialTextBox addTextBox(MaterialRow row, String label, String grid) {
		MaterialTextBox textBox = new MaterialTextBox();
		textBox.setLabel(label);
		textBox.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid(grid);
		column.add(textBox);
		row.add(column);
		return textBox;
	}
	
	public MaterialComboBox<String> addComboBox(MaterialRow row, String label, String grid, String ...item) {
		MaterialComboBox<String> combo = new MaterialComboBox<>();
		combo.setLabel(label);
		for (String s : item) {
			combo.addItem(s);
		}
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid(grid);
		column.add(combo);
		row.add(column);
		return combo;
		
	}
	
	public MaterialIcon addSearchIcon(MaterialRow row) {
		MaterialIcon icon = new MaterialIcon(IconType.SEARCH);
		icon.setFontSize("35px");
		icon.setGrid("s1");
		icon.setMarginTop(25);
		icon.addClickHandler(event -> {
			qryList(true);
		});
		row.add(icon);
		return icon;
	}
}

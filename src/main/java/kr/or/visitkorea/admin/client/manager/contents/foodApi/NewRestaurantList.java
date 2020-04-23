package kr.or.visitkorea.admin.client.manager.contents.foodApi;

import java.util.Arrays;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentsTree;
import kr.or.visitkorea.admin.client.manager.contents.foodApi.component.AbstractSearchComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class NewRestaurantList extends AbstractSearchComponent {
	
	private MaterialComboBox<String> dateOption;
	private MaterialTextBox startDate;
	private MaterialTextBox endDate;
	
	
	
	public NewRestaurantList(MaterialExtentsWindow window) {
		super(window);
	}
	
	@Override
	protected void setFetchCmd() {
		super.fetchCmd = "GET_NEW_RESTAURANT_LIST";
	}
	
	@Override
	protected void initSearchType(MaterialComboBox<String> searchType) {
		String[] searchTypeValues = {"컨텐츠명", "CID"};
		Arrays.asList(searchTypeValues).forEach(searchValue -> {
			super.searchType.addItem(searchValue);
		});
	}
	
	@Override
	protected void initSearchToolsArea(MaterialPanel searchBarArea) {
		String[] dateOptionValues = {"생성일", "수정일"};
		this.dateOption = new MaterialComboBox<String>();
		Arrays.asList(dateOptionValues).forEach(dateOptionValue -> {
			this.dateOption.addItem(dateOptionValue);
		});
		this.dateOption.setWidth("150px");
		this.dateOption.setMarginRight(30);
		this.dateOption.setLabel("날짜옵션");
		searchBarArea.add(this.dateOption);
		
		
		
		this.startDate = new MaterialTextBox();
		this.startDate.setMarginRight(30);
		this.startDate.setWidth("150px");
		this.startDate.setType(InputType.DATE);
		searchBarArea.add(this.startDate);
		
		
		this.endDate = new MaterialTextBox();
		this.endDate.setMarginRight(30);
		this.endDate.setWidth("150px");
		this.endDate.setType(InputType.DATE);
		searchBarArea.add(this.endDate);
	}

	@Override
	protected void initTable(ContentTable table) {
		table.appendTitle("CID", 80, TextAlign.CENTER);
		table.appendTitle("서비스 분류", 110, TextAlign.CENTER);
		table.appendTitle("지역", 80, TextAlign.CENTER);
		table.appendTitle("시군구", 80, TextAlign.CENTER);
		table.appendTitle("제목", 400, TextAlign.CENTER);
		table.appendTitle("대표태그", 150, TextAlign.CENTER);
		table.appendTitle("처리상태", 90, TextAlign.CENTER);
		table.appendTitle("수정일", 165, TextAlign.CENTER);
		table.appendTitle("생성일", 165, TextAlign.CENTER);
		table.appendTitle("작성자", 130, TextAlign.CENTER);
	}

	@Override
	protected JSONObject gatherSubmitData() {
		JSONObject submitData = new JSONObject();
		
		String dateOption = this.dateOption.getSelectedValue().get(0);
		String startDate = this.startDate.getText();
		if(!startDate.equals("")) {
			startDate += " 00:00:00";
		}
		String endDate = this.endDate.getText();
		if(!endDate.equals("")) {
			endDate += " 23:59:59";
		}
		String searchType = super.searchType.getSelectedValue().get(0);
		String keyword = super.keyword.getValue();
		
		submitData.put("dateOption", new JSONString(dateOption));
		submitData.put("startDate", new JSONString(startDate));
		submitData.put("endDate", new JSONString(endDate));
		submitData.put("searchType", new JSONString(searchType));
		submitData.put("keyword", new JSONString(keyword));
		
		submitData.put("offset", new JSONNumber(super.offset = 0));
		submitData.put("limit", new JSONNumber(super.limit));
		
		return submitData;
	}

	@Override
	protected void addTableRow(JSONObject rowData) {
		ContentTableRow row = super.table.addRow(Color.WHITE, new int[]{4}, 
				getString(rowData, "CONTENT_ID"), 
				Registry.getContentType((int) rowData.get("CONTENT_TYPE").isNumber().doubleValue()), 
				getString(rowData, "AREA_NAME"), 
				getString(rowData, "SIGUGUN_NAME"), 
				getString(rowData, "TITLE"), 
				getString(rowData, "MASTER_TAG"), 
				getString(rowData, "CONTENT_STATUS"), 
				getString(rowData, "MODIFIED_DATE"), 
				getString(rowData, "CREATE_DATE"), 
				getString(rowData, "USER_NAME"));
		row.put("RETOBJ", rowData);
		
		addRowStyleAndEvent(row, rowData);
	}
	
	private void addRowStyleAndEvent(ContentTableRow row, JSONObject rowData) {
		MaterialWidget titleColumn = row.getColumnObject(4);
		titleColumn.setTextAlign(TextAlign.LEFT);
		titleColumn.addClickHandler(handler -> {
			
			DatabaseContentsTree cPanel = (DatabaseContentsTree)Registry.put("FoodApiContentsTree");
			cPanel.setTitle(rowData.get("TITLE").isString().stringValue());
			if(super.getMaterialExtentsWindow().getValueMap().containsKey("OTD_ID"))
				cPanel.setOtdId(super.getMaterialExtentsWindow().getValueMap().get("OTD_ID").toString());
			cPanel.setCotId(getString(rowData, "COT_ID"));
			cPanel.setRow(row);
			cPanel.goTree(1);
			cPanel.go(0);
			cPanel.loading();
			cPanel.setBackgroundColor(Color.WHITE);
			
			getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
			
		});
	}

}

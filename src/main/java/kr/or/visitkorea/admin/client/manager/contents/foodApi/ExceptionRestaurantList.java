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
import kr.or.visitkorea.admin.client.manager.contents.foodApi.component.AbstractSearchComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ExceptionRestaurantList extends AbstractSearchComponent {
	
	private MaterialComboBox<String> exceptionReason;
	private MaterialTextBox startDate;
	private MaterialTextBox endDate;
	
	
	
	public ExceptionRestaurantList(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	protected void setFetchCmd() {
		super.fetchCmd = "GET_EXCEPTION_LIST";
	}

	@Override
	protected void initSearchType(MaterialComboBox<String> searchType) {
		String[] searchTypeValues = {"업소명", "인허가번호", "컨텐츠명"};
		Arrays.asList(searchTypeValues).forEach(searchValue -> {
			super.searchType.addItem(searchValue);
		});
	}

	@Override
	protected void initSearchToolsArea(MaterialPanel searchBarArea) {
		String[] exceptionReasons = {"전체", "지정등급", "처분여부", "폐업여부"};
		this.exceptionReason = new MaterialComboBox<String>();
		Arrays.asList(exceptionReasons).forEach(reasonValue -> {
			this.exceptionReason.addItem(reasonValue);
		});
		this.exceptionReason.setWidth("150px");
		this.exceptionReason.setMarginRight(30);
		this.exceptionReason.setLabel("예외사유");
		this.exceptionReason.addValueChangeHandler(handler -> {
			super.fetch(gatherSubmitData());
		});
		searchBarArea.add(this.exceptionReason);
		

		this.startDate = new MaterialTextBox();
		this.startDate.setType(InputType.DATE);
		this.startDate.setWidth("150px");
		this.startDate.setMarginRight(30);
		searchBarArea.add(this.startDate);
		
		
		this.endDate = new MaterialTextBox();
		this.endDate.setType(InputType.DATE);
		this.endDate.setWidth("150px");
		this.endDate.setMarginRight(30);
		searchBarArea.add(this.endDate);
	}
	
	@Override
	protected void initTable(ContentTable table) {
		table.appendTitle("　", 100, TextAlign.CENTER);// 공백
		
		table.appendTitle("갱신일자", 200, TextAlign.CENTER);
		table.appendTitle("인허가번호", 160, TextAlign.CENTER);
		table.appendTitle("업소명", 315, TextAlign.CENTER);
		table.appendTitle("예외처리 사유구분", 175, TextAlign.CENTER);
		table.appendTitle("대구석 컨텐츠명", 400, TextAlign.CENTER);
		
		table.appendTitle("　", 100, TextAlign.CENTER);// 공백
	}

	@Override
	protected JSONObject gatherSubmitData() {
		JSONObject submitData = new JSONObject();
		
		String exceptionReason = this.exceptionReason.getSelectedValue().get(0);
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
		
		submitData.put("exceptionReason", new JSONString(exceptionReason));
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
		ContentTableRow row = super.table.addRow(Color.WHITE, new int[]{}, 
				"　", // 공백
				
				getString(rowData, "CREATE_DATE"), 
				getString(rowData, "LCNS_NO"), 
				getString(rowData, "BSSH_NM"), 
				getString(rowData, "RSN_CD"), 
				getString(rowData, "TITLE"), 
				
				"　");// 공백
		
		addRowStyleAndEvent(row);
	}
	
	private void addRowStyleAndEvent(ContentTableRow row) {
		MaterialWidget titleColumn = row.getColumnObject(5);
		titleColumn.addClickHandler(handler -> {
			
		});
	}

}

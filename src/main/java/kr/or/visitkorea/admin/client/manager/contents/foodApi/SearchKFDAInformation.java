package kr.or.visitkorea.admin.client.manager.contents.foodApi;

import java.util.Arrays;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.contents.foodApi.component.AbstractSearchComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchKFDAInformation extends AbstractSearchComponent {
	
	private MaterialComboBox<String> disposition;// 처분.
	private MaterialComboBox<String> grade;// 지정 등급.
	
	
	
	public SearchKFDAInformation(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	protected void setFetchCmd() {
		super.fetchCmd = "GET_KFDA_DATA_LIST";
	}
	
	@Override
	protected void initSearchType(MaterialComboBox<String> searchType) {
		String[] searchTypeValues = {"업소명", "인허가번호", "대표자명"};
		Arrays.asList(searchTypeValues).forEach(searchValue -> {
			super.searchType.addItem(searchValue);
		});
	}

	@Override
	protected void initSearchToolsArea(MaterialPanel searchBarArea) {
		String[] dispositionValues = {"전체", "처분", "미해당"};
		this.disposition = new MaterialComboBox<String>();
		Arrays.asList(dispositionValues).forEach(dispositionVal -> {
			this.disposition.addItem(dispositionVal);
		});
		this.disposition.setWidth("150px");
		this.disposition.setMarginRight(30);
		this.disposition.setLabel("처분여부");
		this.disposition.addValueChangeHandler(handler -> {
			super.fetch(gatherSubmitData());
		});
		searchBarArea.add(this.disposition);
		
		
		String[] gradeValues = {"전체", "매우우수", "우수", "좋음"};
		this.grade = new MaterialComboBox<String>();
		Arrays.asList(gradeValues).forEach(gradeVal -> {
			this.grade.addItem(gradeVal);
		});
		this.grade.setWidth("150px");
		this.grade.setMarginRight(30);
		this.grade.setLabel("지정등급");
		this.grade.addValueChangeHandler(handler -> {
			super.fetch(gatherSubmitData());
		});
		searchBarArea.add(this.grade);

	}

	@Override
	protected void initTable(ContentTable table) {
		table.appendTitle("인허가번호", 150, TextAlign.CENTER);
		table.appendTitle("대표자명", 110, TextAlign.CENTER);
		table.appendTitle("업소명", 305, TextAlign.CENTER);
		table.appendTitle("주소", 330, TextAlign.CENTER);
		table.appendTitle("전화번호", 150, TextAlign.CENTER);
		table.appendTitle("폐업일자", 165, TextAlign.CENTER);
		table.appendTitle("지정등급", 120, TextAlign.CENTER);
		table.appendTitle("처분여부", 120, TextAlign.CENTER);
	}

	@Override
	protected JSONObject gatherSubmitData() {
		JSONObject submitData = new JSONObject();
		String disposition = this.disposition.getSelectedValue().get(0);
		String grade = this.grade.getSelectedValue().get(0);
		String searchType = super.searchType.getSelectedValue().get(0);
		String keyword = super.keyword.getValue();
		
		submitData.put("disposition", new JSONString(disposition));
		submitData.put("grade", new JSONString(grade));
		submitData.put("searchType", new JSONString(searchType));
		submitData.put("keyword", new JSONString(keyword));
		
		submitData.put("offset", new JSONNumber(super.offset = 0));
		submitData.put("limit", new JSONNumber(super.limit));
		
		return submitData;
	}

	@Override
	protected void addTableRow(JSONObject rowData) {
		ContentTableRow row = super.table.addRow(Color.WHITE, new int[]{}, 
				getString(rowData, "LCNS_NO"), 
				getString(rowData, "PRSDNT_NM"), 
				getString(rowData, "BSSH_NM"), 
				getString(rowData, "LOCP_ADDR"), 
				getString(rowData, "TELNO"), 
				getString(rowData, "CLSBIZ_DT"), 
				getString(rowData, "HG_ASGN_LV"), 
				getString(rowData, "DSPS_YN"));
		
		addRowStyleAndEvent(row);
	}
	
	private void addRowStyleAndEvent(ContentTableRow row) {
		MaterialWidget spotNameColumn = row.getColumnObject(2);
		spotNameColumn.setTextAlign(TextAlign.LEFT);
		
		
		MaterialWidget addressColumn = row.getColumnObject(3);
		addressColumn.setTextAlign(TextAlign.LEFT);
	}

}

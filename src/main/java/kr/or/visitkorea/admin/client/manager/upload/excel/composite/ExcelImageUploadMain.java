package kr.or.visitkorea.admin.client.manager.upload.excel.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.upload.excel.ExcelImageUploadApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMultilineText;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


/**
 * @author Admin
 * 이미지 업로드 Main
 */
public class ExcelImageUploadMain extends AbstractContentPanel implements ExcelImageUploadApplication.OnSearchListener {

	private ContentTable mainTable;
	private ContentTable countTable;
	private ContentMultilineText detailPanel;
	
	private int offset;
	private String eihId;
	private String xmlFilePath;

	public ExcelImageUploadMain(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		Registry.put("ExcelImageUploadMain", this);
		offset = 0;
		setLayoutPosition(Position.RELATIVE);
		buildUploadHistoryList();
		buildUploadHistoryCount();
		buildUploadHistoryDetail();

		fetchHistoryList(offset);
	}
	
	public void buildUploadHistoryList() {
		mainTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		mainTable.setLayoutPosition(Position.ABSOLUTE);
		mainTable.setHeight(575);
		mainTable.setWidth("600px");
		mainTable.setLeft(30);
		mainTable.setTop(55);
		mainTable.appendTitle("상태", 80, TextAlign.LEFT);
		mainTable.appendTitle("실행일자", 170, TextAlign.CENTER);
		mainTable.appendTitle("파일명", 250, TextAlign.LEFT);
		mainTable.appendTitle("전체건수", 80, TextAlign.RIGHT);
		
		MaterialIcon uploadIcon = new MaterialIcon(IconType.CLOUD_UPLOAD); // FILE_UPLOAD
		uploadIcon.setTextAlign(TextAlign.CENTER);
		uploadIcon.addClickHandler(event-> {
			getMaterialExtentsWindow().openDialog(ExcelImageUploadApplication.UPLOAD_EXCEL_DIALOG, 720);
		});

		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event-> {
			getMaterialExtentsWindow().openDialog(ExcelImageUploadApplication.SEARCH_WITH_CALENDAR_DIALOG, 720);
		});
		mainTable.getTopMenu().addIcon(uploadIcon, "업로드", com.google.gwt.dom.client.Style.Float.LEFT, "1.8em", "26px", 24, false);
		mainTable.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event-> {
			offset += 20;
			fetchHistoryList(offset);
		});
		mainTable.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		this.add(mainTable);
	}

	@Override
	public void onSearch(String startDate, String endDate) {
		offset = 0;
		fetchHistoryList(offset, startDate, endDate);
	}
	
	private void buildUploadHistoryCount() {
        MaterialLabel titleLabel = new MaterialLabel("- 건수");
        titleLabel.setLayoutPosition(Position.ABSOLUTE);
        titleLabel.setTop(50);
        titleLabel.setTextAlign(TextAlign.LEFT);
        titleLabel.setFontWeight(Style.FontWeight.BOLD);
        titleLabel.setTextColor(Color.BLUE);
        titleLabel.setFontSize("1.2em");
        titleLabel.setWidth("800px");
        titleLabel.setRight(40);
        this.add(titleLabel);
        
		countTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		countTable.setLayoutPosition(Position.ABSOLUTE);
		countTable.setHeight(240);
		countTable.setWidth("800px");
		countTable.setRight(40);
		countTable.setTop(55);
		countTable.appendTitle("컨텐츠 전체건수", 180, TextAlign.RIGHT);
		countTable.appendTitle("컨텐츠 스킵건수", 180, TextAlign.RIGHT);
		countTable.appendTitle("이미지 저장건수", 180, TextAlign.RIGHT);
		countTable.appendTitle("이미지 실패건수", 180, TextAlign.RIGHT);
		this.add(countTable);
		
		MaterialIcon insertIcon = new MaterialIcon(IconType.SETTINGS_APPLICATIONS);
		insertIcon.setTextAlign(TextAlign.CENTER);
		insertIcon.addClickHandler(event -> {
			Map<String, Object> params  = new HashMap<String, Object>();
			params.put("eihId", eihId);
			params.put("xmlFilePath", xmlFilePath);
			
			getMaterialExtentsWindow().openDialog(ExcelImageUploadApplication.INSERT_DB_DIALOG, params, 720);
		});
		
		this.add(insertIcon);
		countTable.getTopMenu().addIcon(insertIcon, "DB 등록", Style.Float.RIGHT, "1.8em", "26px", 24, false);
	}
	
	private void buildUploadHistoryDetail() {
        MaterialLabel titleLabel = new MaterialLabel("- 로그분석");
        titleLabel.setLayoutPosition(Position.ABSOLUTE);
        titleLabel.setTop(300);
        titleLabel.setRight(40);
        titleLabel.setTextAlign(TextAlign.LEFT);
        titleLabel.setFontWeight(Style.FontWeight.BOLD);
        titleLabel.setTextColor(Color.BLUE);
        titleLabel.setFontSize("1.2em");
        titleLabel.setWidth("800px");
        this.add(titleLabel);

		detailPanel = new ContentMultilineText();
		detailPanel.setLayoutPosition(Position.ABSOLUTE);
        detailPanel.setRight(40);
        detailPanel.setTop(327);
		detailPanel.setWidth("800px");
		detailPanel.setHeight("302px");
		detailPanel.setBorder("solid 1px black");
		detailPanel.setTextAlign(TextAlign.LEFT);
		detailPanel.setOverflow(Overflow.AUTO);
		
		this.add(detailPanel);
	}

	public void fetchHistoryList(int offset) {
		fetchHistoryList(offset, null, null);
	}

	public void fetchHistoryList(int offset, String startDate, String endDate) {
		MaterialLoader.loading(true, getImageUploadMain());
		
		if (offset == 0)
			mainTable.clearRows();
		
		JSONObject params = new JSONObject();
		params.put("cmd", new JSONString("EXCEL_UPLOAD_HIST_LIST"));
		params.put("offset", new JSONNumber(offset));
		
		if (startDate != null)
			params.put("startDate", new JSONString(startDate));
		if (endDate != null)
			params.put("endDate", new JSONString(endDate));
		if (eihId != null)
			params.put("eihId", new JSONString(eihId));
		
		VisitKoreaBusiness.post("call", params.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String result = headerObj.get("process").isString().stringValue();
				
				if ("success".equals(result)) {
					JSONArray list = resultObj.get("body").isObject().get("result").isArray();
					if (list.size() > 0) {
						for (int i = 0, size = list.size(); i < size; i ++) {
							JSONObject item = (JSONObject) list.get(i);
							JSONNumber status = item.get("STATUS").isNumber();
							JSONString executionDate = item.get("UPLOAD_DATE") != null ? item.get("UPLOAD_DATE").isString() : null;
							JSONString fileName = item.get("FILE_NAME") != null ? item.get("FILE_NAME").isString() : null;
							JSONNumber totalRow = item.get("TOTAL_ROW") != null ? item.get("TOTAL_ROW").isNumber() : new JSONNumber(0);
							ContentTableRow row = mainTable.addRow(
									Color.WHITE,
									getStatusText(status),
									getExecutionDate(executionDate),
									getSimpleFileName(fileName),
									getCommaNumber(totalRow)
									);
							
							row.put("EIH_ID", string(item,"EIH_ID"));
							row.put("STATUS", integer(item, "STATUS"));
							row.put("TOTAL_ROW", integer(item, "TOTAL_ROW"));
							row.put("SAVE_ROW", integer(item, "SAVE_ROW"));
							row.put("SKIP_ROW", integer(item, "SKIP_ROW"));
							row.put("FAIL_ROW", integer(item, "FAIL_ROW"));
							row.put("FILE_PATH", string(item,"FILE_PATH"));
							row.put("FILE_NAME", string(item,"FILE_NAME"));
							row.put("CSV_PATH", string(item,"CSV_PATH"));
							
							row.addClickHandler(event -> {
								countTable.clearRows();
								detailPanel.setText("");
								
								ContentTableRow selectedRow = (ContentTableRow) event.getSource();
								eihId = selectedRow.get("EIH_ID").toString();
								xmlFilePath = item.get("CSV_PATH").isString().stringValue();
								
								excelUploadHistoryDetail(eihId, xmlFilePath);
							});
						}
					}
				}
				MaterialLoader.loading(false, getImageUploadMain());
			}
		});
	}

	public void excelUploadHistoryDetail(String eihId, String xmlFilePath) {

		JSONObject params = new JSONObject();
		params.put("cmd", new JSONString("EXCEL_UPLOAD_HIST_DETAIL_LIST"));
		params.put("offset", new JSONNumber(offset));
		params.put("xmlFilePath", new JSONString(xmlFilePath));
		
		if (eihId != null)
			params.put("eihId", new JSONString(eihId));
		
		VisitKoreaBusiness.post("call", params.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String result = headerObj.get("process").isString().stringValue();
				
				if ("success".equals(result)) {
					JSONArray listResult = resultObj.get("body").isObject().get("result").isArray();
					JSONArray listXmlResult = resultObj.get("body").isObject().get("xmlResult").isArray();
					
					if (listXmlResult.size() > 0) {
						String strText = "";
						for (int i = 0; i < listXmlResult.size(); i++) {
							JSONObject xmlItems = (JSONObject) listXmlResult.get(i);
							String xmlContentId = xmlItems.get("contentId") != null ? xmlItems.get("contentId").isString().stringValue() : null;
							String xmlReason = xmlItems.get("reason") != null ? xmlItems.get("reason").isString().stringValue() : null;
							strText += "CONTENT_ID : " + xmlContentId + "&nbsp;&nbsp;&nbsp;&nbsp;(" + xmlReason + ")\r\n";
						}
						detailPanel.setText(strText);
					}
					
					if (listResult.size() > 0) {
						countTable.clearRows();
						
						for (int j = 0; j < listResult.size(); j++) {
							JSONObject resultItem = (JSONObject) listResult.get(j);
							JSONNumber totalCount = resultItem.get("TOTAL_ROW") != null ? resultItem.get("TOTAL_ROW").isNumber() : new JSONNumber(0);
							JSONNumber skipCount = resultItem.get("SKIP_ROW") != null ? resultItem.get("SKIP_ROW").isNumber() : new JSONNumber(0);
							JSONNumber saveCount = resultItem.get("SAVE_ROW") != null ? resultItem.get("SAVE_ROW").isNumber() : new JSONNumber(0);
							JSONNumber failCount = resultItem.get("FAIL_ROW") != null ? resultItem.get("FAIL_ROW").isNumber() : new JSONNumber(0);
							countTable.addRow(Color.WHITE,
									getCommaNumber(totalCount),
									getCommaNumber(skipCount),
									getCommaNumber(saveCount),
									getCommaNumber(failCount)
									);
						}
					}
				}
			}
		});
	}
	
	private ExcelImageUploadMain getImageUploadMain() {
		return this;
	}
	
	private static int integer(JSONObject item, String key) {
		if (item.get(key) == null) 
			return 0;
		
		return (int) item.get(key).isNumber().doubleValue();
	}

	private static String string(JSONObject item, String key) {
		if (item.get(key) == null)
			return "";
		
		return item.get(key).isString().stringValue();
	}

	private static String getCommaNumber(JSONNumber number) {
		int value = (int) number.doubleValue();
		
		return getCommaNumber(value);
	}

	private static String getCommaNumber(int value) {
		NumberFormat fmt = NumberFormat.getDecimalFormat();
		
		return fmt.format(value);
	}

	private static String getExecutionDate(JSONString date) {
		return date == null ? "--" : date.stringValue();
	}

	private static String getSimpleFileName(JSONString complexFileName) {
		if (complexFileName == null) 
			return "--";
		
		return complexFileName.stringValue();
	}

	private static String getStatusText(JSONNumber statusNumber) {
		int status = (int) statusNumber.doubleValue();
		switch (status) {
			case 2: return "업로드완료";
			case 4: return "분석";
			case 8: return "처리완료";
			default: return "상태없음";
		}
	}
}
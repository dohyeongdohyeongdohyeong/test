package kr.or.visitkorea.admin.client.manager.stamp;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class StampMain extends AbstractContentPanel {
	private MaterialPanel mainPanel;
	private MaterialButton xlsBtn;
	private ContentTable table;
	private MaterialComboBox<Object> eventType;
	private MaterialComboBox<Object> ordered;
	private MaterialInput startDate;
	private MaterialInput endDate;
	private int offset;
	private int totalCnt;
	private MaterialLabel countLabel;
	
	public StampMain() {
		super();
	}

	public StampMain(MaterialExtentsWindow meWindow, StampApplication host) {
		super(meWindow);
	}

	@Override
	public void init() {
		this.mainPanel = new MaterialPanel();
		this.mainPanel.setPadding(15);
		this.mainPanel.setWidth("100%");
		this.mainPanel.setHeight("100%");
		this.add(this.mainPanel);

		this.buildForm();
		this.buildTable();
		
		this.xlsBtn = new MaterialButton();
		this.xlsBtn.setText("결과 XLS 다운로드");
		this.xlsBtn.setMarginLeft(16);
		this.xlsBtn.setFloat(Float.LEFT);
		this.xlsBtn.addClickHandler(event -> {
			this.processXlsDownload();
		});
		this.add(this.xlsBtn);
	}

	private void processXlsDownload() {
		String downurl = "./call?cmd=SELECT_STAMP_STATS_XLS";
		StringBuilder sb = new StringBuilder();
		sb.append(downurl)
		  .append("&evntId=" + this.eventType.getSelectedValue().get(0))
		  .append("&offset=" + this.offset)
		  .append("&startDate=" + this.startDate.getValue())
		  .append("&endDate=" + this.endDate.getValue())
		  .append("&ordered=" + (this.ordered.getSelectedIndex() == 0 ? "ASC" : "DESC"));

		Window.open(sb.toString(), "_self", "enabled");
	}
	
	private void buildForm() {
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setMargin(0);
		row.setMarginTop(5);
		this.mainPanel.add(row);
		
		eventType = addComboBox(row, "이벤트 회차", "s2");
		eventType.addItem("1차 이벤트", "3d815f1c-afa0-421a-8d6c-efae95167f65");
		eventType.addItem("2차 이벤트", "6242ad6a-df4d-11e9-b3f0-e0cb4e4f79cd");
		eventType.addItem("3차 이벤트", "3b8786ec-df58-11e9-b3f0-e0cb4e4f79cd");
		eventType.addItem("4차 이벤트", "42aaf2b4-df58-11e9-b3f0-e0cb4e4f79cd");
		eventType.addItem("5차 이벤트", "502fd893-df58-11e9-b3f0-e0cb4e4f79cd");
		
		ordered = addComboBox(row, "날짜별 정렬", "s2");
		ordered.addItem("오름차순");
		ordered.addItem("내림차순");
		
		startDate = addInputDate(row, "시작일", "s2");
		endDate = addInputDate(row, "종료일", "s2");
	}
	
	private void buildTable() {
		this.table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		this.table.setWidth("100%");
		this.table.setHeight(520);
		this.mainPanel.add(table);
		
		this.table.appendTitle("일자", 150, TextAlign.CENTER);
		this.table.appendTitle("발도장 획득수", 300, TextAlign.CENTER);
		this.table.appendTitle("이벤트1 미션 완료수", 250, TextAlign.CENTER);
		this.table.appendTitle("이벤트1 참여 완료수", 250, TextAlign.CENTER);
		this.table.appendTitle("이벤트2 미션 완료수", 250, TextAlign.CENTER);
		this.table.appendTitle("이벤트2 참여 완료수", 250, TextAlign.CENTER);
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.addClickHandler(e -> {
			qryList(true);
		});
		this.table.getTopMenu().addIcon(searchIcon, "검색", Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(e -> {
			if (this.offset + 20 > this.totalCnt)
				return;
			qryList(false);
		});
		this.table.getButtomMenu().addIcon(moreIcon, "20개 더보기", Float.RIGHT);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLD);
		this.table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
	}
	
	private void qryList(boolean bstart) {
		if (bstart) {
			this.offset = 0;
			this.table.clearRows();
		} else offset += 20;
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_STAMP_STATS"));
		paramJson.put("evntId", new JSONString((String) this.eventType.getSelectedValue().get(0)));
		paramJson.put("startDate", new JSONString(this.startDate.getValue()));
		paramJson.put("endDate", new JSONString(this.endDate.getValue()));
		paramJson.put("offset", new JSONNumber(this.offset));
		paramJson.put("ordered", new JSONString(this.ordered.getSelectedIndex() == 0 ? "ASC" : "DESC"));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();
				JSONObject bodyResultCnt = bodyObj.get("resultCnt").isObject();
				JSONObject bodyResultTot = bodyObj.get("resultTot").isObject();
				
				this.totalCnt = (int) bodyResultCnt.get("CNT").isNumber().doubleValue();
				this.countLabel.setText(this.totalCnt + " 건");
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					ContentTableRow tableRow = this.table.addRow(Color.WHITE
						, obj.get("CREATE_DATE").isString().stringValue()
						, obj.get("STP_CNT").isNumber().doubleValue()
						, obj.get("ONE_MISSION_CNT").isNumber().doubleValue()
						, obj.get("ONE_ATTEND_CNT").isNumber().doubleValue()
						, obj.get("TWO_MISSION_CNT").isNumber().doubleValue()
						, obj.get("TWO_ATTEND_CNT").isNumber().doubleValue()
					);
				}

				this.table.setTagVisible(true);
				this.table.addTagRow(Color.GREY_LIGHTEN_2
						, "합계"
						, bodyResultTot.get("STP_SUM").isNumber().doubleValue() 
						, bodyResultTot.get("ONE_MISSION_SUM").isNumber().doubleValue() 
						, bodyResultTot.get("ONE_ATTEND_SUM").isNumber().doubleValue() 
						, bodyResultTot.get("TWO_MISSION_SUM").isNumber().doubleValue() 
						, bodyResultTot.get("TWO_ATTEND_SUM").isNumber().doubleValue() 
				);
			}
		});
	}
	
	private MaterialComboBox<Object> addComboBox(MaterialWidget widget, String text, String grid) {
		MaterialComboBox<Object> combo = new MaterialComboBox<Object>();
		combo.setLabel(text);
		combo.setMargin(0);
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid(grid);
		column.add(combo);
		widget.add(column);
		return combo;
	}
	
	private MaterialInput addInputDate(MaterialWidget widget, String text, String grid) {
		MaterialInput input = new MaterialInput();
		input.setType(InputType.DATE);
		input.setText(text);
		input.setMargin(0);
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid(grid);
		column.add(input);
		widget.add(column);
		return input;
	}
}

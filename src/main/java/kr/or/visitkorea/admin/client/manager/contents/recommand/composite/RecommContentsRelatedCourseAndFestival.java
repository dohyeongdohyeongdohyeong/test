package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
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
import gwt.material.design.incubator.client.alert.Alert;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsRelatedCourseAndFestival extends AbtractRecommContents {

	private ContentTable table;
	private ContentTable table1;
	private MaterialIcon saveIcon;
	private SelectionPanel festivalViewPanel;
	private SelectionPanel courseViewPanel;
	private RecommContentsTree host;
	
	public RecommContentsRelatedCourseAndFestival(MaterialExtentsWindow materialExtentsWindow, RecommContentsTree host) {
		super(materialExtentsWindow);
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("연관컨텐츠 :: 코스 와 축제");
		buildContent(); 
		saveIcon = this.showSaveIconAndGetIcon();
		saveIcon.addClickHandler(event -> {
			saveDB();
		});
	}

	private void saveDB() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_ARTICLE_VIEW"));
		parameterJSON.put("ATM_ID", new JSONString(this.host.getAtmid()));
		parameterJSON.put("COURSE_VIEW", new JSONNumber((int) this.courseViewPanel.getSelectedValue()));
		parameterJSON.put("FESTIVAL_VIEW", new JSONNumber((int) this.festivalViewPanel.getSelectedValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			this.getWindow().alert("저장되었습니다.");
		});
	}
	
	private void buildContent() {
		this.setLayoutPosition(Position.RELATIVE);

		MaterialLabel label = new MaterialLabel();
		label.setLayoutPosition(Position.ABSOLUTE);
		label.setLeft(30);
		label.setTop(60);
		label.setText("- 코스 선택");
		this.add(label);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(190);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setRight(30);
		table.setTop(90);
		table.appendTitle("순서", 50, TextAlign.RIGHT);
		table.appendTitle("지역", 100, TextAlign.CENTER);
		table.appendTitle("지역*(소)", 100, TextAlign.CENTER);
		table.appendTitle("대표태그", 120, TextAlign.CENTER);
		table.appendTitle("코스명", 520, TextAlign.CENTER);
		this.add(table);

		MaterialIcon courseReloadIcon = new MaterialIcon(IconType.REFRESH);
		courseReloadIcon.addClickHandler(event->{
			doCourseDataload(); 
		});
		table.getTopMenu().addIcon(courseReloadIcon, "리로드", Style.Float.RIGHT, "1.8em", false);

		MaterialIcon courseIcon1 = new MaterialIcon(IconType.ADD);
		courseIcon1.addClickHandler(event->{
			doCourseInsert();
		});
		table.getTopMenu().addIcon(courseIcon1, "추가", Style.Float.RIGHT, "1.8em", false);
		
		MaterialIcon courseIcon3 = new MaterialIcon(IconType.DELETE);
		courseIcon3.setTextAlign(TextAlign.CENTER);
		courseIcon3.addClickHandler(event->{
			doCourseSelectedDelete();
		});
		table.getButtomMenu().addIcon(courseIcon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);

		courseViewPanel = addSelectionViewPanel();
		table.getTopMenu().add(courseViewPanel);
		
		MaterialLabel label2 = new MaterialLabel();
		label2.setLayoutPosition(Position.ABSOLUTE);
		label2.setText("- 축제 선택");
		label2.setLeft(30);
		label2.setTop(320);
		this.add(label2);
		
		table1 = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table1.setHeight(190);
		table1.setLayoutPosition(Position.ABSOLUTE);
		table1.setLeft(30);
		table1.setRight(30);
		table1.setTop(350);
		table1.appendTitle("순서", 50, TextAlign.RIGHT);
		table1.appendTitle("지역", 100, TextAlign.CENTER);
		table1.appendTitle("지역(소)", 100, TextAlign.CENTER);
		table1.appendTitle("대표태그", 120, TextAlign.CENTER);
		table1.appendTitle("컨텐츠명", 420, TextAlign.LEFT);
		table1.appendTitle("작성자", 100, TextAlign.CENTER);
		
		this.add(table1);

		MaterialIcon festivalReloadIcon = new MaterialIcon(IconType.REFRESH);
		festivalReloadIcon.addClickHandler(event->{
			doFestivalDataload(); 
		});
		table1.getTopMenu().addIcon(festivalReloadIcon, "리로드", Style.Float.RIGHT, "1.8em", false);

/*		
		MaterialIcon festivalIcon2 = new MaterialIcon(IconType.LINK);
		festivalIcon2.setTextAlign(TextAlign.CENTER);
		festivalIcon2.setLayoutPosition(Position.ABSOLUTE);
		festivalIcon2.setRight(65);
		festivalIcon2.setTop(320);
		festivalIcon2.addClickHandler(event->{
			doFestivalLinkInsert();
		});
		this.add(festivalIcon2);
*/
		MaterialIcon festivalIcon1 = new MaterialIcon(IconType.ADD);
		festivalIcon1.addClickHandler(event->{
			doFestivalInsert();
		});
		table1.getTopMenu().addIcon(festivalIcon1, "추가", Style.Float.RIGHT, "1.8em", false);
		
		MaterialIcon festivalIcon3 = new MaterialIcon(IconType.DELETE);
		festivalIcon3.setTextAlign(TextAlign.CENTER);
		festivalIcon3.addClickHandler(event->{
			doFestivalSelectedDelete();
		});
		
		table1.getButtomMenu().addIcon(festivalIcon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);

		festivalViewPanel = addSelectionViewPanel();
		table1.getTopMenu().add(festivalViewPanel);
	}
	
	public void setViewPanelData(JSONObject obj) {
		int festivalView = (int) obj.get("FESTIVAL_VIEW").isNumber().doubleValue();
		int courseView = (int) obj.get("COURSE_VIEW").isNumber().doubleValue();
		festivalViewPanel.setSelectionOnSingleMode(festivalView == 1 ? "자동" : "수동");
		courseViewPanel.setSelectionOnSingleMode(courseView == 1 ? "자동" : "수동");
	}
	
	public void doCourseDataload() {
		
		table.clearRows();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DB_RELATED_CONTENT_LIST"));
		parameterJSON.put("tbl", new JSONString("RELATED_COURSE"));
		parameterJSON.put("cotId", new JSONString(getCotId()));
		parameterJSON.put("cotId", new JSONString(getCotId()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray resultArray = bodyObj.get("result").isArray();
					
					int usrCnt = resultArray.size();
					for (int i=0; i<usrCnt; i++) {
						
						JSONObject recObj = resultArray.get(i).isObject();

						ContentTableRow tableRow = table.addRow(
								Color.WHITE, (i+1)+"", 
								getString(recObj, "AREA_NAME", "·"),
								getString(recObj, "SIGUGUN_CODE", "·"),
								getString(recObj, "MASTER_TAG", "·"),
								getString(recObj, "TITLE", "·")
						);
						
						tableRow.put("RETOBJ", recObj);
						
					}

				}
			}
		});		
	}
	
	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	public void doFestivalDataload() {
		
		table1.clearRows();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DB_RELATED_CONTENT_LIST"));
		parameterJSON.put("tbl", new JSONString("RELATED_FESTIVAL"));
		parameterJSON.put("cotId", new JSONString(getCotId()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray resultArray = bodyObj.get("result").isArray();
					
					int usrCnt = resultArray.size();
					for (int i=0; i<usrCnt; i++) {
						
						JSONObject recObj = resultArray.get(i).isObject();
						
						ContentTableRow tableRow = table1.addRow(
								Color.WHITE, (i+1)+"", 
								getString(recObj, "AREA_NAME", "·"),
								getString(recObj, "SIGUGUN_CODE", "·"),
								getString(recObj, "MASTER_TAG", "·"),
								getString(recObj, "TITLE", "·"),
								getString(recObj, "AUTHOR", "·")
						);
						
						tableRow.put("RETOBJ", recObj);
						
					}

				}
			}
		});				
	}

	private void doFestivalInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "축제 컨텐츠 선택");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "FESTIVAL");
		diagParam.put("TABLE", table);
		diagParam.put("this", this);
		diagParam.put("CONTENT_TYPE", "15");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doFestivalLinkInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "축제 링크 입력");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "FESTIVAL");
		diagParam.put("TABLE", table);
		diagParam.put("this", this);
		diagParam.put("CONTENT_TYPE", "15");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doCourseInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "코스 컨텐츠 선택");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "COURSE");
		diagParam.put("this", this);
		diagParam.put("TABLE", table1);
		diagParam.put("CONTENT_TYPE", "25");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doCourseLinkInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "코스 링크 입력");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "COURSE");
		diagParam.put("this", this);
		diagParam.put("TABLE", table1);
		diagParam.put("CONTENT_TYPE", "25");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doFestivalSelectedDelete() {
		
		JSONObject retObj = (JSONObject) table1.getSelectedRows().get(0).get("RETOBJ");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DELETE_RELATED_CONTENT"));
		parameterJSON.put("tbl", new JSONString("RELATED_FESTIVAL"));
		parameterJSON.put("cotId", new JSONString(getCotId()));
		parameterJSON.put("subCotId", new JSONString(retObj.get("COT_ID").isString().stringValue()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				ContentTableRow removeTarget = table1.getSelectedRows().get(0);
				int removeTargetIndex = table1.getRowContainer().getWidgetIndex(removeTarget);
				table1.getRowContainer().remove(table1.getSelectedRows().get(0));
				table1.setSelectedIndex(removeTargetIndex);
				
			}
		});
			
	}

	private void doCourseSelectedDelete() {
		
		JSONObject retObj = (JSONObject) table.getSelectedRows().get(0).get("RETOBJ");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DELETE_RELATED_CONTENT"));
		parameterJSON.put("tbl", new JSONString("RELATED_COURSE"));
		parameterJSON.put("cotId", new JSONString(getCotId()));
		parameterJSON.put("subCotId", new JSONString(retObj.get("COT_ID").isString().stringValue()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				ContentTableRow removeTarget = table.getSelectedRows().get(0);
				int removeTargetIndex = table.getRowContainer().getWidgetIndex(removeTarget);
				table.getRowContainer().remove(table.getSelectedRows().get(0));
				table.setSelectedIndex(removeTargetIndex);
			}
		});
		
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public void loadData() {
		doCourseDataload();
		doFestivalDataload();
		loading(false);
		
	}

}
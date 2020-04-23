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
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMenu;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsRelatedRecommandAndSights extends AbtractRecommContents {

	private ContentTable table;
	private ContentMenu pager;
	private ContentTable table1;
	private MaterialIcon saveIcon;
	private SelectionPanel recommViewPanel;
	private SelectionPanel sightViewPanel;
	private RecommContentsTree host;
	
	public RecommContentsRelatedRecommandAndSights(MaterialExtentsWindow materialExtentsWindow, RecommContentsTree host) {
		super(materialExtentsWindow);
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("연관컨텐츠 :: 추천 과 명소");
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
		parameterJSON.put("RECOMM_VIEW", new JSONNumber((int) this.recommViewPanel.getSelectedValue()));
		parameterJSON.put("SIGHT_VIEW", new JSONNumber((int) this.sightViewPanel.getSelectedValue()));
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
		label.setText("- 추천 선택");
		this.add(label);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(190);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setRight(30);
		table.setTop(90);
		table.appendTitle("순서", 50, TextAlign.RIGHT);
		table.appendTitle("지역", 100, TextAlign.CENTER);
		table.appendTitle("시군구", 100, TextAlign.CENTER);
		table.appendTitle("대표태그", 120, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 420, TextAlign.LEFT);
		table.appendTitle("작성자", 100, TextAlign.CENTER);
		this.add(table);
		
		MaterialIcon icon11111 = new MaterialIcon(IconType.REFRESH);
		icon11111.addClickHandler(event->{
			doRecommDataload(); 
		});
		table.getTopMenu().addIcon(icon11111, "리로드", Style.Float.RIGHT, "1.8em", false);

		MaterialIcon icon1 = new MaterialIcon(IconType.ADD);
		icon1.addClickHandler(event->{
			doRecommInsert();
		});
		table.getTopMenu().addIcon(icon1, "추가", Style.Float.RIGHT, "1.8em", false);
		
		MaterialIcon icon3 = new MaterialIcon(IconType.DELETE);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
			doRecommSelectedDelete();
		});
		table.getButtomMenu().addIcon(icon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);

		recommViewPanel = addSelectionViewPanel();
		table.getTopMenu().add(recommViewPanel);
		
		MaterialLabel label2 = new MaterialLabel();
		label2.setLayoutPosition(Position.ABSOLUTE);
		label2.setText("- 명소 선택");
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
		table1.appendTitle("시군구", 100, TextAlign.CENTER);
		table1.appendTitle("대표태그", 120, TextAlign.CENTER);
		table1.appendTitle("컨텐츠명", 420, TextAlign.CENTER);
		table1.appendTitle("작성자", 100, TextAlign.CENTER);
		
		this.add(table1);

		MaterialIcon icon1111 = new MaterialIcon(IconType.REFRESH);
		icon1111.addClickHandler(event->{
			doSightDataload(); 
		});
		table1.getTopMenu().addIcon(icon1111, "리로드", Style.Float.RIGHT, "1.8em", false);

		MaterialIcon icon112 = new MaterialIcon(IconType.ADD);
		icon112.addClickHandler(event->{
			doSightInsert();
		});
		table1.getTopMenu().addIcon(icon112, "추가", Style.Float.RIGHT, "1.8em", false);
		
		MaterialIcon icon31 = new MaterialIcon(IconType.DELETE);
		icon31.setTextAlign(TextAlign.CENTER);
		icon31.addClickHandler(event->{
			doSightSelectedDelete();
		});
		table1.getButtomMenu().addIcon(icon31, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		sightViewPanel = addSelectionViewPanel();
		table1.getTopMenu().add(sightViewPanel);
	}

	public void setViewPanelData(JSONObject obj) {
		int recommView = (int) obj.get("RECOMM_VIEW").isNumber().doubleValue();
		int sightView = (int) obj.get("SIGHT_VIEW").isNumber().doubleValue();
		recommViewPanel.setSelectionOnSingleMode(recommView == 1 ? "자동" : "수동");
		sightViewPanel.setSelectionOnSingleMode(sightView == 1 ? "자동" : "수동");
	}
	
	
	public void doRecommDataload() {
		table.clearRows();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_RELATED_CONTENT_LIST"));
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
						
						System.out.println(recObj.toString());
						
						ContentTableRow tableRow = table.addRow(
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
	
	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	public void doSightDataload() {
		
		table1.clearRows();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DB_RELATED_CONTENT_LIST"));
		parameterJSON.put("tbl", new JSONString("RELATED_SIGHT"));
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
						
						System.out.println(recObj.toString());
						
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

	private void doRecommInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "추천 기사 컨텐츠 선택");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "RECOMMAND");
		diagParam.put("this", this);
		diagParam.put("TABLE", table);
		diagParam.put("CONTENT_TYPE", "(118, 123, 304, 328, 437, 608, 1119,"
				+ " 10100, 10200, 10300, 10400, 10500, 10600, 10700, 10800, 10900, 11000,"
				+ "11100, 11101, 11102, 11103, 11200, 11201, 11202, 11300, 11400, 11500, 11501, 11502, 11600)");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doRecommLinkInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "추천 링크 입력");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "RECOMMAND");
		diagParam.put("this", this);
		diagParam.put("TABLE", table);
		diagParam.put("CONTENT_TYPE", "(118, 123, 304, 328, 437, 608, 1119,"
				+ " 10100, 10200, 10300, 10400, 10500, 10600, 10700, 10800, 10900, 11000,"
				+ "11100, 11101, 11102, 11103, 11200, 11201, 11202, 11300, 11400, 11500, 11501, 11502, 11600)");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doSightInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "명소 컨텐츠 선택");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "SIGHT");
		diagParam.put("this", this);
		diagParam.put("TABLE", table1);
		diagParam.put("CONTENT_TYPE", "(12, 14, 28, 32, 38, 39)");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doSightLinkInsert() {
		Map<String, Object> diagParam = new HashMap<String, Object>();
		diagParam.put("TITLE", "명소 링크 입력");
		diagParam.put("COT_ID", getCotId());
		diagParam.put("TBL", "SIGHT");
		diagParam.put("this", this);
		diagParam.put("TABLE", table1);
		diagParam.put("CONTENT_TYPE", "12");
		getWindow().openDialog(RecommApplication.SELECT_RELATED_CONTENT, diagParam, 640);
	}

	private void doRecommSelectedDelete() {
		
		JSONObject retObj = (JSONObject) table.getSelectedRows().get(0).get("RETOBJ");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DELETE_RELATED_CONTENT"));
		parameterJSON.put("tbl", new JSONString("RELATED_RECOMMAND"));
		parameterJSON.put("cotId", new JSONString(getCotId()));
		parameterJSON.put("subCotId", new JSONString(retObj.get("COT_ID").isString().stringValue()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				ContentTableRow removeTarget = table.getSelectedRows().get(0);
				int removeTargetIndex = table.getRowContainer().getWidgetIndex(removeTarget);
				table.getRowContainer().remove(table.getSelectedRows().get(0));
				if(table.getRowContainer().getChildrenList().size() > 0)
					table.setSelectedIndex(removeTargetIndex);
			}
		});
	}

	private void doSightSelectedDelete() {
		
		JSONObject retObj = (JSONObject) table1.getSelectedRows().get(0).get("RETOBJ");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DELETE_RELATED_CONTENT"));
		parameterJSON.put("tbl", new JSONString("RELATED_SIGHT"));
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

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public void loadData() {
		doRecommDataload();
		doSightDataload();
		loading(false);
	}

}
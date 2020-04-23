package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.curation;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.CustomRowTable;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CurationContentDetail extends AbtractOtherDepartmentMainContents implements ContentDetail{

	private static String otdId;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private MaterialPanel panel;
	private CustomRowTable table;
	private String manId;

	public CurationContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor, String manId) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		this.manId = manId;
		Registry.put("ShowcaseContentDetail", this);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("메인 큐레이션 상세");
	}
	
	private CurationContentDetail getPanel() {
		return this;
	}


	private void buildContent() {

		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		table = new CustomRowTable();
		table.setLeft(30);
		table.setRight(30);
		table.setTop(60);
		table.setBottom(30);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setHeaderVisible(false);
//		table.appendTitle("이미지", 350, TextAlign.CENTER);
//		table.appendTitle("정보", 448, TextAlign.CENTER);
		
		MaterialIcon saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setTextAlign(TextAlign.CENTER);
		saveIcon.addClickHandler(event->{
			
			JSONArray dataArr = table.getData();
			
			JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
			
			String userId = baseinfo.get("USR_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_SM_CURATION"));
			parameterJSON.put("MAN_ID", new JSONString(manId));
			parameterJSON.put("CONTENT", dataArr);
			parameterJSON.put("USR_ID", new JSONString(userId));
			executeBusiness(parameterJSON);
		});
		
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setTextAlign(TextAlign.CENTER);
		addIcon.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("OTD_ID", otdId);
			paramMap.put("PARENT", getPanel());
			paramMap.put("MAN_ID", manId);
			getWindow().openDialog(OtherDepartmentMainApplication.SELECT_MASTER_CONTENT, paramMap, 800);
			
		});
		
		MaterialIcon linkIcon = new MaterialIcon(IconType.CLOUD);
		linkIcon.setTextAlign(TextAlign.CENTER);
		linkIcon.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("OTD_ID", otdId);
			paramMap.put("PARENT", getPanel());
			paramMap.put("MAN_ID", manId);
			getWindow().openDialog(OtherDepartmentMainApplication.MASTER_EXTERNAL_LINK_NO_IMAGE, paramMap, 600);
			
		});
		
		MaterialIcon deleteIcon = new MaterialIcon(IconType.DELETE);
		deleteIcon.setTextAlign(TextAlign.CENTER);
		deleteIcon.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("MAN_ID", manId);
			paramMap.put("CONTENT_INFO", "컨텐츠를 삭제하면 되돌릴 수 없습니다.");
			paramMap.put("TABLE", table);
			getWindow().openDialog(OtherDepartmentMainApplication.DELETE_CONTENT_INFO, paramMap, 800);
			
		});
		
		MaterialIcon webIcon = new MaterialIcon(IconType.WEB);
		webIcon.setTextAlign(TextAlign.CENTER);
		webIcon.addClickHandler(event->{
			
		});
		
		MaterialIcon editIcon = new MaterialIcon(IconType.EDIT);
		editIcon.setTextAlign(TextAlign.CENTER);
		editIcon.addClickHandler(event->{
			
		});
		
		MaterialIcon upIcon = new MaterialIcon(IconType.ARROW_UPWARD);
		upIcon.setTextAlign(TextAlign.CENTER);
		upIcon.addClickHandler(event->{
			table.reOrder(table.getSelectedPanel(), -1);
		});
		
		MaterialIcon downIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		downIcon.setTextAlign(TextAlign.CENTER);
		downIcon.addClickHandler(event->{
			table.reOrder(table.getSelectedPanel(), 1);
		});
		
		table.addTopMenu(addIcon, "추가", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addTopMenu(linkIcon, "링크 선택", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addTopMenu(saveIcon, "서버 반영", com.google.gwt.dom.client.Style.Float.LEFT);
		table.addBottomMenu(deleteIcon, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
//		table.addBottomMenu(editIcon, "편집", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addBottomMenu(webIcon, "미리 보기", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addBottomMenu(downIcon, "아래로", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addBottomMenu(upIcon, "위로", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			
			saveIcon.setEnabled(Registry.getPermission("90529eab-937c-4f39-9cd2-73b5577ebca5"));
			linkIcon.setEnabled(Registry.getPermission("e6d41ef7-c1b7-405c-810f-c8f24ef1d384"));
			addIcon.setEnabled(Registry.getPermission("7714bb3c-a4fc-481d-ba43-f80310896916"));
			deleteIcon.setEnabled(Registry.getPermission("d1195af7-7048-4291-8c0a-3120dcb37310"));
			upIcon.setEnabled(Registry.getPermission("3a82f682-dc06-4d2e-a09f-b4699fca3793"));
			downIcon.setEnabled(Registry.getPermission("3a82f682-dc06-4d2e-a09f-b4699fca3793"));
			webIcon.setEnabled(Registry.getPermission("250e3fee-06ab-41a7-a956-5b52f6afbe74"));
			editIcon.setEnabled(Registry.getPermission("0d8a130e-eaf2-40c3-ab38-ec97d47247b2"));
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			
			saveIcon.setEnabled(Registry.getPermission("217e6d9b-591f-48e9-8d3b-79a3d7442bba"));
			linkIcon.setEnabled(Registry.getPermission("9f572ccc-5a6e-429f-afbc-ebca78a4495b"));
			addIcon.setEnabled(Registry.getPermission("82b9deee-54ee-4a8d-a066-d4c9652dfbfd"));
			deleteIcon.setEnabled(Registry.getPermission("d09971c9-dde6-4646-b62d-d2302d321314"));
			upIcon.setEnabled(Registry.getPermission("4ac85219-587d-4fe4-b976-eed39c4886be"));
			downIcon.setEnabled(Registry.getPermission("4ac85219-587d-4fe4-b976-eed39c4886be"));
			webIcon.setEnabled(Registry.getPermission("15a5b715-e0cb-42a8-b342-c0a45afcbe19"));
			editIcon.setEnabled(Registry.getPermission("b0d0de74-b81e-484c-8547-8ff279785374"));
		}
		
		addIcon.setEnabled(true);
		linkIcon.setEnabled(true);
		saveIcon.setEnabled(true);
		deleteIcon.setEnabled(true);
		webIcon.setEnabled(true);
		downIcon.setEnabled(true);
		upIcon.setEnabled(true);
		
		
		
		this.add(table);
		
	}
	
	public void addRow() {
		table.addCheckBoxRow(getCustomRow());
	}
	
	public void addRow(ContentRow masterContentRow) {
		table.addCheckBoxRow(masterContentRow);
	}
	
	public MasterCurationContentRow getCustomRow() {
		return new MasterCurationContentRow();
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}
	
	public void setOtdId(String OTDID) {
		otdId = OTDID;
	}

	public void load(String mainId) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_MAIN_CURATION"));
		parameterJSON.put("MAN_ID", new JSONString(mainId));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");

				if (processResult.equals("success")) {

					
					JSONArray resultArray = resultObj.get("body").isObject().get("result").isArray();
					int itemCount = resultArray.size();
					
					for (int i =0; i<itemCount; i++) {
						
						JSONObject tempObj = resultArray.get(i).isObject();
						
						String MAN_ID = getStringProperty(tempObj, "MAN_ID");
						String COT_ID = getStringProperty(tempObj, "COT_ID");
						String CREATE_DATE = getStringProperty(tempObj, "CREATE_DATE");
						String CREATE_USR_ID = getStringProperty(tempObj, "CREATE_USR_ID");
						double CONTENT_ORDER = getNumberProperty(tempObj, "CONTENT_ORDER");
						String DISPLAY_TITLE = getStringProperty(tempObj, "DISPLAY_TITLE");
						double DISPLAY_HEADER_YN = getNumberProperty(tempObj, "DISPLAY_HEADER_YN");
						String DISPLAY_HEADER_COLOR = getStringProperty(tempObj, "DISPLAY_HEADER_COLOR");
						String DISPLAY_HEADER_TITLE = getStringProperty(tempObj, "DISPLAY_HEADER_TITLE");
						String LINK_URL = getStringProperty(tempObj, "LINK_URL");
						String IMG_ID = getStringProperty(tempObj, "IMG_ID");
						
						ContentRow mscr = new MasterCurationContentRow();
						
						JSONObject recordObj = new JSONObject();
						
						recordObj.put("MAN_ID", new JSONString(MAN_ID));
						recordObj.put("COT_ID", new JSONString(COT_ID));
						recordObj.put("CREATE_DATE", new JSONString(CREATE_DATE));
						recordObj.put("CREATE_USR_ID", new JSONString(CREATE_USR_ID));
						recordObj.put("CONTENT_ORDER", new JSONNumber(CONTENT_ORDER));
						recordObj.put("DISPLAY_TITLE", new JSONString(DISPLAY_TITLE));
						recordObj.put("DISPLAY_HEADER_YN", new JSONNumber(DISPLAY_HEADER_YN));
						recordObj.put("DISPLAY_HEADER_COLOR", new JSONString(DISPLAY_HEADER_COLOR));
						recordObj.put("DISPLAY_HEADER_TITLE", new JSONString(DISPLAY_HEADER_TITLE));
						recordObj.put("LINK_URL", new JSONString(LINK_URL));
						recordObj.put("IMG_ID", new JSONString(IMG_ID));
						
						mscr.buildComponent(recordObj);
						
						table.addCheckBoxRowRED(mscr);
						
					}
				}
			}
		});
		
	}

	@Override
	public void setRecIndex(int i) {
		
	}

	public void setManId(String manId) {
		if (manId != null) {
			this.manId = manId;
			buildContent();
			load(manId);
		}
	}

	@Override
	public void loadData() {
		
	}


}
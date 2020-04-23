package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.showcase;

import java.util.HashMap;
import java.util.Map;

import javax.xml.registry.infomodel.User;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
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
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.CustomRowTable;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.MasterContentRowCheckBox;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ShowcaseContentDetail extends AbtractOtherDepartmentMainContents implements ContentDetail{
 
	private static String otdId;
	private MaterialPanel panel;
	private CustomRowTable table;
	private String manId;

	public ShowcaseContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor, String manId) {
		super(materialExtentsWindow);
		this.manId = manId;
		Registry.put("ShowcaseContentDetail", this);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("메인 쇼케이스 상세");
	}
	
	private ShowcaseContentDetail getPanel() {
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
		
		MaterialIcon saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setTextAlign(TextAlign.CENTER);
		saveIcon.addClickHandler(event->{
			
			JSONArray dataArr = table.getData();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_SM_SHOWCASE"));
			parameterJSON.put("MAN_ID", new JSONString(manId));
			parameterJSON.put("CONTENT", dataArr);
			executeBusiness(parameterJSON);		
			
		});
		
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setTextAlign(TextAlign.CENTER);
		addIcon.addClickHandler(event->{

			if(table.getwList().size() > 6) {
				window.alert("메인 쇼케이스는 6개까지만 등록 가능합니다");
				return;
			}
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
			paramMap.put("MAN_ID", manId);
			paramMap.put("PARENT", getPanel());
			getWindow().openDialog(OtherDepartmentMainApplication.MASTER_EXTERNAL_LINK, paramMap, 800);
			
		});
		
		MaterialIcon deleteIcon = new MaterialIcon(IconType.DELETE);
		deleteIcon.setTextAlign(TextAlign.CENTER);
		deleteIcon.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("CONTENT_INFO", "컨텐츠를 삭제하면 되돌릴 수 없습니다.");
			paramMap.put("TABLE", table);
			paramMap.put("MAN_ID", manId);
			getWindow().openDialog(OtherDepartmentMainApplication.DELETE_CONTENT_INFO, paramMap, 800);
			
		});
		
		MaterialIcon webIcon = new MaterialIcon(IconType.WEB);
		webIcon.setTextAlign(TextAlign.CENTER);
		webIcon.addClickHandler(event->{
			MasterContentRowCheckBox item = (MasterContentRowCheckBox)table.getSelectedPanel();
			MasterShowcaseContentRow itemContent = (MasterShowcaseContentRow) item.getContentRow();
			if (item != null && itemContent != null && itemContent.getCotId() != null) {
				Registry.openPreview(webIcon, (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid=" + itemContent.getCotId());
			}
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
		table.addBottomMenu(webIcon, "미리 보기", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addBottomMenu(downIcon, "아래로", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addBottomMenu(upIcon, "위로", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			
			saveIcon.setEnabled(Registry.getPermission("2ba3af57-33ad-4bd0-bc4b-4461b75a04a8"));
			linkIcon.setEnabled(Registry.getPermission("60c3e1ca-851d-44d5-ae5b-8d5513089d32"));
			addIcon.setEnabled(Registry.getPermission("7123cc4b-85f2-4d74-8056-cb50defeafbb"));
			deleteIcon.setEnabled(Registry.getPermission("5220574e-a1d6-4e9d-9891-0721568a2c86"));
			upIcon.setEnabled(Registry.getPermission("506df1b7-def9-444e-b960-128c0cac3727"));
			downIcon.setEnabled(Registry.getPermission("506df1b7-def9-444e-b960-128c0cac3727"));
			webIcon.setEnabled(Registry.getPermission("40764ecd-cb37-488c-b255-988f2ada2ef2"));
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			
			saveIcon.setEnabled(Registry.getPermission("3952ea18-3f07-4f95-b583-14a05a73cbc9"));
			linkIcon.setEnabled(Registry.getPermission("ec24833f-415d-4218-8e37-46dc5b1feb8b"));
			addIcon.setEnabled(Registry.getPermission("18f785b4-2457-4825-8eda-12907dd04e29"));
			deleteIcon.setEnabled(Registry.getPermission("6e2f30c2-71cd-46e9-a270-43f395f75141"));
			upIcon.setEnabled(Registry.getPermission("f373e461-efff-4e52-a4e4-2681193fdfc7"));
			downIcon.setEnabled(Registry.getPermission("f373e461-efff-4e52-a4e4-2681193fdfc7"));
			webIcon.setEnabled(Registry.getPermission("7025f528-68c1-4858-be61-ab1849b40697"));
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
	
	public void addRow(ContentRow contentRow) {
		table.addCheckBoxRow(contentRow);
	}

	protected void addServiceRow(ContentRow contentRow) {
		table.addCheckBoxRowRED(contentRow);
	}

	public ContentRow getCustomRow() {
		return new MasterShowcaseContentRow();
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}
	
	public void setOtdId(String OTDID) {
		otdId = OTDID;
	}

	@Override
	public void loadData() {}
	
	public void load(String mainId) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_MAIN_SHOWCASE"));
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

						String TITLE = getStringProperty(tempObj, "CONTENT_TITLE");
						String IMG_ID = getStringProperty(tempObj, "IMG_ID");
						String LINK_URL = getStringProperty(tempObj, "LINK_URL");
						String START_DATE = getStringProperty(tempObj, "DISPAY_START_DATE");
						String END_DATE = getStringProperty(tempObj, "DISPLAY_END_DATE");
						String DESCRIPTION = getStringProperty(tempObj, "CONTENT_DESCRIPTION");
						String COURSE_DESCRIPTION = getStringProperty(tempObj, "COURSE_DESCRIPTION");
						String COT_ID = getStringProperty(tempObj, "COT_ID");
						
						ContentRow mscr = new MasterShowcaseContentRow();

						JSONObject recordObj = new JSONObject();
						recordObj.put("TITLE", new JSONString(TITLE));
						recordObj.put("IMG_ID", new JSONString(IMG_ID));
						recordObj.put("LINK_URL", new JSONString(LINK_URL));
						recordObj.put("MAIN_TAG", new JSONString(""));
						recordObj.put("START_DATE", new JSONString(START_DATE));
						recordObj.put("END_DATE", new JSONString(END_DATE));
						recordObj.put("DESCRIPTION", new JSONString(DESCRIPTION));
						recordObj.put("COT_ID", new JSONString(COT_ID));
						recordObj.put("COURSE_DESCRIPTION", new JSONString(COURSE_DESCRIPTION));

						mscr.buildComponent(recordObj);
						
						addServiceRow(mscr);
						
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

}
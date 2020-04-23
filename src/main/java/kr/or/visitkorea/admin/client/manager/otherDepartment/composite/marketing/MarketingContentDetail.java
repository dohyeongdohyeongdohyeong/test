package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
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
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MarketingContentDetail extends AbtractOtherDepartmentMainContents implements ContentDetail{

	private static String otdId;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private MaterialPanel panel;
	private CustomRowTable table;
	private int recordIndex = 0;
	private ContentRow selectedItem;
	private MaterialLabel detailTitle;
	private MarketingDetailPanel imagePanel1;
	private MarketingDetailPanel imagePanel2;
	private MarketingDetailPanel imagePanel3;
	private MarketingDetailPanel imagePanel4;
	private String manId;

	public MarketingContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		Registry.put("MarketingContentDetail", this);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("메인 마케팅 상세");
	}
	
	private MarketingContentDetail getPanel() {
		return this;
	}

	public void setRecIndex(int idx) {
		this.recordIndex = idx;
	}
	

	private void buildContent() {

		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		table = new CustomRowTable();
		table.setLeft(30);
		table.setWidth("350px");
		table.setTop(60);
		table.setBottom(30);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setHeaderVisible(false);
		
		MaterialIcon saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setTextAlign(TextAlign.CENTER);
		saveIcon.addClickHandler(event->{

			if (table.validateAll()) {
				
				JSONArray dataArr = table.getData();
				
				JSONObject baseinfo = (JSONObject) Registry.get(Registry.BASE_INFORMATION);
				String userId = baseinfo.get("USR_ID").isString().stringValue();
	
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("INSERT_SM_MARKETING"));
				parameterJSON.put("CONTENT", dataArr);
				parameterJSON.put("MAN_ID", new JSONString(manId));
				parameterJSON.put("USR_ID", new JSONString(userId));
				executeBusiness(parameterJSON);		
				
			}else {
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("CONTENT_INFO", "아직 완성 되지 않은 컨텐츠가 존재합니다. 서비스할 수 있도록 완성해 주십시오~!");
				getWindow().openDialog(OtherDepartmentMainApplication.VALIDATION_RESULT, paramMap, 800);
				
			}
		
		});
		
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setTextAlign(TextAlign.CENTER);
		addIcon.addClickHandler(event->{
			addRow();
		});
		
		MaterialIcon deleteIcon = new MaterialIcon(IconType.DELETE);
		deleteIcon.setTextAlign(TextAlign.CENTER);
		deleteIcon.addClickHandler(event->{
			table.deleteRow();
		});
	
		table.addTopMenu(addIcon, "추가", com.google.gwt.dom.client.Style.Float.RIGHT);
		table.addTopMenu(saveIcon, "서버 반영", com.google.gwt.dom.client.Style.Float.LEFT);
		table.addBottomMenu(deleteIcon, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			
			saveIcon.setEnabled(Registry.getPermission("4cd26f34-db5c-4128-896b-91ec74c605cb"));
			addIcon.setEnabled(Registry.getPermission("77afd9f5-1cab-4999-b712-101422ccfd80"));
			deleteIcon.setEnabled(Registry.getPermission("a46c36a6-e06d-45ca-a710-1c4dee4d11fb"));
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			
			saveIcon.setEnabled(Registry.getPermission("a0220e40-53ac-4eca-8b5c-c1bc9acfe981"));
			addIcon.setEnabled(Registry.getPermission("155dc57b-9cac-4653-a677-aa6e94a7222c"));
			deleteIcon.setEnabled(Registry.getPermission("5fff6072-c8e9-4cd6-92b8-38655737b3d9"));
		}
		
		addIcon.setEnabled(true);
		saveIcon.setEnabled(true);
		deleteIcon.setEnabled(true);
		
		this.add(table);
		
		detailTitle = new MaterialLabel("마케팅 인덱스 :: 0");
		detailTitle.setLayoutPosition(Position.ABSOLUTE);
		detailTitle.setFontSize("1.5em");
		detailTitle.setFontWeight(FontWeight.BOLD);
		detailTitle.setLeft(520);
		detailTitle.setWidth("200px");
		detailTitle.setTop(64);
		this.add(detailTitle);
		
		MaterialPanel detailPanel = new MaterialPanel();
		detailPanel.setLeft(400);
		detailPanel.setRight(30);
		detailPanel.setTop(100);
		detailPanel.setBottom(26);
		detailPanel.setLayoutPosition(Position.ABSOLUTE);
		this.add(detailPanel);
		
		int widthValue = 223;
		int heightValue = 220;
		
		String defaultImageId = Registry.getDefaultImageId();
		
		imagePanel1 = new MarketingDetailPanel( widthValue, heightValue, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+defaultImageId+"&chk="+IDUtil.uuid(16));
		imagePanel1.setLayoutPosition(Position.ABSOLUTE);
		imagePanel1.setLeft(0);
		imagePanel1.setTop(0);
		imagePanel1.setExWindow(this.getWindow()); 
		detailPanel.add(imagePanel1);
		
		imagePanel2 = new MarketingDetailPanel( widthValue, heightValue, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+defaultImageId+"&chk="+IDUtil.uuid(16));
		imagePanel2.setLayoutPosition(Position.ABSOLUTE);
		imagePanel2.setLeft(224);
		imagePanel2.setTop(0);
		imagePanel2.setExWindow(this.getWindow());
		detailPanel.add(imagePanel2);
		
		imagePanel3 = new MarketingDetailPanel( widthValue, heightValue, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+defaultImageId+"&chk="+IDUtil.uuid(16));
		imagePanel3.setLayoutPosition(Position.ABSOLUTE);
		imagePanel3.setLeft(0);
		imagePanel3.setTop(221);
		imagePanel3.setExWindow(this.getWindow());
		detailPanel.add(imagePanel3);
	
		imagePanel4 = new MarketingDetailPanel( widthValue, heightValue, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+defaultImageId+"&chk="+IDUtil.uuid(16));
		imagePanel4.setLayoutPosition(Position.ABSOLUTE);
		imagePanel4.setLeft(224);
		imagePanel4.setTop(221);
		imagePanel4.setExWindow(this.getWindow());
		detailPanel.add(imagePanel4);

		
		
	}
	
	public void addRow() {
		MasterMarketingContentRow row = getCustomRow();
		row.setRowTitle(this.recordIndex + "");
		addEvent(row);
		table.addCheckBoxRow(row);
		this.recordIndex++;
	}
	
	public void addRow(ContentRow masterContentRow) {
		addEvent(masterContentRow);
		table.addCheckBoxRow(masterContentRow);
	}
	
	
	
	private void addEvent(ContentRow masterContentRow) {
		MaterialPanel mp = (MaterialPanel)masterContentRow;
		mp.addClickHandler(event->{
			
			if (this.selectedItem != null) this.selectedItem.setSelected(false);
			
			this.selectedItem = masterContentRow;
			this.selectedItem.setSelected(true);
			String rowIndex = (String)this.selectedItem.getIdenifyedvalue();
			
			Map<String, Object> items = masterContentRow.getItems();
			
			// setup imagePanel
			imagePanel1.setValue((Map<String, Object>)items.get("itemIndex0"));
			imagePanel2.setValue((Map<String, Object>)items.get("itemIndex1"));
			imagePanel3.setValue((Map<String, Object>)items.get("itemIndex2"));
			imagePanel4.setValue((Map<String, Object>)items.get("itemIndex3"));
			
		});
		
	}

	public MasterMarketingContentRow getCustomRow() {
		return new MasterMarketingContentRow();
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
		parameterJSON.put("cmd", new JSONString("GET_MAIN_MARKETING"));
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
					
					Map<String, JSONArray> itemMap = new HashMap<String, JSONArray>();
					
					for (int i =0; i<itemCount; i++) {
						
						JSONObject tempObj = resultArray.get(i).isObject();

						String MAN_ID = getStringProperty(tempObj, "MAN_ID");
						String COT_ID = getStringProperty(tempObj, "COT_ID");
						double CONTENT_ORDER = getNumberProperty(tempObj, "CONTENT_ORDER");
						String TITLE = getStringProperty(tempObj, "TITLE");
						String LINK_URL = getStringProperty(tempObj, "LINK_URL");
						String IMG_ID = getStringProperty(tempObj, "IMG_ID");
						String CREATE_USR_ID = getStringProperty(tempObj, "CREATE_USR_ID");
						String CREATE_DATE = getStringProperty(tempObj, "CREATE_DATE");
						String CGP_ID = getStringProperty(tempObj, "CGP_ID");

						JSONArray jArray = itemMap.get(CGP_ID);
						if (jArray == null) {
							jArray = new JSONArray();
							itemMap.put(CGP_ID, jArray);
						}
						
						JSONObject recordObj = new JSONObject();
					
						recordObj.put("valueChange", new JSONString("false"));
						recordObj.put("COT_ID", new JSONString(COT_ID));
						recordObj.put("title", new JSONString(TITLE));
						recordObj.put("url", new JSONString(LINK_URL.trim()));
						recordObj.put("imageId", new JSONString(IMG_ID));
						recordObj.put("CGP_ID", new JSONString(CGP_ID));
						
						jArray.set(jArray.size(), recordObj);
						
					}
					
					List<String> itemKeyList = new ArrayList<String>(itemMap.keySet());
					for (String itemKey : itemKeyList) {
						JSONArray itemArray = itemMap.get(itemKey);
						MasterMarketingContentRow mscr = new MasterMarketingContentRow();
						mscr.setRowTitle(recordIndex + "");
						mscr.buildComponent(itemArray);
						addEvent(mscr);
						table.addCheckBoxRowRED(mscr);
						recordIndex++;
					}
					
				}
			}
		});
		
	}

	public void setManId(String manId) {
		if (manId != null) {
			this.manId = manId;
			buildContent();
			load(manId);
		}
	}

	public String getManId() {
		return manId;
	}
}
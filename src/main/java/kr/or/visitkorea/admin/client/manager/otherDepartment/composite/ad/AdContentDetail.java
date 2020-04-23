package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AdContentDetail extends AbtractOtherDepartmentMainContents {

	private MaterialPanel panel;
	private MaterialLabel cotLabel;
	private MaterialLabel cidLabel;
	private MaterialTextBox sectionTitle;
	private SelectionPanel selectStatus;
	private SelectionPanel selectStatus1;
	private MaterialTextBox tag1;
	private MaterialTextBox tag2;
	private MaterialTextBox tag3;
	private ContentTable table;
	private AreaComponent areaCompo;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private String COMP_ID;
	private MaterialIcon courseIcon5;

	public AdContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		Registry.put("ShowcaseContentDetail", this);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("광고 영역");
		buildContent();
	}

	private void buildContent() {

		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		//첫 줄
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "섹션명", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		this.sectionTitle = addInputText(row1, "", "s7");
		this.sectionTitle.addValueChangeHandler(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_TITLE"));
			jObj.put("TITLE", new JSONString(this.sectionTitle.getText()));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			executeBusiness(jObj);
		
			this.areaCompo.getInfo().get(0).isObject().put("TITLE", new JSONString(this.sectionTitle.getText()));

		});
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("노출", 1);
		map.put("비노출", 0);
		this.selectStatus1 = addSelectionPanel(row1, "s2", TextAlign.CENTER, map);
		this.selectStatus1.setSelectionOnSingleMode("비노출");
		this.selectStatus1.addStatusChangeEvent(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_TITLE_VIEW"));
			jObj.put("MODE", new JSONNumber((int) selectStatus1.getSelectedValue()));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			List<JSONObject> infoList = this.areaCompo.getInfo();
			for (JSONObject areaCompoJObj : infoList) {
				areaCompoJObj.put("VIEW_TITLE", new JSONNumber((int) selectStatus1.getSelectedValue()));
			}

			executeBusiness(jObj);
			
		});
			
		//두번째 줄
		MaterialRow row2 = addRow(panel);
		addLabel(row1, "더보기 연결 태그", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		this.tag1 = addInputText(row1, "", "s2");
		this.tag2 = addInputText(row1, "", "s2");
		this.tag3 = addInputText(row1, "", "s3");
		
		this.tag1.addValueChangeHandler(event->{
			
			String connMoreValue = tag1.getValue().trim() + "|" + tag2.getValue().trim() + "|" + tag3.getValue().trim();
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE"));
			jObj.put("CONNECT_MORE", new JSONString(connMoreValue));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			executeBusiness(jObj);
		
			List<JSONObject> infoList = this.areaCompo.getInfo();
			for (JSONObject areaCompoJObj : infoList) {
				areaCompoJObj.put("CONNECT_MORE", new JSONString(connMoreValue));
			}

		});
		
		this.tag2.addValueChangeHandler(event->{
			
			String connMoreValue = tag1.getValue().trim() + "|" + tag2.getValue().trim() + "|" + tag3.getValue().trim();

			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE"));
			jObj.put("CONNECT_MORE", new JSONString(connMoreValue));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			executeBusiness(jObj);
		
			List<JSONObject> infoList = this.areaCompo.getInfo();
			for (JSONObject areaCompoJObj : infoList) {
				areaCompoJObj.put("CONNECT_MORE", new JSONString(connMoreValue));
			}

		});
		
		this.tag3.addValueChangeHandler(event->{
			
			String connMoreValue = tag1.getValue().trim() + "|" + tag2.getValue().trim() + "|" + tag3.getValue().trim();

			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE"));
			jObj.put("CONNECT_MORE", new JSONString(connMoreValue));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			executeBusiness(jObj);
		
			List<JSONObject> infoList = this.areaCompo.getInfo();
			for (JSONObject areaCompoJObj : infoList) {
				areaCompoJObj.put("CONNECT_MORE", new JSONString(connMoreValue));
			}

		});
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("노출", "1");
		map1.put("비노출", "0");
		this.selectStatus = addSelectionPanel(row1, "s2", TextAlign.CENTER, map1);
		
		this.selectStatus.addStatusChangeEvent(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE_VIEW"));
			jObj.put("MODE", new JSONString((String) selectStatus.getSelectedValue()));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			
			List<JSONObject> infoList = this.areaCompo.getInfo();
			for (JSONObject areaCompoJObj : infoList) {
				areaCompoJObj.put("VIEW_CONNECT_MORE", new JSONString((String) selectStatus.getSelectedValue()));
			}

			executeBusiness(jObj);
		});

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setRight(30);
		table.setTop(170);
		table.setTopMenuVisible(false);
		table.setHeight(370);
		
		table.appendTitle("노출 순서", 100, TextAlign.RIGHT);
		table.appendTitle("TYPE / COT_ID", 210, TextAlign.CENTER);
		table.appendTitle("제목", 338, TextAlign.CENTER);
		table.appendTitle("등록일자", 150, TextAlign.CENTER);
		
		this.add(table);

		MaterialIcon reloadIcon = new MaterialIcon(IconType.REFRESH);
		reloadIcon.setTextAlign(TextAlign.CENTER);
		reloadIcon.setLayoutPosition(Position.ABSOLUTE);
		reloadIcon.setRight(30);
		reloadIcon.setTop(170);
		reloadIcon.addClickHandler(event->{
			doDataload(); 
		});
		this.add(reloadIcon);

		MaterialIcon courseReloadIcon = new MaterialIcon(IconType.ADD);
		courseReloadIcon.setTextAlign(TextAlign.CENTER);
		courseReloadIcon.setLayoutPosition(Position.ABSOLUTE);
		courseReloadIcon.setRight(60);
		courseReloadIcon.setTop(170);
		courseReloadIcon.addClickHandler(event->{
			doDataAdd(); 
		});
		this.add(courseReloadIcon);

		MaterialIcon fileUploadIcon = new MaterialIcon(IconType.FILE_UPLOAD);
		fileUploadIcon.setTextAlign(TextAlign.CENTER);
		fileUploadIcon.setLayoutPosition(Position.ABSOLUTE);
		fileUploadIcon.setRight(90);
		fileUploadIcon.setTop(170);
		fileUploadIcon.addClickHandler(event->{
			doFileUpload(); 
		});
		this.add(fileUploadIcon);

		MaterialIcon urlIcon = new MaterialIcon(IconType.CLOUD);
		urlIcon.setTextAlign(TextAlign.CENTER);
		urlIcon.setLayoutPosition(Position.ABSOLUTE);
		urlIcon.setRight(120);
		urlIcon.setTop(170);
		urlIcon.addClickHandler(event->{
			doUrlDefine(); 
		});
		this.add(urlIcon);
		
		MaterialIcon courseIcon3 = new MaterialIcon(IconType.DELETE);
		courseIcon3.setTextAlign(TextAlign.CENTER);
		courseIcon3.addClickHandler(event->{
			doSelectedDelete();
		});
		table.getButtomMenu().addIcon(courseIcon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialIcon courseIcon4 = new MaterialIcon(IconType.IMAGE);
		courseIcon4.setTextAlign(TextAlign.CENTER);
		courseIcon4.addClickHandler(event->{
			doImageProcess();
		});
		table.getButtomMenu().addIcon(courseIcon4, "이미지 처리", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		courseIcon5 = new MaterialIcon(IconType.WEB);
		courseIcon5.setTextAlign(TextAlign.CENTER);
		courseIcon5.addClickHandler(event->{
			if (table.getSelectedRows().size() > 0) {
				ContentTableRow tableRow = table.getSelectedRows().get(0);
				MaterialLabel label = (MaterialLabel) tableRow.getChildrenList().get(1);
				String tgrUrl = (String) Registry.get("service.server") + "/detail/detail_view.html?cotid=" + label.getText().trim();
				Registry.openPreview(courseIcon5, tgrUrl);
			}
		});
		table.getButtomMenu().addIcon(courseIcon5, "미리 보기", com.google.gwt.dom.client.Style.Float.RIGHT);
		
	}

	private void doImageProcess() {
		ContentTableRow ctr = table.getSelectedRows().get(0);
		JSONObject jObj = (JSONObject) ctr.get("recInfo");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("ODM_ID", jObj.get("ODM_ID").isString().stringValue());
		if (jObj.get("COT_ID") != null) map.put("COT_ID", jObj.get("COT_ID").isString().stringValue());
		this.getWindow().openDialog(OtherDepartmentMainApplication.IMAGE_CHANGE, map, 640);
	}

	private void doUrlDefine() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("AREA_COMPONENT", areaCompo);
		map.put("DETAIL", this);
		this.getWindow().openDialog(OtherDepartmentMainApplication.EXTERNAL_LINK, map, 640);
	}

	private void doFileUpload() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("AREA_COMPONENT", areaCompo);
		map.put("DETAIL", this);
		this.getWindow().openDialog(OtherDepartmentMainApplication.UPLOAD_FILE, map, 640);
	}

	private void doDataAdd() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("AREA_COMPONENT", areaCompo);
		map.put("DETAIL", this);
		this.getWindow().openDialog(OtherDepartmentMainApplication.SELECT_CONTENT, map, 640);
	}

	private void doDataload() {
		
	}

	private void doSelectedDelete() {
		
		if (table.getSelectedRows().size() > 0) {
		
			ContentTableRow ctr = table.getSelectedRows().get(0);
			int rowIndex = table.getRowContainer().getChildrenList().indexOf(ctr);
			
			String compODM = this.areaCompo.getInfo().get(0).isObject().get("ODM_ID").isString().stringValue();
			
			JSONObject jObj = (JSONObject) ctr.get("recInfo");
			String odmId = jObj.get("ODM_ID").isString().stringValue();

			if (compODM.equals(odmId)) {
				
				JSONObject cmdObj = new JSONObject();
				cmdObj.put("cmd", new JSONString("UPDATE_DEPT_AREA"));
				cmdObj.put("ODM_ID", new JSONString(odmId));
				executeBusiness(cmdObj);

				List<JSONObject> compoObjs = this.areaCompo.getInfo();
				
				JSONObject firstObject = compoObjs.get(0).isObject();
				firstObject.put("COT_ID", null);
				firstObject.put("CONTENT_TITLE", null);
				firstObject.put("IMG_ID", null);
				
			}else {
				
				Widget indexLable = table.getSelectedRows().get(0).getWidget(1);
				MaterialLabel ml = (MaterialLabel)indexLable;
				String cotIdString = ml.getText();
				
				for (JSONObject infoObj : this.areaCompo.getInfo()) {
					JSONValue tgrCotId = infoObj.get("COT_ID");
					if (tgrCotId != null) {
						String cotId = tgrCotId.isString().stringValue();
						
						if (cotId.trim().equals(cotIdString.trim())) {
							
							JSONObject cmdObj = new JSONObject();
							cmdObj.put("cmd", new JSONString("DELETE_DEPT_AREA"));
							cmdObj.put("ODM_ID", new JSONString(infoObj.get("ODM_ID").isString().stringValue()));
							executeBusiness(cmdObj);
							
							areaCompo.getInfo().remove(infoObj);
						}
					}
				}
				
			}
			
			setAreaComponent(this.areaCompo);
		}
		
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {}

	public void setAreaComponent(AreaComponent aac) {
		
		table.clearRows();
		this.selectStatus.reset();
		this.selectStatus1.reset();
		this.sectionTitle.setText("");
		this.tag1.setText("");
		this.tag2.setText(""); 
		this.tag3.setText("");
		if (!aac.equals(this.areaCompo)) this.areaCompo = aac;
		
		if (areaCompo.getInfo().get(0).get("VIEW_TITLE") != null && areaCompo.getInfo().get(0).get("VIEW_TITLE").isNumber().doubleValue() == 1) {
			this.selectStatus1.setSelectionOnSingleMode("노출");
		}else {
			this.selectStatus1.setSelectionOnSingleMode("비노출");
		}
		
		if (areaCompo.getInfo().get(0).get("VIEW_CONNECT_MORE") != null && areaCompo.getInfo().get(0).get("VIEW_CONNECT_MORE").isString().stringValue().trim().equals("1")) {
			this.selectStatus.setSelectionOnSingleMode("노출");
		}else {
			this.selectStatus.setSelectionOnSingleMode("비노출");
		}

		int rowIndex = 1;
		
		for (JSONObject jObj : this.areaCompo.getInfo()) {
			
			if (COMP_ID == null && jObj.get("COMP_ID") != null) COMP_ID = jObj.get("COMP_ID").isString().stringValue();
			
			if (jObj.get("TITLE") != null) {
				String titleValue = jObj.get("TITLE").isString().stringValue();
				if (titleValue.trim().length() > 0) {
					this.sectionTitle.setText(titleValue.trim());
				}
			}
			
			if (jObj.get("CONNECT_MORE") != null) {
				String connMoreValue = jObj.get("CONNECT_MORE").isString().stringValue();
				String[] tags = connMoreValue.split("\\|");
				for (int i=0; i<tags.length; i++) {
					if (i==0) {
						this.tag1.setText(tags[0]);
					}else if (i==1) {
						this.tag2.setText(tags[1]);
					}else if (i==2) {
						this.tag3.setText(tags[2]);
					}
				}
			}
			
			String COT_ID = "";
			String CONTENT_TITLE = "";
			String CREATE_DATE = "";
			
			if (jObj.get("COT_ID") != null) COT_ID = jObj.get("COT_ID").isString().stringValue();
			if (jObj.get("CONTENT_TITLE") != null) CONTENT_TITLE = jObj.get("CONTENT_TITLE").isString().stringValue();
			if (jObj.get("CREATE_DATE") != null) CREATE_DATE = jObj.get("CREATE_DATE").isString().stringValue();
			if (jObj.get("LINK_FILE_URL") != null)  COT_ID = "파일 링크";
			else if (jObj.get("LINK_URL") != null)  COT_ID = "URL 링크";
			
			final String lastRectype = COT_ID;
			
			boolean displayflag = true;
			if (rowIndex == 1 && (COT_ID.equals("") && CONTENT_TITLE.equals(""))) {
				displayflag = false;
			}

			if (displayflag) {
				
				ContentTableRow ctr = table.addRow(Color.WHITE, rowIndex + "",  COT_ID, CONTENT_TITLE,  CREATE_DATE );
				
				ctr.addDoubleClickHandler(event->{
					
					if (table.getSelectedRows().size() > 0) {
						if (lastRectype.equals("파일 링크")){
							
							String fileUrl = jObj.get("LINK_FILE_URL").isString().stringValue();
							Registry.openPreview(courseIcon5, "https://kor.uniess.co.kr" + fileUrl);
							
						}else if (lastRectype.equals("URL 링크")){
							
							String extUrl = jObj.get("LINK_URL").isString().stringValue();
							Registry.openPreview(courseIcon5, extUrl);
							
						}else {
							
							ContentTableRow tableRow = table.getSelectedRows().get(0);
							MaterialLabel label = (MaterialLabel) tableRow.getChildrenList().get(1);
							String tgrUrl = (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid=" + label.getText().trim();
							Registry.openPreview(courseIcon5, tgrUrl);
							
						}
					}
				});
				ctr.put("recInfo", jObj);
				rowIndex++;
			}
				

		}
		
	}

	@Override
	public void loadData() {
		
	}

}
package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
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

public class CalendarContentDetail extends AbtractOtherDepartmentMainContents {

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
	private MaterialIcon editIcon5;

	public CalendarContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		Registry.put("ShowcaseContentDetail", this);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("켈린더");
		buildContent();
	}

	private void buildContent() {

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(490);
		table.setSelectedContainerVisible(true);	// visible selected container
		table.setSelectedPanelLink(true);			// visible selected panel link
		table.setSelectedMoveLink(true);			// visible selected Move link
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setRight(30);
		table.setTop(50);
		
		table.appendTitle("순서", 50, TextAlign.RIGHT);
		table.appendTitle("ID", 80, TextAlign.CENTER);
		table.appendTitle("지역", 120, TextAlign.CENTER);
		table.appendTitle("축제명", 270, TextAlign.CENTER);
		table.appendTitle("등록일자", 100, TextAlign.CENTER);
		table.appendTitle("축제 기간", 200, TextAlign.CENTER);
		
		this.add(table);

		MaterialIcon courseReloadIcon = new MaterialIcon(IconType.ADD);
		courseReloadIcon.setTextAlign(TextAlign.CENTER);
		courseReloadIcon.addClickHandler(event->{
			doDataAdd(); 
		});
		this.add(courseReloadIcon); 
		table.getTopMenu().addIcon(courseReloadIcon, "컨텐츠 추가", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", false);
		
		MaterialIcon courseIcon3 = new MaterialIcon(IconType.DELETE);
		courseIcon3.setTextAlign(TextAlign.CENTER);
		courseIcon3.addClickHandler(event->{
			doSelectedDelete();
		});
		table.getButtomMenu().addIcon(courseIcon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		editIcon5 = new MaterialIcon(IconType.EDIT);
		editIcon5.setTextAlign(TextAlign.CENTER);
		editIcon5.addClickHandler(event->{
		});
		table.getButtomMenu().addIcon(editIcon5, "수정", com.google.gwt.dom.client.Style.Float.RIGHT);
		
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
				String tgrUrl = (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid=" + label.getText().trim();
				Registry.openPreview(courseIcon5, tgrUrl);
			}
		});
		
		table.getButtomMenu().addIcon(courseIcon5, "미리 보기", com.google.gwt.dom.client.Style.Float.RIGHT);

		for (int i=0; i<30; i++) {
			table.addRow(Color.WHITE, i+"", "2", "3", "4", "5", "6");
		}

		table.addSelctedRow(0);
		table.addSelctedRow(1);
		table.addSelctedRow(2);
		table.addSelctedRow(3);
		table.addSelctedRow(7);
		table.addSelctedRow(8);
	

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
package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentMainContentDetailForShowcase extends AbtractOtherDepartmentMainContents {

	private MaterialPanel panel;
	private MaterialLabel cotLabel;
	private MaterialLabel cidLabel;
	private MaterialTextBox sectionTitle;
	private SelectionPanel selectStatus;
	private SelectionPanel selectStatus1;
	private MaterialLabel tag1;
	private ContentTable table;
	private AreaComponent areaCompo;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private String COMP_ID;
	private MaterialIcon courseIcon7;

	public OtherDepartmentMainContentDetailForShowcase(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		Registry.put("OtherDepartmentMainContentDetailForShowcase", this);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("쇼케이스 영역 컨텐츠 상세");
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
		addLabel(row1, "쇼케이스 노출 형태", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		this.tag1 = addLabel(row1, "", TextAlign.CENTER, Color.WHITE, "s7");
	
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("일반", "0");
		map1.put("와이드", "1");
		this.selectStatus = addSelectionPanel(row1, "s2", TextAlign.CENTER, map1);
		this.selectStatus.setSelectionOnSingleMode("일반");
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
		
		MaterialLabel mentLabel = new MaterialLabel("(*) 쇼케이스 이미지 : 일반형 (940 X 700), 와이드형 (1900 X 700)");
		mentLabel.setLayoutPosition(Position.ABSOLUTE);
		mentLabel.setTextAlign(TextAlign.LEFT);
		mentLabel.setTextColor(Color.RED_ACCENT_2);
		mentLabel.setFontWeight(FontWeight.BOLDER);
		mentLabel.setTop(170);
		mentLabel.setLeft(30);
		this.add(mentLabel);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(545);
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

		MaterialIcon courseReloadIcon = new MaterialIcon(IconType.ADD);
		courseReloadIcon.setTextAlign(TextAlign.CENTER);
		courseReloadIcon.setLayoutPosition(Position.ABSOLUTE);
		courseReloadIcon.setRight(30);
		courseReloadIcon.setTop(170);
		courseReloadIcon.setTooltip("등록된 컨텐츠 선택");
		courseReloadIcon.addClickHandler(event->{
			doDataAdd(true,null); 
		});
		this.add(courseReloadIcon);

		MaterialIcon fileUploadIcon = new MaterialIcon(IconType.FILE_UPLOAD);
		fileUploadIcon.setTextAlign(TextAlign.CENTER);
		fileUploadIcon.setLayoutPosition(Position.ABSOLUTE);
		fileUploadIcon.setRight(60);
		fileUploadIcon.setTop(170);
		fileUploadIcon.setTooltip("파일 링크 생성");
		fileUploadIcon.addClickHandler(event->{
			doFileUpload(true,null); 
		});
		this.add(fileUploadIcon);

		MaterialIcon urlIcon = new MaterialIcon(IconType.CLOUD);
		urlIcon.setTextAlign(TextAlign.CENTER);
		urlIcon.setLayoutPosition(Position.ABSOLUTE);
		urlIcon.setRight(90);
		urlIcon.setTop(170);
		urlIcon.setTooltip("링크 URL 생성"); 
		urlIcon.addClickHandler(event->{
			doUrlDefine(true,null); 
		});
		this.add(urlIcon);
		
		MaterialIcon courseIcon3 = new MaterialIcon(IconType.DELETE);
		courseIcon3.setTextAlign(TextAlign.CENTER);
		courseIcon3.addClickHandler(event->{
			doSelectedDelete();
		});
		table.getButtomMenu().addIcon(courseIcon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		
		MaterialIcon courseIcon4 = new MaterialIcon(IconType.ARROW_UPWARD);
		courseIcon4.setTextAlign(TextAlign.CENTER);
		courseIcon4.addClickHandler(event->{
			doContentUpDown(true);
		});
		table.getButtomMenu().addIcon(courseIcon4, "위로", com.google.gwt.dom.client.Style.Float.RIGHT);
		courseIcon4.setMarginLeft(0);
		courseIcon4.setMarginRight(0);
		
		MaterialIcon courseIcon5 = new MaterialIcon(IconType.ARROW_DOWNWARD);
		courseIcon5.setTextAlign(TextAlign.CENTER);
		courseIcon5.addClickHandler(event->{
			doContentUpDown(false);
		});
		courseIcon5.setMarginLeft(0);
		courseIcon5.setMarginRight(0);
		
		table.getButtomMenu().addIcon(courseIcon5, "아래로", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		
		
		courseIcon7 = new MaterialIcon(IconType.WEB);
		courseIcon7.setTextAlign(TextAlign.CENTER);
		courseIcon7.addClickHandler(event->{
			if (table.getSelectedRows().size() > 0) {
				ContentTableRow tableRow = table.getSelectedRows().get(0);
				String Type = tableRow.get("TYPE").toString();
				if(Type.equals("파일 링크") || Type.equals("URL 링크")) {
					MaterialToast.fireToast("파일링크와 URL링크는 미리보기가 불가능합니다");
					return;
				}
				MaterialLabel label = (MaterialLabel) tableRow.getChildrenList().get(1);
				String tgrUrl = (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid=" + label.getText().trim();
				Registry.openPreview(courseIcon7, tgrUrl);
			}
		});
		table.getButtomMenu().addIcon(courseIcon7, "미리 보기", com.google.gwt.dom.client.Style.Float.RIGHT);
		
	}

	private void doUrlDefine(boolean type, JSONObject jObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("AREA_COMPONENT", areaCompo);
		map.put("DETAIL", this);
		map.put("CDTypeImgGuideFlag", true);
		if(type == true)
			map.put("TYPE", "INSERT");
		else {
			map.put("info",jObj);
			map.put("TYPE", "UPDATE");
		}

		this.getWindow().openDialog(OtherDepartmentMainApplication.EXTERNAL_LINK, map, 700);
	}

	private void doFileUpload(boolean type, JSONObject jObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("AREA_COMPONENT", areaCompo);
		map.put("DETAIL", this);
		if(type == true)
			map.put("TYPE", "INSERT");
		else {
			map.put("info",jObj);
			map.put("TYPE", "UPDATE");
		}
		this.getWindow().openDialog(OtherDepartmentMainApplication.UPLOAD_FILE, map, 700);
	}

	private void doDataAdd(boolean type, JSONObject jObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OTD_ID", this.getWindow().getValueMap().get("OTD_ID"));
		map.put("AREA_COMPONENT", areaCompo);
		map.put("DETAIL", this);
		if(type == true) {
			map.put("TYPE", "INSERT");
		this.getWindow().openDialog(OtherDepartmentMainApplication.SELECT_CONTENT, map, 640);
		}else {
			map.put("info",jObj);			
			map.put("TYPE", "UPDATE");
			this.getWindow().openDialog(OtherDepartmentMainApplication.UPDATE_CONTENTS, map, 560);
		}
	}
	

	private void doSelectedDelete() {
		
		if (table.getSelectedRows().size() > 0) {
		
			ContentTableRow ctr = table.getSelectedRows().get(0);
			int rowIndex = table.getRowContainer().getChildrenList().indexOf(ctr);
			
			String compODM = this.areaCompo.getInfo().get(0).isObject().get("ODM_ID").isString().stringValue();
			
			JSONObject jObj = (JSONObject) ctr.get("recInfo");
			String odmId = jObj.get("ODM_ID").isString().stringValue();

			JSONObject tgrODM_ID = (JSONObject)table.getSelectedRows().get(0).get("TARGET_OBJECT");
			
			JSONObject cmdObj = new JSONObject();
			cmdObj.put("cmd", new JSONString("DELETE_DEPT_AREA"));
			cmdObj.put("ODM_ID", new JSONString(tgrODM_ID.get("ODM_ID").isString().stringValue()));
			executeBusiness(cmdObj);
			
			areaCompo.getInfo().remove(tgrODM_ID);
			setAreaComponent(this.areaCompo);
		}
		
	}

	private void doContentUpDown(boolean order) {
		
		List<JSONObject> infoList = this.areaCompo.getInfo();
		ContentTableRow IndexUpBefore = table.getSelectedRows().get(0);
		int TargetIndex = table.getRowContainer().getWidgetIndex(IndexUpBefore);
		
		if(order) {
			if(TargetIndex != 0) {
				Collections.swap(infoList,TargetIndex+1,TargetIndex);
			} else {
				MaterialToast.fireToast("더이상 올릴 수 없습니다.");
			}
		} else {
			if(infoList.size() != TargetIndex+2) {
				Collections.swap(infoList,TargetIndex+1,TargetIndex+2);
			} else {
				MaterialToast.fireToast("더이상 내릴 수 없습니다.");
			}
		}
		
		String reOrderString = "";
		for (int i = 0; i < infoList.size(); i++) {
			String odmId = infoList.get(i).get("ODM_ID").isString().stringValue();
			if( i == 0) {
				reOrderString += ( i+ "_" + odmId);
			} else {
				reOrderString += ( "," + i + "_" + odmId);
			}
		}
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("REORDER_DEPT_AREA"));
		jObj.put("ORDER", new JSONString(reOrderString));
		jObj.put("TYPE", new JSONString("COT_ORDER"));
		executeBusiness(jObj);
		this.setAreaComponent(areaCompo);
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
		if (!aac.equals(this.areaCompo)) this.areaCompo = aac;
		
		if (areaCompo.getInfo().get(0).get("VIEW_TITLE") != null && areaCompo.getInfo().get(0).get("VIEW_TITLE").isNumber().doubleValue() == 1) {
			this.selectStatus1.setSelectionOnSingleMode("노출");
		}else {
			this.selectStatus1.setSelectionOnSingleMode("비노출");
		}
		
		if (areaCompo.getInfo().get(0).get("VIEW_CONNECT_MORE") != null && areaCompo.getInfo().get(0).get("VIEW_CONNECT_MORE").isString().stringValue().trim().equals("1")) {
			this.selectStatus.setSelectionOnSingleMode("와이드");
		}else {
			this.selectStatus.setSelectionOnSingleMode("일반");
		}

		int rowIndex = 1;
		
		if (this.areaCompo.getInfo().get(0).get("TITLE") != null) {
			String titleValue = this.areaCompo.getInfo().get(0).get("TITLE").isString().stringValue();
			if (titleValue.trim().length() > 0) {
				this.sectionTitle.setText(titleValue.trim());
			}
		}
		
		if (this.areaCompo.getInfo().get(0).get("CONNECT_MORE") != null) {
			String connMoreValue = this.areaCompo.getInfo().get(0).get("CONNECT_MORE").isString().stringValue();
			String[] tags = connMoreValue.split("\\|");
			for (int i=0; i<tags.length; i++) {
				if (i==0) {
					this.tag1.setText(tags[0]);
				}
			}
		}

		List<JSONObject> infoList = this.areaCompo.getInfo();
		
		for (int idx=1; idx<infoList.size(); idx++) {
			
			JSONObject jObj = infoList.get(idx);
			
			if (COMP_ID == null && jObj.get("COMP_ID") != null) COMP_ID = jObj.get("COMP_ID").isString().stringValue();
			
			String COT_ID = "";
			String CONTENT_TITLE = "";
			String CREATE_DATE = "";
			
			if (jObj.get("COT_ID") != null) COT_ID = jObj.get("COT_ID").isString().stringValue();
			if (jObj.get("CONTENT_TITLE") != null) CONTENT_TITLE = jObj.get("CONTENT_TITLE").isString().stringValue();
			if (jObj.get("CREATE_DATE") != null) CREATE_DATE = jObj.get("CREATE_DATE").isString().stringValue();
			
			final String lastRectype = COT_ID;
			
			
			ContentTableRow ctr = null;
			
			String DISP_STR = "";
			if (COT_ID.equals("")) {
				
				if (jObj.get("LINK_FILE_URL") != null)  DISP_STR = "파일 링크";
				else if (jObj.get("LINK_URL") != null)  DISP_STR = "URL 링크";
				ctr = table.addRow(Color.WHITE, rowIndex + "",  DISP_STR, CONTENT_TITLE,  CREATE_DATE );
				
			}else {
				
				ctr = table.addRow(Color.WHITE, rowIndex + "",  COT_ID, CONTENT_TITLE,  CREATE_DATE );
			}
			
			if (ctr != null) {
				ctr.put("TARGET_OBJECT", jObj);
				ctr.put("TYPE", DISP_STR);
				
				ctr.addDoubleClickHandler(event->{
					
					if (table.getSelectedRows().size() > 0) {
						ContentTableRow tableRow = table.getSelectedRows().get(0);
						String Type = tableRow.get("TYPE").toString();
						if(Type.equals("파일 링크") ) {
							doFileUpload(false, jObj); 
						} else if(Type.equals("URL 링크")) {
							doUrlDefine(false, jObj); 
						} else {
							doDataAdd(false, jObj);
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
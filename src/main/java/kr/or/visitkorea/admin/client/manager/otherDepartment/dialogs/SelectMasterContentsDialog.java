package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing.MarketingDetailPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectMasterContentsDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;
	private String contentType;
	private MaterialLabel dialogTitle;
	private boolean multipleContents;
	private ContentTable targetTable;
	private String tbl;
	private String cotId;
	private Object tgrPanel;
	private String otdId;
	private MaterialButton selectButton;
	private String manId;

	public SelectMasterContentsDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		manId = (String) getParameters().get("MAN_ID");
		selectButton.setEnabled(true);
		okButton.setEnabled(true);
		
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			// 쇼케이스
			listBox.setEnabled(Registry.getPermission("8fd550af-e03a-4599-8d41-f1f272256aaf"));
			searchBox.setEnabled(Registry.getPermission("8fd550af-e03a-4599-8d41-f1f272256aaf"));
			selectButton.setEnabled(Registry.getPermission("7d9cb476-8d93-437f-940d-e83ce2cecc6a"));
			okButton.setEnabled(Registry.getPermission("e1e097b7-1e8a-46c3-b0c2-c7bdc09ec623"));
			
			// 큐레이션
			listBox.setEnabled(Registry.getPermission("5ff89eed-1450-45d3-8450-8509e6e55051"));
			searchBox.setEnabled(Registry.getPermission("5ff89eed-1450-45d3-8450-8509e6e55051"));
			selectButton.setEnabled(Registry.getPermission("69dc6d5b-a325-48c0-9fb9-1a26154a6a2b"));
			okButton.setEnabled(Registry.getPermission("4c07a335-4ef5-48c9-aea7-1d8925bcee54"));
			
			// 마케팅
			listBox.setEnabled(Registry.getPermission("06e78bb0-23c6-4fc8-9200-782f5f149720"));
			searchBox.setEnabled(Registry.getPermission("06e78bb0-23c6-4fc8-9200-782f5f149720"));
			selectButton.setEnabled(Registry.getPermission("6933db7f-245e-435c-b6b1-6e14c2c3827a"));
			okButton.setEnabled(Registry.getPermission("2921fc45-dac1-457b-b61d-b96ab958d3fb"));
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			// 쇼케이스
			listBox.setEnabled(Registry.getPermission("4035f814-2cf4-495a-90b5-054822b6aa31"));
			searchBox.setEnabled(Registry.getPermission("4035f814-2cf4-495a-90b5-054822b6aa31"));
			selectButton.setEnabled(Registry.getPermission("7ffd07d7-d65a-47c4-a11b-84fd2ffb090d"));
			okButton.setEnabled(Registry.getPermission("5500a497-0d74-4792-9f32-22f8d17909e3"));
			
			// 큐레이션
			listBox.setEnabled(Registry.getPermission("bcf4b6d3-9a05-4d15-aa32-5c2370d02358"));
			searchBox.setEnabled(Registry.getPermission("bcf4b6d3-9a05-4d15-aa32-5c2370d02358"));
			selectButton.setEnabled(Registry.getPermission("6df128a0-4d78-4f55-bab6-f24be81eda0e"));
			okButton.setEnabled(Registry.getPermission("513051f9-3c6e-490a-b9ac-8e43d895c37f"));
			
			// 마케팅
			listBox.setEnabled(Registry.getPermission("65cf1423-772b-44bf-9e6a-51e808dcf75c"));
			searchBox.setEnabled(Registry.getPermission("65cf1423-772b-44bf-9e6a-51e808dcf75c"));
			selectButton.setEnabled(Registry.getPermission("cc41b962-d847-4dbe-a392-47e3cfd4e465"));
			okButton.setEnabled(Registry.getPermission("671bfbdb-f5c3-4849-be5f-4ebecd731f05"));
		}
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 생성 - 등록된 메인 컨텐츠 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		this.add(buildSearchArea());
		this.add(buildTable());
		
		selectButton = new MaterialButton("컨텐츠 추가");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event -> {

			// 컨텐츠 추가 쿼리.
			MaterialLabel cotIdLabel = (MaterialLabel)table.getSelectedRows().get(0).getChildrenList().get(0);
			String cotId = cotIdLabel.getText().trim();
			
			
			Object parentObject = getParameters().get("PARENT");
			
			if(parentObject instanceof ContentDetail) {
			
				ContentDetail scd = (ContentDetail)parentObject;
	
				ContentRow masterContentRow = scd.getCustomRow();
				masterContentRow.setCotId(cotId);
				masterContentRow.buildComponent();
				
				scd.addRow(masterContentRow);
				
			}else if (parentObject instanceof MarketingDetailPanel) {
				
				ContentTableRow selectedRow = table.getSelectedRows().get(0);
				JSONObject retObj = (JSONObject)selectedRow.get("RETOBJ");
				String TITLE = retObj.get("TITLE").isString().toString().replaceAll("\"", "");
				String COT_ID = retObj.get("COT_ID").isString().toString().replaceAll("\"", "");

				String IMG_ID = null; 
				
				if (retObj.get("ARTICLE_IMAGE") != null) {
					IMG_ID = retObj.get("ARTICLE_IMAGE").isString().toString();
				}else if (retObj.get("DATABASE_IMAGE") != null) {
					IMG_ID = retObj.get("DATABASE_IMAGE").isString().toString();
				}
				
				String imageUrl = Registry.getDefaultImageId();
				if (IMG_ID != null) imageUrl = IMG_ID.replaceAll("\"", "");
				
				String linkUrl = (String) Registry.get("service.server") + "/detail/detail_view.html?cotid=" + COT_ID + "&chk=" + IDUtil.uuid(16);
				
				MarketingDetailPanel scd = (MarketingDetailPanel)parentObject;
				scd.getDisplayitle().setValue(TITLE);
				scd.getUrl().setValue(linkUrl);
				scd.getImagePanel1().setImageId(imageUrl);
				scd.updateValue();
				
			}
			
			getMaterialExtentsWindow().closeDialog();

		});
		
		this.addButton(selectButton);

	}

	private MaterialPanel buildPreview() {

		MaterialPanel previewPanel = new MaterialPanel();
		previewPanel.setLayoutPosition(Position.ABSOLUTE);
		previewPanel.setTop(60);
		previewPanel.setLeft(530);
		previewPanel.setRight(30);
		previewPanel.setBottom(123);
		previewPanel.setBorder("1px solid #dddddd");

		return previewPanel;
	}

	private MaterialPanel buildSearchArea() {

		MaterialPanel searchPanel = new MaterialPanel();
		searchPanel.setLayoutPosition(Position.ABSOLUTE);
		searchPanel.setLeft(30);
		searchPanel.setRight(30);
		searchPanel.setHeight("80px");
		
		listBox = new MaterialListBox();
		listBox.setLayoutPosition(Position.ABSOLUTE);
		listBox.addItem("컨텐츠명");
		listBox.addItem("CID");
		listBox.addValueChangeHandler(event->{
			
		});
		
		searchPanel.add(listBox);
		
		searchBox = new MaterialTextBox();
		searchBox.setLayoutPosition(Position.ABSOLUTE);
		searchBox.setPlaceholder("검색할 단어를 입력해 주세요.");
		searchBox.setLeft(180);
		searchBox.setRight(0);
		searchBox.setTextAlign(TextAlign.CENTER);
		searchBox.addKeyUpHandler(event->{
			
			if (event.getNativeKeyCode() == 13 && searchBox.getValue().length() > 0) {
				
				table.loading(false);
				table.clearRows();
				
				int chkSMode = 1;
				
				if (this.multipleContents){
					chkSMode = 0;
				}
				
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("GET_OTHER_DEPARTMENT_M_CONTENT"));
				parameterJSON.put("mode", new JSONNumber(listBox.getSelectedIndex()));
				parameterJSON.put("keyword", new JSONString(searchBox.getText()));
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {

						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().toString();
						processResult = processResult.replaceAll("\"", "");
						
						if (processResult.equals("success")) {

							JSONObject bodyObj =  resultObj.get("body").isObject();
							JSONArray resultArray = bodyObj.get("result").isObject().get("contents").isArray();
	
							int usrCnt = resultArray.size();
							if (usrCnt == 0) {
								getMaterialExtentsWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
							}
							
							for (int i=0; i<usrCnt; i++) {
								
								JSONObject recObj = resultArray.get(i).isObject();
								ContentTableRow tableRow = table.addRow(
										Color.WHITE, 
										getString(recObj, "COT_ID", "·"),
										getString(recObj, "TITLE", "·"));
								
								tableRow.put("RETOBJ", recObj);
								
							}
						}
						
						table.loading(false);
					}
				});

				
			}

		});
		
		searchPanel.add(searchBox);

		
		
		return searchPanel;
	}

	private ContentTable buildTable() {
		
		ContentTable table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setRight(30);
		table.setTop(120);
		table.setHeight(280);
		table.appendTitle("COT_ID", 300, TextAlign.RIGHT);
		table.appendTitle("제목", 415, TextAlign.LEFT);
		
		this.table = table;
	
		return table;
	}
	
	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		searchBox.setText("");
		table.clearRows();
		listBox.setSelectedIndex(0);
	}

	private void setContentType(String contentType) {
		this.contentType = contentType;
		if (this.contentType.startsWith("(")) {
			this.multipleContents = true;
		}else {
			this.multipleContents = false;
		}
	}

	private String getContentType(String object) {
		return this.contentType;
	}

	@Override
	public int getHeight() {
		return 520;
	}
	
	private void executeBusiness(JSONObject parameterJSON) {
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
				}
			}
		});

	}

}

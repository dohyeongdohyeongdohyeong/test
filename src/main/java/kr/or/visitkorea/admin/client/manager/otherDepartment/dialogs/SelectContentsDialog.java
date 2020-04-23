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
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.CategoryMainContentPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentSection;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.course.CourseContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee.SiteseeCurationContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetailForShowcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectContentsDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;
	private String contentType;
	private MaterialLabel dialogTitle;
	private boolean multipleContents;
	private ContentTable targetTable;
	private String tbl;
	private String cotId;
	private ContentSection section;
	private String otdId;

	public SelectContentsDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		this.section = (ContentSection) this.getParameters().get("SECTION");
		if (this.section == ContentSection.EVENT) {
			this.listBox.clear();
			this.listBox.addItem("진행중 이벤트");
			this.listBox.addItem("당첨자 발표 이벤트");
		}
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 생성 - 등록된 컨텐츠 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		this.add(buildSearchArea());
		this.add(buildTable());
		
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event -> {
	
			Object compObj = getParameters().get("AREA_COMPONENT");
			
			if (compObj instanceof AreaComponent) {
				Console.log("compObj object instance of AreaComponent");
				setupAreaComponentValue(compObj);
			}else if (compObj instanceof ContentLayoutPanel) {
				Console.log("compObj object instance of ContentLayoutPanel");
				setupContentLayoutValue(compObj);
			}else {
				Console.log("compObj object instance of " + compObj);
			}

		});
		
		this.addButton(selectButton);

	}

	private void setupContentLayoutValue(Object compObj) {
		
		ContentSection section = (ContentSection) getParameters().get("SECTION");
		ContentLayoutPanel contentLayoutPanel = (ContentLayoutPanel) compObj;

		
		Console.log("section :: " + section);
		
		if (section != null) {
			
			
			
			if (section.equals(ContentSection.ARTICLE)) {
				buildArticleSection(compObj);
				getMaterialExtentsWindow().closeDialog();
			}else if (section.equals(ContentSection.SIGHT)) {
				buildSightSection(compObj);
				getMaterialExtentsWindow().closeDialog();
			}else if (section.equals(ContentSection.COURSE)) {
				buildCourseSection(compObj);
			}else if (section.equals(ContentSection.FESTIVAL)) {
				buildFestivalSection(compObj);
				getMaterialExtentsWindow().closeDialog();
			}else if (section.equals(ContentSection.EVENT)) {
				buildEventSection(compObj);
				getMaterialExtentsWindow().closeDialog();
			}
			
		}
	}

	private void buildArticleSection(Object compObj) {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow tgrRow = table.getSelectedRows().get(0);
			JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
			String COT_ID = retObjects.get("COT_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("ARTICLE_SELECT_WITH_COTID"));
			parameterJSON.put("cotId", new JSONString(COT_ID));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
				@Override
				public void call(Object param1, String param2, Object param3) {
	
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {

						JSONObject rsObj = resultObj.get("body").isObject().get("result").isObject();
						
						CategoryMainContentPanel sectionPanel = 
								(CategoryMainContentPanel)getParameters().get("AREA_COMPONENT");
						
						sectionPanel.setData(rsObj);
	
					}
				}
			});
			
		}
		
	}

	private void buildSightSection(Object compObj) {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow tgrRow = table.getSelectedRows().get(0);
			JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
			String COT_ID = retObjects.get("COT_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("GET_BASE_WITH_COTID"));
			parameterJSON.put("cotId", new JSONString(COT_ID));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
				@Override
				public void call(Object param1, String param2, Object param3) {
	
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {

						JSONObject rsObj = resultObj.get("body").isObject().get("result").isObject();
						Object areaComponent = getParameters().get("AREA_COMPONENT");
						
						if (areaComponent instanceof CategoryMainContentPanel) {
							CategoryMainContentPanel sectionPanel = (CategoryMainContentPanel)areaComponent;
							sectionPanel.setData(rsObj);
						}else if (areaComponent instanceof SiteseeCurationContentLayout) {
							SiteseeCurationContentLayout sectionPanel = (SiteseeCurationContentLayout)areaComponent;
							sectionPanel.addData(rsObj);
						}
					}
				}
			});
			
		}		
	}

	private void buildCourseSection(Object compObj) {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow tgrRow = table.getSelectedRows().get(0);
			JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
			String COT_ID = retObjects.get("COT_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("GET_BASE_WITH_COTID"));
			parameterJSON.put("cotId", new JSONString(COT_ID));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
				@Override
				public void call(Object param1, String param2, Object param3) {
	
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {

						JSONObject rsObj = resultObj.get("body").isObject().get("result").isObject();
						
						
						Object areaComponent = getParameters().get("AREA_COMPONENT");
						
						if (areaComponent instanceof CourseContentLayout) {
							
							if (!rsObj.toString().equals("{}")) {
								CourseContentLayout sectionPanel = (CourseContentLayout)areaComponent;						
								sectionPanel.addData(rsObj);
								getMaterialExtentsWindow().closeDialog();
							}else {
								alert("안내", 600, 250, new String[] { 
										"코스를 생성할 수 없습니다.", 
										"해당 코스는 완성된 코스가 아닙니다. ",
										"완성된 코스가 아니면 선택할 수 없습니다." });

							}
						}
					}
				}
			});
		}		
	}

	private void buildFestivalSection(Object compObj) {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow tgrRow = table.getSelectedRows().get(0);
			JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
			String COT_ID = retObjects.get("COT_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("GET_FESTIVAL_BASE_WITH_COTID"));
			parameterJSON.put("cotId", new JSONString(COT_ID));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
				@Override
				public void call(Object param1, String param2, Object param3) {
	
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {

						JSONObject rsObj = resultObj.get("body").isObject().get("result").isObject();
						Object areaComponent = getParameters().get("AREA_COMPONENT");
						
						if (areaComponent instanceof CategoryMainContentPanel) {
							CategoryMainContentPanel sectionPanel = (CategoryMainContentPanel)areaComponent;
							sectionPanel.setData(rsObj);
						}else if (areaComponent instanceof SiteseeCurationContentLayout) {
							SiteseeCurationContentLayout sectionPanel = (SiteseeCurationContentLayout)areaComponent;
							sectionPanel.addData(rsObj);
						}
					}
				}
			});
			
		}	
		
	}

	private void buildEventSection(Object compObj) {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow tgrRow = table.getSelectedRows().get(0);
			JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
			String COT_ID = retObjects.get("COT_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("GET_EVENT"));
			parameterJSON.put("cotId", new JSONString(COT_ID));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
				@Override
				public void call(Object param1, String param2, Object param3) {
	
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {

						JSONObject rsObj = resultObj.get("body").isObject().get("result").isObject();
						
						CategoryMainContentPanel sectionPanel = 
								(CategoryMainContentPanel) getParameters().get("AREA_COMPONENT");
						
						sectionPanel.setData(rsObj);
	
					}
				}
			});
			
		}
		
	}

	private void setupAreaComponentValue(Object compObj) {
		
		AreaComponent aac = (AreaComponent) compObj;
		
		String COMP_ID = aac.getCOMP_ID();
		
		int size = aac.getInfo().size();
		JSONObject jObj = aac.getInfo().get(0);
		
		ContentTableRow tgrRow = table.getSelectedRows().get(0);
		JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
		String cotId = retObjects.get("COT_ID").isString().stringValue();
		String sectionTitle = "";
		
		if (jObj.get("TITLE") != null) sectionTitle = jObj.get("TITLE").isString().stringValue();
		else if (jObj.get("CONTENT_TITLE") != null) sectionTitle = jObj.get("CONTENT_TITLE").isString().stringValue();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_DEPT_AREA_ROW"));
		parameterJSON.put("COMP_ORDER", jObj.get("COMP_ORDER"));
		parameterJSON.put("VIEW_TITLE", new JSONNumber(1));
		parameterJSON.put("TEMPLATE_ID", jObj.get("TEMPLATE_ID"));
		parameterJSON.put("MAIN_AREA", jObj.get("MAIN_AREA"));
		parameterJSON.put("COMP_ID", new JSONString(COMP_ID));
		parameterJSON.put("OTD_ID", jObj.get("OTD_ID"));
		parameterJSON.put("COT_ID", new JSONString(cotId));
		parameterJSON.put("COT_ORDER", new JSONNumber(size));
		parameterJSON.put("TITLE", new JSONString(sectionTitle));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject rsObj = resultObj.get("body").isObject().get("result").isObject();

					String odmId = rsObj.get("ODM_ID").isString().stringValue();
					String cotId = rsObj.get("COT_ID").isString().stringValue();
					String imgId = (rsObj.get("IMG_ID") == null ) ? "" : rsObj.get("IMG_ID").isString().stringValue();
					String recTitle = rsObj.get("CONTENT_TITLE").isString().stringValue();
					String recCd = rsObj.get("CREATE_DATE").isString().stringValue();

					JSONObject newRecJSON = new JSONObject();
					
					newRecJSON.put("COMP_ORDER", parameterJSON.get("COMP_ORDER"));
					newRecJSON.put("ODM_ID", new JSONString(odmId));
					newRecJSON.put("VIEW_TITLE", new JSONNumber(1));
					newRecJSON.put("TEMPLATE_ID",parameterJSON.get("TEMPLATE_ID"));
					newRecJSON.put("MAIN_AREA",parameterJSON.get("MAIN_AREA"));
					newRecJSON.put("COMP_ID",parameterJSON.get("COMP_ID"));
					newRecJSON.put("OTD_ID",parameterJSON.get("OTD_ID"));
					newRecJSON.put("TITLE",parameterJSON.get("TITLE"));
					newRecJSON.put("COT_ID", new JSONString(cotId));
					newRecJSON.put("IMG_ID", new JSONString(imgId));
					newRecJSON.put("CONTENT_TITLE", new JSONString(recTitle));
					newRecJSON.put("CREATE_DATE", new JSONString(recCd));
					
					aac.getInfo().add(newRecJSON);
					
					Object detail = getParameters().get("DETAIL");
					if (detail instanceof OtherDepartmentMainContentDetail) {
						OtherDepartmentMainContentDetail odmcd = (OtherDepartmentMainContentDetail)getParameters().get("DETAIL");
						odmcd.setAreaComponent(aac);
					}else if (detail instanceof OtherDepartmentMainContentDetailForShowcase) {
						OtherDepartmentMainContentDetailForShowcase odmcdfs = (OtherDepartmentMainContentDetailForShowcase)getParameters().get("DETAIL");
						odmcdfs.setAreaComponent(aac);
					}
				}
			}
		});
		
		getMaterialExtentsWindow().closeDialog();
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
			
			if (event.getNativeKeyCode() == 13) {
				
				table.loading(false);
				table.clearRows();
				
				int chkSMode = 1;
				
				if (this.multipleContents){
					chkSMode = 0;
				}
				
				String OTD_ID = (String) this.getParameters().get("OTD_ID");
				Object SECTION = this.getParameters().get("SECTION");
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("GET_OTHER_DEPARTMENT_CONTENT"));
				parameterJSON.put("otdId", new JSONString(OTD_ID));
				parameterJSON.put("mode", new JSONNumber(listBox.getSelectedIndex()));
				parameterJSON.put("keyword", new JSONString(searchBox.getText()));
				
				if (SECTION != null) {
					parameterJSON.put("otdId", new JSONString(""));
					parameterJSON.put("section", new JSONString(SECTION.toString()));
				}
				
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
		table.appendTitle("COT_ID", 200, TextAlign.RIGHT);
		table.appendTitle("제목", 350, TextAlign.LEFT);
		
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

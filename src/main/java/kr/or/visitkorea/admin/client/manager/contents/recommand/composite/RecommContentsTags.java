package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsTags extends AbtractRecommContents {

	private MaterialTextBox inputBox;
	private ContentTable table;
	private MaterialIcon icon1;
	private ContentTable searchTable;
	private MaterialIcon icon2;
	private MaterialPanel enablePanel;
	private MaterialIcon icon111;
	private MaterialLabel masterTag;
	private ContentTableRow tableRow;

	public RecommContentsTags(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setTitle("태그");
		buildContent();
	}

	private void buildContent() {
		
        // dialog title define
		inputBox = new MaterialTextBox();
		inputBox.setLabel("멤버 태그 검색");
		
		inputBox.addKeyUpHandler(event->{
			if (event.getNativeKeyCode() == 13 && inputBox.getValue().length() > 0) {
				searchTag(inputBox.getValue());
			}
		}); 
		
		inputBox.setIconType(IconType.SEARCH);
		inputBox.setLayoutPosition(Position.ABSOLUTE);
		inputBox.setRight(60);
		inputBox.setLeft(350);
		inputBox.setTop(70);
		
		this.add(inputBox);

		masterTag = new MaterialLabel();
		masterTag.setLayoutPosition(Position.ABSOLUTE);
		masterTag.setWidth("300px");
		masterTag.setLeft(30);
		masterTag.setTop(100);
		masterTag.setFontSize("1.3em");
		masterTag.setFontWeight(FontWeight.BOLD);
		masterTag.setTextColor(Color.RED_LIGHTEN_2);
		this.add(masterTag);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(370);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setWidth("300px");
		table.setTop(140);
		table.appendTitle("멤버 태그", 300, TextAlign.LEFT);

		this.add(table);

		icon1 = new MaterialIcon(IconType.DELETE);
		icon1.setTextAlign(TextAlign.CENTER);
		icon1.addClickHandler(event->{
			removeMemberTag();
		});

		icon111 = new MaterialIcon(IconType.SPELLCHECK);
		icon111.setTextAlign(TextAlign.CENTER);
		icon111.addClickHandler(event->{
			setupMasterTag();
		});

		table.getButtomMenu().addIcon(icon1, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(icon111, "대표태그 선택", com.google.gwt.dom.client.Style.Float.LEFT);
	
		searchTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		searchTable.setHeight(370);
		searchTable.setLayoutPosition(Position.ABSOLUTE);
		searchTable.setLeft(350);
		searchTable.setRight(30);
		searchTable.setTop(140);
		searchTable.appendTitle("검색 태그", 572, TextAlign.LEFT);
	
		this.add(searchTable);
/*
		// 멤버 태그 pager
		searchPager = new ContentBottomMenu();
		searchPager.setLayoutPosition(Position.ABSOLUTE);
		searchPager.setLeft(350);
		searchPager.setTop(510);
		searchPager.setWidth("574px");
*/	
		icon2 = new MaterialIcon(IconType.ADD);
		icon2.setTextAlign(TextAlign.CENTER);
		icon2.addClickHandler(event->{
			appendMemberTag();
		});

		searchTable.getButtomMenu().addIcon(icon2, "등록", com.google.gwt.dom.client.Style.Float.LEFT);
	
//		this.add(searchPager);
		
		enablePanel = new MaterialPanel();
		enablePanel.setLayoutPosition(Position.ABSOLUTE);
		enablePanel.setLeft(30);
		enablePanel.setRight(27);
		enablePanel.setTop(70);
		enablePanel.setBottom(26);
		enablePanel.setBackgroundColor(Color.BLACK);
		enablePanel.setOpacity(0.2);
		enablePanel.setVisibility(Visibility.HIDDEN);
		this.add(enablePanel);		
	}

	private void setupMasterTag() {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow row = table.getSelectedRows().get(0);
			String tagId = (String) row.get("TAG_ID");
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("SETUP_MASTER_TAG"));
			parameterJSON.put("cotId", new JSONString(getCotId()));
			parameterJSON.put("tagId", new JSONString(tagId));
	
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
						if (usrCnt == 0) {
							getWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
						}

						for (int i=0; i<usrCnt; i++) {
							
							JSONObject recObj = resultArray.get(i).isObject();
							masterTag.setText("#"+getString(recObj, "TAG_NAME"));
							((MaterialLabel)tableRow.getChildrenList().get(3)).setText(masterTag.getText());

						}
						
					}
				}

				private String getString(JSONObject recObj, String key) {
					if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
					else return recObj.get(key).isString().stringValue();
				}

			});
		}			
	}

	private void removeMemberTag() {
		
		if (table.getSelectedRows().size() > 0) {
			
			table.clearRows();
			
			ContentTableRow row = table.getSelectedRows().get(0);
			String tagId = (String) row.get("TAG_ID");
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("REMOVE_MEMBER_TAG"));
			parameterJSON.put("cotId", new JSONString(getCotId()));
			parameterJSON.put("tagId", new JSONString(tagId));
	
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
						if (usrCnt == 0) {
							getWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
						}

						for (int i=0; i<usrCnt; i++) {
							
							JSONObject recObj = resultArray.get(i).isObject();
							ContentTableRow tableRow = table.addRow( Color.WHITE,  getString(recObj, "TAG_NAME"));
							tableRow.put("TAG_ID", getString(recObj, "TAG_ID").replaceAll("#", ""));
							
						}
						
					}
				}

				private String getString(JSONObject recObj, String key) {
					if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
					else return "#"+recObj.get(key).isString().stringValue();
				}

			});
		}		
	}

	private void appendMemberTag() {
		
		if (searchTable.getSelectedRows().size() > 0) {
			
			table.clearRows();
			
			ContentTableRow row = searchTable.getSelectedRows().get(0);
			String tagId = (String) row.get("TAG_ID");
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_MEMBER_TAG"));
			parameterJSON.put("cotId", new JSONString(getCotId()));
			parameterJSON.put("tagId", new JSONString(tagId));
	
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
						if (usrCnt == 0) {
							getWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
						}

						for (int i=0; i<usrCnt; i++) {
							
							JSONObject recObj = resultArray.get(i).isObject();
							ContentTableRow tableRow = table.addRow( Color.WHITE,  getString(recObj, "TAG_NAME"));
							tableRow.put("TAG_ID", getString(recObj, "TAG_ID").replaceAll("#", ""));
							
						}
						
					}
				}

				private String getString(JSONObject recObj, String key) {
					if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
					else return "#"+recObj.get(key).isString().stringValue();
				}

			});
		}
	}

	private void searchTag(String keyword) {
		
		if(keyword.indexOf(" ") != -1) {
			MaterialToast.fireToast("태그에는 공백이 들어 갈 수 없습니다");
			return;
		}
		searchTable.clearRows();
		JSONObject informationJSON = (JSONObject)Registry.get(Registry.BASE_INFORMATION);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SEARCH_TAGS"));
		parameterJSON.put("keyword", new JSONString(keyword.trim()));
		parameterJSON.put("usrId", new JSONString(informationJSON.get("USR_ID").isString().stringValue()));

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
					if (usrCnt == 0) {
						getWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
					}

					for (int i=0; i<usrCnt; i++) {
						
						JSONObject recObj = resultArray.get(i).isObject();
						ContentTableRow tableRow = searchTable.addRow( Color.WHITE,  getString(recObj, "TAG_NAME"));
						tableRow.put("TAG_ID", getString(recObj, "TAG_ID").replaceAll("#", ""));
						
					}
					
					loading(false);
					
				}
			}

			private String getString(JSONObject recObj, String key) {
				if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
				else return "#"+recObj.get(key).isString().stringValue();
			}

		});		
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	public void loadData(ContentTableRow tableRow) {
		
		this.tableRow = tableRow;
		
		inputBox.setValue("");
		table.clearRows();
		searchTable.clearRows();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_LIST_OF_COTID"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

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
						ContentTableRow tableRow = table.addRow( Color.WHITE,  getString(recObj, "TAG_NAME"));
						tableRow.put("TAG_ID", getString(recObj, "TAG_ID").replaceAll("#", ""));
						
					}
					
					loading(false);
				}
			}

			private String getString(JSONObject recObj, String key) {
				if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
				else return "#"+recObj.get(key).isString().stringValue();
			}

		});

		parameterJSON.put("cmd", new JSONString("GET_MASTER_TAG"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
	
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
		
					
					JSONObject bodyObj =  resultObj.get("body").isObject().get("result").isObject();
					if (bodyObj.get("TAG_NAME") != null) {
						masterTag.setValue("#"+bodyObj.get("TAG_NAME").isString().stringValue());
					}else {
						masterTag.setValue("대표 태그 없음");
					}
					
					loading(false);
				}
			}

		});

	}

}
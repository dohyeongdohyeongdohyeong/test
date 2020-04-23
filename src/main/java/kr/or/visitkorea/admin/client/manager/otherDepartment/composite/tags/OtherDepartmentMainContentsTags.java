package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.tags;

import java.util.Collections;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
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
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentMainContentsTags extends AbtractOtherDepartmentMainContents {

	private MaterialTextBox inputBox;
	private ContentTable table;
	private MaterialIcon icon1;
	private ContentTable searchTable;
	private MaterialIcon icon2;
	private MaterialPanel enablePanel;
	private MaterialIcon icon111;
	private MaterialLabel masterTag;
	private String otdId;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private String manId;
	private boolean useMainTags;
	private boolean chk =true;
	public OtherDepartmentMainContentsTags(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		//loadData();
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("태그");
		buildContent();
	}
	
	private void setPermissionRole() {
		
		icon1.setEnabled(true); // 삭제
		icon2.setEnabled(true); // 등록
		inputBox.setEnabled(true); // 검색
		
		if (manId != null && manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전
			icon1.setEnabled(Registry.getPermission("bf87efcc-a587-4c2e-95d0-c206a9147361"));
			icon2.setEnabled(Registry.getPermission("85b64dfa-1f57-4deb-aad2-ad37c8dd2bfd"));
			inputBox.setEnabled(Registry.getPermission("59a47f08-55ad-46b2-9981-54b687b546ef"));
		} else if (manId != null && manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전
			icon1.setEnabled(Registry.getPermission("469b0631-2c7c-4ba5-9449-92172aa65e75"));
			icon2.setEnabled(Registry.getPermission("310ce4ab-67cc-409b-8ffa-e74c44379618"));
			inputBox.setEnabled(Registry.getPermission("f39b524c-2517-4bac-a1bc-f1af97e1920f"));
		}
		
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
		table.setWidth("300px");
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setTop(140);
		table.appendTitle("멤버 태그", 293, TextAlign.LEFT);

		this.add(table);

		icon1 = new MaterialIcon(IconType.DELETE);
		icon1.setTextAlign(TextAlign.CENTER);
		icon1.addClickHandler(event->{
			removeMemberTag();
		});

		table.getButtomMenu().addIcon(icon1, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
	
		searchTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		searchTable.setHeight(370);
		searchTable.setLayoutPosition(Position.ABSOLUTE);
		searchTable.setLeft(350);
		searchTable.setRight(30);
		searchTable.setTop(140);
		searchTable.appendTitle("검색 태그", 502, TextAlign.LEFT);
	
		this.add(searchTable);

		icon2 = new MaterialIcon(IconType.ADD);
		icon2.setTextAlign(TextAlign.CENTER);
		icon2.addClickHandler(event->{
			appendMemberTag();
		});

		searchTable.getButtomMenu().addIcon(icon2, "등록", com.google.gwt.dom.client.Style.Float.LEFT);
	
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
		
		
		MaterialIcon UpIcon = new MaterialIcon(IconType.ARROW_UPWARD);
		UpIcon.setTextAlign(TextAlign.CENTER);
		UpIcon.addClickHandler(event->{
			MemberTagUpDown(true);
		});
		table.getButtomMenu().addIcon(UpIcon, "위로", com.google.gwt.dom.client.Style.Float.RIGHT);
		UpIcon.setMarginLeft(0);
		UpIcon.setMarginRight(0);
		
		MaterialIcon DownIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		DownIcon.setTextAlign(TextAlign.CENTER);
		DownIcon.addClickHandler(event->{
			MemberTagUpDown(false);
		});
		table.getButtomMenu().addIcon(DownIcon, "아래로", com.google.gwt.dom.client.Style.Float.RIGHT);

		DownIcon.setMarginLeft(0);
		DownIcon.setMarginRight(0);
		
		
		
	}

	private void removeMemberTag() {
		
		if (table.getSelectedRows().size() > 0) {
			
			
			int nowIndex = table.getRowContainer().getWidgetIndex(table.getSelectedRows().get(0));
			String tagId =  table.getRowsList().get(nowIndex).get("TAG_ID").toString();

			table.clearRows();
			
			String cmd = "DEL_OTHER_DEPARTMENT_MEMBER_TAG";
			if (getUseMainTags()) {
				cmd = "DEL_SITE_MAIN_TAG";
			}
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString(cmd));
			if (manId() != null) {
				parameterJSON.put("manId", new JSONString(manId()));
			}
			parameterJSON.put("otdId", new JSONString(otdId()));
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

			ContentTableRow row = searchTable.getSelectedRows().get(0);
			MaterialLabel childLabel = (MaterialLabel)row.getChildrenList().get(0);
			String tgrText = childLabel.getText();
			int Rowsize = table.getRowsList().size();
			if (!table.checkDuplicate(tgrText, 0)) {
			
				table.clearRows();
				
				String tagId = (String) row.get("TAG_ID");
				
				String cmd = "INSERT_DEPARTMENT_MEMBER_TAG";
				if (getUseMainTags()) {
					cmd = "INSERT_SITE_MAIN_TAG";
				}
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString(cmd));
				if (manId() != null) {
					parameterJSON.put("manId", new JSONString(manId()));
				}
				
				parameterJSON.put("otdId", new JSONString(otdId()));
				parameterJSON.put("order", new JSONNumber(Rowsize));
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
			} else {
				
				
			}
		}
	}

	private String otdId() {
		this.otdId = (String) getWindow().getValueMap().get("OTD_ID");
		return this.otdId;
	}

	private String manId() {
		this.manId = (String) getWindow().getValueMap().get("MAN_ID");
		return this.manId;
	}

	private boolean getUseMainTags() {
		
		if (getWindow().getValueMap().get("USE_MAIN_TAGS") != null) {
			this.useMainTags = ((Boolean) getWindow().getValueMap().get("USE_MAIN_TAGS")).booleanValue();
			return this.useMainTags;
		} else {
			return false;
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

	public void loadData() {
		
		
		inputBox.setValue("");
		table.clearRows();
		searchTable.clearRows();

		String cmd = "GET_OTHER_DEPARTMENT_MEMBER_TAG";
		if (getUseMainTags()) {
			cmd = "GET_SITE_MAIN_TAG";
		}
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString(cmd)); 
		if (manId() != null) parameterJSON.put("manId", new JSONString(manId()));
		if (otdId() != null) parameterJSON.put("otdId", new JSONString(otdId()));

		Console.log("manid :: " + manId);
		Console.log("otdId :: " + otdId);
		//Console.log("useMainTags :: " + useMainTags());
		
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
					table.clearRows();
					
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
	}
	
	public void setManId(String MAN_ID) {
		this.manId = MAN_ID;
		this.setPermissionRole();
	}
	
	private void MemberTagUpDown(boolean order) {
		if(chk == false)
			return;
			chk = false;
		
		ContentTableRow IndexUpBefore = table.getSelectedRows().get(0);
		int TargetIndex = table.getRowContainer().getWidgetIndex(IndexUpBefore);
		int afterTargetIndex = TargetIndex;
		
		if(order) {
			if(TargetIndex != 0) {
				afterTargetIndex = TargetIndex-1;
				Collections.swap(table.getRowsList(),TargetIndex,TargetIndex-1);
			} else {
				MaterialToast.fireToast("더이상 올릴 수 없습니다.");
				chk = true;
				return;
			}
		} else {
			
			if(table.getRowsList().size() != TargetIndex+1) {
				Collections.swap(table.getRowsList(),TargetIndex,TargetIndex+1);
				afterTargetIndex = TargetIndex+1;
			} else {
				MaterialToast.fireToast("더이상 내릴 수 없습니다.");
				chk = true;
				return;
			}
		}
		
		String OrderString = "";
		for (int i = 0; i < table.getRowsList().size(); i++) {
			table.getRowsList().get(i).addClickHandler(event->{
				
			});
			
			String TagId = table.getRowsList().get(i).get("TAG_ID").toString();
			if( i == 0) {
				OrderString += ( i+ "_" + TagId);
			} else {
				OrderString += ( "," + i + "_" + TagId);
			}
		}
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("REORDER_DEPT_TAGS"));
		jObj.put("ORDER", new JSONString(OrderString));
		jObj.put("OTD_ID", new JSONString(otdId));

		String BeforeStatus = table.getRowContainer().getChildrenList().get(TargetIndex).getElement().getInnerText();
		String AfterStatus = table.getRowContainer().getChildrenList().get(afterTargetIndex).getElement().getInnerText();
		
		table.getRowContainer().getChildrenList().get(TargetIndex).getElement().getChild(0).getChild(0).getParentElement().setInnerText(AfterStatus);
		table.getRowContainer().getChildrenList().get(afterTargetIndex).getElement().getChild(0).getChild(0).getParentElement().setInnerText(BeforeStatus);
		
		
		
		executeBusiness(jObj);
		if(order)
			table.setSelectedIndex(TargetIndex - 1);
		else
			table.setSelectedIndex(TargetIndex + 1);
		chk = true;
	}
}
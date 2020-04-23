package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsTags extends AbtractContents {

	private MaterialTextBox inputBox;
	private ContentTable table;
	private MaterialIcon icon1;
	private ContentTable searchTable;
	private MaterialIcon icon2;
	private MaterialPanel enablePanel;
	private MaterialIcon icon111;
	private MaterialLabel masterTag;
	private MaterialIcon saveIcon;
	private MaterialIcon editIcon;
	private List<String> DeleteTagList;
	private List<HashMap<String, String>> AddTagList;
	private String MasterTag;
	private String BeforeMasterTag;
	
	public ContentsTags(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setTitle("태그");
		buildContent();
		SettingBaseIcon();
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
		searchTable.appendTitle("검색 태그", 702, TextAlign.LEFT);
	
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
			MasterTag = (String) row.get("TAG_ID");
			masterTag.setValue("#"+(String) row.get("TAG_NAME"));
	
		}			
	}

	private void removeMemberTag() {
		
		if (table.getSelectedRows().size() > 0) {
			
			ContentTableRow row = table.getSelectedRows().get(0);
			String tagId = (String) row.get("TAG_ID");
			
			table.getRowContainer().remove(row);
			
			int size = AddTagList.size();
			boolean chk = false;
			if(size > 0) {
				for (int i = 0; i < size; i++) {
					if(tagId == AddTagList.get(i).get("TAG_ID").toString()) {
						AddTagList.remove(i);
						chk = true;
					} 
					if(i == size-1) {
						if(chk == false) 
							DeleteTagList.add(tagId);
					}
				}
			} else
				DeleteTagList.add(tagId);
			
			if(tagId == MasterTag) {
				MasterTag = "";
				masterTag.setValue("대표 태그 없음");
			}
			
		}		
	}

	private void appendMemberTag() {
		
		if(searchTable.getSelectedRows().size() == 0) {
			return;
		}
		
		
		
		
		ContentTableRow row = searchTable.getSelectedRows().get(0);
		String tagId = (String) row.get("TAG_ID");
		String tagName = (String) row.get("TAG_NAME");
		
		for (int i = 0; i < table.getRowsList().size(); i++) {
			if(tagId == table.getRowsList().get(i).get("TAG_ID")) {
				MaterialToast.fireToast("이미 추가된 태그입니다.");
				return;
			}
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TAG_ID", tagId);
		map.put("TAG_NAME", tagName);
		if(row.get("CREATE") != null)
			map.put("USR_ID", Registry.getUserId());
		AddTagList.add(map);
		
		ContentTableRow tableRow = table.addRow( Color.WHITE, "#"+tagName);
		tableRow.put("TAG_ID", tagId);
		tableRow.put("TAG_NAME", tagName);
		
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
		parameterJSON.put("mode", new JSONString("DATABASE"));
		parameterJSON.put("usrId", new JSONString(informationJSON.get("USR_ID").isString().stringValue()));

		//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
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
						tableRow.put("TAG_NAME", getString(recObj, "TAG_NAME").replaceAll("#", ""));
					}
					
					loading(false);
					
				} else if(processResult.equals("notTag")) {
					ContentTableRow tableRow = searchTable.addRow( Color.WHITE, "#"+keyword.trim());
					tableRow.put("TAG_ID", IDUtil.uuid());
					tableRow.put("TAG_NAME", keyword.trim());
					tableRow.put("CREATE", "Y");
					
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
		SettingVisible(false);
		
		DeleteTagList = new ArrayList<String>();
		AddTagList =  new ArrayList<HashMap<String, String>>();
		searchTable.getSelectedRows().clear();
		table.getSelectedRows().clear();
		inputBox.setValue("");
		table.clearRows();
		searchTable.clearRows();
		BeforeMasterTag = "";
		
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_LIST_OF_COTID"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
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
						tableRow.put("TAG_NAME", getString(recObj, "TAG_NAME").replaceAll("#", ""));
						
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

		//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
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
						MasterTag = bodyObj.get("TAG_NAME") != null ? bodyObj.get("TAG_NAME").isString().stringValue() : "";
						BeforeMasterTag = bodyObj.get("TAG_NAME") != null ? bodyObj.get("TAG_NAME").isString().stringValue() : "";
					}else {
						masterTag.setValue("대표 태그 없음");
						MasterTag = "";
						BeforeMasterTag = "";
					}
					
					loading(false);
				}
			}

		});

	}

	public void SettingBaseIcon() {
		saveIcon = this.showSaveIconAndGetIcon();
		editIcon = this.showEditIconAndGetIcon();
		saveIcon.setTooltip("컨텐츠 수정 해제");
		
		SettingVisible(false);
		
		saveIcon.addClickHandler(event->{
			UpdateCheck();
		});
		
		editIcon.addClickHandler(event->{
			SettingVisible(true);
		});
	}
	
	public void SettingVisible(Boolean Visible) {
			
		editIcon.setVisible(!Visible);
		saveIcon.setVisible(Visible);
		inputBox.setEnabled(Visible);
		icon1.setEnabled(Visible);
		table.setEnabled(Visible);
		icon111.setEnabled(Visible);
		icon2.setEnabled(Visible);
		inputBox.setEnabled(Visible);
		searchTable.setEnabled(Visible);
	}

	public void UpdateCheck() {
		
		if(AddTagList.size() > 0 
			|| DeleteTagList.size() > 0
			|| MasterTag != BeforeMasterTag) {
	
			getWindow().confirm("내용이 변경되었습니다. 실제 데이터에 반영하시겠습니까?", event->{
				if (event.getSource().toString().contains("yes")) {
					SaveBase();
				}
			});
			
		} else
			SettingVisible(false);
	
	}
	public void SaveBase() {
		
		JSONArray AddArray = new JSONArray();
		for (int i = 0; i < AddTagList.size(); i++) {
			JSONObject AddObject = new JSONObject();
			AddObject.put("TAG_ID", new JSONString(AddTagList.get(i).get("TAG_ID")));
			AddObject.put("TAG_NAME", new JSONString(AddTagList.get(i).get("TAG_NAME")));
			if(AddTagList.get(i).containsKey("USR_ID"))
				AddObject.put("USR_ID", new JSONString(AddTagList.get(i).get("USR_ID")));
			AddArray.set(i, AddObject);
		}
		JSONArray DeleteArray = new JSONArray();
		for (int i = 0; i < DeleteTagList.size(); i++) {
			DeleteArray.set(i, new JSONString(DeleteTagList.get(i)));
		}
		JSONObject paramObj = new JSONObject();
		paramObj.put("cmd", new JSONString("UPDATE_DATABASE_MASTER"));
		paramObj.put("mode", new JSONString("Tag"));
		paramObj.put("cotId", new JSONString(getCotId()));
		paramObj.put("MasterTag", new JSONString(MasterTag));
		if(AddTagList.size() > 0)
			paramObj.put("AddList", AddArray);
		if(DeleteTagList.size() > 0)
			paramObj.put("DeleteList", DeleteArray);
		VisitKoreaBusiness.post("call", paramObj.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if(processResult.equals("success")){
					MaterialToast.fireToast("내용 변경에 성공하였습니다.");
					SettingVisible(false);
					AddTagList.clear();
					DeleteTagList.clear();
					BeforeMasterTag = MasterTag;
				}
			}
		});
		
	}

}
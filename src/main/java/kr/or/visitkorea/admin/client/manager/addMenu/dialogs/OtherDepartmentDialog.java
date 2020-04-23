package kr.or.visitkorea.admin.client.manager.addMenu.dialogs;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexWrap;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.composite.MaterialContentTreeItem;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuApplication;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuMain;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentDialog extends DialogContent {

	private MaterialLabel dialogTitle,
						  serviceNameLbl,
						  useMenuLbl;
	private MaterialTextBox serviceName;
	private MaterialComboBox<String> mainType;
	private MaterialCheckBox newsChk,
							 dbChk;
	private String otdId;
	
	private JSONArray otdMenuList;	//	0.서비스 1. 영역별 2. 기사 3. DB
	private MaterialContentTreeItem selectedTreeItem;
	
	public OtherDepartmentDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public int getHeight() {
		return 330;
	}
	
	@Override
	protected void onLoad() {
		selectedTreeItem = (MaterialContentTreeItem) this.getParameters().get("selectedItem");
		
		if (this.getParameters().get("mode").equals("add")) {
			otdId = IDUtil.uuid();

			serviceName.setValue("");
			newsChk.setValue(false);
			dbChk.setValue(false);
			
		} else if (this.getParameters().get("mode").equals("modify")) {
			JSONObject obj = (JSONObject) selectedTreeItem.get("obj");
			JSONValue menu = obj.get("menu");
			JSONValue parameter = null;
			String otdId = null;
			
			if (menu instanceof JSONObject) {
				parameter = obj.get("menu").isObject().get("parameters").isObject().get("value");
			} else if (menu instanceof JSONArray) {
				parameter = obj.get("menu").isArray().get(0).isObject().get("parameters").isObject().get("value");
			}
			
			if (parameter instanceof JSONObject) {
				otdId = parameter.isObject().get("content").isString().stringValue();
			} else if (parameter instanceof JSONArray) {
				otdId = parameter.isArray().get(0).isObject().get("content").isString().stringValue();
			}
			
			//	추가된 메뉴일 때
			if (selectedTreeItem.get("addedItemInfo") != null) {
				JSONObject addedObj = (JSONObject) selectedTreeItem.get("addedItemInfo");
				setupForm(addedObj);
				
			//	기존에 있던 메뉴를 수정한 이력이 있는 경우
			} else if (selectedTreeItem.get("isModify") != null) {
				JSONObject modifiedObj = (JSONObject) selectedTreeItem.get("modifiedMenuInfo");
				setupForm(modifiedObj);
				
			} else {
				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("SELECT_OTHER_DEPARTMENT"));
				paramJson.put("OTD_ID", new JSONString(otdId));
				
				VisitKoreaBusiness.post("call", paramJson.toString(), (o1, o2, o3) -> {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) o1));
					String result = resultObj.get("header").isObject().get("process").isString().stringValue();
					
					if (result.equals("success")) {
						JSONObject resultJson = resultObj.get("body").isObject().get("result").isObject();
						setupForm(resultJson);
						
					} else {
						String[] msg = {"해당 메뉴의 정보를 불러오는 중, 알 수 없는 오류가 발생하였습니다."};
						alert("오류 발생", 300, 200, msg);
					}
				});
			}
		}
		mainType.setVisibility(Visibility.VISIBLE);
	}
	
	//	수정모드일때 값 셋팅
	public void setupForm(JSONObject obj) {
		otdId = obj.get("OTD_ID").isString().stringValue();
		
		String title = obj.get("TITLE").isString().stringValue();
		serviceName.setValue(title);
		
		if (obj.get("USE_ARTICLE_CONTENTS") instanceof JSONBoolean) {
			newsChk.setValue(obj.get("USE_ARTICLE_CONTENTS").isBoolean().booleanValue());
			dbChk.setValue(obj.get("USE_DB_CONTENTS").isBoolean().booleanValue());
			
		} else {
			int useArticle = (int) obj.get("USE_ARTICLE_CONTENTS").isNumber().doubleValue();
			newsChk.setValue(useArticle == 1 ? true : false);
			int useDB = (int) obj.get("USE_DB_CONTENTS").isNumber().doubleValue();
			dbChk.setValue(useDB == 1 ? true : false);
		}

		String templetType = obj.get("TEMPLATE_TYPE").isString().stringValue();
		mainType.setSelectedIndex(getMainTypeNumber(templetType));
	}

	//	수정 처리
	public void modifyAction() {
		selectedTreeItem.setText(serviceName.getValue());
		
		JSONObject obj = (JSONObject) selectedTreeItem.get("obj");

		JSONObject modifiedMenuInfo = buildMenuInfo();
		
		//	추가된 내용 수정할 때
		if (selectedTreeItem.get("addedItemInfo") != null) {
			selectedTreeItem.put("addedItemInfo", buildMenuInfo());
		}
		
		boolean[] selected = {true, true, newsChk.getValue(), dbChk.getValue()};
		selectedTreeItem.put("isModify", "true");
		selectedTreeItem.put("modifiedMenuInfo", modifiedMenuInfo);
		selectedTreeItem.getTreeItems().forEach(t -> {
			t.removeFromTree();
		});
		obj.put("caption", new JSONString(serviceName.getValue()));
		obj.put("menu", otdMenuObj(otdId, selectedTreeItem, selected));
				
		if (AddMenuMain.modifiedOtdMenus == null) {
			AddMenuMain.modifiedOtdMenus = modifiedMenuInfo;
			
		} else if (AddMenuMain.modifiedOtdMenus instanceof JSONObject) {
			JSONArray newArr = new JSONArray();
			newArr.set(0, AddMenuMain.modifiedOtdMenus);
			newArr.set(1, modifiedMenuInfo);
			
			AddMenuMain.modifiedOtdMenus = newArr;
		} else {
			AddMenuMain.modifiedOtdMenus.isArray().set(AddMenuMain.modifiedOtdMenus.isArray().size(), modifiedMenuInfo);
			
		}
		selectedTreeItem.select();
	}
	
	// 추가 처리
	public void addAction() {
		JSONObject otdObj = (JSONObject) selectedTreeItem.get("obj");
		JSONArray otdObjMenus = otdObj.get("menu").isArray();

		JSONObject newMenuInfo = buildMenuInfo();
		JSONObject newMenuObj = menuObj(serviceName.getValue(), newMenuInfo);
		
		otdObjMenus.set(otdObjMenus.size(), newMenuObj);
		
		if (AddMenuMain.newOtdMenus == null) {
			AddMenuMain.newOtdMenus = newMenuInfo;
			
		} else if (AddMenuMain.newOtdMenus instanceof JSONObject) {
			JSONArray newArr = new JSONArray();
			newArr.set(0, AddMenuMain.newOtdMenus);
			newArr.set(1, newMenuInfo);
			
			AddMenuMain.newOtdMenus = newArr;
		} else {
			AddMenuMain.newOtdMenus.isArray().set(AddMenuMain.newOtdMenus.isArray().size(), newMenuInfo);
			
		}
	}

	public JSONObject buildMenuInfo() {
		JSONObject info = new JSONObject();
		info.put("OTD_ID", new JSONString(otdId));
		info.put("TITLE", new JSONString(serviceName.getValue()));
		info.put("USE_ARTICLE_CONTENTS", JSONBoolean.getInstance(newsChk.getValue()));
		info.put("USE_DB_CONTENTS", JSONBoolean.getInstance(dbChk.getValue()));
		info.put("TEMPLATE_TYPE", new JSONString(getMainTypeString(mainType.getSelectedIndex())));
		return info;
	}
	
	//	메뉴
	public JSONObject menuObj(String serviceName, JSONObject newMenuInfo) {
		JSONObject obj = new JSONObject();
		obj.put("icon", new JSONString("CHECK_BOX_OUTLINE_BLANK"));
		obj.put("caption", new JSONString(serviceName));
		obj.put("permission", permissionObj());

		MaterialContentTreeItem newService = new MaterialContentTreeItem(
				serviceName, IconType.CHECK_BOX_OUTLINE_BLANK);
		newService.put("obj", obj);
		newService.put("icon", obj.get("icon").isString().stringValue());
		newService.put("caption", obj.get("caption").isString().stringValue());
		newService.put("addedItemInfo", newMenuInfo);
		
		JSONObject otdObj = (JSONObject) selectedTreeItem.get("obj");
		JSONArray otdObjMenus = otdObj.get("menu").isArray();
		newService.put("index", AddMenuApplication.OTHER_DEPARTMENT_MENU_INDEX + "-" + otdObjMenus.size());
		
		selectedTreeItem.add(newService);
		newService.select();
		
		boolean[] selected = {true, true, newsChk.getValue(), dbChk.getValue()};
		obj.put("menu", otdMenuObj(otdId, newService, selected));
		
		return obj;
	}
	
	//	메뉴 권한
	public JSONObject permissionObj() {
		JSONObject obj = new JSONObject();
		obj.put("caption", new JSONString("사용여부"));
		obj.put("id", new JSONString("use"));
		obj.put("uuid", new JSONString(IDUtil.uuid()));
		obj.put("content", JSONBoolean.getInstance(false));
		
		return obj;
	}
	
	//	사용메뉴
	public JSONValue otdMenuObj(String otdId, MaterialContentTreeItem newService, boolean ...args) {
		JSONValue menu = null;
		if (args.length == 1) {
			if (args[0]) {
				menu = otdMenuAppend(0, otdId, newService);
			}
			
		} else {
			menu = new JSONArray();
			
			for (int i = 0; i < args.length; i++) {
				if (args[i]) {
					menu.isArray().set(menu.isArray().size(), otdMenuAppend(i, otdId, newService));
				}
			}
		}
		return menu;
	}
	
	//	사용메뉴 Build
	public JSONObject otdMenuAppend(int type, String otdId, MaterialContentTreeItem newService) {
		JSONObject obj = JSONParser.parseStrict(otdMenuList.get(type).isObject().toString()).isObject();

		JSONObject permissionobj = obj.get("permission").isObject();
		permissionobj.put("uuid", new JSONString(IDUtil.uuid()));

		JSONValue parameterValue = obj.get("parameters").isObject().get("value");
		
		if (parameterValue instanceof JSONObject) {
			parameterValue.isObject().put("content", new JSONString(otdId));
			
		} else if (parameterValue instanceof JSONArray) {
			parameterValue.isArray().get(0).isObject().put("content", new JSONString(otdId));
		}

		MaterialContentTreeItem item = new MaterialContentTreeItem(
				obj.get("caption").isString().stringValue(), IconType.valueOf(obj.get("icon").isString().stringValue()));
		item.put("icon", obj.get("icon").isString().stringValue());
		item.put("caption", obj.get("caption").isString().stringValue());
		item.put("obj", obj);
		
		JSONObject otdObj = (JSONObject) selectedTreeItem.get("obj");
		JSONArray otdObjMenus = otdObj.get("menu").isArray();
		item.put("index", AddMenuApplication.OTHER_DEPARTMENT_MENU_INDEX + "-" + otdObjMenus.size() + "-" + 1);
		
		newService.add(item);
		return obj;
	}

	public String getMainTypeString(int selectedMainType) {
		switch (selectedMainType) {
			case 0:	return "A";
			case 1:	return "B";
			case 2:	return "C";
			case 3:	return "D";
			case 4:	return "MA";
			default: return "";
		}
	}
	
	public int getMainTypeNumber(String mainType) {
		switch (mainType) {
			case "A":	return 0;
			case "B":	return 1;
			case "C":	return 2;
			case "D":	return 3;
			case "MA":	return 4;
			default: return -1;
		}
	}
	
	//	입력값 검증
	public String validation() {
 		String numCheck = serviceName.getValue().replaceAll("[^0-9]", "");
		
		if (serviceName.getValue().equals("")) {
			return "서비스명을 입력해주세요.";
		}
		if (AddMenuApplication.valueValidation(serviceName.getValue())) {
			return "< 또는 > 문자는 포함될 수 없습니다.";
		}
		if (numCheck.equals(serviceName.getValue())) 
			return "서비스명은 정수로 입력할 수 없습니다.";
				
		return "pass";
	}
	
	//	PARAMETER XML 불러오기
	public void loadXML() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("LOAD_PARAMETER_FILE"));
		VisitKoreaBusiness.post("call", paramJson.toString(), (o1, o2, o3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) o1));
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject obj = resultObj.get("body").isObject().get("resultJson").isObject();
				otdMenuList = obj.get("parameter").isObject().get("menu").isArray();
			}
		});
	}

	@Override
	public void init() {
		loadXML();
		
		dialogTitle = new MaterialLabel("부서 서비스 추가하기");
		dialogTitle.setWidth("100%");
		dialogTitle.setPadding(20);
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);

		serviceNameLbl = addLabel("25%", "서비스명", 10, 10);
		serviceName = addTextBox("65%", "서비스명", 10);
		
		MaterialRow row1 = addRow(10, 5);
		row1.add(serviceNameLbl);
		row1.add(serviceName);
		row1.setHeight("55px");

		MaterialLabel mainTypeLbl = addLabel("25%", "영역별 컨텐츠 타입", 10, 10);
		mainType = addMainTypeComboBox("45%");
		mainType.setDisplay(Display.INLINE_BLOCK);
		
		MaterialRow row11 = addRow(10, 10);
		row11.add(mainTypeLbl);
		row11.add(mainType);
		
		useMenuLbl = addLabel("25%", "사용메뉴", 10, 10);
		newsChk = addCheckBox("45%", "기사 컨텐츠");
		dbChk = addCheckBox("45%", "DB 컨텐츠");
		
		MaterialRow row2 = addRow(10, 10);
		row2.setDisplay(Display.FLEX);
		
		MaterialRow menuRow = addRow(0, 0);
		menuRow.setDisplay(Display.FLEX);
		menuRow.setFlexWrap(FlexWrap.WRAP);
		menuRow.setFlexAlignItems(FlexAlignItems.CENTER);
		menuRow.setWidth("70%");
		
		row2.add(useMenuLbl);
		row2.add(menuRow);
		menuRow.add(newsChk);
		menuRow.add(dbChk);
		
		MaterialRow row4 = addRow(10, 10);
		
		MaterialButton submitBtn = new MaterialButton();
		submitBtn.setText("확인");
		submitBtn.setFloat(Float.RIGHT);
		submitBtn.addClickHandler(e -> {
			String validation = validation();
			if (!validation.equals("pass")) {
				String[] msg = {validation};
				alert("알림", 400, 200, msg);
				return;
			}
			
			if (this.getParameters().get("mode").equals("add")) {
				addAction();
			} else if (this.getParameters().get("mode").equals("modify")) {
				modifyAction();
			}
			
			getMaterialExtentsWindow().closeDialog();
		});

		MaterialButton cancelBtn = new MaterialButton();
		cancelBtn.setText("취소");
		cancelBtn.setFloat(Float.RIGHT);
		cancelBtn.setMarginRight(10);
		cancelBtn.setMarginLeft(10);
		cancelBtn.addClickHandler(e -> {
			getMaterialExtentsWindow().closeDialog();
		});
		
		row4.add(cancelBtn);
		row4.add(submitBtn);
		
		this.add(dialogTitle);
		this.add(row1);
		this.add(row11);
		this.add(row2);
		this.add(row4);
	}
	
	public MaterialRow addRow(int marginTop, int marginBottom) {
		MaterialRow row = new MaterialRow();
		row.setMarginTop(marginTop);
		row.setMarginBottom(marginBottom);
		row.setWidth("100%");
		return row;
	}
	
	public MaterialLabel addLabel(String width, String title, double leftMargin, double rightMargin) {
		MaterialLabel lb = new MaterialLabel(title);
		lb.setFontWeight(FontWeight.BOLD);
		lb.setTextAlign(TextAlign.CENTER);
		lb.setWidth(width);
		lb.setLineHeight(45);
		lb.setDisplay(Display.INLINE_BLOCK);
		lb.setMarginLeft(leftMargin);
		lb.setMarginRight(rightMargin);
		return lb;
	}

	public MaterialTextBox addTextBox(String width, String placeHolder, double rightMargin) {
		MaterialTextBox tb = new MaterialTextBox();
		tb.setLabel(placeHolder);
		tb.setHeight("45px");
		tb.setWidth(width);
		tb.setDisplay(Display.INLINE_BLOCK);
		tb.setPadding(0);
		tb.setMargin(0);
		tb.setMarginRight(rightMargin);
		return tb;
	}
	
	public MaterialComboBox<String> addMainTypeComboBox(String width) {
		MaterialComboBox<String> combo = new MaterialComboBox<String>();
		combo.addItem("A", "A");
		combo.addItem("B", "B");
		combo.addItem("C", "C");
		combo.addItem("D", "D");
		combo.addItem("MA", "MA");
		combo.setWidth(width);
		combo.setMargin(0);
		combo.setPadding(0);
		return combo;
	}
	
	public MaterialCheckBox addCheckBox(String width, String label) {
		MaterialCheckBox checkbox = new MaterialCheckBox();
		checkbox.setName("mainType");
		checkbox.setText(label);
		checkbox.setWidth(width);
		return checkbox;
	}
}

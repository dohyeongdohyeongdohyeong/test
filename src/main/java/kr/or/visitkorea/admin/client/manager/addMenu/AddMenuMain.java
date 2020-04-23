package kr.or.visitkorea.admin.client.manager.addMenu;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.composite.MaterialContentTreeItem;
import kr.or.visitkorea.admin.client.manager.addMenu.panel.ButtonPanel;
import kr.or.visitkorea.admin.client.manager.addMenu.panel.ContentsPanel;
import kr.or.visitkorea.admin.client.manager.addMenu.panel.MenuTreePanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AddMenuMain extends AbstractContentPanel {
	private AddMenuApplication ama;
	private MaterialRow contentRow, btnRow;
	private MaterialColumn leftContent, rightContent;
	private MaterialInput title, boundsX, boundsY;
	private MaterialComboBox<String> app;
	private MaterialIcon icon;
	
	private MaterialTree paramsTree;
	private MaterialContentTreeItem rootParam;
	private ContentTable permisTable;
	
	private static int[] menuArrIndex;
	
	private MaterialIcon iconModifyBtn;
	private MaterialIcon paramsModifyBtn, paramsAddBtn, paramsRemoveBtn;
	private MaterialIcon permisModifyBtn, permisAddBtn, permisRemoveBtn;

	private JSONArray applicationList;
	public static JSONArray menus;
	public static MaterialContentTreeItem rootMenu;
	public static MaterialContentTreeItem selectedMenuItem;
	public static JSONValue newOtdMenus = null;
	public static JSONValue modifiedOtdMenus = null;
	
	public AddMenuMain(MaterialExtentsWindow meWindow, AddMenuApplication ama) {
		super(meWindow);
		this.ama = ama;
	}

	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		loadXml();
		
		btnRow = new ButtonPanel(this);
		contentRow = new ContentsPanel(this);
		leftContent = new MenuTreePanel(this);
		
		contentRow.add(leftContent);
		
		this.add(btnRow);
		this.add(contentRow);

		buildRightLayout();
	}
	
	//	우측 레이아웃 구성
	public void buildRightLayout() {
		//	우측 Column
		rightContent = new MaterialColumn();
		rightContent.setGrid("s9");
		rightContent.setHeight("590px");
		rightContent.setPaddingRight(20);
		rightContent.setPaddingLeft(10);
		rightContent.setBorder("1px solid #e0e0e0");
		rightContent.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		buildDetailForm();
		contentRow.add(rightContent);
	}	
	
	public void buildDetailForm() {
		MaterialRow row = addRow(10, 0);
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid("s12");
		row.add(column);
		
		MaterialRow row1 = addRow(10, 0);
		MaterialLabel detailTitle = addTitle("- 기본정보", 0);
		
		MaterialRow row2 = addRow(3, 0);
		MaterialLabel titleLabel = addLabel("18%", "메뉴명", 15);
		MaterialLabel iconLabel = addLabel("18%", "아이콘", 15);
		
		MaterialColumn iconColumn = new MaterialColumn();
		iconColumn.setWidth("20%");
		iconColumn.setHeight("45px");
		iconColumn.setDisplay(Display.FLEX);
		iconColumn.setFlexAlignItems(FlexAlignItems.CENTER);
		iconColumn.setFlexJustifyContent(FlexJustifyContent.START);
		
		title = addTextBox("30%", "메뉴명", 15);
		title.addBlurHandler(e -> {
			if (!valueValidation(title.getValue())) {
				JSONObject selectedMenuObj = (JSONObject) selectedMenuItem.get("obj");
				selectedMenuObj.put("caption", new JSONString(title.getValue()));
				selectedMenuItem.put("caption", new JSONString(title.getValue()));
				
			} else {
				title.setFocus(true);
				MaterialToast.fireToast("< 또는 > 문자는 포함될 수 없습니다.");
			}
		});
		title.addKeyPressHandler(e -> {
			if (e.getCharCode() == '<' || e.getCharCode() == '>') {
				e.preventDefault();
				MaterialToast.fireToast("< 또는 > 문자는 포함될 수 없습니다.");
			}
		});
		
		icon = new MaterialIcon();
		icon.setTextColor(Color.BLUE);
		icon.setFontSize("40px");
		icon.setMarginRight(8);
		
		iconModifyBtn = new MaterialIcon(IconType.EDIT);
		iconModifyBtn.setFontSize("18px");
		iconModifyBtn.addClickHandler(e -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("selectedMenuItem", selectedMenuItem);
			getMaterialExtentsWindow().openDialog(AddMenuApplication.MODIFY_ICON_DIALOG, params, 800);
		});
		iconColumn.add(icon);
		iconColumn.add(iconModifyBtn);
		
		MaterialRow row3 = addRow(3, 0);
		MaterialLabel appLabel = addLabel("18%", "app", 15);
		MaterialLabel boundsLabel = addLabel("18%", "bounds", 15);
		
		app = new MaterialComboBox<String>();
		app.setFloat(Float.LEFT);
		app.setWidth("30%");
		app.setMargin(0);
		app.setMarginRight(15);
		app.addItem("없음", "NULL");
		app.addSelectionHandler(e -> {
			String selectedValue = app.getValues().get(app.getSelectedIndex());
			JSONObject selectedMenuObj = (JSONObject) selectedMenuItem.get("obj");
			if (selectedValue.equals("NULL")) {
				if (selectedMenuObj.get("app") != null) {
					selectedMenuObj.put("app", null);
					selectedMenuItem.put("app", null);
				}
			} else {
				selectedMenuObj.put("app", new JSONString(selectedValue));
				selectedMenuItem.put("app", new JSONString(selectedValue));
			}
		});
		
		boundsX = addTextBox("12%", "X", 10);
		boundsX.addBlurHandler(e -> {
			if (!valueValidation(boundsX.getValue())) {
				JSONObject selectedMenuObj = (JSONObject) selectedMenuItem.get("obj");
				JSONObject selectedMenuBoundX = selectedMenuObj.get("bounds").isObject();
				selectedMenuBoundX.put("width", new JSONString(boundsX.getValue()));
			}
		});
		boundsX.addKeyPressHandler(e -> {
			if (e.getCharCode() == '<' || e.getCharCode() == '>') {
				e.preventDefault();
				MaterialToast.fireToast("< 또는 > 문자는 포함될 수 없습니다.");
			}
		});
		
		boundsY = addTextBox("12%", "Y", 15);
		boundsY.addBlurHandler(e -> {
			if (!valueValidation(boundsY.getValue())) {
				JSONObject selectedMenuObj = (JSONObject) selectedMenuItem.get("obj");
				JSONObject selectedMenuBoundX = selectedMenuObj.get("bounds").isObject();
				selectedMenuBoundX.put("height", new JSONString(boundsY.getValue()));
			}
		});
		boundsY.addKeyPressHandler(e -> {
			if (e.getCharCode() == '<' || e.getCharCode() == '>') {
				e.preventDefault();
				MaterialToast.fireToast("< 또는 > 문자는 포함될 수 없습니다.");
			}
		});
		
		MaterialRow row4 = addRow(6, 0);
		buildPermisTable(row4);
		buildParamsTree(row4);
		
		row1.add(detailTitle);
		
		row2.add(titleLabel);
		row2.add(title);
		row2.add(iconLabel);
		row2.add(iconColumn);
		
		row3.add(appLabel);
		row3.add(app);
		row3.add(boundsLabel);
		row3.add(boundsX);
		row3.add(boundsY);
		
		column.add(row1);
		column.add(row2);
		column.add(row3);
		rightContent.add(row);
		rightContent.add(row4);
	}

	public void buildPermisTable(MaterialRow row) {
		MaterialColumn column = new MaterialColumn();
		column.setGrid("s3");
		
		MaterialLabel permissionTitle = addTitle("- 권한", 0);
		permissionTitle.setFloat(Float.LEFT);
		
		permisModifyBtn = new MaterialIcon(IconType.EDIT);
		permisModifyBtn.addClickHandler(e -> {
			if (permisTable.getSelectedRows().size() != 0) {
				ContentTableRow selectedRow = permisTable.getSelectedRows().get(0);
				
				if (selectedRow.get("caption").equals("사용여부")) {
					getMaterialExtentsWindow().alert("사용여부 권한은 수정할 수 없습니다.");
					return;
				}
				HashMap<String, Object> params = new HashMap<>();
				params.put("mode", "modify");
				params.put("caption", (String) selectedRow.get("caption"));
				params.put("selectedRow", selectedRow);
				
				getMaterialExtentsWindow().openDialog(AddMenuApplication.PERMISSON_DIALOG, params, 450);
				
			} else {
				getMaterialExtentsWindow().alert("수정할 권한을 선택해주세요");
			}
		});
		permisAddBtn = new MaterialIcon(IconType.ADD);
		permisAddBtn.addClickHandler(e -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("mode", "add");
			params.put("table", permisTable);
			params.put("selectedMenuItem", selectedMenuItem);
			getMaterialExtentsWindow().openDialog(AddMenuApplication.PERMISSON_DIALOG, params, 450);
		});
		
		permisRemoveBtn = new MaterialIcon(IconType.REMOVE);
		permisRemoveBtn.addClickHandler(e -> {
			if (permisTable.getSelectedRows().size() == 0) {
				getMaterialExtentsWindow().alert("선택된 권한이 없습니다.");
			} else {
				ContentTableRow selectedRow = permisTable.getSelectedRows().get(0);
				String selectedCaption = selectedRow.get("caption").toString();
				
				if (selectedCaption.equals("사용여부")) {
					getMaterialExtentsWindow().alert("사용여부 권한은 삭제할 수 없습니다.");
					
				} else {
					getMaterialExtentsWindow().confirm("Permission 삭제 경고", selectedCaption + "을(를) 삭제하시겠습니까?", 500, event -> {
						if (event.getSource().toString().contains("yes")) {
							selectedRow.setDisplay(Display.NONE);
							
							JSONObject obj = (JSONObject) selectedMenuItem.get("obj");
							JSONArray arr = obj.get("permission").isArray();
							
							JSONArray newArr = new JSONArray();
							
							for (int i = 0; i < arr.size(); i++) {
								JSONObject o = arr.get(i).isObject();
								String caption = o.get("caption").isString().stringValue();
								if (!selectedCaption.equals(caption)) {
									newArr.set(newArr.size(), o);
								}
							}
							obj.put("permission", newArr);
							
							permisTable.getSelectedRows().remove(0);
							getMaterialExtentsWindow().alert(selectedCaption + "을(를) 삭제했습니다.");
						} else if (event.getSource().toString().contains("no"))  {
						} else {
							getMaterialExtentsWindow().alert(selectedCaption + "을(를) 삭제하는데 실패했습니다.");
						}
					});
				}
			}
		});
		
		permisTable = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		permisTable.setHeight(396);
		permisTable.appendTitle("caption", 245, TextAlign.CENTER);
		permisTable.getTopMenu().add(permissionTitle);
		permisTable.getTopMenu().addIcon(permisRemoveBtn, "삭제", Float.RIGHT, "20px", false);
		permisTable.getTopMenu().addIcon(permisAddBtn, "추가", Float.RIGHT, "20px", false);
		permisTable.getTopMenu().addIcon(permisModifyBtn, "수정", Float.RIGHT, "20px", false);
		
		column.add(permisTable);
		row.add(column);
	}

	public void buildParamsTree(MaterialRow row) {
		MaterialColumn column = new MaterialColumn();
		column.setGrid("s9");
		
		MaterialRow headerRow = addRow(0, 0);
		
		MaterialLabel paramsTitle = addTitle("- 파라미터", 0);
		paramsTitle.setFloat(Float.LEFT);
		
		paramsModifyBtn = new MaterialIcon(IconType.EDIT);
		paramsModifyBtn.setTooltip("수정");
		paramsModifyBtn.setFloat(Float.RIGHT);
		paramsModifyBtn.setFontSize("20px");
		paramsModifyBtn.setMarginLeft(10);
		paramsModifyBtn.addClickHandler(e -> {
			if (paramsTree.getSelectedItem() != null) {
				MaterialContentTreeItem selectedItem = (MaterialContentTreeItem) paramsTree.getSelectedItem();
				
				if (selectedItem.get("index") != null && selectedItem.get("index").equals("ROOT")) {
					getMaterialExtentsWindow().alert("ROOT 파라미터는 수정할 수 없습니다.");
						
				} else {
					JSONObject obj = (JSONObject) selectedItem.get("obj");
					
					String id = obj.get("id").isString().stringValue();
					String type = obj.get("type").isString().stringValue();
					
					HashMap<String, Object> params = new HashMap<>();
					params.put("mode", "modify");
					params.put("id", id);
					params.put("type", type);
					params.put("selectedItem", selectedItem);
					
					if (!type.equals("MAP")) {
						params.put("value", obj.get("content"));
					}
					getMaterialExtentsWindow().openDialog(AddMenuApplication.PARAMETER_DIALOG, params, 450);
				}
			} else {
				getMaterialExtentsWindow().alert("수정할 파라미터를 선택해주세요");
			}
		});
		
		paramsAddBtn = new MaterialIcon(IconType.ADD);
		paramsAddBtn.setTooltip("추가");
		paramsAddBtn.setFloat(Float.RIGHT);
		paramsAddBtn.setFontSize("20px");
		paramsAddBtn.setMarginLeft(10);
		paramsAddBtn.addClickHandler(e -> {
			MaterialContentTreeItem parentItem = (MaterialContentTreeItem) paramsTree.getSelectedItem();
			if (parentItem != null) {
				if (parentItem.get("index").equals("ROOT") | parentItem.get("index").equals("MAP")) {
					HashMap<String, Object> params = new HashMap<>();
					params.put("mode", "add");
					params.put("parentItem", parentItem);
					params.put("selectedMenuItem", selectedMenuItem);
					getMaterialExtentsWindow().openDialog(AddMenuApplication.PARAMETER_DIALOG, params, 450);
				} else {
					getMaterialExtentsWindow().alert("ROOT 파라미터와 타입이 MAP인 파라미터에만 추가할 수 있습니다.");
				}
			} else {
				getMaterialExtentsWindow().alert("추가하길 희망하는 상위 파라미터를 선택해주세요.");
			}
		});
		
		paramsRemoveBtn = new MaterialIcon(IconType.REMOVE);
		paramsRemoveBtn.setTooltip("삭제");
		paramsRemoveBtn.setFloat(Float.RIGHT);
		paramsRemoveBtn.setFontSize("20px");
		paramsRemoveBtn.setMarginLeft(10);
		paramsRemoveBtn.setMarginRight(8);
		paramsRemoveBtn.addClickHandler(e -> {
			if (paramsTree.getSelectedItem() != null) {
				MaterialContentTreeItem selectedItem = (MaterialContentTreeItem) paramsTree.getSelectedItem();
				
				if (selectedItem.get("index") != null && (selectedItem.get("index").equals("ROOT"))) {
					getMaterialExtentsWindow().alert("ROOT 파라미터는 삭제할 수 없습니다.");
					
				} else {
					String selectedId = selectedItem.get("id").toString();
					
					getMaterialExtentsWindow().confirm("Parameter 삭제 경고", selectedId + "을(를) 삭제하시겠습니까?", 500, event -> {
						if (event.getSource().toString().contains("예")) {
							
							MaterialContentTreeItem parent = (MaterialContentTreeItem) selectedItem.getParent();
							JSONObject obj = (JSONObject) selectedMenuItem.get("obj");
							
							JSONObject objParam = obj.get("parameters").isObject();
							if (parent.get("index").equals("ROOT")) {
								paramsRemoveAction(selectedItem, selectedId, objParam, true, obj);
								
							} else if (parent.get("index").equals("MAP")) {

								JSONValue objMapValue = objParam.get("value");
								JSONObject objMapObj = null;
								if (objMapValue instanceof JSONArray) {
									objMapObj = objMapValue.isArray().get(objMapValue.isArray().size() - 1).isObject();
									
								} else if (objMapValue instanceof JSONObject) {
									objMapObj = objMapValue.isObject();
									
								}
								paramsRemoveAction(selectedItem, selectedId, objMapObj, false, obj);
							}
							
							getMaterialExtentsWindow().alert(selectedId + "을(를) 삭제했습니다.");
						} else if (event.getSource().toString().contains("no"))  {
							getMaterialExtentsWindow().alert(selectedId + "을(를) 삭제하는데 실패했습니다.");
						}
					});
				}
			} else {
				getMaterialExtentsWindow().alert("삭제할 파라미터를 선택해주세요.");
			}
		});

		//	파라미터 Row
		MaterialRow contentRow = addRow(0, 10);
		paramsTree = new MaterialTree();
		paramsTree.setWidth("100%");
		paramsTree.setHeight("370px");
		paramsTree.setBorder("1px solid #e0e0e0");
		paramsTree.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		headerRow.add(paramsTitle);
		headerRow.add(paramsRemoveBtn);
		headerRow.add(paramsAddBtn);
		headerRow.add(paramsModifyBtn);
		contentRow.add(paramsTree);

		column.add(headerRow);
		column.add(contentRow);
		row.add(column);
	}
	
	public void paramsRemoveAction(MaterialContentTreeItem selectedItem, String selectedId, JSONObject obj, boolean isRoot, JSONObject rootObj) {
		if (obj.get("value") instanceof JSONArray) {
			JSONArray valueArr = obj.get("value").isArray();
			JSONArray newArr = new JSONArray();

			for (int i = 0; i < valueArr.size(); i++) {
				JSONObject o = valueArr.get(i).isObject();
				String id = o.get("id").isString().stringValue();
				
				if (!id.equals(selectedId)) {
					newArr.set(newArr.size(), o);
				}
			}
			
			if (valueArr.size() > 2) {
				obj.put("value", newArr);
			} else {
				obj.put("value", newArr.get(0).isObject());
			}

		} else if (obj.get("value") instanceof JSONObject) {
			if (isRoot) {
				rootObj.put("parameters", null);
			} else {
				obj.put("value", null);
			}
		}
		
		selectedItem.removeFromParent();
		paramsTree.setSelectedItem(null);
	}
	
	//	메뉴 트리 생성
	public static void buildMenu(JSONValue menusValue, MaterialWidget tree, String indexStr) {
		if (menusValue instanceof JSONArray) {
			for (int i = 0; i < menusValue.isArray().size(); i++) {
				JSONObject obj = menusValue.isArray().get(i).isObject();
				appendMenu(obj, tree, indexStr, i);
			}
		} else if (menusValue instanceof JSONObject) {
			JSONObject obj = menusValue.isObject();
			appendMenu(obj, tree, indexStr, 0);
		}
	}
	
	public static void appendMenu(JSONObject obj, MaterialWidget tree, String indexStr, Integer index) {
		String caption = obj.get("caption").isString().stringValue();
		String icon = obj.get("icon").isString().stringValue();
		
		MaterialContentTreeItem item = new MaterialContentTreeItem(caption, IconType.valueOf(icon));
		item.setTextAlign(TextAlign.LEFT);
		
		String menuIndex = indexStr == null ? Integer.toString(index) : indexStr + "-" + index;

		if (caption.equals("부서 서비스")) {
			AddMenuApplication.OTHER_DEPARTMENT_MENU_INDEX = menuIndex;
		}
		
		item.put("caption", caption);
		item.put("icon", icon);
		item.put("obj", obj);
		item.put("index", menuIndex);
		
		item.getElement().setAttribute("index", menuIndex);
		
		if (obj.get("menu") != null) {
			item.put("level", "PARENT");
			buildMenu(obj.get("menu"), item, menuIndex);
		}
		tree.add(item);
	}
	
	public static void _buildMenu() {
		rootMenu.removeFromTree();
		
		//	최상위 메뉴
		rootMenu = new MaterialContentTreeItem("ROOT", IconType.MENU);
		rootMenu.setTextAlign(TextAlign.LEFT);
		rootMenu.put("index", "ROOT");
		rootMenu.put("caption", "ROOT");
		rootMenu.put("level", "ROOT");
		rootMenu.put("obj", new JSONObject().put("menu", menus));
		rootMenu.getElement().setAttribute("index", "ROOT");
		
		MenuTreePanel.menuTree.add(rootMenu);
		buildMenu(menus, rootMenu, null);
	}
	
	public JSONObject getIndexOfObj(String index) {
		String[] arr = index.split("-");
		int[] tempIndex = new int[arr.length];
		
		for (int i = 0; i < arr.length; i++) {
			tempIndex[i] = Integer.parseInt(arr[i]);
		}
		
		menuArrIndex = tempIndex;
		
		JSONArray jsonArr = menus;
		JSONObject result = null;
		
		for (int i = 0; i < arr.length; i++) {
			int num = Integer.parseInt(arr[i]);
			
			if (i == arr.length - 1) {
				result = jsonArr.get(num).isObject();
			} else {
				jsonArr = jsonArr.get(num).isObject().get("menu").isArray();
			}
		}
		
		return result;
	}

	public void setupForm(JSONObject object) {
		permisTable.clearRows();
		paramsTree.clear();
		
		rootParam = new MaterialContentTreeItem("Parameters", IconType.MENU);
		rootParam.setTextAlign(TextAlign.LEFT);
		rootParam.put("index", "ROOT");
		
		paramsTree.add(rootParam);
		
		//	Menu 상세정보 값 셋팅
		setupDetail(object);

		//	Menu Permission 값 셋팅
		JSONValue permissionValue = object.get("permission");
		if (permissionValue != null) {
			if (permissionValue instanceof JSONArray) {
				for (int i = 0; i < permissionValue.isArray().size(); i++) {
					JSONObject obj = permissionValue.isArray().get(i).isObject();
					setupPermission(obj);
				}
			} else if (permissionValue instanceof JSONObject) {
				JSONObject obj = permissionValue.isObject();
				setupPermission(obj);
			}
		}

		//	Menu Parameters 값 셋팅
		JSONValue parameters = object.get("parameters");
		if (parameters != null) {
			JSONValue parameterValue = parameters.isObject().get("value");
			
			if (parameterValue instanceof JSONArray) {
				for (int i = 0; i < parameterValue.isArray().size(); i++) {
					JSONObject obj = parameterValue.isArray().get(i).isObject();
					setupParameters(obj, null, i, rootParam);
				}
			} else if (parameterValue instanceof JSONObject) {
				JSONObject obj = parameterValue.isObject();
				setupParameters(obj, null, 0, rootParam);
			} else if (parameterValue == null) {
				
			}
		}
	}
	
	public void setupDetail(JSONObject object) {
		if (object.get("caption") != null) {
			String titleStr = object.get("caption").isString().stringValue();
			title.setValue(titleStr);
		}

		if (object.get("icon") != null) {
			String iconStr = object.get("icon").isString().stringValue();
			icon.setIconType(IconType.valueOf(iconStr));
		}

		if (object.get("app") != null) {
			String appStr = object.get("app").isString().stringValue();
			
			for (int i = 0; i < applicationList.size(); i++) {
				if (applicationList.get(i).isString().stringValue().equals(appStr)) {
					app.setSelectedIndex(i + 1);
				}
			}
		} else {
			app.setSelectedIndex(0);
		}
		
		if (object.get("bounds") != null) {
			double boundsXVal = object.get("bounds").isObject().get("width").isNumber().doubleValue();
			double boundsYVal = object.get("bounds").isObject().get("height").isNumber().doubleValue();
			boundsX.setValue(Double.toString(boundsXVal));
			boundsY.setValue(Double.toString(boundsYVal));
			boundsX.setEnabled(true);
			boundsY.setEnabled(true);
		} else {
			boundsX.setValue(null);
			boundsY.setValue(null);
			boundsX.setEnabled(false);
			boundsY.setEnabled(false);
		}
	}
	
	public void setupPermission(JSONObject obj) {
		String caption = obj.get("caption").isString().stringValue();
		String uuid = obj.get("uuid").isString().stringValue();
		
		ContentTableRow row = permisTable.addRow(Color.WHITE, 
				caption);
		
		row.put("obj", obj);
		row.put("caption", caption);
		row.put("uuid", uuid);
		row.put("value", obj.get("content").isBoolean().booleanValue());
	}
	
	public void setupParameters(JSONObject obj, String indexStr, Integer index, MaterialWidget treeItem) {
		String id = obj.get("id").isString().stringValue();
		String type = obj.get("type").isString().stringValue();
		JSONValue content = obj.get("content");
		String paramIndex = indexStr == null ? Integer.toString(index) : indexStr + "-" + Integer.toString(index);
		
		MaterialContentTreeItem item = new MaterialContentTreeItem(
				id + "　/　" + type + "　/　" + (content == null ? "" : content instanceof JSONString ? content.isString().stringValue().replaceAll("\"", "") : content), IconType.ADD_BOX);

		item.put("obj", obj);
		item.put("id", id);
		item.put("type", type);
		item.put("index", paramIndex);
		
		if (type.equals("BOOLEAN")) {
			item.put("value", Boolean.toString(obj.get("content").isBoolean().booleanValue()));
			
		} else if (type.equals("STRING")) {
			item.put("value", obj.get("content").isString().stringValue().replaceAll("\"", ""));
			
		} else if (type.equals("MAP")) {
			item.put("index", "MAP");
			JSONValue mapValue = obj.get("value");
			
			if (mapValue instanceof JSONArray) {
				JSONArray mapArr = mapValue.isArray();
				
				for (int j = 0; j < mapArr.size(); j++) {
					JSONObject mapParamObj = mapArr.isArray().get(j).isObject();
					
					setupParameters(mapParamObj, indexStr, j, item);
				}
				
			} else if (mapValue instanceof JSONObject) {
				JSONObject mapParamObj = mapValue.isObject();
				
				setupParameters(mapParamObj, indexStr, 0, item);
			}
		}
		treeItem.add(item);
	}
	
	//	불러오기 시
	public void loadXml() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("FILE_LOAD"));
		
		VisitKoreaBusiness.post("call", paramJson.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				String process = resultObj.get("header").isObject().get("process").isString().stringValue();
				
				if (process.equals("success")) {
					JSONObject resultBody =  resultObj.get("body").isObject();
					JSONObject json =  resultBody.get("resultJson").isObject();
					applicationList =  resultBody.get("resultAppList").isArray();
					
					menus = json
							.get("menu").isObject()
							.get("menu").isArray();
					
					//	최상위 메뉴
					rootMenu = new MaterialContentTreeItem("ROOT", IconType.MENU);
					rootMenu.setTextAlign(TextAlign.LEFT);
					rootMenu.put("index", "ROOT");
					rootMenu.put("caption", "ROOT");
					rootMenu.put("level", "ROOT");
					rootMenu.put("obj", menus);
					MenuTreePanel.menuTree.add(rootMenu);
					
					buildMenu(menus, rootMenu, null);

					for (int i = 0; i < applicationList.size(); i++) {
						app.addItem(applicationList.get(i).isString().stringValue());
					}
					
					getMaterialExtentsWindow().alert("메뉴 목록을 성공적으로 로드했습니다.", 400);	
				} else {
					getMaterialExtentsWindow().alert("오류가 발생하였습니다.", 400);
				}
			}
		});
	}
	
	public static Boolean valueValidation(String value) {
		
		return value.contains("<") || value.contains(">");
	}

	public MaterialRow addRow(int marginTop, int marginBottom) {
		MaterialRow row = new MaterialRow();
		row.setMarginTop(marginTop);
		row.setMarginBottom(marginBottom);
		row.setWidth("100%");
		return row;
	}
	
	public MaterialLabel addTitle(String title, double rightMargin) {
		MaterialLabel lb = new MaterialLabel(title);
		lb.setTextColor(Color.BLUE);
		lb.setTextAlign(TextAlign.LEFT);
		lb.setFontWeight(FontWeight.BOLD);
		lb.setFontSize("18px");
		return lb;
	}
	
	public MaterialLabel addLabel(String width, String title, double rightMargin) {
		MaterialLabel lb = new MaterialLabel(title);
		lb.setBackgroundColor(Color.GREY_LIGHTEN_3);
		lb.setTextAlign(TextAlign.CENTER);
		lb.setOverflow(Overflow.HIDDEN);
		lb.setWidth(width);
		lb.setLineHeight(45);
		lb.setFloat(Float.LEFT);
		lb.setMarginRight(rightMargin);
		return lb;
	}

	public MaterialInput addTextBox(String width, String placeHolder, double rightMargin) {
		MaterialInput tb = new MaterialInput();
		tb.setOverflow(Overflow.HIDDEN);
		tb.setHeight("45px");
		tb.setWidth(width);
		tb.setFloat(Float.LEFT);
		tb.setMarginRight(rightMargin);
		return tb;
	}

	public void formEnable(boolean enable) {
		permisTable.clearRows();
		paramsTree.clear();
		
		title.setValue("");
		title.setEnabled(enable);
		icon.setIconType(IconType.DEFAULT);
		iconModifyBtn.setEnabled(enable);
		app.setSelectedIndex(0);
		app.setEnabled(enable);
		boundsX.setValue("");
		boundsY.setValue("");
		boundsX.setEnabled(enable);
		boundsY.setEnabled(enable);
		paramsAddBtn.setEnabled(enable);
		paramsModifyBtn.setEnabled(enable);
		paramsRemoveBtn.setEnabled(enable);
		permisAddBtn.setEnabled(enable);
		permisModifyBtn.setEnabled(enable);
		permisRemoveBtn.setEnabled(enable);
	}
	
	public MaterialIcon getIcon() {
		return icon;
	}

	public void setIcon(MaterialIcon icon) {
		this.icon = icon;
	}

	public MaterialInput getTitleValue() {
		return title;
	}

	public void setTitle(MaterialInput title) {
		this.title = title;
	}

	public MaterialInput getBoundsX() {
		return boundsX;
	}

	public void setBoundsX(MaterialInput boundsX) {
		this.boundsX = boundsX;
	}

	public MaterialInput getBoundsY() {
		return boundsY;
	}

	public void setBoundsY(MaterialInput boundsY) {
		this.boundsY = boundsY;
	}

	public MaterialTree getParamsTree() {
		return paramsTree;
	}

	public void setParamsTree(MaterialTree paramsTree) {
		this.paramsTree = paramsTree;
	}

	public ContentTable getPermisTable() {
		return permisTable;
	}

	public void setPermisTable(ContentTable permisTable) {
		this.permisTable = permisTable;
	}
}
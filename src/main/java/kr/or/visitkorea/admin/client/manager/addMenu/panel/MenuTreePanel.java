package kr.or.visitkorea.admin.client.manager.addMenu.panel;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.account.composite.MaterialContentTreeItem;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuApplication;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuMain;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;

public class MenuTreePanel extends MaterialColumn {
	private AddMenuMain host;
	private MaterialPanel iconPanel;
	private MaterialIcon addIcon,
						 removeIcon,
						 otdModifyIcon;
	public static MaterialTree menuTree;
	
	public MenuTreePanel(AddMenuMain host) {
		this.setGrid("s3");
		this.setHeight("590px");
		this.setPaddingLeft(20);
		this.setPaddingRight(10);

		menuTree = new MaterialTree();
		menuTree.setWidth("100%");
		menuTree.setHeight("564px");
		menuTree.setBorder("1px solid #e0e0e0");
		menuTree.setBorderBottom("0px");
		menuTree.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		menuTree.addSelectionHandler(e -> {
			MaterialContentTreeItem item = (MaterialContentTreeItem) e.getSelectedItem();
			AddMenuMain.selectedMenuItem = item;
			
			String indexStr = item.get("index").toString();
			
			if (indexStr.equals("ROOT")) {
				addIcon.setEnabled(true);
				removeIcon.setEnabled(false);
				host.formEnable(false);
				return;
			}
			
			addIcon.setEnabled(true);
			removeIcon.setEnabled(true);
			otdModifyIcon.setDisplay(Display.NONE);
			host.formEnable(true);
			host.setupForm((JSONObject) item.get("obj"));
			
			if (indexStr.split("-")[0].equals(AddMenuApplication.OTHER_DEPARTMENT_MENU_INDEX)) {
				int size = indexStr.split("-").length;
				
				switch (size) {
					case 1:
						addIcon.setEnabled(true);
						removeIcon.setEnabled(true);
						otdModifyIcon.setDisplay(Display.NONE);
						break;
					case 2:
						addIcon.setEnabled(false);
						removeIcon.setEnabled(true);
						otdModifyIcon.setDisplay(Display.BLOCK);
						break;
					case 3:
						addIcon.setEnabled(false);
						removeIcon.setEnabled(false);
						otdModifyIcon.setDisplay(Display.NONE);
						break;
					default:
						break;
				}
			}
		});
		
		//	아이콘 영역
		iconPanel = new MaterialPanel();
		iconPanel.setWidth("100%");
		iconPanel.setHeight("26px");
		iconPanel.setBorder("1px solid #e0e0e0");
		iconPanel.setPadding(0);
		iconPanel.setLayoutPosition(Position.RELATIVE);

		//	메뉴 추가 버튼
		addIcon = addIcon(IconType.ADD, "메뉴추가", Float.LEFT);
		addIcon.addClickHandler(e -> {
			MaterialContentTreeItem selectedItem = (MaterialContentTreeItem) menuTree.getSelectedItem();
			if (selectedItem != null) {
				String indexStr = (String) selectedItem.get("index");
				String[] index = indexStr.split("-");
				
				if (index.length >= 3) {
					host.getMaterialExtentsWindow().alert("3단계 이상으로 메뉴를 추가할 수 없습니다.");
					return;
				}
				
				if (index[0].equals(AddMenuApplication.OTHER_DEPARTMENT_MENU_INDEX)) {
					HashMap<String, Object> params = new HashMap<>();
					params.put("selectedItem", selectedItem);
					params.put("mode", "add");
					host.getMaterialExtentsWindow().openDialog(AddMenuApplication.OTHER_DEPARTMENT_DIALOG, params, 800);
					return;
				}
				
				menuAddAction(selectedItem, indexStr);
				
			} else {
				host.getMaterialExtentsWindow().alert("추가할 위치의 메뉴를 선택해주세요.");
			}
		});
		
		//	메뉴 삭제 버튼
		removeIcon = addIcon(IconType.REMOVE, "메뉴삭제", Float.LEFT);
		removeIcon.addClickHandler(e -> {
			if (menuTree.getSelectedItem() != null) {
				MaterialContentTreeItem selectedItem = (MaterialContentTreeItem) menuTree.getSelectedItem();
				
				if (selectedItem.equals(AddMenuMain.rootMenu)) {
					host.getMaterialExtentsWindow().alert("ROOT메뉴는 삭제할 수 없습니다.");
					return;
				}
				
				String menuTitle = selectedItem.get("caption").toString();
				host.getMaterialExtentsWindow().confirm("메뉴 삭제 경고", menuTitle + "메뉴를 삭제하시겠습니까?", 400, event -> {
					if (event.getSource().toString().contains("yes")) {
						host.getTitleValue().setValue("");
						host.getIcon().setIconType(IconType.DEFAULT);
						host.getBoundsX().setValue("");
						host.getBoundsY().setValue("");
						host.getPermisTable().clearRows();
						host.getParamsTree().clear();

						MaterialContentTreeItem parent = (MaterialContentTreeItem) selectedItem.getParent();
						JSONValue parentObj = (JSONValue) parent.get("obj");
						
						menuRemoveAction(parentObj, menuTitle);

						selectedItem.removeFromParent();
						menuTree.setSelectedItem(null);
						
						host.getMaterialExtentsWindow().alert(menuTitle + "메뉴를 삭제하였습니다.");
					} else if (event.getSource().toString().contains("no")) {
						
					} else {
						host.getMaterialExtentsWindow().alert(menuTitle + "메뉴를 삭제하는데 실패하였습니다.");
					}
				});
			} else {
				host.getMaterialExtentsWindow().alert("삭제를 희망하는 메뉴를 선택해주세요.");
			}
		});

		otdModifyIcon = addIcon(IconType.EDIT, "OTD 항목 수정", Float.RIGHT);
		otdModifyIcon.setDisplay(Display.NONE);
		otdModifyIcon.addClickHandler(e -> {
			MaterialContentTreeItem selectedItem = (MaterialContentTreeItem) menuTree.getSelectedItem();
			HashMap<String, Object> params = new HashMap<>();
			params.put("selectedItem", selectedItem);
			params.put("mode", "modify");
			host.getMaterialExtentsWindow().openDialog(AddMenuApplication.OTHER_DEPARTMENT_DIALOG, params, 800);
			return;
		});
		
		iconPanel.add(addIcon);
		iconPanel.add(removeIcon);
		iconPanel.add(otdModifyIcon);
		this.add(menuTree);
		this.add(iconPanel);
	}

	public void menuAddAction(MaterialContentTreeItem selectedItem, String indexStr) {
		JSONObject newObj = new JSONObject();
		newObj.put("icon", new JSONString("CROP_DIN"));
		newObj.put("caption", new JSONString("이름없음"));
		
		JSONObject permission = new JSONObject();
		permission.put("caption", new JSONString("사용여부"));
		permission.put("id", new JSONString("use"));
		permission.put("uuid", new JSONString(IDUtil.uuid()));
		permission.put("content", JSONBoolean.getInstance(false));
		
		newObj.put("permission", permission);
		
		MaterialContentTreeItem newItem = new MaterialContentTreeItem("이름없음", IconType.CROP_DIN);
		newItem.put("icon", "CROP_DIN");
		newItem.put("caption", "이름없음");
		newItem.put("obj", newObj);

		if (indexStr.equals("ROOT")) {
			JSONArray menuArr = (JSONArray) selectedItem.get("obj");
			menuArr.set(menuArr.size(), newObj);
			
			newObj.put("index", new JSONString(Integer.toString(menuArr.size())));
			newItem.put("index", menuArr.size());
			
		} else {
			JSONObject targetObj = (JSONObject) selectedItem.get("obj");
			
			if (targetObj.get("menu") instanceof JSONArray) {
				JSONArray menuArr = targetObj.get("menu").isArray();
				menuArr.set(menuArr.size(), newObj);
				targetObj.put("menu", menuArr);

				newObj.put("index", new JSONString(indexStr + "-" + menuArr.size()));
				newItem.put("index", indexStr + "-" + menuArr.size());
				
			} else if (targetObj.get("menu") instanceof JSONObject) {
				JSONArray newArr = new JSONArray();
				newArr.set(0, targetObj.get("menu").isObject());
				newArr.set(1, newObj);
				targetObj.put("menu", newArr);

				newObj.put("index", new JSONString(indexStr + "-" + 1));
				newItem.put("index", indexStr + "-" + 1);
				
			} else if (targetObj.get("menu") == null) {
				targetObj.put("menu", newObj);
				
				newObj.put("index", new JSONString(indexStr + "-" + Integer.toString(0)));
				newItem.put("index", indexStr + "-" + 0);
			}
		}
		
		selectedItem.addItem(newItem);
		newItem.select();
	}
	
	public void menuRemoveAction(JSONValue parentObj, String menuTitle) {
		if (parentObj instanceof JSONArray) {
			JSONArray parentMenuArr = parentObj.isArray();
			JSONArray newArr = new JSONArray();
			
			for (int i = 0; i < parentMenuArr.size(); i++) {
				if (!parentMenuArr.get(i).isObject().get("caption").isString().stringValue().equals(menuTitle)) {
					newArr.set(newArr.size(), parentMenuArr.get(i));
				}
			}
			AddMenuMain.menus = newArr;
			
		} else {
			JSONValue parentMenuValue = parentObj.isObject().get("menu");
			
			if (parentMenuValue instanceof JSONArray) {
				JSONArray parentMenuArr = parentMenuValue.isArray();
				JSONArray newArr = new JSONArray();
				
				for (int i = 0; i < parentMenuArr.size(); i++) {
					if (!parentMenuArr.get(i).isObject().get("caption").isString().stringValue().equals(menuTitle)) {
						newArr.set(newArr.size(), parentMenuArr.get(i));
					}
				}
				parentObj.isObject().put("menu", newArr);
				
			} else if (parentMenuValue instanceof JSONObject) {
				
				parentObj.isObject().put("menu", null);
			}
		}
	}
	
	public MaterialIcon addIcon(IconType iconType, String tooltip, Float flot) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setWidth("26px");
		icon.setHeight("26px");
		icon.setLineHeight(26);
		icon.setFontSize("1.0em");
		icon.setMargin(0);
		icon.setTooltip(tooltip);
		icon.setFloat(flot);
		if (flot == Float.LEFT) {
			icon.setBorderRight("1px solid #e0e0e0");
		} else {
			icon.setBorderLeft("1px solid #e0e0e0");
		}
		return icon;
	}
}

package kr.or.visitkorea.admin.client.manager.addMenu.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.RegExp;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.account.composite.MaterialContentTreeItem;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ParamsDialog extends DialogContent {

	private String dialogMode;
	private String idVal, typeVal, valueVal;
	private MaterialTextBox id, value;
	private MaterialComboBox<String> type;
	private MaterialContentTreeItem parentItem, selectedItem;
	private MaterialContentTreeItem selectedMenuItem;
	private MaterialButton submitBtn;
	
	public ParamsDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public void init() {
	}

	@Override
	public int getHeight() {
		return 400;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.clear();
		this.dialogMode = this.getParameters().get("mode").toString();
		if (this.dialogMode.equals("modify")) {
			this.idVal = this.getParameters().get("id") != null ? this.getParameters().get("id").toString() : null;
			this.typeVal = this.getParameters().get("type") != null ? this.getParameters().get("type").toString() : null;
			this.valueVal = this.getParameters().get("value") != null ? this.getParameters().get("value").toString() : null;
			this.selectedItem = (MaterialContentTreeItem) this.getParameters().get("selectedItem");
			
		} else if (this.dialogMode.equals("add")) {
			this.idVal = null;
			this.typeVal = null;
			this.valueVal = null;
			this.selectedItem = null;
			this.parentItem = (MaterialContentTreeItem) this.getParameters().get("parentItem");
			this.selectedMenuItem = (MaterialContentTreeItem) this.getParameters().get("selectedMenuItem");
		}

		buildHeader();
		buildContent();
		buildFooter();
	}

	public void buildHeader() {
		MaterialLabel title = new MaterialLabel();
		title.setTextColor(Color.BLUE);
		title.setFontSize("1.4em");
		title.setTextAlign(TextAlign.LEFT);
		title.setFontWeight(FontWeight.BOLD);
		title.setPadding(15);
		if (this.dialogMode.equals("add")) {
			title.setText("파라미터 추가하기");
		} else if (this.dialogMode.equals("modify")) {
			title.setText("파라미터 수정하기");
		}
		this.add(title);
	}
	
	public void buildContent() {
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setHeight("280px");
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		MaterialRow row1 = addRow("id", this.idVal);
		id = addTextBox(idVal, row1);
		id.addKeyUpHandler(e -> {
			if (type.getSelectedIndex() == 2) {
				submitBtn.setEnabled(!id.getValue().equals(""));
				
				return;
			}
			submitBtn.setEnabled(id.getValue().equals("") & value.getValue().equals(""));
		});
		
		MaterialRow row2 = addRow("type", this.typeVal);
		type = addComboBox(typeVal, row2);
		type.addSelectionHandler(e -> {
			if (type.getSelectedIndex() == 2) {
				submitBtn.setEnabled(!id.getValue().equals(""));
				
				return;
			}
		});
		
		if (this.dialogMode.equals("modify") && typeVal.equals("MAP")) {
			row2.setDisplay(Display.NONE);
		}
		
		MaterialRow row3 = addRow("value", this.valueVal);
		value = addTextBox(valueVal, row3);
		value.addKeyUpHandler(e -> {
			submitBtn.setEnabled(!id.getValue().equals("") && !value.getValue().equals(""));
		});
		if (this.dialogMode.equals("modify") && typeVal.equals("MAP")) {
			row3.setDisplay(Display.NONE);
		}
		
		MaterialIcon uuidGetIcon = new MaterialIcon(IconType.REFRESH);
		uuidGetIcon.setMarginLeft(6);
		uuidGetIcon.setTooltip("UUID 발급");
		uuidGetIcon.addClickHandler(e -> {
			value.setValue(IDUtil.uuid());
			submitBtn.setEnabled(!id.getValue().equals("") && !value.getValue().equals(""));
		});
		
		RegExp regExp = RegExp.compile("^[0-9A-Za-z]{8}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{4}\\-[0-9A-Fa-f]{12}$");
		if (regExp.exec(valueVal) != null) {
			value.setEnabled(false);
		}

		type.addSelectionHandler(e -> {
			if (type.getSelectedIndex() == 2) {
				row3.setDisplay(Display.NONE);
			} else {
				row3.setDisplay(Display.FLEX);
			}
		});
		
		row3.add(uuidGetIcon);
		
		panel.add(row1);
		panel.add(row2);
		panel.add(row3);
		
		this.add(panel);
	}
	
	public void buildFooter() {
		MaterialRow btnRow = new MaterialRow();
		btnRow.setMarginTop(10);
		btnRow.setWidth("100%");
		btnRow.setTextAlign(TextAlign.RIGHT);
		
		submitBtn = new MaterialButton("확인");
		submitBtn.setMarginRight(10);
		if (id.getValue().equals("") || value.getValue().equals("")) {
			submitBtn.setEnabled(false);
		}
		submitBtn.addClickHandler(e -> {
			if (this.dialogMode.equals("modify")) {
				modifyAction();
				
				alert("수정 완료", 350, 250, new String[]{"Parameter 항목이 수정되었습니다."});
			} else {
				addAction();

				alert("추가 완료", 350, 250, new String[]{"Parameter 항목이 추가되었습니다."});
			}
		});
		
		MaterialButton cancelBtn = new MaterialButton("취소");
		cancelBtn.setMarginRight(10);
		cancelBtn.addClickHandler(e -> {
			getMaterialExtentsWindow().closeDialog();
		});
		
		btnRow.add(submitBtn);
		btnRow.add(cancelBtn);
		this.add(btnRow);
	}
	
	public void modifyAction() {
		String typeStr = (type.getSelectedIndex() == 0 ? "STRING" : (type.getSelectedIndex() == 1) ? "BOOLEAN" : "MAP");
		selectedItem.setText(id.getValue() + "　/　" + typeStr + "　/　" + (value.getValue()));
		selectedItem.put("id", id.getValue());
		selectedItem.put("type", typeStr);
		selectedItem.put("value", value.getValue());
		
		JSONObject obj = (JSONObject) this.selectedItem.get("obj");
		obj.put("id", new JSONString(id.getValue()));
		obj.put("type", new JSONString(typeStr));
		if (!typeStr.equals("MAP")) {
			obj.put("value", new JSONString(value.getValue()));
		}
	}
	
	public void addAction() {
		JSONObject obj = (JSONObject) this.selectedMenuItem.get("obj");
		String level = (String) parentItem.get("index");
		
		String typeStr = (type.getSelectedIndex() == 0 ? "STRING" : (type.getSelectedIndex() == 1) ? "BOOLEAN" : "MAP");
		MaterialContentTreeItem newItem = new MaterialContentTreeItem(
				id.getValue() + "　/　" + typeStr + "　/　" + (value.getValue()), IconType.ADD);
		
		newItem.put("id", id.getValue());
		newItem.put("type", typeStr);
		newItem.put("value", value.getValue());
		
		JSONObject newObj = new JSONObject();
		newObj.put("id", new JSONString(id.getValue()));
		newObj.put("type", new JSONString(typeStr));
		if (!typeStr.equals("MAP")) {
			newObj.put("content", new JSONString(value.getValue()));
		}
		
		if (level.equals("ROOT")) {
			appendParam(obj, newObj, newItem);
			
		} else if (level.equals("MAP")) {
			JSONObject mapObj = (JSONObject) parentItem.get("obj");
			
			appendMapParam(mapObj, newObj, newItem);
		}
		
		parentItem.addItem(newItem);
	}
	
	public void appendMapParam(JSONObject mapObj, JSONObject newObj, MaterialContentTreeItem treeItem) {
		JSONValue mapValue = mapObj.get("value");
		
		if (mapValue == null) {
			mapObj.put("value", newObj);
			String type = newObj.get("type").isString().stringValue();
			
			if (type.equals("MAP")) {
				treeItem.put("index", "MAP");
				
			} else {
				treeItem.put("index", 0);
			}
			
			treeItem.put("obj", newObj);
			return;
		}
		
		if (mapValue instanceof JSONArray) {
			JSONArray parentArr = mapValue.isArray();
			String type = newObj.get("type").isString().stringValue();
			
			if (type.equals("MAP")) {
				treeItem.put("index", "MAP");
				
			} else {
				treeItem.put("index", new JSONNumber(parentArr.size()));
				
			}
			
			JSONObject lastParam = parentArr.get(parentArr.size() - 1).isObject();
			String lastParamType = lastParam.get("type").isString().stringValue();
			
			if (lastParamType.equals("MAP")) {
				parentArr.set(parentArr.size(), lastParam);
				parentArr.set(parentArr.size() - 2, newObj);
				
			} else {
				parentArr.set(parentArr.size(), newObj);
				
			}
			mapObj.put("value", parentArr);

			
		} else if (mapValue instanceof JSONObject) {
			JSONObject parentObj = mapValue.isObject();
			String type = newObj.get("type").isString().stringValue();

			if (type.equals("MAP")) {
				treeItem.put("index", "MAP");
				
			} else {
				treeItem.put("index", 0);
				
			}
			
			JSONArray newArr = new JSONArray();
			newArr.set(newArr.size(), parentObj);
			newArr.set(1, newObj);
			
			mapObj.put("value", newArr);
			
		}
		treeItem.put("obj", newObj);
	}
	
	public void appendParam(JSONObject obj, JSONObject newObj, MaterialContentTreeItem treeItem) {
		JSONValue parametersObj = obj.get("parameters");
		
		if (parametersObj == null) {
			JSONObject valueObj = new JSONObject();
			String type = newObj.get("type").isString().stringValue();
			
			if (type.equals("MAP")) {
				treeItem.put("index", "MAP");
				
			} else {
				treeItem.put("index", 0);
			}
			
			valueObj.put("value", newObj);
			obj.put("parameters", valueObj);
			treeItem.put("obj", newObj);
			return;
		}
		
		if (parametersObj.isObject().get("value") instanceof JSONArray) {
			JSONArray parentArr = parametersObj.isObject().get("value").isArray();
			String type = newObj.get("type").isString().stringValue();
			
			if (type.equals("MAP")) {
				treeItem.put("index", "MAP");
				
			} else {
				treeItem.put("index", new JSONNumber(parentArr.size()));
			}
			
			JSONObject lastParam = parentArr.get(parentArr.size() - 1).isObject();
			String lastParamType = lastParam.get("type").isString().stringValue();
			
			if (lastParamType.equals("MAP")) {
				parentArr.set(parentArr.size(), lastParam);
				parentArr.set(parentArr.size() - 2, newObj);
				
			} else {
				parentArr.set(parentArr.size(), newObj);
				
			}
			parametersObj.isObject().put("value", parentArr);
			
		} else if (parametersObj.isObject().get("value") instanceof JSONObject) {
			JSONObject parentObj = parametersObj.isObject().get("value").isObject();
			String type = newObj.get("type").isString().stringValue();

			if (type.equals("MAP")) {
				treeItem.put("index", "MAP");
				
			} else {
				treeItem.put("index", 0);
				
			}
			
			JSONArray newArr = new JSONArray();
			newArr.set(newArr.size(), parentObj);
			newArr.set(1, newObj);
			
			parametersObj.isObject().put("value", newArr);
		}
		treeItem.put("obj", newObj);
	}
	
	public MaterialRow addRow(String title, String value) {
		MaterialRow row = new MaterialRow();
		row.setDisplay(Display.FLEX);
		row.setFlexAlignItems(FlexAlignItems.CENTER);
		row.setMargin(0);

		MaterialLabel label = new MaterialLabel(title);
		label.setWidth("30%");
		label.setTextAlign(TextAlign.CENTER);
		
		row.add(label);
		return row;
	}

	public MaterialTextBox addTextBox(String value, MaterialRow row) {
		MaterialTextBox textBox = new MaterialTextBox();
		textBox.setValue(value != null ? value.replaceAll("\"", "") : "");
		textBox.setWidth("60%");
		
		row.add(textBox);
		return textBox;
	}
	
	public MaterialComboBox<String> addComboBox(String selectedValue, MaterialRow row) {
		MaterialComboBox<String> combo = new MaterialComboBox<String>();
		combo.setMarginBottom(0);
		combo.setWidth("60%");
		if (selectedValue != null) {
			combo.addItem("STRING");
			combo.addItem("BOOLEAN");
			combo.addItem("MAP");
			
			if (selectedValue.equals("STRING")) {
				combo.setSelectedIndex(0);
			} else if (selectedValue.equals("BOOLEAN")) {
				combo.setSelectedIndex(1);
			} else if (selectedValue.equals("MAP")) {
				combo.setSelectedIndex(2);
			}
		} else {
			combo.addItem("STRING");
			combo.addItem("BOOLEAN");
			combo.addItem("MAP");
			combo.setSelectedIndex(0);
		}

		row.add(combo);
		return combo;
	}
}

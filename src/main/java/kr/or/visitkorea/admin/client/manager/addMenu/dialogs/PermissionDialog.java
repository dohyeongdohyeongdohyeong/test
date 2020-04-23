package kr.or.visitkorea.admin.client.manager.addMenu.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.account.composite.MaterialContentTreeItem;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PermissionDialog extends DialogContent {

	private String dialogMode;
	private String captionVal;
	private MaterialTextBox caption;
	private ContentTableRow selectedRow;
	private ContentTable table;
	private MaterialContentTreeItem selectedMenuItem;
	private MaterialButton submitBtn;
	
	public PermissionDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public void init() {
	}

	@Override
	public int getHeight() {
		return 200;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.clear();
		this.dialogMode = this.getParameters().get("mode").toString();
		if (dialogMode.equals("modify")) {
			this.captionVal = this.getParameters().get("caption") != null ? this.getParameters().get("caption").toString() : null;
			this.selectedRow = (ContentTableRow) this.getParameters().get("selectedRow");
		} else if (dialogMode.equals("add")) {
			this.captionVal = null;
			this.selectedRow = null;
			this.selectedMenuItem = (MaterialContentTreeItem) this.getParameters().get("selectedMenuItem");
			this.table = (ContentTable) this.getParameters().get("table");
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
			title.setText("권한 추가하기");
		} else if (this.dialogMode.equals("modify")) {
			title.setText("권한 수정하기");
		}
		this.add(title);
	}
	
	public void buildContent() {
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setHeight("80px");
		
		MaterialRow row1 = addRow("caption", this.captionVal);
		caption = addTextBox(captionVal, row1);
		caption.addKeyUpHandler(e -> {
			submitBtn.setEnabled(!caption.getValue().equals(""));
		});
		
		panel.add(row1);
		
		this.add(panel);
	}
	
	public void buildFooter() {
		MaterialRow btnRow = new MaterialRow();
		btnRow.setMarginTop(10);
		btnRow.setWidth("100%");
		btnRow.setTextAlign(TextAlign.RIGHT);
		
		submitBtn = new MaterialButton("확인");
		submitBtn.setMarginRight(10);
		if (caption.getValue().equals("")) {
			submitBtn.setEnabled(false);
		}
		submitBtn.addClickHandler(e -> {
			if (AddMenuApplication.valueValidation(caption.getValue())) {
				alert("알림", 350, 250, new String[] {"< 또는 > 문자는 포함될 수 없습니다."});
				
			} else if (caption.getValue().equals("")) {
				alert("알림", 350, 250, new String[] {"Caption을 입력해주세요."});
				
			} else if (validationIsString(caption.getValue())) {
				alert("알림", 350, 250, new String[] {"Caption에는 문자열만 입력가능합니다."});
				
			} else {
				if (this.dialogMode.equals("modify")) {
					modifyAction();

					alert("수정 성공", 350, 250, new String[] {"Permission이 수정되었습니다."});
					
				} else {
					addAction();
					
					alert("추가 성공", 350, 250, new String[] {"Permission이 추가되었습니다."});
				}
				getMaterialExtentsWindow().closeDialog();
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
	
	//	Parameter 수정 처리
	public void modifyAction() {
		MaterialLabel changedCaption = (MaterialLabel) selectedRow.getColumnObject(0);
		changedCaption.setText(caption.getValue()); 

		JSONObject obj = (JSONObject) selectedRow.get("obj");
		obj.put("caption", new JSONString(caption.getValue()));
	}

	//	Parameter 추가 처리
	public void addAction() {
		ContentTableRow row = table.addRow(Color.WHITE, caption.getValue());
		
		JSONObject selectedMenuObj = (JSONObject) selectedMenuItem.get("obj");
		JSONValue selectedMenuPermisList = selectedMenuObj.get("permission");
		
		String uuid = IDUtil.uuid();
		
		JSONObject obj = new JSONObject();
		obj.put("caption", new JSONString(caption.getValue()));
		obj.put("uuid", new JSONString(uuid));
		obj.put("content", JSONBoolean.getInstance(false));

		row.put("obj", obj);
		row.put("caption", caption.getValue());
		row.put("uuid", uuid);
		row.put("value", false);

		if (selectedMenuPermisList instanceof JSONArray) { 
			selectedMenuPermisList.isArray().set(selectedMenuPermisList.isArray().size(), obj);
			
		} else if (selectedMenuPermisList instanceof JSONObject) {
			JSONArray newArr = new JSONArray();
			newArr.set(0, selectedMenuPermisList.isObject());
			newArr.set(1, obj);
			selectedMenuObj.put("permission", newArr);
		}
	}
	
	public boolean validationIsString(String value) {
		try {
			Integer.parseInt(value);
		} catch (Exception e) {
			return false;
		}
		return true;
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
		textBox.setValue(value);
		textBox.setWidth("60%");

		row.add(textBox);
		return textBox;
	}
}

package kr.or.visitkorea.admin.client.manager.contents.author.dialogs;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorEditDialog extends DialogContent {

	private String mode = null;
	private MaterialLabel dialogTitle;
	private MaterialTextBox authorName;
	private SelectionPanel authorTypePanel;
	private SelectionPanel activeStatusPanel;
	private MaterialRow dupliLabelRow;
	private MaterialRow row1;
	private MaterialRow row2;
	private MaterialLabel dupliLabel;
	private JSONArray authorTypeList;
	private String athId = null;
	
	public AuthorEditDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}
	
	@Override
	protected void onLoad() {
		this.mode = this.getParameters().get("mode").toString();
		this.authorTypeList = (JSONArray) this.getParameters().get("authorTypeList");

		this.row2.setMarginBottom(20);
		this.dupliLabelRow.setDisplay(Display.NONE);
		this.dupliLabel.setTextColor(Color.BLACK);
		
		addAuthorTypeItem();

		if (this.mode.equals("ADD")) {
			this.dialogTitle.setText("작가 추가");
			this.athId = IDUtil.uuid();
		} else if (this.mode.equals("MODIFY")) {
			this.dialogTitle.setText("작가 수정");
			this.athId = this.getParameters().get("athId").toString();
		}
		setupForm();
	}

	public void setupForm() {
		if (this.mode.equals("ADD")) {
			this.authorName.setValue("");
			this.activeStatusPanel.setSelectionOnSingleMode("활동");
		} else if (this.mode.equals("MODIFY")) {
			String type = this.getParameters().get("type").toString();
			
			this.authorName.setValue(this.getParameters().get("name").toString());
			this.activeStatusPanel.setSelectionOnSingleMode(this.getParameters().get("status").toString());
			this.authorTypePanel.setSelectionOnSingleMode(type);
		}
	}
	
	@Override
	public void init() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel();
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);
		
		row1 = new MaterialRow();
		addLabel(row1, "* 작가 구분", "s3");
		this.add(row1);

		row2 = new MaterialRow();
		addLabel(row2, "* 작가 이름", "s3");
		authorName = addTextBox(row2, "s5");
		
		MaterialButton dupBtn = new MaterialButton("중복확인");
		dupBtn.setGrid("s2");
		dupBtn.setMarginTop(5);
		dupBtn.addClickHandler(event -> {
			duplicationChk();
		});
		
		row2.add(dupBtn);
		this.add(row2);
		
		dupliLabelRow = new MaterialRow();
		dupliLabelRow.setDisplay(Display.NONE);
		
		dupliLabel = new MaterialLabel();
		dupliLabel.setGrid("s9");
		dupliLabel.setFloat(Float.RIGHT);
		dupliLabel.setMarginTop(20);
		dupliLabelRow.add(dupliLabel);
		this.add(dupliLabelRow);
		
		MaterialRow row4 = new MaterialRow();
		addLabel(row4, "* 활동 여부", "s3");

		HashMap<String, Object> activeValueMap = new HashMap<>();
		activeValueMap.put("활동", 1);
		activeValueMap.put("미활동", 0);
		
		activeStatusPanel = addSelectionPanel(row4, "s7", 5, activeValueMap);
		activeStatusPanel.setSelectionOnSingleMode("활동");
		this.add(row4);
		
		MaterialButton saveBtn = new MaterialButton("저장");
		saveBtn.setMarginRight(10);
		saveBtn.setFloat(Float.RIGHT);
		saveBtn.addClickHandler(event -> {
			submitProcess();
		});
		this.getButtonArea().add(saveBtn);
		
	}

	public void addAuthorTypeItem() {
		if (this.authorTypePanel != null)
			this.authorTypePanel.removeFromParent();
		HashMap<String, Object> typeValueMap = new HashMap<>();
		for (int i = 0; i < this.authorTypeList.size(); i++) {
			JSONObject obj = this.authorTypeList.get(i).isObject();
			String name = obj.containsKey("TYPE_NAME") ? obj.get("TYPE_NAME").isString().stringValue() : null;
			String keyword = obj.containsKey("KEYWORD") ? obj.get("KEYWORD").isString().stringValue() : null;
			String typeId = obj.containsKey("TYPE_ID") ? obj.get("TYPE_ID").isString().stringValue() : null;
			typeValueMap.put(name + "(" + keyword + ")", typeId);
		}
		this.authorTypePanel = addSelectionPanel(row1, "s9", 10, typeValueMap);
		this.authorTypePanel.setSelectionOnSingleMode((String) typeValueMap.keySet().toArray()[0]);
	}
	
	public String isValidate() {
		String msg = null;
		if (this.authorName.getValue().equals("")) 
			msg = "작가 이름을 입력해주세요.";
		else if (this.dupliLabel.getTextColor() != Color.GREEN)
			msg = "작가 이름 중복확인을 해주세요.";
		else if (this.authorTypePanel.getSelectedValue() == null) 
			msg = "작가 구분을 선택해주세요.";
		return msg;
	}
	
	public void submitProcess() {
		String msg = isValidate();
		if (msg != null) {
			alert("알림", 400, 300, new String[] {
					"입력 누락된 항목이 있습니다.",
					msg
			});
			return;
		}
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_AUTHOR_INFO"));
		parameterJSON.put("mode", new JSONString(this.mode));
		parameterJSON.put("athId", new JSONString(this.athId));
		parameterJSON.put("name", new JSONString(this.authorName.getValue()));
		parameterJSON.put("typeId", new JSONString((String) this.authorTypePanel.getSelectedValue()));
		parameterJSON.put("status", new JSONNumber((int) this.activeStatusPanel.getSelectedValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			String ment = resultObj.get("header").isObject().get("ment").isString().stringValue();
			
			if (process.equals("success")) {
				alert("작가 수정 성공", 400, 300, new String[] {ment});
			} else {
				alert("작가 수정 실패", 400, 300, new String[] {ment});
			}
		});
	}
	
	public void duplicationChk() {
		this.row2.setMarginBottom(0);
		this.dupliLabelRow.setDisplay(Display.BLOCK);
		if (this.authorName.getValue().equals("")) {
			this.dupliLabel.setText("작가 이름을 입력해주세요.");
			this.dupliLabel.setTextColor(Color.RED);
			return;
		}
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_LIST_DUPLICATE"));
		parameterJSON.put("name", new JSONString(this.authorName.getValue()));
		parameterJSON.put("typeId", new JSONString((String) this.authorTypePanel.getSelectedValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
			
			if (resultArr.size() == 0) {
				this.dupliLabel.setText("등록이 가능합니다.");
				this.dupliLabel.setTextColor(Color.GREEN);
			} else {
				this.dupliLabel.setText("선택한 구분 중 기존에 동일한 이름의 작가가 있습니다.");
				this.dupliLabel.setTextColor(Color.RED);
			}
		});
	}
	
	@Override
	public int getHeight() {
		return 400;
	}
	
	public MaterialTextBox addTextBox(MaterialRow row, String grid) {
		MaterialTextBox tbox = new MaterialTextBox();
		tbox.setMargin(0);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tbox);
		
		tbox.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		row.add(col);
		return tbox;
	}

	public MaterialLabel addLabel(MaterialRow row, String text, String grid) {
		MaterialLabel label = new MaterialLabel(text);
		label.setTextAlign(TextAlign.CENTER);
		label.setBackgroundColor(Color.GREY_LIGHTEN_2);
		label.setLineHeight(46.25);
		label.setHeight("46.25px");
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		row.add(col);
		return label;
	}
	
	public SelectionPanel addSelectionPanel(MaterialRow row, String grid, int padding, HashMap<String, Object> valueMap) {
		SelectionPanel panel = new SelectionPanel();
		panel.setValues(valueMap);
		panel.setTextAlign(TextAlign.LEFT);
		panel.setLineHeight(46.25);
		panel.setElementPadding(padding);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(panel);
		row.add(col);
		return panel;
	}

}

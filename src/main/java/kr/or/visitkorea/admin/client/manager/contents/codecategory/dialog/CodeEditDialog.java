package kr.or.visitkorea.admin.client.manager.contents.codecategory.dialog;

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
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.codecategory.CodeCategoryMain;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CodeEditDialog extends DialogContent {

	private String mode = null;
	private MaterialLabel dialogTitle;
	private MaterialTextBox CodeValue;
	private SelectionPanel StatusPanel;
	private String codId = null;
	private MaterialTextBox CodeDesc;
	private CodeCategoryMain host;
	public CodeEditDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}
	
	@Override
	protected void onLoad() {
		setupForm();
	}

	public void setupForm() {
		
		this.mode = this.getParameters().get("mode").toString();
		CodeValue.setText("");
		CodeDesc.setText("");
		StatusPanel.setSelectionOnSingleMode("");
		this.codId = "";
		
		Console.log("status :: " + this.getParameters().get("status"));
		if (this.mode.equals("ADD")) {
			this.dialogTitle.setText("분류 추가");
			this.codId = IDUtil.uuid();
			this.StatusPanel.setSelectionOnSingleMode("미사용(비활성화)");
		} else if (this.mode.equals("UPDATE")) {
			this.dialogTitle.setText("분류 수정");
			if(this.getParameters().containsKey("codId"))
				this.codId = this.getParameters().get("codId").toString();
			if(this.getParameters().containsKey("value"))
				this.CodeValue.setText(this.getParameters().get("value").toString());
			if(this.getParameters().containsKey("desc"))
				this.CodeDesc.setText(this.getParameters().get("desc").toString());
			this.StatusPanel.setSelectionOnSingleMode(this.getParameters().get("status").toString());
		}
		if(this.getParameters().containsKey("host"))
			host = (CodeCategoryMain) this.getParameters().get("host");
	}
	
	@Override
	public void init() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel("분류 추가");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);
		
		MaterialRow row1 = new MaterialRow();
		addLabel(row1, "분류명", "s3",Color.GREY_LIGHTEN_2);
		this.add(row1);
		CodeValue = addTextBox(row1, "s8");
		CodeValue.setPlaceholder("새로운 분류명을 입력해주세요.");

		MaterialRow row2 = new MaterialRow();
		addLabel(row2, "가이드", "s3",Color.GREY_LIGHTEN_2);
		CodeDesc = addTextBox(row2, "s8");
		CodeDesc.setPlaceholder("해당 분류에 대한 간단한 설명을 적어주세요. (60자 이내)");
		this.add(row2);
		
		MaterialRow row3 = new MaterialRow();
		addLabel(row3, "사용여부", "s3",Color.GREY_LIGHTEN_2);

		HashMap<String, Object> StatusValueMap = new HashMap<>();
		StatusValueMap.put("사용", 1);
		StatusValueMap.put("미사용(비활성화)", 0);
		
		StatusPanel = addSelectionPanel(row3, "s8", 5, StatusValueMap);
		StatusPanel.setSelectionOnSingleMode("미사용(비활성화)");
		this.add(row3);
		
		MaterialRow row4 = new MaterialRow();
		MaterialLabel Tiplabel = addLabel(row4, "※ 분류명 수정 시 전체 콘텐츠에 적용됩니다.", "s11",null);
		Tiplabel.getElement().getStyle().setBackgroundColor("#ededed");
		this.add(row4);
		
		MaterialButton saveBtn = new MaterialButton("저장");
		saveBtn.setMarginRight(10);
		saveBtn.setFloat(Float.RIGHT);
		saveBtn.addClickHandler(event -> {
			submitProcess();
		});
		this.getButtonArea().add(saveBtn);
		
	}

	public String isValidate() {
		String msg = null;
		if (this.CodeValue.getValue().equals("")) 
			msg = "코드명을 입력해주세요.";
		else if (this.CodeDesc.getValue().equals(""))
			msg = "가이드명을 해주세요.";
		else if (this.StatusPanel.getSelectedValue() == null) 
			msg = "사용여부를 선택해주세요.";
		
		String value = CodeValue.getValue();
		String[] Check = new String[] {
				"!","~","`","@","#","$","%","^","*","(",")","+","=","{","}","[","]",":",
				";","\"","'","\\","|","<",">",",",".","/","?"
		};
		
		for (int i = 0; i < Check.length; i++) {
			if(value.indexOf(Check[i]) != -1) {
				msg = "특수문자는 입력이 불가합니다.";
				break;
			}
		}
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
		parameterJSON.put("cmd", new JSONString("UPDATE_CODE_INFO"));
		parameterJSON.put("mode", new JSONString(this.mode));
		parameterJSON.put("codId", new JSONString(this.codId));
		parameterJSON.put("value", new JSONString(this.CodeValue.getValue()));
		parameterJSON.put("desc", new JSONString(this.CodeDesc.getValue()));
		parameterJSON.put("status", new JSONNumber((int) this.StatusPanel.getSelectedValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			String ment = resultObj.get("header").isObject().get("ment").isString().stringValue();
			
			if (process.equals("success")) {
				host.qryList(true);
				if(this.mode.equals("UPDATE"))
					alert("코드 수정 성공", 400, 300, new String[] {ment});
				else
					alert("코드 추가 성공", 400, 300, new String[] {ment});
			} else {
				
				if(this.mode.equals("UPDATE"))
					alert("코드 수정 실패", 400, 300, new String[] {ment});
				else
					alert("코드 추가 실패", 400, 300, new String[] {ment});
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

	public MaterialLabel addLabel(MaterialRow row, String text, String grid,Color color) {
		MaterialLabel label = new MaterialLabel(text);
		label.setTextAlign(TextAlign.CENTER);
		if(color != null)
			label.setBackgroundColor(color);
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

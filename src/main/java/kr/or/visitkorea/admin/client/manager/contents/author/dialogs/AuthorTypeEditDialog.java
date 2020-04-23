package kr.or.visitkorea.admin.client.manager.contents.author.dialogs;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorTypeEditDialog extends DialogContent {
	private String mode = null;
	private String typeId;
	private AuthorTypeDialog host;
	private MaterialLabel dialogTitle;
	private MaterialTextBox typeName;
	private MaterialTextBox keyword;
	private MaterialLabel typeNameDupliLabel;
	private MaterialLabel keywordDupliLabel;
	private MaterialButton submitBtn;
	
	public AuthorTypeEditDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}
	
	@Override
	protected void onLoad() {
		if (submitBtn == null) {
			submitBtn = new MaterialButton("저장");
			submitBtn.setFloat(Float.RIGHT);
			submitBtn.setMarginRight(10);
			submitBtn.addClickHandler(event -> {
				submitProcess();
			});
			this.getButtonArea().add(submitBtn);
		}

		this.keywordDupliLabel.setDisplay(Display.NONE);
		this.keywordDupliLabel.setTextColor(Color.BLACK);
		this.typeNameDupliLabel.setDisplay(Display.NONE);
		this.typeNameDupliLabel.setTextColor(Color.BLACK);
		this.host = (AuthorTypeDialog) this.getParameters().get("host");
		this.mode = this.getParameters().get("mode").toString();
		if (mode.equals("ADD")) {
			dialogTitle.setText("작가 구분 추가");
			this.typeId = IDUtil.uuid();
			this.keyword.setValue("");
			this.typeName.setValue("");
		} else if (mode.equals("MODIFY")) {
			dialogTitle.setText("작가 구분 수정");
			this.typeId = this.getParameters().get("typeId").toString();
			this.keyword.setValue(this.getParameters().get("keyword").toString());
			this.typeName.setValue(this.getParameters().get("typeName").toString());
		}
	}

	@Override
	public void init() {
		dialogTitle = new MaterialLabel();
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);
		this.add(buildForm());
	}
	
	private void submitProcess() {
		if (this.keywordDupliLabel.getTextColor() != Color.GREEN || this.typeNameDupliLabel.getTextColor() != Color.GREEN) {
			alert("알림", 400, 300, new String[] { "입력값이 부적절한 항목이 있습니다." });
			return;
		}

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_AUTHOR_TYPE"));
		parameterJSON.put("mode", new JSONString(this.mode));
		parameterJSON.put("keyword", new JSONString(this.keyword.getValue()));
		parameterJSON.put("typeName", new JSONString(this.typeName.getValue()));
		parameterJSON.put("typeId", new JSONString(this.typeId));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			alert("알림", 400, 300, new String[] { 
					this.mode.equals("ADD") ? "작가 구분이 성공적으로 추가되었습니다." : "작가 구분이 성공적으로 수정되었습니다."
			});
			this.host.getMain().fetchAuthorType();
			this.host.qryList(true);
		});
	}
	
	private void duplicateChk(MaterialLabel resultLabel, String type) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_TYPE"));
		if (type.equals("keyword"))
			parameterJSON.put("keyword", new JSONString(this.keyword.getValue()));
		else if (type.equals("typeName"))
			parameterJSON.put("typeName", new JSONString(this.typeName.getValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyResultObj.get("result").isArray();

				resultLabel.setDisplay(Display.BLOCK);
				if (bodyResultArr.size() == 0) {
					resultLabel.setText("사용 가능합니다.");
					resultLabel.setTextColor(Color.GREEN);
				} else {
					resultLabel.setText("기존에 입력한 값과 동일한 값의 항목이 있습니다.");
					resultLabel.setTextColor(Color.RED);
				}
			}
		});
	}
	
	private MaterialPanel buildForm() {
		MaterialPanel panel = new MaterialPanel();
		panel.setPadding(10);
		
		MaterialRow row1 = new MaterialRow(); row1.setMargin(0);
		addLabel(row1, "s3", Color.GREY_LIGHTEN_3, "* 작가 구분명");
		typeName = addTextBox(row1, "s6");
		MaterialButton typeNameDupliBtn = addBtn(row1, "s3", "중복확인");
		typeNameDupliBtn.addClickHandler(event -> {
			if (typeName.getValue().equals("")) {
				typeNameDupliLabel.setText("값을 입력 후 중복확인를 해주세요.");
				typeNameDupliLabel.setTextColor(Color.RED);
				typeNameDupliLabel.setDisplay(Display.BLOCK);
				return;
			}
			duplicateChk(typeNameDupliLabel, "typeName");
		});
		panel.add(row1);

		MaterialRow row10 = new MaterialRow();
		typeNameDupliLabel = new MaterialLabel();
		typeNameDupliLabel.setGrid("s9");
		typeNameDupliLabel.setFloat(Float.RIGHT);
		typeNameDupliLabel.setDisplay(Display.NONE);
		row10.add(typeNameDupliLabel);
		panel.add(row10);
		
		MaterialRow row2 = new MaterialRow(); row2.setMargin(0);
		addLabel(row2, "s3", Color.GREY_LIGHTEN_3, "* 구분 약어");
		keyword = addTextBox(row2, "s6");
		keyword.setMaxLength(2);
		MaterialButton keywordDupliBtn = addBtn(row2, "s3", "중복확인");
		keywordDupliBtn.addClickHandler(event -> {
			if (keyword.getValue().equals("")) {
				keywordDupliLabel.setText("값을 입력 후 중복확인를 해주세요.");
				keywordDupliLabel.setTextColor(Color.RED);
				keywordDupliLabel.setDisplay(Display.BLOCK);
				return;
			}
			duplicateChk(keywordDupliLabel, "keyword");
		});
		panel.add(row2);

		MaterialRow row20 = new MaterialRow();
		keywordDupliLabel = new MaterialLabel();
		keywordDupliLabel.setGrid("s9");
		keywordDupliLabel.setFloat(Float.RIGHT);
		keywordDupliLabel.setDisplay(Display.NONE);
		row20.add(keywordDupliLabel);
		
		panel.add(row20);

		return panel;
	}
	
	private MaterialLabel addLabel(MaterialRow row, String grid, Color color, String text) {
		MaterialLabel label = new MaterialLabel(text);
		label.setText(text);
		label.setBackgroundColor(color);
		label.setLineHeight(46);
		label.setHeight("46px");
		label.setTextAlign(TextAlign.CENTER);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		row.add(col);
		return label;
	}
	
	private MaterialTextBox addTextBox(MaterialRow row, String grid) {
		MaterialTextBox tBox = new MaterialTextBox();
		tBox.setMargin(0);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tBox);
		row.add(col);
		return tBox;
	}
	
	private MaterialButton addBtn(MaterialRow row, String grid, String text) {
		MaterialButton btn = new MaterialButton(text);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(btn);
		row.add(col);
		return btn;
	}
	
	@Override
	public int getHeight() {
		return 400;
	}

}

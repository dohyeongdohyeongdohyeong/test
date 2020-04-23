package kr.or.visitkorea.admin.client.manager.contents.database.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentsList;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class InsertDatabaseContent extends DialogContent {
	protected DatabaseContentsList databaseContentsList;
	private MaterialLabel alertLabel;
	private MaterialTextBox contentIdText;
	private MaterialTextBox cotIdText;
	private MaterialComboBox<Object> contenttypecombo;
	private MaterialTextBox titleText;
	private MaterialButton insertButton;
	private MaterialButton contentIdButton;
	private String otdId;
	private boolean contentIdDuplicatedCheck = false;
	private boolean contentIdCheck = false;
	
	public InsertDatabaseContent(MaterialExtentsWindow window) {
		super(window);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		if (this.getParameters().containsKey("OTD_ID") && this.getParameters().get("OTD_ID") != null)
			this.otdId = getParameters().get("OTD_ID").toString();
	}
	
	public void buildContent() {
		addDefaultButtons();
		setBackgroundColor(Color.GREY_LIGHTEN_5);
		
		MaterialLabel title = new MaterialLabel("데이터베이스 컨텐츠 등록");
		title.setFontSize("1.4em");
		title.setFontWeight(FontWeight.BOLD);
		title.setTextColor(Color.BLUE);
		title.setPaddingTop(10);
		title.setPaddingLeft(30);
		this.add(title);
		this.add(buildWriteArea());
		
		insertButton = new MaterialButton("등록");
		insertButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		insertButton.addClickHandler(event -> {
			if (!contentIdDuplicatedCheck) {
				alertLabel.setText("컨텐츠 ID가 확정되지 않았습니다. 확인해주세요.");
				return;
			}
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_CONTENTID_CHECK"));
			parameterJSON.put("CONTENT_ID", new JSONString(contentIdText.getText().toString()));
			parameterJSON.put("COT_ID", new JSONString(cotIdText.getText().toString()));
			parameterJSON.put("CONTENT_TYPE", new JSONString(contenttypecombo.getSelectedValue().get(0).toString()));
			parameterJSON.put("TITLE", new JSONString(titleText.getText().toString()));
			if (this.getParameters().containsKey("OTD_ID") && this.getParameters().get("OTD_ID") != null)
				parameterJSON.put("OTD_ID", new JSONString(otdId));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject result = (JSONObject) resultObj.get("body");
						contentIdButton.setEnabled(false);
						insertButton.setEnabled(false);
						alertLabel.setText("해당 컨텐츠 등록되었습니다.");
						alertLabel.setTextColor(Color.GREEN_LIGHTEN_2);
					} else {
						alertLabel.setText("등록에 실패했습니다. 관리자에게 문의하세요.");
						alertLabel.setTextColor(Color.RED_LIGHTEN_2);
					}
				}
			});
		});
		this.addButton(insertButton);
	}
	
	private MaterialPanel buildWriteArea() {
		
		MaterialPanel writePanel = new MaterialPanel();
		writePanel.setLayoutPosition(Position.ABSOLUTE);
		writePanel.setLeft(30);
		writePanel.setRight(30);
		writePanel.setHeight("80px");
		
		MaterialLabel cidLabel = new MaterialLabel("CONTENT ID");
		cidLabel.setLayoutPosition(Position.ABSOLUTE);
		cidLabel.setTop(100);
		cidLabel.setLeft(50);
		cidLabel.setFontSize("18px");
		this.add(cidLabel);
		
		contentIdText = new MaterialTextBox();
		contentIdText.setLayoutPosition(Position.ABSOLUTE);
		contentIdText.setTop(70);
		contentIdText.setLeft(200);
		contentIdText.setWidth("400px");
		contentIdText.setMaxLength(12);
		contentIdText.setText("");
		contentIdText.addValueChangeHandler(event -> {
			if (contentIdText.getValue().length() > 0)
				contentIdCheck = true;
			else
				contentIdCheck = false;
		});
		this.add(contentIdText);
		
		contentIdButton = new MaterialButton("중복 확인");
		contentIdButton.setLayoutPosition(Position.ABSOLUTE);
		contentIdButton.setBackgroundColor(Color.GREEN_LIGHTEN_2);
		contentIdButton.setTop(95);
		contentIdButton.setRight(50);
		contentIdButton.addClickHandler(event -> {
			if (!contentIdCheck) {
				alertLabel.setText("컨텐츠 ID가 입력되지 않았습니다. 확인해주세요.");
				return;
			}
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("SELECT_CONTENTID_CHECK"));
			parameterJSON.put("CONTENT_ID", new JSONString(contentIdText.getText().toString()));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject result = (JSONObject) resultObj.get("body");
						if (result.get("result").isString().stringValue().equals("1")) {
							contentIdDuplicatedCheck = true;
							contentIdText.setReadOnly(true);
							alertLabel.setText("해당 컨텐츠 ID로 확정되었습니다.");
							alertLabel.setTextColor(Color.GREEN_LIGHTEN_2);
						} else {
							alertLabel.setText("해당 컨텐츠 ID는 현재 사용 중입니다.");
							alertLabel.setTextColor(Color.RED_LIGHTEN_2);
						}
					} else {
						alertLabel.setText("조회에 실패했습니다. 관리자에게 문의하세요.");
						alertLabel.setTextColor(Color.RED_LIGHTEN_2);
					}
				}
			});
		});
		this.add(contentIdButton);
		
		MaterialLabel cotIdLabel = new MaterialLabel("COT ID");
		cotIdLabel.setLayoutPosition(Position.ABSOLUTE);
		cotIdLabel.setTop(170);
		cotIdLabel.setLeft(50);
		cotIdLabel.setFontSize("18px");
		this.add(cotIdLabel);
		
		cotIdText = new MaterialTextBox();
		cotIdText.setLayoutPosition(Position.ABSOLUTE);
		cotIdText.setTop(145);
		cotIdText.setLeft(200);
		cotIdText.setWidth("400px");
		cotIdText.setText(IDUtil.uuid());
		cotIdText.setReadOnly(true);
		this.add(cotIdText);
		
		MaterialLabel contentTypeLabel = new MaterialLabel("컨텐트 타입");
		contentTypeLabel.setLayoutPosition(Position.ABSOLUTE);
		contentTypeLabel.setTop(240);
		contentTypeLabel.setLeft(50);
		contentTypeLabel.setFontSize("18px");
		this.add(contentTypeLabel);
		
		contenttypecombo = new MaterialComboBox<Object>();
		contenttypecombo.setLayoutPosition(Position.ABSOLUTE);
		contenttypecombo.setTop(210);
		contenttypecombo.setLeft(200);
		contenttypecombo.setWidth("400px");
		contenttypecombo.setSelectedIndex(0);
		contenttypecombo.addItem("관광지", 12);
		contenttypecombo.addItem("문화시설", 14);
		contenttypecombo.addItem("축제행사공연", 15);
		contenttypecombo.addItem("여행코스", 25);
		contenttypecombo.addItem("레포츠", 28);
		contenttypecombo.addItem("숙박", 32);
		contenttypecombo.addItem("쇼핑", 38);
		contenttypecombo.addItem("음식점", 39);
		contenttypecombo.addItem("시티투어", 25000);
		this.add(contenttypecombo);
		
		MaterialLabel titleLabel = new MaterialLabel("제목");
		titleLabel.setLayoutPosition(Position.ABSOLUTE);
		titleLabel.setTop(310);
		titleLabel.setLeft(50);
		titleLabel.setFontSize("18px");
		this.add(titleLabel);
		
		titleText = new MaterialTextBox();
		titleText.setLayoutPosition(Position.ABSOLUTE);
		titleText.setTop(280);
		titleText.setLeft(200);
		titleText.setWidth("400px");
		this.add(titleText);
		
		alertLabel = new MaterialLabel();
		alertLabel.setLayoutPosition(Position.ABSOLUTE);
		alertLabel.setTextColor(Color.RED_LIGHTEN_2);
		alertLabel.setFontWeight(FontWeight.BOLD);
		alertLabel.setTop(380);
		alertLabel.setLeft(50);
		alertLabel.setFontSize("18px");
		alertLabel.setWidth("400px");
		this.add(alertLabel);
		
		return writePanel;
	}
	
	@Override
	public void init() {
		buildContent();
	}
	
	@Override
	public int getHeight() {
		return 440;
	}
}

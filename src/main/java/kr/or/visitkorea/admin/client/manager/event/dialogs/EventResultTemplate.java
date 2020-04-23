package kr.or.visitkorea.admin.client.manager.event.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness; 
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentsList;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsResultTemplate;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.StringUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventResultTemplate extends DialogContent {
	protected DatabaseContentsList databaseContentsList;
	private MaterialLabel alertLabel;
	private MaterialTextBox prizeNmText;
	private MaterialTextBox giftDescText;
	private MaterialComboBox<Object> subEvtCombo;
	private MaterialComboBox<Object> giftCombo;

	private MaterialButton insertButton;

	private String evtId;
	private EventContentsResultTemplate parent;
	
	public EventResultTemplate(MaterialExtentsWindow window, EventContentsResultTemplate parent) {
		super(window);
		this.parent = parent;
	}
	
	@Override
	public void init() {

	}
	
	
	@Override
	protected void onLoad() {
		super.onLoad();
		this.evtId = String.valueOf(getParameters().get("evtId"));
		buildContent();
	}
	
	public void buildContent() {
		addDefaultButtons();
		setBackgroundColor(Color.GREY_LIGHTEN_5);
		
		MaterialLabel title = new MaterialLabel("이벤트 당첨자 발표 템플릿");
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
			
			String subEvtId = subEvtCombo.getSelectedValue().get(0).toString();
			String gftId = giftCombo.getSelectedValue().get(0).toString();
			String prizeNm = prizeNmText.getText().toString();
			String giftDesc = giftDescText.getText().toString();
			
			if (subEvtCombo.getSelectedIndex() == 0) {
				alertLabel.setText("프로그램을 선택해주세요");
				return;
			}
			
			if ("프로그램을 선택해주세요".equals(giftCombo.getSelectedValue().get(0).toString())) {
				alertLabel.setText("경품을 선택해 주세요");
				return;
			}
			
			if (StringUtil.isEmpty(prizeNm)) {
				alertLabel.setText("상이름을 입력해 주세요");
				return;
			}
			
			if (StringUtil.isEmpty(giftDesc)) {
				alertLabel.setText("경품명/수량을 입력해 주세요");
				return;
			}

			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("SAVE_EVENT_ANNOUNCE_PAGE"));
			
			parameterJSON.put("evtId", new JSONString(evtId));
			parameterJSON.put("subEvtId", new JSONString(subEvtId));
			parameterJSON.put("giftId", new JSONString(gftId));
			parameterJSON.put("prizeNm", new JSONString(prizeNm));
			parameterJSON.put("giftDesc", new JSONString(giftDesc));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						insertButton.setEnabled(false);
						parent.loadData(null);
						getMaterialExtentsWindow().closeDialog();

						MaterialToast.fireToast("해당 컨텐츠 등록되었습니다.", 3000);
					} else {
						
						MaterialToast.fireToast("등록에 실패했습니다. 관리자에게 문의하세요.", 3000);
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
		
		
		MaterialLabel contentTypeLabel = new MaterialLabel("프로그램 선택");
		contentTypeLabel.setLayoutPosition(Position.ABSOLUTE);
		contentTypeLabel.setTop(100);
		contentTypeLabel.setLeft(50);
		contentTypeLabel.setFontSize("18px");
		this.add(contentTypeLabel);
		
		subEvtCombo = new MaterialComboBox<Object>();
		subEvtCombo.setLayoutPosition(Position.ABSOLUTE);
		subEvtCombo.setTop(70);
		subEvtCombo.setLeft(200);
		subEvtCombo.setWidth("200px");
		subEvtCombo.addItem("프로그램 선택", 0);
		
		
		JSONObject parameterJSON = new JSONObject();
		
		parameterJSON.put("cmd", new JSONString("SELECT_EVENT_SUB_LIST"));
		parameterJSON.put("evtId", new JSONString(String.valueOf(evtId)));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");

					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과없음", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						subEvtCombo.addItem(obj.get("TITLE").isString().stringValue(), obj.get("SUB_EVT_ID").isString().stringValue());
					}
					
					subEvtCombo.setSelectedIndex(0);
				}
			}
		});
		
		subEvtCombo.addValueChangeHandler(ee->{
			giftCombo.clear();
			if(subEvtCombo.getSelectedIndex() == 0) {
				giftCombo.addItem("프로그램을 선택해주세요",0);
				MaterialToast.fireToast("프로그램을 선택해주세요", 3000);
				return;
			}
			
		
			JSONObject paramJSON = new JSONObject();
			paramJSON.put("cmd", new JSONString("SELECT_EVENT_GIFT_LIST"));
			String subEvtId = subEvtCombo.getValues().get(subEvtCombo.getSelectedIndex()).toString();
			paramJSON.put("evtId", new JSONString(evtId));
			paramJSON.put("subEvtId", new JSONString(subEvtId));
			paramJSON.put("isNotWin", new JSONString("0"));
			
			VisitKoreaBusiness.post("call", paramJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject bodyObj = (JSONObject) resultObj.get("body");
						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
						int usrCnt = bodyResultObj.size();
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							String title = 	obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "제목없음";
							giftCombo.addItem(title, obj.get("GFT_ID").isString().stringValue());
						}
						giftCombo.setSelectedIndex(0);
						giftCombo.setEnabled(true);

					}
				}
			});
			
		
		});
		
		this.add(subEvtCombo);
		
		
		giftCombo = new MaterialComboBox<Object>();
		giftCombo.setLayoutPosition(Position.ABSOLUTE);
		giftCombo.setTop(70);
		giftCombo.setLeft(450);
		giftCombo.setWidth("200px");
		giftCombo.addItem("이벤트를 선택해주세요",0);
		giftCombo.setSelectedIndex(0);
		giftCombo.setEnabled(false);
		this.add(giftCombo);
	
		
	
		MaterialLabel prizeNmLabel = new MaterialLabel("상이름");
		prizeNmLabel.setLayoutPosition(Position.ABSOLUTE);
		prizeNmLabel.setTop(200);
		prizeNmLabel.setLeft(50);
		prizeNmLabel.setFontSize("18px");
		this.add(prizeNmLabel);
		
	
		prizeNmText = new MaterialTextBox();
		prizeNmText.setLayoutPosition(Position.ABSOLUTE);
		prizeNmText.setTop(170);
		prizeNmText.setLeft(200);
		prizeNmText.setWidth("400px");
		prizeNmText.setMaxLength(50);
		prizeNmText.setText("");
		this.add(prizeNmText);
		
		MaterialLabel giftDescLabel = new MaterialLabel("경품명/수량");
		giftDescLabel.setLayoutPosition(Position.ABSOLUTE);
		giftDescLabel.setTop(300);
		giftDescLabel.setLeft(50);
		giftDescLabel.setFontSize("18px");
		this.add(giftDescLabel);
		
	
		giftDescText = new MaterialTextBox();
		giftDescText.setLayoutPosition(Position.ABSOLUTE);
		giftDescText.setTop(270);
		giftDescText.setLeft(200);
		giftDescText.setWidth("400px");
		giftDescText.setMaxLength(50);
		giftDescText.setText("");
		this.add(giftDescText);

		
		
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
	public int getHeight() {
		return 440;
	}
}

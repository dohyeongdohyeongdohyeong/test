package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class CitytourDetailItem extends MaterialPanel {

	private JSONObject jsonObj;
	private String fee;
	private String ctiId;
	private String ctfId;
	private String title;
	private MaterialLabel titleLabel;
	private MaterialLabel summaryPanel;
	private String summary;
	private MaterialLabel feeLabel;
	private boolean saved;
	private MaterialInput titleInput;
	private MaterialInput summaryInput;
	private MaterialInput feeInput;
	private JSONObject intro;
	private String serviceDate;
	private String serviceTime;
	private String addInfo;
	private MaterialLabel serviceDateLabel;
	private MaterialInput serviceDateInput;
	private MaterialLabel serviceTimeLabel;
	private MaterialInput serviceTimeInput;
	private MaterialLabel addInfoLabel;
	private MaterialInput addInfoInput;
	private MaterialLabel summaryDisp;
	private MaterialLabel feeDisp;
	private MaterialLabel serviceDateDisp;
	private MaterialLabel serviceTimeDisp;
	private MaterialLabel addInfoDisp;
	private MaterialLabel serviceTimeDisp1;

	public CitytourDetailItem(JSONObject jsonObject, JSONObject intro) {
		super();
		
		this.jsonObj = jsonObject;
		this.intro = intro;
		
		init();
		

		if (this.intro == null) {
			
			this.ctiId =  IDUtil.uuid().toString();
			
		}else {
			
			this.ctiId =  this.intro.get("CTI_ID").isString().stringValue();
			
		}
		
		if (this.jsonObj == null) {
		
			this.saved = false;
			this.ctfId = IDUtil.uuid().toString();
			
		}else {
			
			this.saved = true;
			
			if (this.ctiId == null) {
				this.ctiId =  this.jsonObj.get("CTI_ID").isString().stringValue();
			}
			
			this.summary =  this.jsonObj.get("SUMMARY").isString().stringValue();
			this.ctfId =  this.jsonObj.get("CTF_ID").isString().stringValue();
			this.fee =  this.jsonObj.get("FEE").isString().stringValue();
			this.title =  this.jsonObj.get("TITLE").isString().stringValue();
			if (this.jsonObj.get("SERVICE_DATE") != null) this.serviceDate =  this.jsonObj.get("SERVICE_DATE").isString().stringValue();
			if (this.jsonObj.get("SERVICE_TIME") != null)this.serviceTime =  this.jsonObj.get("SERVICE_TIME").isString().stringValue();
			if (this.jsonObj.get("ADDITIONAL_INFORMATION") != null)this.addInfo =  this.jsonObj.get("ADDITIONAL_INFORMATION").isString().stringValue();
			
			this.titleLabel.setText(this.title);
			this.summaryPanel.setText(this.summary);
			this.feeLabel.setText(this.fee);
			if (this.serviceDate != null) this.serviceDateLabel.setText(this.serviceDate); 
			if (this.serviceTime != null) this.serviceTimeLabel.setText(this.serviceTime); 
			if (this.addInfo != null) this.addInfoLabel.setText(this.addInfo); 
							
		}
		
	}
	
	private CitytourDetailItem getPanel() {
		return this;
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("278px");
		this.setMargin(10);
		this.setBackgroundColor(Color.GREY_LIGHTEN_3);
		
		MaterialPanel titlePanel =new MaterialPanel();
		titlePanel.setLayoutPosition(Position.ABSOLUTE);
		titlePanel.setTop(0);
		titlePanel.setLeft(0);
		titlePanel.setWidth("100%");
		titlePanel.setHeight("24px");
		titlePanel.setBackgroundColor(Color.GREY_LIGHTEN_1);
		
		titleLabel =new MaterialLabel();
		titleLabel.setLayoutPosition(Position.ABSOLUTE);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setTop(0);
		titleLabel.setLeft(5);
		titleLabel.setHeight("24px");
		
		titleInput = new MaterialInput(InputType.TEXT);
		titleInput.setLayoutPosition(Position.ABSOLUTE);
		titleInput.setTextAlign(TextAlign.LEFT);
		titleInput.setTop(0);
		titleInput.setLeft(5);
		titleInput.setWidth("800px");
		titleInput.setHeight("24px");
		titleInput.addKeyUpHandler(event->{
			titleLabel.setText(titleInput.getValue());
			title = titleInput.getValue();
			applyDatabaseTitle();
		});
		
		MaterialPanel functionContainer = new MaterialPanel();
		functionContainer.setLayoutPosition(Position.ABSOLUTE);
		functionContainer.setTop(0);
		functionContainer.setRight(0);
		functionContainer.setWidth("100%");
		functionContainer.setHeight("24px");
		
		MaterialIcon editFunction = new MaterialIcon(IconType.EDIT);
		editFunction.setFloat(Style.Float.RIGHT);
		editFunction.setMarginLeft(0);
		editFunction.addClickHandler(event->{
			labelVisible(false);
		});
		
		MaterialIcon saveFunction = new MaterialIcon(IconType.SAVE);
		saveFunction.setFloat(Style.Float.RIGHT);
		saveFunction.setMarginLeft(0);
		saveFunction.addClickHandler(event->{
			labelVisible(true);
			applyDatabaseSave();
		});
		
		MaterialIcon deleteFunction = new MaterialIcon(IconType.REMOVE);
		deleteFunction.setFloat(Style.Float.RIGHT);
		deleteFunction.setMarginLeft(0);
		deleteFunction.addClickHandler(event->{
			getPanel().removeFromParent();
			applyDatabaseRemove();
		});
		
		functionContainer.add(deleteFunction);
		functionContainer.add(saveFunction);
		functionContainer.add(editFunction);
		
		summaryDisp =new MaterialLabel("코스");
		summaryDisp.setTextAlign(TextAlign.LEFT);
		summaryDisp.setLayoutPosition(Position.ABSOLUTE);
		summaryDisp.setTop(25);
		summaryDisp.setLeft(0);
		summaryDisp.setPadding(5);
		summaryDisp.setWidth("95px");
		summaryDisp.setHeight("50px");
		summaryDisp.setBackgroundColor(Color.GREY_LIGHTEN_4);
		
		summaryPanel =new MaterialLabel();
		summaryPanel.setTextAlign(TextAlign.LEFT);
		summaryPanel.setLayoutPosition(Position.ABSOLUTE);
		summaryPanel.setTop(25);
		summaryPanel.setLeft(100);
		summaryPanel.setPadding(5);
		summaryPanel.setWidth("785px");
		summaryPanel.setHeight("50px");
		summaryPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);

		summaryInput = new MaterialInput(InputType.TEXT);
		summaryInput.setTextAlign(TextAlign.LEFT);
		summaryInput.setLayoutPosition(Position.ABSOLUTE);
		summaryInput.setTop(41);
		summaryInput.setLeft(100);
		summaryInput.setPadding(5);
		summaryInput.setWidth("785px");
		summaryInput.setHeight("25px");
		summaryInput.setBackgroundColor(Color.GREY_LIGHTEN_4);
		summaryInput.addKeyUpHandler(event->{
			summaryPanel.setText(summaryInput.getValue());
			summary = summaryInput.getValue();
			applyDatabaseSummary();
		});

		feeDisp =new MaterialLabel("이용요금");
		feeDisp.setBorderTop("1px dotted #cccccc");
		feeDisp.setTextAlign(TextAlign.LEFT);
		feeDisp.setLayoutPosition(Position.ABSOLUTE);
		feeDisp.setTop(75);
		feeDisp.setLeft(0);
		feeDisp.setPadding(5);
		feeDisp.setWidth("95px");
		feeDisp.setHeight("50px");
		feeDisp.setBackgroundColor(Color.GREY_LIGHTEN_4);

		feeLabel =new MaterialLabel();
		feeLabel.setBorderTop("1px dotted #cccccc");
		feeLabel.setTextAlign(TextAlign.LEFT);
		feeLabel.setLayoutPosition(Position.ABSOLUTE);
		feeLabel.setTop(75);
		feeLabel.setLeft(100);
		feeLabel.setPadding(5);
		feeLabel.setWidth("785px");
		feeLabel.setHeight("50px");
		feeLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		
		feeInput = new MaterialInput(InputType.TEXT);
		feeInput.setTextAlign(TextAlign.LEFT);
		feeInput.setLayoutPosition(Position.ABSOLUTE);
		feeInput.setTop(92);
		feeInput.setLeft(100);
		feeInput.setPadding(5);
		feeInput.setWidth("785px");
		feeInput.setHeight("25px");
		feeInput.setBackgroundColor(Color.GREY_LIGHTEN_4);
		feeInput.addKeyUpHandler(event->{
			feeLabel.setText(feeInput.getValue());
			fee = feeInput.getValue();
			applyDatabaseFee();
		});

		serviceDateDisp =new MaterialLabel("운행 일시");
		serviceDateDisp.setBorderTop("1px dotted #cccccc");
		serviceDateDisp.setTextAlign(TextAlign.LEFT);
		serviceDateDisp.setLayoutPosition(Position.ABSOLUTE);
		serviceDateDisp.setTop(125);
		serviceDateDisp.setLeft(0);
		serviceDateDisp.setPadding(5);
		serviceDateDisp.setWidth("95px");
		serviceDateDisp.setHeight("50px");
		serviceDateDisp.setBackgroundColor(Color.GREY_LIGHTEN_4);

		serviceDateLabel =new MaterialLabel();
		serviceDateLabel.setBorderTop("1px dotted #cccccc");
		serviceDateLabel.setTextAlign(TextAlign.LEFT);
		serviceDateLabel.setLayoutPosition(Position.ABSOLUTE);
		serviceDateLabel.setTop(125);
		serviceDateLabel.setLeft(100);
		serviceDateLabel.setPadding(5);
		serviceDateLabel.setWidth("785px");
		serviceDateLabel.setHeight("50px");
		serviceDateLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		
		serviceDateInput = new MaterialInput(InputType.TEXT);
		serviceDateInput.setTextAlign(TextAlign.LEFT);
		serviceDateInput.setLayoutPosition(Position.ABSOLUTE);
		serviceDateInput.setTop(140);
		serviceDateInput.setLeft(100);
		serviceDateInput.setPadding(5);
		serviceDateInput.setWidth("785px");
		serviceDateInput.setHeight("25px");
		serviceDateInput.setBackgroundColor(Color.GREY_LIGHTEN_4);
		serviceDateInput.addKeyUpHandler(event->{
			serviceDateLabel.setText(serviceDateInput.getValue());
			serviceDate = serviceDateInput.getValue();
			applyDatabaseServiceDate();
		});

		serviceTimeDisp =new MaterialLabel("1회당");
		serviceTimeDisp.setBorderTop("1px dotted #cccccc");
		serviceTimeDisp.setTextAlign(TextAlign.LEFT);
		serviceTimeDisp.setLayoutPosition(Position.ABSOLUTE);
		serviceTimeDisp.setTop(175);
		serviceTimeDisp.setLeft(0);
		serviceTimeDisp.setPadding(5);
		serviceTimeDisp.setWidth("95px");
		serviceTimeDisp.setHeight("25px");
		serviceTimeDisp.setBackgroundColor(Color.GREY_LIGHTEN_4);

		serviceTimeDisp1 =new MaterialLabel("소요시간");
		serviceTimeDisp1.setTextAlign(TextAlign.LEFT);
		serviceTimeDisp1.setLineHeight(10);
		serviceTimeDisp1.setLayoutPosition(Position.ABSOLUTE);
		serviceTimeDisp1.setTop(200);
		serviceTimeDisp1.setLeft(0);
		serviceTimeDisp1.setPadding(5);
		serviceTimeDisp1.setWidth("95px");
		serviceTimeDisp1.setHeight("26px");
		serviceTimeDisp1.setBackgroundColor(Color.GREY_LIGHTEN_4);

		serviceTimeLabel =new MaterialLabel();
		serviceTimeLabel.setBorderTop("1px dotted #cccccc");
		serviceTimeLabel.setTextAlign(TextAlign.LEFT);
		serviceTimeLabel.setLayoutPosition(Position.ABSOLUTE);
		serviceTimeLabel.setTop(175);
		serviceTimeLabel.setLeft(100);
		serviceTimeLabel.setPadding(5);
		serviceTimeLabel.setWidth("785px");
		serviceTimeLabel.setHeight("50px");
		serviceTimeLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		
		serviceTimeInput = new MaterialInput(InputType.TEXT);
		serviceTimeInput.setTextAlign(TextAlign.LEFT);
		serviceTimeInput.setLayoutPosition(Position.ABSOLUTE);
		serviceTimeInput.setTop(190);
		serviceTimeInput.setLeft(100);
		serviceTimeInput.setPadding(5);
		serviceTimeInput.setWidth("785px");
		serviceTimeInput.setHeight("25px");
		serviceTimeInput.setBackgroundColor(Color.GREY_LIGHTEN_4);
		serviceTimeInput.addKeyUpHandler(event->{
			serviceTimeLabel.setText(serviceTimeInput.getValue());
			serviceTime = serviceTimeInput.getValue();
			applyDatabaseServiceTime();
		});

		addInfoDisp =new MaterialLabel("추가 정보");
		addInfoDisp.setBorderTop("1px dotted #cccccc");
		addInfoDisp.setTextAlign(TextAlign.LEFT);
		addInfoDisp.setLayoutPosition(Position.ABSOLUTE);
		addInfoDisp.setTop(225);
		addInfoDisp.setLeft(0);
		addInfoDisp.setPadding(5);
		addInfoDisp.setWidth("95px");
		addInfoDisp.setHeight("50px");
		addInfoDisp.setBackgroundColor(Color.GREY_LIGHTEN_4);

		addInfoLabel =new MaterialLabel();
		addInfoLabel.setBorderTop("1px dotted #cccccc");
		addInfoLabel.setTextAlign(TextAlign.LEFT);
		addInfoLabel.setLayoutPosition(Position.ABSOLUTE);
		addInfoLabel.setTop(225);
		addInfoLabel.setLeft(100);
		addInfoLabel.setPadding(5);
		addInfoLabel.setWidth("785px");
		addInfoLabel.setHeight("50px");
		addInfoLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		
		addInfoInput = new MaterialInput(InputType.TEXT);
		addInfoInput.setTextAlign(TextAlign.LEFT);
		addInfoInput.setLayoutPosition(Position.ABSOLUTE);
		addInfoInput.setTop(242);
		addInfoInput.setLeft(100);
		addInfoInput.setPadding(5);
		addInfoInput.setWidth("785px");
		addInfoInput.setHeight("25px");
		addInfoInput.setBackgroundColor(Color.GREY_LIGHTEN_4);
		addInfoInput.addKeyUpHandler(event->{
			addInfoLabel.setText(addInfoInput.getValue());
			addInfo = addInfoInput.getValue();
			applyDatabaseAddInfo();
		});

		titlePanel.add(functionContainer);
		titlePanel.add(titleLabel);
		titlePanel.add(summaryPanel);
		titlePanel.add(feeLabel);
		titlePanel.add(serviceDateLabel);
		titlePanel.add(serviceTimeLabel);
		titlePanel.add(addInfoLabel);
		
		titlePanel.add(titleInput);
		titlePanel.add(summaryInput);
		titlePanel.add(feeInput);
		titlePanel.add(serviceDateInput);
		titlePanel.add(serviceTimeInput);
		titlePanel.add(addInfoInput);	
		
		titlePanel.add(summaryDisp);
		titlePanel.add(feeDisp);
		titlePanel.add(serviceDateDisp);
		titlePanel.add(serviceTimeDisp);
		titlePanel.add(serviceTimeDisp1);
		titlePanel.add(addInfoDisp);
		
		this.add(titlePanel);
		labelVisible(true);
	}
	
	private void applyDatabaseSave() {
		
		if (this.saved) {
			databaseUpdate();
		} else {
			databaseInsert();
		}
		
	}
	
	private void databaseUpdate() {

		MaterialWidget parentWidget = (MaterialWidget)getParent();
		int widgetIndex = parentWidget.getWidgetIndex(this);
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("ctfId", new JSONString(this.ctfId));
		paramObj.put("fee", new JSONString(this.fee));
		paramObj.put("summary", new JSONString(this.summary));
		paramObj.put("title", new JSONString(this.title));
		paramObj.put("ordering", new JSONNumber(widgetIndex));
		paramObj.put("serviceDate", new JSONString(this.serviceDate));
		paramObj.put("serviceTime", new JSONString(this.serviceTime));
		paramObj.put("addInfo", new JSONString(this.addInfo));
	
		invokeQuery("UPDATE_ALL_CITYTOUR_SINGLE_ROW", paramObj, new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
			
		});
		
	}
	
	private void databaseInsert() {

		MaterialWidget parentWidget = (MaterialWidget)getParent();
		int widgetIndex = parentWidget.getWidgetIndex(this);
		
		if (this.summary == null) this.summary = "";
		if (this.fee == null) this.fee = "";
		if (this.title == null) this.title = "";
		if (this.serviceDate == null) this.serviceDate = "";
		if (this.serviceTime == null) this.serviceTime = "";
		if (this.addInfo == null) this.addInfo = "";
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("ctfId", new JSONString(this.ctfId));
		paramObj.put("ctiId", new JSONString(this.ctiId));
		paramObj.put("fee", new JSONString(this.fee));
		paramObj.put("summary", new JSONString(this.summary));
		paramObj.put("title", new JSONString(this.title));
		paramObj.put("ordering", new JSONNumber(widgetIndex));
		paramObj.put("serviceDate", new JSONString(this.serviceDate));
		paramObj.put("serviceTime", new JSONString(this.serviceTime));
		paramObj.put("addInfo", new JSONString(this.addInfo));
	
		invokeQuery("INSERT_CITYTOUR_SINGLE_ROW", paramObj, new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
			
		});
		
	}

	private void applyDatabaseRemove() {
		
		if (this.saved) {
			executeRemove();
		}
		
	}
	
	private void applyDatabaseServiceDate() {
		
		if (this.saved) {
			executeUpdate("SERVICE_DATE", new JSONString(serviceDate));
		}
	}
	
	private void applyDatabaseServiceTime() {
		
		if (this.saved) {
			executeUpdate("SERVICE_TIME", new JSONString(serviceTime));
		}
	}
	
	private void applyDatabaseAddInfo() {
		
		if (this.saved) {
			executeUpdate("ADDITIONAL_INFORMATION", new JSONString(addInfo));
		}
	}

	private void applyDatabaseFee() {
		
		if (this.saved) {
			executeUpdate("FEE", new JSONString(fee));
		}
		
	}

	private void applyDatabaseSummary() {
		
		if (this.saved) {
			executeUpdate("SUMMARY", new JSONString(summary));
		}
		
	}

	private void applyDatabaseTitle() {
		
		if (this.saved) {
			executeUpdate("TITLE", new JSONString(title));
		}
	}

	private void executeRemove() {

		JSONObject paramObj = new JSONObject();
		paramObj.put("ctfId", new JSONString(this.ctfId));
		
		invokeQuery("DELETE_CITYTOUR_SINGLE_ROW", paramObj, new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
			
		});

	}

	private void executeUpdate(String columnName, JSONValue value) {

		JSONObject paramObj = new JSONObject();
		paramObj.put("colTitle", new JSONString(columnName));
		paramObj.put("ctfId", new JSONString(this.ctfId));
		paramObj.put("value", value);
		
		invokeQuery("UPDATE_CITYTOUR_SINGLE_ROW", paramObj, new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
			
		});

	}

	private void labelVisible(boolean visible) {
		
		titleLabel.setVisible(visible);
		summaryPanel.setVisible(visible);
		feeLabel.setVisible(visible);
		
		serviceDateLabel.setVisible(visible);
		serviceTimeLabel.setVisible(visible);
		addInfoLabel.setVisible(visible);

		compVisible(!visible);
		
	}

	private void compVisible(boolean visible) {
		
		titleInput.setVisible(visible);
		titleInput.setValue(title);
		
		summaryInput.setVisible(visible);
		summaryInput.setValue(summary);
		
		feeInput.setVisible(visible);
		feeInput.setValue(fee);
		
		serviceDateInput.setVisible(visible);
		serviceDateInput.setValue(serviceDate);
		
		serviceTimeInput.setVisible(visible);
		serviceTimeInput.setValue(serviceTime);
		
		addInfoInput.setVisible(visible);
		addInfoInput.setValue(addInfo);
		
	}

	protected void invokeQuery(String cmd, JSONObject parameterJSON, Func3<Object, String, Object> callback) {
		
		parameterJSON.put("cmd", new JSONString(cmd));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), callback);
	}

}

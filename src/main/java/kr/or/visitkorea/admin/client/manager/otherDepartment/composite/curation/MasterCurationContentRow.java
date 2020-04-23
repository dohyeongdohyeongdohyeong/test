package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.curation;

import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.RowType;
import kr.or.visitkorea.admin.client.manager.widgets.FileUploadSimplePanel;

public class MasterCurationContentRow extends MaterialPanel implements ContentRow{
 
	private String cotId;
	private String imgId;
	private MaterialTextBox t1;
	private MaterialTextBox t2;
	private MaterialTextBox t21;
	private FileUploadSimplePanel imagePanel;
	private MaterialPanel t3;
	private RowType rowType;
	private JSONObject record;
	private MaterialTextBox t11;
	private MaterialCheckBox chkbox;
	
	public MasterCurationContentRow() {
		super();
		init();
	}

	public MasterCurationContentRow(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setWidth("733px");
		this.setHeight("210px");
		
		imagePanel = new FileUploadSimplePanel(170, 40, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");
		imagePanel.setLayoutPosition(Position.ABSOLUTE);
		imagePanel.setRight(180);
		imagePanel.setTop(88);
		imagePanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			t11.setText((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);
		
		});
		this.add(imagePanel);
		
		t1 = new MaterialTextBox();
		t1.setLayoutPosition(Position.ABSOLUTE);
		t1.setLeft(20);
		t1.setRight(20); 
		t1.setTop(5);
		t1.setLabel("제목");
		this.add(t1);
		
		t11 = new MaterialTextBox();
		t11.setLayoutPosition(Position.ABSOLUTE);
		t11.setLeft(20);
		t11.setRight(200); 
		t11.setTop(68);
		t11.setLabel("링크 URL");
		this.add(t11);
		
		t2 = new MaterialTextBox();
		t2.setLayoutPosition(Position.ABSOLUTE);
		t2.setLeft(20);
		t2.setRight(400);
		t2.setTop(131);
		t2.setLabel("머릿말");
		t2.setEnabled(false);
		this.add(t2);
		
		t21 = new MaterialTextBox();
		t21.setLayoutPosition(Position.ABSOLUTE);
		t21.setRight(200);
		t21.setTop(131);
		t21.setWidth("160px");
		t21.setLabel("머릿말 색상");
		t21.setMaxLength(6);
		t21.setEnabled(false);
		this.add(t21);
		
		chkbox = new MaterialCheckBox();
		chkbox.setText("머릿말 사용여부");
		chkbox.addValueChangeHandler(event->{
			
			t2.setEnabled(chkbox.getValue());
			t21.setEnabled(chkbox.getValue());
			
		});
		t3 = new MaterialPanel();
		t3.add(chkbox);
		t3.setLineHeight(40);
		t3.setLayoutPosition(Position.ABSOLUTE);
		t3.setRight(0);
		t3.setWidth("160px");
		t3.setTop(151);
		
		this.add(t3);
		
	}

	public RowType getRowType() {
		return rowType;
	}

	public void setRowType(RowType rowType) {
		this.rowType = rowType;
	}

	public void setCotId(String cotId) {
		this.cotId=cotId;
	}

	// build component base on content
	public void buildComponent() {
		
		this.rowType = RowType.OWNER_CONTENT;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_OD_CONTENT_DATA"));
		parameterJSON.put("cotId", new JSONString(this.cotId));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyResult = resultObj.get("body").isObject().get("result").isObject();
				
					String IMG_ID = "";
					String MASTER_TAG = "";
					JSONObject COTNENT = null;

					if (bodyResult.get("IMG_ID") != null) IMG_ID = bodyResult.get("IMG_ID").isString().stringValue().trim();
					if (bodyResult.get("MASTER_TAG") != null) MASTER_TAG = bodyResult.get("MASTER_TAG").isString().stringValue().trim();
					if (bodyResult.get("CONTENT") != null) COTNENT = bodyResult.get("CONTENT").isObject();
					if (bodyResult.get("CONTENT") != null) COTNENT = bodyResult.get("CONTENT").isObject();

					record = bodyResult;

					imgId = IMG_ID;

					t1.setText(COTNENT.get("TITLE").isString().stringValue());
					t11.setText((String) Registry.get("service.server")  + "/detail/detail_view.html?cotid="+cotId);
					
				}
				
			}
		});
		
	}

	// build component base on link content
	public void buildComponent(JSONObject recordObj) {
		
		this.record = recordObj;
		String TITLE = null;
		if (recordObj.get("DISPLAY_TITLE") != null) TITLE = recordObj.get("DISPLAY_TITLE").isString().stringValue();
		String LINK_URL = recordObj.get("LINK_URL").isString().stringValue();
		double DISPLAY_HEADER_YN = 0;
		if (recordObj.get("DISPLAY_HEADER_YN") != null) DISPLAY_HEADER_YN = recordObj.get("DISPLAY_HEADER_YN").isNumber().doubleValue();
		
		t1.setText(TITLE);
		t11.setText(LINK_URL);
		if (DISPLAY_HEADER_YN == 1) {
			
			String DISPLAY_HEADER_TITLE = recordObj.get("DISPLAY_HEADER_TITLE").isString().stringValue();
			t2.setText(DISPLAY_HEADER_TITLE);
			t2.setReadOnly(false);
			
			String DISPLAY_HEADER_COLOR = recordObj.get("DISPLAY_HEADER_COLOR").isString().stringValue();
			t21.setText(DISPLAY_HEADER_COLOR);
			t21.setReadOnly(false);
			
			chkbox.setValue(true);
			
		}else {
			chkbox.setValue(false);
		}

	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public FileUploadSimplePanel getFileUploadPanel() {
		return imagePanel;
	}

	public void setUploadPanel(FileUploadSimplePanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	public String getCotId() {
		return cotId;
	}

	@Override
	public JSONObject getJSONObject() {

		JSONObject retObj = new JSONObject();
		
		if (this.record.get("MAN_ID") != null) retObj.put("MAN_ID", this.record.get("MAN_ID"));
		if (this.cotId != null) retObj.put("COT_ID", new JSONString(this.cotId));
		if (chkbox.getValue()) {
			retObj.put("DISPLAY_HEADER_YN", new JSONNumber(1));
		}else {
			retObj.put("DISPLAY_HEADER_YN", new JSONNumber(0));
		}
		retObj.put("DISPLAY_TITLE", new JSONString(this.t1.getText()));
		retObj.put("DISPLAY_HEADER_COLOR", new JSONString(this.t21.getText()));
		retObj.put("DISPLAY_HEADER_TITLE", new JSONString(this.t2.getText()));
		retObj.put("LINK_URL", new JSONString(this.t11.getText()));
		
		return retObj;
	}

	@Override
	public String getParentHeight() {
		return "230px";
	}

	@Override
	public int getParentCheckBoxLineHeight() {
		return 220;
	}

	@Override
	public void setSelected(boolean selected) {
		
	}

	@Override
	public Object getIdenifyedvalue() {
		return null;
	}

	@Override
	public Map<String, Object> getItems() {
		return null;
	}

	@Override
	public boolean isValidate() {
		return false;
	}

	@Override
	public void buildComponent(JSONArray recordObj) {
		
	}

	@Override
	public void setRowTitle(String string) {
		
	}

}

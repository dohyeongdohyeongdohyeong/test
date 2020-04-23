package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.showcase;

import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.RowType;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;

public class MasterShowcaseContentRow extends MaterialPanel implements ContentRow{
 
	private String cotId;
	private String imgId;
	private MaterialTextBox t1;
	private MaterialTextBox t2;
	private UploadPanel imagePanel;
	private MaterialInput t3;
	private MaterialInput t4;
	private RowType rowType;
//	private JSONObject record;
	private String imgExt;
	private MaterialTextBox t21;
	private MaterialTextBox t22;
	
	public MasterShowcaseContentRow() {
		super();
		init();
	}

	public MasterShowcaseContentRow(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setWidth("733px");
		this.setHeight("306px");
		
		imagePanel = new UploadPanel(347, 301, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");
		imagePanel.setButtonPostion(false);
		imagePanel.setLayoutPosition(Position.ABSOLUTE);
		imagePanel.setLeft(0);
		imagePanel.setTop(0);
		imagePanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			
			this.imgId = uploadValue.substring(0,  uploadValue.lastIndexOf("."));
			this.imgExt = uploadValue.substring(uploadValue.lastIndexOf("."));
			
			imagePanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);
		
		});
		this.add(imagePanel);
		
		t1 = new MaterialTextBox();
		t1.setLayoutPosition(Position.ABSOLUTE);
		t1.setLeft(360);
		t1.setRight(20); 
		t1.setTop(5);
		t1.setLabel("제목");
		this.add(t1);
		
		t2 = new MaterialTextBox();
		t2.setLayoutPosition(Position.ABSOLUTE);
		t2.setLeft(360);
		t2.setRight(20);
		t2.setTop(68);
		t2.setLabel("컨텐츠 설명");
		this.add(t2);
		
		t22 = new MaterialTextBox();
		t22.setLayoutPosition(Position.ABSOLUTE);
		t22.setLeft(360);
		t22.setRight(20);
		t22.setTop(131);
		t22.setLabel("코스 설명");
		this.add(t22);
		
		t21 = new MaterialTextBox();
		t21.setLayoutPosition(Position.ABSOLUTE);
		t21.setLeft(360);
		t21.setRight(20);
		t21.setTop(194);
		t21.setLabel("URL");
		this.add(t21);
		
		t3 = new MaterialInput(InputType.DATE);
		t3.setLayoutPosition(Position.ABSOLUTE);
		t3.setTop(257);
		t3.setLeft(360);
		t3.setWidth("160px");
		this.add(t3);
		
		t4 = new MaterialInput(InputType.DATE);
		t4.setLayoutPosition(Position.ABSOLUTE);
		t4.setTop(257);
		t4.setLeft(555);
		t4.setWidth("160px");
		this.add(t4);
		
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

//					record = bodyResult;

					imgId = IMG_ID;
					imgExt = null;
					
					imagePanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+IMG_ID+"&chk="+IDUtil.uuid(16));
					t1.setText(COTNENT.get("TITLE").isString().stringValue());
					t2.setText(MASTER_TAG);
					t21.setText((String) Registry.get("service.server")  + "/detail/detail_view.html?cotid=" + cotId);
				}
				
			}
		});
		
	}

	public void buildComponent(JSONObject recordObj) {
		
		this.rowType = RowType.LINK_CONTENT;
//		this.record = recordObj;
		String TITLE = "";
		String IMG_ID = recordObj.get("IMG_ID").isString().stringValue();
		String START_DATE = "";
		String END_DATE = "";
		String UPLOAD_VALUE = null;
		String LINK_URL = null;
		String COURSE_DESC = null;
		this.imgExt = null;
		this.imgId = IMG_ID;
		
		t2.setText("");
		t21.setText("");
		t22.setText("");

		if (recordObj.get("TITLE") != null ) TITLE = recordObj.get("TITLE").isString().stringValue();
		if (recordObj.get("START_DATE") != null ) START_DATE = recordObj.get("START_DATE").isString().stringValue();
		if (recordObj.get("END_DATE") != null ) END_DATE = recordObj.get("END_DATE").isString().stringValue();
		if (recordObj.get("UPLOAD_VALUE") != null) UPLOAD_VALUE = recordObj.get("UPLOAD_VALUE").isString().stringValue();
		if (recordObj.get("IMG_EXT") != null) imgExt = recordObj.get("IMG_EXT").isString().stringValue();
		if (recordObj.get("COURSE_DESCRIPTION") != null) {
			COURSE_DESC = recordObj.get("COURSE_DESCRIPTION").isString().stringValue();
			t22.setText(COURSE_DESC);
		}
		if (recordObj.get("LINK_URL") != null) {
			LINK_URL = recordObj.get("LINK_URL").isString().stringValue();
			t21.setText(LINK_URL);
		}
		if (recordObj.get("COT_ID") != null) {
			this.cotId = recordObj.get("COT_ID").isString().stringValue();
			t21.setText((String) Registry.get("service.server") + "/detail/detail_view.html?cotid="+this.cotId);
		}
		if (recordObj.get("DESCRIPTION") != null) {
			t2.setText(recordObj.get("DESCRIPTION").isString().stringValue());
		}
				
		if (imgExt != null) {
			imagePanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name="+UPLOAD_VALUE+"&chk="+IDUtil.uuid());
		}else {
			imagePanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+IMG_ID+"&chk="+IDUtil.uuid());
		}
			
		t1.setText(TITLE);
		t3.setText(START_DATE);
		t4.setText(END_DATE);

	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public UploadPanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(UploadPanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	public String getCotId() {
		return cotId;
	}

	public String getCourseDescription() {
		return this.t21.getValue();
	}
	
	@Override
	public JSONObject getJSONObject() {
		
		JSONObject retObj = new JSONObject();
		
		if (this.cotId != null) retObj.put("COT_ID", new JSONString(this.cotId));
		if (this.imgExt != null) retObj.put("IMG_EXT", new JSONString(this.imgExt));
		
		retObj.put("LINK_URL", new JSONString(t21.getValue()));
		retObj.put("CONTENT_TYPE", new JSONString(this.rowType.toString()));
		retObj.put("CONTENT_TITLE", new JSONString(this.t1.getText()));
		retObj.put("CONTENT_DESCRIPTION", new JSONString(this.t2.getText()));
		retObj.put("COURSE_DESCRIPTION", new JSONString(this.t22.getText()));
		retObj.put("IMG_ID", new JSONString(this.imgId));
		retObj.put("START_DATE", new JSONString(this.t3.getValue()));
		retObj.put("END_DATE", new JSONString(this.t4.getValue()));
		retObj.put("IMG_URL", imagePanel.getImageUrl());
		
		
		return retObj;
	}

	@Override
	public String getParentHeight() {
		return "330px";
	}

	@Override
	public int getParentCheckBoxLineHeight() {
		return 330;
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

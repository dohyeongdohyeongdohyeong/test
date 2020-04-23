package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ImageChangeDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;
	private String contentType;
	private MaterialLabel dialogTitle;
	private boolean multipleContents;
	private ContentTable targetTable;
	private String tbl;
	private String cotId;
	private Object tgrPanel;
	private String otdId;
	private UploadPanel uploadPanel;
	private MaterialTextBox imageDescription;
	protected String imageId;
	private MaterialTextBox imageLinkUrl;
	private MaterialTextBox ContentTitle;
	private double areaCode;
	private double sigugunCode;
	
	private MaterialComboBox<Object> bigArea;
	private MaterialComboBox<Object> midArea;
	private String savePath;
	private String ImageId;
	
	public ImageChangeDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 수정");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);

		MaterialButton save = new MaterialButton("저장");
		save.setFloat(Float.RIGHT);
		save.setMarginRight(20);
		this.buttonAreaPanel.add(save);
		save.addClickHandler(event->{
			Save();
			
		});
		buildUploadContent();
		
	}
	
	private void Save() {
		
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_OTHER_DEPARTMENT_COMPONENT"));
		parameterJSON.put("imgId", new JSONString(imageId));
		parameterJSON.put("imgPath", new JSONString(savePath));
		parameterJSON.put("imgDesc", new JSONString(imageDescription.getValue()));
		
		parameterJSON.put("odmId", new JSONString((String) getParameters().get("ODM_ID")));
		parameterJSON.put("LINK_URL", new JSONString(imageLinkUrl.getValue()));
		parameterJSON.put("title", new JSONString(ContentTitle.getValue()));
		parameterJSON.put("areaCode", new JSONNumber(areaCode));
		parameterJSON.put("sigugunCode", new JSONNumber(sigugunCode));
	}

	private void buildUploadContent() {

		uploadPanel = new UploadPanel(300, 250, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setLeft(30);
		uploadPanel.setTop(70);
		uploadPanel.setButtonPostion(false);
		
		uploadPanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			ImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			savePath = "";
			String[] imgMainSplitArr = ImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			
				uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+ImageId + "&chk=" + IDUtil.uuid());
			
		});
		
		this.add(uploadPanel);
		
		ContentTitle = new MaterialTextBox();
		ContentTitle.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		ContentTitle.setLayoutPosition(Position.ABSOLUTE);
		ContentTitle.setLabel("제목");
		ContentTitle.setWidth("330px");
		ContentTitle.setRight(30);
		ContentTitle.setTop(90);
		ContentTitle.setMarginTop(0);
		ContentTitle.setMarginBottom(0);
		ContentTitle.addValueChangeHandler(event->{
			
		});
		
		this.add(ContentTitle);
		
		bigArea = new MaterialComboBox<>();
		bigArea.setLayoutPosition(Position.ABSOLUTE);
		bigArea.setWidth("150px");
		bigArea.setRight(210);
		bigArea.setTop(180);
		bigArea.setLabel("지역 선택");
		bigArea.setMarginTop(0);
		bigArea.setMarginBottom(0);
		
		this.add(bigArea);
		
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		bigArea.addItem("시도 선택");
		for (String key : map.keySet()) {
			bigArea.addItem(map.get(key));
		}
		
		this.bigArea.addValueChangeHandler(event->{
			
			midArea.clear();
			midArea.addItem("구군 선택");
			String bigAreaText = bigArea.getSelectedValue().get(0).toString();
			Console.log("bigAreaText :: " + bigAreaText);
			String bigInteger = getBigKey(bigAreaText);
			if (bigInteger != null) {
				
				Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
				Map<String, String> selectMidMap = midMap.get(bigInteger);

				midArea.getElement().setPropertyString("BIG", bigInteger);
				
				for (String key : selectMidMap.keySet()) {
					String midString = selectMidMap.get(key);
					String bigString = bigArea.getSelectedValue().get(0).toString();
					if (midString == null || midString.trim().equals("") || midString.equals(bigString)) {
					}else {
						midArea.addItem(selectMidMap.get(key));
					}
				}
				
				areaCode = Double.parseDouble(bigInteger);
				
			}
			
		});

		midArea = new MaterialComboBox<>();
		midArea.setLayoutPosition(Position.ABSOLUTE);
		midArea.setWidth("150px");
		midArea.setRight(30);
		midArea.setLabel("시군구 선택");
		midArea.setTop(180);
		midArea.setMarginTop(0);
		midArea.setMarginBottom(0);
		
		this.midArea.addValueChangeHandler(event->{
			
			Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
			
			String area = midArea.getElement().getPropertyString("BIG");
			midArea.getSelectedIndex();
			
			sigugunCode = Double.parseDouble(new ArrayList<String>(midMap.get(area).keySet()).get(midArea.getSelectedIndex()));
			
		});

		this.add(midArea);
		
		imageDescription = new MaterialTextBox();
		imageDescription.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imageDescription.setLayoutPosition(Position.ABSOLUTE);
		imageDescription.setLabel("이미지 설명 (ALT)");
		imageDescription.setWidth("330px");
		imageDescription.setRight(30);
		imageDescription.setBottom(170);
		imageDescription.setMarginTop(0);
		imageDescription.setMarginBottom(0);
		imageDescription.addValueChangeHandler(event->{
			
			if (imageId != null && imageId.length() > 0) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_DESCRIPTION"));
				parameterJSON.put("imgId", new JSONString(imageId));
				parameterJSON.put("desc", new JSONString(imageDescription.getText()));
//				executeBusiness(parameterJSON);
			}
				
		});
		this.add(imageDescription);
		
		imageLinkUrl = new MaterialTextBox();
		imageLinkUrl.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imageLinkUrl.setLayoutPosition(Position.ABSOLUTE);
		imageLinkUrl.setLabel("이미지 링크 URL");
		imageLinkUrl.setLeft(30);
		imageLinkUrl.setRight(30);
		imageLinkUrl.setBottom(80);
		imageLinkUrl.setMarginTop(0);
		imageLinkUrl.setMarginBottom(0);
		imageLinkUrl.addValueChangeHandler(event->{
			
			if (imageId != null && imageId.length() > 0) {
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_PATH_WITH_ODMID"));
				parameterJSON.put("odmId", new JSONString((String) getParameters().get("ODM_ID")));
				parameterJSON.put("LINK_URL", new JSONString(imageLinkUrl.getValue()));
				
//				executeBusiness(parameterJSON);

			}
				
		});
		this.add(imageLinkUrl);
		
		
		okButton.addClickHandler(event->{
			uploadPanel.setImageUrl("");
			getMaterialExtentsWindow().closeDialog();			
		});

		
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		
		reset();

		if(parameters.get("TYPE").equals("URL 링크")){
			imageLinkUrl.setEnabled(true);
		} else {
			imageLinkUrl.setEnabled(false);
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmd", "");
		map.put("ODM_ID", parameters.get("ODM_ID"));
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_IMAGE_INFORMATION_WITH_ODMID"));
		parameterJSON.put("ODM_ID", new JSONString((String) parameters.get("ODM_ID")));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyResult = resultObj.get("body").isObject().get("result").isObject();
					
					String imgId = "";
					
					if (bodyResult.get("IMG_ID") == null) {
						uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png&chk="+IDUtil.uuid());
					}else {
						imgId = bodyResult.get("IMG_ID").isString().stringValue();
						imageId = imgId;
						uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imgId + "&chk=" + IDUtil.uuid() );
					}
					
					if (bodyResult.get("IMAGE_DESCRIPTION") != null ) {
						String imgDescription = bodyResult.get("IMAGE_DESCRIPTION").isString().stringValue();
						imageDescription.setText(imgDescription);
					}
					
					if (bodyResult.get("LINK_URL") != null ) {
						String imglinkurl = bodyResult.get("LINK_URL").isString().stringValue();
						imageLinkUrl.setText(imglinkurl);
					}
					
					
				}
			}
		});
				
				
	}

	private void reset() {
		uploadPanel.setImageUrl("");
		imageDescription.setText("");
		imageLinkUrl.setText("");
	}

	@Override
	public int getHeight() {
		return 480;
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}
	
	protected String getBigKey(String bigAreaText) {
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		for (String key : map.keySet()) {
			if (bigAreaText.equals(map.get(key))) {
				return key;
			}
		}
		return null;
	}
	
}

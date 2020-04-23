package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Date;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetailForShowcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.FileUploadPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class UpdateContentsDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialTextBox titleInputBox;
	private UploadPanel imageUploadPanel;
	private MaterialTextBox imgDescInputBox;
	private String imageUUID;
	private String savePath;
	private String ODM_ID;
	private double COT_ORDER = -1;
	private String COT_ID;
	
	public UpdateContentsDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
			JSONObject obj = (JSONObject) getParameters().get("info");
			if(obj.containsKey("IMG_ID")) {
				imageUUID = obj.get("IMG_ID").isString().stringValue();
				imageUploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imageUUID + "&chk=" + IDUtil.uuid() );
			}
			if(obj.containsKey("CONTENT_TITLE"))
				titleInputBox.setText(obj.get("CONTENT_TITLE").isString().stringValue());
			if(obj.containsKey("IMAGE_DESCRIPTION"))
				imgDescInputBox.setText(obj.get("IMAGE_DESCRIPTION").isString().stringValue());
			if(obj.containsKey("ODM_ID"))
				ODM_ID = obj.get("ODM_ID").isString().stringValue();
			if(obj.containsKey("COT_ORDER"))
				COT_ORDER = obj.get("COT_ORDER").isNumber().doubleValue();
			if(obj.containsKey("COT_ID"))
				COT_ID = obj.get("COT_ID").isString().stringValue();
			
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 수정 - 등록된 컨텐츠 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		buildUploadContent();
		
	}
	
	private void buildUploadContent() {

		imageUploadPanel = new UploadPanel(500, 300, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
		imageUploadPanel.setLayoutPosition(Position.ABSOLUTE);
		imageUploadPanel.setLeft(30);
		imageUploadPanel.setTop(70);
		imageUploadPanel.setButtonPostion(false);
		imageUploadPanel.getUploader().setAcceptedFiles("image/*"); 
		imageUploadPanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			savePath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			
			imageUUID = IDUtil.uuid();
			
			imageUploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);

		});
		this.add(imageUploadPanel);
		
		titleInputBox = new MaterialTextBox();
		titleInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleInputBox.setLayoutPosition(Position.ABSOLUTE);
		titleInputBox.setLabel("제목");
		titleInputBox.setLeft(30);
		titleInputBox.setRight(30);
		titleInputBox.setTop(395);
		titleInputBox.setMarginTop(0);
		titleInputBox.setMarginBottom(0);
		this.add(titleInputBox);
		
		imgDescInputBox = new MaterialTextBox();
		imgDescInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imgDescInputBox.setLayoutPosition(Position.ABSOLUTE);
		imgDescInputBox.setLabel("이미지 설명 (ALT)");
		imgDescInputBox.setLeft(30);
		imgDescInputBox.setRight(30);
		imgDescInputBox.setTop(470);
		imgDescInputBox.setMarginTop(0);
		imgDescInputBox.setMarginBottom(0);
		this.add(imgDescInputBox);

		
		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{

			if(SaveCheck() == false)
				return;
			
			// insert image
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_IMAGE_NOT_COTID"));
			parameterJSON.put("imgId", new JSONString(imageUUID));
			if(savePath != null)			
			parameterJSON.put("imgPath", new JSONString(savePath));
			parameterJSON.put("imgDesc", new JSONString(imgDescInputBox.getValue()));
			executeBusiness(parameterJSON);

			// insert area content
			AreaComponent  areaComponent = (AreaComponent) this.getParameters().get("AREA_COMPONENT");
			String COMP_ID = areaComponent.getInfo().get(0).get("COMP_ID").isString().stringValue();
			double COMP_ORDER = areaComponent.getInfo().get(0).get("COMP_ORDER").isNumber().doubleValue();
			String TEMPLATE_ID = areaComponent.getInfo().get(0).get("TEMPLATE_ID").isString().stringValue();
			String MAIN_AREA = areaComponent.getInfo().get(0).get("MAIN_AREA").isString().stringValue();
			String OTD_ID = areaComponent.getInfo().get(0).get("OTD_ID").isString().stringValue();
			if(ODM_ID == null)
				ODM_ID = IDUtil.uuid();
			if(COT_ORDER == -1)
				COT_ORDER = areaComponent.getInfo().size();

			// insert dept area row
			JSONObject pJSON = new JSONObject();
			pJSON.put("cmd", new JSONString("INSERT_DEPT_AREA_ROW"));

			pJSON.put("ODM_ID", new JSONString(ODM_ID));
			pJSON.put("IMG_ID", new JSONString(imageUUID));
			pJSON.put("COMP_ORDER", new JSONNumber(COMP_ORDER));
			pJSON.put("VIEW_TITLE", new JSONNumber(1));
			pJSON.put("TEMPLATE_ID", new JSONString(TEMPLATE_ID));
			pJSON.put("MAIN_AREA", new JSONString(MAIN_AREA));
			pJSON.put("COMP_ID", new JSONString(COMP_ID));
			pJSON.put("OTD_ID", new JSONString(OTD_ID));
			pJSON.put("COT_ID", new JSONString(COT_ID));
			pJSON.put("COT_ORDER", new JSONNumber(COT_ORDER));
			pJSON.put("TYPE", new JSONString("UPDATE"));
			pJSON.put("TITLE", new JSONString(titleInputBox.getValue()));
			
			VisitKoreaBusiness.post("call", pJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {
						
						AreaComponent aac = (AreaComponent) getParameters().get("AREA_COMPONENT");
						
						JSONObject newRecJSON = new JSONObject();
						
						newRecJSON.put("COMP_ORDER", pJSON.get("COMP_ORDER"));
						newRecJSON.put("ODM_ID", new JSONString(ODM_ID));
						newRecJSON.put("VIEW_TITLE", new JSONNumber(1));
						newRecJSON.put("TEMPLATE_ID", new JSONString(TEMPLATE_ID));
						newRecJSON.put("MAIN_AREA", new JSONString(MAIN_AREA));
						newRecJSON.put("COMP_ID",new JSONString(COMP_ID));
						newRecJSON.put("OTD_ID", new JSONString(OTD_ID));
						newRecJSON.put("COT_ID", new JSONString(COT_ID));
						newRecJSON.put("CONTENT_TITLE", new JSONString(titleInputBox.getValue()));
						newRecJSON.put("IMG_ID", new JSONString(imageUUID));
							
						newRecJSON.put("FILE_DESCRIPTION", new JSONString(imgDescInputBox.getValue()));
						newRecJSON.put("COT_ORDER", new JSONNumber(COT_ORDER));
						DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd kk:mm:ss");
						String createDateString = dtf.format(new Date());
						newRecJSON.put("CREATE_DATE", new JSONString(createDateString));
						
						if(getParameters().get("TYPE").equals("UPDATE"))
							aac.getInfo().set((int)COT_ORDER, newRecJSON);
						else
							aac.getInfo().add(newRecJSON);
						
						if (getParameters().get("DETAIL") != null && getParameters().get("DETAIL") instanceof OtherDepartmentMainContentDetail) {
							OtherDepartmentMainContentDetail detail = (OtherDepartmentMainContentDetail)getParameters().get("DETAIL");
							detail.setAreaComponent(aac);
						}else if (getParameters().get("DETAIL") != null && getParameters().get("DETAIL") instanceof OtherDepartmentMainContentDetailForShowcase) {
							OtherDepartmentMainContentDetailForShowcase detail = (OtherDepartmentMainContentDetailForShowcase)getParameters().get("DETAIL");
							detail.setAreaComponent(aac);
						}
						
						getMaterialExtentsWindow().closeDialog();
					}
				}
				
			});
			
		});
		this.addButton(selectButton);
	
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		initClear();
	}

	@Override
	public int getHeight() {
		return 610;
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

	
	private boolean SaveCheck() {
		
		if(imageUUID == null) {
			MaterialToast.fireToast("이미지를 등록해주세요");
			return false;
		}
		
		if(titleInputBox.getText().replaceAll(" ", "").length() == 0) {
			MaterialToast.fireToast("제목을 입력해주세요");
			return false;
		}
		
		return true;
		
	}
	
	private void initClear() {
		titleInputBox.setText("");
		imageUploadPanel.setImageUrl(GWT.getHostPageBaseURL() + "images/default-image.png");
		imgDescInputBox.setText("");
		imgDescInputBox.setText("");
		imageUUID = null;
		ODM_ID = null;
		COT_ORDER = -1;
	}
	
	
}

package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
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
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetailForShowcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ExternalLinkDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private UploadPanel imageUploadPanel;
	private MaterialTextBox titleInputBox;
	private MaterialTextBox imgDescInputBox;
	private MaterialTextBox urlInputBox;
	private MaterialLabel guideLabel;
	private String savePath;
	private String imageUUID;
	private MaterialComboBox<Object> bigArea,midArea;
	private double areaCode;
	private double sigunguCode;
	private double COT_ORDER;
	private String ODM_ID;
	private String AREA_NAME;
	private String SIGUNGU_NAME;

	public ExternalLinkDialog(MaterialExtentsWindow window) {
		super(window);
		buildContent();
	}

	@Override
	public void init() {}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (this.getParameters().get("CDTypeImgGuideFlag") != null)
			guideLabel.setVisible((boolean) this.getParameters().get("CDTypeImgGuideFlag"));
		if (getParameters().get("TYPE").equals("UPDATE")) {
			dialogTitle.setText("컨텐츠 수정 - 링크 컨텐츠");
			JSONObject obj = (JSONObject) getParameters().get("info");
			if(obj.containsKey("IMG_ID")) {
				imageUUID = obj.get("IMG_ID").isString().stringValue();
				imageUploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imageUUID + "&chk=" + IDUtil.uuid() );
			}
			if(obj.containsKey("CONTENT_TITLE"))
				titleInputBox.setText(obj.get("CONTENT_TITLE").isString().stringValue());
			if(obj.containsKey("LINK_URL"))
				urlInputBox.setText(obj.get("LINK_URL").isString().stringValue());
			if(obj.containsKey("AREA_CODE")) {
				areaCode = obj.get("AREA_CODE").isNumber().doubleValue();
			}
			if(obj.containsKey("SIGUNGU_CODE"))
				sigunguCode = obj.get("SIGUNGU_CODE").isNumber().doubleValue();
			if(obj.containsKey("IMAGE_DESCRIPTION"))
				imgDescInputBox.setText(obj.get("IMAGE_DESCRIPTION").isString().stringValue());
			if(obj.containsKey("ODM_ID"))
				ODM_ID = obj.get("ODM_ID").isString().stringValue();
			if(obj.containsKey("COT_ORDER"))
				COT_ORDER = obj.get("COT_ORDER").isNumber().doubleValue();
			if(obj.containsKey("AREA_NAME")) {
				AREA_NAME = obj.get("AREA_NAME").isString().stringValue();
				for (int i = 0; i < this.bigArea.getValues().size(); i++) {
					if(this.bigArea.getValues().get(i) == AREA_NAME) {
						bigArea.setSelectedIndex(i);
						ValueChangeEvent.fire(bigArea, bigArea.getSelectedValue());
					}
				}
			}
			if(obj.containsKey("SIGUGUN_NAME")) {
				SIGUNGU_NAME = obj.get("SIGUGUN_NAME").isString().stringValue();
				for (int i = 0; i < this.midArea.getValues().size(); i++) {
					if(this.midArea.getValues().get(i) == SIGUNGU_NAME) {
						midArea.setSelectedIndex(i);
					}
				}
			}
		} else {
			dialogTitle.setText("컨텐츠 생성 - 링크 컨텐츠");
		}
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 생성 - 링크 컨텐츠");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);

		imageUploadPanel = new UploadPanel(300, 250, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
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
			
			imageUUID = tempImageId;
			
			imageUploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);

		});
		this.add(imageUploadPanel);
		
		titleInputBox = new MaterialTextBox();
		titleInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleInputBox.setLayoutPosition(Position.ABSOLUTE);
		titleInputBox.setLabel("제목");
		titleInputBox.setWidth("330px");
		titleInputBox.setRight(30);
		titleInputBox.setTop(90);
		titleInputBox.setMarginTop(0);
		titleInputBox.setMarginBottom(0);
		this.add(titleInputBox);
		
		imgDescInputBox = new MaterialTextBox();
		imgDescInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imgDescInputBox.setLayoutPosition(Position.ABSOLUTE);
		imgDescInputBox.setLabel("이미지 설명");
		imgDescInputBox.setWidth("330px");
		imgDescInputBox.setRight(30);
		imgDescInputBox.setBottom(170);
		imgDescInputBox.setMarginTop(0);
		imgDescInputBox.setMarginBottom(0);
		this.add(imgDescInputBox);
		
		bigArea= new MaterialComboBox<>();
		bigArea.setLayoutPosition(Position.ABSOLUTE);
		bigArea.setWidth("150px");
		bigArea.setRight(210);
		bigArea.setTop(180);
		bigArea.setLabel("지역 선택");
		bigArea.setMarginTop(0);
		bigArea.setMarginBottom(0);
		
		this.add(bigArea);
		
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		bigArea.addItem("선택 없음",-1);
		for (String key : map.keySet()) {
			bigArea.addItem(map.get(key));
		}
		
		
		this.bigArea.addValueChangeHandler(event->{
			
			midArea.clear();
			midArea.addItem("선택 없음",-1);
			String bigAreaText = bigArea.getSelectedValue().get(0).toString();
			String bigInteger = getBigKey(bigAreaText);
			AREA_NAME = bigAreaText;
			if(bigArea.getSelectedIndex() == 0) 
				areaCode = -1;
			else {
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
		midArea.addItem("선택 없음",-1);
		this.midArea.addValueChangeHandler(event->{
			
			Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
			SIGUNGU_NAME = midArea.getSelectedValue().get(0).toString();;
			String area = midArea.getElement().getPropertyString("BIG");
			midArea.getSelectedIndex();
			
			sigunguCode = Double.parseDouble(new ArrayList<String>(midMap.get(area).keySet()).get(midArea.getSelectedIndex()));
			
		});

		this.add(midArea);
		
		guideLabel = new MaterialLabel();
		guideLabel.setFontSize("0.7em");
		guideLabel.setFontWeight(FontWeight.BOLD);
		guideLabel.setTextColor(Color.BLUE);
		guideLabel.setMarginTop(230);
		guideLabel.setPaddingLeft(30);
		guideLabel.setPaddingBottom(10);
		guideLabel.setVisible(false);
		guideLabel.setText("*일반형 940 X 700 , 와이드형 1900 X 700");
		
		this.add(guideLabel);
		
		urlInputBox = new MaterialTextBox();
		urlInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		urlInputBox.setLayoutPosition(Position.ABSOLUTE);
		urlInputBox.setLabel("Link URL");
		urlInputBox.setLeft(30);
		urlInputBox.setRight(30);
		urlInputBox.setBottom(80);
		urlInputBox.setMarginTop(0);
		urlInputBox.setMarginBottom(0);
		this.add(urlInputBox);
		
		
		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			if(SaveCheck() == false)
				return;

			if(bigArea.getSelectedIndex() == 0) {
				areaCode = -1;
				sigunguCode = -1;
			}
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
			pJSON.put("TITLE", new JSONString(titleInputBox.getValue()));
			pJSON.put("LINK_URL", new JSONString(urlInputBox.getValue()));
			pJSON.put("COT_ORDER", new JSONNumber(COT_ORDER));
			pJSON.put("AREA_CODE", new JSONNumber(areaCode));
			pJSON.put("SIGUNGU_CODE", new JSONNumber(sigunguCode));
			
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
						
						//2e15a221-7a28-4bbf-b2b2-6de2e4678310
						newRecJSON.put("COMP_ORDER", pJSON.get("COMP_ORDER"));
						newRecJSON.put("ODM_ID", new JSONString(ODM_ID));
						newRecJSON.put("VIEW_TITLE", new JSONNumber(1));
						newRecJSON.put("TEMPLATE_ID", new JSONString(TEMPLATE_ID));
						newRecJSON.put("MAIN_AREA", new JSONString(MAIN_AREA));
						newRecJSON.put("COMP_ID",new JSONString(COMP_ID));
						newRecJSON.put("OTD_ID", new JSONString(OTD_ID));
						newRecJSON.put("CONTENT_TITLE", new JSONString(titleInputBox.getValue()));
						newRecJSON.put("IMG_ID", new JSONString(imageUUID));
						newRecJSON.put("AREA_CODE", new JSONNumber(areaCode));
						newRecJSON.put("AREA_NAME", new JSONString(AREA_NAME));
						newRecJSON.put("SIGUNGU_CODE", new JSONNumber(sigunguCode));
						newRecJSON.put("SIGUGUN_NAME", new JSONString(SIGUNGU_NAME));
						newRecJSON.put("LINK_URL", new JSONString(urlInputBox.getValue()));
						newRecJSON.put("IMAGE_DESCRIPTION", new JSONString(imgDescInputBox.getValue()));
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

	private boolean SaveCheck() {
		
		if(imageUUID == null) {
			MaterialToast.fireToast("이미지를 등록해주세요");
			return false;
		}
		
//		if(titleInputBox.getText().replaceAll(" ", "").length() == 0) {
//			MaterialToast.fireToast("제목을 입력해주세요");
//			return false;
//		}
		
		return true;
	}


	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		initClear();
	}

	private void initClear() {
		titleInputBox.setText("");
		imageUploadPanel.setImageUrl(GWT.getHostPageBaseURL() + "images/default-image.png");
		imgDescInputBox.setText("");
		urlInputBox.setText("");
		bigArea.setSelectedIndex(0);
		midArea.setSelectedIndex(0);
		areaCode = -1;
		sigunguCode = -1;
		imageUUID = null;
		ODM_ID = null;
		COT_ORDER = -1;
		AREA_NAME = "선택 없음";
		SIGUNGU_NAME = "선택 없음";
	}


	@Override
	public int getHeight() {
		return 480;
	}
	
	public void setImageGuideLabel(boolean visibleFlag) {
		this.guideLabel.setVisible(visibleFlag);
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

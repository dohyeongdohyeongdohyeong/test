package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.banner;

import java.util.Date;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.amcore.client.formatter.DateFormatter;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainImageBanner extends AbtractOtherDepartmentMainContents implements ContentDetail{

	private static String otdId;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private MaterialPanel panel;
	private UploadPanel uploadPanel;
	private MaterialTextBox ImagedescLabel;
	private MaterialTextBox ImageTitleLabel;
	private MaterialTextBox ImageLinkLabel;
	private String savePath;
	private String imageUUID;
	private String NewimageUUID;
	private String manId;
	
	public MainImageBanner(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor, String manId) {
		super(materialExtentsWindow);
		this.manId = manId;
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("메인 이미지 배너 상세");
	}
	
	private MainImageBanner getPanel() {
		return this;
	}


	private void buildContent() {

		
		MaterialLink headLink = this.addLink(new MaterialLink());
		headLink.setLayoutPosition(Position.ABSOLUTE);
		headLink.setTooltip("서버 반영");
		headLink.setTop(4);
		headLink.setRight(0);
		headLink.setIconType(IconType.SAVE);
		headLink.setIconColor(Color.WHITE);
		headLink.addClickHandler(event->{
			
			String msg = isValidate();
			if (msg != null) {
				MaterialToast.fireToast(msg);
				return;
			}
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_SM_IMAGE_BANNER"));
			if(imageUUID != null)
				parameterJSON.put("imgId", new JSONString(imageUUID));
			if(NewimageUUID != null)
				parameterJSON.put("NewimgId", new JSONString(NewimageUUID));
			if(savePath != null)
				parameterJSON.put("imgPath", new JSONString(savePath));
			parameterJSON.put("img_desc", new JSONString(ImagedescLabel.getValue()));
			parameterJSON.put("img_link", new JSONString(ImageLinkLabel.getValue()));
			parameterJSON.put("img_title", new JSONString(ImageTitleLabel.getValue()));
			if(manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2"))
				parameterJSON.put("order", new JSONNumber(1));
			else
				parameterJSON.put("order", new JSONNumber(0));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {
					MaterialToast.fireToast("저장되었습니다.");
				}
			});
			
			
		});
		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		
		uploadPanel = new UploadPanel(517, 300, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setButtonPostion(false);
		uploadPanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			savePath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			
			NewimageUUID = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("uuid").isString().stringValue();
			
			uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);

			
		});
		
		uploadPanel.getElement().getStyle().setProperty("margin", "auto");
		MaterialRow row1 = addRow(panel);
		row1.add(uploadPanel);
		
		MaterialRow row2 = addRow(panel);
		addLabel(row2, "이미지 배너 명", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		ImageTitleLabel = addInputText(row2, "", "s9");
		
		MaterialRow row3 = addRow(panel);
		addLabel(row3, "이미지 배너 링크", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		ImageLinkLabel = addInputText(row3, "", "s9");
		
		MaterialRow row4 = addRow(panel);
		addLabel(row4, "이미지 배너 설명", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		ImagedescLabel = addInputText(row4, "", "s9");
		
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}
	
	public void setOtdId(String OTDID) {
		otdId = OTDID;
	}

	public void load(String mainId) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_MAIN_IMAGE_BANNER"));
		if(manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2"))
			parameterJSON.put("order", new JSONNumber(1));
		else
			parameterJSON.put("order", new JSONNumber(0));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");

				if (processResult.equals("success")) {
					JSONObject resultObject = resultObj.get("body").isObject().get("result").isObject();
				
					if(resultObject.containsKey("imgId")) {
						imageUUID = resultObject.get("imgId").isString().stringValue();
						uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+imageUUID);
					}
					if(resultObject.containsKey("img_desc")) 
						ImagedescLabel.setValue(resultObject.get("img_desc").isString().stringValue());
					if(resultObject.containsKey("img_link")) 
						ImageLinkLabel.setValue(resultObject.get("img_link").isString().stringValue());
					if(resultObject.containsKey("img_title")) 
						ImageTitleLabel.setValue(resultObject.get("img_title").isString().stringValue());
				}
			}
		});
		
	}

	@Override
	public void setRecIndex(int i) {
		
	}
	
	public void setManId(String manId) {
		if (manId != null) {
			this.manId = manId;
			buildContent();
			load(manId);
		}
	}
	
	@Override
	public void loadData() {
		
	}

	@Override
	public ContentRow getCustomRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRow(ContentRow masterContentRow) {
		// TODO Auto-generated method stub
		
	}

	private String isValidate() {
		if(imageUUID == null) {
			return "이미지를 등록해주세요";
		} else if(ImageTitleLabel.getText().equals("")) {
			return "이미지배너의  제목을 입력해주세요.";
		} else if(ImageLinkLabel.getText().equals("")) {
			return "이미지를 클릭했을때 이동될 링크값을 입력해주세요.";
		} else if(ImagedescLabel.getText().equals("")) {
			return "이미지에 대한  설명을 입력해주세요.";
		} 
		return null;
	}

}
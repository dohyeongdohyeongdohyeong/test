package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MasterExternalLinkDialog extends DialogContent {

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
	private UploadPanel imageUploadPanel;
	private MaterialTextBox titleInputBox;
	private MaterialTextBox imgDescInputBox;
	private MaterialTextBox urlInputBox;
	private String savePath;
	private String imageUUID;
	private String uploadValue;
	private String extString;
	private MaterialButton selectButton;
	private String manId;

	public MasterExternalLinkDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		manId = (String) getParameters().get("MAN_ID");
		
		selectButton.setEnabled(true);
		okButton.setEnabled(true);
//		imageUploadPanel.getElement().getFirstChildElement().getFirstChildElement().getElementsByTagName("button").getItem(1).addClassName("disabled");
		
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			
			selectButton.setEnabled(Registry.getPermission("de6c0944-dac3-4b54-a3ec-d55d745e6d85"));
			okButton.setEnabled(Registry.getPermission("8b7f6c89-fadd-449e-a689-3d9207b05003"));
			if (Registry.getPermission("e293df00-d252-45ca-a051-92648104a350"))
				imageUploadPanel.getElement().getFirstChildElement().getFirstChildElement().getElementsByTagName("button").getItem(1).removeClassName("disabled");
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			
			selectButton.setEnabled(Registry.getPermission("353a2357-db5e-42b9-af0c-03e43008cf02"));
			okButton.setEnabled(Registry.getPermission("cf50a278-8f6b-4181-ad29-d61f965b3b60"));
			if (Registry.getPermission("c89e66b9-6295-4462-be10-e4d14aa4b6c6"))
				imageUploadPanel.getElement().getFirstChildElement().getFirstChildElement().getElementsByTagName("button").getItem(1).removeClassName("disabled");
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

		imageUploadPanel = new UploadPanel(200, 200, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
		imageUploadPanel.setLayoutPosition(Position.ABSOLUTE);
		imageUploadPanel.setLeft(30);
		imageUploadPanel.setTop(70);
		imageUploadPanel.setButtonPostion(false);
		imageUploadPanel.getUploader().setAcceptedFiles("image/*"); 
		imageUploadPanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			imageUUID = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			extString = uploadValue.substring(uploadValue.lastIndexOf("."));
			savePath = "";
			String[] imgMainSplitArr = imageUUID.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			
			imageUploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);


		});
		this.add(imageUploadPanel);
		
		titleInputBox = new MaterialTextBox();
		titleInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleInputBox.setLayoutPosition(Position.ABSOLUTE);
		titleInputBox.setLabel("제목");
		titleInputBox.setLeft(250);
		titleInputBox.setRight(30);
		titleInputBox.setTop(90);
		titleInputBox.setMarginTop(0);
		titleInputBox.setMarginBottom(0);
		this.add(titleInputBox);
		
		imgDescInputBox = new MaterialTextBox();
		imgDescInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imgDescInputBox.setLayoutPosition(Position.ABSOLUTE);
		imgDescInputBox.setLabel("이미지 설명");
		imgDescInputBox.setLeft(250);
		imgDescInputBox.setRight(30);
		imgDescInputBox.setTop(200);
		imgDescInputBox.setMarginTop(0);
		imgDescInputBox.setMarginBottom(0);
		this.add(imgDescInputBox);
		
		urlInputBox = new MaterialTextBox();
		urlInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		urlInputBox.setLayoutPosition(Position.ABSOLUTE);
		urlInputBox.setLabel("Link URL");
		urlInputBox.setLeft(30);
		urlInputBox.setRight(30);
		urlInputBox.setTop(300);
		urlInputBox.setMarginTop(0);
		urlInputBox.setMarginBottom(0);
		this.add(urlInputBox);
		
		
		selectButton = new MaterialButton("컨텐츠 생성");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			ContentDetail scd = (ContentDetail)getParameters().get("PARENT");
			
			JSONObject recordObj = new JSONObject();
			recordObj.put("TITLE", new JSONString(this.titleInputBox.getValue()));
			recordObj.put("IMG_ID", new JSONString(this.imageUUID));
			recordObj.put("IMG_EXT", new JSONString(this.extString));
			recordObj.put("UPLOAD_VALUE", new JSONString(uploadValue));
			recordObj.put("LINK_URL", new JSONString(urlInputBox.getValue().trim()));
			recordObj.put("MAIN_TAG", new JSONString(""));
			recordObj.put("START_DATE", new JSONString(""));
			recordObj.put("END_DATE", new JSONString(""));
			
			ContentRow masterContentRow = scd.getCustomRow();
			masterContentRow.buildComponent(recordObj);
			
			scd.addRow(masterContentRow);
			
			getMaterialExtentsWindow().closeDialog();
			
		});
		
		this.addButton(selectButton);

	}

	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		titleInputBox.setText("");
		imgDescInputBox.setText("");
		imageUploadPanel.setImageUrl("");
		urlInputBox.setText("");
	}

	private String getContentType(String object) {
		return this.contentType;
	}

	@Override
	public int getHeight() {
		return 450;
	}

	private void executeBusiness(JSONObject jObj) {
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

}

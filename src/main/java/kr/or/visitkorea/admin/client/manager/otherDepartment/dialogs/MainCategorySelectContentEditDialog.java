package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.CategoryMainContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainCategorySelectContentEditDialog extends DialogContent {

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
	private MaterialTextBox titleBox;
	private MaterialTextBox titleInputBox;
	private MaterialTextBox urlInputBox;
	private String savePath;
	private String imageUUID;
	private String uploadValue;
	private String extString;
	private MaterialTextBox masterTagBox;
	private MaterialCheckBox chkbox;

	public MainCategorySelectContentEditDialog(MaterialExtentsWindow window) {
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

		imageUploadPanel = new UploadPanel(250, 250, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
		imageUploadPanel.setLayoutPosition(Position.ABSOLUTE);
		imageUploadPanel.setLeft(30);
		imageUploadPanel.setTop(70);
		imageUploadPanel.setButtonPostion(false);
		imageUploadPanel.getUploader().setAcceptedFiles("image/*"); 
		imageUploadPanel.updateImageInformation(true);
		this.add(imageUploadPanel);
		
		titleBox = new MaterialTextBox();
		titleBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleBox.setLayoutPosition(Position.ABSOLUTE);
		titleBox.setLabel("지역");
		titleBox.setLeft(300);
		titleBox.setRight(250);
		titleBox.setTop(90);
		titleBox.setMarginTop(0);
		titleBox.setMarginBottom(0);
		titleBox.setFontSize("1.5em");
		titleBox.setFontWeight(FontWeight.BOLD);
		this.add(titleBox);
		
		masterTagBox = new MaterialTextBox();
		masterTagBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		masterTagBox.setLayoutPosition(Position.ABSOLUTE);
		masterTagBox.setLabel("대표태그");
		masterTagBox.setMaxLength(6);
		masterTagBox.setRight(30);
		masterTagBox.setWidth("200px");
		masterTagBox.setTop(90);
		masterTagBox.setMarginTop(0);
		masterTagBox.setMarginBottom(0);
		this.add(masterTagBox);
		
		titleInputBox = new MaterialTextBox();
		titleInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleInputBox.setLayoutPosition(Position.ABSOLUTE);
		titleInputBox.setLabel("제목");
		titleInputBox.setLeft(300);
		titleInputBox.setRight(30);
		titleInputBox.setTop(170);
		titleInputBox.setMarginTop(0);
		titleInputBox.setMarginBottom(0);
		this.add(titleInputBox);
		
		urlInputBox = new MaterialTextBox();
		urlInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		urlInputBox.setLayoutPosition(Position.ABSOLUTE);
		urlInputBox.setLabel("Link URL");
		urlInputBox.setLeft(300);
		urlInputBox.setRight(30);
		urlInputBox.setTop(250);
		urlInputBox.setMarginTop(0);
		urlInputBox.setMarginBottom(0);
		this.add(urlInputBox);
		
		MaterialButton selectButton = new MaterialButton("컨텐츠 수정");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			CategoryMainContentPanel tgrPanel = (CategoryMainContentPanel) getParameters().get("TARGET");
			
			JSONObject paramJSON = (JSONObject)getParameters().get("SECTION_OBJECT");
			
			JSONObject recordObj = new JSONObject();
			recordObj.put("header", new JSONString(titleBox.getValue()));
			recordObj.put("headerColor", new JSONString("#"+masterTagBox.getValue()));
			recordObj.put("title", new JSONString(titleInputBox.getValue()));
			recordObj.put("url", new JSONString(urlInputBox.getValue()));
			if (imageUploadPanel.getImageId() != null) recordObj.put("image", new JSONString(imageUploadPanel.getImageId()));
			if (extString != null) recordObj.put("ext", new JSONString(extString));
			if (paramJSON != null && paramJSON.get("COT_ID") != null) recordObj.put("COT_ID", new JSONString(paramJSON.get("COT_ID").isString().stringValue()));
			
			tgrPanel.setupContent(recordObj);
			tgrPanel.registContent();
			
			titleBox.setText("");
			masterTagBox.setText("");
			titleInputBox.setText("");
			imageUploadPanel.setImageUrl("");
			urlInputBox.setText("");

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
		
		titleBox.setText("");
		masterTagBox.setText("");
		titleInputBox.setText("");
		imageUploadPanel.setImageUrl("");
		urlInputBox.setText("");
		
		if (getParameters().get("LINK_OBJECT") != null) {
			
			JSONObject paramJSON = (JSONObject)getParameters().get("LINK_OBJECT");
			
			if (paramJSON.get("header") != null) titleBox.setText(paramJSON.get("header").isString().stringValue());
			if (paramJSON.get("headerColor") != null) masterTagBox.setText(paramJSON.get("headerColor").isString().stringValue().substring(1));
			if (paramJSON.get("title") != null) titleInputBox.setText(paramJSON.get("title").isString().stringValue());
			if (paramJSON.get("url") != null) urlInputBox.setText(paramJSON.get("url").isString().stringValue());
			if (paramJSON.get("image") != null) {
				String uuid = paramJSON.get("image").isString().stringValue();
				String ext = paramJSON.get("ext").isString().stringValue();
				imageUploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + (uuid + ext).replaceAll("\"", ""));
			}
			
		}else if (getParameters().get("SECTION_OBJECT") != null) {
				
				JSONObject paramJSON = (JSONObject)getParameters().get("SECTION_OBJECT");
				
				if (paramJSON.get("header") != null) titleBox.setText(paramJSON.get("header").isString().stringValue());
				if (paramJSON.get("headerColor") != null) masterTagBox.setText(paramJSON.get("headerColor").isString().stringValue().substring(1));
				if (paramJSON.get("TITLE") != null) titleInputBox.setText(paramJSON.get("TITLE").isString().stringValue());
				if (paramJSON.get("IMG_ID") != null) {
					String uuid = paramJSON.get("IMG_ID").isString().stringValue();
					imageUUID = uuid;
					imageUploadPanel.setImageId(imageUUID);
				}
				if (paramJSON.get("LINK_URL") != null) {
					urlInputBox.setValue(paramJSON.get("LINK_URL").isString().stringValue());
				}				
		}
	}

	private String getContentType(String object) {
		return this.contentType;
	}

	@Override
	public int getHeight() {
		return 430;
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

}

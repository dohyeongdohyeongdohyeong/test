package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.CheckBoxType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.localGov.MetroUIContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LocalGovMetroUiEditDialog extends DialogContent {

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
	private MaterialTextBox headerInputBox;
	private MaterialTextBox titleInputBox;
	private MaterialTextBox urlInputBox;
	private MaterialTextBox headerColorInputBox;
	private MaterialCheckBox chkbox;

	public LocalGovMetroUiEditDialog(MaterialExtentsWindow window) {
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
		imageUploadPanel.updateImageInformation(true);
		imageUploadPanel.setButtonPostion(false);
		imageUploadPanel.getUploader().setAcceptedFiles("image/*"); 
		this.add(imageUploadPanel);
		
		headerInputBox = new MaterialTextBox();
		headerInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		headerInputBox.setLayoutPosition(Position.ABSOLUTE);
		headerInputBox.setLabel("헤더");
		headerInputBox.setLeft(300);
		headerInputBox.setRight(250);
		headerInputBox.setTop(90);
		headerInputBox.setMarginTop(0);
		headerInputBox.setMarginBottom(0);
		headerInputBox.setFontSize("1.5em");
		headerInputBox.setFontWeight(FontWeight.BOLD);
		this.add(headerInputBox);
		
		headerColorInputBox = new MaterialTextBox();
		headerColorInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		headerColorInputBox.setLayoutPosition(Position.ABSOLUTE);
		headerColorInputBox.setLabel("헤더 컬러");
		headerColorInputBox.setMaxLength(6);
		headerColorInputBox.setRight(30);
		headerColorInputBox.setWidth("200px");
		headerColorInputBox.setTop(90);
		headerColorInputBox.setMarginTop(0);
		headerColorInputBox.setMarginBottom(0);
		headerColorInputBox.addValueChangeHandler(event->{
			if (event.getValue().length() == 6) {
				headerInputBox.getElement().getStyle().setProperty("color", "#"+event.getValue());
			}
		});
		this.add(headerColorInputBox);
		
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

		MaterialPanel chkboxPanel = new MaterialPanel();
		chkboxPanel.setLayoutPosition(Position.ABSOLUTE);
		chkbox = new MaterialCheckBox();
		chkbox.setType(CheckBoxType.FILLED);
		chkbox.setText("텍스트 컨텐츠로 표시");
		chkboxPanel.add(chkbox);
		chkboxPanel.setLeft(40);
		chkboxPanel.setBottom(50);
		
		this.add(chkboxPanel);
		
		MaterialButton selectButton = new MaterialButton("컨텐츠 수정");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			MetroUIContentPanel tgrPanel = (MetroUIContentPanel) getParameters().get("TARGET");
			
			JSONObject recordObj = new JSONObject();
			recordObj.put("HEADER", new JSONString(headerInputBox.getValue()));
			recordObj.put("HEADER_COLOR", new JSONString("#"+headerColorInputBox.getValue()));
			recordObj.put("TITLE", new JSONString(titleInputBox.getValue()));
			recordObj.put("LINK_URL", new JSONString(urlInputBox.getValue()));
			if (chkbox.getValue()) {
				recordObj.put("CONTENT_TYPE_NAME", new JSONString("TEXT"));
			}else {
				recordObj.put("CONTENT_TYPE_NAME", new JSONString("IMAGE"));
			}
			
			recordObj.put("IMG_ID", new JSONString(imageUploadPanel.getImageId() != null ? imageUploadPanel.getImageId() : ""));
			
			tgrPanel.setupContent(recordObj);
			tgrPanel.registContent();
			
			headerInputBox.setText("");
			headerColorInputBox.setText("");
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
		if (getParameters().get("LINK_OBJECT") != null) {
			
			JSONObject paramJSON = (JSONObject)getParameters().get("LINK_OBJECT");
			
			if (paramJSON.get("HEADER") != null) headerInputBox.setText(paramJSON.get("HEADER").isString().stringValue());
			if (paramJSON.get("HEADER_COLOR") != null) headerColorInputBox.setText(paramJSON.get("HEADER_COLOR").isString().stringValue().substring(1));
			if (paramJSON.get("TITLE") != null) titleInputBox.setText(paramJSON.get("TITLE").isString().stringValue());
			if (paramJSON.get("LINK_URL") != null) urlInputBox.setText(paramJSON.get("LINK_URL").isString().stringValue());
			if (paramJSON.get("IMG_ID") != null) {
				String uuid = paramJSON.get("IMG_ID").isString().stringValue();
				imageUploadPanel.setImageId(uuid);
			}
			if (paramJSON.get("CONTENT_TYPE_NAME") != null) {
				String contentType = paramJSON.get("CONTENT_TYPE_NAME").isString().stringValue();
				if (contentType.equals("TEXT")) {
					chkbox.setValue(true);
				}else {
					chkbox.setValue(false);
				}
			}
			
		}else {
			headerInputBox.setText("");
			headerColorInputBox.setText("");
			titleInputBox.setText("");
			imageUploadPanel.setImageUrl("");
			urlInputBox.setText("");
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

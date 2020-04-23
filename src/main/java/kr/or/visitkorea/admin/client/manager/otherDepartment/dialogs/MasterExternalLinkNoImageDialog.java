package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MasterExternalLinkNoImageDialog extends DialogContent {

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
	private MaterialTextBox titleInputBox;
	private MaterialTextBox urlInputBox;
	private String savePath;
	private String imageUUID;
	private String uploadValue;
	private String extString;
	private MaterialButton selectButton;
	private String manId;
	

	public MasterExternalLinkNoImageDialog(MaterialExtentsWindow window) {
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
		
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			
			selectButton.setEnabled(Registry.getPermission("a8c23b1a-4e39-4e36-8d04-1ba6c185eb98"));
			okButton.setEnabled(Registry.getPermission("c22a10d7-a60a-46c2-9c49-8a1f1624e056"));
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			
			selectButton.setEnabled(Registry.getPermission("37a477ec-536f-459a-8444-a4fd3e734e60"));
			okButton.setEnabled(Registry.getPermission("ddba857d-5623-44e0-8e90-4215e0b118be"));
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

		titleInputBox = new MaterialTextBox();
		titleInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleInputBox.setLayoutPosition(Position.ABSOLUTE);
		titleInputBox.setLabel("제목");
		titleInputBox.setLeft(30);
		titleInputBox.setRight(30);
		titleInputBox.setTop(90);
		titleInputBox.setMarginTop(0);
		titleInputBox.setMarginBottom(0);
		this.add(titleInputBox);
		
		urlInputBox = new MaterialTextBox();
		urlInputBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		urlInputBox.setLayoutPosition(Position.ABSOLUTE);
		urlInputBox.setLabel("Link URL");
		urlInputBox.setLeft(30);
		urlInputBox.setRight(30);
		urlInputBox.setTop(200);
		urlInputBox.setMarginTop(0);
		urlInputBox.setMarginBottom(0);
		this.add(urlInputBox);
		
		
		selectButton = new MaterialButton("컨텐츠 생성");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			
			ContentDetail scd = (ContentDetail)getParameters().get("PARENT");

			JSONObject recordObj = new JSONObject();
			recordObj.put("TITLE", new JSONString(this.titleInputBox.getText()));
			recordObj.put("LINK_URL", new JSONString(this.urlInputBox.getValue()));

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
		urlInputBox.setText("");
	}

	private String getContentType(String object) {
		return this.contentType;
	}

	@Override
	public int getHeight() {
		return 350;
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

}

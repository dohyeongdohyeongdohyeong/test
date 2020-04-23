package kr.or.visitkorea.admin.client.manager.widgets.editor.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.ContentEditor;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;

public class EditorDialogContent extends DialogContent {

	private ContentEditor contentEditor;
	private IconType iconType;
	private JSONObject messagesObject;

	@Override
	public void init() {
		
		this.setBackgroundColor(Color.WHITE);
		this.setPaddingTop(10);
		this.setPaddingLeft(20);
		this.setPaddingRight(20);
		this.setPaddingBottom(60);
		
		addDefaultButtons();

	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		
		super.setParameters(parameters);
		
		contentEditor = (ContentEditor)parameters.get("editor");
		iconType = (IconType)parameters.get("icon");
		
		if (iconType != null) {
			MaterialLink link = new MaterialLink(iconType);
			link.setIconSize(IconSize.SMALL);
			link.setLayoutPosition(Position.ABSOLUTE);
			link.setTop(10);
			link.setLeft(20);
			this.add(link);
		}
		
		JSONObject messagesObject = (JSONObject)parameters.get("messages");
		List<String> titleList = toStringList(messagesObject, "title");
		
		for (String title : titleList) {
			MaterialLabel titleLabel = new MaterialLabel(title);
			titleLabel.setTextAlign(TextAlign.LEFT);
			titleLabel.setFontSize("1.5em");
			titleLabel.setFontWeight(FontWeight.BOLDER);
			if (iconType != null){
				titleLabel.setPaddingLeft(40);
			}
			add(titleLabel);
		}
		
		List<String> headerList = toStringList(messagesObject, "header");
		
		for (String header : headerList) {
			MaterialLabel  headerLabel = new MaterialLabel(header);
			headerLabel.setTextAlign(TextAlign.LEFT);
			headerLabel.setFontSize("1.3em");
			headerLabel.setPaddingLeft(40);
			add(headerLabel);
		}
		
		List<String> bodyList = toStringList(messagesObject, "body");
		
		for (String body : bodyList) {
			MaterialLabel  bodyLabel = new MaterialLabel(body);
			bodyLabel.setTextAlign(TextAlign.LEFT);
			bodyLabel.setFontSize("1.1em");
			bodyLabel.setPaddingTop(4);
			bodyLabel.setPaddingLeft(40);
			add(bodyLabel);
		}
		
	}

	protected List<String> toStringList(JSONObject tgrObject, String property) {
		
		Object titleStringArray = tgrObject.get(property);
		List<String> memberArr = new ArrayList<String>();
		
		if (titleStringArray != null && titleStringArray instanceof JSONArray) {
			
			JSONArray jrvalue = (JSONArray)titleStringArray;
			for (int i=0; i<jrvalue.size(); i++) {
				if (jrvalue.get(i).isObject() != null) {
					JSONObject tgrObj = jrvalue.get(i).isObject();
					String valueString = tgrObj.get("value").isString().stringValue();
					if (valueString != null) memberArr.add(valueString);
				}
			}
			
		}
		
		return memberArr;
	}

	protected ContentEditor getContentEditor() {
		return this.contentEditor;
	}
	
	@Override
	protected void addDefaultButtons() {
		
		if (buttonAreaPanel == null) {
			
			buttonAreaPanel = new MaterialPanel();
			buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
			buttonAreaPanel.setWidth("100%");
			buttonAreaPanel.setPaddingLeft(30);
			buttonAreaPanel.setPaddingRight(30);
			buttonAreaPanel.setLeft(0); 
			buttonAreaPanel.setBottom(30); 
			this.add(buttonAreaPanel); 
			
		}

		okButton = new MaterialButton("닫기");
		okButton.setLayoutPosition(Position.RELATIVE);
		okButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		okButton.addClickHandler(event->{
			closeDialog();
		});
		
		buttonAreaPanel.add(okButton);

	}
	
	@Override
	public void closeDialog() {
		contentEditor.closeDialog();
	}
	
	
	@Override
	public int getHeight() {
		return -1;
	}

}

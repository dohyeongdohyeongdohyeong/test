package kr.or.visitkorea.admin.client.manager.widgets.editor.models;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLink;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.PropertiesPanel;

public class PropertiesGroup extends MaterialCollapsibleItem{

	private JSONObject jsonObject;

	private String title;
	private MaterialLink titleLink;
	private MaterialCollapsibleHeader header;
	private MaterialCollapsibleBody body;

	private int DEFAULT_CONTENT_WIDTH = 200;
	private int DEFAULT_CONTENT_HEIGHT = 200;

	private int DEFAULT_CONTENT_POSITION_LEFT= 20;
	private int DEFAULT_CONTENT_POSITION_TOP = 20;

	private PropertiesPanel propertiesPanel;

	private String icon;
	
	public PropertiesGroup(PropertiesPanel propertiesPanel, JSONObject jsonObject) {
		super();
		
		this.propertiesPanel = propertiesPanel;
		this.jsonObject = jsonObject;
		this.title = jsonObject.get("caption").isString().stringValue();
		
		if (jsonObject.get("icon") != null) {
			this.icon = jsonObject.get("icon").isString().stringValue();
		}
		
		layout();
		propPanelSetup();
	}

	private void propPanelSetup() {
		JSONArray propArray = this.jsonObject.get("property").isArray();
		for (int i=0; i<propArray.size(); i++) {
			String id = propArray.get(i).isObject().get("id").isString().stringValue();
			String[] idArray = id.split("\\.");
			if (idArray.length == 2) {
				
				this.propertiesPanel.getDictionaryPanel(idArray[0], idArray[1]);
				
			}
			
			this.body.add(new MaterialLink(id));
			
		}
	}

	private void layout() {

		this.titleLink = new MaterialLink();
		this.titleLink.setIconType(IconType.fromStyleName(this.icon));
		
		this.header = new MaterialCollapsibleHeader();
		this.header.add(titleLink);
		this.body = new MaterialCollapsibleBody();
		this.body.setPadding(10);
	
		this.add(header);
		this.add(body);
		
		this.setText(this.title);
		this.setTextColor(Color.BLACK);
		
	}

	@Override
    public void setText(String text) {
		this.titleLink.setText(text);
    }

    @Override
    public void setTextColor(Color textColor) {
    	this.titleLink.setTextColor(textColor);
    }

	public void setIconType(IconType iconType) {
		this.titleLink.setIconType(iconType);
	}
	
	public void setIconPosition(IconPosition iconPosition) {
		this.titleLink.setIconPosition(iconPosition);
	}

	public void addContent(Widget editorItemContent) {
		this.body.add(editorItemContent);
	}

}

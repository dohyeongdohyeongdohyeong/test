package kr.or.visitkorea.admin.client.manager.widgets.editor.panel;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.editor.ContentEditor;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;
import kr.or.visitkorea.admin.client.manager.widgets.editor.models.PropertiesGroup;
import kr.or.visitkorea.admin.client.manager.widgets.editor.parts.EditorItemPanel;

public class LayerPanel extends MaterialPanel {

	private ContentEditor editor;
	private EditorContentPanel contentPanel;
	private Widget targetWidget;
	private boolean openFlag;
	private JSONObject dictionary;
	private EditorItemPanel propertiesContentArea;
	private Map<String, JSONObject> dictionaryMap;

	public LayerPanel(ContentEditor contentEditor, EditorContentPanel contentPanel) {
		super();
		this.editor = contentEditor;
		this.editor.add(this);
		this.contentPanel = contentPanel;
		this.contentPanel.setLayerPanel(this);
		init();
	}

	public void init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setBorder("1px solid #efefef");
		this.setBackgroundColor(Color.AMBER);
		this.setRight(-200);
		
		MaterialLabel panelTitle = new MaterialLabel("Layer");
		panelTitle.setWidth("100%");
		panelTitle.setHeight("30px");
		panelTitle.setLineHeight(30);
		panelTitle.setTop(0);
		panelTitle.setBackgroundColor(Color.GREY_LIGHTEN_3);
		panelTitle.addClickHandler(event->{
			go(-200);
		});
		
		this.add(panelTitle);
	
		propertiesContentArea = new EditorItemPanel();
		propertiesContentArea.setLayoutPosition(Position.ABSOLUTE);
		propertiesContentArea.setWidth("100%");
		propertiesContentArea.setBackgroundColor(Color.TRANSPARENT);
	
		MaterialPanel propertiesArea = new MaterialPanel();
		propertiesArea.setLayoutPosition(Position.ABSOLUTE);
		propertiesArea.setWidth("100%");
		propertiesArea.setTop(30);
		propertiesArea.setBottom(30);
		propertiesArea.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		propertiesArea.getElement().getStyle().setOverflowY(Overflow.AUTO);
		propertiesArea.setBackgroundColor(Color.TRANSPARENT);
		this.add(propertiesArea);
		
		propertiesArea.add(propertiesContentArea);
		
		
		MaterialPanel panelBottomArea = new MaterialPanel();
		panelBottomArea.setLayoutPosition(Position.ABSOLUTE);
		panelBottomArea.setWidth("100%");
		panelBottomArea.setHeight("30px");
		panelBottomArea.setLineHeight(30);
		panelBottomArea.setBottom(0);
		panelBottomArea.setBackgroundColor(Color.AMBER_LIGHTEN_4);
		this.add(panelBottomArea);
	
	}
	
	private void go(int position) {
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("right");
		cfg.setDuration(300);
		this.setTransition(cfg);
		this.setTransform("translate("+position+"px,0);");
		this.setRight(position);
	}
	
	public void open() {
		go(0);
		this.openFlag = true;
	}
	
	public void close() {
		this.openFlag = false;
		go(-200);
	}
	
	public void addGroup(PropertiesGroup group) {
		propertiesContentArea.add(group);
	}

	public void setContent(Widget widget) {
		
		this.targetWidget = widget;
		
		if ( widget instanceof ContentDetailPanel) {
			setupContentPanelContents();
		}else if (widget instanceof ItemBox){
			setupItemBoxContents();
		}
		
	}

	private void setupContentPanelContents() {
		Console.log("PropertiesPanel.setContent.widget.setupContentPanelProperties");
	}

	private void setupItemBoxContents() {
		
		ItemBox itemBox = (ItemBox) this.targetWidget;
		JSONObject itemBoxJsonObject = itemBox.getComponentData();
		
		JSONValue groupValue = itemBoxJsonObject.get("properties").isObject().get("group");
		
		if (groupValue.isObject() != null) {
			
			// in case of single object.
			
			propertiesContentArea.clear();
			
			JSONObject groupJSONObject = groupValue.isObject();
			setupContents(groupJSONObject, true);
			
			
		}else if (groupValue.isArray() != null) {
			
			// in case of multiple object
			
			propertiesContentArea.clear();
			
			JSONArray groupArray = groupValue.isArray();
			
			int groupArrLength = groupArray.size();
			
			for (int i=0; i<groupArrLength; i++) {
				
				JSONObject groupJSONObject = groupArray.get(i).isObject();
				
				if (i == 0) {
					setupContents(groupJSONObject, true);
				} else {
					setupContents(groupJSONObject, false);
				}
				
			}
		}
		
	}

	private void setupContents(JSONObject groupJSONObject, boolean isExpand) {
		
	}

	public void setDictionary(JSONObject dictionary) {
		
		this.dictionary = dictionary;
		this.dictionaryMap = new HashMap<String, JSONObject>();
		
		JSONArray propertisArray = this.dictionary.get("collection").isObject().get("group").isArray();

		int propLength = propertisArray.size();
		
		for (int i=0; i<propLength; i++) {
			
			JSONObject propObject = propertisArray.get(i).isObject();
			dictionaryMap.put(propObject.get("id").isString().stringValue(), propObject);
			
		}
		
	}
	
	public JSONObject getDictionaryValue(String group, String prop) {
		
		JSONObject retJSON = new JSONObject();
		
		if (this.dictionary == null) {
			
			retJSON.put("status", new JSONString("EXCEPTION"));
			retJSON.put("message", new JSONString("dictionary is not found~!"));
			
		}else {
			
			JSONObject groupObject = this.dictionaryMap.get(group).isObject();
			if (groupObject != null && groupObject.get("property").isArray() != null) {
				
				JSONArray propArray = groupObject.get("property").isArray();
				int propArrayLength = propArray.size();
				
				for (int i=0; i<propArrayLength; i++) {
					
					JSONObject propertyObject = propArray.get(i).isObject();
					String propertyId = propertyObject.get("id").isString().stringValue();
					
					if (propertyId.equals(prop)) {
						
						retJSON.put("status", new JSONString("SUCCESS"));
						retJSON.put("prop", propertyObject);
						
					}
				}
			}
		}
		
		return retJSON;
		
	}

	public void getDictionaryPanel(String groupId, String itemId) {
		
		JSONObject retJSON = new JSONObject();
		
		if (this.dictionary == null) {
			
			retJSON.put("status", new JSONString("EXCEPTION"));
			retJSON.put("message", new JSONString("dictionary is not found~!"));
			
		}else {
			
			retJSON.put("status", new JSONString("SUCCESS"));
			retJSON.put("message", new JSONString(""));
			
			Console.log("==> PropertiesPanel.getDictionaryPanel.groupId :: " + groupId );
			Console.log("==> PropertiesPanel.getDictionaryPanel.itemId :: " + itemId );
			Console.log("=====> dictionary :: " + this.dictionary);
			
			JSONArray propertisArray = this.dictionary.get("collection").isObject().get("group").isArray();
			
		}
		
	}

}

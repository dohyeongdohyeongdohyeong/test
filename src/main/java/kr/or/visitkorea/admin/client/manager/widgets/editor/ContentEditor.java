package kr.or.visitkorea.admin.client.manager.widgets.editor;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.editor.dialog.ContentDialog;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.EditorContentDialog.TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;
import kr.or.visitkorea.admin.client.manager.widgets.editor.models.ComponentGroup;
import kr.or.visitkorea.admin.client.manager.widgets.editor.palette.PalettePanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.EditorContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.LayerPanel;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.PropertiesPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.ConfirmDialogContent;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;

public class ContentEditor extends MaterialPanel {

	private PalettePanel palettePanel;
	private PropertiesPanel propertiesPanel;
	private EditorContentPanel contentPanel;
	private LayerPanel layerPanel;
	private MaterialPanel dialogOverlay = new MaterialPanel();
    private MaterialPanel dialog = new ContentDialog();
	private int diagHeight;
	
	public ContentEditor() {
		super();
		init();
	}
	
	public ContentEditor(String... initialClass) {
		super(initialClass);
		init();
	}
	
	public void  init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setBackgroundColor(Color.BLUE_GREY_LIGHTEN_3);
		this.setLeft(0);
		this.setRight(0);
		this.setTop(0);
		this.setBottom(0);
		
		this.contentPanel = new EditorContentPanel(this);
		this.palettePanel = new PalettePanel(this, this.contentPanel);
		this.propertiesPanel = new PropertiesPanel(this, this.contentPanel);
		this.layerPanel = new LayerPanel(this, this.contentPanel);
		
		this.dialogOverlay.setStyleName("dialogOverlay");
		this.dialogOverlay.setVisible(false);
		this.dialogOverlay.setTop(30);
		this.dialogOverlay.setHeight("630px");
		
		this.palettePanel.setWidth("200px");
		this.palettePanel.setHeight("100%");
		
		this.propertiesPanel.setWidth("200px");
		this.propertiesPanel.setTop(30);

		this.layerPanel.setWidth("200px");
		this.layerPanel.setTop(30);
		
		this.add(this.contentPanel);
		this.add(this.palettePanel);
		this.add(this.propertiesPanel);
		this.add(this.layerPanel);
		this.add(this.dialogOverlay);
		this.add(this.dialog);
		
		this.propertiesPanel.getElement().getStyle().setProperty("height", "calc( 100% - 30px )");
		this.layerPanel.getElement().getStyle().setProperty("height", "calc( 100% - 30px )");

	}

	public void loadComponentGroups(String[] strings) {
		
		loadComponentProperties();
		
		for (String groupName : strings) {
			
			loadComponentGroup(groupName) ;
			
		}
		
	}

	private void loadComponentProperties() {
		
    	JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_EDITOR_PROPERTIES"));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					
					Console.log(bodyObj.toString());
					
					JSONObject bodyResultObj = (JSONObject) bodyObj.get("result").isObject().get("PROPERTIES");
					propertiesPanel.setDictionary(bodyResultObj);
				}
			}
		});
		
	}

	private void loadComponentGroup(final String title) {
		
    	JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_EDITOR_GROUP"));
		parameterJSON.put("CAPTION", new JSONString(title));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					
					Console.log(bodyObj.toString());
					
					JSONObject bodyResultObj = (JSONObject) bodyObj.get("result").isObject().get("COMPONENT");
					
					if (bodyResultObj != null) {
						
						// group have a single member
						if (bodyResultObj.get("group").isObject() != null) {
							
							JSONObject grpObject = bodyResultObj.get("group").isObject();
							
							if (grpObject.get("group").isObject() != null) {

								JSONObject groupObject = grpObject.get("group").isObject();
								
								ComponentGroup componentGroup = new ComponentGroup(groupObject, contentPanel.getContentPanel());
								applylEvent(componentGroup);
								palettePanel.addGroup(componentGroup);
								
							}else if (grpObject.get("group").isArray() != null) {
								
								JSONArray groupArray = grpObject.get("group").isArray();
								
								int grpSize = groupArray.size();
								int i = 0;
								for (i = 0; i<grpSize; i++) {
									
									JSONObject grpArrObject = groupArray.get(i).isObject();
									ComponentGroup componentGroup = new ComponentGroup(grpArrObject, contentPanel.getContentPanel());
									applylEvent(componentGroup);
									palettePanel.addGroup(componentGroup);
									
								}
							}
						}
					}
				}
			}
		});
		
	}

	private void applylEvent(ComponentGroup componentGroup) {
		
	}

	public PalettePanel getPalettePane() {
		return this.palettePanel;
	}

	public PropertiesPanel getPropertiesPanel() {
		return this.propertiesPanel;
	}

	public LayerPanel getLayerPanel() {
		return this.layerPanel;
	}


	public void openDialog(TYPE diagType, ItemBox itemBox, JSONObject dialogParameter) {
		
		if (diagType.equals(TYPE.CONFIRM)) {
			
			// define display target dialog content
			DialogContent confirmDialogContent = new ConfirmDialogContent();
			
			// setup dialog parameters
			Map<String, Object> diagParameter = new HashMap<String, Object>();
			
			diagParameter.put("item", itemBox);
			diagParameter.put("icon", IconType.CHECK_CIRCLE);
			diagParameter.put("messages",  dialogParameter);
			
			// opening dialog content
			openDialog(confirmDialogContent, diagParameter, 600, 200);
			
		}else if (diagType.equals(TYPE.ALERT)) {
			
		}else if (diagType.equals(TYPE.CUSTOM)) {
			
		}
		
	}

	
	/**
	 * open dialog with paramters and opening diag size
	 * 
	 * @param string
	 * @param paramters
	 */
	public void openDialog(DialogContent dContent, Map<String, Object> paramters, int diagWidth, int diagHeight) {
		
		paramters.put("editor", this);
      	dContent.setParameters(paramters);
		dContent.setHeight("100%");
		
		this.diagHeight = diagHeight;
		
		dialog.setHeight(diagHeight+"px");
		dialog.clear();
        dialog.add(dContent);
     
        dialogOverlay.setVisible(true);
        dialogOverlay.setVisibility(Visibility.VISIBLE);
		
        dialog.setWidth(diagWidth+"px");
        dialog.setLeft((this.getOffsetWidth() - diagWidth) / 2);
		dialog.setVisibility(Visibility.VISIBLE);		
		dialog.setTransform("translate(0, "+(diagHeight * -2)+"px);");
		dialog.setTop(0);
		
	}

	public void closeDialog() {
        
		dialogOverlay.setVisible(false);
        dialogOverlay.setVisibility(Visibility.HIDDEN);
        
		dialog.setTransform("translate(0, 0px);");
		dialog.setTop((diagHeight * -2));
		
	}

}

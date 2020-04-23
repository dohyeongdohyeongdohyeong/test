package kr.or.visitkorea.admin.client.manager.widgets.editor.models;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.widgets.editor.controller.EditAreaController;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ImageItem;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;
import kr.or.visitkorea.admin.client.manager.widgets.editor.panel.ContentDetailPanel;

public class ComponentGroup extends MaterialCollapsibleItem{

	private JSONObject jsonObject;

	private String title;
	private MaterialLink titleLink;
	private MaterialCollapsibleHeader header;
	private MaterialCollapsibleBody body;

	private int DEFAULT_CONTENT_WIDTH = 200;
	private int DEFAULT_CONTENT_HEIGHT = 200;

	private int DEFAULT_CONTENT_POSITION_LEFT= 20;
	private int DEFAULT_CONTENT_POSITION_TOP = 20;
	
	private ContentDetailPanel contentPanel;

	public ComponentGroup(JSONObject jsonObject, ContentDetailPanel contentPanel) {
		super();
		
		this.contentPanel = contentPanel;
		this.jsonObject = jsonObject;
		this.title = jsonObject.get("caption").isString().stringValue();
		
		layout();
		setupComponent();
		
	}

	private void setupComponent() {
		
		if (this.jsonObject.containsKey("component")) {
			
			if (this.jsonObject.get("component").isObject() != null) {
				
				JSONObject targetObject = this.jsonObject.get("component").isObject();
				MaterialButton editoritem = new MaterialButton();
				editoritem.setPaddingLeft(6);
				editoritem.setPaddingRight(3);
				editoritem.setWidth("140px");
				if (targetObject.containsKey("icon")) {
					IconType objIcon = IconType.fromStyleName(
							targetObject.get("icon").isString().stringValue().toLowerCase());
					editoritem.setIconType(objIcon);
				}
				editoritem.setFontSize("0.7em");
				editoritem.setMargin(2);
				editoritem.setIconPosition(IconPosition.LEFT);
				editoritem.setText(targetObject.get("caption").isString().stringValue());
				editoritem.addClickHandler(event->{
					
				    MaterialImage imageItem = new MaterialImage("https://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg");
				    
				    EditAreaController windowController = new EditAreaController(contentPanel);
				    windowController.getPickupDragController().addDragHandler(new WindowDragHandler(imageItem));

				    ItemBox windowPanel1 = new ItemBox(windowController, imageItem, false, contentPanel);
				    contentPanel.add(windowPanel1, DEFAULT_CONTENT_POSITION_LEFT, DEFAULT_CONTENT_POSITION_TOP);

				});
				addContent(editoritem);
				
			}else if (this.jsonObject.get("component").isArray() != null) {
				
				JSONArray componentArray = this.jsonObject.get("component").isArray();
				int cSiz = componentArray.size();
				for (int i=0; i<cSiz; i++) {
					JSONObject targetObject = componentArray.get(i).isObject();
					
					MaterialButton editoritem = new MaterialButton();
					editoritem.setPaddingLeft(6);
					editoritem.setPaddingRight(3);
					editoritem.setWidth("140px");
					
					if (targetObject.containsKey("icon")) {
						IconType objIcon = IconType.fromStyleName( targetObject.get("icon").isString().stringValue().toLowerCase());
						editoritem.setIconType(objIcon);
					}
					
					editoritem.setFontSize("0.7em");
					editoritem.setMargin(2);
					editoritem.setIconPosition(IconPosition.LEFT);
					editoritem.setText(targetObject.get("caption").isString().stringValue());
					editoritem.addClickHandler(event->{

						Widget contentItem = null;
						
						//TODO 아이템 컴포넌트 타입별 핸들링 및 정리 ==> 일반화 필요함
						if (editoritem.getText().equals("텍스트")) {
							
							contentItem = new MaterialLabel("텍스트");
							contentItem.setPixelSize(40, 20);
							((MaterialLabel)contentItem).addKeyDownHandler(keyDownEvent->{
								MaterialToast.fireToast("keyDownEvent :: "+ keyDownEvent.getNativeKeyCode());
							});
							
						}else if (editoritem.getText().equals("이미지")) {
							
							ImageItem dfiip = new ImageItem();
							dfiip.setRenderSideWidget(new MaterialImage("/images/adminanswerlogo.png"));
							dfiip.setPixelSize(100, 100);
							dfiip.setViewMode(true);
							
							contentItem = dfiip;
							
						}
					    
					    EditAreaController windowController = new EditAreaController(contentPanel);
					    windowController.getPickupDragController().addDragHandler(new WindowDragHandler(contentItem));

					    ItemBox itemBox = new ItemBox(windowController, contentItem, false, contentPanel);
					    itemBox.setComponentData(targetObject);
					    contentPanel.add(itemBox, DEFAULT_CONTENT_POSITION_LEFT, DEFAULT_CONTENT_POSITION_TOP);

					});
					addContent(editoritem);
					
				}
			}
		}
	}
	
	private void layout() {

		this.titleLink = new MaterialLink();
		
		this.header = new MaterialCollapsibleHeader();
		this.header.add(titleLink);
		this.body = new MaterialCollapsibleBody();
		this.body.setPadding(10);
	
		this.add(header);
		this.add(body);
		
		this.setText(this.title);
		
		if (jsonObject.containsKey("icon")) {
			IconType objIcon = IconType.fromStyleName(jsonObject.get("icon").isString().stringValue().toLowerCase());
			this.setIconType(objIcon);
		}

		this.setIconPosition(IconPosition.LEFT);
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

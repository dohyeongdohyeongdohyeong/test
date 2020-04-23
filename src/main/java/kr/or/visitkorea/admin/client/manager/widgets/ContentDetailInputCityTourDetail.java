package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;

public class ContentDetailInputCityTourDetail extends ContentDetailItem{
	
	public ContentDetailInputCityTourDetail(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete) {
		super(treeItem, displayLabel, cotId, displayDelete, false);
		init();
	}

	protected void init() {
		
		this.isRenderMode = true;
		
		if (isDisplayLabel) {
			
			this.icon = new MaterialIcon(IconType.ARCHIVE);
			this.icon.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
			this.title = new MaterialLabel(titleName);
			this.title.setTextAlign(TextAlign.CENTER);
			this.title.setFontSize("1.2em");
			this.title.setFontWeight(FontWeight.BOLD);

			this.add(this.icon);
			this.add(this.title);

		}	

		this.content = new MaterialPanel();
		this.content.setStyle("clear:both;");
		this.content.setFloat(com.google.gwt.dom.client.Style.Float.NONE);
		this.content.setPaddingTop(1);

		if (!isDisplayLabel) {
			
			this.contentPreview = new MaterialPanel();
			this.contentPreview.setVisible(true);
			this.contentPreview.setLayoutPosition(Position.RELATIVE);
			
			MaterialLabel previewTitle = new MaterialLabel(titleName);
			previewTitle.setTextAlign(TextAlign.LEFT);
			previewTitle.setFontSize("1.1em");
			previewTitle.setPaddingLeft(10);
			previewTitle.setBackgroundColor(Color.GREY_LIGHTEN_2);
	
			MaterialPanel dispContent = new MaterialPanel();
			dispContent.setWidth("100%");
			
			if (this.item.getEditorSafeValue() == null) {
				dispContent.setHeight("40px");
			}else {
				dispContent.getElement().setInnerSafeHtml(this.item.getEditorSafeValue());
			}
			
			dispContent.setBorder("1px solid #e0e0e0");
			
			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");
			
			contentPreview.add(previewTitle);
			contentPreview.add(dispContent);
			contentPreview.add(menuContent);
			
				
			dispContent.setTextAlign(TextAlign.CENTER);
			
			MaterialIcon iconAdd = new MaterialIcon(IconType.ADD);
			iconAdd.setFloat(Style.Float.RIGHT);
			iconAdd.setMarginLeft(0);
			iconAdd.addClickHandler(event->{
				
				JSONObject introJSON = (JSONObject) this.item.getInternalReferences().get("INTRO");
				dispContent.add(new CitytourDetailItem(null, introJSON));
				
			});
			
			menuContent.add(iconAdd);
			renderComponent(dispContent);

			
			this.content.add(this.contentPreview);
			
		}
		
		this.add(this.content);
		
	}

	protected void renderComponent(MaterialPanel dispContent) {
		
		this.isRenderMode = true;
		
		dispContent.clear();
		dispContent.setHeight("");
		
		JSONArray jArray = (JSONArray)item.getInternalReferences().get("TGR_OBJ");
		JSONObject intro = (JSONObject)item.getInternalReferences().get("INTRO");
		int itemSize = jArray.size();
		itemList.clear();
		for (int i=0; i<itemSize; i++) {
			itemList.add(jArray.get(i).isObject());
			dispContent.add(new CitytourDetailItem(jArray.get(i).isObject(), intro));
		}
		
	}

	protected void createComponent(MaterialPanel tgrPanel) {
		this.isRenderMode = false;
	}
	
    public void addContent(MaterialWidget child) {
    	child.setVisible(false);
    	this.content.add(child);
    }
	
	public MaterialIcon getIcon() {
		return this.icon;
	}
	
	public void setIconType(IconType iconType) {
		this.icon.setIconType(iconType);
	}

	public void setCotentMarginTop(double i) {
		this.content.setMarginTop(i);
	}
	

	@Override
	public void deleteData() {

		afterProcessing();

/*
		
		if (this.item.getInternalReferences().get("ACI_ID") != null) {
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("DELETE_ARTICLE_SINGLE_ROW"));
			parameterJSON.put("aciId", new JSONString(this.item.getInternalReferences().get("ACI_ID").toString()));
			
			VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					afterProcessing();
				}
			});
		}else {
			afterProcessing();
		}
*/
		
	}

	private void afterProcessing() {
		
		Object contentTree = item.getInternalReferences().get("CONTENT_TREE");
		
		item.removeFromParent();
		getParent().removeFromParent();
		
		if (contentTree != null && contentTree instanceof RecommContentsTree) {
			RecommContentsTree rct = (RecommContentsTree) contentTree;
			rct.reOrderItems();
			rct.renderDetail();
		}
	}
		
}
package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;

public class ContentDetailInputAdditionalInformation extends ContentDetailItem{

	public ContentDetailInputAdditionalInformation(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete) {
		super(treeItem, displayLabel, cotId, displayDelete, false);
		init();
	}

	protected void init() {
		
		this.isRenderMode = true;
		this.setWidth("100%");
		
		if (isDisplayLabel) {
			
			this.icon = new MaterialIcon(IconType.ARCHIVE);
			this.icon.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
			this.title = new MaterialLabel(titleName);
			this.title.setTextAlign(TextAlign.LEFT);
			this.title.setFontSize("1.2em");
			this.title.setFontWeight(FontWeight.BOLD);
			this.title.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);

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
			
			MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
			iconEdit.setFloat(Style.Float.RIGHT);
			iconEdit.setMarginLeft(0);
			iconEdit.addClickHandler(event->{
				if (this.isRenderMode) createComponent(dispContent);
			});
			
			MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);
			iconSave.setFloat(Style.Float.RIGHT);
			iconSave.setMarginLeft(0);
			iconSave.addClickHandler(event->{
				if (!this.isRenderMode) renderComponent(dispContent);
			});

			menuContent.add(iconSave);
			menuContent.add(iconEdit);
			renderComponent(dispContent);

			this.content.add(this.contentPreview);
			
		}
		
		this.add(this.content);
		
	}

	protected void renderComponent(MaterialPanel dispContent) {
		
		this.isRenderMode = true;
		
	}

	protected void createComponent(MaterialPanel tgrPanel) {
		this.isRenderMode = false;
		
		tgrPanel.clear();
		tgrPanel.getElement().setInnerText("");
		
		AdditionalInformationPanel additionalPanel = new AdditionalInformationPanel();
		additionalPanel.setPadding(5);
		
		tgrPanel.setHeight("");
		tgrPanel.add(additionalPanel);
			
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
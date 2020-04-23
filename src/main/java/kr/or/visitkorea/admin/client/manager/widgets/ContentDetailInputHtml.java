package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.editor.EditorBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputHtml extends ContentDetailItem{

	private MaterialIcon iconDelete;
	
	private boolean isSave = false;

	public ContentDetailInputHtml(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete, boolean ordering) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		init();
	}

	public ContentDetailInputHtml(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete, boolean ordering, boolean isSave) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		this.isSave = isSave;
		init();
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
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
			previewTitle.setTextAlign(TextAlign.CENTER);
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
			
			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");
			
			MaterialPanel verticalEffect = new MaterialPanel();
			verticalEffect.setWidth("6px");
			verticalEffect.setHeight("20px");
			verticalEffect.setTop(28);
			verticalEffect.setLeft(-12);
			verticalEffect.setLayoutPosition(Position.ABSOLUTE);
			verticalEffect.setBackgroundColor(Color.BLUE);
			verticalEffect.setVisibility(Visibility.HIDDEN);
			
			contentPreview.add(verticalEffect);
			contentPreview.add(previewTitle);
			contentPreview.add(dispContent);
			contentPreview.add(menuContent);

			MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
			MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);

			iconEdit.setFloat(Style.Float.RIGHT);
			iconEdit.setMarginLeft(0);
			iconEdit.addClickHandler(event->{
				if (this.isRenderMode) {
					iconEdit.setVisible(false);
					iconSave.setVisible(true);
					if (iconDelete != null) iconDelete.setVisible(false);
					createComponent(dispContent);
				}
			});
			
			iconSave.setFloat(Style.Float.RIGHT);
			iconSave.setMarginLeft(0);
			iconSave.addClickHandler(event->{
				if (!this.isRenderMode) {

					if (isSave) saveData();
					
					if (item.getInternalReferences().get("CONTENT_TREE") != null && item.getInternalReferences().get("CONTENT_TREE") instanceof RecommContentsTree) {
						RecommContentsTree rct = (RecommContentsTree) item.getInternalReferences().get("CONTENT_TREE");
						rct.reOrderItems();
					}
					iconEdit.setVisible(true);
					iconSave.setVisible(false);
					if (iconDelete != null) iconDelete.setVisible(true)
					;

					renderComponent(dispContent);
					
				}
			});
			
			iconSave.setVisible(false);
			
			if (this.displayDelete) {
				
				iconDelete = new MaterialIcon(IconType.REMOVE);
				iconDelete.setFloat(Style.Float.RIGHT);
				iconDelete.setMarginLeft(0);
				iconDelete.addClickHandler(event->{
					if (this.isRenderMode) {
						
						Object windowObj = this.item.getInternalReferences().get("WINDOW");
						Object diagId = this.item.getInternalReferences().get("DIALOG_ID");
						
						if (windowObj != null && diagId != null) {
							
							MaterialExtentsWindow win = (MaterialExtentsWindow) windowObj;
							String diagIdStr = (String)diagId;
							
							Map<String, Object> paramterMap = new HashMap<String, Object>();
							paramterMap.put("ITEM", this);
							paramterMap.put("CONTENT_TREE", this.item.getInternalReferences().get("WINDOW"));
							
							win.openDialog(diagIdStr, paramterMap, 800);
							
						}

					}
				});
				menuContent.add(iconDelete);
			}
			addOrdering(menuContent);
			
			menuContent.add(iconSave);
			menuContent.add(iconEdit);

			if (this.item.getInternalReferences().containsKey("IS_VERTICAL")) {
				double isVerticle = (double) this.item.getInternalReferences().get("IS_VERTICAL");
				double isBox = (double) this.item.getInternalReferences().get("IS_BOX");
				
				dispContent.setBorder(isBox == 0 ? "1px solid #e0e0e0" : "2px solid #2196f3");
				verticalEffect.setVisibility(isVerticle == 0 ? Visibility.HIDDEN : Visibility.VISIBLE);
				
				MaterialLink linkVerticle = new MaterialLink("V");
				linkVerticle.setFloat(Float.RIGHT);
				linkVerticle.setPaddingLeft(6);
				linkVerticle.setPaddingRight(7);
				linkVerticle.setFontSize("17px");
				linkVerticle.setTextColor(Color.BLUE);
				linkVerticle.setFontWeight(FontWeight.BOLD);
				linkVerticle.setTextColor(isVerticle == 1 ? Color.RED : Color.BLACK);
				linkVerticle.addClickHandler(event -> {
					double tempIsVerticle = 
							(Double) this.item.getInternalReferences().get("IS_VERTICAL") == 0 ? 1 : 0;
					
					verticalEffect.setVisibility(tempIsVerticle == 0 ? Visibility.HIDDEN : Visibility.VISIBLE);
					linkVerticle.setTextColor(tempIsVerticle == 1 ? Color.RED : Color.BLACK);
					item.getInternalReferences().put("IS_VERTICAL", tempIsVerticle);
				});
	
				MaterialLink linkBox = new MaterialLink("B");
				linkBox.setFloat(Float.RIGHT);
				linkBox.setPaddingLeft(6);
				linkBox.setPaddingRight(7);
				linkBox.setFontSize("17px");
				linkBox.setTextColor(Color.BLACK);
				linkBox.setFontWeight(FontWeight.BOLD);
				linkBox.setTextColor(isBox == 1 ? Color.RED : Color.BLACK);
				linkBox.addClickHandler(event -> {
					double tempIsBox = 
							(Double) this.item.getInternalReferences().get("IS_BOX") == 0 ? 1 : 0;
	
					dispContent.setBorder(tempIsBox == 0 ? "1px solid #e0e0e0" : "2px solid #2196f3");
					linkBox.setTextColor(tempIsBox == 1 ? Color.RED : Color.BLACK);
					item.getInternalReferences().put("IS_BOX", tempIsBox);
				});
				
				menuContent.add(linkBox);
				menuContent.add(linkVerticle);
			}
			
			renderComponent(dispContent);
				
			this.content.add(this.contentPreview);
			
		}
		
		this.add(this.content);
		
	}

	protected void renderComponent(MaterialPanel dispContent) {
		
		this.isRenderMode = true;
		
		dispContent.clear();
		dispContent.setHeight("");
		
		if (item.getEditorSafeValue() != null && item.getEditorSafeValue().asString().length() > 0) {

			String value = item.getEditorSafeValue().asString().replace("safe:", "");
			
			if(value.indexOf("<") == -1
				&& value.indexOf(">") == -1) {
				value = "<p>" + value + "</p>";
				SafeHtmlBuilder boxContentBuilder = new SafeHtmlBuilder();
				boxContentBuilder.appendHtmlConstant(value);
				item.setEditorSafeValue(boxContentBuilder.toSafeHtml());
			}
			
			dispContent.getElement().setInnerSafeHtml(item.getEditorSafeValue());
		}
		
		if (this.displayDelete && iconDelete != null) {
			iconDelete.setVisibility(Visibility.VISIBLE);
			iconDelete.setVisible(true);
		}

	}

	protected void createComponent(MaterialPanel tgrPanel) {
		
		this.isRenderMode = false;
		
		tgrPanel.clear();
		tgrPanel.getElement().setInnerText("");
		
		EditorBase box = new EditorBase();
		box.setMiscOptions(ToolbarButton.CODE_VIEW);
		box.setAirMode(false);
		box.setHeight("300px");
		box.setTop(-30);
		box.setPadding(5);
		
		if  (item.getEditorSafeValue() != null) box.setText(item.getEditorSafeValue().asString());
		
		box.addValueChangeHandler(boxEvent->{
			SafeHtmlBuilder boxContentBuilder = new SafeHtmlBuilder();
			boxContentBuilder.appendHtmlConstant(box.getText());
			item.setEditorSafeValue(boxContentBuilder.toSafeHtml());
		});
		
		tgrPanel.setHeight("");
		tgrPanel.add(box);
		
		box.setFocus(true);
			
		if (this.displayDelete && iconDelete != null) {
			iconDelete.setVisibility(Visibility.HIDDEN);
			iconDelete.setVisible(false);
		}
		
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
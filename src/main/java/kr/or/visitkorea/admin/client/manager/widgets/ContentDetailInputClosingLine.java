package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputClosingLine extends ContentDetailItem {

	private MaterialIcon iconDelete;
	
	public ContentDetailInputClosingLine(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete,
			boolean ordering) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		init();
	}

	@Override
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
			this.contentPreview.setHeight("100%");
			
			MaterialLabel previewTitle = new MaterialLabel(titleName);
			previewTitle.setTextAlign(TextAlign.CENTER);
			previewTitle.setFontSize("1.1em");
			previewTitle.setPaddingLeft(10);
			previewTitle.setBackgroundColor(Color.GREY_LIGHTEN_2);
			
			MaterialPanel dispContent = new MaterialPanel();
			dispContent.setWidth("100%");

			MaterialPanel dispContentBox = new MaterialPanel();
			dispContentBox.setLayoutPosition(Position.RELATIVE);
			dispContentBox.setHeight("55px");
			dispContentBox.setBorder("1px solid #e0e0e0");
			
			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");
			
			MaterialPanel line = new MaterialPanel();
			line.setBorder("1px solid black");
			line.setWidth("80%");
			line.setMargin(25);
			line.setMarginLeft(80);
			line.setMarginRight(80);
			
			dispContentBox.add(line);
			dispContentBox.add(dispContent);
			contentPreview.add(previewTitle);
			contentPreview.add(dispContentBox);
			contentPreview.add(menuContent);
			
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
			this.content.add(this.contentPreview);
		}
		
		this.add(this.content);
	}

	@Override
	protected void renderComponent(MaterialPanel dispContent) {
	}

	@Override
	protected void createComponent(MaterialPanel tgrPanel) {
	}

	@Override
	public void deleteData() {
		afterProcessing();
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


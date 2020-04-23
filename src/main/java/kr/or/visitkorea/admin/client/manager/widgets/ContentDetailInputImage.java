package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsDetails;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputImage extends ContentDetailItem{
	
	private MaterialIcon iconDelete;
	private MaterialPanel dispContentBox;
	private RecommContentsDetails recommContentsDetails;
	public ContentDetailInputImage(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete, boolean ordering, RecommContentsDetails recommContentsDetails) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		this.recommContentsDetails= recommContentsDetails;
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

		if (!isDisplayLabel) {
			
			this.contentPreview = new MaterialPanel();
			this.contentPreview.setVisible(true);
			this.contentPreview.setLayoutPosition(Position.RELATIVE);
			
			MaterialLabel previewTitle = new MaterialLabel(titleName);
			previewTitle.setTextAlign(TextAlign.CENTER);
			previewTitle.setFontSize("1.1em");
			previewTitle.setPaddingLeft(10);
			previewTitle.setBackgroundColor(Color.GREY_LIGHTEN_2);

			dispContentBox = new MaterialPanel();
			dispContentBox.setLayoutPosition(Position.RELATIVE);
			dispContentBox.getElement().getStyle().setOverflowX(Overflow.VISIBLE);
			dispContentBox.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
			dispContentBox.setHeight("240px");
			dispContentBox.setBorder("1px solid #e0e0e0");
			
			MaterialPanel dispContent = new MaterialPanel();
			dispContent.setDisplay(Display.INLINE_BLOCK);
			dispContent.setWidth("auto");
			dispContent.setHeight("238px");
			dispContent.setLayoutPosition(Position.ABSOLUTE);
			dispContent.setLeft(0);
			dispContent.setMaxHeight("238px");;
			
			dispContentBox.add(dispContent);
			
			if (this.item.getEditorSafeValue() == null) {
				//dispContent.setHeight("40px");
			}else {
				dispContent.getElement().setInnerSafeHtml(this.item.getEditorSafeValue());
			}
			
			
			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");
			
			contentPreview.add(previewTitle);
			contentPreview.add(dispContentBox);
			contentPreview.add(menuContent);
			
			dispContent.setTextAlign(TextAlign.CENTER);
			
			MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
			MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);

			iconSave.setVisible(false);
			
			iconEdit.setFloat(Style.Float.RIGHT);
			iconEdit.setMarginLeft(0);
			iconEdit.addClickHandler(event->{
				if (this.isRenderMode) {
					
					dispContent.setWidth("100%");
					dispContentBox.setOverflow(Overflow.HIDDEN);
					dispContentBox.setHeight("470px");
					
					iconSave.setVisible(true);
					iconEdit.setVisible(false);
					iconDelete.setVisible(false);
					
					createComponent(dispContent);
				}
			});
			
			iconSave.setFloat(Style.Float.RIGHT);
			iconSave.setMarginLeft(0);
			iconSave.addClickHandler(event->{
				
				if (!this.isRenderMode) {
					
					dispContent.setLeft(0);

					this.item.setEditorObject(imageList);

					dispContentBox.getElement().getStyle().setOverflowX(Overflow.VISIBLE);
					dispContentBox.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
					dispContentBox.setHeight("240px");
					
					if (item.getInternalReferences().get("CONTENT_TREE") != null && item.getInternalReferences().get("CONTENT_TREE") instanceof RecommContentsTree) {
						RecommContentsTree rct = (RecommContentsTree) item.getInternalReferences().get("CONTENT_TREE");
						rct.reOrderItems();
					}
					
					iconSave.setVisible(false);
					iconEdit.setVisible(true);
					iconDelete.setVisible(true);

					renderComponent(dispContent);
				}
			});
			
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
			renderComponent(dispContent);
			
			this.content.add(this.contentPreview);
			
		}
		
		this.add(this.content);
		
	}

	protected void renderComponent(MaterialPanel dispContent) {
		
		this.isRenderMode = true;
		
		dispContent.clear();
		
		renderImage(this.item, dispContent);
		
		if (this.displayDelete) {
			iconDelete.setVisibility(Visibility.VISIBLE);
			iconDelete.setVisible(true);
		}
	
	}
	
	private void renderImage(ContentTreeItem treeItem, MaterialPanel dispContent) {
		
		List<ItemInformation> children = treeItem.getEditorObject();
		
		this.imageList = children;
		
		dispContent.setPadding(10);
		dispContent.setTextAlign(TextAlign.LEFT);
		
		
		for (ItemInformation child : children) {
			
			ImageInformation imageInfo = (ImageInformation)child;
			String url = imageInfo.getUrl();
			String comment = imageInfo.getComment();
			
			ContentImage image = new ContentImage();
			image.setDisplay(Display.INLINE_BLOCK);
			image.setUrl(url);
			image.setCaption(comment);
			image.setWidth("auto");
			image.setHeight("auto");
			image.setMaxWidth("270px");
			image.setMaxHeight("230px");
			image.setMarginRight(10);
			
			dispContent.add(image);
			
		}
		
		List<Widget> widgets = dispContent.getChildrenList();
		for (Widget widget : widgets) {
			Console.log(((ContentImage)widget).getWidth() + "px");
		}
		
		dispContent.setWidth(((270 + 20) * children.size())+"px"); 

	}

	protected void createComponent(MaterialPanel tgrPanel) {
		
		this.isRenderMode = false;
		
		tgrPanel.setLeft(0);
		tgrPanel.clear();
		tgrPanel.getElement().setInnerText("");
		tgrPanel.setPadding(10);
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("COT_ID", this.cotId);
		valueMap.put("CONTENT_ORDER", this.item.getInternalReferences().get("CONTENT_ORDER"));
		valueMap.put("ARTICLE_ORDER", this.item.getInternalReferences().get("ARTICLE_ORDER"));
		valueMap.put("ARTICLE_TYPE", this.item.getInternalReferences().get("ARTICLE_TYPE"));
		valueMap.put("ID_MAP", this.item.getInternalReferences().get("ID_MAP"));

		ContentImageListPanel cilp = new ContentImageListPanel(this.imageList, valueMap,this);
		
		tgrPanel.add(cilp);
		
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
		
		HashMap<String, Object> idValueMap = (HashMap<String, Object>) item.getInternalReferences().get("ID_MAP");
		Set<String> idArr = idValueMap.keySet();
		String targets = "";
		int idIdx = 0;
		for (String id : idArr) {
			if (idIdx == 0) {
				targets += "'"+id+"'";
			}else {
				targets += ",'"+id+"'";
			}
			idIdx++;
		}

		afterProcessing();
/*		
		if (targets.length() > 0) {
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("DELETE_ARTICLE_IMAGES"));
			parameterJSON.put("targets", new JSONString(targets));
			
			VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					Console.log("targets.length  is not zero~!");
					afterProcessing();
				}
			});
			
		}else {
			
			Console.log("targets.length  is zero~!");
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

	public RecommContentsDetails getrecommContentsDetails() {
		return recommContentsDetails;
	}
	
}
package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialVideo;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputMovie extends ContentDetailItem{
	
	private MaterialIcon iconDelete;

	public ContentDetailInputMovie(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete, boolean ordering) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
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
			MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);

			iconSave.setVisible(false);
			
			iconEdit.setFloat(Style.Float.RIGHT);
			iconEdit.setMarginLeft(0);
			iconEdit.addClickHandler(event->{
				if (this.isRenderMode) {
					
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
				
					String playerId = IDUtil.uuid().toString();
					String divId = IDUtil.uuid().toString();
					String backupValue = item.getEditorValue();
					
					Console.log(backupValue);
					String videoId = getVideoId();
					String videoFormat = "<div id=\"youtube\" style=\"height:100%;\"><div id=\""+divId+"\"></div></div>​<script>window.ytPlayerList.push({ Id: '"+playerId+"', DivId: '"+divId+"', VideoId: '"+videoId
							+"', playerVars: {rel:0, playsinline:1,}"
							+ "});</script>\n";
					item.getInternalReferences().put("ARTICLE_TITLE", backupValue);
					item.getInternalReferences().put("VIDEO_FORMAT", videoFormat);
/*
					item.setEditorValue(videoFormat);
					saveData();
*/
					item.setEditorValue(backupValue);

/*
					if (item.getInternalReferences().get("CONTENT_TREE") != null && item.getInternalReferences().get("CONTENT_TREE") instanceof RecommContentsTree) {
						RecommContentsTree rct = (RecommContentsTree) item.getInternalReferences().get("CONTENT_TREE");
						rct.reOrderItems();
					}
*/
					
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
			}
			
			addOrdering(menuContent);
			
			menuContent.add(iconDelete);
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
		dispContent.setHeight("");
		
		String videoId = getVideoId();
		String videoUrl = "https://www.youtube.com/embed/" + videoId;
		dispContent.add(new MaterialVideo(videoUrl));
		
		if (this.displayDelete && iconDelete != null) {
			iconDelete.setVisibility(Visibility.VISIBLE);
			iconDelete.setVisible(true);
		}
		
	}

	protected void createComponent(MaterialPanel tgrPanel) {
		
		this.isRenderMode = false;
			
		tgrPanel.clear();
		tgrPanel.setHeight("");
		tgrPanel.getElement().setInnerText("");
		
		MaterialVideo player = new MaterialVideo();
		
		MaterialTextBox box = new MaterialTextBox();
		box.setText(player.getUrl());
		box.setMarginTop(20);
		box.setLeft(30);
		box.setWidth("830px");
		box.setLabel("URL");
		box.setPlaceholder("동영상의 URL 을 입력해 주세요.");
		
		box.addKeyUpHandler(event->{
//			if (event.getNativeKeyCode() == 13) {
				String value = box.getValue();
				value = value.replace("https://youtu.be/", "https://www.youtube.com/embed/");
				player.setUrl(value);
				item.setEditorValue(value);
//			}
		});
		
		String videoId = null;
		
		if (item.getEditorValue() == null || item.getEditorValue().length() == 0) {
			player.setUrl("https://www.youtube.com/embed/S1mirlD8ORw");
			box.setValue("https://www.youtube.com/embed/S1mirlD8ORw");
		}else {
			
			videoId = getVideoId();
			player.setUrl("https://www.youtube.com/embed/" + videoId);
			box.setValue("https://www.youtube.com/embed/" + videoId);

		}
		
		if (videoId == null) {
			videoId = "S1mirlD8ORw";
		}

		String playerId = IDUtil.uuid().toString();
		String divId = IDUtil.uuid().toString();

		String videoFormat = "<div id=\"youtube\" style=\"height:100%;\"><div id=\""+divId+"\"></div></div>​<script>window.ytPlayerList.push({ Id: '"+playerId+"', DivId: '"+divId+"', VideoId: '"+videoId
				+"', playerVars: {rel:0, playsinline:1,}"
				+ "});</script>\n";
		
		item.getInternalReferences().put("VIDEO_FORMAT", videoFormat);

		tgrPanel.add(player);
		tgrPanel.add(box);
			
		
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
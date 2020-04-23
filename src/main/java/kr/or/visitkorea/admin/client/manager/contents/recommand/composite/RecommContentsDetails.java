package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputClosingLine;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputCouponInfo;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputHtml;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputImage;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputMovie;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputNone;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputText;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputTravelInfo;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailItem;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsDetails extends AbtractRecommContents {

	private ContentTreeItem treeItem;
	private MaterialPanel contentPanel;
	private MaterialExtentsWindow window;
	private MaterialIcon saveIcon;
	private RecommContentsTree host;
	
	public RecommContentsDetails(MaterialExtentsWindow materialExtentsWindow, RecommContentsTree host) {
		super(materialExtentsWindow);
		this.window = materialExtentsWindow;
		this.host = host;
	}

	@Override
	public void init() {
		
		super.init();
		
		this.setTitle("컨텐츠 편집");
		this.setLayoutPosition(Position.RELATIVE);
		this.setOverflow(Overflow.HIDDEN);

		saveIcon = this.showSaveIconAndGetIcon();
		saveIcon.addClickHandler(event -> {
			MaterialLoader.loading(true);
			this.host.saveDB();
		});
		
		contentPanel = new MaterialPanel();
		contentPanel.setStyle("overflow-x:hidden;overflow-y:scroll;");
		contentPanel.setLayoutPosition(Position.ABSOLUTE);
		contentPanel.setTop(34);
		contentPanel.setLeft(0);
		contentPanel.setRight(0);
		contentPanel.setBottom(0);
//		contentPanel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		
		this.add(contentPanel);
	}
	
	public void render() {
		contentPanel.clear();
		
		MaterialPanel mPanel = new MaterialPanel();
		mPanel.setStyle("clear:both");
		mPanel.setPaddingLeft(10);
		mPanel.setPaddingRight(10);

		contentPanel.add(buildContent(mPanel, this.treeItem));
	}

	private MaterialPanel buildContent(MaterialPanel mPanel, ContentTreeItem treeItem) {

		List<MaterialTreeItem> children = treeItem.getTreeItems();

		String uniqueId = Document.get().createUniqueId();
		
		treeItem.setId(uniqueId);
		treeItem.setUniqueId(uniqueId);
		
		ContentDetailItem cdi = null;
		
		
		if (treeItem.getContentType() != null) {
			
			treeItem.getInternalReferences().put("WINDOW", getWindow());
			treeItem.getInternalReferences().put("DIALOG_ID", "REMOVE_ARTICLE_CONTENT");
			
			if (treeItem.getContentType().equals(DatabaseContentType.INPUT_SELECT)) {
				
				MaterialLabel label = new MaterialLabel(treeItem.getText());
				label.setTextAlign(TextAlign.LEFT);
				label.setFontSize("1.2em");
				label.setWidth("150px");
				label.setFontWeight(FontWeight.BOLD);
				label.setMarginBottom(10);
				
				mPanel.add(label);

				MaterialPanel checkContentPanel = new MaterialPanel();
				checkContentPanel.setDisplay(Display.INLINE_BLOCK);
				checkContentPanel.setPaddingLeft(30);
				checkContentPanel.setPaddingRight(30);
				checkContentPanel.setTextAlign(TextAlign.LEFT);
				
				String[] checks = treeItem.getParamters();
				for (String check : checks) {
					
					MaterialPanel checkPanel = new MaterialPanel();

					MaterialCheckBox checkBox = new MaterialCheckBox();
					checkBox.setText(check);
					
					checkPanel.add(checkBox);
					checkPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
					checkPanel.setMarginRight(30);
					
					checkContentPanel.add(checkPanel);
					
					
				}
				
				mPanel.add(checkContentPanel);

			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_IMAGE)) {
				cdi = new ContentDetailInputImage(treeItem, false, getCotId(), true, true,this);
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
				
			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_MOVIE)) {
				cdi = new ContentDetailInputMovie(treeItem, false, getCotId(), true, true);
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);

			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_TEXT)) {
				
				cdi = new ContentDetailInputText(treeItem, false, getCotId(), true, true);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
				
				MaterialTextBox box = new MaterialTextBox();
				box.setLabel(treeItem.getText());
				box.addChangeHandler(event->{
					treeItem.setEditorValue(box.getText());
				});
				
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);
				cdi.addContent(box);
				
			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_HTML)) {
				
				cdi = new ContentDetailInputHtml(treeItem, false, getCotId(), true, true);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
				
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);

			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_CLOSING_LINE)) {
				
				cdi = new ContentDetailInputClosingLine(treeItem, false, getCotId(), true, true);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
				
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);

			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_TRAVEL_INFO)) {

				cdi = new ContentDetailInputTravelInfo(treeItem, false, getCotId(), true, true, this.window);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
				
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);

			}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_COUPON_INFO)) {

				cdi = new ContentDetailInputCouponInfo(treeItem, false, getCotId(), true, true, this.window);
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
				
				ContentTreeItem cti = (ContentTreeItem)treeItem;
				cti.getInternalReferences().put("DETAIL_ITEM", cdi);

			} else if (treeItem.getContentType().equals(DatabaseContentType.NONE)) {
				
				if (treeItem.getText().equals("컨텐츠")) {
					cdi = null;
				} else if(treeItem.getText().equals("문단")) {
					cdi = new ContentDetailInputNone(treeItem, false, getCotId(), true, true);
					mPanel.add(cdi);
					mPanel.setTextAlign(TextAlign.LEFT);
					ContentTreeItem cti = (ContentTreeItem)treeItem;
				} else {
					cdi = new ContentDetailInputNone(treeItem, true, getCotId(), true, true);
					mPanel.add(cdi);
					mPanel.setTextAlign(TextAlign.LEFT);
					ContentTreeItem cti = (ContentTreeItem)treeItem;
				}

			}
			
		}else {
			
			Console.log("treeItem :: " + treeItem.getText() + " is null ~!");
		
		}

		for (Widget widget : children) {

			if (widget != null && widget instanceof ContentTreeItem) {
				ContentTreeItem child = (ContentTreeItem)widget;
				child.getInternalReferences().put("WINDOW", getWindow());
				if (cdi == null) {
					buildContent(mPanel, child);
				}else {
					buildContent(cdi, child);
				}
			}
			
		}
		
		return mPanel;
	}

	private void appendDetailItem(MaterialPanel mPanel, ContentDetailItem cdi) {
		
		mPanel.add(cdi);
		
	}	
	

	private SafeHtml getItemSafeHtml(ContentTreeItem ctItem) {
		
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		
		for(Widget item : ctItem.getChildrenList()) {
			
			if (item instanceof ContentTreeItem) {
				ContentTreeItem tgrItem = (ContentTreeItem)item;
				
				if (tgrItem.getChildren().size() > 1) {
				
					builder.append(getItemSafeHtml(tgrItem));
				
				}else{

					SafeHtml editorValue = tgrItem.getEditorSafeValue();
					if (editorValue == null) {
						builder.appendHtmlConstant(tgrItem.getEditorValue());
					}else {
						builder.append(editorValue);
					}
				}
			}
			
		}
		return builder.toSafeHtml();
	}

	public void setTarget(ContentTreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void reLoad() {
		render();
	}
	
	public void addContentPanel(MaterialPanel contentPanel) {
		contentPanel.add(contentPanel);
	}

	public MaterialExtentsWindow getWindow() {
		return window;
	}

	public void setWindow(MaterialExtentsWindow window) {
		this.window = window;
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public void loadData() {
		render();
		loading(false);
	}
	
	public RecommContentsTree getHost() {
		return host;
	}

}
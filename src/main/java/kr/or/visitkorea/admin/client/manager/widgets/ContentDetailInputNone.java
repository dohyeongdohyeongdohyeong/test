package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;

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
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputNone extends ContentDetailItem {

	public ContentDetailInputNone(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete,
			boolean ordering) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		init();
	}

	protected void init() {

		this.isRenderMode = true;

		if (isDisplayLabel) {

			this.title = new MaterialLabel("[ " + titleName + " ]");
			this.title.setTextAlign(TextAlign.CENTER);
			this.title.setFontSize("1.2em");
			this.title.setFontWeight(FontWeight.BOLD);

			this.add(this.title);
			this.setPaddingBottom(10);
			this.setBackgroundColor(Color.GREY_LIGHTEN_4);

			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");

			this.add(menuContent);

			addOrdering2(menuContent);

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
			dispContent.setHeight("40px");
			dispContent.setBorder("1px solid #e0e0e0");

			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");

			MaterialIcon iconDelete = new MaterialIcon(IconType.REMOVE);
			iconDelete.setVisible(true);
			iconDelete.setFloat(Style.Float.RIGHT);
			iconDelete.setMarginLeft(0);
			iconDelete.addClickHandler(event -> {

				Object windowObj = this.item.getInternalReferences().get("WINDOW");
				Object diagId = this.item.getInternalReferences().get("DIALOG_ID");
				Console.log((item.getInternalReferences()) + "");
				if (windowObj != null && diagId != null) {

					MaterialExtentsWindow win = (MaterialExtentsWindow) windowObj;
					String diagIdStr = (String) diagId;

					Map<String, Object> paramterMap = new HashMap<String, Object>();
					paramterMap.put("ITEM", this);
					paramterMap.put("CONTENT_TREE", this.item.getInternalReferences().get("CONTENT_TREE"));

					win.openDialog(diagIdStr, paramterMap, 800);
				}

			});

			menuContent.add(iconDelete);

			contentPreview.add(previewTitle);
			contentPreview.add(dispContent);
			contentPreview.add(menuContent);

			dispContent.setTextAlign(TextAlign.LEFT);

			addOrdering(menuContent);

			renderComponent(dispContent);

			this.content.add(this.contentPreview);

		}

		this.add(this.content);

	}

	protected void renderComponent(MaterialPanel dispContent) {

		this.isRenderMode = true;

		dispContent.clear();
		dispContent.setHeight("");

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
	}

	@Override
	protected void createComponent(MaterialPanel tgrPanel) {

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
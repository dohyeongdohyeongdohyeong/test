package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsDetails extends AbtractContents {

	private ContentTreeItem treeItem;
	private MaterialPanel panel;
	private MaterialPanel previewPanel;
	private MaterialLabel switchLabel;
	private JSONObject resultObject;

	public ContentsDetails(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		
		super.init();
		this.buildTitle();
		
		this.setTitle("컨텐츠 세부 정보");
		this.setLayoutPosition(Position.RELATIVE);
		
		this.panel = new MaterialPanel();
		this.panel.setId("edit.panel");
		this.panel.setLayoutPosition(Position.RELATIVE);
		this.panel.setOverflow(Overflow.AUTO);
		this.panel.setPaddingTop(30);
		this.panel.setPaddingRight(30);
		this.panel.setWidth("100%");
		this.panel.setHeight("516px");
		this.panel.setVisible(false);
		this.add(panel);
		
		this.previewPanel = new MaterialPanel();
		this.previewPanel.setLayoutPosition(Position.RELATIVE);
		this.previewPanel.setOverflow(Overflow.AUTO);
		this.previewPanel.setPaddingTop(30);
		this.previewPanel.setPaddingRight(30);
		this.previewPanel.setWidth("100%");
		this.previewPanel.setHeight("516px");
		this.previewPanel.setVisible(true);
		this.add(previewPanel);
		
		MaterialIcon ico = new MaterialIcon(IconType.EDIT);
		
		MaterialPanel iconPanel = new MaterialPanel();
		iconPanel.add(ico);
		
		this.switchLabel = new MaterialLabel("[ 입력 화면으로 ]");
		this.switchLabel.setLayoutPosition(Position.ABSOLUTE);
		this.switchLabel.setTop(5);
		this.switchLabel.setRight(20);
		this.switchLabel.setTextAlign(TextAlign.LEFT);
		this.switchLabel.setFontWeight(FontWeight.BOLD);
		this.switchLabel.setTextColor(Color.WHITE);
		this.switchLabel.setFontSize("1.1em");
		this.switchLabel.addClickHandler(event->{
			if (this.panel.isVisible()) {
				this.switchLabel.setText("[ 입력 화면으로 ]");
			}else {
				this.switchLabel.setText("[ 랜더링 화면으로 ]");
			}
			this.panel.setVisible(!this.panel.isVisible());
			this.previewPanel.setVisible(!this.previewPanel.isVisible());
		});
		this.add(this.switchLabel);
		
		
	}

	@Override
	public void clear() {
		this.previewPanel.clear();
		this.panel.clear();
	}
	
	@Override
	protected void onLoad() {
        super.onLoad();
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
		this.onLoad();
	}

	public void addContentPanel(MaterialPanel buildContent) {
		this.panel.add(buildContent);
	}

	public void setupResult(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public void goTop(double position) {
		this.panel.getElement().setScrollTop(0);
	}

	public void loadData() {
		
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

}
package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class MarketingImageListItem extends MaterialPanel {
	
	private Map<String, Object> map;
	private String itemId;
	private int orderIndex;
	private MarketingPanel marketingPanel;
	private MaterialLabel titleLabel;
	private MaterialLabel indexLabel;
	private MaterialIcon prevIcon;
	private MaterialIcon nextIcon;

	public MarketingImageListItem(MarketingPanel marketingPanel) {
		super();
		this.marketingPanel = marketingPanel;
		init();
	}

	public MarketingImageListItem(String... initialClass) {
		super(initialClass);
		init();
	}
	
	public void setOrderIndex(int index) {
		this.orderIndex = index;
		this.indexLabel.setText(""+orderIndex);
	}
	
	public void init() {
		
		prevIcon = new MaterialIcon();
		prevIcon.setLayoutPosition(Position.RELATIVE);
		prevIcon.setLeft(-60);
		prevIcon.setTop(35);
		prevIcon.setTextColor(Color.WHITE);
		prevIcon.setLineHeight(10);
		prevIcon.setIconType(IconType.ARROW_BACK);
		prevIcon.addClickHandler(event->{
			this.marketingPanel.back(this.orderIndex);
		});
		
		nextIcon = new MaterialIcon();
		nextIcon.setLayoutPosition(Position.RELATIVE);
		nextIcon.setLeft(60);
		nextIcon.setTop(35);
		nextIcon.setTextColor(Color.WHITE);
		nextIcon.setLineHeight(10); 
		nextIcon.setIconType(IconType.ARROW_FORWARD);
		nextIcon.addClickHandler(event->{
			this.marketingPanel.next(this.orderIndex);
		});
		
		titleLabel = new MaterialLabel(Document.get().createUniqueId());
		titleLabel.setLineHeight(30);
		titleLabel.setTextColor(Color.WHITE);
		titleLabel.setBackgroundColor(Color.BLACK);
		titleLabel.setOpacity(0.6);
		titleLabel.setLayoutPosition(Position.RELATIVE);
		titleLabel.setLeft(0);
		titleLabel.setTop(-204);
		titleLabel.setHeight("30px");
		
		indexLabel = new MaterialLabel("1");
		indexLabel.addStyleName("text_border");
		indexLabel.setLineHeight(30);
		indexLabel.setTextColor(Color.WHITE);
		indexLabel.setBackgroundColor(Color.BLACK);
		indexLabel.setOpacity(0.6);
		indexLabel.setLayoutPosition(Position.RELATIVE);
		indexLabel.setLeft(0);
		indexLabel.setTop(124);
		indexLabel.setHeight("30px");
		
		this.add(indexLabel);
		this.add(prevIcon);
		this.add(nextIcon);
		this.add(titleLabel);

	}
	
	public void setUrl(String url) {
		this.setStyle("background-size: cover; background-image: url("+url+");");
	}
	
	public void setData(Map<String, Object> map) {
		this.map = map;
		this.map.put("itemid", this.itemId);
	}
	
    @Override
    protected void onLoad() {
        super.onLoad();
        indexLabel.setText(this.orderIndex+"");
    }

	public int getOrderIndex() {
		return this.orderIndex;
	}

	public MaterialLabel getTitleLabel() {
		return titleLabel;
	}

	public void setNavButtons(boolean left, boolean right) {
		prevIcon.setVisible(left);
		nextIcon.setVisible(right);
	}

}

package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.VisitKorea;

public class ContentImageListItem extends MaterialPanel {
	
	private ContentDetailPanel detail;
	private MaterialImage img;
	private Map<String, Object> map;
	private String itemId;
	private int orderIndex;
	private ButtonOnPanelLeft badge1;
	private ButtonOnPanelRight badge2;
	private ButtonOnPanelLeftBottom badge3;
	private ButtonOnPanel center;
	private ContentCollection parentCollection;
	private VisitKorea contentType;
	private int visibleMode = 0;

	public ContentImageListItem() {
		super();
		this.visibleMode  = 1;
		init();
	}

	public ContentImageListItem(ContentCollection parentCollection, int orderIndex) {
		super();
		this.orderIndex = orderIndex;
		this.parentCollection = parentCollection;
		init();
	}
	

	public ContentImageListItem(String... initialClass) {
		super(initialClass);
		init();
	}
	
	public void setOrderIndex(int index) {
		this.orderIndex = index;
		this.center.setText(""+orderIndex);
		
		if (this.orderIndex == 1) {
			badge2.setVisibility(Visibility.HIDDEN);
		}else {
			badge2.setVisibility(Visibility.VISIBLE);
		}
		
		if (this.parentCollection.getWidgetCount() == this.orderIndex) {
			badge1.setVisibility(Visibility.HIDDEN);
		}else {
			badge1.setVisibility(Visibility.VISIBLE);
		}

	}
	
	public void init() {
		img = new MaterialImage();
		
		if (this.visibleMode == 1) {

			this.setStyle("background-size: cover; background-image: url("+img.getUrl()+");");
			MaterialIcon prevIcon = new MaterialIcon();
			prevIcon.setLayoutPosition(Position.RELATIVE);
			prevIcon.setLeft(-60);
			prevIcon.setTop(35);
			prevIcon.setTextColor(Color.WHITE);
			prevIcon.setLineHeight(10);
			prevIcon.setIconType(IconType.ARROW_BACK);
			
			MaterialIcon nextIcon = new MaterialIcon();
			nextIcon.setLayoutPosition(Position.RELATIVE);
			nextIcon.setLeft(60);
			nextIcon.setTop(35);
			nextIcon.setTextColor(Color.WHITE);
			nextIcon.setLineHeight(10);
			nextIcon.setIconType(IconType.ARROW_FORWARD);
			
			MaterialLabel titleLabel = new MaterialLabel("컨텐츠가 비었습니다.");
			titleLabel.setLineHeight(30);
			titleLabel.setTextColor(Color.WHITE);
			titleLabel.setBackgroundColor(Color.BLACK);
			titleLabel.setOpacity(0.6);
			titleLabel.setLayoutPosition(Position.RELATIVE);
			titleLabel.setLeft(0);
			titleLabel.setTop(-204);
			titleLabel.setHeight("30px");
			
			MaterialLabel indexLabel = new MaterialLabel("1");
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
			
		}else if (this.visibleMode == 0) {
			
			img.setMaxHeight("120px");
			this.add(img);
			this.setWidth("100%");
			this.setMaxHeight("120px");
			this.setHeight("120px");
			this.setLeft(0);
			this.setBorderRadius("3px");
			
			itemId = DOM.createUniqueId();
			
			MaterialIcon down = new MaterialIcon();
			down.setIconType(IconType.KEYBOARD_ARROW_DOWN);
			down.setLineHeight(10);
			badge1 = new ButtonOnPanelLeft();
			badge1.add(down);
			
			MaterialIcon up = new MaterialIcon();
			up.setIconType(IconType.KEYBOARD_ARROW_UP);
			up.setLineHeight(10);
			badge2 = new ButtonOnPanelRight();
			badge2.add(up);
			if (this.orderIndex == 1) {
				badge2.setVisibility(Visibility.HIDDEN);
			}else {
				badge2.setVisibility(Visibility.VISIBLE);
			}
			
			badge3 = new ButtonOnPanelLeftBottom();
			badge3.setVisibility(Visibility.VISIBLE);
			badge3.setBackgroundColor(Color.BLACK);
			badge3.setOpacity(0.6);
	
			center = new ButtonOnPanel();
			center.setText(""+orderIndex);
	
			this.add(center);
			this.add(badge3);
			this.add(badge1);
			this.add(badge2);
		}
		
	}
	
	public void setUrl(String url) {
		this.img.setUrl(url);
		this.setStyle("background-size: cover; background-image: url("+img.getUrl()+");");
	}
	
	public void setContentDetailPanel(ContentDetailPanel detailPanel) {
		this.detail = detailPanel;
	}
	
	
	public void setData(Map<String, Object> map) {
		this.map = map;
		this.map.put("itemid", this.itemId);
	}
	
    @Override
    protected void onLoad() {
    	
        super.onLoad();
		
        if (this.visibleMode == 0) {
        	
	        this.registerHandler(badge1.addClickHandler(event->{
		    	ContentImageListItem listItem1 = (ContentImageListItem) ((MaterialCollectionItem)parentCollection.getWidget(this.orderIndex-1)).getWidget(0);
		    	ContentImageListItem listItem2 = (ContentImageListItem) ((MaterialCollectionItem)parentCollection.getWidget(this.orderIndex)).getWidget(0);
		    	
		    	parentCollection.insert(parentCollection.getWidget(this.orderIndex), this.orderIndex-1);
		    	
		    	int index1 = listItem1.getOrderIndex();
		    	int index2 = listItem2.getOrderIndex();
		    	
		    	listItem1.setOrderIndex(index2);
		    	listItem2.setOrderIndex(index1);
		    	
	 		}));
	
	        this.registerHandler(badge1.addMouseOverHandler(event->{
	        	Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.POINTER);
	 		}));
	
	        this.registerHandler(badge1.addMouseOutHandler(event->{
	           	Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.DEFAULT);
	 		}));
		
		    	
		    this.registerHandler(badge2.addClickHandler(event->{
		    	
		    	ContentImageListItem listItem1 = (ContentImageListItem) ((MaterialCollectionItem)parentCollection.getWidget(this.orderIndex-1)).getWidget(0);
		    	ContentImageListItem listItem2 = (ContentImageListItem) ((MaterialCollectionItem)parentCollection.getWidget(this.orderIndex-2)).getWidget(0);
		    	
		    	parentCollection.insert(parentCollection.getWidget(this.orderIndex-1), this.orderIndex-2);
		    	
		    	int index1 = listItem1.getOrderIndex();
		    	int index2 = listItem2.getOrderIndex();
		    	
		    	listItem1.setOrderIndex(index2);
		    	listItem2.setOrderIndex(index1);
				
			}));
		
		    this.registerHandler(badge2.addMouseOverHandler(event->{
		    	Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.POINTER);
			}));
		
		    this.registerHandler(badge2.addMouseOutHandler(event->{
		       	Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.DEFAULT);
			}));
	
	        this.registerHandler(center.addClickHandler(event->{
				
				if (this.detail != null) {
					this.detail.setData(map);
					this.detail.setType(this.contentType);
				}
	
	 		}));
	
	        this.registerHandler(center.addMouseOverHandler(event->{
	        	Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.POINTER);
	 		}));
	
	        this.registerHandler(center.addMouseOutHandler(event->{
	           	Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.DEFAULT);
	            
	 		}));
        }
    }

	public int getOrderIndex() {
		return this.orderIndex;
	}

	public void notDisplayDown() {
		badge1.setVisibility(Visibility.HIDDEN);
	}

	public void setContentType(VisitKorea content) {
		this.contentType = content;
		if(content.equals(VisitKorea.CONTENT_TYPE_DEFAULT)) {
			badge3.setText("내부 컨텐츠");
		}else {
			badge3.setText("외부 링크");
		}
		
	}
    
}

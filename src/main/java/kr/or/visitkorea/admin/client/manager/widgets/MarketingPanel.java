package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.main.MainManagerApplication;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MarketingPanel extends AbstractContentPanel {

	private MaterialPanel controlArea;
	private List<MarketingImageListItem> listItems;
	private MaterialPanel contentContainer;
	private String dialogOpenId;
	private MarketingImageListItem selectedItem;
	private MaterialLabel headArea;
	
	
	public MarketingPanel(MaterialExtentsWindow materialExtentsWindow, String id) {
		super(materialExtentsWindow);
		this.dialogOpenId = id;
	}

	public void init() {
		this.listItems = new ArrayList<MarketingImageListItem>();
		
		this.setBorder("1px solid #e0e0e0");
		this.setHeight("230px");
		
		this.controlArea = new MaterialPanel();
		this.controlArea.setHeight("30px");
		
		setupControlArea();
		setupContentArea();
		setupBottomArea();
	}

	private void setupBottomArea() {

		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorderTop("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SEARCH);
		icon1.setLineHeight(26);
		icon1.setFontSize("1.0em");
		icon1.setBorderRight("1px solid #e0e0e0");
		icon1.setHeight("26px");
		icon1.setMargin(0);
		icon1.setWidth("26px");
		icon1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		icon1.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(MainManagerApplication.SELECT_CONTENT, 720);
		});
		
		MaterialIcon icon2 = new MaterialIcon(IconType.CLOUD_QUEUE);
		icon2.setLineHeight(26);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon2.setFontSize("1.0em");
		icon2.setBorderRight("1px solid #e0e0e0");
		icon2.setHeight("26px");
		icon2.setMargin(0);
		icon2.setWidth("26px");
		icon2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		icon2.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(MainManagerApplication.CREATE_URL_LINK, 720);
		});
		
		panelBottom.add(icon1);
		panelBottom.add(icon2);
		
		this.add(panelBottom);
		
	}

	private void setupControlArea() {

		
		MaterialPanel checkContainer = new MaterialPanel();
		MaterialCheckBox checkBox = new MaterialCheckBox();
		checkBox.setText("노출 여부");
		checkContainer.add(checkBox);
		checkContainer.setMargin(4);
		checkContainer.setMarginRight(8);
		checkContainer.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);

		headArea = new MaterialLabel("영역1");
		headArea.setFontWeight(FontWeight.BOLD);
		headArea.setMargin(4);
		headArea.setMarginLeft(8);
		headArea.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		this.add(headArea);
		this.add(checkContainer);
		
	}

	private void setupContentArea() {
		contentContainer = new MaterialPanel();
		contentContainer.setOverflow(Overflow.HIDDEN);
		contentContainer.setVerticalAlign(VerticalAlign.MIDDLE);
		contentContainer.setMarginTop(30);
		contentContainer.setWidth("100%");
		contentContainer.setHeight("167px");
		contentContainer.setBorderTop("1px solid #e0e0e0");
	
		addItem(163, "23.3%", "163px", false, true, 
				"https://img-wishbeen.akamaized.net/plan/1463029230608__%EC%8A%AC%EB%A1%9C%EB%B2%A0%EB%8B%88%EC%95%8403.jpg"); 
		addItem(163, "23.3%", "163px", true, true, 
				"https://img-wishbeen.akamaized.net/plan/1463029230608__%EC%8A%AC%EB%A1%9C%EB%B2%A0%EB%8B%88%EC%95%8403.jpg"); 
		addItem(163, "23.3%", "163px", true, true, 
				"https://img-wishbeen.akamaized.net/plan/1463029230608__%EC%8A%AC%EB%A1%9C%EB%B2%A0%EB%8B%88%EC%95%8403.jpg"); 
		addItem(163, "23.3%", "163px", true, false, 
				"https://img-wishbeen.akamaized.net/plan/1463029230608__%EC%8A%AC%EB%A1%9C%EB%B2%A0%EB%8B%88%EC%95%8403.jpg"); 
		render();
		
		this.add(contentContainer);

	}
	
	public void render() {

		contentContainer.clear();
		int i=1;
		for (MarketingImageListItem item : this.listItems) {
			item.setNavButtons(true, true);
			contentContainer.add(getGap());
			contentContainer.add(item);
			item.setOrderIndex(i);
			i++;
		}

		this.listItems.get(0).setNavButtons(false, true);
		this.listItems.get(this.listItems.size()-1).setNavButtons(true, false);
		
		contentContainer.add(getGap());
	}

	private MaterialPanel getGap() {
		MaterialPanel gap1 = new MaterialPanel();
		gap1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		gap1.setWidth("10px");
		gap1.setHeight("167px");
		gap1.setPaddingTop(10);
		gap1.setPaddingBottom(10);
		return gap1;
	}
	
	private void addItem(int lineHeight, String width, String maxHeight, boolean leftDisplay, boolean rightDisplay, String url) {

		MarketingImageListItem retItem = new MarketingImageListItem(this);
		retItem.setUrl(url);
		retItem.setWidth(width);
		retItem.setHeight(lineHeight+"px");
		retItem.setPaddingTop(10);
		retItem.setPaddingBottom(10);
		retItem.setVerticalAlign(VerticalAlign.MIDDLE);
		retItem.setDisplay(Display.BLOCK);
		retItem.setLineHeight(lineHeight);
		retItem.setMaxHeight(maxHeight);
		retItem.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		retItem.setData(new HashMap<String, Object>());
		
		retItem.addClickHandler(event->{

			if (this.selectedItem != null && !this.selectedItem.equals(retItem))
				this.selectedItem.getTitleLabel().setTextColor(Color.WHITE);
			this.selectedItem = retItem;
			this.selectedItem.getTitleLabel().setTextColor(Color.BLUE);
		});

		retItem.addMouseOverHandler(event->{
			if (this.selectedItem == null || !this.selectedItem.equals(event.getSource()))
				retItem.getTitleLabel().setTextColor(Color.RED);
		});
		

		retItem.addMouseOutHandler(event->{
			if (this.selectedItem == null || !this.selectedItem.equals(event.getSource()))
				retItem.getTitleLabel().setTextColor(Color.WHITE);
		});

		
		this.listItems.add(retItem);
	}

	public void back(int orderIndex) {
		if (orderIndex > 1) {
			int prevIndex = orderIndex -1;
			MarketingImageListItem orderIndexItem = this.listItems.get(orderIndex-1);
			MarketingImageListItem prevIndexItem = this.listItems.get(prevIndex-1);
			this.listItems.set(prevIndex-1, orderIndexItem);
			this.listItems.set(orderIndex-1, prevIndexItem);
			
			orderIndexItem.setOrderIndex(prevIndex-1);
			prevIndexItem.setOrderIndex(orderIndex-1);
			
			render();
		}
	}

	public void next(int orderIndex) {
		if (orderIndex < 4) {
			int prevIndex = orderIndex +1;
			MarketingImageListItem orderIndexItem = this.listItems.get(orderIndex-1);
			MarketingImageListItem prevIndexItem = this.listItems.get(prevIndex-1);
			this.listItems.set(prevIndex-1, orderIndexItem);
			this.listItems.set(orderIndex-1, prevIndexItem);
			
			orderIndexItem.setOrderIndex(prevIndex-1);
			prevIndexItem.setOrderIndex(orderIndex-1);
			
			render();
		}
	}

	public void setText(String title) {
		this.headArea.setText(title);
	}

}

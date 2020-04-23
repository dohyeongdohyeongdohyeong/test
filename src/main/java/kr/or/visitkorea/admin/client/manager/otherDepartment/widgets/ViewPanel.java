package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;

public class ViewPanel extends MaterialPanel {

	private MaterialLabel areaName;
	private MaterialLabel countLabel;
	private ContentLayoutPanel selectedItem;
	private int selcetedItemIndex;
	private Map<String, Object> internalCollection = new HashMap<String, Object>();
	private MaterialImage bgImage;

	public ViewPanel() {
		super();
		init();
	}

	public ViewPanel(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		
		bgImage = new MaterialImage();
		bgImage.setLayoutPosition(Position.ABSOLUTE);
		bgImage.setLeft(0);
		bgImage.setTop(0);
		bgImage.setWidth("100%");
		bgImage.setHeight("100%");
		bgImage.setVisible(false);
		this.add(bgImage);
		
		areaName = new MaterialLabel();
		areaName.setLayoutPosition(Position.ABSOLUTE);
		areaName.setTop(5);
		areaName.setLeft(5);
		areaName.setRight(5);
		areaName.setBottom(5);
		areaName.setTextAlign(TextAlign.CENTER);
		areaName.setFontWeight(FontWeight.BOLDER);
		areaName.setTextColor(Color.GREY_LIGHTEN_1);
		areaName.setFontSize("1.2em");
		this.add(areaName);

		countLabel = new MaterialLabel();
		countLabel.setLayoutPosition(Position.ABSOLUTE);
		countLabel.setRight(5);
		countLabel.setBottom(5);
		countLabel.setTextAlign(TextAlign.CENTER);
		countLabel.setFontWeight(FontWeight.NORMAL);
		countLabel.setTextColor(Color.GREY_LIGHTEN_1);
		countLabel.setFontSize("1.0em");
		this.add(countLabel);

	}
	
	public void setVisibleBackgroundImage(boolean visible) {
		this.bgImage.setVisible(visible);
	}
	
	public void setBackgroundImageUrl(String url) {
		this.bgImage.setUrl(url);
	}
	
	public MaterialLabel getAreaNameLabel() {
		return this.areaName;
	}
	
	public void setData(String key, Object value) {
		this.internalCollection.put(key, value);
	}
	
	public Object getData(String key) {
		return this.internalCollection.get(key);
	}
	
	public void setAreaName(String areaNameString) {
		this.areaName.setText(areaNameString);
		if (this.areaName.getText().equals("태그 영역")) {
			countLabel.setText("");
		}
	}
	
	public void setComponentCount(int count) {
		this.countLabel.setText("총 " + count + " 개");
	}

	public void setSelectedItem(ContentLayoutPanel item) {
		this.selectedItem = item;
	}

	public ContentLayoutPanel getSelectedItem() {
		return this.selectedItem;
	}

	public void setSelectedItemIndex(int index) {
		this.selcetedItemIndex = index;
	}

	public int getSelectedItemIndex() {
		return this.selcetedItemIndex;
	}

	public String getArea() {
		return this.getElement().getAttribute("area");
	}

	public Map<String, Object> getData() {
		return this.internalCollection;
	}

}

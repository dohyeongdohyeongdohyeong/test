package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.CheckBoxType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentSection;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;
 
public class SiteseeCurationContentLayout extends ContentLayoutPanel {

	private MaterialCheckBox selectedCheck;
	private List<CurationItem> items;
	private MaterialWidget mainContent;
	private MaterialInput mainTitle;
	private MaterialInput subTitle;
	
	@Override
	protected void init() {
		
		items = new ArrayList<CurationItem>();
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setWidth("485px");
		this.setHeight("270px");
		this.setPadding(20);
		this.setMargin(10);
		this.setBorder("2px solid #e6ac00");
		
		selectedCheck = new MaterialCheckBox();
		selectedCheck.setType(CheckBoxType.FILLED);
		selectedCheck.addValueChangeHandler(event->{
			selectedCheckValueEvent(event);
		});
		
		mainTitle = new MaterialInput(InputType.TEXT);
		mainTitle.setLayoutPosition(Position.ABSOLUTE);
		mainTitle.setTop(0);
		mainTitle.setLeft(10);
		mainTitle.setFontSize("1.3em");
		mainTitle.setFontWeight(FontWeight.BOLDER);
		mainTitle.setWidth("278px");
		mainTitle.setHeight("40px");
		this.add(mainTitle);
		
		subTitle = new MaterialInput(InputType.TEXT);
		subTitle.setLayoutPosition(Position.ABSOLUTE);
		subTitle.setTop(40);
		subTitle.setLeft(10);
		subTitle.setWidth("278px");
		subTitle.setHeight("30px");
		this.add(subTitle);
		
		MaterialPanel checkPanel = new MaterialPanel();
		checkPanel.add(selectedCheck);
		
		checkPanel.setLayoutPosition(Position.ABSOLUTE);
		checkPanel.setTop(10);
		checkPanel.setRight(0);
		checkPanel.setWidth("30px");
		checkPanel.setHeight("30px");
		this.add(checkPanel);

		MaterialLabel topLine = new MaterialLabel();
		topLine.setHeight("2px");
		topLine.setBackgroundColor(Color.GREY_LIGHTEN_2);
		topLine.setLayoutPosition(Position.ABSOLUTE);
		topLine.setTop(80);
		topLine.setLeft(10);
		topLine.setRight(10);
		this.add(topLine);
		
		MaterialLabel bottomLine = new MaterialLabel();
		bottomLine.setHeight("2px");
		bottomLine.setBackgroundColor(Color.GREY_LIGHTEN_2);
		bottomLine.setLayoutPosition(Position.ABSOLUTE);
		bottomLine.setBottom(28);
		bottomLine.setLeft(10);
		bottomLine.setRight(10);
		this.add(bottomLine);
		
		MaterialLink addLink = new MaterialLink();
		addLink.setIconType(IconType.ADD);
		addLink.setLayoutPosition(Position.ABSOLUTE);
		addLink.setTop(50);
		addLink.setRight(0);
		addLink.addClickHandler(event->{
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("OTD_ID", this.extentsWindow.getValueMap().get("OTD_ID"));
			map.put("AREA_COMPONENT", this);
			map.put("SECTION", ContentSection.SIGHT);
			this.extentsWindow.openDialog(OtherDepartmentMainApplication.SELECT_CONTENT, map, 640);

		});
		this.add(addLink);
		
		MaterialLink outLink = new MaterialLink();
		outLink.setIconType(IconType.CLOUD);
		outLink.setLayoutPosition(Position.ABSOLUTE);
		outLink.setTop(50);
		outLink.setRight(30);
		outLink.addClickHandler(event->{
			
			CurationItem item = new CurationItem(null, "[공지]", "", "신규 제목", "#000000", "");
			items.add(item);
			renderItem();
			
		});
		this.add(outLink);
		
		MaterialLink removeLink = new MaterialLink();
		removeLink.setIconType(IconType.REMOVE);
		removeLink.setLayoutPosition(Position.ABSOLUTE);
		removeLink.setBottom(2);
		removeLink.setLeft(10);
		removeLink.addClickHandler(event->{
			
			for (int i=0; i<items.size(); i++) {
				if (items.get(i).isSelected()) {
					items.remove(i);
					break;
				}
			}
			
			renderItem();
			
		});
		this.add(removeLink);
		
		mainContent =new MaterialPanel();
		mainContent.setLayoutPosition(Position.ABSOLUTE);
		mainContent.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		mainContent.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		mainContent.setTop(83);
		mainContent.setLeft(10);
		mainContent.setRight(10);
		mainContent.setBottom(32);
		this.add(mainContent);
		
	}
	
	private void renderItem() {
		
		int col01Position = 10;
		int col02Position = 230;
		int itemWidth = 210;
		int rowCount = 0;

		mainContent.clear();
		for (int i=0; i<items.size(); i+=2) {
			buildCurationItem(col01Position, itemWidth, rowCount, i);
			buildCurationItem(col02Position, itemWidth, rowCount, i+1);
			rowCount++;
		}
	}

	private void buildCurationItem(int col01Position, int itemWidth, int rowCount, int i) {
		if (items.size() > i) {
			CurationItem tgrItem = items.get(i);
			if (tgrItem !=null) {
				tgrItem.setLayoutPosition(Position.ABSOLUTE);
				tgrItem.setWidth(itemWidth + "px");
				tgrItem.setTop( 14 + (rowCount * 35));
				tgrItem.setLeft(col01Position);
				mainContent.add(tgrItem);
				
				tgrItem.addClickHandler(event->{
					
					for (CurationItem ci : items) {
						if (ci.isSelected()) {
							ci.setSelected(false);
						}
					}
				
					
					CurationItem eventItem = (CurationItem) event.getSource();
					eventItem.setSelected(true);
					eventItem.setBackgroundColor(Color.GREY_LIGHTEN_1);
	
				});
				
				tgrItem.addDoubleClickHandler(event->{
					
					Map<String, Object> diagValueMap = new HashMap<String, Object>();
					diagValueMap.put("ITEM", event.getSource());
					extentsWindow.openDialog(OtherDepartmentMainApplication.CURATION_HEADER, diagValueMap, 500);
					
				});
			}
		}
	}

	private void selectedCheckValueEvent(ValueChangeEvent<Boolean> event) {
		
		ViewPanel pWidget = (ViewPanel)this.getParent();
		int index = pWidget.getChildrenList().indexOf(this);
		pWidget.setSelectedItem(this);
		pWidget.setSelectedItemIndex(index);

		for (Widget widget : pWidget.getChildrenList()) {
			
			if (!widget.equals(this)) {
				ContentLayoutPanel clp = (ContentLayoutPanel)widget;
				clp.setSelected(false);
			}
		}

	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow) {
		super.setWindow(materialExtentsWindow);
	}

	@Override
	public void setSelected(boolean flag) {
		this.selectedCheck.setValue(flag);
	}

	@Override
	public void loadData() {
		
		if (this.tgrCompObj != null) {
			items = new ArrayList<CurationItem>();
			
			String mainTitleString = this.tgrCompObj.get("TITLE").isString().stringValue();
			String subTitleString = this.tgrCompObj.get("SUB_TITLE").isString().stringValue();
			
			this.mainTitle.setText(mainTitleString);
			this.subTitle.setText(subTitleString);
			
			JSONArray contentArray = this.tgrCompObj.get("CONTENTS").isArray();
			int arrLength = contentArray.size();
			for (int i=0; i<arrLength; i++) {
				CurationItem item = new CurationItem(contentArray.get(i).isObject());
				items.add(item);
			}
			
			renderItem();
		}
		
	}

	@Override
	public JSONObject getJSONObject() {
		
		JSONObject retJSONObject = this.tgrCompObj;
		
		if (retJSONObject == null) {
			
			retJSONObject = new JSONObject();
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_ID", new JSONString(IDUtil.uuid()));
			retJSONObject.put("MAIN_TYPE", new JSONNumber(0));
			retJSONObject.put("COMP_TYPE", new JSONString("curation"));
			
		}else {
			
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_TYPE", new JSONString("curation"));
		}
		
		JSONArray contentArray = new JSONArray();
		int idx=0;
		for (CurationItem item : items) {
			contentArray.set(idx, item.getJSONObject());
			idx++;
		}
		
		retJSONObject.put("CONTENTS", contentArray);
		
		return retJSONObject;
		
	}

	public void addData(JSONObject rsObj) {
		
		String TITLE = "";
		String COT_ID = null;

		if (rsObj.get("TITLE") != null) TITLE = rsObj.get("TITLE").isString().stringValue();
		if (rsObj.get("COT_ID") != null) COT_ID = rsObj.get("COT_ID").isString().stringValue();
		
		CurationItem item = new CurationItem(COT_ID, "", "", TITLE, "#000000", "");
		items.add(item);
		
		renderItem();

	}

}

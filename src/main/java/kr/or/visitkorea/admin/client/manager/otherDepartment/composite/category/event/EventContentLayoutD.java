package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.CheckBoxType;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.CategoryMainContentPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentSection;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentLayoutD extends ContentLayoutPanel {
	private MaterialCheckBox selectedCheck;
	private MaterialInput mainTitle;
	private MaterialInput subTitle;	
	private List<CategoryMainContentPanel> bottomPanelList;

	@Override
	protected void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setDisplay(Display.BLOCK);
		this.setWidth("485px");
		this.setHeight("270px");
		this.setPadding(20);
		this.setMargin(10);
		this.setBorder("2px solid #e6ac00");
		
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
		
		selectedCheck = new MaterialCheckBox();
		selectedCheck.setType(CheckBoxType.FILLED);
		selectedCheck.addValueChangeHandler(event->{
			selectedCheckValueEvent(event);
		});
		
		MaterialPanel checkPanel = new MaterialPanel();
		checkPanel.add(selectedCheck);
		checkPanel.setLayoutPosition(Position.ABSOLUTE);
		checkPanel.setTop(30);
		checkPanel.setRight(10);
		checkPanel.setWidth("30px");
		checkPanel.setHeight("30px");
		this.add(checkPanel);
		
		bottomPanelList = new ArrayList<CategoryMainContentPanel>();
		for (int i = 0; i < 10; i++) {
			int width = 85;
			int height = 85;
			int top = i >= 5 ? 162 : 70;
			int left = i >= 5 ? (i - 5) * 94 : i * 94;
			CategoryMainContentPanel panel = this.addCategoryMainContentPanel(this, width, height, top, left);
			this.bottomPanelList.add(panel);
		}
	}
	
	private CategoryMainContentPanel addCategoryMainContentPanel(MaterialWidget parent, int width, int height, int top, int left) {
		CategoryMainContentPanel panel = new CategoryMainContentPanel(ContentSection.EVENT);
		panel.setMargin(10);
		panel.setLayoutPosition(Position.ABSOLUTE);
		panel.setBorder("1px solid #aaaaaa");
		panel.setTop(top);
		panel.setLeft(left);
		panel.setWidth(width + "px");
		panel.setHeight(height + "px");
		parent.add(panel);
		return panel;
	}

	private void selectedCheckValueEvent(ValueChangeEvent<Boolean> event) {
		ViewPanel pWidget = (ViewPanel) this.getParent();
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
		bottomPanelList.forEach(item -> item.setWindow(materialExtentsWindow));
	}

	@Override
	public void setSelected(boolean flag) {
		this.selectedCheck.setValue(flag);
	}

	@Override
	public void loadData() {
		if (this.tgrCompObj != null) {
			this.mainTitle.setText(this.tgrCompObj.get("TITLE").isString().stringValue());
			this.subTitle.setText(this.tgrCompObj.get("SUB_TITLE").isString().stringValue());
			
			JSONArray contentArray = this.tgrCompObj.get("CONTENTS").isArray();
			for (int i = 0; i < 10; i++) {
				if (contentArray.get(i) != null) {
					JSONObject Obj = contentArray.get(i).isObject();
					this.bottomPanelList.get(i).setData(Obj);
				}
			}
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
			retJSONObject.put("MAIN_TYPE", new JSONNumber(15));
			retJSONObject.put("COMP_TYPE", new JSONString("9"));
		} else {
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_TYPE", new JSONString("9"));
		}
		
		JSONArray contentArray = new JSONArray();
		for (int i = 0; i < 10; i++) {
			contentArray.set(i, this.bottomPanelList.get(i).getJSONObject());
		}
		
		retJSONObject.put("CONTENTS", contentArray);
		
		return retJSONObject;
	}

}

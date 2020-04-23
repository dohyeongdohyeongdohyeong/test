package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.festival;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.CheckBoxType;
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

public class FestivalContentLayoutB extends ContentLayoutPanel {

	private CategoryMainContentPanel mainPannel;
	private CategoryMainContentPanel bottomPannel01;
	private CategoryMainContentPanel bottomPannel02;
	private CategoryMainContentPanel bottomPannel03;
	private MaterialCheckBox selectedCheck;
	private MaterialInput mainTitle;
	private MaterialInput subTitle;
	private int mainType;
	private ContentSection contentSection;

	public FestivalContentLayoutB(ContentSection contentSection) {
		this.contentSection = contentSection;
		layoutInit();
	}

	protected void layoutInit() {
		
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setWidth("485px");
		this.setHeight("320px");
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
		
		if (this.contentSection != null) {
			mainPannel = new CategoryMainContentPanel(this.contentSection);
		} else {
			mainPannel = new CategoryMainContentPanel(ContentSection.ARTICLE);
		}
		
		mainPannel.setMargin(10);
		mainPannel.setLayoutPosition(Position.ABSOLUTE);
		mainPannel.setBorder("1px solid #aaaaaa");
		mainPannel.setBottom(90);
		mainPannel.setLeft(0);
		mainPannel.setWidth("460px");
		mainPannel.setHeight("136px");
		this.add(mainPannel);
		
		if (this.contentSection != null) {
			bottomPannel01 = new CategoryMainContentPanel(this.contentSection);
		} else {
			bottomPannel01 = new CategoryMainContentPanel(ContentSection.ARTICLE);
		}
		
		bottomPannel01.setMargin(10);
		bottomPannel01.setLayoutPosition(Position.ABSOLUTE);
		bottomPannel01.setBorder("1px solid #aaaaaa");
		bottomPannel01.setLeft(0);
		bottomPannel01.setBottom(0);
		bottomPannel01.setWidth("145px");
		bottomPannel01.setHeight("80px");
		this.add(bottomPannel01);

		
		if (this.contentSection != null) {
			bottomPannel02 = new CategoryMainContentPanel(this.contentSection);
		} else {
			bottomPannel02 = new CategoryMainContentPanel(ContentSection.ARTICLE);
		}
		bottomPannel02.setMargin(10);
		bottomPannel02.setLayoutPosition(Position.ABSOLUTE);
		bottomPannel02.setBorder("1px solid #aaaaaa");
		bottomPannel02.setLeft(157);
		bottomPannel02.setBottom(0);
		bottomPannel02.setWidth("145px");
		bottomPannel02.setHeight("80px");
		this.add(bottomPannel02);

		if (this.contentSection != null) {
			bottomPannel03 = new CategoryMainContentPanel(this.contentSection);
		} else {
			bottomPannel03 = new CategoryMainContentPanel(ContentSection.ARTICLE);
		}
		
		bottomPannel03.setMargin(10);
		bottomPannel03.setLayoutPosition(Position.ABSOLUTE);
		bottomPannel03.setBorder("1px solid #aaaaaa");
		bottomPannel03.setLeft(314);
		bottomPannel03.setBottom(0);
		bottomPannel03.setWidth("145px");
		bottomPannel03.setHeight("80px");
		this.add(bottomPannel03);
	}
	
	private void selectedCheckValueEvent(ValueChangeEvent<Boolean> event) {
		
		if (this.getParent() instanceof ViewPanel) {
			
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
			
		}else if (this.getParent() instanceof MaterialPanel) {
			
			MaterialPanel pWidget = (MaterialPanel)this.getParent();
			int index = pWidget.getChildrenList().indexOf(this);
//			pWidget.setSelectedItem(this);
//			pWidget.setSelectedItemIndex(index);
	
			for (Widget widget : pWidget.getChildrenList()) {
				
				if (!widget.equals(this)) {
					ContentLayoutPanel clp = (ContentLayoutPanel)widget;
					clp.setSelected(false);
				}
			}
			
		}

	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow) {
		super.setWindow(materialExtentsWindow);
		mainPannel.setWindow(materialExtentsWindow);
		bottomPannel01.setWindow(materialExtentsWindow);
		bottomPannel02.setWindow(materialExtentsWindow);
		bottomPannel03.setWindow(materialExtentsWindow);
	}

	@Override
	public void setSelected(boolean flag) {
		this.selectedCheck.setValue(flag);
	}

	@Override
	public void loadData() {
		
		if (this.tgrCompObj != null) {
			
			String mainTitleString = this.tgrCompObj.get("TITLE").isString().stringValue();
			String subTitleString = this.tgrCompObj.get("SUB_TITLE").isString().stringValue();
			this.mainTitle.setText(mainTitleString);
			this.subTitle.setText(subTitleString);
			JSONArray contentArray = this.tgrCompObj.get("CONTENTS").isArray();
			
			if (contentArray.get(0) != null) {
				JSONObject Obj_00 = contentArray.get(0).isObject();
				mainPannel.setData(Obj_00);
			}
			
			if (contentArray.get(1) != null) {
				JSONObject Obj_01 = contentArray.get(1).isObject();
				bottomPannel01.setData(Obj_01);
			}
			
			if (contentArray.get(2) != null) {
				JSONObject Obj_02 = contentArray.get(2).isObject();
				bottomPannel02.setData(Obj_02);
			}
			
			if (contentArray.get(3) != null) {
				JSONObject Obj_03 = contentArray.get(3).isObject();
				bottomPannel03.setData(Obj_03);
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
			retJSONObject.put("MAIN_TYPE", new JSONNumber(mainType));
			retJSONObject.put("COMP_TYPE", new JSONString("4"));
			
		}else {
			
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_TYPE", new JSONString("4"));
		}
		
		JSONArray contentArray = new JSONArray();

		contentArray.set(0, mainPannel.getJSONObject());
		contentArray.set(1, bottomPannel01.getJSONObject());
		contentArray.set(2, bottomPannel02.getJSONObject());
		contentArray.set(3, bottomPannel03.getJSONObject());
		
		retJSONObject.put("CONTENTS", contentArray);
		
		return retJSONObject;
	}
	
	public void setMainType(int mainType) {
		this.mainType = mainType;
	}

	@Override
	protected void init() {
		
	}
}

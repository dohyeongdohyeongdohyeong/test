package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.recommand;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

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
 
public class RecommandContentLayoutA extends ContentLayoutPanel {
 
	private CategoryMainContentPanel mainPannel;
	private CategoryMainContentPanel leftTopPannel;
	private CategoryMainContentPanel leftBottomPannel;
	private MaterialCheckBox selectedCheck;
	private MaterialInput mainTitle;
	private MaterialInput subTitle;
	
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
		
		mainPannel = new CategoryMainContentPanel(ContentSection.ARTICLE);
		mainPannel.setMargin(10);
		mainPannel.setLayoutPosition(Position.ABSOLUTE);
		mainPannel.setBorder("1px solid #aaaaaa");
		mainPannel.setTop(70);
		mainPannel.setLeft(0);
		mainPannel.setRight(115);
		mainPannel.setHeight("176px");
		this.add(mainPannel);
		
		leftTopPannel = new CategoryMainContentPanel(ContentSection.ARTICLE);
		leftTopPannel.setMargin(10);
		leftTopPannel.setLayoutPosition(Position.ABSOLUTE);
		leftTopPannel.setBorder("1px solid #aaaaaa");
		leftTopPannel.setTop(70);
		leftTopPannel.setRight(0);
		leftTopPannel.setWidth("105px");
		leftTopPannel.setHeight("85px");
		this.add(leftTopPannel);

		leftBottomPannel = new CategoryMainContentPanel(ContentSection.ARTICLE);
		leftBottomPannel.setMargin(10);
		leftBottomPannel.setLayoutPosition(Position.ABSOLUTE);
		leftBottomPannel.setBorder("1px solid #aaaaaa");
		leftBottomPannel.setRight(0);
		leftBottomPannel.setBottom(0);
		leftBottomPannel.setWidth("105px");
		leftBottomPannel.setHeight("85px");
		this.add(leftBottomPannel);
	
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
		mainPannel.setWindow(materialExtentsWindow);
		leftTopPannel.setWindow(materialExtentsWindow);
		leftBottomPannel.setWindow(materialExtentsWindow);
		
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
			
			if (contentArray.get(0) != null) {
				JSONObject Obj_00 = contentArray.get(0).isObject();
				mainPannel.setData(Obj_00);
			}
			if (contentArray.get(1) != null) {
				JSONObject Obj_01 = contentArray.get(1).isObject();
				leftTopPannel.setData(Obj_01);
			}
			if (contentArray.get(2) != null) {
				JSONObject Obj_02 = contentArray.get(2).isObject();
				leftBottomPannel.setData(Obj_02);
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
			retJSONObject.put("MAIN_TYPE", new JSONNumber(0));
			retJSONObject.put("COMP_TYPE", new JSONString("3"));
			
		}else {
			
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_TYPE", new JSONString("3"));
		}
		
		JSONArray contentArray = new JSONArray();
		
		contentArray.set(0, mainPannel.getJSONObject());
		contentArray.set(1, leftTopPannel.getJSONObject());
		contentArray.set(2, leftBottomPannel.getJSONObject());
		
		retJSONObject.put("CONTENTS", contentArray);
		
		
		return retJSONObject;
	}

}

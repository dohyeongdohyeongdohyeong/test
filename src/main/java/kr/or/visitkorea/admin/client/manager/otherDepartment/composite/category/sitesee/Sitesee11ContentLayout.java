package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee;

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
 
public class Sitesee11ContentLayout extends ContentLayoutPanel {

	private MaterialCheckBox selectedCheck;
	private List<CategoryMainContentPanel> panelList;
	private MaterialInput mainTitle;
	private MaterialInput subTitle;
	
	@Override
	protected void init() {
		
		panelList = new ArrayList<CategoryMainContentPanel>();

		this.setLayoutPosition(Position.RELATIVE);
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
		
		redraw();
	
	}

	private void redraw() {
		
		for (int row=0; row<2; row++) {
			
			for (int col=0; col<6; col++) {
				
				if (!(row == 1 && col == 6)) {
					CategoryMainContentPanel mainPannel = new CategoryMainContentPanel(ContentSection.SIGHT);
					mainPannel.setMargin(10);
					mainPannel.setLayoutPosition(Position.ABSOLUTE);
					mainPannel.setBorder("1px solid #aaaaaa");
					mainPannel.setTop(80 + (row*85));
					mainPannel.setLeft(8 + (col*75));
					mainPannel.setRight(115);
					mainPannel.setWidth("71.1666px");
					mainPannel.setHeight("80px");
					panelList.add(mainPannel);
				}
			}
		}

		for (CategoryMainContentPanel pan : panelList) {
			this.add(pan);
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
		for (CategoryMainContentPanel mainPannel : panelList) {
			mainPannel.setWindow(this.extentsWindow);
		}
	}

	@Override
	public void setSelected(boolean flag) {
		this.selectedCheck.setValue(flag);
	}

	public JSONObject getJSONObject() {
		
		JSONObject retJSONObject = this.tgrCompObj;
		
		if (retJSONObject == null) {
			
			retJSONObject = new JSONObject();
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_ID", new JSONString(IDUtil.uuid()));
			retJSONObject.put("MAIN_TYPE", new JSONNumber(0));
			retJSONObject.put("COMP_TYPE", new JSONString("11content"));
			
		}else {
			
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_TYPE", new JSONString("11content"));
		}
		
		JSONArray contentArray = new JSONArray();
		int idx=0;
		for (CategoryMainContentPanel cPanel :  this.panelList) {
			contentArray.set(idx, cPanel.getJSONObject());
			idx++;
			
		}
		
		retJSONObject.put("CONTENTS", contentArray);
		
		return retJSONObject;
		
	}
	
	@Override
	public void loadData() {
		if (this.tgrCompObj != null) {
			String mainTitleString = this.tgrCompObj.get("TITLE").isString().stringValue();
			String subTitleString = this.tgrCompObj.get("SUB_TITLE").isString().stringValue();
			
			this.mainTitle.setText(mainTitleString);
			this.subTitle.setText(subTitleString);
			
			this.tgrCompObj.get("CONTENTS").isArray();
			
			JSONArray contentArray = this.tgrCompObj.get("CONTENTS").isArray();
			int arrLength = contentArray.size();
			if (arrLength > 12) arrLength = 12;
			for (int i=0; i<arrLength; i++) {
				JSONObject jobj = contentArray.get(i).isObject();
				panelList.get(i).setData(jobj);
			}
		}
		
	}

}

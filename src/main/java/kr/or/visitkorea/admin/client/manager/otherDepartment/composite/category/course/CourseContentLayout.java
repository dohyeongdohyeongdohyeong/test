package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.CheckBoxType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentSection;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
 
public class CourseContentLayout extends ContentLayoutPanel {

	private MaterialCheckBox selectedCheck;
	private MaterialPanel mainContent;
	private List<CourseItem> items;
	private MaterialInput mainTitle;
	private MaterialInput subTitle;
	private CourseItem selectedItem;
	
	@Override
	protected void init() {
		
		items = new ArrayList<CourseItem>();
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

		MaterialLabel topLine = new MaterialLabel();
		topLine.setHeight("2px");
		topLine.setBackgroundColor(Color.GREY_LIGHTEN_2);
		topLine.setLayoutPosition(Position.ABSOLUTE);
		topLine.setTop(105);
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
		addLink.setTop(78);
		addLink.setLeft(10);
		addLink.addClickHandler(event->{
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("OTD_ID", this.extentsWindow.getValueMap().get("OTD_ID"));
			map.put("AREA_COMPONENT", this);
			map.put("SECTION", ContentSection.COURSE);
			this.extentsWindow.openDialog(OtherDepartmentMainApplication.COURSE_CONTENT, map, 640);
		});
		this.add(addLink);
		
		MaterialLink removeLink = new MaterialLink();
		removeLink.setIconType(IconType.REMOVE);
		removeLink.setLayoutPosition(Position.ABSOLUTE);
		removeLink.setBottom(2);
		removeLink.setLeft(10);
		removeLink.addClickHandler(event->{
			items.remove(selectedItem);
			renderItem();
		});
		this.add(removeLink);
		
		MaterialPanel guidPanel = new MaterialPanel();
		guidPanel.setLayoutPosition(Position.ABSOLUTE);
		guidPanel.setTop(107);
		guidPanel.setLeft(10);
		guidPanel.setRight(10);
		guidPanel.setBottom(32);
		guidPanel.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		guidPanel.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		this.add(guidPanel);
		
		mainContent =new MaterialPanel();
		mainContent.setLayoutPosition(Position.ABSOLUTE);
		mainContent.setTop(0);
		mainContent.setLeft(0);
		mainContent.setHeight("100%");
		guidPanel.add(mainContent);
//		buildCourse(); 
//		renderItem();
	}
	
	private void renderItem() {
		
		mainContent.clear();
		int widthUnit = 250;
		for (CourseItem item01 : items) {
			
			item01.setLayoutPosition(Position.RELATIVE);
			item01.setMarginRight(10);
			item01.setFloat(Style.Float.LEFT);
			item01.setWidth("250px");
			item01.setHeight("167px");
			item01.addClickHandler(event->{
				for (CourseItem ci : items) {
					if (ci.isSelected()) {
						ci.setSelected(false);
					}
				}
				
				if (selectedItem != null) {
					selectedItem.setTextColor(Color.WHITE);
				}
				selectedItem = item01;
				
				item01.setSelected(true);
				item01.setBackgroundColor(Color.GREY_LIGHTEN_1);
			});
			
			mainContent.add(item01);
			widthUnit += widthUnit;
		}
		
		mainContent.setWidth(widthUnit + "px");
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
			
			items = new ArrayList<CourseItem>();
			int arrLength = contentArray.size();
			
			for (int i=0; i<arrLength; i++) {
				
				JSONObject jobj = contentArray.get(i).isObject();
				CourseItem courseItem = new CourseItem(jobj);
				courseItem.addMouseOverHandler(event->{
					if (selectedItem != courseItem) {
						courseItem.setTextColor(Color.RED);
					}
				});
				courseItem.addMouseOutHandler(event->{
					if (selectedItem != courseItem) {
						courseItem.setTextColor(Color.WHITE);
					}
				});
				courseItem.addClickHandler(event->{

					if (selectedItem != null) {
						selectedItem.setTextColor(Color.WHITE);
					}
					selectedItem = courseItem;
					
				});
				
				items.add(courseItem);
			}
			
			mainContent.setWidth((260*(arrLength)) + "px");
			
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
			retJSONObject.put("COMP_TYPE", new JSONString("course"));
			
		}else {
			
			retJSONObject.put("SUB_TITLE", new JSONString(this.subTitle.getValue()));
			retJSONObject.put("TITLE", new JSONString(this.mainTitle.getValue()));
			retJSONObject.put("COMP_TYPE", new JSONString("course"));
		}
		
		JSONArray contentArray = new JSONArray();
		int idx=0;
		
		for (CourseItem item :  this.items) {
			contentArray.set(idx, item.getJSONObject());
			idx++;
			
		}
		
		retJSONObject.put("CONTENTS", contentArray);
		
		return retJSONObject;
	}

	public void addData(JSONObject rsObj) {
		
		Console.log("addData.rsObj.toString() :: " + rsObj.toString());
		
		if (!rsObj.toString().equals("{}")) {
			String cotId = "";
			String crsId = "";
			if (rsObj.containsKey("CRS_ID"))
				crsId = rsObj.get("CRS_ID").isString().stringValue();
			if (rsObj.containsKey("COT_ID"))
				cotId = rsObj.get("COT_ID").isString().stringValue();
			
			String imgId = rsObj.get("IMG_ID").isString().stringValue();
			String title = rsObj.get("TITLE").isString().stringValue();
			String areaName = "";
			String sigunguName = "";
			
			if (rsObj.get("AREA_NAME") != null) areaName = rsObj.get("AREA_NAME").isString().stringValue();
			if (rsObj.get("SIGUGUN_NAME") != null) sigunguName = rsObj.get("SIGUGUN_NAME").isString().stringValue();
			
			CourseItem item = new CourseItem(		
					cotId,
					(String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+imgId, 
					title, 
					areaName + " " + sigunguName, 
					"",
					crsId);
			
			items.add(item);
			renderItem();
			
/*			
		} else {
			this.dialog.alert(					
					"안내", 600, 250,
					new String[] {
						"선택한 컨텐츠가 10개가 넘었습니다.",
						"태그당 컨텐츠의 수는 최대 10개를 넘을 수 없습니다. ", 
						"신규 데이터를 선택해야 하는 경우 기존 데이터를 삭제하여 공간을 확보해야 합니다."
					});
			MaterialToast.fireToast("선택된 사용자 코스는 완성되지 않아 사용할 수 없습니다.", 5000);
 */			
		}
	}
}

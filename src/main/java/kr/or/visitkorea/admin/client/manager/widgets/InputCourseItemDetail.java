package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseApplication;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;

public class InputCourseItemDetail extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.getFrameCss());
    }

	private JSONObject jsonObject;
	private InputCourseItem inputCourseItem;
	private int index;
	private int listSize;

	public InputCourseItemDetail(JSONObject jsonValue, InputCourseItem inputCourseItem) {
		super();
		this.jsonObject = jsonValue;
		this.inputCourseItem = inputCourseItem;
		this.index = this.inputCourseItem.getIndex(jsonValue);
		this.listSize = this.inputCourseItem.getListSize();
		init();
	}
	
	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setPaddingLeft(20);
		this.setPaddingTop(10);
		this.setPaddingBottom(10);
		this.setPaddingRight(20);
		this.setMinHeight("300px");
		this.setBorderBottom("1px solid #efefef");
		
		
		MaterialRow headerRow = new MaterialRow();
		headerRow.setBorderBottom("1px solid #efefef");
		add(headerRow);
		
		MaterialColumn headerColumn = new MaterialColumn();
		headerColumn.setGrid("s6");
		headerColumn.setTextAlign(TextAlign.LEFT);
		headerRow.add(headerColumn);
		
		MaterialColumn controlColumn = new MaterialColumn();
		controlColumn.setGrid("s6");
		controlColumn.setTextAlign(TextAlign.RIGHT);
		headerRow.add(controlColumn);
		
		MaterialLink removeLink = new MaterialLink();
		removeLink.setIconType(IconType.CLEAR);
		removeLink.addClickHandler(event->{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("COURSE_ITEM", inputCourseItem);
			paramMap.put("DETAIL_ITEM", this);
			this.inputCourseItem.getWindow().openDialog(DatabaseApplication.COURSE_ITEM_DELETE, paramMap, 800);
		});	
		headerColumn.add(removeLink);

		if (this.index != 0) {
			MaterialLink upLink = new MaterialLink();
			upLink.setDisplay(Display.INLINE_BLOCK);
			upLink.setIconType(IconType.ARROW_UPWARD);
			upLink.addClickHandler(event->{
				int newIndex = this.index-1;
				this.inputCourseItem.swap(this.index, newIndex);
				this.inputCourseItem.redraw();
			});
			controlColumn.add(upLink);
		}
		
		if (this.index != (this.listSize-1)) {
			MaterialLink downLink = new MaterialLink();
			downLink.setDisplay(Display.INLINE_BLOCK);
			downLink.setIconType(IconType.ARROW_DOWNWARD);
			downLink.addClickHandler(event->{
				int newIndex = this.index+1;
				this.inputCourseItem.swap(this.index, newIndex);
				this.inputCourseItem.redraw();
			});
			controlColumn.add(downLink);
		}
		
		
		MaterialRow row = new MaterialRow();
		add(row);
		
		MaterialColumn imageColumn = new MaterialColumn();
		imageColumn.setGrid("s4");
		
		MaterialColumn conentColumn = new MaterialColumn();
		conentColumn.setGrid("s8");
		
		row.add(imageColumn);
		row.add(conentColumn);
		
		ContentDetailCourseItem cdci = new ContentDetailCourseItem("대표 이미지", DatabaseContentType.INPUT_IMAGE, this, "SUB_DETAIL_IMG");
		imageColumn.add(cdci);
		
		ContentDetailCourseItem titleItem = new ContentDetailCourseItem("제목", DatabaseContentType.INPUT_HTML, this, "SUB_NAME");
		titleItem.setPaddingBottom(20);
		conentColumn.add(titleItem);
		
		ContentDetailCourseItem bodyItem = new ContentDetailCourseItem("요약", DatabaseContentType.INPUT_HTML, this, "SUB_DETAIL_OVERVIEW");
		bodyItem.setPaddingBottom(20);
		conentColumn.add(bodyItem);

		
	}
	
	public JSONValue getValue(String key) {
		return this.jsonObject.get(key);
	}
	
	public void setValue(String key, String value) {
		this.jsonObject.put(key, new JSONString(value));
	}
	
	public String getCotId() {
		return this.jsonObject.get("COT_ID").isString().stringValue();
	}

	public String getSubContentId() {
		return this.jsonObject.get("SUB_CONTENT_ID").isString().stringValue();
	}

}

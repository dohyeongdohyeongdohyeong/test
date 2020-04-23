package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class CurationItem extends MaterialPanel{

	private MaterialLabel header;
	private MaterialLabel title;
	private String headString;
	private String headColor;
	private String titleString;
	private String titleColor;
	private boolean isSelected;
	private String linkUrl;
	private String cotId;
	private JSONObject valueObj;

	public CurationItem(String cotId, String headString, String headColor, String titleString, String titleColor, String linkUrl) {
		super();
		init();
		this.cotId = cotId;
		this.headString = headString;
		this.headColor = headColor;
		this.titleString = titleString;
		this.titleColor = titleColor;
		this.linkUrl = linkUrl;
		setupProperty();
	}

	public CurationItem(JSONObject jobj) {
		super();
		init();
		this.valueObj = jobj;
		setupProperty();
	}

	public JSONObject getValueObj() {
		return valueObj;
	}

	public void setValueObj(JSONObject valueObj) {
		this.valueObj = valueObj;
	}

	private void setupProperty() {
		
		if (this.valueObj == null) {
		
			this.header.setText(this.headString);
			this.header.getElement().getStyle().setColor(this.headColor);
			this.title.setText(this.titleString);
			this.title.getElement().getStyle().setColor(this.titleColor);
		
		}else {
			
			this.header.setText(this.valueObj.get("DISPLAY_HEADER_TITLE").isString().stringValue());
			this.header.getElement().getStyle().setColor("#"+this.valueObj.get("DISPLAY_HEADER_COLOR").isString().stringValue());
			this.title.setText(this.valueObj.get("DISPLAY_TITLE").isString().stringValue());
			this.title.getElement().getStyle().setColor("#000000");
			if (this.valueObj.get("DISPLAY_HEADER_TITLE") != null) this.linkUrl = this.valueObj.get("DISPLAY_HEADER_TITLE").isString().stringValue();
			if (this.valueObj.get("DISPLAY_HEADER_COLOR") != null) this.headColor = "#"+this.valueObj.get("DISPLAY_HEADER_COLOR").isString().stringValue();
			if (this.valueObj.get("LINK_URL") != null) this.linkUrl = this.valueObj.get("LINK_URL").isString().stringValue();
			
		}
	}

	private void init() {
		
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setHeight("30px");
		
		header = new MaterialLabel();
		header.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		header.setOverflow(Overflow.HIDDEN);
		header.setLayoutPosition(Position.ABSOLUTE);
		header.setTop(0);
		header.setLeft(0);
		header.setFontWeight(FontWeight.BOLD);
		header.setLineHeight(30);
		header.setWidth("50px");
		header.setHeight("30px");
		this.add(header);
		
		title = new MaterialLabel();
		title.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		title.setOverflow(Overflow.HIDDEN);
		title.setLayoutPosition(Position.ABSOLUTE);
		title.setTextAlign(TextAlign.LEFT);
		title.setTop(0);
		title.setLineHeight(30);
		title.setLeft(60);
		title.setRight(0);
		title.setHeight("30px");
		this.add(title);
		
		this.addMouseOverHandler(event->{
			if (!this.isSelected) {
				this.setBackgroundColor(Color.GREY_LIGHTEN_2);
			}
		});
		
		this.addMouseOutHandler(event->{
			if (!this.isSelected) {
				this.setBackgroundColor(Color.WHITE);
			}
		});
		
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		
		this.isSelected = selected;
		
		if (selected) {
			this.setBackgroundColor(Color.BLUE_GREY_DARKEN_1);
		}else {
			this.setBackgroundColor(Color.WHITE);
		}
	}

	public JSONObject getJSONObject() {
		
		JSONObject retJSONObject = this.valueObj;
		if (retJSONObject == null) retJSONObject = new JSONObject();
		
		if (this.cotId != null) retJSONObject.put("COT_ID", new JSONString(this.cotId));
		retJSONObject.put("HEAD", new JSONString(header.getValue()));
		retJSONObject.put("HEAD_COLOR", new JSONString(this.headColor));
		retJSONObject.put("TITLE", new JSONString(title.getValue()));
		retJSONObject.put("TITLE_COLOR", new JSONString("#000000"));
		retJSONObject.put("LINK_URL", new JSONString(this.linkUrl));
		
		return retJSONObject;
		
	}

	public void setHeader(String value) {
		this.headString = value;
		this.header.setValue(value);
	}

	public void setHeaderColor(String value) {
		this.headColor = value;
		if (value.startsWith("#")) {
			this.header.getElement().getStyle().setColor(value);
		}else{
			this.header.getElement().getStyle().setColor("#"+value);
		}
	}

	public String getLink() {
		return this.linkUrl;
	}

	public String getHeaderColor() {
		return this.headColor;
	}

	public String getHeader() {
		return header.getValue();
	}

	public String getBoxTitle() {
		return title.getValue();
	}

	public void setLink(String value) {
		this.linkUrl = value;
	}

	public void setBoxTitle(String value) {
		this.title.setValue(value);
	}


}

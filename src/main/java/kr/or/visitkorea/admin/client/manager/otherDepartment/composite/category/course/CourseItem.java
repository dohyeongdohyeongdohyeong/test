package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.course;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Registry;

public class CourseItem extends MaterialPanel{

	private MaterialPanel headerPanel;
	private String url;
	private String title;
	private String area;
	private boolean isSelected;
	private String cotId;
	private String crsId;
	private JSONObject valueObject;
	private MaterialImage courseHeaderImage;
	private MaterialLabel titleLable;
	private MaterialLabel areaLabel;
	private MaterialLabel depLabel;
	
	public CourseItem(String cotId, String url, String title, String area, String dep, String crsId) {
		super();
		this.cotId = cotId;
		this.url = url;
		this.title = title;
		this.area = area;
		this.crsId = crsId;
		
		init();
	}
	
	public CourseItem(JSONObject jsonObject) {
		super();
		this.valueObject = jsonObject;
		init();
		setupValue();
	}

	@Override
	public void setTextColor(Color textColor) {
		super.setTextColor(textColor);
		titleLable.setTextColor(textColor);
		areaLabel.setTextColor(textColor);
	}

	private void setupValue() {
		
		if (this.valueObject.get("COT_ID") != null ) {
			this.cotId = this.valueObject.get("COT_ID").isString().stringValue();
		}
		if (this.valueObject.get("CRS_ID") != null ) {
			this.crsId = this.valueObject.get("CRS_ID").isString().stringValue();
		}
		
		
		if (this.valueObject.get("FIRST_IMAGE") != null ) {
			String urlStr = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=";
			courseHeaderImage.setUrl(urlStr + this.valueObject.get("FIRST_IMAGE").isString().stringValue());
		}
		
		if (this.valueObject.get("TITLE") != null ) {
			title = this.valueObject.get("TITLE").isString().stringValue();
			titleLable.setValue(title);
		}
		
		if (this.valueObject.get("AREA_NAME") != null ) {
			area = this.valueObject.get("AREA_NAME").isString().stringValue();
			areaLabel.setValue(area);
		}
		
	}

	public JSONObject getValueObject() {
		return valueObject;
	}

	public void setValueObject(JSONObject valueObject) {
		this.valueObject = valueObject;
	}

	private void init() {
		
		this.setFloat(Style.Float.LEFT);
		this.setWidth("250px");
		this.setHeight("167px");
		
		headerPanel = new MaterialPanel();
		headerPanel.setLayoutPosition(Position.ABSOLUTE);
		headerPanel.setTop(0);
		headerPanel.setLeft(0);
		headerPanel.setWidth("100%");
		headerPanel.setHeight("100px");
		this.add(headerPanel);
		
		courseHeaderImage = new MaterialImage();
		courseHeaderImage.setLayoutPosition(Position.ABSOLUTE);
		courseHeaderImage.setTop(0);
		courseHeaderImage.setUrl("https://devm.uniess.co.kr/static/resources/images/temp/temp_cs02.jpg");
		courseHeaderImage.setLeft(0);
		courseHeaderImage.setWidth("100%");
		courseHeaderImage.setHeight("100%");
		headerPanel.add(courseHeaderImage);
		
		MaterialPanel screenPanel = new MaterialPanel();
		screenPanel.setLayoutPosition(Position.ABSOLUTE);
		screenPanel.setBackgroundColor(Color.BLACK);
		screenPanel.setTop(0);
		screenPanel.setLeft(0);
		screenPanel.setWidth("100%");
		screenPanel.setHeight("100%");
		screenPanel.setOpacity(0.3);
		headerPanel.add(screenPanel);
		
		titleLable = new MaterialLabel();
		titleLable.setText(this.title);
		titleLable.setTextColor(Color.WHITE);
		titleLable.setLayoutPosition(Position.ABSOLUTE);
		titleLable.setPadding(5);
		titleLable.setFontSize("0.8em");
		titleLable.setTop(20);
		titleLable.setLeft(5);
		titleLable.setRight(5);
		headerPanel.add(titleLable);

		areaLabel = new MaterialLabel();
		areaLabel.setText(this.area);
		areaLabel.setTextColor(Color.WHITE);
		areaLabel.setLayoutPosition(Position.ABSOLUTE);
		areaLabel.setPadding(5);
		areaLabel.setFontSize("0.5em");
		areaLabel.setTop(60);
		areaLabel.setLeft(5);
		areaLabel.setRight(5);
		headerPanel.add(areaLabel);

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
			this.setBackgroundColor(Color.RED);
		}else {
			this.setBackgroundColor(Color.WHITE);
		}
	}

	public JSONObject getJSONObject() {

		JSONObject retJSONObject = new JSONObject();
		if (this.cotId != null)
			retJSONObject.put("COT_ID", new JSONString(this.cotId));
		if (this.crsId != null)
			retJSONObject.put("CRS_ID", new JSONString(this.crsId));
		return retJSONObject;
		
	}
	
}

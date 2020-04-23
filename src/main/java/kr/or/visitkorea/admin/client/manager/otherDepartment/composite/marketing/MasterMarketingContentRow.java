package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ImagewithUploadPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.RowType;

public class MasterMarketingContentRow extends MaterialPanel implements ContentRow{
 
	private String cotId;
	private String imgId;
	private MaterialLabel t1;
	private ImagewithUploadPanel imagePanel;
	private RowType rowType;
	private JSONObject record;
	private String imgExt;
	private MaterialTextBox t11;
	private MaterialLink s1;
	private MaterialLink s2;
	private MaterialLink s3;
	private MaterialLink s4;
	private MaterialPanel contentPanel;
	private Map<String, Object> items;
	private MaterialLink h4;
	private MaterialLink h3;
	private MaterialLink h2;
	private MaterialLink h1;  
	
	public MasterMarketingContentRow() {
		super();
		init();
	}

	public MasterMarketingContentRow(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		items = new HashMap<String, Object>();
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setWidth("290px");
		this.setHeight("50px");
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		
		t1 = new MaterialLabel();
		t1.setLayoutPosition(Position.ABSOLUTE);
		t1.setTextAlign(TextAlign.LEFT);
		t1.setMarginLeft(10);
		t1.setLeft(10);
		t1.setTop(0);
		t1.setFontSize("3.0em");
		t1.setFontWeight(FontWeight.BOLDER);
		t1.setTextColor(Color.BLUE);
		t1.setLineHeight(50);
		t1.setBottom(0);
		this.add(t1);
		
		s1 = new MaterialLink();
		s1.setLayoutPosition(Position.ABSOLUTE);
		s1.setTop(-2.5);
		s1.setRight(110);
		s1.setFloat(Style.Float.RIGHT);
		s1.setTextAlign(TextAlign.LEFT);
		s1.setFontWeight(FontWeight.BOLDER);
		s1.setTextColor(Color.BLUE);
		s1.setIconType(IconType.LOOKS_ONE);
		s1.setIconSize(IconSize.SMALL);
		s1.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(s1);
		
		h1 = new MaterialLink();
		h1.setLayoutPosition(Position.ABSOLUTE);
		h1.setBottom(-2.5);
		h1.setRight(110);
		h1.setFloat(Style.Float.RIGHT);
		h1.setTextAlign(TextAlign.LEFT);
		h1.setFontWeight(FontWeight.BOLDER);
		h1.setTextColor(Color.BLUE);
		h1.setIconType(IconType.OPEN_IN_BROWSER);
		h1.setIconSize(IconSize.SMALL);
		h1.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(h1);

		
		s2 = new MaterialLink();
		s2.setLayoutPosition(Position.ABSOLUTE);
		s2.setTop(-2.5);
		s2.setRight(85);
		s2.setFloat(Style.Float.RIGHT);
		s2.setTextAlign(TextAlign.LEFT);
		s2.setFontWeight(FontWeight.BOLDER);
		s2.setTextColor(Color.BLUE);
		s2.setIconType(IconType.LOOKS_TWO);
		s2.setIconSize(IconSize.SMALL);
		s2.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(s2);
		
		h2 = new MaterialLink();
		h2.setLayoutPosition(Position.ABSOLUTE);
		h2.setBottom(-2.5);
		h2.setRight(85);
		h2.setFloat(Style.Float.RIGHT);
		h2.setTextAlign(TextAlign.LEFT);
		h2.setFontWeight(FontWeight.BOLDER);
		h2.setTextColor(Color.BLUE);
		h2.setIconType(IconType.OPEN_IN_BROWSER);
		h2.setIconSize(IconSize.SMALL);
		h2.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(h2);
		
		s3 = new MaterialLink();
		s3.setLayoutPosition(Position.ABSOLUTE);
		s3.setTop(-2.5);
		s3.setRight(60);
		s3.setFloat(Style.Float.RIGHT);
		s3.setTextAlign(TextAlign.LEFT);
		s3.setFontWeight(FontWeight.BOLDER);
		s3.setTextColor(Color.BLUE);
		s3.setIconType(IconType.LOOKS_3);
		s3.setIconSize(IconSize.SMALL);
		s3.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(s3);
		
		h3 = new MaterialLink();
		h3.setLayoutPosition(Position.ABSOLUTE);
		h3.setBottom(-2.5);
		h3.setRight(60);
		h3.setFloat(Style.Float.RIGHT);
		h3.setTextAlign(TextAlign.LEFT);
		h3.setFontWeight(FontWeight.BOLDER);
		h3.setTextColor(Color.BLUE);
		h3.setIconType(IconType.OPEN_IN_BROWSER);
		h3.setIconSize(IconSize.SMALL);
		h3.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(h3);

		
		s4 = new MaterialLink();
		s4.setLayoutPosition(Position.ABSOLUTE);
		s4.setTop(-2.5);
		s4.setRight(35);
		s4.setFloat(Style.Float.RIGHT);
		s4.setTextAlign(TextAlign.LEFT);
		s4.setFontWeight(FontWeight.BOLDER);
		s4.setTextColor(Color.BLUE);
		s4.setIconType(IconType.LOOKS_4);
		s4.setIconSize(IconSize.SMALL);
		s4.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(s4);
		
		h4 = new MaterialLink();
		h4.setLayoutPosition(Position.ABSOLUTE);
		h4.setBottom(-2.5);
		h4.setRight(35);
		h4.setFloat(Style.Float.RIGHT);
		h4.setTextAlign(TextAlign.LEFT);
		h4.setFontWeight(FontWeight.BOLDER);
		h4.setTextColor(Color.BLUE);
		h4.setIconType(IconType.OPEN_IN_BROWSER);
		h4.setIconSize(IconSize.SMALL);
		h4.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);

		this.add(h4);

		buildDefaultItems();
	
	}

	public void buildDefaultItems() {
		
		Map<String, Object> item1 = new HashMap<String, Object>();
		JSONObject jObj1 = new JSONObject();
		jObj1.put("valueChange", new JSONString("false"));
		jObj1.put("imageId", new JSONString(Registry.getDefaultImageId()));
		item1.put("JSONObject", jObj1);
		item1.put("IMAGE", this.s1);
		item1.put("LINK", this.h1);
		items.put("itemIndex0", item1);
		
		Map<String, Object> item2 = new HashMap<String, Object>();
		JSONObject jObj2 = new JSONObject();
		jObj2.put("valueChange", new JSONString("false"));
		jObj2.put("imageId", new JSONString(Registry.getDefaultImageId()));
		item2.put("JSONObject", jObj2);
		item2.put("IMAGE", this.s2);
		item2.put("LINK", this.h2);
		items.put("itemIndex1", item2);
		
		Map<String, Object> item3 = new HashMap<String, Object>();
		JSONObject jObj3 = new JSONObject();
		jObj3.put("valueChange", new JSONString("false"));
		jObj3.put("imageId", new JSONString(Registry.getDefaultImageId()));
		item3.put("JSONObject", jObj3);
		item3.put("IMAGE", this.s3);
		item3.put("LINK", this.h3);
		items.put("itemIndex2", item3);
		
		Map<String, Object> item4 = new HashMap<String, Object>();
		JSONObject jObj4 = new JSONObject();
		jObj4.put("valueChange", new JSONString("false"));
		jObj4.put("imageId", new JSONString(Registry.getDefaultImageId()));
		item4.put("JSONObject", jObj4);
		item4.put("IMAGE", this.s4);
		item4.put("LINK", this.h4);
		items.put("itemIndex3", item4);
		
	}
	
	public Map<String, Object> getItems(){
		return this.items;
	}
	
	
	public RowType getRowType() {
		return rowType;
	}

	public void setRowType(RowType rowType) {
		this.rowType = rowType;
	}

	public void setCotId(String cotId) {
		this.cotId=cotId;
	}

	// build component base on content
	public void buildComponent() {
	
	}

	// build component base on link content
	public void buildComponent(JSONObject recordObj) {}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public ImagewithUploadPanel getFileUploadPanel() {
		return imagePanel;
	}

	public void setUploadPanel(ImagewithUploadPanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	public String getCotId() {
		return cotId;
	}

	@Override
	public JSONObject getJSONObject() {
		
		JSONObject retObj = new JSONObject();
		JSONArray resultArray = new JSONArray();
		retObj.put("result", resultArray);
		
		List<String> keyList = new ArrayList<String>(this.items.keySet());
		int idex = 0;
		for (String keyString : keyList) {
			Object tmpObj = this.items.get(keyString);
			Map<String, Object> tmpObjMap = (Map<String, Object>)tmpObj;
			JSONObject valueObj = (JSONObject)tmpObjMap.get("JSONObject");
			valueObj.put("contentOrder", new JSONNumber(idex));
			resultArray.set(idex, valueObj);
			idex++;
		}
		
		return retObj;
	}

	@Override
	public String getParentHeight() {
		return "70px";
	}

	@Override
	public int getParentCheckBoxLineHeight() {
		return 86;
	}

	public void setRowTitle(String rowtitle) {
		this.t1.setText(rowtitle);
	}

	@Override
	public void setSelected(boolean selected) {
		if (selected) {
			this.t1.setTextColor(Color.RED);
		}else {
			this.t1.setTextColor(Color.BLUE);
		}
	}

	@Override
	public Object getIdenifyedvalue() {
		return t1.getText();
	}

	@Override
	public boolean isValidate() {
		
		boolean returnChk = 
				this.s1.getTextColor().equals(Color.RED) &&
				this.s2.getTextColor().equals(Color.RED) &&
				this.s3.getTextColor().equals(Color.RED) &&
				this.s4.getTextColor().equals(Color.RED) &&
				this.h1.getTextColor().equals(Color.RED) &&
				this.h2.getTextColor().equals(Color.RED) &&
				this.h3.getTextColor().equals(Color.RED) &&
				this.h4.getTextColor().equals(Color.RED);
		
		
		return returnChk;
	}

	@Override
	public void buildComponent(JSONArray recordObj) {
		
		int size = recordObj.size();
		
		Console.log("buildComponent.recordObj :: " + recordObj);
		Console.log("buildComponent.recordObj.size() :: " + size);
		
		for (int i=0; i<size; i++) {
			
			JSONObject jObj = recordObj.get(i).isObject();
			
			Map<String, Object> itemMap = (Map<String, Object>) items.get("itemIndex"+i);
			
			if (itemMap != null) {
				
				Console.log("buildComponent.itemIndex"+i + " :: " + itemMap);
				
				itemMap.put("JSONObject", jObj);
				
				if (i==0) {
					if (jObj.get("url") != null && jObj.get("url").isString().stringValue().length() > 0){
						h1.setTextColor(Color.RED);
					}else {
						h1.setTextColor(Color.RED);
					}
					
					if (jObj.get("imageId") != null && jObj.get("imageId").isString().stringValue().length() > 0){
						s1.setTextColor(Color.RED);
					}else {
						s1.setTextColor(Color.RED);
					}
				}
				
				if (i==1) {
					if (jObj.get("url") != null && jObj.get("url").isString().stringValue().length() > 0){
						h2.setTextColor(Color.RED);
					}else {
						h2.setTextColor(Color.RED);
					}
					
					if (jObj.get("imageId") != null && jObj.get("imageId").isString().stringValue().length() > 0){
						s2.setTextColor(Color.RED);
					}else {
						s2.setTextColor(Color.RED);
					}
				}
				
				if (i==2) {
					if (jObj.get("url") != null && jObj.get("url").isString().stringValue().length() > 0){
						h3.setTextColor(Color.RED);
					}else {
						h3.setTextColor(Color.RED);
					}
					
					if (jObj.get("imageId") != null && jObj.get("imageId").isString().stringValue().length() > 0){
						s3.setTextColor(Color.RED);
					}else {
						s3.setTextColor(Color.RED);
					}
				}
				
				if (i==3) {
					if (jObj.get("url") != null && jObj.get("url").isString().stringValue().length() > 0){
						h4.setTextColor(Color.RED);
					}else {
						h4.setTextColor(Color.RED);
					}
					
					if (jObj.get("imageId") != null && jObj.get("imageId").isString().stringValue().length() > 0){
						s4.setTextColor(Color.RED);
					}else {
						s4.setTextColor(Color.RED);
					}
				}
				
			}
			
		}
		
	}
	
}

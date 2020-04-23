package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ImagewithUploadPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.UploadSuccessEventFunc;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MarketingDetailPanel extends MaterialPanel {

	private ImagewithUploadPanel imagePanel1;
	private int widthValue;
	private int heightValue;
	private MaterialInput url;
	private String urlString;
	private MaterialPanel coverPanel;
	private Map<String, Object> valueMap;
	private Object linkButton;
	private MaterialInput title;
	private MaterialExtentsWindow exWindow;

	public MarketingDetailPanel(int width, int height, String url) {
		super();
		this.widthValue = width;
		this.heightValue = height;
		this.urlString = url;
		this.setWidth(widthValue + "px");
		this.setHeight(heightValue + "px");
		init();
	}

	public MarketingDetailPanel(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setBorder("1px dashed #aaaaaa");
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		
		imagePanel1 = new ImagewithUploadPanel( widthValue-10, heightValue-80, urlString);
		imagePanel1.setLayoutPosition(Position.ABSOLUTE);
		imagePanel1.setBorderVisible(false);
		imagePanel1.updateImageInformation(true);
		imagePanel1.setLeft(0);
		imagePanel1.setTop(0);
		imagePanel1.setUploadSuccessEvent(new UploadSuccessEventFunc() {
			
			@Override
			public void invoke(SuccessEvent<UploadFile> event) {
				
				JSONObject jsonValue = (JSONObject) valueMap.get("JSONObject");
				if (jsonValue.get("cotId") != null) jsonValue.put("cotId", new JSONString(imagePanel1.getImageId()));
				if (jsonValue.get("imageId") != null) jsonValue.put("imageId", new JSONString(imagePanel1.getImageId()));
				
				MaterialLink linkObj = (MaterialLink) valueMap.get("IMAGE");
				linkObj.setTextColor(Color.RED);
			}
		});
		
		this.add(imagePanel1);
		
		title = new MaterialInput(InputType.TEXT);
		title.setLayoutPosition(Position.ABSOLUTE);
		title.setWidth("195px");
		title.setHeight("40px");
		title.setLeft(10);
		title.setBottom(40);
		title.addKeyUpHandler(event->{
			JSONObject jObj = (JSONObject) valueMap.get("JSONObject");
			jObj.put("title", new JSONString(title.getValue()));
		});
		this.add(title);
		
		title.setTooltip("제목");
	
		url = new MaterialInput(InputType.TEXT);
		url.setLayoutPosition(Position.ABSOLUTE);
		url.setWidth("195px");
		url.setHeight("40px");
		url.setLeft(10);
		url.setBottom(0);
		url.addKeyUpHandler(event->{
			MaterialLink linkObj = (MaterialLink) valueMap.get("LINK");
			JSONObject jObj = (JSONObject) valueMap.get("JSONObject");
			jObj.put("url", new JSONString(url.getValue()));
			if (url.getText().length() > 0) {
				linkObj.setTextColor(Color.RED);
			}else {
				linkObj.setTextColor(Color.BLUE);
			}
		});
		this.add(url);
		
		url.setTooltip("URL");
		
		imagePanel1.setLinkComponent(url);

		coverPanel = new MaterialPanel();
		coverPanel.setBackgroundColor(Color.BLACK);
		coverPanel.setOpacity(0.2);
		coverPanel.setLayoutPosition(Position.ABSOLUTE);
		coverPanel.setTop(0);
		coverPanel.setLeft(0);
		coverPanel.setWidth("100%");
		coverPanel.setHeight("100%");
		this.add(coverPanel);
		
	}
	
	public void updateValue() {
		
		JSONObject jObj = (JSONObject) valueMap.get("JSONObject");
		jObj.put("title", new JSONString(title.getValue()));
		jObj.put("url", new JSONString(url.getValue()));
		jObj.put("imageId", new JSONString(imagePanel1.getImageId()));
		jObj.put("cotId", new JSONString(imagePanel1.getImageId()));
		
		MaterialLink linkObj = (MaterialLink) valueMap.get("LINK");
		if (url.getText().length() > 0) {
			linkObj.setTextColor(Color.RED);
		}else {
			linkObj.setTextColor(Color.BLUE);
		}
		
		MaterialLink imgObj = (MaterialLink) valueMap.get("IMAGE");
		imgObj.setTextColor(Color.RED);

	}
	
	public void setCoverVisible(boolean visible) {
		this.coverPanel.setVisible(visible);
	}

	public void setValue(Map<String, Object> itemMap) { 

		List<String> keyList = new ArrayList(itemMap.keySet());
		
		this.imagePanel1.setImageUrl("");
		this.url.setText("");
		this.title.setText("");

		this.valueMap = itemMap;
		JSONObject jsonValue = (JSONObject) this.valueMap.get("JSONObject");
		if (jsonValue != null) {
			if (jsonValue.get("imageId") != null) {
				String imageId = jsonValue.get("imageId").isString().stringValue();
				this.imagePanel1.setImageId(imageId);
				this.coverPanel.setVisible(false);
			}
			if (jsonValue.get("url") != null) {
				String urlStr = jsonValue.get("url").isString().stringValue();
				this.url.setText(urlStr);
				MaterialLink linkObj = (MaterialLink) valueMap.get("LINK");
				if (this.url.getText().length() > 0) {
					linkObj.setTextColor(Color.RED);
				}else {
					linkObj.setTextColor(Color.BLUE);
				}
			}
			if (jsonValue.get("title") == null) {
				this.title.setText("");
			}else {
				String titleStr = jsonValue.get("title").isString().stringValue();
				this.title.setText(titleStr);
			}
		}
		
	}

	public void setExWindow(MaterialExtentsWindow window) {
		this.exWindow = window; 
		
		imagePanel1.addRemoveButtonEvent(new RemoveContentEvent() {

			@Override
			public void invoke() {
				
				MarketingContentDetail marketingContentDetail = (MarketingContentDetail)Registry.get("MarketingContentDetail");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("CONTENT_INFO", "컨텐츠를 삭제하면 되돌릴 수 없습니다.");
				map.put("PARENT", getPanel());
				map.put("MAN_ID", marketingContentDetail.getManId().toString());
				marketingContentDetail.getWindow().openDialog(OtherDepartmentMainApplication.DELETE_CONTENT_INFO, map, 800);
			}
		});
		
		imagePanel1.setAddButtonEvent(new AddContentEvent() {

			@Override
			public void invoke() {
				
				MarketingContentDetail marketingContentDetail = (MarketingContentDetail)Registry.get("MarketingContentDetail");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PARENT", getPanel());
				map.put("MAN_ID", marketingContentDetail.getManId().toString());
				marketingContentDetail.getWindow().openDialog(OtherDepartmentMainApplication.SELECT_MASTER_CONTENT, map, 800);
			}
		});
	}

	public MarketingDetailPanel getPanel() {
		return this;
	}
	
	public ImagewithUploadPanel getImagePanel1() {
		return imagePanel1;
	}

	public void setImagePanel1(ImagewithUploadPanel imagePanel1) {
		this.imagePanel1 = imagePanel1;
	}

	public MaterialInput getUrl() {
		return url;
	}

	public void setUrl(MaterialInput url) {
		this.url = url;
	}

	public MaterialInput getDisplayitle() {
		return title;
	}

	public void setDisplayTitle(MaterialInput title) {
		this.title = title;
	}
}

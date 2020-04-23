package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;

public class ImageNoButton extends MaterialPanel {

	private MaterialImage imgPreview;
	private MaterialProgress progress;
	private int componentWidth = 210;
	private int componentHeight = 351;
	private MaterialButton btn;
	private String url;
	private MaterialFileUploader uploader;
	private String uploadValue;
	private UploadSuccessEventFunc uploadSuccessEventFunc;
	private String savePath;
	private String imageUUID;
	private String borderStr = "1px dashed #aaaaaa";
	private boolean updateImageInformation;
	private String COT_ID;

	public ImageNoButton(int width, int height, String url) {
		this.componentWidth = width;
		this.componentHeight = height;
		super.setWidth(width+"px");
		super.setHeight(height+"px");
		this.url = url;
		init();
	}

	public ImageNoButton(String... initialClass) {
		super(initialClass);
		init();
	}

	public void setWidth(int width) {
		super.setWidth(width+"px");
		this.componentWidth = width;
	}
	
	public void setHeight(int height) {
		super.setHeight(height+"px");
		this.componentHeight = height;
	}
	
	private void init() {
		
		MaterialRow row = new MaterialRow();
		row.setDisplay(Display.INLINE_BLOCK);
		row.setLayoutPosition(Position.RELATIVE);
		row.setWidth(this.componentWidth + "px");
		row.setHeight(this.componentHeight + "px");
		
		String uniqueId = DOM.createUniqueId();
		
		uploader = new MaterialFileUploader();
		uploader.setHeight(this.componentHeight + "px");
		uploader.setId(IDUtil.uuid());
		uploader.setPadding(0);
		uploader.setAcceptedFiles("image/*"); 
		uploader.setGrid("14");
		uploader.setUrl(this.url);
		uploader.setPreview(false);
		uploader.setMaxFileSize(20);  
		uploader.setWidth(this.componentWidth+"px");
		uploader.setHeight(this.componentHeight+"px");
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);
		
		imgPreview = new MaterialImage();
		imgPreview.setLayoutPosition(Position.ABSOLUTE);
		imgPreview.setTop(0);
		imgPreview.setLeft(0);
		imgPreview.setDisplay(Display.BLOCK);
		imgPreview.setUrl(this.url);
		imgPreview.setWidth("auto");
		imgPreview.setHeight("auto");
		imgPreview.setDisplay(Display.INLINE_BLOCK);
		imgPreview.setMaxHeight((this.componentHeight)+"px");
		imgPreview.setMaxWidth((this.componentWidth)+"px");
		imgPreview.setVerticalAlign(VerticalAlign.MIDDLE);
		imgPreview.setId(uniqueId);
		imgPreview.setLineHeight(this.componentHeight);
		
		MaterialPanel areaPanel = new MaterialPanel();
		areaPanel.setTextAlign(TextAlign.CENTER);
		areaPanel.setLayoutPosition(Position.RELATIVE);
		areaPanel.setLineHeight(this.componentHeight);
		areaPanel.setBackgroundColor(Color.WHITE);
		areaPanel.setBorder(borderStr);
		areaPanel.setWidth(this.componentWidth+"px");
		areaPanel.setHeight(this.componentHeight+"px");
		areaPanel.setPadding(5);
		areaPanel.add(imgPreview);
		uploader.add(areaPanel);
/*
		// add button
		MaterialLink li_01 = new MaterialLink();
		li_01.setIconType(IconType.ADD);
		li_01.setLayoutPosition(Position.ABSOLUTE);
		li_01.setTop(5);
		li_01.setRight(5);
		li_01.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_01.setBorder("1px solid #bbbbbb");
		li_01.setBackgroundColor(Color.WHITE);
		li_01.addClickHandler(event->{
			invokeClickAddButtonHandler();
		});
		areaPanel.add(li_01);

		// Link button
		MaterialLink li_02 = new MaterialLink();
		li_02.setIconType(IconType.CLOUD);
		li_02.setLayoutPosition(Position.ABSOLUTE);
		li_02.setTop(5);
		li_02.setRight(33);
		li_02.setBorder("1px solid #bbbbbb");
		li_02.setBackgroundColor(Color.WHITE);
		li_02.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_02.addClickHandler(event->{
			invokeClickLinkButtonHandler();
		});
		areaPanel.add(li_02);

		// Link button
		MaterialLink li_03 = new MaterialLink();
		li_03.setIconType(IconType.REMOVE);
		li_03.setLayoutPosition(Position.ABSOLUTE);
		li_03.setBottom(5);
		li_03.setLeft(5);
		li_03.setBorder("1px solid #bbbbbb");
		li_03.setBackgroundColor(Color.WHITE);
		li_03.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_03.addClickHandler(event->{
			invokeClickRemoveButtonHandler();
		});
		areaPanel.add(li_03);
*/
		progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setBottom(4);
		progress.setLeft(4);
		progress.setRight(4);
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);
		progress.setVisibility(Visibility.HIDDEN);
		uploader.add(progress);
		
		row.add(uploader);
		
		uploader.addAddedFileHandler(event->{
			progress.setPercent(0);
			progress.setVisibility(Visibility.VISIBLE);
		});
		
		uploader.addTotalUploadProgressHandler(event->{
			progress.setPercent(event.getProgress());
		});
		
		uploader.addSuccessHandler(event->{
			progress.setVisibility(Visibility.HIDDEN);
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			savePath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			
			imageUUID = tempImageId;
			
			setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);

			if (updateImageInformation) {
				
				JSONObject parameterJSON = new JSONObject();
				if (COT_ID == null) {
					parameterJSON.put("cmd", new JSONString("INSERT_IMAGE_NOT_COTID"));
				}else{
					parameterJSON.put("cmd", new JSONString("INSERT_IMAGE"));
					parameterJSON.put("cotId", new JSONString(COT_ID));
				}
				parameterJSON.put("imgId", new JSONString(imageUUID));
				parameterJSON.put("imgPath", new JSONString(savePath));
				parameterJSON.put("imgDesc", new JSONString(""));
				executeBusiness(parameterJSON);

			}
			
			if (this.uploadSuccessEventFunc != null) this.uploadSuccessEventFunc.invoke(event);
			
		});
		
		this.add(row);		
	}
	
	public String getBorderStr() {
		return borderStr;
	}

	public void setBorderStr(String borderStr) {
		this.borderStr = borderStr;
	}

	private void invokeClickRemoveButtonHandler() {
		
	}

	private void invokeClickLinkButtonHandler() {
		
	}

	private void invokeClickAddButtonHandler() {
		
	}

	public UploadSuccessEventFunc getUploadSuccessEventFunc() {
		return uploadSuccessEventFunc;
	}

	public void setUploadSuccessEventFunc(UploadSuccessEventFunc uploadSuccessEventFunc) {
		this.uploadSuccessEventFunc = uploadSuccessEventFunc;
	}

	public String getUploadValue() {
		return uploadValue;
	}

	public void setButtonPostion(boolean pos) {
		if (pos) {
			btn.setTop(10);
		}else {
			btn.setBottom(10);
		}
	}
	
	public JSONString getImageUrl() {
		return new JSONString(this.imgPreview.getUrl());
	}

	public void setImageUrl(String url) {
		this.imgPreview.setUrl(url);	
		this.progress.setPercent(0);
	}

	public void setProgress(int i) {
		this.progress.setPercent(0);
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

	public void setImageId(String imageId) {
		this.imageUUID = imageId;
		imgPreview.setUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+imageId);
	}

	public void updateImageInformation(boolean update) {
		this.updateImageInformation = update;
	}

	public String getImageId() {
		return this.imageUUID;
	}

}

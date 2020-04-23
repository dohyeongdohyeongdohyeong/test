package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardImage;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.UploadSuccessEventFunc;

public class UploadPanel extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.buttonOnPanelCss());
    }

	private MaterialImage imgPreview;
	private MaterialProgress progress;
	private int componentWidth = 210;
	private int componentHeight = 351;
	private MaterialButton btn;
	private String url;
	private MaterialFileUploader uploader;
	private String uploadValue;
	private String savePath;
	private String imageUUID;
	private boolean updateImageInformation;
	private String COT_ID;
	private UploadSuccessEventFunc uploadSuccessEventFunc;

	public UploadPanel() {
		this.url = Registry.get("image.server") + "/img/call";
		init();
	}

	public UploadPanel(int width, int height, String url) {
		this.componentWidth = width;
		this.componentHeight = height;
		super.setWidth(width+"px");
		super.setHeight(height+"px");
		this.url = url;
		init();
	}

	public UploadPanel(String... initialClass) {
		super(initialClass);
		this.url = Registry.get("image.server") + "/img/call";
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
	
	
	public void setCotId(String cotId) {
		this.COT_ID = cotId;
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
		uploader.setAcceptedFiles("image/*"); 
		uploader.setGrid("14");
		uploader.setUrl(this.url);
		uploader.setPreview(false);
		uploader.setMaxFileSize(20);  
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);
		
		MaterialCard card = new MaterialCard();
		MaterialCardImage cardImage = new MaterialCardImage();
		cardImage.setTextAlign(TextAlign.CENTER);
		cardImage.setHeight((this.componentHeight-20)+"px");
		cardImage.setLineHeight((this.componentHeight-20));
		
		imgPreview = new MaterialImage();
		imgPreview.setDisplay(Display.INLINE);
		imgPreview.setWidth("auto");
		imgPreview.setHeight("auto");
		imgPreview.setMaxHeight((this.componentHeight-50)+"px");
		imgPreview.setMaxWidth((this.componentWidth-50)+"px");
		imgPreview.setVerticalAlign(VerticalAlign.MIDDLE);
		cardImage.add(imgPreview);
		
		progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setBottom(0);
		progress.setLeft(0);
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);
		
		btn = new MaterialButton();
		btn.setId(uniqueId);
		btn.setLayoutPosition(Position.ABSOLUTE);
		btn.setRight(10);
		btn.setType(ButtonType.FLOATING);
		btn.setBackgroundColor(Color.PINK);
		btn.setSize(ButtonSize.LARGE);
		btn.setIconType(IconType.CLOUD_UPLOAD);
		btn.setIconColor(Color.WHITE);
		btn.setShadow(2);
		
		card.add(cardImage);
		card.add(progress);
		card.add(btn);
		uploader.add(card);
		row.add(uploader);
		
		uploader.addAddedFileHandler(event->{
			progress.setPercent(0);
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
			

			if (this.updateImageInformation) {
				
				JSONObject parameterJSON = new JSONObject();
				
				if (this.COT_ID == null) {
					parameterJSON.put("cmd", new JSONString("INSERT_IMAGE_NOT_COTID"));
				}else{
					parameterJSON.put("cmd", new JSONString("INSERT_IMAGE"));
					parameterJSON.put("cotId", new JSONString(this.COT_ID));
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
	
	public String getUploadValue() {
		return uploadValue;
	}

	public void setUploadValue(String uploadValue) {
		this.uploadValue = uploadValue;
	}

	public MaterialFileUploader getUploader() {
		return this.uploader;
	}

	public void setButtonPostion(boolean pos) {
		if (pos) {
			btn.setTop(10);
		}else {
			btn.setBottom(10);
		}
	}

	public void setImageId(String imageId) {
		this.imageUUID = imageId;
		imgPreview.setUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+imageId);
	}

	public String getImageId() {
		return this.imageUUID;
	}
	
	public JSONString getImageUrl() {
		return new JSONString(this.imgPreview.getUrl());
	}

	public void setImageUrl(String url) {
		this.imgPreview.setUrl(url);	
		this.progress.setPercent(0);
	}
	
	public String getImagePath() {
		return this.savePath;	
	}
	public void setImagePath(String ImagePath) {
		this.savePath = ImagePath;	
	}
	
	public MaterialImage getImage() {
		return imgPreview;
	}

	public void setProgress(int i) {
		this.progress.setPercent(0);
	}

	public void updateImageInformation(boolean update) {
		this.updateImageInformation = update;
	}

	public UploadSuccessEventFunc getUploadSuccessEventFunc() {
		return uploadSuccessEventFunc;
	}

	public void setUploadSuccessEvent(UploadSuccessEventFunc uploadSuccessEventFunc) {
		this.uploadSuccessEventFunc = uploadSuccessEventFunc;
	}

	private void executeBusiness(JSONObject jObj) {
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imageUUID);
			}
		});
	}

	public MaterialButton getBtn() {
		return btn;
	}

}

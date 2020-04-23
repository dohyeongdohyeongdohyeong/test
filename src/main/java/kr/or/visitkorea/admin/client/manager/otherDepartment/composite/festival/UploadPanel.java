package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
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
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;

public class UploadPanel extends MaterialPanel {

	
	private MaterialFileUploader uploader;
	private MaterialImage imgPreview;
	private MaterialProgress progress;
	private MaterialButton btn;
	private MaterialCardImage cardImage;
	private MaterialRow row;
	private MaterialImage source;
	private String previewUrl;
	private String uuid;
	private String uuidType;
	private Map<String, Object> parameters;

	public UploadPanel(String title) {
		super();
		init(title);
	}

	private void init(String title) {

		// dialog title define
		MaterialLabel panelTitle = new MaterialLabel(title);
		panelTitle.setFontSize("1.2em");
		panelTitle.setFontWeight(FontWeight.BOLD);
		panelTitle.setTextColor(Color.BLUE);
		panelTitle.setPaddingLeft(10);
		panelTitle.setPaddingBottom(10);
		this.add(panelTitle);

		row = new MaterialRow();
		row.setDisplay(Display.INLINE_BLOCK);
		row.setLayoutPosition(Position.RELATIVE);
		row.setWidth("100%");
		row.setHeight("100%");
		row.setTextAlign(TextAlign.CENTER);
		
		String uniqueId = DOM.createUniqueId();
		
		uploader = new MaterialFileUploader();
		uploader.setId(IDUtil.uuid());
		uploader.setAcceptedFiles("image/*"); 
		uploader.setGrid("14");
		uploader.setUrl(Registry.get("image.server") + "/img/call");
		uploader.setPreview(false);
		uploader.setMaxFileSize(50);  
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);
		uploader.setBackgroundColor(Color.TRANSPARENT);
		
		MaterialCard card = new MaterialCard();
		cardImage = new MaterialCardImage();
		cardImage.setTextAlign(TextAlign.CENTER);
		
		imgPreview = new MaterialImage();
		imgPreview.setDisplay(Display.INLINE_BLOCK);
		imgPreview.setWidth("auto");
		imgPreview.setHeight("auto");
		imgPreview.setMaxHeight("385px");
		imgPreview.setMaxWidth("436.51px");
		imgPreview.setUrl(GWT.getHostPageBaseURL() + "images/default-image.png");
		imgPreview.setVerticalAlign(VerticalAlign.MIDDLE);
		cardImage.add(imgPreview);
		cardImage.setLineHeight(385);
		cardImage.setBackgroundColor(Color.TRANSPARENT);
		
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
		btn.setBottom(10);
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
			
			String uploadFileName = event.getTarget().getName();
			
			Console.log(uploadFileName.substring(0, uploadFileName.indexOf(".")));
			Console.log("type :: " + event.getTarget().getType());
			
            String body = event.getResponse().getBody();
            JSONObject object = JSONParser.parseStrict(body).isObject()
                    .get("body").isObject()
                    .get("result").isArray().get(0).isObject();

            Console.log(object.toString());
            
            String saveName = object.get("saveName").isString().stringValue();
            uuid = object.get("uuid").isString().stringValue();
            uuidType = object.get("contentType").isString().stringValue();
            previewUrl = Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + saveName;
            imgPreview.setUrl(previewUrl);
            
            if (this.parameters.containsKey("CONTENT_DETAIL")) {
            	if (this.parameters.get("CONTENT_DETAIL") instanceof SeasonContentDetailPanel) {
		            SeasonContentDetailPanel seasonContentDetailPanel =  (SeasonContentDetailPanel)this.parameters.get("CONTENT_DETAIL");
		            seasonContentDetailPanel.setSeasonImageUrl(previewUrl);
            	}
            }
            
            if (this.parameters.containsKey("RESULT") && 
            		this.parameters.containsKey("INDEX")) {
            	
            	JSONObject result = (JSONObject) this.parameters.get("RESULT");
            	int index = (int)this.parameters.get("INDEX");
            	
    			JSONObject targetObject = result.get("seasonTagsArr").isArray().get(index).isObject();
    			targetObject.put("TMP_IMG_URL", new JSONString(previewUrl));
    			targetObject.put("IMG_ID", new JSONString(Registry.getExtractID(previewUrl)));

            }
             
		});
		
		this.add(row);			
	}
	
	public void setUrl(String url) {
		this.uploader.setUrl(url);
	}

	public void setImageWidth(int width) {
		this.imgPreview.setMaxWidth(width + "px");
		this.cardImage.setWidth(width + "px");
	}
	
	public void setImageHeight(int height) {
		this.imgPreview.setMaxHeight(height + "px");
		this.cardImage.setLineHeight(height);
		this.uploader.setHeight(height + "px");
		this.row.setHeight(height + "px");
	}

	public void setSource(MaterialImage materialImage) {
		this.source = materialImage;
	}
	
	public void apply() {
		if (this.source != null && previewUrl != null) {
			this.source.setUrl(previewUrl);
		}
	}
	
	public String getUUID() {
		return this.uuid;
	}
	
	public String getSaveName() {
		return this.uuidType;
	}

	public void setParameter(Map<String, Object> parameters) {
		this.parameters = parameters;
		if (this.parameters.get("CONTENT_DETAIL") != null) {
			if (this.parameters.get("CONTENT_DETAIL") instanceof SeasonContentDetailPanel) {
				SeasonContentDetailPanel seasonContentDetailPanel = (SeasonContentDetailPanel)this.parameters.get("CONTENT_DETAIL");
				if (seasonContentDetailPanel.getSeasonImageUrl() != null) {
					imgPreview.setUrl(seasonContentDetailPanel.getSeasonImageUrl());
				}else {
					imgPreview.setUrl(GWT.getHostPageBaseURL() + "images/default-image.png");
				}
			}
		}
	}

	public void setPrevUrl(String url) {
		this.imgPreview.setUrl(url);
	}
}

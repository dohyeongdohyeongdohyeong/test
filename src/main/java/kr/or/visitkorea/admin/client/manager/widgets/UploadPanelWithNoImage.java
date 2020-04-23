package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialRow;

public class UploadPanelWithNoImage extends MaterialPanel {

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

	public UploadPanelWithNoImage() {
		init();
	}

	public UploadPanelWithNoImage(int width, int height, String url) {
		this.componentWidth = width;
		this.componentHeight = height;
		this.url = url;
		init();
	}

	public UploadPanelWithNoImage(String... initialClass) {
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
		
		String uniqueId = Document.get().createUniqueId();
		
		uploader = new MaterialFileUploader();

		String uploaderId = Document.get().createUniqueId();

		uploader.setHeight(this.componentHeight + "px");
		uploader.setId(uploaderId);
		uploader.setGrid("14");
		uploader.setUrl(this.url);
		uploader.setPreview(false);
		uploader.setMaxFileSize(20);  
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);
		
		MaterialCard card = new MaterialCard();

		progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setBottom(-10);
		progress.setLeft(0);
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);
		
		btn = new MaterialButton();
		btn.setId(uniqueId);
		btn.setLayoutPosition(Position.ABSOLUTE);
		btn.setTop(20);
		btn.setRight(10);
		btn.setType(ButtonType.FLOATING);
		btn.setBackgroundColor(Color.PINK);
		btn.setSize(ButtonSize.LARGE);
		btn.setIconType(IconType.CLOUD_UPLOAD);
		btn.setIconColor(Color.WHITE);
		btn.setShadow(2);

		card.add(progress);
		card.add(btn);
		uploader.add(card);
		row.add(uploader);
		
//		uploader.addAddedFileHandler(event->{
//			progress.setPercent(0);
//		});
//		
//		uploader.addTotalUploadProgressHandler(event->{
//			progress.setPercent(event.getProgress());
//		});

		this.add(row);		
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

	public MaterialButton getBtn() {
		return btn;
	}

}

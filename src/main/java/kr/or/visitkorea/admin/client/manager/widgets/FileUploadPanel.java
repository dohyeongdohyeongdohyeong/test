package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.DOM;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialProgress;

public class FileUploadPanel extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.buttonOnPanelCss());
    }

	private int componentWidth = 210;
	private int componentHeight = 351;
	private MaterialButton btn;
	private String url;
	private MaterialFileUploader uploader;
	private String uploadValue;
	private MaterialLabel fileNames;
	private MaterialProgress progress;

	public FileUploadPanel() {
		init();
	}

	public FileUploadPanel(int width, int height, String url) {
		this.componentWidth = width;
		this.componentHeight = height;
/*		
		super.setWidth(width+"px");
		super.setHeight(height+"px");
*/		
		this.url = url;
		init();
	}

	public FileUploadPanel(String... initialClass) {
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
		
		setLayoutPosition(Position.ABSOLUTE);
		
		String uniqueId = DOM.createUniqueId();
		
		uploader = new MaterialFileUploader();

		uploader.setHeight(this.componentHeight + "px");
		uploader.setId(IDUtil.uuid());
		uploader.setGrid("14");
		uploader.setUrl(this.url);
		uploader.setPreview(false);
		uploader.setMaxFileSize(900);
		uploader.setAcceptedFiles("application/*,image/*,text/*,audio/*,video/*");
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);

		progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setTop(31);
		progress.setLeft(205);
		progress.setWidth("370px");
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);

		MaterialCard card = new MaterialCard();

		fileNames = new MaterialLabel();
		fileNames.setLayoutPosition(Position.ABSOLUTE);
		fileNames.setTop(6);
		fileNames.setLeft(210);
		this.add(fileNames);
		
		btn = new MaterialButton();
		btn.setWidth("200px");
		btn.setId(uniqueId);
		btn.setLayoutPosition(Position.ABSOLUTE);
		btn.setType(ButtonType.FLAT);
		btn.setBackgroundColor(Color.BLUE);
		btn.setTextColor(Color.WHITE);
		btn.setShadow(2);
		btn.setText("파일 선택");

		card.add(progress);
		card.add(btn);
		card.add(fileNames);
		uploader.add(card);
		add(uploader);
		
		uploader.addAddedFileHandler(event->{
			btn.setEnabled(false);
			fileNames.setText(event.getTarget().getName());
			progress.setPercent(0);
		});
		
		uploader.addTotalUploadProgressHandler(event->{
			btn.setText("파일선택 (" + Math.round(event.getProgress()) + "%)");
			progress.setPercent(event.getProgress());
		});
		
		uploader.addCompleteHandler(event->{
			btn.setEnabled(true);
			btn.setText("파일선택 (완료)");
			progress.setPercent(0);
		});
		
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
	
	public void setFileName(String Name) {
		fileNames.setText(Name);
	}

}

package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.DOM;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;

public class FileUploadSimplePanel extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.buttonOnPanelCss());
    }

	private int componentWidth = 210;
	private int componentHeight = 351;
	private MaterialButton btn;
	private String url;
	private MaterialFileUploader uploader;
	private String uploadValue;

	public FileUploadSimplePanel() {
		init();
	}

	public FileUploadSimplePanel(int width, int height, String url) {
		this.componentWidth = width;
		this.componentHeight = height;
		this.url = url;
		init();
	}

	public FileUploadSimplePanel(String... initialClass) {
		super(initialClass);
		init();
	}

	public void setWidth(int width) {
		super.setWidth(width+"px");
		this.componentWidth = width;
//		btn.setWidth(width+"px");
	}
	
	public void setHeight(int height) {
		super.setHeight(height+"px");
		this.componentHeight = height;
//		uploader.setHeight(height+"px");
	}
	
	public void setLayoutPosition(Position type) {
		super.setLayoutPosition(type);
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
//		uploader.setBackgroundColor(Color.RzED);

		btn = new MaterialButton();
		btn.setWidth(this.componentWidth + "px");
		btn.setId(uniqueId);
		btn.setLayoutPosition(Position.ABSOLUTE);
		btn.setType(ButtonType.FLAT);
		btn.setBackgroundColor(Color.BLUE);
		btn.setTextColor(Color.WHITE);
		btn.setShadow(2);
		btn.setText("파일 선택");

		uploader.add(btn);
		add(uploader);
		
		uploader.addAddedFileHandler(event->{
			btn.setEnabled(false);
		});
		
		uploader.addTotalUploadProgressHandler(event->{
			btn.setText("파일선택 (" + Math.round(event.getProgress()) + "%)");
		});
		
		uploader.addCompleteHandler(event->{
			btn.setEnabled(true);
			btn.setText("파일선택 (완료)");
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

}

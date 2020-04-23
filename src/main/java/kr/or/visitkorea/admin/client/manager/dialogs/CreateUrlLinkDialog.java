
package kr.or.visitkorea.admin.client.manager.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CreateUrlLinkDialog extends DialogContent {


	private MaterialLabel messageLabel;
	private MaterialPanel bodyScrollPanel;
	private MaterialPanel scrollMain;
	private MaterialTextBox textBox01;
	private MaterialTextBox textBox02;
	private MaterialLabel prev;
	private MaterialTextBox textBox03;

	public CreateUrlLinkDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		
		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("Link URL 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		
		this.add(dialogTitle);

		messageLabel = new MaterialLabel("");
		messageLabel.setLayoutPosition(Position.ABSOLUTE);
		messageLabel.setLeft(30);
		messageLabel.setFontSize("1.2em");
		messageLabel.setTop(getHeight() - 30 - 25);
		this.add(messageLabel);
		this.addDefaultButtons();
		
		MaterialButton saveButton = new MaterialButton("선택");
		saveButton.setLayoutPosition(Position.ABSOLUTE);
		saveButton.setLeft(500);
		saveButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		saveButton.setTop(getHeight() - 30 - 30);
		saveButton.addClickHandler(event->{
			if (validation()) {
				getMaterialExtentsWindow().closeDialog();
			}
		});
		
		this.add(saveButton);

		int bspWidth = (760*3)+50;
		
		scrollMain = new MaterialPanel();
		scrollMain.setOverflow(Overflow.HIDDEN);
		scrollMain.setWidth("720px");
		scrollMain.setHeight("400px");
		
		bodyScrollPanel = new MaterialPanel();
		bodyScrollPanel.setLayoutPosition(Position.RELATIVE);
		bodyScrollPanel.setTop(0);
		bodyScrollPanel.setLeft(0);
		bodyScrollPanel.setWidth(bspWidth+"px");
		bodyScrollPanel.setHeight("400px");
		bodyScrollPanel.add(getTargetSelectionArea());
		
		scrollMain.add(bodyScrollPanel);
	
		this.add(scrollMain);
	}

	private boolean validation() {
		return true;
	}

	public void go(int position) {
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(250);
		bodyScrollPanel.setTransition(cfg);
		bodyScrollPanel.setTransform("translate("+position+"px,0);");
		bodyScrollPanel.setLeft(position);
		
	}

	public void setPrevButton(Transition transition) {
		
		MaterialAnimation animation = new MaterialAnimation();
		animation.setDelay(0);
		animation.setDuration(500);
		animation.setTransition(transition);
		animation.animate(prev);

	}
	
	private MaterialPanel getTargetSelectionArea() {
		
		MaterialPanel dataTable =new MaterialPanel();
		
		dataTable.setMargin(20);
		dataTable.setPadding(0);
		dataTable.setWidth("680px");
		dataTable.setHeight("360px");
		dataTable.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialCollection collection = new MaterialCollection();
		textBox01 = buildTextInputForm(collection, "컨텐츠 이름");
		textBox02 = buildTextInputForm(collection, "이미지 설명");
		textBox03 = buildTextInputForm(collection, "Link URL");
		
//		collection.setHeight("196px");
		collection.setPaddingLeft(250);
		collection.setPaddingRight(35);
		dataTable.add(collection);
		
		MaterialFileUploader fileUploader = new MaterialFileUploader();
		fileUploader.setLayoutPosition(Position.RELATIVE);
		fileUploader.setUrl("/pudas");
		fileUploader.setMaxFiles(100);
		fileUploader.setShadow(0);
		fileUploader.setMaxFileSize(20);
		fileUploader.setLeft(40);
		fileUploader.setTop(-306);
		fileUploader.setWidth("210px");
		fileUploader.setHeight("193px");
		MaterialUploadLabel upLabel = new MaterialUploadLabel();
		upLabel.setTitle("컨텐츠 사진");
		upLabel.setDescription("이미지 사이즈(111x111)");
		
		fileUploader.add(upLabel);
		fileUploader.setEnabled(true);

		dataTable.add(fileUploader);

		return dataTable;
	}

	private MaterialTextBox buildTextInputForm(MaterialCollection collection, String inputTitle) {
		MaterialCollectionItem item = new MaterialCollectionItem();
		MaterialTextBox component = new MaterialTextBox();
		component.setPlaceholder("값을 입력해 주세요.");
		component.setLabel(inputTitle);
		component.setActive(true);
		item.add(component);
		collection.add(item);
		return component;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		messageLabel.setText("");
		go(0);
	}

	@Override
	public int getHeight() {
		return 500;
	}

}

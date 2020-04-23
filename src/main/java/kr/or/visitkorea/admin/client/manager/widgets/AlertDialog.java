package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AlertDialog extends DialogContent {
	private String title = "알림"; 
	private String msg = "알림창 입니다.";
	private MaterialLabel dialogTitle;
	private MaterialLabel mentLabel; 
	public AlertDialog(MaterialExtentsWindow window) {
		super(window);
		onCreate();
	}

	@Override
	public void init() {
	}

	public void setMsg(String msg) {
		setMsg(title, msg);
	}
		
	
	public void setMsg(String title, String msg) {
		
		this.title = title;
		this.msg = msg;
		
		dialogTitle.setValue(this.title);
		mentLabel.setValue(this.msg);
	}
	
	protected void onCreate() {
		
		clear();
		
		if (buttonAreaPanel == null) {
			
			buttonAreaPanel = new MaterialPanel();
			buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
			buttonAreaPanel.setWidth("100%");
			buttonAreaPanel.setPaddingLeft(30);
			buttonAreaPanel.setPaddingRight(30);
			buttonAreaPanel.setLeft(0); 
			buttonAreaPanel.setBottom(30); 
			this.add(buttonAreaPanel); 
			
		}

		okButton = new MaterialButton("닫기");
		okButton.setLayoutPosition(Position.RELATIVE);
		okButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		okButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		
		buttonAreaPanel.add(okButton);

		
		// dialog title define
		dialogTitle = new MaterialLabel(title);
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.RED);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(20);
		
		this.add(dialogTitle);
		 
		// dialog title define
		mentLabel = new MaterialLabel(msg);
		mentLabel.setFontSize("1.1em");
		mentLabel.setLayoutPosition(Position.ABSOLUTE);
		mentLabel.setFontWeight(FontWeight.BOLD);
		mentLabel.setTextColor(Color.BLUE_GREY);
		mentLabel.setPaddingLeft(50);
		mentLabel.setPaddingRight(50);
		mentLabel.setTop(80);
		this.add(mentLabel);
		
		
   }

	@Override
	public int getHeight() {
		return 250;
	}

}

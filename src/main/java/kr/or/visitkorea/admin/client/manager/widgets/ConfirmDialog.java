package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ConfirmDialog extends DialogContent {
	final static int YES = 1;
	final static int NO = 0;
	private String title = "질문"; 
	private String msg = "질문창 입니다.";
	private boolean thirdchk = false;
//	private ConfirmDialog self;
	public ConfirmDialog(MaterialExtentsWindow window) {
		super(window);
//		self = this;
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
	}
	@Override
	protected void onLoad() {
		super.onLoad();
		clear();
		addDefaultButtons();
   
		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel(title);
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.RED);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(20);
		
		this.add(dialogTitle);
		 
		// dialog title define
		MaterialLabel mentLabel = new MaterialLabel(msg);
		mentLabel.setFontSize("1.1em");
		mentLabel.setLayoutPosition(Position.ABSOLUTE);
		mentLabel.setFontWeight(FontWeight.BOLD);
		mentLabel.setTextColor(Color.BLUE_GREY);
		mentLabel.setPaddingLeft(50);
		mentLabel.setPaddingRight(50);
		mentLabel.setTop(80);
		this.add(mentLabel);
	}
	private MaterialPanel buttonAreaPanel;
	protected MaterialButton yesButton;
	protected MaterialButton noButton;
	private ClickHandler handler;
	public int result = YES;
	protected void addDefaultButtons() {
		buttonAreaPanel = new MaterialPanel();
		buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
		buttonAreaPanel.setWidth("100%");
		buttonAreaPanel.setPaddingLeft(20);
		buttonAreaPanel.setPaddingRight(20);
		buttonAreaPanel.setLeft(0); 
		buttonAreaPanel.setBottom(30); 
		
		
		
		noButton = new MaterialButton("아니오");
		noButton.setLayoutPosition(Position.RELATIVE);
		noButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		noButton.setMarginLeft(20);
		noButton.setId("no");
		noButton.addClickHandler(event->{
			if(handler != null) {
				handler.onClick(event);
			}
			if(thirdchk == true)
				close();
			else
				getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(noButton);
		
		yesButton = new MaterialButton("예");
		yesButton.setLayoutPosition(Position.RELATIVE);
		yesButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		yesButton.setId("yes");
		yesButton.addClickHandler(event->{
			if(handler != null) {
				handler.onClick(event);
			}
			if(thirdchk == true)
				close();
			else
				getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(yesButton);
		this.add(buttonAreaPanel); 
	}
	public void addHandler(final ClickHandler handler) {
		this.handler = handler;
	}

	@Override
	public int getHeight() {
		return 250;
	}
	public void setThirdChk(boolean thirdchk) {
		this.thirdchk = thirdchk;
	}
	public void close() {
		
		this.getParent().getElement().getPreviousSiblingElement().getStyle().setVisibility(Visibility.HIDDEN);
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("top");
		cfg.setDuration(300);

		this.getParent().getElement().getStyle().setTop(-500,Unit.PX);
	}

}

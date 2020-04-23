package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.localGov.LocalGovernmentContentDetail;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LocalGovSelectMetorUITypeDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialLabel comments;
	private MaterialImage selectedImg;
	
	public LocalGovSelectMetorUITypeDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void buildContent() {
		
		addDefaultButtons();
		
		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 생성 - 메트로 UI 타입 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
		MaterialImage mi1 = new MaterialImage();
		mi1.setLayoutPosition(Position.ABSOLUTE);
		mi1.setTitle("3");
		mi1.setTop(60);
		mi1.setLeft(60);
		mi1.setWidth("150px");
		mi1.setHeight("160px");
		mi1.setUrl(GWT.getHostPageBaseURL() + "images/localGov/3.gif");
		addEvent(mi1);
		this.add(mi1);
		
		MaterialImage mi2 = new MaterialImage();
		mi2.setTitle("4");
		mi2.setLayoutPosition(Position.ABSOLUTE);
		mi2.setTop(60);
		mi2.setLeft(220);
		mi2.setUrl(GWT.getHostPageBaseURL() + "images/localGov/4.gif");
		mi2.setWidth("150px");
		mi2.setHeight("160px");
		addEvent(mi2);
		this.add(mi2);
		
		MaterialImage mi3 = new MaterialImage();
		mi3.setTitle("5");
		mi3.setLayoutPosition(Position.ABSOLUTE);
		mi3.setTop(60);
		mi3.setLeft(380);
		mi3.setUrl(GWT.getHostPageBaseURL() + "images/localGov/5.gif");
		mi3.setWidth("150px");
		mi3.setHeight("160px");
		addEvent(mi3);
		this.add(mi3);
		
		comments = new MaterialLabel("코멘트");
		comments.setLayoutPosition(Position.ABSOLUTE);
		comments.setTop(240);
		comments.setLeft(60);
		comments.setFontSize("1.2em");
		this.add(comments);
		
		MaterialButton selectButton = new MaterialButton("타입 선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			if (selectedImg != null) {
				
				LocalGovernmentContentDetail targetContentDetail = (LocalGovernmentContentDetail)getParameters().get("TARGET");
				
				int contentCnt = Integer.parseInt(selectedImg.getTitle());
				targetContentDetail.buildMetroUIContentTemplate(contentCnt);
				
				getMaterialExtentsWindow().closeDialog();
			}
		});
		
		this.addButton(selectButton);
		
	}
	
	private void addEvent(MaterialImage mimg ) {
		mimg.getElement().getStyle().setCursor(Cursor.POINTER);
		mimg.addClickHandler(event->{
			selectedImg = mimg;
			comments.setText(mimg.getTitle() + "개 컨텐츠 형태를 입력할 수 잇는 UI를 선택했습니다.");
		});
	}
	
	@Override
	public int getHeight() {
		return 350;
	}

}

package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.MonthlyContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeEventMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeRecommandMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeSightMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainCategorySelectContentLayoutTypeDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialLabel comments;
	private MaterialImage selectedImg;
	
	public MainCategorySelectContentLayoutTypeDialog(MaterialExtentsWindow window) {
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
		dialogTitle = new MaterialLabel("컨텐츠 레이아웃 타입 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
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
				if (getParameters().get("TARGET") instanceof LayoutTypeRecommandMain) {
					LayoutTypeRecommandMain layoutType = (LayoutTypeRecommandMain)getParameters().get("TARGET");
					layoutType.buildContentTemplate(selectedImg.getTitle(), null);
				}else if (getParameters().get("TARGET") instanceof MonthlyContentDetailPanel) {
					MonthlyContentDetailPanel layoutType = (MonthlyContentDetailPanel)getParameters().get("TARGET");
					layoutType.buildContentTemplate(selectedImg.getTitle(), null);
				}else if (getParameters().get("TARGET") instanceof LayoutTypeSightMain) {
					LayoutTypeSightMain layoutType = (LayoutTypeSightMain) getParameters().get("TARGET");
					layoutType.buildContentTemplate(selectedImg.getTitle(), null);
				}else if (getParameters().get("TARGET") instanceof LayoutTypeEventMain) {
					LayoutTypeEventMain layoutType = (LayoutTypeEventMain) getParameters().get("TARGET");
					layoutType.buildContentTemplate(selectedImg.getTitle(), null);
				}
				
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
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);

		buildDispContent();
		
/*
		map.put("TARGET", this);
		map.put("DISP_TYPE", typeIndex);
*/
		

	}

	private void buildDispContent() {
		
		int disptype = (int) getParameters().get("DISP_TYPE");
		
		switch(disptype) {
		case 1 : buildType0() ; break;
		case 2 : buildType1() ; break;
		case 3 : buildType2() ; break;
		case 4 : buildType3() ; break;
		case 5 : buildType4() ; break;
		}
		
		
	}

	private void buildType0() {
		
		MaterialImage mi1 = new MaterialImage();
		mi1.setLayoutPosition(Position.ABSOLUTE);
		mi1.setTitle("3");
		mi1.setTop(80);
		mi1.setLeft(165);
		mi1.setWidth("150px");
		mi1.setHeight("102px");
		mi1.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/3.gif");
		addEvent(mi1);
		this.add(mi1);
		
		MaterialImage mi2 = new MaterialImage();
		mi2.setTitle("4");
		mi2.setLayoutPosition(Position.ABSOLUTE);
		mi2.setTop(80);
		mi2.setLeft(325);
		mi2.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/4.gif");
		mi2.setWidth("150px");
		mi2.setHeight("102px");
		addEvent(mi2);
		this.add(mi2);
		
		MaterialImage mi3 = new MaterialImage();
		mi3.setTitle("5");
		mi3.setLayoutPosition(Position.ABSOLUTE);
		mi3.setTop(80);
		mi3.setLeft(485);
		mi3.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/5.gif");
		mi3.setWidth("150px");
		mi3.setHeight("102px");
		addEvent(mi3);
		this.add(mi3);
		
	}

	private void buildType1() {
		
		MaterialImage mi1 = new MaterialImage();
		mi1.setLayoutPosition(Position.ABSOLUTE);
		mi1.setTitle("course");
		mi1.setTop(80);
		mi1.setLeft(325);
		mi1.setWidth("150px");
		mi1.setHeight("102px");
		mi1.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/course.gif");
		addEvent(mi1);
		this.add(mi1);
		
	}

	private void buildType2() {
		
		MaterialImage mi1 = new MaterialImage();
		mi1.setLayoutPosition(Position.ABSOLUTE);
		mi1.setTitle("11content");
		mi1.setTop(80);
		mi1.setLeft(245);
		mi1.setWidth("150px");
		mi1.setHeight("102px");
		mi1.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/11content.gif");
		addEvent(mi1);
		this.add(mi1);
		
		MaterialImage mi2 = new MaterialImage();
		mi2.setTitle("curation");
		mi2.setLayoutPosition(Position.ABSOLUTE);
		mi2.setTop(80);
		mi2.setLeft(405);
		mi2.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/curation.gif");
		mi2.setWidth("150px");
		mi2.setHeight("102px");
		addEvent(mi2);
		this.add(mi2);
		
	}

	private void buildType3() {
		
	}

	private void buildType4() {
		MaterialImage mi1 = new MaterialImage();
		mi1.setLayoutPosition(Position.ABSOLUTE);
		mi1.setTitle("1");
		mi1.setTop(80);
		mi1.setLeft(85);
		mi1.setWidth("150px");
		mi1.setHeight("102px");
		mi1.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/1.gif");
		addEvent(mi1);
		this.add(mi1);
		
		MaterialImage mi2 = new MaterialImage();
		mi2.setTitle("2");
		mi2.setLayoutPosition(Position.ABSOLUTE);
		mi2.setTop(80);
		mi2.setLeft(245);
		mi2.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/2.gif");
		mi2.setWidth("150px");
		mi2.setHeight("102px");
		addEvent(mi2);
		this.add(mi2);
		
		MaterialImage mi3 = new MaterialImage();
		mi3.setTitle("3");
		mi3.setLayoutPosition(Position.ABSOLUTE);
		mi3.setTop(80);
		mi3.setLeft(405);
		mi3.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/3.gif");
		mi3.setWidth("150px");
		mi3.setHeight("102px");
		addEvent(mi3);
		this.add(mi3);
		
		MaterialImage mi4 = new MaterialImage();
		mi4.setTitle("9");
		mi4.setLayoutPosition(Position.ABSOLUTE);
		mi4.setTop(80);
		mi4.setLeft(565);
		mi4.setUrl(GWT.getHostPageBaseURL() + "images/categoryMain/9.gif");
		mi4.setWidth("150px");
		mi4.setHeight("102px");
		addEvent(mi4);
		this.add(mi4);
	}
	
	@Override
	public int getHeight() {
		return 350;
	}

}

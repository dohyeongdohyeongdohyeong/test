package kr.or.visitkorea.admin.client.manager.main.course;

import java.util.Map;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.dialogs.CreateUrlLinkDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.SelectContentWithImageDialog;
import kr.or.visitkorea.admin.client.manager.main.course.dialogs.SelectAutoTagTypeDialog;
import kr.or.visitkorea.admin.client.manager.main.course.dialogs.SelectYearMonthDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CourseMainApplication extends ApplicationBase{
	
	public CourseMainApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String CREATE_UPDATE_TAG = "CREATE_UPDATE_TAG";
	public static final String SELECT_YEAR_MONTH = "SELECT_YEAR_MONTH";
	public static final String SELECT_CONTENT = "SELECT_CONTENT";
	public static final String SELECT_AUTO_TAG_TYPE = "SELECT_AUTO_TAG_TYPE";
	public static final String CREATE_URL_LINK = "CREATE_URL_LINK";

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("코스 메인 :: 노출 컨텐츠");
		this.window.addDialog(SELECT_CONTENT, new SelectContentWithImageDialog(this.window));
		this.window.addDialog(CREATE_URL_LINK, new CreateUrlLinkDialog(this.window));
		this.window.addDialog(SELECT_YEAR_MONTH, new SelectYearMonthDialog(this.window)); 
		this.window.addDialog(SELECT_AUTO_TAG_TYPE, new SelectAutoTagTypeDialog(this.window));
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
	}

	public void start() {
		
		
		start(null);
		
	}
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		MaterialIcon iconSelectHeader1 = new MaterialIcon(IconType.COLLECTIONS);
		iconSelectHeader1.setCircle(true);
		iconSelectHeader1.setWaves(WavesType.DEFAULT);
		iconSelectHeader1.addClickHandler(event->{
			window.setTitle("코스 메인 :: 노출 컨텐츠");
			window.goContentSlider(0);
		});
		
		MaterialIcon iconSelectHeader2 = new MaterialIcon(IconType.CODE);
		iconSelectHeader2.setCircle(true);
		iconSelectHeader2.setWaves(WavesType.DEFAULT);
		iconSelectHeader2.addClickHandler(event->{
			window.setTitle("코스 메인 :: 태그");
			window.goContentSlider(window.getWidth() * 1 * -1);
		});
		
		this.window.appendTitleWidget(iconSelectHeader2);
		this.window.appendTitleWidget(iconSelectHeader1);

		this.window.add(new CourseMainContents(this.window));
		this.window.add(new CourseMainTags(this.window));
		this.window.open(this.window);
	}
}

package kr.or.visitkorea.admin.client.manager.main.sights;

import java.util.Map;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.dialogs.SelectContentWithImageDialog;
import kr.or.visitkorea.admin.client.manager.main.sights.dialogs.SelectAutoTagTypeDialog;
import kr.or.visitkorea.admin.client.manager.main.sights.dialogs.SelectSightTypeDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SightMainApplication extends ApplicationBase{
	
	public SightMainApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String SELECT_CONTENT = "SELECT_CONTENT";
	public static final String SELECT_AUTO_TAG_TYPE = "SELECT_AUTO_TAG_TYPE";
	public static final String SELECT_SIGHT_TYPE = "SELECT_SIGHT_TYPE";
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("명소 메인 :: 노출 컨텐츠");
		this.window.addDialog(SELECT_CONTENT, new SelectContentWithImageDialog(this.window));
		this.window.addDialog(SELECT_AUTO_TAG_TYPE, new SelectAutoTagTypeDialog(this.window));
		this.window.addDialog(SELECT_SIGHT_TYPE, new SelectSightTypeDialog(this.window));
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
			window.setTitle("명소 메인 :: 노출 컨텐츠");
			window.goContentSlider(0);
		});
		
		MaterialIcon iconSelectHeader2 = new MaterialIcon(IconType.CODE);
		iconSelectHeader2.setCircle(true);
		iconSelectHeader2.setWaves(WavesType.DEFAULT);
		iconSelectHeader2.addClickHandler(event->{
			window.setTitle("명소 메인 :: 태그");
			window.goContentSlider(window.getWidth() * 1 * -1);
		});
		
		this.window.appendTitleWidget(iconSelectHeader2);
		this.window.appendTitleWidget(iconSelectHeader1);

		this.window.add(new SightMainContents(this.window));
		this.window.add(new SightMainTags(this.window));
		this.window.open(this.window);
	}
}

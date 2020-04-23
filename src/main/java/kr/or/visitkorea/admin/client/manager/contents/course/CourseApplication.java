package kr.or.visitkorea.admin.client.manager.contents.course;

import java.util.Map;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.dialogs.EditorDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CourseApplication extends ApplicationBase{
	
	public CourseApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String RICH_EDITOR = "RICH_EDITOR";
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("코스 컨텐츠 :: 목록");
		this.window.addDialog(RICH_EDITOR, new EditorDialog(this.window));
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
			window.setTitle("코스 컨텐츠 :: 목록");
			window.goContentSlider(0);
		});
		
		MaterialIcon iconSelectHeader2 = new MaterialIcon(IconType.CODE);
		iconSelectHeader2.setCircle(true);
		iconSelectHeader2.setWaves(WavesType.DEFAULT);
		iconSelectHeader2.addClickHandler(event->{
			window.setTitle("코스 컨텐츠 :: 분야별 상세 정보");
			window.goContentSlider(window.getWidth() * 1 * -1);
		});
		
		this.window.appendTitleWidget(iconSelectHeader2);
		this.window.appendTitleWidget(iconSelectHeader1);

		this.window.add(new CourseContentsList(this.window));
		this.window.add(new CourseContentsTree(this.window));
		this.window.open(this.window);
	}

}

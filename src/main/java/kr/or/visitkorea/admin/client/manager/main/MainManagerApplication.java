package kr.or.visitkorea.admin.client.manager.main;

import java.util.Map;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicatonInformation;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.dialogs.CreateUrlLinkDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.SelectContentWithImageDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

@ApplicatonInformation(key="WINDOW_KEY_MOBILE_MAIN")
public class MainManagerApplication extends ApplicationBase{
	
	public MainManagerApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String SELECT_CONTENT = "SELECT_CONTENT";
	public static final String CREATE_URL_LINK = "CREATE_URL_LINK";
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String dviName) {
		this.setDivisionName(dviName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.actionTarget(dviName);
		this.window.addDialog(SELECT_CONTENT, new SelectContentWithImageDialog(this.window));
		this.window.addDialog(CREATE_URL_LINK, new CreateUrlLinkDialog(this.window));
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
		iconSelectHeader1.setTooltip("쇼케이스");
		iconSelectHeader1.setWaves(WavesType.DEFAULT);
		iconSelectHeader1.addClickHandler(event->{
			window.setTitle(divisionName + " :: 쇼케이스");
			window.goContentSlider(0);
		});
		
		MaterialIcon iconSelectHeader2 = new MaterialIcon(IconType.RECENT_ACTORS);
		iconSelectHeader2.setCircle(true);
		iconSelectHeader2.setTooltip("큐레이션");
		iconSelectHeader2.setWaves(WavesType.DEFAULT);
		iconSelectHeader2.addClickHandler(event->{
			window.setTitle("Mobile :: " + "큐레이션");
			window.goContentSlider(window.getWidth() * 1 * -1);
		});

		
		MaterialIcon iconSelectHeader3 = new MaterialIcon(IconType.CODE);
		iconSelectHeader3.setCircle(true);
		iconSelectHeader3.setTooltip("태그");
		iconSelectHeader3.setWaves(WavesType.DEFAULT);
		iconSelectHeader3.addClickHandler(event->{
			window.setTitle(divisionName + " :: " + "태그");
			window.goContentSlider(window.getWidth() * 2 * -1);
		});

		
		MaterialIcon iconSelectHeader4 = new MaterialIcon(IconType.CARD_GIFTCARD);
		iconSelectHeader4.setCircle(true);
		iconSelectHeader4.setTooltip("홍보 마케팅");
		iconSelectHeader4.setWaves(WavesType.DEFAULT);
		iconSelectHeader4.addClickHandler(event->{
			window.setTitle(divisionName + " :: " + "홍보 마케팅");
			window.goContentSlider(window.getWidth() * 3 * -1);
		});

		
		MaterialIcon iconSelectHeader5 = new MaterialIcon(IconType.TODAY);
		iconSelectHeader5.setTooltip("여행켈린더");
		iconSelectHeader5.setCircle(true);
		iconSelectHeader5.setWaves(WavesType.DEFAULT);
		iconSelectHeader5.addClickHandler(event1->{
			window.setTitle(divisionName + " :: " + "여행켈린더");
			window.goContentSlider(window.getWidth() * 4 * -1);
		});

		this.window.appendTitleWidget(iconSelectHeader5);
		this.window.appendTitleWidget(iconSelectHeader4);
		this.window.appendTitleWidget(iconSelectHeader3);
		this.window.appendTitleWidget(iconSelectHeader2);
		this.window.appendTitleWidget(iconSelectHeader1);

		this.window.add(new MainManagerShowCase(this.window));
		this.window.add(new MainManagerCuration(this.window));
		this.window.add(new MainManagerTags(this.window));
		this.window.add(new MainManagerMarketing(this.window));
		this.window.add(new MainManagerTravelCalendar(this.window));
		this.window.open(this.window);
	}
}

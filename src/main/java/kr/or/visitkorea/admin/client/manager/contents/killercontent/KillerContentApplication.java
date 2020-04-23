package kr.or.visitkorea.admin.client.manager.contents.killercontent;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.killercontent.dialogs.BannerSettingDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class KillerContentApplication extends ApplicationBase{
	
	public KillerContentApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	public static final String BANNER_SETTING_DIALOG = "BANNER_SETTING_DIALOG";
	
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.addDialog(KillerContentApplication.BANNER_SETTING_DIALOG, new BannerSettingDialog(this.window));
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
		this.window.setTitle("데이터베이스컨텐츠 :: 킬러 컨텐츠 관리");
		this.window.add(new KillerContentMain(this.window,this));
		this.window.open(this.window);
		
		
	}
}

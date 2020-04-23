package kr.or.visitkorea.admin.client.manager.contents.banner;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AdbannerApplication extends ApplicationBase{
	public static final String MODIFY_BANNER_DETIAL = "MODIFY_BANNER_DETIAL";
	public AdbannerApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
		this.window.addDialog(MODIFY_BANNER_DETIAL, new ModifyBannerDetailDialog(this.window));
		
	}
	
	public void start() {

		start(null);
		
	}
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new AdbannerMain(this.window, this));
		this.window.open(this.window);
		
	}
}

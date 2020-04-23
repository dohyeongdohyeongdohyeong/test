package kr.or.visitkorea.admin.client.manager.event.blacklist;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BlacklistApplication extends ApplicationBase {
	
	public static String USER_PERMISSION;
	public static String ADMIN_PERMISSION;
	
	public BlacklistApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.setHeight("800px");
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
		
		BlacklistApplication.USER_PERMISSION = this.window.getValueMap().get("USER_PERMISSION").toString();
		BlacklistApplication.ADMIN_PERMISSION = this.window.getValueMap().get("ADMIN_PERMISSION").toString();
		
	}
	
	public void start() {
		start(null);
	}
	
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new BlacklistMain(this.window));
		this.window.open(this.window);
	}
}

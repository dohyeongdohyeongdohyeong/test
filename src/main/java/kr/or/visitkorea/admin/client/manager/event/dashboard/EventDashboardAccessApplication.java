package kr.or.visitkorea.admin.client.manager.event.dashboard;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.event.dashboard.panel.EventAccessList;
import kr.or.visitkorea.admin.client.manager.event.dashboard.panel.EventUserList;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventDashboardAccessApplication extends ApplicationBase {

	public static String ADMIN_PERMISSION;
	
	public EventDashboardAccessApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.windowLiveFlag = true;
		this.window.addCloseHandler(event -> {
			this.windowLiveFlag = false;
		});
		EventDashboardAccessApplication.ADMIN_PERMISSION = this.window.getValueMap().get("ADMIN_PERMISSION").toString();
	}

	@Override
	public void start() {
		this.start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new EventAccessList(this.window)); 
		this.window.open(this.window);
	}

}

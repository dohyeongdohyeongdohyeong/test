package kr.or.visitkorea.admin.client.manager.monitoring;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.monitoring.panel.GroupAddDialog;
import kr.or.visitkorea.admin.client.manager.monitoring.panel.UserAddDialog;
import kr.or.visitkorea.admin.client.manager.monitoring.panel.UserUpdateDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MonitoringApplication extends ApplicationBase{

	private MonitoringMain host;
	public static final String USER_ADD_DIALOG = "USER_ADD_DIALOG";
	public static final String USER_UPDATE_DIALOG = "USER_UPDATE_DIALOG";
	public static final String GROUP_ADD_DIALOG = "GROUP_ADD_DIALOG";
	
	public MonitoringApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		setDivisionName(divisionName);
	      windowLiveFlag = true;
	        window = materialExtentsWindow;
	        host = new MonitoringMain(this.window,this);
	        this.window.addDialog(USER_ADD_DIALOG,new UserAddDialog(this.window,host));
	        this.window.addDialog(USER_UPDATE_DIALOG,new UserUpdateDialog(this.window,host));
	        this.window.addDialog(GROUP_ADD_DIALOG,new GroupAddDialog(this.window,host));
	        window.addCloseHandler(event-> {
	        	window.closeDialog();
		             windowLiveFlag = false;
		             host.stoptimer();
	               });}

	@Override
	public void start() {
		start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		window.add(host);
		window.open(window);
	}

}

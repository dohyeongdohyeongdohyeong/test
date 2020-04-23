package kr.or.visitkorea.admin.client.manager.contents.department;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.department.dialogs.DepartmentInfoDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DepartmentApplication extends ApplicationBase{
	public static final String MODIFY_DEPARTMENT_INFO = "MODIFY_DEPARTMENT_INFO";
	public DepartmentApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
		this.window.addDialog(MODIFY_DEPARTMENT_INFO, new DepartmentInfoDialog(this.window));
	}
	
	public void start() {

		start(null);
		
	}
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new DepartmentMain(this.window, this));
		this.window.open(this.window);
	}
}

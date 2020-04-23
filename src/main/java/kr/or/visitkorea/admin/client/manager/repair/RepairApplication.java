package kr.or.visitkorea.admin.client.manager.repair;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RepairApplication extends ApplicationBase{
	public static final String REPAIR_REQUEST_ADD = "REPAIR_REQUEST_ADD";
	public static final String REPAIR_RESULT = "REPAIR_RESULT";
	public static final String REPAIR_MAN_ADD = "REPAIR_MAN_ADD";
	public static final String REPAIR_MAN_DEL = "REPAIR_MAN_DEL";
	public RepairApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addDialog(REPAIR_REQUEST_ADD, new RepairRequestAddDialog(this.window));
		this.window.addDialog(REPAIR_RESULT, new RepairResultDialog(this.window));
		this.window.addDialog(REPAIR_MAN_ADD, new RepairManAddDialog(this.window));
		this.window.addDialog(REPAIR_MAN_DEL, new RepairManDelDialog(this.window));
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
		this.window.add(new RepairMain(this.window, this));
		this.window.open(this.window);
	}
}

package kr.or.visitkorea.admin.client.manager.stamp;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class StampApplication extends ApplicationBase {

	public StampApplication(ApplicationView applicationView) {
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
	}

	@Override
	public void start() {
		start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.setParams(params);
		this.window.add(new StampMain(this.window, this));
		this.window.open(this.window);
	}

}

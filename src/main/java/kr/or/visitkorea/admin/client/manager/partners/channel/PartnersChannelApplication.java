package kr.or.visitkorea.admin.client.manager.partners.channel;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersChannelApplication extends ApplicationBase {
	
	public PartnersChannelApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
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
		this.window.add(new PartnersChannelMain(this.window, this));
		this.window.open(this.window);
	}
}

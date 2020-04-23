package kr.or.visitkorea.admin.client.manager.member;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MemberApplication extends ApplicationBase{
	public static final String PARTNERS_DETAIL = "PARTNERS_DETAIL";
	public MemberApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addDialog(PARTNERS_DETAIL, new PartnersDetailDialog(this.window));
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
		this.window.add(new MemberMain(this.window, this));
		this.window.open(this.window);
	}
}

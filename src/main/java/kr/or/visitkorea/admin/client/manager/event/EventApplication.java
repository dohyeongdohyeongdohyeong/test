package kr.or.visitkorea.admin.client.manager.event;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.event.dialogs.SelectRouletteTargetDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventApplication extends ApplicationBase {
	public static String USER_PERMISSION;
	public static String ADMIN_PERMISSION;
	public static final String SELECT_ROULETTE_TARGET = "SELECT_ROULETTE_TARGET";
	public static final String INSERT_EVENT_RESULT_TEMPLATE = "INSERT_EVENT_RESULT_TEMPLATE";
	public static final String INSERT_EVENT_RESULT_DESCRIPTION = "INSERT_EVENT_RESULT_DESCRIPTION";
	public static final String INSERT_EVENT_OXQUIZ_QUESTION = "INSERT_EVENT_OXQUIZ_QUESTION";
	
	public EventApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addDialog(EventApplication.SELECT_ROULETTE_TARGET, new SelectRouletteTargetDialog(this.window));
		this.window.setHeight("800px");
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
		
		EventApplication.USER_PERMISSION = this.window.getValueMap().get("USER_PERMISSION").toString();
		EventApplication.ADMIN_PERMISSION = this.window.getValueMap().get("ADMIN_PERMISSION").toString();
	}
	
	public void start() {
		start(null);
	}
	
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new EventContentsList(this.window));
		this.window.add(new EventContentsTree(this.window));
		this.window.open(this.window);
	}
}

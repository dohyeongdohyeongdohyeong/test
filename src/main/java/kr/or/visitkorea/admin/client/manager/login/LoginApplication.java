package kr.or.visitkorea.admin.client.manager.login;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.login.dialogs.LoginVaildationDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LoginApplication extends ApplicationBase{

	public static final String LOGIN_VALIDATION_DIALOG = "LOGIN_VALIDATION_DIALOG";
	public static final String SELECT_YEAR_MONTH = "SELECT_YEAR_MONTH";
	public static final String SELECT_CONTENT = "SELECT_CONTENT";
	public static final String SELECT_AUTO_TAG_TYPE = "SELECT_AUTO_TAG_TYPE";
	public static final String CREATE_URL_LINK = "CREATE_URL_LINK";
	
	public LoginApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("로그인");
		this.window.addDialog(LOGIN_VALIDATION_DIALOG, new LoginVaildationDialog(this.window));
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
		this.window.add(new LoginPanel(this.window));
		this.window.open(this.window);
	}
}

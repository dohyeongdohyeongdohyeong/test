package kr.or.visitkorea.admin.client.manager.account;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.account.dialogs.AccountNotFoundDialog;
import kr.or.visitkorea.admin.client.manager.account.dialogs.AvailableAccountDialog;
import kr.or.visitkorea.admin.client.manager.account.dialogs.CreateAccountDialog;
import kr.or.visitkorea.admin.client.manager.account.dialogs.DeSelectAllPermissionsForAdmin;
import kr.or.visitkorea.admin.client.manager.account.dialogs.DeSelectAllPermissionsForUser;
import kr.or.visitkorea.admin.client.manager.account.dialogs.DeleteAccountDialog;
import kr.or.visitkorea.admin.client.manager.account.dialogs.ModifyAccountDialog;
import kr.or.visitkorea.admin.client.manager.account.dialogs.SelectAllPermissionsForAdmin;
import kr.or.visitkorea.admin.client.manager.account.dialogs.SelectAllPermissionsForUser;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AccountApplication extends ApplicationBase{
	
	private AccountMain accountMain;
	
	public AccountApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String CREATE_ACCOUNT = "CREATE_ACCOUNT";
	public static final String MODIFY_ACCOUNT = "MODIFY_ACCOUNT";
	public static final String AVAILABLE_ACCOUNT = "AVAILABLE_ACCOUNT";
	public static final String NOT_FOUND_ACCOUNT = "NOT_FOUND_ACCOUNT";
	public static final String DELETE_ACCOUNT = "DELETE_ACCOUNT";
	public static final String DESELECT_ALL_PERMISSION_FOR_ADMIN = "DESELECT_ALL_PERMISSION_FOR_ADMIN";
	public static final String DESELECT_ALL_PERMISSION_FOR_USER = "DESELECT_ALL_PERMISSION_FOR_USER";
	public static final String SELECT_ALL_PERMISSION_FOR_ADMIN = "SELECT_ALL_PERMISSION_FOR_ADMIN";
	public static final String SELECT_ALL_PERMISSION_FOR_USER = "SELECT_ALL_PERMISSION_FOR_USER";
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addDialog(CREATE_ACCOUNT, new CreateAccountDialog(this.window));
		this.window.addDialog(MODIFY_ACCOUNT, new ModifyAccountDialog(this.window));
		this.window.addDialog(AVAILABLE_ACCOUNT, new AvailableAccountDialog(this.window));
		this.window.addDialog(NOT_FOUND_ACCOUNT, new AccountNotFoundDialog(this.window));
		this.window.addDialog(DELETE_ACCOUNT, new DeleteAccountDialog(this.window));
		this.window.addDialog(DESELECT_ALL_PERMISSION_FOR_ADMIN, new DeSelectAllPermissionsForAdmin(this.window));
		this.window.addDialog(DESELECT_ALL_PERMISSION_FOR_USER, new DeSelectAllPermissionsForUser(this.window));
		this.window.addDialog(SELECT_ALL_PERMISSION_FOR_ADMIN, new SelectAllPermissionsForAdmin(this.window));
		this.window.addDialog(SELECT_ALL_PERMISSION_FOR_USER, new SelectAllPermissionsForUser(this.window));
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
	}

	public void start() {
		accountMain = new AccountMain(this.window);
		
		this.window.add(accountMain);
		this.window.open();
	}
	
	@Override
	public void start(Map<String, Object> params) {
		accountMain = new AccountMain(this.window);
		
		this.params = params;
		this.window.add(accountMain);
		this.window.open(this.window);
	}
}

package kr.or.visitkorea.admin.client.manager.contents.author;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.author.dialogs.AuthorContentDialog;
import kr.or.visitkorea.admin.client.manager.contents.author.dialogs.AuthorEditDialog;
import kr.or.visitkorea.admin.client.manager.contents.author.dialogs.AuthorTypeDialog;
import kr.or.visitkorea.admin.client.manager.contents.author.dialogs.AuthorTypeEditDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorApplication extends ApplicationBase {
	public static final String AUTHOR_EDIT_DIALOG = "AUTHOR_EDIT_DIALOG";
	public static final String AUTHOR_TYPE_DIALOG = "AUTHOR_TYPE_DIALOG";
	public static final String AUTHOR_CONTENT_DIALOG = "AUTHOR_CONTENT_DIALOG";
	public static final String AUTHOR_TYPE_EDIT_DIALOG = "AUTHOR_TYPE_EDIT_DIALOG";
	
	public AuthorApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.windowLiveFlag = true;
		this.window.addDialog(AuthorApplication.AUTHOR_EDIT_DIALOG, new AuthorEditDialog(this.window));
		this.window.addDialog(AuthorApplication.AUTHOR_TYPE_DIALOG, new AuthorTypeDialog(this.window));
		this.window.addDialog(AuthorApplication.AUTHOR_CONTENT_DIALOG, new AuthorContentDialog(this.window));
		this.window.addDialog(AuthorApplication.AUTHOR_TYPE_EDIT_DIALOG, new AuthorTypeEditDialog(this.window));
		this.window.addCloseHandler(event -> {
			this.windowLiveFlag = false;
		});
	}

	@Override
	public void start() {
		this.start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		this.window.add(new AuthorMain(this.window));
		this.window.open(this.window);
	}
	
}

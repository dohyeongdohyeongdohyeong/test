package kr.or.visitkorea.admin.client.manager.contents.codecategory;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.codecategory.dialog.CodeEditDialog;
import kr.or.visitkorea.admin.client.manager.contents.codecategory.dialog.CodeRecommDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CodeCategoryApplication extends ApplicationBase {
	public static final String CODE_EDIT_DIALOG = "CODE_EDIT_DIALOG";
	public static final String CODE_RECOMM_DIALOG = "CODE_RECOMM_DIALOG";
	
	public CodeCategoryApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.windowLiveFlag = true;
		this.window.addDialog(CodeCategoryApplication.CODE_EDIT_DIALOG, new CodeEditDialog(this.window));
		this.window.addDialog(CodeCategoryApplication.CODE_RECOMM_DIALOG, new CodeRecommDialog(this.window));
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
		this.window.add(new CodeCategoryMain(this.window));
		this.window.open(this.window);
	}
	
}

package kr.or.visitkorea.admin.client.manager.contents.foodApi;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseApplication;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentsTree;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.ChageStatusDeleteDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.CourseItemDeleteDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.InsertDatabaseContent;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectCourseItemDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectInformOfferDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.UpdateAccommodationDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.MoreResultNotFoundDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.InputSingleValueDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.SelectDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class FoodApiManagementApplication extends ApplicationBase {

	public FoodApiManagementApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		super.window = materialExtentsWindow;
		super.divisionName = divisionName;
		
		super.window.addDialog(DatabaseApplication.MORE_RESULT_IS_NOT_FOUNT, new MoreResultNotFoundDialog(super.window));
		super.window.addDialog(DatabaseApplication.SELECT_DIALOG, new SelectDialog(super.window));
		super.window.addDialog(DatabaseApplication.INPUT_SINGLE_VALUE, new InputSingleValueDialog(super.window));
		super.window.addDialog(DatabaseApplication.STATUS_DELETE, new ChageStatusDeleteDialog(super.window));
		super.window.addDialog(DatabaseApplication.COURSE_ITEM_DELETE, new CourseItemDeleteDialog(super.window));
		super.window.addDialog(DatabaseApplication.SELECT_COURSE_ITEM, new SelectCourseItemDialog(super.window));
		super.window.addDialog(DatabaseApplication.INSERT_DATABASE_CONTENT, new InsertDatabaseContent(super.window));
		super.window.addDialog(DatabaseApplication.SELECT_INFORM_OFFER, new SelectInformOfferDialog(super.window));
		super.window.addDialog(DatabaseApplication.UPDATE_ACCOMMODATION, new UpdateAccommodationDialog(super.window));
		
		super.window.addCloseHandler(handler -> {
			super.windowLiveFlag = false;
		});
	}

	@Override
	public void start() {
		super.window.add(new FoodApiManagementMain(super.window));
		
		Registry.put("FoodApiContentsTree", new DatabaseContentsTree(super.window));
		super.window.add((DatabaseContentsTree) Registry.put("FoodApiContentsTree"));
		
		super.window.open(super.window);
	}

	@Override
	public void start(Map<String, Object> params) {
		start();
	}
}

package kr.or.visitkorea.admin.client.manager.otherDepartment;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.CommentDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.CurationHeaderDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.DeleteConfirmDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.ExternalLinkDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.ImageChangeDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.LocalGovMetroUiEditDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.LocalGovSelectMetorUITypeDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.MainCategorySelectContentEditDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.MainCategorySelectContentLayoutTypeDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.MasterExternalLinkDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.MasterExternalLinkNoImageDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.OnlyUrlDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SaveContentDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SeasonContentDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SeasonImageSetupDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SelectComponentTypeDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SelectComponentTypeWithShowcaseAndTitleDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SelectContentsDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SelectCourseContentsDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SelectMasterContentsDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.UpdateContentsDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.UploadfileDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.ValidationResultDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentMainApplication extends ApplicationBase{
	
	public OtherDepartmentMainApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	
	public static final String SELECT_UI_COMPONENT = "SELECT_UI_COMPONENT";
	public static final String SELECT_UI_COMPONENT_2 = "SELECT_UI_COMPONENT_2";
	public static final String SELECT_CONTENT = "SELECT_CONTENT";
	public static final String SELECT_MASTER_CONTENT = "SELECT_MASTER_CONTENT";
	public static final String UPLOAD_FILE = "UPLOAD_FILE";
	public static final String EXTERNAL_LINK = "EXTERNAL_LINK";
	public static final String MASTER_EXTERNAL_LINK = "MASTER_EXTERNAL_LINK";
	public static final String MASTER_EXTERNAL_LINK_NO_IMAGE = "MASTER_EXTERNAL_LINK_NO_IMAGE";
	public static final String IMAGE_CHANGE = "IMAGE_CHANGE";
	public static final String COMMENTS = "COMMENTS";
	public static final String DELETE_CONTENT_INFO = "DELETE_CONTENT_INFO";
	public static final String ONLY_URL = "ONLY_URL";
	public static final String LOCAL_GOV_METRO_UI = "LOCAL_GOV_METRO_UI";
	public static final String LOCAL_GOV_METRO_UI_EDIT = "LOCAL_GOV_METRO_UI_EDIT";
	public static final String CATEGORY_MAIN_SELECT_CONTENT_LAYOUT = "CATEGORY_MAIN_SELECT_CONTENT_LAYOUT";
	public static final String CATEGORY_MAIN_SELECT_CONTENT_EDIT = "CATEGORY_MAIN_SELECT_CONTENT_EDIT";
	public static final String VALIDATION_RESULT = "VALIDATION_RESULT";
	public static final String CURATION_HEADER = "CURATION_HEADER";
	public static final String SEASON_CONTENT = "SEASON_CONTENT";
	public static final String SEASON_IMAGES = "SEASON_IMAGES";
	public static final String COURSE_CONTENT = "COURSE_CONTENT";
	public static final String SAVE_CONTENTS = "SAVE_CONTENTS";
	public static final String UPDATE_CONTENTS = "UPDATE_CONTENTS";

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		
		this.setDivisionName(divisionName);
		
		this.windowLiveFlag = true;
		
		// setup target window
		this.window = materialExtentsWindow;
		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});

		// setup dialog
		this.window.addDialog(CURATION_HEADER, new CurationHeaderDialog(this.window));
		this.window.addDialog(SELECT_UI_COMPONENT, new SelectComponentTypeDialog(this.window));
		this.window.addDialog(SELECT_UI_COMPONENT_2, new SelectComponentTypeWithShowcaseAndTitleDialog(this.window));
		this.window.addDialog(SELECT_CONTENT, new SelectContentsDialog(this.window));
		this.window.addDialog(SELECT_MASTER_CONTENT, new SelectMasterContentsDialog(this.window));
		this.window.addDialog(UPLOAD_FILE, new UploadfileDialog(this.window));
		this.window.addDialog(EXTERNAL_LINK, new ExternalLinkDialog(this.window));
		this.window.addDialog(MASTER_EXTERNAL_LINK, new MasterExternalLinkDialog(this.window));
		this.window.addDialog(IMAGE_CHANGE, new ImageChangeDialog(this.window));
		this.window.addDialog(COMMENTS, new CommentDialog(this.window));
		this.window.addDialog(DELETE_CONTENT_INFO, new DeleteConfirmDialog(this.window));
		this.window.addDialog(MASTER_EXTERNAL_LINK_NO_IMAGE, new MasterExternalLinkNoImageDialog(this.window));
		this.window.addDialog(ONLY_URL, new OnlyUrlDialog(this.window));
		this.window.addDialog(LOCAL_GOV_METRO_UI, new LocalGovSelectMetorUITypeDialog(this.window));
		this.window.addDialog(LOCAL_GOV_METRO_UI_EDIT, new LocalGovMetroUiEditDialog(this.window));
		this.window.addDialog(CATEGORY_MAIN_SELECT_CONTENT_LAYOUT, new MainCategorySelectContentLayoutTypeDialog(this.window));
		this.window.addDialog(CATEGORY_MAIN_SELECT_CONTENT_EDIT, new MainCategorySelectContentEditDialog(this.window));
		this.window.addDialog(VALIDATION_RESULT, new ValidationResultDialog(this.window));
		this.window.addDialog(SEASON_CONTENT, new SeasonContentDialog(this.window));
		this.window.addDialog(SEASON_IMAGES, new SeasonImageSetupDialog(this.window));
		this.window.addDialog(COURSE_CONTENT, new SelectCourseContentsDialog(this.window));
		this.window.addDialog(SAVE_CONTENTS, new SaveContentDialog(this.window));
		this.window.addDialog(UPDATE_CONTENTS, new UpdateContentsDialog(this.window));
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
		this.window.add(new OtherDepartmentMainEditor(this.window));
		this.window.open(this.window);
	}
}

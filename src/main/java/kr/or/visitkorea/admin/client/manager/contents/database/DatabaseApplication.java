package kr.or.visitkorea.admin.client.manager.contents.database;

import java.util.HashMap;
import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.ChageStatusDeleteDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.CourseItemDeleteDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.DBExcelDownloadDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.InsertDatabaseContent;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectCourseItemDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectInformOfferDialog;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.UpdateAccommodationDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.MoreResultNotFoundDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.InputSingleValueDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.SelectDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DatabaseApplication extends ApplicationBase{
	
	public DatabaseApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String STATUS_DELETE = "STATUS_DELETE";
	public static final String SELECT_DIALOG = "SELECT_DIALOG";
	public static final String INPUT_SINGLE_VALUE = "INPUT_SINGLE_VALUE";
	public static final String SEARCH_CONTENT = "SEARCH_CONTENT";
	public static final String SELECT_DURATION_GOV = "SELECT_DURATION_GOV";
	public static final String MORE_RESULT_IS_NOT_FOUNT = "MORE_RESULT_IS_NOT_FOUNT";
	public static final String COURSE_ITEM_DELETE = "COURSE_ITEM_DELETE";
	public static final String SELECT_COURSE_ITEM = "SELECT_COURSE_ITEM";
	public static final String INSERT_DATABASE_CONTENT = "INSERT_DATABASE_CONTENT";
	public static final String SELECT_INFORM_OFFER = "SELECT_INFORM_OFFER";
	public static final String UPDATE_ACCOMMODATION = "UPDATE_ACCOMMODATION";
	public static final String DB_EXCEL_DOWNLOAD = "DB_EXCEL_DOWNLOAD";
	private static final Map<String, Object> internalMap = new HashMap<String, Object>();
	
	public static void setValue(String key, Object value) {
		internalMap.put(key, value);
	}

	public static Object getValue(String key) {
		return internalMap.get(key);
	}
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		
		this.windowLiveFlag = true;
		
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("데이터베이스 컨텐츠 :: 목록");
		this.window.addDialog(MORE_RESULT_IS_NOT_FOUNT, new MoreResultNotFoundDialog(this.window));
		this.window.addDialog(SELECT_DIALOG, new SelectDialog(this.window));
		this.window.addDialog(INPUT_SINGLE_VALUE, new InputSingleValueDialog(this.window));
		this.window.addDialog(STATUS_DELETE, new ChageStatusDeleteDialog(this.window)); 
		this.window.addDialog(COURSE_ITEM_DELETE, new CourseItemDeleteDialog(this.window)); 
		this.window.addDialog(SELECT_COURSE_ITEM, new SelectCourseItemDialog(this.window)); 
		this.window.addDialog(INSERT_DATABASE_CONTENT, new InsertDatabaseContent(this.window));
		this.window.addDialog(SELECT_INFORM_OFFER, new SelectInformOfferDialog(this.window));
		this.window.addDialog(UPDATE_ACCOMMODATION, new UpdateAccommodationDialog(this.window));
		this.window.addDialog(DB_EXCEL_DOWNLOAD, new DBExcelDownloadDialog(this.window));
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
		
		Registry.put("DatabaseContentsList", new DatabaseContentsList(this.window));
		Registry.put("DatabaseContentsTree", new DatabaseContentsTree(this.window));
		this.window.add((DatabaseContentsList)Registry.put("DatabaseContentsList"));
		this.window.add((DatabaseContentsTree)Registry.put("DatabaseContentsTree"));
		this.window.open(this.window);
		
	}
}

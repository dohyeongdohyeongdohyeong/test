package kr.or.visitkorea.admin.client.manager.upload.excel;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.upload.excel.composite.ExcelImageUploadMain;
import kr.or.visitkorea.admin.client.manager.upload.excel.dialog.InsertDialog;
import kr.or.visitkorea.admin.client.manager.upload.excel.dialog.SearchDialog;
import kr.or.visitkorea.admin.client.manager.upload.excel.dialog.UploadDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * @author Admin
 * 이미지 업로드 Application
 */
public class ExcelImageUploadApplication extends ApplicationBase {

	public static final String UPLOAD_EXCEL_DIALOG = "UPLOAD_EXCEL_DILAOG";
	public static final String INSERT_DB_DIALOG = "INSERT_DB_DIALOG";
	public static final String SEARCH_WITH_CALENDAR_DIALOG = "SEARCH_WITH_CALENDAR_DIALOG";

	private ExcelImageUploadMain main;

	public interface OnSearchListener {
		void onSearch(String startDate, String endDate);
	}

	public ExcelImageUploadApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		setDivisionName(divisionName);
		windowLiveFlag = true;
		window = materialExtentsWindow;
		window.addCloseHandler(event -> {
			windowLiveFlag = false;
		});

		main = new ExcelImageUploadMain(window);

		window.addDialog(UPLOAD_EXCEL_DIALOG, new UploadDialog(window));
		window.addDialog(INSERT_DB_DIALOG, new InsertDialog(window));
		window.addDialog(SEARCH_WITH_CALENDAR_DIALOG, new SearchDialog(window, main));
	}

	@Override
	public void start() {
		start(null);
	}

	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		window.add(main);
		window.open(window);
	}
}
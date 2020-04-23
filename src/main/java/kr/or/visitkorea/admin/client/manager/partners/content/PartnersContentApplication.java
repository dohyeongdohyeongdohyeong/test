package kr.or.visitkorea.admin.client.manager.partners.content;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersContentApplication extends ApplicationBase{
	public static final String PARTNERS_CONTENT_DETAIL = "PARTNERS_CONTENT_DETAIL";
	public static final String PARTNERS_CONTENT_RESULT = "PARTNERS_CONTENT_RESULT";
	public static final String PARTNERS_CONTENT_EXCEL_DIALOG = "PARTNERS_CONTENT_EXCEL_DIALOG";
	public PartnersContentApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		this.window.addDialog(PARTNERS_CONTENT_DETAIL, new PartnersContentDetailDialog(this.window));
		this.window.addDialog(PARTNERS_CONTENT_RESULT, new PartnersContentResultDialog(this.window));
		this.window.addDialog(PARTNERS_CONTENT_EXCEL_DIALOG, new PartnersContentExcelDialog(this.window));
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
		this.window.add(new PartnersContentMain(this.window, this));
		this.window.open(this.window);
	}
}

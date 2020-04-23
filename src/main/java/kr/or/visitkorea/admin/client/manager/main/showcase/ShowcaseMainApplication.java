package kr.or.visitkorea.admin.client.manager.main.showcase;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ShowcaseMainApplication extends ApplicationBase{
	
	public ShowcaseMainApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
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
		/**
		 * insert into SHOWCASE ( |   ???MAN_ID, CONTENT_TITLE, |   ???CONTENT_DESCRIPTION, CONTENT_ORDER, IMG_ID, |   ???DISPAY_START_DATE, |   ???DISPLAY_END_DATE|   ???  , COT_ID |   ??? |   ??)|   ??
		 * values|   ??( ?, ?, |   ???, ?, ?, |   ??str_to_date(?,'%Y-%m-%d'), |  ??str_to_date(?,'%Y-%m-%d')|  ???  , ? |   ??? |   ??), 
		 * parameters ['45a40683-96e9-11e8-8165-020027310001','김천직지 나이트투어 2018 ','컨텐츠 설명',<null>,'ffdbee3b-0fed-4898-ad66-54eb51b452b8','2018-08-01','2018-08-31','33c359f7-da02-47ee-8829-758d166db2fe']
		 */
		this.window.open(this.window);
	}
}

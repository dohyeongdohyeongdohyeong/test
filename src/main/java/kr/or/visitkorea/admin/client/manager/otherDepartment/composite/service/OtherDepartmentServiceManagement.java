package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.service;

import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentServiceManagement extends AbstractContentPanel {

	public OtherDepartmentServiceManagement(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}
	
	private void buildContent() {
		
	/**
	 * 
	 * 1. 	add main title
	 * 2. 	add tab interface
	 * 3. 	add save button
	 * 
	 * additional main content panel
	 * 
	 * 4. 	add "Main Screen" TAB content
	 * 4.1	create ContentSplitPanel
	 * 4.2 	add PreviewContent
	 * 4.3  add DetailMainScreen component for "Main Screen"
	 * 
	 * additional sub content panel
	 * 
	 * 5.	add "Sub Screen #1" TAB content
	 * 5.1	add ContentSplitPanel
	 * 5.2	add PreviewContent
	 * 5.3	add DetailSubScreen component for "Sub Screen"
	 */
		
		this.add(new ContentTitle("서비스 관리"));

		ContentTab mainScreenTab = new ContentTab("주 화면");
		mainScreenTab.add(new MainScreenContent());
		
		ContentTab listScreenTab = new ContentTab("목록 화면");
		listScreenTab.add(new ListScreenContent());

		ContentTabs contentTabs = new ContentTabs(mainScreenTab, listScreenTab);
		contentTabs.setSelection(0);
		
		this.add(contentTabs);
		
	}

	@Override
	public void onLoad() {
		super.onLoad();
		/**
		 * 전달된 파라메터를 통해서 운영할 부서를 결정한다.
		 */
		String OTD_ID = (String)this.getWindowParameters().get("OTD_ID");
		
	}
}

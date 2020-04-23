package kr.or.visitkorea.admin.client.manager;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class ApplicationBase {
	
	protected ApplicationView applicationView;
	protected MaterialExtentsWindow window;
	protected boolean windowLiveFlag = true;
	protected String divisionName;
	protected Map<String,Object> params;
	
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public ApplicationBase(ApplicationView applicationView) {
		this.applicationView = applicationView;
		this.divisionName = "";
	}
 
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDivisionName() {
		return this.divisionName;
	}
	
	abstract public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName);
	abstract public void start();
	abstract public void start(Map<String,Object> params);

	public void show() {
		this.window.open();
		this.applicationView.reorderWindows(this.window);
	}
	public void show(Map<String,Object> params) {
		this.params = params;
		this.window.open();
		this.applicationView.reorderWindows(this.window);
	}
	public ApplicationView getAppView() {
		return this.applicationView;
	}
	public MaterialExtentsWindow getWindow() {
		return this.window;
	}

	public boolean getLiveFlag() {
		return windowLiveFlag;
	}

}

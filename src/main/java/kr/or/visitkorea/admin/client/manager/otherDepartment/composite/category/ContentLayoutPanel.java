package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class ContentLayoutPanel extends MaterialPanel {

	protected MaterialExtentsWindow extentsWindow;
	public JSONObject tgrCompObj;

	public ContentLayoutPanel() {
		super();
		this.setOverflow(Overflow.HIDDEN);
		init();
	}

	public ContentLayoutPanel(String... initialClass) {
		super(initialClass);
		init();
	}

	abstract protected void init();


	public void setWindow(MaterialExtentsWindow materialExtentsWindow) {
		this.extentsWindow = materialExtentsWindow;
	}
	
	public MaterialExtentsWindow getWindow() {
		return this.extentsWindow;
	}
	
	abstract public void setSelected(boolean flag);

	abstract public void loadData();
	
	abstract public JSONObject getJSONObject();

	public void setData(JSONObject tgrCompObj) {
		this.tgrCompObj = tgrCompObj;
	}

}

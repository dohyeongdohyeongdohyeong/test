package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListBody;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaSearch;

public class NoneSearchWidget extends MaterialPanel implements VisitKoreaSearch{

	private VisitKoreaListBody body;

	public NoneSearchWidget() {
		super();
		init();
	}

	public void init() {
		
	}

	public void executeBusiness(JSONObject parameterJSON) {

	}
	
	@Override
	public void setBody(VisitKoreaListBody searchBody) {
		this.body = searchBody;
	}
	
	
}

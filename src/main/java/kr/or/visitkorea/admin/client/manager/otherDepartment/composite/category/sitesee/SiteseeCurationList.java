package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.ui.MaterialPanel;

public class SiteseeCurationList extends MaterialPanel{

	private List<CurationItem> items = new ArrayList<CurationItem>();

	public SiteseeCurationList() {
		super();
		init();
	}

	public SiteseeCurationList(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
	}
	
}

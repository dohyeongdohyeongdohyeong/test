package kr.or.visitkorea.admin.client.manager.widgets;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;

public class ContentBottomPager extends ContentMenu {

	private MaterialIcon lastPageIcon;
	private MaterialIcon nextPageIcon;
	private MaterialIcon prevPageIcon;
	private MaterialIcon firstPageIcon;

	@Override
	public void init() {
		super.init();

		this.lastPageIcon = new MaterialIcon(IconType.LAST_PAGE);

		this.nextPageIcon = new MaterialIcon(IconType.CHEVRON_RIGHT);

		this.prevPageIcon = new MaterialIcon(IconType.CHEVRON_LEFT);

		this.firstPageIcon = new MaterialIcon(IconType.FIRST_PAGE);
		
		defaultLayout();
		
	}

	public void defaultLayout() {
		
		this.addIcon(this.lastPageIcon, "끝 페이지로", com.google.gwt.dom.client.Style.Float.RIGHT);
		this.addIcon(this.nextPageIcon, "다음 페이지로", com.google.gwt.dom.client.Style.Float.RIGHT);
		this.addIcon(this.prevPageIcon, "이전 페이지로", com.google.gwt.dom.client.Style.Float.RIGHT);
		this.addIcon(this.firstPageIcon, "처음 페이지로", com.google.gwt.dom.client.Style.Float.RIGHT);
		
	}

}

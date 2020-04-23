package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsCourseDetail extends AbtractContents {

	public ContentsCourseDetail(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("코스 상세");
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

}
package kr.or.visitkorea.admin.client.manager.contents.course.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsPartners extends AbtractContents {

	public ContentsPartners(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("담당부서 및 문의처");
	}

}
package kr.or.visitkorea.admin.client.manager.contents.course.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsBase extends AbtractContents {

	public ContentsBase(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("기본 정보");
	}

}

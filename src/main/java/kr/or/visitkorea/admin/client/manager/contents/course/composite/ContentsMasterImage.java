package kr.or.visitkorea.admin.client.manager.contents.course.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsMasterImage extends AbtractContents {

	public ContentsMasterImage(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("대표 이미지 설정");
	}

}
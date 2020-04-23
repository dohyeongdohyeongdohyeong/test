package kr.or.visitkorea.admin.client.manager.contents.course.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsTags extends AbtractContents {

	public ContentsTags(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("태그");
	}

}
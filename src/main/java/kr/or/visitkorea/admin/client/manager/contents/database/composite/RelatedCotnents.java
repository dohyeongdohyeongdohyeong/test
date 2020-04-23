package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RelatedCotnents extends AbtractContents {

	public RelatedCotnents(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("연관 컨텐츠");
	}

	public void loadData() {
		
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

}
package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsPartners extends AbtractRecommContents {

	public RecommContentsPartners(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("담당부서 및 문의처");
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

}
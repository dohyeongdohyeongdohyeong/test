package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsInfomation extends AbtractContents {

	public ContentsInfomation(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("링크(연결) 정보");
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

}

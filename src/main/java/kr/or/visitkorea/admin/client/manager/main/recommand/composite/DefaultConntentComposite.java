package kr.or.visitkorea.admin.client.manager.main.recommand.composite;

import com.google.gwt.dom.client.Style.Position;

import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;

public class DefaultConntentComposite extends AbstractContentComposite {

	@Override
	public void init() {
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setWidth("430px");
		this.setHeight("380px");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		buildDefaultTypeContent();
	}
	
	@Override
	public void buildDefaultTypeContent() {
		clear();
		this.buildRow("컨텐츠 이름", "제목을 이력해 주세요.", 0);
		this.buildRow("컨텐츠 설명", "요약을 입력해 주세요.", 0);
		this.buildRow("메인 테그", "메인이 되는 테그를 입력해 주세요.", 0);
	}

	@Override
	public void buildOutLinkTypeContent() {
		clear();
		this.buildRow("컨텐츠 이름", "제목을 이력해 주세요.", 0);
		this.buildRow("컨텐츠 설명", "요약을 입력해 주세요.", 0);
		this.buildRow("링크 URL", "메인이 되는 테그를 입력해 주세요.", 0);
	}


}

package kr.or.visitkorea.admin.client.manager.account.composite;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;

public class AccountIntroComposite extends AbstractContentComposite {

	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setWidth("100%");
		this.setHeight("426px");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		clear();

		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("사용자 관리");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setLayoutPosition(Position.ABSOLUTE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setLeft(300);
		this.add(dialogTitle);

		// dialog title define
		MaterialLabel mentOtherLabel0 = new MaterialLabel("- 사용자를 추가 하거나 권한을 조절할 수 있습니다.");
		mentOtherLabel0.setFontSize("1.1em");
		mentOtherLabel0.setLayoutPosition(Position.ABSOLUTE);
		mentOtherLabel0.setTextColor(Color.BLUE_GREY);
		mentOtherLabel0.setLeft(40);
		mentOtherLabel0.setTop(80);
		this.add(mentOtherLabel0);

		// dialog title define
		MaterialLabel mentOtherLabel1 = new MaterialLabel("- 지정된 권한 이외의 접근은 금지합니다.");
		mentOtherLabel1.setFontSize("1.1em");
		mentOtherLabel1.setLayoutPosition(Position.ABSOLUTE);
		mentOtherLabel1.setTextColor(Color.BLUE_GREY);
		mentOtherLabel1.setLeft(40);
		mentOtherLabel1.setTop(120);
		this.add(mentOtherLabel1);

		// dialog title define
		MaterialLabel mentOtherLabel2 = new MaterialLabel("- 계정관련 문의는 시스템 관리자에게 문의해 주세요.");
		mentOtherLabel2.setFontSize("1.1em");
		mentOtherLabel2.setLayoutPosition(Position.ABSOLUTE);
		mentOtherLabel2.setTextColor(Color.BLUE_GREY);
		mentOtherLabel2.setLeft(40);
		mentOtherLabel2.setTop(160);
		this.add(mentOtherLabel2);
	}

	@Override
	public void buildOutLinkTypeContent() {
		
	}

	@Override
	public void buildDefaultTypeContent() {
		
	}

}

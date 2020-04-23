package kr.or.visitkorea.admin.client.manager.account.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AccountNotFoundDialog extends DialogContent {
 
	public AccountNotFoundDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
	
	}

	@Override
	protected void onLoad() {
	       super.onLoad();
	       clear();
	       addDefaultButtons();
	       
	        // dialog title define
			MaterialLabel dialogTitle = new MaterialLabel("사용자를 먼저 선택해 주세요.");
			dialogTitle.setFontSize("1.4em");
			dialogTitle.setFontWeight(FontWeight.BOLD);
			dialogTitle.setTextColor(Color.RED);
			dialogTitle.setPaddingTop(10);
			dialogTitle.setPaddingLeft(30);
			
			this.add(dialogTitle);
			 
	        // dialog title define
			MaterialLabel mentLabel = new MaterialLabel("특정 계정 관련 작업은 사용자를 우선 선택해야 합니다.");
			mentLabel.setFontSize("1.1em");
			mentLabel.setLayoutPosition(Position.ABSOLUTE);
			mentLabel.setFontWeight(FontWeight.BOLD);
			mentLabel.setTextColor(Color.BLUE_GREY);
			mentLabel.setLeft(100);
			mentLabel.setTop(80);
			this.add(mentLabel);

   }

	@Override
	public int getHeight() {
		return 250;
	}

}

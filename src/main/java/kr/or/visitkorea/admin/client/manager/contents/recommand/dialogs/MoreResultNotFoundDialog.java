package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MoreResultNotFoundDialog extends DialogContent {
 
	public MoreResultNotFoundDialog(MaterialExtentsWindow window) {
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
			MaterialLabel dialogTitle = new MaterialLabel("결과 세트가 존재하지 않습니다.");
			dialogTitle.setFontSize("1.4em");
			dialogTitle.setFontWeight(FontWeight.BOLD);
			dialogTitle.setTextColor(Color.RED);
			dialogTitle.setPaddingTop(10);
			dialogTitle.setPaddingLeft(30);
			
			this.add(dialogTitle);
			 
	        // dialog title define
			MaterialLabel mentLabel = new MaterialLabel("더 이상의 데이터가 없습니다.");
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

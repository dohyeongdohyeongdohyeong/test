
package kr.or.visitkorea.admin.client.manager.tags.dialogs;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DeleteTagDialog extends DialogContent {


	public DeleteTagDialog(MaterialExtentsWindow window) {
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
			MaterialLabel inputBox = new MaterialLabel();
			
			inputBox.setFontSize("1.2em");
			inputBox.setText("정보를 삭제하시겠습니까?");
			inputBox.setLayoutPosition(Position.ABSOLUTE);
			inputBox.setRight(100);
			inputBox.setLeft(100);
			inputBox.setTop(30);
			
			this.add(inputBox);

			
			MaterialButton selectButton = new MaterialButton("삭제");
			selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
			selectButton.addClickHandler(event->{
				getMaterialExtentsWindow().closeDialog();
			});
			this.addButton(selectButton);
			
			inputBox.setFocus(true);
   }
	
	@Override
	public int getHeight() {
		return 200;
	}
}

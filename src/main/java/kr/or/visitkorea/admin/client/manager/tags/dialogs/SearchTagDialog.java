
package kr.or.visitkorea.admin.client.manager.tags.dialogs;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchTagDialog extends DialogContent {


	public SearchTagDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {

	       addDefaultButtons();
			
	        // dialog title define
			MaterialTextBox inputBox = new MaterialTextBox();
			inputBox.setLabel("검색 태그명");
		
			inputBox.addKeyUpHandler(event->{
				if (event.getNativeKeyCode() == 13) {
					getMaterialExtentsWindow().closeDialog();
				}
			});
			
			inputBox.setLayoutPosition(Position.ABSOLUTE);
			inputBox.setRight(100);
			inputBox.setLeft(100);
			inputBox.setTop(30);
			
			this.add(inputBox);

			
			MaterialButton selectButton = new MaterialButton("검색");
			selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
			selectButton.addClickHandler(event->{
				getMaterialExtentsWindow().closeDialog();
			});
			this.addButton(selectButton);
			
			inputBox.setFocus(true);	
	}

	@Override
	protected void onLoad() {
	       super.onLoad();
   }
	
	@Override
	public int getHeight() {
		return 200;
	}
}

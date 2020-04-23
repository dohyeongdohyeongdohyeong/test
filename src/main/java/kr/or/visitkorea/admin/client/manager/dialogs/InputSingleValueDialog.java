package kr.or.visitkorea.admin.client.manager.dialogs;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class InputSingleValueDialog extends DialogContent {

	public InputSingleValueDialog(MaterialExtentsWindow window) {
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
			
	       ContentTreeItem target = (ContentTreeItem)this.getParameters().get("TARGET");
			
	        // dialog title define
			MaterialTextBox inputBox = new MaterialTextBox();
			inputBox.setLabel(target.getText());
			
			inputBox.addKeyUpHandler(event->{
				if (event.getNativeKeyCode() == 13) {
					target.setEditorValue(inputBox.getValue());
					getMaterialExtentsWindow().closeDialog();
				}
			});
			
			if (target.getEditorValue() != null) {
				inputBox.setValue(target.getEditorValue());
			}
			
			inputBox.setLayoutPosition(Position.ABSOLUTE);
			inputBox.setRight(100);
			inputBox.setLeft(100);
			inputBox.setTop(30);
			
			this.add(inputBox);

			
			MaterialButton selectButton = new MaterialButton("선택");
			selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
			selectButton.addClickHandler(event->{
				target.setEditorValue(inputBox.getValue());
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
